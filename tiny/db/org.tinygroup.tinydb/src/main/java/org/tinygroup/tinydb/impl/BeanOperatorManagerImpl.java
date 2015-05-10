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
package org.tinygroup.tinydb.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.tinydb.BeanDbNameConverter;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.Configuration;
import org.tinygroup.tinydb.DbOperatorFactory;
import org.tinygroup.tinydb.config.TableConfiguration;
import org.tinygroup.tinydb.config.TableConfigurationContainer;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.operator.DBOperator;
import org.tinygroup.tinydb.relation.Relation;

/**
 * bean操作管理器接口的实现
 * 
 * @author renhui
 * 
 */
public class BeanOperatorManagerImpl implements BeanOperatorManager,InitializingBean {

	private String mainSchema;

	private Configuration configuration;

	private BeanDbNameConverter beanDbNameConverter;

	private TableConfigurationContainer container;
	
	public BeanOperatorManagerImpl(){
		
	}

	public BeanOperatorManagerImpl(Configuration configuration) {
		this.configuration = configuration;
		container = configuration.getContainer();
		beanDbNameConverter = configuration.getConverter();
		mainSchema = configuration.getDefaultSchema();
	}

	public DBOperator<?> getDbOperator(String schema) throws TinyDbException {
		String realSchema = getRealSchema(schema);
		try {
			//DBOperator operator = (DBOperator) Class.forName(configuration.getOperatorType()).newInstance();
			Class<?> clazz = Class.forName(configuration.getOperatorType());
			DBOperator operator = (DBOperator) BeanContainerFactory.getBeanContainer(clazz.getClassLoader()).getBean(clazz);
			JdbcTemplate jdbcTemplate=configuration.getJdbcTemplate();
			if(jdbcTemplate==null){
				jdbcTemplate=new JdbcTemplate(configuration.getUseDataSource());
			}
			operator.setJdbcTemplate(jdbcTemplate);
			operator.setConfiguration(configuration);
			operator.setSchema(realSchema);
			operator.setDialect(configuration.getDialect());
			operator.setBeanDbNameConverter(beanDbNameConverter);
			operator.setManager(this);
			return operator;
		} catch (Exception e) {
			throw new TinyDbException("获取dboperator实例出现异常", e);
		}
	}

	public DBOperator<?> getDbOperator() throws TinyDbException {
		return getDbOperator(mainSchema);
	}

	public DBOperator<?> getNewDbOperator() throws TinyDbException {
		return getNewDbOperator(mainSchema);
	}

	public DBOperator<?> getNewDbOperator(String schema) throws TinyDbException {
		DBOperator operator=getDbOperator(schema);
		operator.setTransactionDefinition(new DefaultTransactionDefinition(
				TransactionDefinition.PROPAGATION_REQUIRES_NEW));// 事务传播特性是REQUIRES_NEW,不管之前是不是事务存在，总是新开启事务
		return operator;
	}

	public TableConfiguration getTableConfiguration(String beanType,
			String schema) {
		String tableName = beanDbNameConverter.typeNameToDbTableName(beanType);
		return container
				.getTableConfiguration(getRealSchema(schema), tableName);
	}

	public TableConfiguration getTableConfiguration(String beanType) {
		return getTableConfiguration(beanType, mainSchema);
	}

	public TableConfigurationContainer getTableConfigurationContainer() {
		return container;
	}

	public Relation getRelationById(String id) {
		return configuration.getRelationById(id);
	}

	public Relation getRelationByBeanType(String beanType) {
		return configuration.getRelationByBeanType(beanType);
	}

	public boolean existsTableByType(String beanType, String schema) {
		String tableName = beanDbNameConverter.typeNameToDbTableName(beanType);
		String realSchema = getRealSchema(schema);
		TableConfiguration configuration = container.getTableConfiguration(
				realSchema, tableName);
		return configuration != null;
	}

	public BeanDbNameConverter getBeanDbNameConverter() {
		return beanDbNameConverter;
	}

	public String getRealSchema(String schema) {
		if (StringUtil.isBlank(schema)) {
			return mainSchema;
		}
		return schema;
	}

	public String getMainSchema() {
		return mainSchema;
	}

	public void afterPropertiesSet() throws Exception {
		if(configuration==null){
			DbOperatorFactory factory = BeanContainerFactory.getBeanContainer(
					getClass().getClassLoader()).getBean(DbOperatorFactory.class);
			configuration=factory.getConfiguration();
		}
		container = configuration.getContainer();
		beanDbNameConverter = configuration.getConverter();
		mainSchema = configuration.getDefaultSchema();
	}

}
