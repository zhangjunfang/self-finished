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
package org.tinygroup.bundle.test.fileresolver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import junit.framework.TestCase;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.fileresolver.impl.FileResolverImpl;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringBeanContainer;
import org.tinygroup.springutil.fileresolver.SpringBeansFileProcessor;

public class FileResolverRefreshTest extends TestCase {
	private static Logger logger = LoggerFactory
			.getLogger(FileResolverRefreshTest.class);

	public void testScan() {
		BeanContainerFactory.setBeanContainer(SpringBeanContainer.class
				.getName());
		FileResolver resolver = new FileResolverImpl();
		resolver.addFileProcessor(new SpringBeansFileProcessor());
		String path = System.getProperty("user.dir") + File.separator
				+ "testJar" + File.separator
				+ "org.tinygroup.bundlejar-1.2.0-SNAPSHOT.jar";
		URL u = null;
		File f = new File(path);
		try {
			u = f.toURI().toURL();
		} catch (MalformedURLException e) {
			assertTrue(false);
		}

		ClassLoader loader = new URLClassLoader(new URL[] { u });
		resolver.setClassLoader(loader);
		resolver.addResolvePath(path);
		resolver.addIncludePathPattern("");
		resolver.resolve();
		try {
			BeanContainerFactory.getBeanContainer(
					loader).getBean(
					"bundleTestService");
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}

		logger.logMessage(LogLevel.INFO, "==================================");
		resolver.removeResolvePath(path);
		resolver.refresh();
		try {
			BeanContainerFactory.getBeanContainer(
					loader).getBean(
					"bundleTestService");
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(true);
		}
	}
}
