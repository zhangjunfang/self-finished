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
package org.tinygroup.tinydb.operator.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.Configuration;
import org.tinygroup.tinydb.config.ColumnConfiguration;
import org.tinygroup.tinydb.config.TableConfiguration;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.operator.DbBaseOperator;
import org.tinygroup.tinydb.relation.Relation;

class BeanDBBaseOperator extends DBSpringBaseOperator implements DbBaseOperator {

	protected BeanOperatorManager manager;

	public BeanDBBaseOperator() {
		super();
	}

	public BeanDBBaseOperator(JdbcTemplate jdbcTemplate,
			Configuration configuration) {
		super(jdbcTemplate, configuration);
	}

	public int getAutoIncreaseKey() throws TinyDbException {
		try {
			return getDialect().getNextKey();
		} catch (DataAccessException e) {
			throw new TinyDbException(e.getRootCause());
		}
	}

	protected String getDeleteSqlByKey(String beanType) throws TinyDbException {
		String tableName = getFullTableName(beanType);
		StringBuffer sb = new StringBuffer();
		StringBuffer condition = new StringBuffer();
		sb.append("delete from ").append(tableName);
		TableConfiguration table = manager.getTableConfiguration(beanType,
				getSchema());
		String pk = table.getPrimaryKey().getColumnName();
		condition.append(pk).append("=?");
		sb.append(" where ").append(condition);
		return sb.toString();
	}

	/**
	 * 获取操作的所有数据库字段名称
	 * 
	 * @param bean
	 * @return
	 */
	protected List<String> getColumnNames(Bean bean) {
		TableConfiguration table = manager.getTableConfiguration(
				bean.getType(), getSchema());
		List<String> insertColumnNames = new ArrayList<String>();
		for (ColumnConfiguration column : table.getColumns()) {
			String columnsName = column.getColumnName();
			String propertyName = beanDbNameConverter
					.dbFieldNameToPropertyName(columnsName);
			if (bean.containsKey(propertyName)) {
				insertColumnNames.add(columnsName);
			}
		}
		return insertColumnNames;
	}

	/**
	 * 获取操作的所有数据库字段类型
	 * 
	 * @param bean
	 * @return
	 */
	protected List<Integer> getDataTypes(Bean bean) {
		TableConfiguration table = manager.getTableConfiguration(
				bean.getType(), getSchema());
		List<Integer> dataTypes = new ArrayList<Integer>();
		for (ColumnConfiguration column : table.getColumns()) {
			String columnsName = column.getColumnName();
			String propertyName = beanDbNameConverter
					.dbFieldNameToPropertyName(columnsName);
			if (bean.containsKey(propertyName)) {
				dataTypes.add(column.getDataType());
			}
		}
		return dataTypes;
	}

	protected SqlParameterValue[] getSqlParameterValues(Bean bean) {
		TableConfiguration table = manager.getTableConfiguration(
				bean.getType(), getSchema());
		List<ColumnConfiguration> columns = table.getColumns();
		List<SqlParameterValue> params = new ArrayList<SqlParameterValue>();
		for (ColumnConfiguration column : columns) {
			String columnsName = column.getColumnName();
			String propertyName = beanDbNameConverter
					.dbFieldNameToPropertyName(columnsName);
			if (bean.containsKey(propertyName)) {
				params.add(createSqlParamter(bean.getProperty(propertyName),
						column));
			}
		}
		return params.toArray(new SqlParameterValue[0]);
	}

	protected List<Integer> getDataTypes(List<ColumnConfiguration> paramsKeys) {
		List<Integer> dataTypes = new ArrayList<Integer>();
		for (ColumnConfiguration column : paramsKeys) {
			dataTypes.add(column.getDataType());
		}
		return dataTypes;
	}

	protected SqlParameterValue createSqlParamter(Object value,
			ColumnConfiguration primaryColumn) {
		String columnsName = primaryColumn.getColumnName();
		String propertyName = beanDbNameConverter
				.dbFieldNameToPropertyName(columnsName);
		String scaleStr = primaryColumn.getDecimalDigits();
		int scale = 0;
		if (scaleStr != null) {
			scale = Integer.parseInt(scaleStr);
		}
		SqlParameter sqlParameter = new SqlParameter(propertyName,
				primaryColumn.getDataType(), scale);
		return new SqlParameterValue(sqlParameter, value);
	}

