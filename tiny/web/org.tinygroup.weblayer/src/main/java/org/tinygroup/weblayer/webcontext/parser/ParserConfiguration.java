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
package org.tinygroup.weblayer.webcontext.parser;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.InitializingBean;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.beanwrapper.BeanWrapperHolder;
import org.tinygroup.beanwrapper.BeanWrapperImpl;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.weblayer.webcontext.parser.upload.ParameterParserFilter;
import org.tinygroup.weblayer.webcontext.parser.upload.UploadService;
import org.tinygroup.xmlparser.node.XmlNode;

public class ParserConfiguration extends AbstractConfiguration {

	private static final String PARSER_CONFIG="/application/parser";
	private static final String PROPERTY_EDITOR = "property-editor";
	private static final String PARAM_PARSER_FILTER = "param-parser-filter";
	private static final String UPLOAD_SERVICE = "upload-service";
	private static final String BEAN_NAME = "bean-name";
	private static final String PROPERTY = "property";
	private BeanWrapperImpl beanWrapper;
	private PropertyEditorRegistrar[] propertyEditors;
	private ParameterParserFilter[] parserFilters;
	private UploadService uploadService;
	public String getApplicationNodePath() {
		return PARSER_CONFIG;
	}

	public String getComponentConfigPath() {
		return null;
	}

	@Override
	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		super.config(applicationConfig, componentConfig);
		if(applicationConfig!=null){
			BeanWrapperHolder holder=new BeanWrapperHolder();
			beanWrapper=holder.getBeanWrapper();
			resolvePropertyEditors(applicationConfig);
			resolverParserFilters(applicationConfig);
			resolverUploadService(applicationConfig);
		}
	}
	
	public PropertyEditorRegistrar[] getPropertyEditors() {
		return propertyEditors;
	}

	public ParameterParserFilter[] getParserFilters() {
		return parserFilters;
	}

	public UploadService getUploadService() {
		return uploadService;
	}

	private void resolverUploadService(XmlNode applicationConfig) {
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(applicationConfig);
		XmlNode subNode = nameFilter.findNode(UPLOAD_SERVICE);
		if(subNode!=null){
			uploadService=(UploadService) wrapperObject(subNode);
		}
	}

	private void resolverParserFilters(XmlNode applicationConfig) {
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(applicationConfig);
		List<XmlNode> subNodes = nameFilter.findNodeList(PARAM_PARSER_FILTER);
		if (!CollectionUtil.isEmpty(subNodes)) {
			parserFilters = (ParameterParserFilter[]) Array.newInstance(ParameterParserFilter.class, subNodes.size());
			for (int i = 0; i < subNodes.size(); i++) {
				XmlNode xmlNode = subNodes.get(i);
				String beanName=xmlNode.getAttribute(BEAN_NAME);
				ParameterParserFilter parserFilter=BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBean(beanName);
				parserFilters[i] = parserFilter;
			}
		}
	}

	private void resolvePropertyEditors(XmlNode applicationConfig) {
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(applicationConfig);
		List<XmlNode> subNodes = nameFilter.findNodeList(PROPERTY_EDITOR);
		if (!CollectionUtil.isEmpty(subNodes)) {
			propertyEditors = (PropertyEditorRegistrar[]) Array.newInstance(PropertyEditorRegistrar.class, subNodes.size());
			for (int i = 0; i < subNodes.size(); i++) {
				XmlNode xmlNode = subNodes.get(i);
				propertyEditors[i]=(PropertyEditorRegistrar)wrapperObject(xmlNode);
			}
		}
	}

	private Object wrapperObject(XmlNode xmlNode) {
		String beanName=xmlNode.getAttribute(BEAN_NAME);
		Object object=BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBean(beanName);
		Map<String, String> properties = CollectionUtil.createHashMap();
		NameFilter<XmlNode> propertyFilter = new NameFilter<XmlNode>(xmlNode);
		List<XmlNode> subNodes = propertyFilter.findNodeList(PROPERTY);
		for (XmlNode subNode : subNodes) {
			String value = subNode.getAttribute("value");
			if (value == null) {
				value = subNode.getContent();
			}
			properties.put(subNode.getAttribute("name"), value);
		}
		setAttribute(object, properties);
		return object;
	}
	
	private  void setAttribute(Object object, Map<String, String> properties) {
		beanWrapper.setWrappedInstance(object);
		for (String attribute : properties.keySet()) {
			try {
				String value = properties.get(attribute);
				beanWrapper.setPropertyValue(attribute, value);
			} catch (Exception e) {
				throw new RuntimeException("设置对象属性出现异常", e);
			}
		}
		if (object instanceof InitializingBean) {
			try {
				((InitializingBean) object).afterPropertiesSet();
			} catch (Exception e) {
				throw new RuntimeException("initializingBean error", e);
			}
		}
	}

}
