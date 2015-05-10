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

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.tinygroup.database.config.dialectfunction.DialectFunctions;
import org.tinygroup.database.dialectfunction.DialectFunctionProcessor;
import org.tinygroup.database.dialectfunction.impl.DialectFunctionProcessorImpl;
import org.tinygroup.tinydb.config.BeanQueryConfig;
import org.tinygroup.tinydb.config.BeanQueryConfigs;
import org.tinygroup.tinydb.config.TableConfiguration;
import org.tinygroup.tinydb.config.TableConfigurationContainer;
import org.tinygroup.tinydb.convert.impl.MetadataTableConfigLoad;
import org.tinygroup.tinydb.dialect.Dialect;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.impl.DefaultNameConverter;
import org.tinygroup.tinydb.operator.impl.BeanStringOperator;
import org.tinygroup.tinydb.relation.Relation;
import org.tinygroup.tinydb.relation.Relations;
import org.tinygroup.tinydb.sql.SQLGenerateContainer;
import org.tinygroup.tinydb.sql.condition.ConditionGenerater;
import org.tinygroup.tinydb.sql.group.GroupGenerater;
import org.tinygroup.tinydb.sql.order.OrderGenerater;

/**
 * 配置对象
 * 
 * @author renhui
 * 
 */
public class Configuration {

	private String defaultDataSource;

	private String defaultSchema;

	private String operatorType = BeanStringOperator.class.getName();

	private DataSource useDataSource;

	private boolean increase;

	private Dialect dialect;

	private JdbcTemplate jdbcTemplate;

	private BeanDbNameConverter converter = new DefaultNameConverter();

	private DialectFunctionProcessor functionProcessor = new DialectFunctionProcessorImpl();

	private Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();

	private Map<String, Relation> relationIdMap = new HashMap<String, Relation>();

	private Map<String, Relation> relationTypeMap = new HashMap<String, Relation>();

	private TableConfigurationContainer container = new TableConfigurationContainer();

	private Map<String, BeanQueryConfig> beanQueryMap = new HashMap<String, BeanQueryConfig>();

	private SQLGenerateContainer sqlGenerateContainer=new SQLGenerateContainer();
	
	public Configuration() {
		sqlGenerateContainer.initContainer();
	}

	public String getDefaultDataSource() {
		return defaultDataSource;
	}

	public void setDefaultDataSource(String defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}

	public String getDefaultSchema() {
		return defaultSchema;
	}

	public void setDefaultSchema(String defaultSchema) {
		this.defaultSchema = defaultSchema;
	}

	public DataSource getUseDataSource() {
		return useDataSource;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	public void setUseDataSource(DataSource useDataSource) {
		this.useDataSource = useDataSource;
	}

	public void putDataSource(String dataSourceId, DataSource dataSource) {
		dataSourceMap.put(dataSourceId, dataSource);
	}

	public void removeDataSource(String dataSourceId) {
		dataSourceMap.remove(dataSourceId);
	}

	public DataSource getDataSource(String dataSourceId) {
		return dataSourceMap.get(dataSourceId);
	}

	public void addTableConfiguration(TableConfiguration tableConfiguration) {
		container.addTableConfiguration(tableConfiguration);
	}
	
	public void addTableConfiguration(String schema) throws TinyDbException {
		MetadataTableConfigLoad configLoad=new MetadataTableConfigLoad();
		configLoad.setSchema(schema);
		configLoad.setTableNamePattern("%");
		configLoad.loadTable(this);
	}

	public TableConfigurationContainer getContainer() {
		return container;
	}

	public Dialect getDialect() {
		return dialect;
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	public void addRelationConfigs(Relations relations) {
		for (Relation relation : relations.getRelations()) {
			relationIdMap.put(relation.getId(), relation);
			relationTypeMap.put(relation.getType(), relation);
		}
	}

	public void removeRelationConfigs(Relations relations) {
		for (Relation relation : relations.getRelations()) {
			relationIdMap.remove(relation.getId());
			relationTypeMap.remove(relation.getType());
		}
	}

	public void addBeanQueryConfigs(BeanQueryConfigs queryConfigs) {
		for (BeanQueryConfig queryConfig : queryConfigs.getQueryConfigs()) {
			beanQueryMap.put(queryConfig.getBeanType(), queryConfig);
		}
	}
	

	public void removeBeanQueryConfigs(BeanQueryConfigs queryConfigs) {
		for (BeanQueryConfig queryConfig : queryConfigs.getQueryConfigs()) {
			beanQueryMap.remove(queryConfig.getBeanType());
		}
	}

	public Relation getRelationById(String id) {
		return relationIdMap.get(id);
	}

	public Relation getRelationByBeanType(String beanType) {
		return relationTypeMap.get(beanType);
	}

	public BeanDbNameConverter getConverter() {
		return converter;
	}

	public void setConverter(BeanDbNameConverter converter) {
		this.converter = converter;
	}

	public boolean isIncrease() {
		return increase;
	}

	public void setIncrease(boolean increase) {
		this.increase = increase;
	}

	public DialectFunctionProcessor getFunctionProcessor() {
		return functionProcessor;
	}

	public void addDialectFunctions(DialectFunctions functions) {
		functionProcessor.addDialectFunctions(functions);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public BeanQueryConfig getBeanQueryConfig(String beanType) {
		return beanQueryMap.get(beanType);
	}

	public TableConfiguration getTableConfiguration(String beanType) {
		return getTableConfiguration(beanType, defaultSchema);
	}

	public TableConfiguration getTableConfiguration(String beanType,
			String schema) {
		String tableName = converter.typeNameToDbTableName(beanType);
		return container.getTableConfiguration(schema, tableName);
	}
	
	public ConditionGenerater getConditionGenerater(String conditionMode){
		return sqlGenerateContainer.getConditionGenerater(conditionMode);
	}
	
	public OrderGenerater getOrderGenerater(String orderMode){
		return sqlGenerateContainer.getOrderGenerater(orderMode);
	}
	public GroupGenerater getGroupGenerater(){
		return sqlGenerateContainer.getGroupGenerater();
	}
}