	protected SqlParameterValue[] getSqlParamterValue(Bean bean,
			List<String> conditionColumns) throws TinyDbException {
		List<SqlParameterValue> params = new ArrayList<SqlParameterValue>();
		if (conditionColumns == null || conditionColumns.size() == 0) {
			throw new TinyDbException("beanType为:" + bean.getType()
					+ "不存在查询条件信息");
		}
		TableConfiguration table = manager.getTableConfiguration(
				bean.getType(), getSchema());
		if (table != null) {
			List<ColumnConfiguration> columns = table.getColumns();
			// 设置更新字段参数
			setUpdateParamter(bean, params, columns, conditionColumns);
			// 设置条件字段参数
			setConditionParamter(bean, params, conditionColumns);
			return params.toArray(new SqlParameterValue[0]);
		}
		throw new TinyDbException("不存在beanType：" + bean.getType() + "的表格");

	}

	private void setUpdateParamter(Bean bean, List<SqlParameterValue> params,
			List<ColumnConfiguration> columns, List<String> conditionColumns) {
		for (ColumnConfiguration column : columns) {
			String columnName = column.getColumnName();
			if (!column.isPrimaryKey()) {
				String propertyName = beanDbNameConverter
						.dbFieldNameToPropertyName(columnName);
				// 如果说不是条件字段
				if (!conditionColumns.contains(columnName)) {
					if (bean.containsKey(propertyName)
							&& bean.getMark(propertyName)) {
						params.add(createSqlParamter(
								bean.getProperty(propertyName), column));
					}
				}
			}
		}

	}

	private void setConditionParamter(Bean bean,
			List<SqlParameterValue> params, List<String> conditionColumns) {
		for (String columnName : conditionColumns) {
			String propertyName = beanDbNameConverter
					.dbFieldNameToPropertyName(columnName);
			if (bean != null && !checkBeanPropertyNull(bean, columnName)) {
				TableConfiguration table = manager.getTableConfiguration(
						bean.getType(), getSchema());
				ColumnConfiguration column = table.getColumn(columnName);
				params.add(createSqlParamter(bean.getProperty(propertyName),
						column));
			}
		}
	}

	protected List<List<Object>> getParamList(Bean[] beans,
			List<ColumnConfiguration> paramsKeys) {
		List<List<Object>> paramsList = new ArrayList<List<Object>>();
		for (Bean bean : beans) {
			List<Object> params = new ArrayList<Object>();
			for (int i = 0; i < paramsKeys.size(); i++) {
				ColumnConfiguration column = paramsKeys.get(i);
				String propertyName = beanDbNameConverter
						.dbFieldNameToPropertyName(column.getColumnName());
				params.add(bean.get(propertyName));

			}
			paramsList.add(params);
		}
		return paramsList;
	}

	protected List<Object> getParam(Bean bean,
			List<ColumnConfiguration> paramsKeys) {
		List<Object> params = new ArrayList<Object>();
		for (int i = 0; i < paramsKeys.size(); i++) {
			ColumnConfiguration column = paramsKeys.get(i);
			String propertyName = beanDbNameConverter
					.dbFieldNameToPropertyName(column.getColumnName());
			params.add(bean.get(propertyName));
		}
		return params;
	}

	protected SqlParameterValue[] createSqlParameterValue(Bean bean)
			throws TinyDbException {
		TableConfiguration table = manager.getTableConfiguration(
				bean.getType(), getSchema());
		if (table != null) {
			List<ColumnConfiguration> columns = table.getColumns();
			if (columns != null && columns.size() > 0) {
				List<SqlParameterValue> parameterValues = new ArrayList<SqlParameterValue>();
				boolean isIncrease = configuration.isIncrease();
				for (ColumnConfiguration column : columns) {
					String columnsName = column.getColumnName();
					String propertyName = beanDbNameConverter
							.dbFieldNameToPropertyName(columnsName);
					Object value = null;
					if (column.isPrimaryKey()) {// 主键值自动生成
						value = getPrimaryKeyValue(bean, isIncrease,
								propertyName);
						parameterValues.add(createSqlParamter(value, column));
					} else {
						if (bean.containsKey(propertyName)) {
							value = bean.getProperty(propertyName);
							parameterValues
									.add(createSqlParamter(value, column));
						}
					}
				}
				return parameterValues.toArray(new SqlParameterValue[0]);

			}

		}
		throw new TinyDbException("不存在beanType：" + bean.getType() + "的表格");

	}

