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

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.bundle.BundleManager;
import org.tinygroup.bundle.test.util.TestUtil;

public class LoaderTest extends TestCase {
	public void testStart() {
		TestUtil.init();
		BundleManager manager = BeanContainerFactory.getBeanContainer(
				this.getClass().getClassLoader()).getBean(
				BundleManager.BEAN_NAME);
		manager.start();
		try {
			manager.getTinyClassLoader("test1").loadClass(
					"org.tinygroup.MyTestInterface");
			Class<?> c = manager.getTinyClassLoader("test1").loadClass(
					"org.tinygroup.MyTestImpl");
			for (Method m : c.getMethods()) {
				if (m.getName().equals("test")) {

					m.invoke(c.newInstance());

				}
			}
			System.out.println(this.getClass().getClassLoader());

		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(true);
		}
		// manager.stop();
	}

}
