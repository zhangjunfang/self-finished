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
package org.tinygroup.springutil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.util.StringUtils;
import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;

public class SpringBeanContainer implements BeanContainer<ApplicationContext> {
	private static Logger logger = LoggerFactory
			.getLogger(SpringBeanContainer.class);
	private ApplicationContext applicationContext = null;
	private List<String> configs = new ArrayList<String>();
	private Map<ClassLoader, BeanContainer<?>> subs = new HashMap<ClassLoader, BeanContainer<?>>();
	private boolean inited = false;
	private BeanContainer<?> parent;
	private String noBeanCaseInfo;

	public ApplicationContext getBeanContainerPrototype() {
		return applicationContext;
	}

	private void initNoBeanCaseInfo() {
		if (noBeanCaseInfo != null)
			return;
		try {
			applicationContext.getBean(hashCode() + "");
		} catch (NoSuchBeanDefinitionException e) {
			noBeanCaseInfo = e.getMessage();
		}
	}

	public SpringBeanContainer() {
		if (inited == true)
			return;
		inited = true;
		FileSystemXmlApplicationContext fileSystemXmlApplicationContext = new FileSystemXmlApplicationContext();
		fileSystemXmlApplicationContext.setAllowBeanDefinitionOverriding(true);
		fileSystemXmlApplicationContext.refresh();
		applicationContext = fileSystemXmlApplicationContext;
		initNoBeanCaseInfo();
	}

	public SpringBeanContainer(SpringBeanContainer parent, ClassLoader loader) {
		if (inited == true)
			return;
		inited = true;
		FileSystemXmlApplicationContext fileSystemXmlApplicationContext = new FileSystemXmlApplicationContext(
				parent.getBeanContainerPrototype());
		fileSystemXmlApplicationContext.setAllowBeanDefinitionOverriding(true);
		fileSystemXmlApplicationContext.setClassLoader(loader);
		fileSystemXmlApplicationContext.refresh();
		applicationContext = fileSystemXmlApplicationContext;
		initNoBeanCaseInfo();
	}

	public SpringBeanContainer(SpringBeanContainer parent,
			List<FileObject> files, ClassLoader loader) {
		if (inited == true)
			return;
		inited = true;
		List<String> configLocations = new ArrayList<String>();
		for (FileObject fileObject : files) {
			String urlString = fileObject.getURL().toString();
			if (!configLocations.contains(urlString)) {
				configLocations.add(urlString);
				logger.logMessage(LogLevel.INFO, "添加Spring配置文件:{urlString}",
						urlString);
			}
		}
		FileSystemXmlApplicationContext fileSystemXmlApplicationContext = new FileSystemXmlApplicationContext(
				listToArray(configLocations),
				parent.getBeanContainerPrototype());
		fileSystemXmlApplicationContext.setAllowBeanDefinitionOverriding(true);
		fileSystemXmlApplicationContext.setClassLoader(loader);
		fileSystemXmlApplicationContext.refresh();
		applicationContext = fileSystemXmlApplicationContext;
		initNoBeanCaseInfo();
	}

	public BeanContainer<?> getSubBeanContainer(List<FileObject> files,
			ClassLoader loader) {
		SpringBeanContainer b = new SpringBeanContainer(this, files, loader);
		subs.put(loader, b);
		b.setParent(this);
		return b;
	}

	public BeanContainer<?> getSubBeanContainer(ClassLoader loader) {
		return subs.get(loader);
	}

	private static String[] listToArray(List<String> list) {
		String[] a = new String[0];
		return (String[]) list.toArray(a);
	}

	public Map<ClassLoader, BeanContainer<?>> getSubBeanContainers() {
		return subs;
	}

	public <T> Collection<T> getBeans(Class<T> type) {
		Collection<T> collection = applicationContext.getBeansOfType(type)
				.values();
		if (collection.size() == 0 && parent != null) {
			collection = parent.getBeans(type);
		}
		return collection;
	}

	public <T> T getBean(String name) {
		try {
			return (T) applicationContext.getBean(name);
		} catch (NoSuchBeanDefinitionException e) {
			String message = e.getMessage();
			if (message.equals(noBeanCaseInfo.replace(hashCode() + "", name))) {
				if (parent != null) {
					return (T) parent.getBean(name);
				}
			}
			throw e;
		}

	}

	public <T> T getBean(Class<T> clazz) {

		String[] beanNames = applicationContext.getBeanNamesForType(clazz);
		if (beanNames.length == 1) {
			return (T) applicationContext.getBean(beanNames[0]);
		} else if (beanNames.length == 0 && parent != null) {
			return parent.getBean(clazz);
		} else {
			throw new NoSuchBeanDefinitionException(clazz,
					"expected single bean but found "
							+ beanNames.length
							+ ": "
							+ StringUtils
									.arrayToCommaDelimitedString(beanNames));
		}
	}

	public <T> T getBean(String name, Class<T> clazz) {
		try {
			return (T) applicationContext.getBean(name, clazz);
		} catch (NoSuchBeanDefinitionException e) {
			String message = e.getMessage();
			if (message.equals(noBeanCaseInfo.replace(hashCode() + "", name))) {
				if (parent != null) {
					return (T) parent.getBean(name,clazz);
				}
			}
			throw e;
		}

	}

	public void regSpringConfigXml(List<FileObject> files) {
		for (FileObject fileObject : files) {
			logger.logMessage(LogLevel.INFO, "添加文件:{}", fileObject.getPath());
			String urlString = fileObject.getURL().toString();
			addUrl(urlString);
		}
	}

	public void regSpringConfigXml(String files) {
		String urlString = SpringBeanContainer.class.getResource(files)
				.toString();
		addUrl(urlString);
	}

	private void addUrl(String urlString) {
		if (!configs.contains(urlString)) {
			configs.add(urlString);
			logger.logMessage(LogLevel.INFO, "添加Spring配置文件:{urlString}",
					urlString);
		}
	}

	public void removeUrl(String urlString) {
		if (configs.contains(urlString)) {
			configs.remove(urlString);
			logger.logMessage(LogLevel.INFO, "删除Spring配置文件:{urlString}",
					urlString);
		}
	}

	public void refresh() {
		FileSystemXmlApplicationContext app = (FileSystemXmlApplicationContext) applicationContext;
		app.setConfigLocations(listToArray(configs));
		app.refresh();
	}

	public void setParent(BeanContainer<?> container) {
		this.parent = container;
	}

	public void removeSubBeanContainer(ClassLoader loader) {
		subs.remove(loader);
	}

}