	private Object getPrimaryKeyValue(Bean bean, boolean isIncrease,
			String propertyName) throws TinyDbException {
		Object value = null;
		if (isIncrease) {
			value = getAutoIncreaseKey();
		} else {
			value = bean.getProperty(propertyName);
			if (value == null) {
				value = UUID.randomUUID().toString().replaceAll("-", "");
			}
		}
		bean.setProperty(propertyName, value);
		return value;
	}

	protected List<SqlParameterValue[]> getInsertParams(Bean[] beans)
			throws TinyDbException {
		List<SqlParameterValue[]> params = new ArrayList<SqlParameterValue[]>();
		for (Bean bean : beans) {
			params.add(createSqlParameterValue(bean));
		}
		return params;
	}

	protected List<SqlParameterValue[]> getParams(Bean[] beans,
			SqlParameterValue[] values) {
		List<SqlParameterValue[]> params = new ArrayList<SqlParameterValue[]>();
		params.add(values);
		for (int i = 1; i < beans.length; i++) {
			Bean bean = beans[i];
			SqlParameterValue[] param = new SqlParameterValue[values.length];
			for (int j = 0; j < values.length; j++) {
				SqlParameterValue value = values[j];
				param[j] = new SqlParameterValue(value, bean.getProperty(value
						.getName()));
			}
			params.add(param);
		}
		return params;
	}

	protected List<List<Object>> getDeleteParams(Bean[] beans) {
		List<List<Object>> params = new ArrayList<List<Object>>();
		for (Bean bean : beans) {
			params.add(getParams(bean));
		}
		return params;
	}

	protected List<Object> getParams(Bean bean) {
		TableConfiguration table = manager.getTableConfiguration(
				bean.getType(), getSchema());
		List<Object> params = new ArrayList<Object>();
		for (ColumnConfiguration column : table.getColumns()) {
			String columnName = column.getColumnName();
			String propertyName = beanDbNameConverter
					.dbFieldNameToPropertyName(columnName);
			if (bean.containsKey(propertyName)) {
				params.add(bean.get(propertyName));
			}
		}
		return params;
	}

	protected String getQuerySql(String beanType) throws TinyDbException {
		StringBuffer sb = new StringBuffer();
		StringBuffer where = new StringBuffer();
		// 条件字段计算
		TableConfiguration table = manager.getTableConfiguration(beanType,
				getSchema());
		String primaryKeyName = table.getPrimaryKey().getColumnName();
		where.append(primaryKeyName).append("=?");
		sb.append("select * from ").append(getFullTableName(beanType));
		sb.append(" where ");
		sb.append(where);
		return sb.toString();
	}

	protected String getTableNameWithSchame(String tableName) {
		if (schema == null || "".equals(schema)) {
			return tableName;
		}
		return schema + "." + tableName;
	}

	public String getSchema() {
		if (StringUtil.isBlank(schema)) {
			schema = manager.getMainSchema();
		}
		return schema;
	}

	public void setManager(BeanOperatorManager manager) {
		this.manager = manager;
	}

	public BeanOperatorManager getManager() {
		return manager;
	}

	public Relation getRelation(String beanType) {
		if (manager != null) {
			return manager.getRelationByBeanType(beanType);
		}
		return null;
	}

	protected String getPrimaryFieldName(DbBaseOperator operator,
			String beanType) throws TinyDbException {
		TableConfiguration configuration = manager.getTableConfiguration(
				beanType, operator.getSchema());
		if (configuration != null) {
			return operator.getBeanDbNameConverter().dbFieldNameToPropertyName(
					configuration.getPrimaryKey().getColumnName());
		}
		throw new TinyDbException("beanType:" + beanType + "不存在主键字段信息");
	}

	/**
	 * 获取主键值
	 * 
	 * @param operator
	 * @param bean
	 * @return
	 * @throws TinyDbException
	 */
	protected String getPrimaryKeyValue(DbBaseOperator operator, Bean bean)
			throws TinyDbException {
		Object primaryKeyValue = bean.getProperty(getPrimaryFieldName(operator,
				bean.getType()));
		if (primaryKeyValue != null) {
			return String.valueOf(primaryKeyValue);
		}
		return null;
	}

}
