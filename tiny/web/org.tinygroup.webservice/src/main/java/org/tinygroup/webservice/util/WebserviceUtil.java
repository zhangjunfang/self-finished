/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.webservice.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

import org.tinygroup.event.Parameter;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.loader.LoaderManager;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

public class WebserviceUtil {
	private static Logger logger = LoggerFactory.getLogger(WebserviceUtil.class);
	/**
	 * 以wsdl形式表示serviceInfo对应的webservice服务，并返回该wsdl字符串
	 * 
	 * @param serviceInfo
	 * @return
	 */
	public static void genWSDL(ServiceInfo serviceInfo) {
		loadServiceClass(serviceInfo);// 加载webservice服务
	}

	/**
	 * 加载通过ServiceInfo对象参数值动态生成的webservice服务
	 * 
	 * @param serviceInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static Class loadServiceClass(ServiceInfo serviceInfo) {
		String serviceInfoId = serviceInfo.getServiceId();
		String className = serviceInfoId.substring(0, 1).toUpperCase()
				+ serviceInfoId.substring(1);
		className = PACKAGE_NAME + "." + className;

		// 判断类是否已经加载
		Class c = classLoaded(className);
		if (c != null) {
			return c;
		}

		// 新建类CtClass对象
		ClassPool pool = ClassPool.getDefault();
		pool.importPackage(PACKAGE_NAME);
		CtClass cc = pool.makeClass(className);
		cc.stopPruning(true);

		// 类添加javax.jws.WebService注解
		ClassFile cf = cc.getClassFile();
		java.util.Map<String, String> params = new HashMap<String, String>();
		params.put("targetNamespace", PACKAGE_NAME);
		addAnnotation(cf, "javax.jws.WebService", params);

		// 类添加方法
		try {
			CtMethod cm = getCtMethod(cc, serviceInfo);
			cc.addMethod(cm);
		} catch (CannotCompileException e1) {
			logger.errorMessage(e1.getMessage(),e1);
		} catch (Exception e1) {
			logger.errorMessage(e1.getMessage(),e1);
		}

		// 写入字节码
		try {
			cc.writeFile();
			cc.defrost();
			c = cc.toClass();
			return c;
		} catch (NotFoundException e) {
			logger.errorMessage("class:{className} 文件未找到",e,className);
		} catch (CannotCompileException e) {
			logger.errorMessage(e.getMessage(),e);
		} catch (IOException e) {
			logger.errorMessage(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * 将serviceInfo对象解析成xml形式的endpoint元素，并添加到sun-jaxws.xml文件中
	 * 
	 * @param serviceInfo
	 */
	public static InputStream getXmlInputStream(ServiceInfo serviceInfo) {
		XmlNode root = new XmlNode("endpoints");
		root.setAttribute("xmlns",
				"http://java.sun.com/xml/ns/jax-ws/ri/runtime");
		root.setAttribute("version", "2.0");
		String serviceId = serviceInfo.getServiceId();
		String urlPattern = "/services/" + serviceId;
		String className = serviceId.substring(0, 1).toUpperCase()
				+ serviceId.substring(1);
		className = PACKAGE_NAME + "." + className;
		List<XmlNode> endpoints = root.getSubNodes(serviceId);
		if (endpoints != null) {
			for (XmlNode xmlNode : endpoints) {
				root.removeNode(xmlNode);
			}
		} else {
			XmlNode node = new XmlNode("endpoint");
			node.setAttribute("name", serviceId);
			node.setAttribute("implementation", className);
			node.setAttribute("url-pattern", urlPattern);
			root.addNode(node);
		}
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + root;
		try {
			InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
			return is;
		} catch (UnsupportedEncodingException e) {
			logger.errorMessage(e.getMessage(),e);
		}
		return null;
	}


	private static void addAnnotation(ClassFile cf, String name,
			java.util.Map<String, String> params) {
		ConstPool cp = cf.getConstPool();
		AnnotationsAttribute attribute = new AnnotationsAttribute(cp,
				AnnotationsAttribute.visibleTag);
		Annotation annotation = new Annotation(name, cp);
		Iterator<java.util.Map.Entry<String, String>> iterator = params
				.entrySet().iterator();
		while (iterator.hasNext()) {
			java.util.Map.Entry<String, String> entry = iterator.next();
			annotation.addMemberValue(entry.getKey(), new StringMemberValue(
					entry.getValue(), cp));
		}
		attribute.addAnnotation(annotation);
		cf.addAttribute(attribute);
		cf.setVersionToJava5();
	}

