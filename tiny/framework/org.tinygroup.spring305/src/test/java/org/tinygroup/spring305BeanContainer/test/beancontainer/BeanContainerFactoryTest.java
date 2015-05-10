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
package org.tinygroup.spring305BeanContainer.test.beancontainer;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.spring305BeanContainer.SpringBeanContainer;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

public class BeanContainerFactoryTest extends TestCase {
	public void testFactory() {
		BeanContainerFactory
				.setBeanContainer("org.tinygroup.spring305BeanContainer.SpringBeanContainer");
		BeanContainer<ApplicationContext> container = (BeanContainer<ApplicationContext>) BeanContainerFactory
				.getBeanContainer();
		assertNotNull(container);
		FileObject f = VFS.resolveURL(this.getClass().getClassLoader()
				.getResource("beancontainer.beans.xml"));
		List<FileObject> fl = new ArrayList<FileObject>();
		fl.add(f);
		ApplicationContext app = container.getSubBeanContainer(fl, this
				.getClass().getClassLoader());
		assertNotNull(app);
		assertEquals(1, container.getSubBeanContainers().size());

	}
	
	public void testFactoryGetContainer() {
		BeanContainerFactory
				.setBeanContainer("org.tinygroup.spring305BeanContainer.SpringBeanContainer");
		BeanContainer<ApplicationContext> container = (BeanContainer<ApplicationContext>) BeanContainerFactory
				.getBeanContainer();
		assertNotNull(container);
		FileObject f = VFS.resolveURL(this.getClass().getClassLoader()
				.getResource("beancontainer.beans.xml"));
		SpringBeanContainer spring = (SpringBeanContainer)container;
		List<FileObject> fl = new ArrayList<FileObject>();
		fl.add(f);
		spring.regSpringConfigXml(fl);
		spring.refresh();
		ContainerBean bean = spring.getBean("containerbean");
		assertNotNull(bean);

	}
}
