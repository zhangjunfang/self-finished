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
package org.tinygroup.tinydb;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.database.config.dialectfunction.DialectFunctions;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinydb.config.BeanQueryConfigs;
import org.tinygroup.tinydb.convert.TableConfigLoad;
import org.tinygroup.tinydb.dialect.Dialect;
import org.tinygroup.tinydb.dialect.impl.AbstractDialect;
import org.tinygroup.tinydb.exception.TinyDbRuntimeException;
import org.tinygroup.tinydb.relation.Relations;
import org.tinygroup.tinydb.util.TinyDBUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class ConfigurationBuilder {
	private static final String DIALECT_FUNCTION = "dialect-function";
	private static final String DIALECT_FUNCTIONS = "dialect-functions";
	private static final String CONVERT_TYPE = "convert-type";
	private static final String RELATION = "relation";
	private static final String RELATIONS = "relations";
	private static final String DIALECT = "dialect";
	private static final String TABLE_LOADS = "table-loads";
	private static final String DATA_SOURCE_ID = "id";
	private static final String PROPERTY = "property";
	private static final String TYPE = "type";
	private static final String DEFAULT = "default";
	private static final String DATASOURCES = "dataSources";
	private static final String QUERY_CONFIGS = "bean-query-configs";
	private static final String QUERY_CONFIG = "bean-query-config";
	
	private boolean parsed;
	private XmlNode xmlNode;
	private String dataSource;
	private final Configuration configuration;
	private Logger logger = LoggerFactory.getLogger(ConfigurationBuilder.class);

	public ConfigurationBuilder(InputStream inputStream) {
		this(inputStream, null);
	}

	public ConfigurationBuilder(InputStream inputStream, String dataSource) {
		try {
			XmlStringParser parser = new XmlStringParser();
			String configXml = StreamUtil.readText(inputStream, "UTF-8", true);
			xmlNode = parser.parse(configXml).getRoot();
		} catch (IOException e) {
			logger.errorMessage("载入数据库配置信息时出现异常，错误原因：{}！", e, e.getMessage());
		}
		this.parsed = false;
		this.dataSource = dataSource;
		this.configuration = new Configuration();
	}

	public ConfigurationBuilder(Reader reader) {
		this(reader, null);
	}

	public ConfigurationBuilder(Reader reader, String dataSource) {
		try {
			XmlStringParser parser = new XmlStringParser();
			String configXml = StreamUtil.readText(reader, true);
			xmlNode = parser.parse(configXml).getRoot();
		} catch (IOException e) {
			logger.errorMessage("载入数据库配置信息时出现异常，错误原因：{}！", e, e.getMessage());
		}
		this.parsed = false;
		this.dataSource = dataSource;
		this.configuration = new Configuration();
	}

	public ConfigurationBuilder(XmlNode xmlNode, String dataSource) {
		this.xmlNode = xmlNode;
		this.parsed = false;
		this.dataSource = dataSource;
		this.configuration = new Configuration();
	}

	public Configuration parser() {
		if (parsed) {
			throw new TinyDbRuntimeException(
					"Each ConfigurationBuilder can only be used once.");
		}
		parsed = true;
		parserOperator();
		parserDataSources();
		parserTableLoad();
		parserDialect();
		parserRelations();
		parserDialectFunctions();
		parserBeanQueryConfig();
		return configuration;
	}

	private void parserBeanQueryConfig() {
		XmlNode qeuryConfigsNode = xmlNode.getSubNode(QUERY_CONFIGS);
		if (qeuryConfigsNode == null) {
			return;
		}
		List<XmlNode> queryConfigNodes = qeuryConfigsNode.getSubNodes(QUERY_CONFIG);
		if (!CollectionUtil.isEmpty(queryConfigNodes)) {
			for (XmlNode queryConfigNode : queryConfigNodes) {
				String url = queryConfigNode.getAttribute("url");
				String resource = queryConfigNode.getAttribute("resource");
				XStream stream = XStreamFactory.getXStream();
				stream.processAnnotations(BeanQueryConfigs.class);
				if (url != null && resource == null) {
					FileObject fileObject = VFS.resolveFile(url);
					BeanQueryConfigs queryConfigs = (BeanQueryConfigs) stream.fromXML(fileObject
							.getInputStream());
					configuration.addBeanQueryConfigs(queryConfigs);
				} else if (url == null && resource != null) {
					InputStream inputStream = getClass().getClassLoader()
							.getResourceAsStream(resource);
					BeanQueryConfigs queryConfigs = (BeanQueryConfigs) stream
							.fromXML(inputStream);
					configuration.addBeanQueryConfigs(queryConfigs);
				} else {
					throw new TinyDbRuntimeException(
							"bean-query-config元素的属性只能是url或者是resource");
				}
			}
		}
	}

	private void parserDialectFunctions() {
		XmlNode functionsNode = xmlNode.getSubNode(DIALECT_FUNCTIONS);
		if (functionsNode == null) {
			return;
		}
		List<XmlNode> functionNodes = functionsNode
				.getSubNodes(DIALECT_FUNCTION);
		if (!CollectionUtil.isEmpty(functionNodes)) {
			for (XmlNode node : functionNodes) {
				String url = node.getAttribute("url");
				String resource = node.getAttribute("resource");
				XStream stream = XStreamFactory.getXStream();
				stream.processAnnotations(DialectFunctions.class);
				if (url != null && resource == null) {
					FileObject fileObject = VFS.resolveFile(url);
					DialectFunctions functions = (DialectFunctions) stream
							.fromXML(fileObject.getInputStream());
					configuration.addDialectFunctions(functions);
				} else if (url == null && resource != null) {
					InputStream inputStream = getClass().getClassLoader()
							.getResourceAsStream(resource);
					DialectFunctions functions = (DialectFunctions) stream
							.fromXML(inputStream);
					configuration.addDialectFunctions(functions);
				} else {
					throw new TinyDbRuntimeException(
							"relation元素的属性只能是url或者是resource");
				}
			}
		}

	}

	private void parserRelations() {
		XmlNode relationsNode = xmlNode.getSubNode(RELATIONS);
		if (relationsNode == null) {
			return;
		}
		List<XmlNode> relationNodes = relationsNode.getSubNodes(RELATION);
		if (!CollectionUtil.isEmpty(relationNodes)) {
			for (XmlNode relationNode : relationNodes) {
				String url = relationNode.getAttribute("url");
				String resource = relationNode.getAttribute("resource");
				XStream stream = XStreamFactory.getXStream();
				stream.processAnnotations(Relations.class);
				if (url != null && resource == null) {
					FileObject fileObject = VFS.resolveFile(url);
					Relations relations = (Relations) stream.fromXML(fileObject
							.getInputStream());
					configuration.addRelationConfigs(relations);
				} else if (url == null && resource != null) {
					InputStream inputStream = getClass().getClassLoader()
							.getResourceAsStream(resource);
					Relations relations = (Relations) stream
							.fromXML(inputStream);
					configuration.addRelationConfigs(relations);
				} else {
					throw new TinyDbRuntimeException(
							"relation元素的属性只能是url或者是resource");
				}
			}
		}

	}

	private void parserDialect() {
		XmlNode dialectNode = xmlNode.getSubNode(DIALECT);
		if (dialectNode == null) {
			return;
		}
		String dialectType = dialectNode.getAttribute(TYPE);
		Dialect dialect = (Dialect) newInstance(dialectType);
		setProperties(dialectNode, dialect);
		AbstractDialect abstractDialect = (AbstractDialect) dialect;
		abstractDialect.setFunctionProcessor(configuration
				.getFunctionProcessor());
		abstractDialect.setDataSource(configuration.getUseDataSource());
		configuration.setDialect(abstractDialect);
	}

	private Object newInstance(String type) {
		try {
			Class clazz = Class.forName(type);
			return clazz.newInstance();
		} catch (Exception e) {
			throw new TinyDbRuntimeException("对象实例化出现异常", e);
		}
	}

	private void parserTableLoad() {
		XmlNode loadsNode = xmlNode.getSubNode(TABLE_LOADS);
		if (loadsNode == null) {
			return;
		}
		setDefaultSchema(loadsNode);
		List<XmlNode> loadNodes = loadsNode.getSubNodes("table-load");
		if (!CollectionUtil.isEmpty(loadNodes)) {
			for (XmlNode node : loadNodes) {
				try {
					TableConfigLoad load = newTableConfigLoad(node);
					setProperties(node, load);
					load.loadTable(configuration);
				} catch (Exception e) {
					throw new TinyDbRuntimeException("解析表配置出现异常", e);
				}
			}
		}
	}

	private TableConfigLoad newTableConfigLoad(XmlNode node) throws Exception {
		String type = node.getAttribute(TYPE);
		TableConfigLoad load = (TableConfigLoad) newInstance(type);
		return load;
	}

	private void setDefaultSchema(XmlNode loadsNode) {
		String defaultSchema = loadsNode.getAttribute(DEFAULT);
		if (StringUtil.isBlank(defaultSchema)) {
			throw new TinyDbRuntimeException("未设置默认的schema");
		}
		configuration.setDefaultSchema(defaultSchema);
	}

	private void parserOperator() {
		String operatorType = xmlNode.getAttribute(TYPE);
		configuration.setOperatorType(operatorType);
		String convertType = xmlNode.getAttribute(CONVERT_TYPE);
		if (!StringUtil.isBlank(convertType)) {
			BeanDbNameConverter converter = (BeanDbNameConverter) newInstance(convertType);
			configuration.setConverter(converter);
		}
		String increase = xmlNode.getAttribute("is-increase");
		if (!StringUtil.isBlank(increase)) {
			configuration.setIncrease(Boolean.parseBoolean(increase));
		}
	}

	private void parserDataSources() {
		XmlNode dataSources = xmlNode.getSubNode(DATASOURCES);
		if (dataSources == null) {
			return;
		}
		String defaultDataSource = dataSources.getAttribute(DEFAULT);
		if (dataSource == null) {
			dataSource = defaultDataSource;
		}
		if (dataSource == null) {
			throw new TinyDbRuntimeException("默认数据源不能为空");
		}
		configuration.setDefaultDataSource(defaultDataSource);
		List<XmlNode> nodes = dataSources.getSubNodes("dataSource");
		if (!CollectionUtil.isEmpty(nodes)) {
			boolean existDefault = false;
			for (XmlNode node : nodes) {
				if (parserDataSource(node)) {
					existDefault = true;
				}
			}
			if (!existDefault) {
				throw new TinyDbRuntimeException("默认数据源:" + dataSource + "没有设置");
			}
			JdbcTemplate template = new JdbcTemplate(
					configuration.getUseDataSource());
			configuration.setJdbcTemplate(template);
		}
	}

	private boolean parserDataSource(XmlNode node) {
		if (node == null) {
			return false;
		}
		boolean isDefault = false;
		String type = node.getAttribute(TYPE);
		try {
			DataSource newDataSource = newDataSourceInstance(type);
			String dataSourceId = node.getAttribute(DATA_SOURCE_ID);
			setProperties(node, newDataSource);
			// DynamicDataSource dynamic=new DynamicDataSource();
			// dynamic.setDataSource(newDataSource);
			// if(dataSourceId.equals(dataSource)){
			// configuration.setUseDataSource(dynamic);
			// isDefault=true;
			// }
			// configuration.putDataSource(type, dynamic);
			if (dataSourceId.equals(dataSource)) {
				configuration.setUseDataSource(newDataSource);
				isDefault = true;
			}
			configuration.putDataSource(type, newDataSource);
		} catch (Exception e) {
			throw new TinyDbRuntimeException("解析数据源配置出现异常", e);
		}
		return isDefault;

	}

	private void setProperties(XmlNode node, Object object) {
		Map<String, String> properties = new HashMap<String, String>();
		List<XmlNode> subNodes = node.getSubNodes(PROPERTY);
		if (!CollectionUtil.isEmpty(subNodes)) {
			for (XmlNode subNode : subNodes) {
				properties.put(subNode.getAttribute("name"),
						subNode.getAttribute("value"));
			}
		}
		TinyDBUtil.setProperties(object, properties);
	}

	private DataSource newDataSourceInstance(String type)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		Class dataSourceType = Class.forName(type);
		DataSource dataSource = (DataSource) dataSourceType.newInstance();
		return dataSource;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

}