	private static CtMethod getCtMethod(CtClass cc, ServiceInfo serviceInfo)
			throws NotFoundException, CannotCompileException {
		// 获取javassist方法参数列表params
		List<Parameter> parameters = serviceInfo.getParameters();
		CtClass[] params = new CtClass[parameters.size()];
		Parameter parameter = null;
		for (int i = 0; i < parameters.size(); i++) {
			parameter = parameters.get(i);
			CtClass param = getCtMethodParam(parameter);
			params[i] = param;
		}

		// 创建新CtMethod对象
		String methodName = serviceInfo.getServiceId();
		String _sReturnType = getResultType(serviceInfo.getResults());
		CtClass returnType = ClassPool.getDefault().get(_sReturnType);
		CtMethod cm = new CtMethod(returnType, methodName, params, cc);
		cm.setModifiers(Modifier.PUBLIC);
		String body = getMethodBody(serviceInfo.getResults());
		if (body != null) {
			cm.setBody(body.toString());
			// cm.setBody("return \"张三\"; ");
		} else {
			cm.setBody("return;");
		}

		// String methodDes = getMethodDes(serviceInfo);
		// CtMethod cm = CtNewMethod.make(methodDes, cc);
		// cc.addMethod(cm);

		// 为方法添加javax.jws.WebResult注解
		List<Parameter> results = serviceInfo.getResults();
		if (results != null && results.size() > 0) {
			Parameter result = results.get(0);
			String resultName = result.getName().trim();
			if (resultName.length() > 0) {
				ClassFile cf = cc.getClassFile();
				ConstPool cp = cf.getConstPool();
				MethodInfo minfo = cm.getMethodInfo();
				AnnotationsAttribute attr = new AnnotationsAttribute(cp,
						AnnotationsAttribute.visibleTag);
				Annotation webResult = new Annotation("javax.jws.WebResult", cp);
				webResult.addMemberValue("name", new StringMemberValue(
						resultName, cp));
				attr.addAnnotation(webResult);
				minfo.addAttribute(attr);
				
				
			}
		}

		return cm;
	}

	private static CtClass getCtMethodParam(Parameter parameter) {
//		String name = parameter.getName();
		CtClass cc = null;
		try {
			Class<?> type = getJavassistParamType(parameter);
			ClassClassPath classPath = new ClassClassPath(type);
			ClassPool.getDefault().insertClassPath(classPath);
			cc = ClassPool.getDefault().get(type.getName());
		} catch (Exception e) {
			logger.errorMessage(e.getMessage(),e);
		}
//		// 类添加javax.jws.WebParam注解
//		ClassFile cf = cc.getClassFile();
//		java.util.Map<String, String> params = new HashMap<String, String>();
//		params.put("name", parameter.getName());
//		addAnnotation(cf, "javax.jws.WebParam", params);
//		ConstPool cp = cf.getConstPool();
//		AnnotationsAttribute attribute = new AnnotationsAttribute(cp,
//				AnnotationsAttribute.visibleTag);
//		Annotation annotation = new Annotation("javax.xml.bind.annotation.XmlAccessorType", cp);
//		annotation.addMemberValue("value", new ClassMemberValue("javax.xml.bind.annotation.XmlAccessType.FIELD", cp));
//		Iterator<java.util.Map.Entry<String, String>> iterator = params
//				.entrySet().iterator();
//		while (iterator.hasNext()) {
//			java.util.Map.Entry<String, String> entry = iterator.next();
//			annotation.addMemberValue(entry.getKey(), new StringMemberValue(
//					entry.getValue(), cp));
//		}
//		attribute.addAnnotation(annotation);
//		cf.addAttribute(attribute);
//		cf.setVersionToJava5();
		return cc;
	}

