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
package org.tinygroup.beanwrapper;

import java.beans.PropertyEditor;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.springutil.SpringBeanContainer;

public class BeanWrapperHolder {

	private BeanWrapperImpl beanWrapper;

	public BeanWrapperHolder() {
		beanWrapper = new BeanWrapperImpl();
		SpringBeanContainer container = (SpringBeanContainer) BeanContainerFactory
				.getBeanContainer(getClass().getClassLoader());
		if(container!=null){
			DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ((FileSystemXmlApplicationContext) container
					.getBeanContainerPrototype()).getBeanFactory();
			Map customEditors = beanFactory.getCustomEditors(); 
			Set keySet = customEditors.keySet();
			for (Object key : keySet) {
				Class requiredType = (Class) key;
				if (customEditors.get(requiredType) instanceof Class) {
					try {
						beanWrapper.registerCustomEditor(requiredType,
								(PropertyEditor) ((Class) customEditors
										.get(requiredType)).newInstance());
					} catch (Exception e) {
						throw new RuntimeException("注册客户自定义类型转换出现异常", e);
					}
				}
				if (customEditors.get(requiredType) instanceof PropertyEditor) {
					beanWrapper.registerCustomEditor(requiredType,
							(PropertyEditor) customEditors.get(requiredType));
				}
			}
		}
	}
	
	public BeanWrapperImpl getBeanWrapper(){
		return beanWrapper;
	}

}
