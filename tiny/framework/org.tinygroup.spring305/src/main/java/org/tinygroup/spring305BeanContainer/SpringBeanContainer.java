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
package org.tinygroup.spring305BeanContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
	ApplicationContext applicationContext = null;
	List<String> configs = new ArrayList<String>();
	List<ApplicationContext> subs = new ArrayList<ApplicationContext>();
	boolean inited = false;

	public ApplicationContext getBeanContainerPrototype() {
		return applicationContext;
	}

	public SpringBeanContainer() {
		if (inited == true)
			return;
		inited = true;
		FileSystemXmlApplicationContext fileSystemXmlApplicationContext = new FileSystemXmlApplicationContext();
		fileSystemXmlApplicationContext.setAllowBeanDefinitionOverriding(true);
		fileSystemXmlApplicationContext.refresh();
		applicationContext = fileSystemXmlApplicationContext;

	}

	public ApplicationContext getSubBeanContainer(List<FileObject> files,
			ClassLoader loader) {
		List<String> configLocations = new ArrayList<String>();
		for (FileObject fileObject : files) {
			String urlString = fileObject.getURL().toString();
			if (!configLocations.contains(urlString)) {
				configLocations.add(urlString);
				logger.logMessage(LogLevel.INFO, "添加Spring配置文件:{urlString}",
						urlString);
			}
		}
		FileSystemXmlApplicationContext sub = new FileSystemXmlApplicationContext(
				listToArray(configLocations), applicationContext);
		sub.setClassLoader(loader);
		sub.refresh();
		subs.add(sub);
		return sub;

	}

	private static String[] listToArray(List<String> list) {
		String[] a = new String[0];
		return (String[]) list.toArray(a);
	}

	public List<ApplicationContext> getSubBeanContainers() {
		return subs;
	}

	public <T> Collection<T> getBeans(Class<T> type) {
		return applicationContext.getBeansOfType(type).values();
	}

	public <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}

	public <T> T getBean(Class<T> clazz) {

		String[] beanNames = applicationContext.getBeanNamesForType(clazz);
		if (beanNames.length == 1) {
			return (T) applicationContext.getBean(beanNames[0], clazz);
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
		return (T) applicationContext.getBean(name, clazz);
	}

	public void regSpringConfigXml(List<FileObject> files) {
		for (FileObject fileObject : files) {
			String urlString = fileObject.getURL().toString();
			addUrl(urlString);
		}
	}

	public void regSpringConfigXml(String files) {
		String urlString = SpringBeanContainer.class.getResource(files).toString();
		addUrl(urlString);
	}

	private void addUrl(String urlString) {
		if (!configs.contains(urlString)) {
			configs.add(urlString);
			logger.logMessage(LogLevel.INFO, "添加Spring配置文件:{urlString}",
					urlString);
		}
	}

	public void refresh() {
		FileSystemXmlApplicationContext app = (FileSystemXmlApplicationContext) applicationContext;
		app.setConfigLocations(listToArray(configs));
		app.refresh();
	}
}