	private static Class<?> classLoaded(String className) {
		Class<?> c = null;
		try {
			c = Class.forName(className);
			return c;
		} catch (ClassNotFoundException e) {
		}
		return null;
	}

	/**
	 * 将parameter对象包含的信息翻译为Javassist能够加载的方法参数类型
	 * 
	 * @param parameter
	 * @return
	 */
//	private static String getJavassistParamType(Parameter parameter) {
//		if (parameter == null) {
//			return null;
//		}
//		String type = parameter.getType();
//		if (type == null || type.trim().length() == 0) {
//			return null;
//		} else if ("char".equals(type)) {
//			return "java.lang.Character";
//		} else if ("byte".equals(type)) {
//			return "java.lang.Byte";
//		} else if ("short".equals(type)) {
//			return "java.lang.Short";
//		} else if ("int".equals(type)) {
//			return "java.lang.Integer";
//		} else if ("long".equals(type)) {
//			return "java.lang.Long";
//		} else if ("float".equals(type)) {
//			return "java.lang.Float";
//		} else if ("double".equals(type)) {
//			return "java.lang.Double";
//		} else if ("boolean".equals(type)) {
//			return "java.lang.Boolean";
//		}
//		return type;
//	}
	private static Class<?> getJavassistParamType(Parameter parameter) throws ClassNotFoundException {
		if (parameter == null) {
			return null;
		}
		if (parameter.getCollectionType() != null
				&& parameter.getCollectionType().trim().length() > 0) {
			return LoaderManager.getClass(parameter.getCollectionType());
		}
		String type = parameter.getType();
		if(parameter.isArray()){
			return getJavassistParamArray(type);
		}
		if ("char".equals(type)) {
			return char.class;
		} else if ("byte".equals(type)) {
			return byte.class;
		} else if ("short".equals(type)) {
			return short.class;
		} else if ("int".equals(type)) {
			return int.class;
		} else if ("long".equals(type)) {
			return long.class;
		} else if ("float".equals(type)) {
			return float.class;
		} else if ("double".equals(type)) {
			return double.class;
		} else if ("boolean".equals(type)) {
			return boolean.class;
		}
		
		return LoaderManager.getClass(type);
	}
	
	private static Class<?> getJavassistParamArray(String type) throws ClassNotFoundException {
		if ("char".equals(type)) {
			return char[].class;
		} else if ("byte".equals(type)) {
			return byte[].class;
		} else if ("short".equals(type)) {
			return short[].class;
		} else if ("int".equals(type)) {
			return int[].class;
		} else if ("long".equals(type)) {
			return long[].class;
		} else if ("float".equals(type)) {
			return float[].class;
		} else if ("double".equals(type)) {
			return double[].class;
		} else if ("boolean".equals(type)) {
			return boolean[].class;
		}
		return Array.newInstance(LoaderManager.getClass(type),1).getClass();
	}

//	private static String getParameterType(Parameter parameter) {
//		if (parameter.getCollectionType() != null
//				&& parameter.getCollectionType().trim().length() > 0) {
//			return parameter.getCollectionType();
//		}
//
//		String paramType = parameter.getType();
//		if ("int".equals(paramType) || "char".equals(paramType)
//				|| "byte".equals(paramType) || "short".equals(paramType)
//				|| "long".equals(paramType) || "double".equals(paramType)
//				|| "float".equals(paramType) || "boolean".equals(paramType)
//				|| paramType.startsWith("java.")) {
//			return paramType;
//		}
//
//		return paramType;
//	}

	/**
	 * 解析方法返回类型
	 * 
	 * @param results
	 * @return
	 */
	private static String getResultType(List<Parameter> results) {
		if (results == null || results.size() == 0) {
			return "void";
		}
		return getResultType(results.get(0));
	}

	private static String getResultType(Parameter parameter) {
		if (parameter == null) {
			return "void";
		}
		if (parameter.getCollectionType() != null
				&& parameter.getCollectionType().trim().length() > 0) {
			return parameter.getCollectionType();
		}
		String type = parameter.getType();
		if (type == null || type.trim().length() == 0 || "void".equals(type)) {
			return "void";
		} 
		if(parameter.isArray()){
			return type+"[]";
		}
		return type;
	}

