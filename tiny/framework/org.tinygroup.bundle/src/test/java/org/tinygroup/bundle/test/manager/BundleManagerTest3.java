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
package org.tinygroup.bundle.test.manager;

import junit.framework.TestCase;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.bundle.BundleManager;
import org.tinygroup.bundle.test.util.TestUtil;

public class BundleManagerTest3 extends TestCase {

	public void testSpringBean(){
		TestUtil.init();
		BeanContainer<?> container = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader());
		BundleManager manager = (BundleManager) container.getBean(BundleManager.BEAN_NAME);
		manager.start();
		try {
			manager.getTinyClassLoader("test2").loadClass("org.tinygroup.MyTestImpl2");
			
			BeanContainer<?> app = container.getSubBeanContainer(manager.getTinyClassLoader("test2")
			);
			System.out.println(app);
			assertNotNull(app);
			System.out.println(app.getBean("mytestImpl2"));
			assertNotNull(app.getBean("mytestImpl2"));
			try {
				System.out.println(app.getBean("mytestImpl"));
				assertTrue(false);
			} catch (NoSuchBeanDefinitionException e) {
				assertTrue(true);
			}
			//manager.getTinyClassLoader().loadClass("org.tinygroup.hello.HelloImpl2");
			
		} catch (ClassNotFoundException e) {
			assertFalse(true);
		}
		
//		manager.stop();
	}

}
