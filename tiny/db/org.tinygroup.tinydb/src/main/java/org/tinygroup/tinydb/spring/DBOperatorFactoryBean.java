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
package org.tinygroup.tinydb.spring;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.tinygroup.commons.tools.ArrayUtil;
import org.tinygroup.commons.tools.Assert;
import org.tinygroup.database.config.dialectfunction.DialectFunctions;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinydb.Configuration;
import org.tinygroup.tinydb.ConfigurationBuilder;
import org.tinygroup.tinydb.DbOperatorFactory;
import org.tinygroup.tinydb.DbOperatorFactoryBuilder;
import org.tinygroup.tinydb.dialect.Dialect;
import org.tinygroup.tinydb.dialect.impl.AbstractDialect;
import org.tinygroup.tinydb.relation.Relations;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

/**
 * 创建DbOperatorFactory的spring factory bean
 * @author renhui
 *
 */
public class DBOperatorFactoryBean implements FactoryBean, InitializingBean {

	private Resource configLocation;

	private DbOperatorFactoryBuilder factoryBuilder = new DbOperatorFactoryBuilder();

	private Resource[] relationLocations;

	private Resource[] functionLocations;

	private DbOperatorFactory factory;

	private DataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;

	private Dialect dialect;

	private Logger logger = LoggerFactory
			.getLogger(DBOperatorFactoryBean.class);

	public Resource getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	public Resource[] getRelationLocations() {
		return relationLocations;
	}

	public void setRelationLocations(Resource[] relationLocations) {
		this.relationLocations = relationLocations;
	}

	public Resource[] getFunctionLocations() {
		return functionLocations;
	}

	public void setFunctionLocations(Resource[] functionLocations) {
		this.functionLocations = functionLocations;
	}

	public DbOperatorFactory getFactory() {
		return factory;
	}

	public void setFactory(DbOperatorFactory factory) {
		this.factory = factory;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public DbOperatorFactoryBuilder getFactoryBuilder() {
		return factoryBuilder;
	}

	public void setFactoryBuilder(DbOperatorFactoryBuilder factoryBuilder) {
		this.factoryBuilder = factoryBuilder;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setDataSource(DataSource dataSource) {
		if (dataSource instanceof TransactionAwareDataSourceProxy) {
			// If we got a TransactionAwareDataSourceProxy, we need to perform
			// transactions for its underlying target DataSource, else data
			// access code won't see properly exposed transactions (i.e.
			// transactions for the target DataSource).
			this.dataSource = ((TransactionAwareDataSourceProxy) dataSource)
					.getTargetDataSource();
		} else {
			this.dataSource = dataSource;
		}
	}

	public Dialect getDialect() {
		return dialect;
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.assertNotNull(dataSource, "dataSource is required");
		Assert.assertNotNull(factoryBuilder, "factoryBuilder is required");
		factory = buildDBOperatorFactory();
	}

	private DbOperatorFactory buildDBOperatorFactory() throws IOException {
		Configuration configuration = new Configuration();
		ConfigurationBuilder configBuilder = null;
		if (configLocation != null) {
			configBuilder = new ConfigurationBuilder(
					configLocation.getInputStream());
			configuration = configBuilder.getConfiguration();
		}
//		boolean isDynamic = dataSource instanceof DynamicDataSource;
//		DataSource dynamicDataSource = dataSource;
//		if (!isDynamic) {
//			logger.logMessage(LogLevel.DEBUG,
//					"datasource 不是DynamicDataSource类型，需要包装成DynamicDataSource");
//			DynamicDataSource newDynamic = new DynamicDataSource();
//			newDynamic.setDataSource(dataSource);
//			dynamicDataSource = newDynamic;
//		}
//		configuration.setUseDataSource(dynamicDataSource);
		configuration.setUseDataSource(dataSource);
		if (dialect != null) {
			logger.logMessage(LogLevel.DEBUG, "设置数据库dialect");
			configuration.setDialect(dialect);
		}
		if(jdbcTemplate!=null){
			configuration.setJdbcTemplate(jdbcTemplate);
		}
		if (!ArrayUtil.isEmptyArray(relationLocations)) {
			for (Resource relationLocation : relationLocations) {
				logger.logMessage(LogLevel.DEBUG, "加载relation-config:[{0}]",
						relationLocation.getURL().toString());
				addRelationConfig(configuration, relationLocation);
			}
		}
		if (!ArrayUtil.isEmptyArray(functionLocations)) {
			for (Resource functionLocation : functionLocations) {
				logger.logMessage(LogLevel.DEBUG, "加载function-config:[{0}]",
						functionLocation.getURL().toString());
				addDialectFunctions(configuration, functionLocation);
			}
			AbstractDialect dialect = (AbstractDialect) configuration
					.getDialect();
			dialect.setFunctionProcessor(configuration.getFunctionProcessor());
		}
		if (configBuilder != null) {
			logger.logMessage(LogLevel.DEBUG, "开始解析tinydb配置文件");
			configuration = configBuilder.parser();
			logger.logMessage(LogLevel.DEBUG, "解析tinydb配置文件结束");
		}
		return factoryBuilder.build(configuration);
	}

	private void addDialectFunctions(Configuration configuration,
			Resource functionLocation) throws IOException {
		XStream stream = XStreamFactory.getXStream();
		stream.processAnnotations(DialectFunctions.class);
		DialectFunctions functions = (DialectFunctions) stream
				.fromXML(functionLocation.getInputStream());
		configuration.addDialectFunctions(functions);
	}

	private void addRelationConfig(Configuration configuration,
			Resource relationLocation) throws IOException {
		XStream stream = XStreamFactory.getXStream();
		stream.processAnnotations(Relations.class);
		Relations relations = (Relations) stream.fromXML(relationLocation
				.getInputStream());
		configuration.addRelationConfigs(relations);
	}

	public Object getObject() throws Exception {
		if (factory == null) {
			afterPropertiesSet();
		}
		return factory;
	}

	public Class getObjectType() {
		return factory == null ? DbOperatorFactory.class : factory.getClass();
	}

	public boolean isSingleton() {
		return true;
	}

}