	/**
	 * 根据函数返回类型，生成空的函数方法体
	 * 
	 * @param returnType
	 * @return
	 */
//	private static String getMethodBody(String returnType) {
//		if ("void".equals(returnType)) {
//			return "{}";
//		} else if ("char".equals(returnType)) {
//			return "{return ' ';}";
//		} else if ("byte".equals(returnType)) {
//			return "{return 0;}";
//		} else if ("short".equals(returnType)) {
//			return "{return 0;}";
//		} else if ("int".equals(returnType)) {
//			return "{return 0;}";
//		} else if ("long".equals(returnType)) {
//			return "{return 0L;}";
//		} else if ("float".equals(returnType)) {
//			return "{return 0;}";
//		} else if ("double".equals(returnType)) {
//			return "{return 0.0;}";
//		} else if ("boolean".equals(returnType)) {
//			return "{return true;}";
//		} else {
//			return "{return null;}";
//		}
//	}

	/**
	 * 根据函数返回类型，生成空的函数方法体
	 * 
	 * @param returnType
	 * @return
	 */
	private static String getMethodBody(Parameter returnType) {
		if (returnType == null) {
			return null;
		}
		if(returnType.isArray()){
			return "return null;";
		}
		if (returnType.getCollectionType() != null
				&& returnType.getCollectionType().trim().length() > 0) {
			return "return null;";
		}
		String type = returnType.getType();
		if (type == null || type.trim().length() == 0 || "void".equals(type)) {
			return null;
		} else if ("char".equals(type)) {
			return "return ' ';";
		} else if ("byte".equals(type)) {
			return "return 0;";
		} else if ("short".equals(type)) {
			return "return 0;";
		} else if ("int".equals(type)) {
			return "return 0;";
		} else if ("long".equals(type)) {
			return "return 0L;";
		} else if ("float".equals(type)) {
			return "return 0;";
		} else if ("double".equals(type)) {
			return "return 0.0;";
		} else if ("boolean".equals(type)) {
			return "return true;";
		} else {
			return "return null;";
		}
	}

	/**
	 * 根据函数返回类型，生成空的函数方法体
	 * 
	 * @param returnType
	 * @return
	 */
	private static String getMethodBody(List<Parameter> returns) {
		if (returns == null || returns.size() == 0) {
			return null;
		}
		return getMethodBody(returns.get(0));
	}

	

	/**************************************************************************/
	// private static Logger log = Logger.getLogger(WebserviceUtil.class);

	/**
	 * 动态加载类的包名
	 */
	public static final String PACKAGE_NAME = "org.tinygroup.webservice.server.impl";

	/**************************************************************************/

//	private static String getMethodDes(ServiceInfo serviceInfo) {
//		String methodName = serviceInfo.getServiceId();
//		if (methodName == null || methodName.trim().length() == 0) {
//		}
//
//		// 定义方法key字符串，用于唯一查找方法的标示符
//		StringBuilder methodKey = new StringBuilder(methodName);
//		// 方法返回值类型描述字符串
//		String returnType = getResultType(serviceInfo.getResults());
//		// 方法体描述字符串
//		String bodyDes = getMethodBody(returnType);
//
//		// 参数内容描述字符�?
//		StringBuilder paramsDes = new StringBuilder("(");
//		List<Parameter> parameters = serviceInfo.getParameters();
//		Parameter parameter = null;
//		String paramType = null;
//		if (parameters == null) {
//			parameters = new ArrayList<Parameter>();
//		}
//		String[] methodNames = new String[parameters.size()];
//		for (int i = 0; i < parameters.size(); i++) {
//			parameter = parameters.get(i);
//			if (i != 0) {
//				paramsDes.append(",");
//			}
//			paramType = getParameterType(parameter);
//
//			paramsDes.append(paramType);
//			paramsDes.append(" ").append(parameter.getName());
//
//			methodKey.append("_").append(paramType);
//			methodNames[i] = parameter.getName();
//		}
//		paramsDes.append(")");
//
//		StringBuilder des = new StringBuilder("public ");
//		des.append(returnType);
//		des.append(" ");
//		des.append(methodName);
//		des.append(paramsDes);
//		des.append(bodyDes);
//
//		return des.toString();
//	}

}
