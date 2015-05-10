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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.tinygroup.commons.tools.ArrayUtil;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.Configuration;
import org.tinygroup.tinydb.config.ColumnConfiguration;
import org.tinygroup.tinydb.config.TableConfiguration;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.operator.DbBatchOperator;
import org.tinygroup.tinydb.relation.Relation;
import org.tinygroup.tinydb.sql.SqlAndValues;
import org.tinygroup.tinydb.util.TinyDBUtil;

public class BeanDBBatchOperator<K> extends BeanDBSingleOperator<K> implements
		DbBatchOperator<K> {

	public BeanDBBatchOperator() {
		super();
	}

	public BeanDBBatchOperator(JdbcTemplate jdbcTemplate,
			Configuration configuration) {
		super(jdbcTemplate, configuration);
	}

	public Bean[] getBeans(Bean bean) throws TinyDbException {
		SqlAndValues sqlAndValues = toSelect(bean);
		List<Bean> beans = findBeansByList(sqlAndValues.getSql(),
				bean.getType(), getSchema(), sqlAndValues.getValues());
		return relationProcess(bean.getType(), beans);
	}

	public Bean[] batchInsert(Bean[] beans) throws TinyDbException {
		Bean[] insertBeans = insertTopBeans(beans);
		// 关联bean插入
		if (insertBeans != null && insertBeans.length > 0) {
			Relation relation = getRelation(insertBeans[0].getType());
			if (relation != null) {
				for (Bean bean : insertBeans) {
					processRelation(bean, relation,
							new InsertRelationCallBack());
				}
			}
		}
		return insertBeans;
	}

	private Bean[] insertTopBeans(Bean[] beans) throws TinyDbException {
		if (beans == null || beans.length == 0) {
			return null;
		}
		checkBeanType(beans);
		String sql = getInsertSql(beans[0]);
		List<SqlParameterValue[]> params = getInsertParams(beans);
		executeBatchBySqlParamterValues(sql, params);
		return beans;
	}

	private void checkBeanType(Bean[] beans) throws TinyDbException {
		String beanType = beans[0].getType();
		for (int i = 1; i < beans.length; i++) {
			Bean bean = beans[i];
			if (!beanType.equals(bean.getType())) {
				throw new TinyDbException("批处理对象的bean类型不同");
			}
		}
	}

	public int[] batchUpdate(Bean[] beans) throws TinyDbException {
		int[] records = updateTopBeans(beans);
		if (beans != null && beans.length > 0) {
			Relation relation = getRelation(beans[0].getType());
			if (relation != null) {
				for (Bean bean : beans) {
					processRelation(bean, relation,
							new UpdateRelationCallBack());
				}
			}
		}
		return records;
	}

	private int[] updateTopBeans(Bean[] beans) throws TinyDbException {
		if (beans == null || beans.length == 0) {
			return null;
		}
		checkBeanType(beans);
		List<String> conditionFields = new ArrayList<String>();
		TableConfiguration table = manager.getTableConfiguration(
				beans[0].getType(), getSchema());
		ColumnConfiguration pk = table.getPrimaryKey();
		conditionFields.add(pk.getColumnName());
		// 所有的bean都采用根据第一个bean生成的sql来做处理
		String sql = getUpdateSql(beans[0], conditionFields);
		SqlParameterValue[] values = getSqlParamterValue(beans[0],
				conditionFields);
		List<SqlParameterValue[]> params = getParams(beans, values);
		return executeBatchBySqlParamterValues(sql, params);
	}

	public int[] batchDelete(Bean[] beans) throws TinyDbException {
		int[] records = deleteTopBeans(beans);
		if (beans != null && beans.length > 0) {
			Relation relation = getRelation(beans[0].getType());
			if (relation != null) {
				for (Bean bean : beans) {
					processRelation(bean, relation,
							new DeleteRelationCallBack());
				}
			}
		}
		return records;
	}

	private int[] deleteTopBeans(Bean[] beans) throws TinyDbException {
		if (beans == null || beans.length == 0) {
			return null;
		}
		checkBeanType(beans);
		List<String> conditionColumns = getColumnNames(beans[0]);
		String sql = getDeleteSql(beans[0], conditionColumns);
		SqlParameterValue[] values = getSqlParameterValues(beans[0]);
		List<SqlParameterValue[]> params = getParams(beans, values);
		return executeBatchBySqlParamterValues(sql, params);
	}

	public int[] deleteById(K[] beanIds, String beanType)
			throws TinyDbException {
		if (beanIds == null || beanIds.length == 0) {
			return null;
		}
		List<SqlParameterValue[]> params = new ArrayList<SqlParameterValue[]>();
		TableConfiguration table = manager.getTableConfiguration(beanType,
				getSchema());
		String sql = getDeleteSqlByKey(beanType);
		for (K beanId : beanIds) {
			SqlParameterValue value = createSqlParamter(beanId,
					table.getPrimaryKey());
			SqlParameterValue[] values = new SqlParameterValue[1];
			values[0] = value;
			params.add(values);
		}
		return executeBatchBySqlParamterValues(sql, params);
	}

	public Bean[] getBeansById(K[] beanIds, String beanType)
			throws TinyDbException {
		List<Bean> list = queryBean(beanType, beanIds);
		Relation relation = getRelation(beanType);
		if (relation != null) {
			for (Bean bean : list) {
				processRelation(bean, relation, new QueryRelationCallBack());
			}
		}
		return TinyDBUtil.collectionToArray(list);
	}

	public Bean[] batchInsert(Collection<Bean> beans) throws TinyDbException {
		return batchInsert(TinyDBUtil.collectionToArray(beans));
	}

	public Bean[] batchInsert(Collection<Bean> beans, int batchSize)
			throws TinyDbException {
		return batchInsert(TinyDBUtil.collectionToArray(beans), batchSize);
	}

	public int[] batchUpdate(Collection<Bean> beans) throws TinyDbException {
		return batchUpdate(TinyDBUtil.collectionToArray(beans));
	}

	public void batchUpdate(Collection<Bean> beans, int batchSize)
			throws TinyDbException {
		batchUpdate(TinyDBUtil.collectionToArray(beans), batchSize);
	}

	public int[] batchDelete(Collection<Bean> beans) throws TinyDbException {
		return batchDelete(TinyDBUtil.collectionToArray(beans));
	}

	public void batchDelete(Collection<Bean> beans, int batchSize)
			throws TinyDbException {
		batchDelete(TinyDBUtil.collectionToArray(beans), batchSize);
	}

	@SuppressWarnings("unchecked")
	public int[] deleteById(Collection<K> beanIds, String beanType)
			throws TinyDbException {
		return deleteById((K[]) beanIds.toArray(), beanType);
	}

	public Bean[] getBeansById(Collection<K> beanIds, String beanType)
			throws TinyDbException {
		return getBeansById(TinyDBUtil.collectionToArray(beanIds), beanType);
	}

	private List<Bean> queryBean(String beanType, K[] beanIds)
			throws TinyDbException {
		String sql = getQueryInSql(beanType, beanIds);
		List<Object> params = new ArrayList<Object>();
		if (!ArrayUtil.isEmptyArray(beanIds)) {
			for (K beanId : beanIds) {
				params.add(beanId);
			}
		}
		return findBeansByList(sql, beanType, getSchema(), params);
	}

	private String getQueryInSql(String beanType, K[] beanIds)
			throws TinyDbException {
		StringBuffer sb = new StringBuffer();
		StringBuffer where = new StringBuffer();
		// 条件字段计算
		TableConfiguration table = manager.getTableConfiguration(beanType,
				schema);
		String primaryKeyName = table.getPrimaryKey().getColumnName();
		if (!ArrayUtil.isEmptyArray(beanIds)) {
			where.append(primaryKeyName).append(" in (");
			for (int i = 0; i < beanIds.length; i++) {
				where.append("?");
				if (i != beanIds.length - 1) {
					where.append(",");
				}
			}
			where.append(")");
		}
		sb.append("select * from ").append(getFullTableName(beanType));
		if (!ArrayUtil.isEmptyArray(beanIds)) {
			sb.append(" where ");
			sb.append(where);
		}
		return sb.toString();
	}

	public Bean[] batchInsert(Bean[] beans, int batchSize)
			throws TinyDbException {

		if (beans.length > batchSize) {

			batchProcess(batchSize, Arrays.asList(beans), new BatchCallBack() {

				public void callback(List<Bean> beans) throws TinyDbException {
					batchInsert(beans);
				}
			});

		} else {
			batchInsert(beans);
		}
		return beans;

	}

	public void batchUpdate(Bean[] beans, int batchSize) throws TinyDbException {
		if (beans.length > batchSize) {
			batchProcess(batchSize, Arrays.asList(beans), new BatchCallBack() {
				public void callback(List<Bean> beans) throws TinyDbException {
					batchUpdate(beans);
				}
			});

		} else {
			batchUpdate(beans);
		}
	}

	public void batchDelete(Bean[] beans, int batchSize) throws TinyDbException {
		if (beans.length > batchSize) {
			batchProcess(batchSize, Arrays.asList(beans), new BatchCallBack() {
				public void callback(List<Bean> beans) throws TinyDbException {
					batchDelete(beans);
				}
			});

		} else {
			batchDelete(beans);
		}
	}

	private void batchProcess(int batchSize, List<Bean> beans,
			BatchCallBack callback) throws TinyDbException {
		int totalSize = beans.size();
		int times = totalSize % batchSize == 0 ? totalSize / batchSize
				: totalSize / batchSize + 1;
		int numOfEach = totalSize % times == 0 ? totalSize / times : totalSize
				/ times + 1;
		int fromIndex = 0;
		for (int i = 0; i < times; i++) {
			int endIndex = fromIndex + numOfEach;
			if (endIndex > totalSize) {
				endIndex = totalSize;
			}
			List<Bean> processBeans = beans.subList(fromIndex, endIndex);
			fromIndex += numOfEach;
			callback.callback(processBeans);
		}

	}

	interface BatchCallBack {
		void callback(List<Bean> beans) throws TinyDbException;
	}

	public Bean[] insertBean(Bean[] beans) throws TinyDbException {
		for (Bean bean : beans) {
			insert(bean);
		}
		return beans;
	}

	public int[] updateBean(Bean[] beans) throws TinyDbException {
		int[] records = new int[beans.length];
		for (int i = 0; i < beans.length; i++) {
			records[i] = update(beans[i]);
		}
		return records;
	}

	public int[] deleteBean(Bean[] beans) throws TinyDbException {
		int[] records = new int[beans.length];
		for (int i = 0; i < beans.length; i++) {
			records[i] = delete(beans[i]);
		}
		return records;
	}

	public int[] executeBatch(List<String> sqls) throws TinyDbException {
		return executeBatchOperator(sqls.toArray(new String[0]));
	}

	public int[] executeBatchByList(String sql, List<List<Object>> parameters)
			throws TinyDbException {
		return executeBatchOperator(sql, parameters, new int[0]);
	}

	public int[] executeBatchByList(String sql, List<List<Object>> parameters,
			int[] columnTypes) throws TinyDbException {
		return executeBatchOperator(sql, parameters, columnTypes);
	}

	public int[] executeBatchByMap(String sql,
			List<Map<String, Object>> parameters) throws TinyDbException {
		List<List<Object>> paramList = new ArrayList<List<Object>>();
		String executeSql = sql;
		if (!CollectionUtil.isEmpty(parameters)) {
			StringBuffer buf = new StringBuffer();
			Map<String, Object> firstParam = parameters.get(0);
			List<Object> paraList = getParamArray(sql, firstParam, buf);
			paramList.add(paraList);
			executeSql = buf.toString();
			for (int i = 1; i < parameters.size(); i++) {
				Map<String, Object> param = parameters.get(i);
				paraList = getParamArray(sql, param, new StringBuffer());
				paramList.add(paraList);
			}
		}
		return executeBatchByList(executeSql, paramList);
	}
}
