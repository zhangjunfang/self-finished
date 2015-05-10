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
package org.tinygroup.service.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.beanutil.BeanUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.event.Parameter;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.service.ServiceMappingManager;
import org.tinygroup.service.ServiceProxy;
import org.tinygroup.service.exception.ServiceLoadException;
import org.tinygroup.service.loader.AnnotationServiceLoader;
import org.tinygroup.service.registry.ServiceRegistry;
import org.tinygroup.service.registry.ServiceRegistryItem;
import org.tinygroup.service.util.ServiceUtil;

public abstract class AbstractAnnotationServiceLoader implements
		AnnotationServiceLoader {
	private Logger logger = LoggerFactory
			.getLogger(AbstractAnnotationServiceLoader.class);

	private ServiceMappingManager serviceMappingManager;

	public ServiceMappingManager getServiceMappingManager() {
		return serviceMappingManager;
	}

	public void setServiceMappingManager(
			ServiceMappingManager serviceMappingManager) {
		this.serviceMappingManager = serviceMappingManager;
	}

	/**
	 * 载入服务
	 */
	public void loadService(ServiceRegistry serviceRegistry,ClassLoader classLoader)
			throws ServiceLoadException {
		List<String> classNames = getClassNames();// 这个由子类提供
		for (String className : classNames) {
			try {
				logger.logMessage(LogLevel.INFO,
						"从{className}中查找ServiceAnnotation", className);
				Class<?> clazz = classLoader.loadClass(className);
				Annotation annotation = clazz
						.getAnnotation(ServiceComponent.class);
				if (annotation != null) {
					registerServices(clazz, annotation, serviceRegistry);
				} else {
					logger.logMessage(LogLevel.INFO,
							"{className}中无ServiceAnnotation", className);
				}
				logger.logMessage(LogLevel.INFO,
						"从{className}中查找ServiceAnnotation完成", className);

			} catch (Exception e) {
				logger.error("service.loadServiceException", e, className);
			}
		}
	}

	/**
	 * 从AnnotationClassAction接入，新增注解服务
	 * 
	 * @param clazz
	 * @param annotation
	 * @param serviceRegistry
	 */
	public void loadService(Class<?> clazz, Annotation annotation,
			ServiceRegistry serviceRegistry) {
		String className = clazz.getName();
		logger.logMessage(LogLevel.INFO, "从{}中查找ServiceAnnotation", className);
		try {
			registerServices(clazz, annotation, serviceRegistry);
		} catch (Exception e) {
			logger.error("service.loadServiceException", e, className);
		}
	}

	public void removeService(ServiceRegistry serviceRegistry,ClassLoader classLoader) {
		List<String> classNames = getClassNames();// 这个由子类提供
		for (String className : classNames) {
			try {
				Class<?> clazz = classLoader.loadClass(className);
				Annotation annotation = clazz
						.getAnnotation(ServiceComponent.class);
				if (annotation != null) {
					removeServices(clazz, serviceRegistry);
				}

			} catch (Exception e) {
				logger.log(LogLevel.ERROR, "service.loadServiceException",
						className);
			}
		}
	}

	private void removeServices(Class<?> clazz, ServiceRegistry serviceRegistry)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, InstantiationException {
		for (Method method : clazz.getMethods()) {
			Annotation annotation = method.getAnnotation(ServiceMethod.class);
			if (annotation != null) {
				String serviceId = getAnnotationStringValue(annotation,
						ServiceMethod.class, "serviceId");
				serviceRegistry.removeService(serviceId);
			}
		}

	}

	/**
	 * 注册所有服务服务
	 * 
	 * @param clazz
	 * @param annotation
	 * @param serviceRegistry
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ServiceLoadException
	 * @throws Exception
	 */
	private void registerServices(Class<?> clazz, Annotation annotation,
			ServiceRegistry serviceRegistry) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			InstantiationException, ServiceLoadException {
		ServiceRegistryItem item = new ServiceRegistryItem();
		logger.logMessage(LogLevel.INFO, "读取ServiceComponent: {}",
				clazz.getName());
		registerServices(clazz, item, serviceRegistry);

	}

	/**
	 * 注册服务
	 * 
	 * @param clazz
	 * @param superItem
	 * @param serviceRegistry
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ServiceLoadException
	 * @throws Exception
	 */
	private void registerServices(Class<?> clazz,
			ServiceRegistryItem superItem, ServiceRegistry serviceRegistry)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, InstantiationException, ServiceLoadException {
		for (Method method : clazz.getMethods()) {
			Annotation annotation = method.getAnnotation(ServiceMethod.class);
			if (annotation != null) {
				logger.logMessage(LogLevel.INFO, "开始加载方法{0}为服务",
						method.getName());
				ServiceRegistryItem item = new ServiceRegistryItem();
				// serviceId
				// localName
				String localName = getAnnotationStringValue(annotation,
						ServiceMethod.class, "localName");
				item.setLocalName(localName);
				// category
				String category = getAnnotationStringValue(annotation,
						ServiceMethod.class, "category");
				item.setCategory(category);
				// description
				String description = getAnnotationStringValue(annotation,
						ServiceMethod.class, "description");
				item.setDescription(description);

				registerService(clazz, method, item);

				ServiceRegistryItem registryItem =  ServiceUtil.copyServiceItem(item);
				String serviceId = getAnnotationStringValue(annotation,
						ServiceMethod.class, "serviceId");
				if (StringUtil.isBlank(serviceId)) {
					serviceId = StringUtil.toCamelCase(clazz.getSimpleName())
							+ "." + StringUtil.toCamelCase(method.getName());
				}
				registryItem.setServiceId(serviceId);
				serviceRegistry.registerService(registryItem);
				String alias = getAnnotationStringValue(annotation,
						ServiceMethod.class, "alias");
				if (!StringUtil.isBlank(alias)) {
					registryItem =  ServiceUtil.copyServiceItem(item);
					registryItem.setServiceId(alias);
					serviceRegistry.registerService(registryItem);
				}
				logger.logMessage(LogLevel.INFO, "加载方法{0}为服务完毕",
						method.getName());
				// 跳转信息servicemapping
//				ServiceViewMapping serviceViewMapping = method
//						.getAnnotation(ServiceViewMapping.class);
//				if (serviceViewMapping != null) {
//					org.tinygroup.service.config.ServiceViewMapping mapping = new org.tinygroup.service.config.ServiceViewMapping();
//					mapping.setServiceId(serviceId);
//					mapping.setPath(serviceViewMapping.value());
//					mapping.setType(StringUtil.defaultIfBlank(
//							serviceViewMapping.type(), "forward"));
//					serviceMappingManager.addServiceMapping(mapping);
//					if (!StringUtil.isBlank(alias)) {
//						mapping = new org.tinygroup.service.config.ServiceViewMapping();
//						mapping.setServiceId(alias);
//						mapping.setPath(serviceViewMapping.value());
//						mapping.setType(StringUtil.defaultIfBlank(
//								serviceViewMapping.type(), "forward"));
//						serviceMappingManager.addServiceMapping(mapping);
//					}
//				}
			}
		}
	}


	/**
	 * 注册服务
	 * 
	 * @param clazz
	 * @param method
	 * @param item
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ServiceLoadException
	 * @throws Exception
	 */
	private void registerService(Class<?> clazz, Method method,
			ServiceRegistryItem item) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			InstantiationException, ServiceLoadException {
		ServiceProxy serviceProxy = new ServiceProxy();
		serviceProxy.setObjectInstance(getServiceInstance(clazz));
		serviceProxy.setMethod(method);
		getInputParameterNames(item, method, serviceProxy);
		getOutputParameterNames(item, clazz, method, serviceProxy);

		item.setService(serviceProxy);

	}

	protected Object getServiceInstance(Class<?> clazz) {
		ServiceComponent serviceComponent = clazz
				.getAnnotation(ServiceComponent.class);
		BeanContainer<?> container = BeanContainerFactory.getBeanContainer(this
				.getClass().getClassLoader());
		try {
			if (StringUtil.isBlank(serviceComponent.bean())) {
				return container.getBean(clazz);
			}
			return container.getBean(serviceComponent.bean());
		} catch (RuntimeException e) {
			logger.logMessage(LogLevel.WARN, "查找Bean:{0}时发生异常：{1}",
					serviceComponent.bean(), e.getMessage());
			if (!clazz.isInterface()) {
				try {
					return clazz.newInstance();
				} catch (Exception e1) {
					// Do Nothing
				}
			}
			throw e;
		}
	}

	private void getOutputParameterNames(ServiceRegistryItem item,
			Class<?> clazz, Method method, ServiceProxy serviceProxy)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, ServiceLoadException {
		logger.logMessage(LogLevel.INFO, "开始加载方法对应的服务出参,方法{0},服务:{1}",
				method.getName(), item.getServiceId());
		Class<?> parameterType = method.getReturnType();
		List<Parameter> outputParameterDescriptors = new ArrayList<Parameter>();
		Annotation annotation = method.getAnnotation(ServiceResult.class);
		Parameter descriptor = new Parameter();
		if (implmentInterface(parameterType, Collection.class)) {
			ParameterizedType pt = (ParameterizedType) (method
					.getGenericReturnType());
			Type[] actualTypeArguments = pt.getActualTypeArguments();
			Class<?> actualClass = (Class<?>) actualTypeArguments[0];
			if (!ServiceUtil.assignFromSerializable(actualClass)) {
				throw new ServiceLoadException("服务返回值为集合类型，其中元素类型:<"
						+ actualClass.getName() + ">必须实现Serializable接口");
			}
			descriptor.setType(actualClass.getName());
			descriptor.setCollectionType(parameterType.getName());
		} else {
			if (!ServiceUtil.assignFromSerializable(parameterType)) {
				throw new ServiceLoadException("服务返回值类型:<"
						+ parameterType.getName() + ">必须实现Serializable接口");
			}
			descriptor.setType(parameterType.getName());
		}
		logger.logMessage(LogLevel.INFO, "服务出参type:{name}",
				descriptor.getType());
		descriptor.setArray(parameterType.isArray());
		String name = null;
		if (annotation != null) {
			Boolean required = Boolean.valueOf(getAnnotationStringValue(
					annotation, ServiceResult.class, "required"));
			descriptor.setRequired(required);
			name = getAnnotationStringValue(annotation, ServiceResult.class,
					"name");
			String validatorSence = getAnnotationStringValue(annotation,
					ServiceResult.class, "validatorSence");
			descriptor.setValidatorSence(validatorSence);
			String localName = getAnnotationStringValue(annotation,
					ServiceResult.class, "localName");
			descriptor.setTitle(localName);
			String description = getAnnotationStringValue(annotation,
					ServiceResult.class, "description");
			descriptor.setDescription(description);
			boolean isArray = Boolean.valueOf(getAnnotationStringValue(
					annotation, ServiceResult.class, "isArray"));
			descriptor.setArray(isArray);
			logger.logMessage(LogLevel.INFO, "服务出参name:{name}", name);
		} else {
			logger.logMessage(LogLevel.INFO, "服务出参未配置");
		}
		if (StringUtil.isBlank(name)) {
			name = StringUtil.toCamelCase(clazz.getSimpleName()) + "_"
					+ StringUtil.toCamelCase(method.getName()) + "_" + "result";
		}
		descriptor.setName(name);
		serviceProxy.setOutputParameter(descriptor);
		outputParameterDescriptors.add(descriptor);
		item.setResults(outputParameterDescriptors);
		logger.logMessage(LogLevel.INFO, "加载方法对应的服务出参完毕,方法{0},服务:{1}",
				method.getName(), item.getServiceId());

	}

	/**
	 * 把参数名称注册过来
	 * 
	 * @param method
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws ServiceLoadException
	 */
	private void getInputParameterNames(ServiceRegistryItem item,
			Method method, ServiceProxy serviceProxy)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, ServiceLoadException {
		logger.logMessage(LogLevel.INFO, "开始加载方法对应的服务入参,方法{0}",
				method.getName());
		String[] parameterNames = BeanUtil.getMethodParameterName(
				method.getDeclaringClass(), method);
		Annotation[][] annotations = method.getParameterAnnotations();
		Class<?>[] parameterTypes = method.getParameterTypes();
		List<Parameter> inputParameterDescriptors = new ArrayList<Parameter>();
		for (int i = 0; i < parameterTypes.length; i++) {
			Annotation annotation = getParameterAnnotation(annotations, i);
			Parameter descriptor = new Parameter();
			Class<?> parameterType = parameterTypes[i];
			if (implmentInterface(parameterType, Collection.class)) {
				ParameterizedType pt = (ParameterizedType) (method
						.getGenericParameterTypes()[i]);
				Type[] actualTypeArguments = pt.getActualTypeArguments();
				Class<?> clazz = (Class<?>) actualTypeArguments[0];
				if (!ServiceUtil.assignFromSerializable(clazz)) {
					throw new ServiceLoadException("服务参数集合类型中元素类型:<"
							+ clazz.getName() + ">必须实现Serializable接口");
				}
				descriptor.setType(clazz.getName());
				descriptor.setCollectionType(parameterType.getName());
			} else {
				if (!ServiceUtil.assignFromSerializable(parameterType)) {
					throw new ServiceLoadException("服务参数类型:<"
							+ parameterType.getName() + ">必须实现Serializable接口");
				}
				if(parameterType.isArray()){
					descriptor.setType(parameterType.getComponentType().getName());
					descriptor.setArray(true);
				}else{
					descriptor.setType(parameterType.getName());
				}
			}
			if (annotation != null) {
				String name = getAnnotationStringValue(annotation,
						ServiceParameter.class, "name");
				if (name.length() == 0) {
					name = parameterNames[i];
				}
				descriptor.setName(name);
				boolean required = Boolean.valueOf(getAnnotationStringValue(
						annotation, ServiceParameter.class, "required"));
				descriptor.setRequired(required);
				String validatorSence = getAnnotationStringValue(annotation,
						ServiceParameter.class, "validatorSence");
				descriptor.setValidatorSence(validatorSence);
				String localName = getAnnotationStringValue(annotation,
						ServiceParameter.class, "localName");
				descriptor.setTitle(localName);
				String description = getAnnotationStringValue(annotation,
						ServiceParameter.class, "description");
				descriptor.setDescription(description);
				String collectionType = getAnnotationStringValue(annotation,
						ServiceParameter.class, "collectionType");
				descriptor.setCollectionType(collectionType);
				boolean isArray = Boolean.valueOf(getAnnotationStringValue(
						annotation, ServiceParameter.class, "isArray"));
				descriptor.setArray(isArray);
			} else {
				descriptor.setName(parameterNames[i]);
			}
			inputParameterDescriptors.add(descriptor);
		}
		item.setParameters(inputParameterDescriptors);
		serviceProxy.setInputParameters(inputParameterDescriptors);
		logger.logMessage(LogLevel.INFO, "加载方法对应的服务入参完毕,方法{0}",
				method.getName());

	}

	private boolean implmentInterface(Class<?> clazz, Class<?> interfaceClazz) {
		return interfaceClazz.isAssignableFrom(clazz);
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

	private String getAnnotationStringValue(Annotation annotation,
			Class<?> annotationClazz, String name)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Object[] args = null;
		Class<?>[] argsType = null;
		return annotationClazz.getMethod(name, argsType)
				.invoke(annotation, args).toString();
	}

	protected abstract List<String> getClassNames();

}
