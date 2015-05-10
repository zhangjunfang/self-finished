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

import org.springframework.jdbc.core.JdbcTemplate;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.Configuration;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.operator.DbSqlQueryOperator;
import org.tinygroup.tinydb.order.OrderBean;
import org.tinygroup.tinydb.query.Conditions;
import org.tinygroup.tinydb.select.SelectBean;

public class BeanDBSqlQueryOperator<K> extends  BeanDBSqlOperator<K> implements DbSqlQueryOperator<K>{

	public BeanDBSqlQueryOperator() {
		super();
	}

	public BeanDBSqlQueryOperator(JdbcTemplate jdbcTemplate,Configuration configuration) {
		super(jdbcTemplate,configuration);
	}
	
	/**
	 * 产生SQL语句
	 * 
	 * @param queryBean
	 * @param stringBuffer
	 *            用于存放SQL
	 * @param valueList
	 *            用于存放值列表
	 */
	public void generateQuerySqlClause(Conditions conditions,
			StringBuffer stringBuffer, List<Object> valueList) {
		stringBuffer.append(conditions.getSegmentWithVariable());
		valueList.addAll(conditions.getParamterList());
	}

	/**
	 * 生成查询部分的sql片段
	 * 
	 * @param selectBeans
	 * @param stringBuffer
	 */
	public void generateSelectSqlClause(SelectBean[] selectBeans,
			StringBuffer stringBuffer) {
		if (selectBeans != null && selectBeans.length > 0) {
			for (int i = 0; i < selectBeans.length; i++) {
				SelectBean selectBean = selectBeans[i];
				stringBuffer.append(selectBean.getSelectClause());
				if (i < selectBeans.length - 1) {
					stringBuffer.append(",");
				}
			}
		} else {
			stringBuffer.append("*");
		}
	}

	/**
	 * 生成order by 子句
	 * 
	 * @param orderBeans
	 * @param stringBuffer
	 */
	public void generateOrderSqlClause(OrderBean[] orderBeans,
			StringBuffer stringBuffer) {
		if (orderBeans != null && orderBeans.length > 0) {
			stringBuffer.append(" order by ");
			for (int i = 0; i < orderBeans.length; i++) {
				OrderBean orderBean = orderBeans[i];
				stringBuffer
						.append(getBeanDbNameConverter()
								.propertyNameToDbFieldName(
										orderBean.getPropertyName()))
						.append(" ").append(orderBean.getOrderMode())
						.append(" ");
				if (i < orderBeans.length - 1) {
					stringBuffer.append(",");
				}
			}
		}
	}

	/**
	 * 生成sql语句
	 * 
	 * @param selectBeans
	 * @param queryBean
	 * @param orderBeans
	 * @param valueList
	 */
	public String generateSqlClause(SelectBean[] selectBeans,String beanType,
			Conditions conditions, OrderBean[] orderBeans,
			List<Object> valueList) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select ");
		generateSelectSqlClause(selectBeans, stringBuffer);
		stringBuffer.append(" from ").append(
				getBeanDbNameConverter().typeNameToDbTableName(beanType));
		stringBuffer.append(" where ");
		generateQuerySqlClause(conditions, stringBuffer, valueList);
		generateOrderSqlClause(orderBeans, stringBuffer);
		return stringBuffer.toString();
	}

	public String generateSqlClause(String selectClause, String beanType,Conditions conditions,
			OrderBean[] orderBeans, List<Object> valueList) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select ").append(selectClause);
		stringBuffer.append(" from ").append(
				getBeanDbNameConverter().typeNameToDbTableName(beanType));
		stringBuffer.append(" where ");
		generateQuerySqlClause(conditions, stringBuffer, valueList);
		generateOrderSqlClause(orderBeans, stringBuffer);
		return stringBuffer.toString();
	}
	
	public Bean[] getBeans(SelectBean[] selectBeans,String beanType, Conditions conditions,
			OrderBean[] orderBeans) throws TinyDbException {
		List<Object> valueList = new ArrayList<Object>();
		String sql = this.generateSqlClause(selectBeans,beanType, conditions,
				orderBeans, valueList);
		return getBeans(sql, valueList.toArray());
	}

	public Bean[] getBeans(SelectBean[] selectBeans,String beanType, Conditions conditions,
			OrderBean[] orderBeans, int start, int limit) throws TinyDbException {
		List<Object> valueList = new ArrayList<Object>();
		String sql = this.generateSqlClause(selectBeans,beanType, conditions,
				orderBeans, valueList);
		return getPageBeans(sql, start, limit,
				valueList.toArray());
	}

	public Bean[] getBeans(String beanType,Conditions conditions, OrderBean[] orderBeans,
			int start, int limit) throws TinyDbException {
		SelectBean[] selectBeans = new SelectBean[0];
		return getBeans(selectBeans,beanType, conditions, orderBeans, start, limit);
	}

	public Bean getSingleValue(String beanType,Conditions conditions) throws TinyDbException {
		SelectBean[] selectBeans = new SelectBean[0];
		return (Bean) getSingleValue(selectBeans,beanType, conditions);
	}

	public Bean getSingleValue(SelectBean[] selectBeans, String beanType,Conditions conditions) throws TinyDbException {
		List<Object> valueList = new ArrayList<Object>();
		String sql = this.generateSqlClause(selectBeans, beanType,conditions, null,
				valueList);
		Bean bean = (Bean) queryObject(sql, beanType, getSchema(),
				valueList.toArray());
		return bean;
	}

	public Bean[] getBeans(String selectClause, String beanType,Conditions conditions,
			OrderBean[] orderBeans) throws TinyDbException {
		List<Object> valueList = new ArrayList<Object>();
		String sql = this.generateSqlClause(selectClause, beanType,conditions,
				orderBeans, valueList);
		return getBeans(sql, valueList.toArray());
	}

	public Bean[] getBeans(String selectClause, String beanType,Conditions conditions,
			OrderBean[] orderBeans, int start, int limit) throws TinyDbException {
		List<Object> valueList = new ArrayList<Object>();
		String sql = this.generateSqlClause(selectClause,beanType, conditions,
				orderBeans, valueList);
		return getPageBeans(sql, start, limit,
				valueList.toArray());
	}

	public Bean getSingleValue(String selectClause,String beanType, Conditions conditions) throws TinyDbException {
		List<Object> valueList = new ArrayList<Object>();
		String sql = this.generateSqlClause(selectClause, beanType,conditions, null,
				valueList);
		Bean bean = (Bean) queryObject(sql, beanType, getSchema(),
				valueList.toArray());
		return bean;
	}

	public Bean[] getBeans(String beanType,Conditions conditions, OrderBean[] orderBeans) throws TinyDbException {
		SelectBean[] selectBeans = new SelectBean[0];
		return getBeans(selectBeans, beanType,conditions, orderBeans);
	}

}
