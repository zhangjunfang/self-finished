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

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.bundle.BundleException;
import org.tinygroup.bundle.BundleManager;
import org.tinygroup.bundle.config.BundleDefine;
import org.tinygroup.bundle.test.util.TestUtil;

public class BundleTest extends TestCase{
	public void setUp(){
		
	}
	
	public void testAddAndRemove(){
		TestUtil.init();
		BundleManager bundleManager = BeanContainerFactory.getBeanContainer(
				TestUtil.class.getClassLoader()).getBean(
				BundleManager.BEAN_NAME);
		BundleDefine d = new BundleDefine();
		d.setName("testJar");
		bundleManager.addBundleDefine(d);
		bundleManager.start("testJar");
		ClassLoader loader = null;
		loader = bundleManager.getTinyClassLoader(d);
		try {
			BeanContainerFactory.getBeanContainer(
					loader).getBean(
					"bundleTestService");
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
		try {
			bundleManager.removeBundle(bundleManager.getBundleDefine("testJar"));
		} catch (BundleException e) {
			e.printStackTrace();
		}
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
