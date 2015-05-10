package org.tinygroup.springutil;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.parser.filter.PathFilter;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

public class ApplicationPropertyResourceConfigurer extends
		PropertyPlaceholderConfigurer {

	private Resource application;

	public void setApplication(Resource application) {
		this.application = application;
	}

	@Override
	protected Properties mergeProperties() throws IOException {
		if (application.exists()) {
			Properties properties = new Properties();
			// 应该有个应用配置加载对象,优先于spring容器启动
			String config = StreamUtil.readText(application.getInputStream(),
					"UTF-8", true);
			XmlNode applicationConfig = new XmlStringParser().parse(config)
					.getRoot();
			PathFilter<XmlNode> filter = new PathFilter<XmlNode>(
					applicationConfig);
			List<XmlNode> propertyList = filter
					.findNodeList("/application/application-properties/property");
			if(!CollectionUtil.isEmpty(propertyList)){
				for (XmlNode property : propertyList) {
					String name = property.getAttribute("name");
					String value = property.getAttribute("value");
					properties.put(name, value);
				}
			}
			return properties;
		}
		return super.mergeProperties();

	}

}
