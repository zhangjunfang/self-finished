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
package org.tinygroup.service.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.Configuration;
import org.tinygroup.event.Parameter;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.service.ServiceProxy;
import org.tinygroup.service.exception.ServiceLoadException;
import org.tinygroup.service.loader.ServiceLoader;
import org.tinygroup.service.registry.ServiceRegistry;
import org.tinygroup.service.registry.ServiceRegistryItem;
import org.tinygroup.service.util.ServiceUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlparser.node.XmlNode;

public abstract class XmlConfigServiceLoader extends AbstractFileProcessor
		implements ServiceLoader, Configuration {
	private Logger logger = LoggerFactory
			.getLogger(XmlConfigServiceLoader.class);
	private XmlNode applicationConfig;
	private XmlNode componentConfig;

	private static Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
	static {
		classMap.put("int", int.class);
		classMap.put("short", short.class);
		classMap.put("byte", byte.class);
		classMap.put("char", char.class);
		classMap.put("long", long.class);
		classMap.put("double", double.class);
		classMap.put("float", float.class);
		classMap.put("boolean", boolean.class);
		classMap.put("void", void.class);
	}

	/**
	 * 载入服务
	 */
	public void loadService(ServiceRegistry serviceRegistry,ClassLoader classLoader)
			throws ServiceLoadException {
		List<ServiceComponents> list = getServiceComponents();// 这个由子类提供
		for (ServiceComponents serviceComponents : list) {
			loadService(serviceRegistry, serviceComponents,classLoader);
		}
	}

	public void removeService(ServiceRegistry serviceRegistry,ClassLoader classLoader) {
		List<ServiceComponents> list = getServiceComponents();
		for (ServiceComponents serviceComponents : list) {
			removeServiceComponents(serviceRegistry, serviceComponents);
		}
	}

	public void removeServiceComponents(ServiceRegistry serviceRegistry,
			ServiceComponents serviceComponents) {
		for (ServiceComponent component : serviceComponents
				.getServiceComponents()) {
			for (ServiceMethod method : component.getServiceMethods()) {
				serviceRegistry.removeService(method.getServiceId());
			}

		}
	}

	private void loadService(ServiceRegistry serviceRegistry,
			ServiceComponents serviceComponents,ClassLoader classLoader) throws ServiceLoadException {
		for (ServiceComponent serviceComponent : serviceComponents
				.getServiceComponents()) {
			try {

				Object object = getServiceInstance(serviceComponent);
				registerServices(object, serviceComponent, serviceRegistry,classLoader);

			} catch (Exception e) {
				logger.errorMessage("实例化ServiceComponent时出现异常,类名:", e,
						serviceComponent.getType());
			}
		}
	}

	/**
	 * 注册服务
	 * 
	 * @param object
	 * @param serviceComponent
	 * @param serviceRegistry
	 * @throws ServiceLoadException
	 * @throws ClassNotFoundException
	 */
	private void registerServices(Object object,
			ServiceComponent serviceComponent, ServiceRegistry serviceRegistry,ClassLoader classLoader)
			throws ClassNotFoundException, ServiceLoadException {
		for (ServiceMethod serviceMethod : serviceComponent.getServiceMethods()) {
			ServiceRegistryItem item = new ServiceRegistryItem();
			item.setLocalName(serviceMethod.getLocalName());
			item.setDescription(serviceMethod.getDescription());
			item.setCategory(serviceMethod.getCategory());
			registerService(object, serviceMethod, item,classLoader);
			String serviceId=serviceMethod.getServiceId();
			if(!StringUtil.isBlank(serviceId)){
				ServiceRegistryItem registryItem =  ServiceUtil.copyServiceItem(item);
				registryItem.setServiceId(serviceId);
				serviceRegistry.registerService(registryItem);
			}
			String alias=serviceMethod.getAlias();
			if(!StringUtil.isBlank(alias)){
				ServiceRegistryItem registryItem =  ServiceUtil.copyServiceItem(item);
				registryItem.setServiceId(alias);
				serviceRegistry.registerService(registryItem);
			}

		}
	}

	/**
	 * 注册服务
	 * 
	 * @param object
	 * @param serviceMethod
	 * @param item
	 * @throws ServiceLoadException
	 * @throws ClassNotFoundException
	 */
	private void registerService(Object object, ServiceMethod serviceMethod,
			ServiceRegistryItem item,ClassLoader classLoader) throws ClassNotFoundException,
			ServiceLoadException {
		ServiceProxy serviceProxy = new ServiceProxy();
		serviceProxy.setMethodName(serviceMethod.getMethodName());
		serviceProxy.setLoader(classLoader);
		serviceProxy.setObjectInstance(object);
		getInputParameterNames(item, serviceMethod, serviceProxy,classLoader);
		getOutputParameterNames(item, serviceMethod, serviceProxy,classLoader);
		item.setService(serviceProxy);

	}

	/**
	 * 把参数名称注册过来
	 * 
	 * @param item
	 * @param serviceMethod
	 * @param serviceProxy
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws ServiceLoadException
	 */
	private void getInputParameterNames(ServiceRegistryItem item,
			ServiceMethod serviceMethod, ServiceProxy serviceProxy,ClassLoader classLoader)
			throws ClassNotFoundException, ServiceLoadException {
		List<Parameter> inputParameterDescriptors = new ArrayList<Parameter>();
		// ==================入参处理 begin========================
		for (ServiceParameter serviceParameter : serviceMethod
				.getServiceParameters()) {
			String type = serviceParameter.getType();
			Class<?> parameterType = classMap.get(type);
			if (parameterType == null) {
				parameterType = classLoader.loadClass(type);
				classMap.put(type, parameterType);
			}
			Parameter descriptor = new Parameter();
			if (!ServiceUtil.assignFromSerializable(parameterType)) {
				throw new ServiceLoadException("服务返回值类型:<"
						+ parameterType.getName() + ">必须实现Serializable接口");
			}
			descriptor.setType(parameterType.getName());
			descriptor.setArray(serviceParameter.isArray());
			descriptor.setName(serviceParameter.getName());
			descriptor.setRequired(serviceParameter.isRequired());
			descriptor.setValidatorSence(serviceParameter.getValidatorScene());
			descriptor.setTitle(serviceParameter.getLocalName());
			descriptor.setCollectionType(serviceParameter.getCollectionType());
			descriptor.setDescription(serviceParameter.getDescription());
			inputParameterDescriptors.add(descriptor);
		}
		// ==================入参处理 end========================
		item.setParameters(inputParameterDescriptors);
		serviceProxy.setInputParameters(inputParameterDescriptors);
	}

	private void getOutputParameterNames(ServiceRegistryItem item,
			ServiceMethod serviceMethod, ServiceProxy serviceProxy,ClassLoader classLoader)
			throws ClassNotFoundException, ServiceLoadException {
		// ==================出参处理 begin========================
		if (serviceMethod.getServiceResult() != null) {
			ServiceParameter serviceResult = serviceMethod.getServiceResult();
			String type = serviceResult.getType();
			Class<?> parameterType = classMap.get(type);
			if (parameterType == null) {
				parameterType = classLoader.loadClass(type);
				classMap.put(type, parameterType);
			}
			Parameter descriptor = new Parameter();
			if (!ServiceUtil.assignFromSerializable(parameterType)) {
				throw new ServiceLoadException("服务返回值类型:<"
						+ parameterType.getName() + ">必须实现Serializable接口");
			}
			descriptor.setType(parameterType.getName());
			descriptor.setArray(serviceResult.isArray());
			descriptor.setRequired(serviceResult.isRequired());
			descriptor.setName(serviceResult.getName());
			descriptor.setValidatorSence(serviceResult.getValidatorScene());
			descriptor.setTitle(serviceResult.getLocalName());
			descriptor.setDescription(serviceResult.getDescription());
			descriptor.setCollectionType(serviceResult.getCollectionType());
			serviceProxy.setOutputParameter(descriptor);
			List<Parameter> outputParameterDescriptors = new ArrayList<Parameter>();
			outputParameterDescriptors.add(descriptor);
			item.setResults(outputParameterDescriptors);
		}
		// ==================出参处理 end========================
	}

	Annotation getParameterAnnotation(Annotation[][] annotations, int index) {
		for (int i = 0; i < annotations[index].length; i++) {
			if (annotations[index][i].annotationType().equals(
					ServiceParameter.class)) {
				return annotations[index][i];
			}
		}
		return null;
	}

	protected abstract List<ServiceComponents> getServiceComponents();

	protected abstract Object getServiceInstance(
			ServiceComponent serviceComponent) throws Exception;

	public String getApplicationNodePath() {
		return null;
	}

	public String getComponentConfigPath() {
		return null;
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		this.applicationConfig = applicationConfig;
		this.componentConfig = componentConfig;
	}

	public XmlNode getComponentConfig() {
		return componentConfig;
	}

	public XmlNode getApplicationConfig() {
		return applicationConfig;
	}

	public boolean isMatch(FileObject fileObject) {
		return false;
	}

	public void process() {

	}
}
