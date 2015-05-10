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
package org.tinygroup.springutil.fileresolver;

import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.springutil.SpringBeanContainer;
import org.tinygroup.vfs.FileObject;

public class SpringBeansFileProcessor extends AbstractFileProcessor {

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(".springbeans.xml")
				|| fileObject.getFileName().endsWith(".beans.xml");
	}

	public void process() {
		SpringBeanContainer container = (SpringBeanContainer) BeanContainerFactory
				.getBeanContainer(this.getClass().getClassLoader());
		ClassLoader fileResolverLoader = getFileResolver().getClassLoader();
		if (fileResolverLoader == this.getClass().getClassLoader()) {
			dealMainContainer(container);
		} else {
			dealSubContainer(container, fileResolverLoader);
		}

	}
	private void dealMainContainer(SpringBeanContainer container){
		if (fileObjects.size() != 0) {
			container.regSpringConfigXml(fileObjects);
		}
		if (deleteList.size() != 0) {
			for (FileObject fileObject : deleteList) {
				container.removeUrl(fileObject.getURL().toString());
			}
		}
		container.refresh();
	}

	private void dealSubContainer(SpringBeanContainer container,
			ClassLoader fileResolverLoader) {
		BeanContainer<?> subContainer = container
				.getSubBeanContainer(fileResolverLoader);
		SpringBeanContainer springBeanContainer = null;
		if (subContainer == null) {
			// 这次肯定不会有deleteList的内容，因为是第一次
			subContainer = container.getSubBeanContainer(fileObjects,
					getFileResolver().getClassLoader());
		} else {
			// 不是第一次了，所有有deleteList的内容
			springBeanContainer = (SpringBeanContainer) subContainer;
			if (fileObjects.size() != 0) {
				springBeanContainer.regSpringConfigXml(fileObjects);
			}
			if (deleteList.size() != 0) {
				for (FileObject fileObject : deleteList) {
					springBeanContainer.removeUrl(fileObject.getURL()
							.toString());
				}
			}
			springBeanContainer.refresh();
		}
	}

	public boolean supportRefresh() {
		return true;
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}

}
