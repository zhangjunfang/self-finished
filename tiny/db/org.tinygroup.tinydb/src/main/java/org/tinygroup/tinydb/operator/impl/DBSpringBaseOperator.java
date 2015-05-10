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
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.tinygroup.commons.tools.ArrayUtil;
import org.tinygroup.commons.tools.ObjectUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.BeanDbNameConverter;
import org.tinygroup.tinydb.Configuration;
import org.tinygroup.tinydb.dialect.Dialect;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.operator.TransactionCallBack;
import org.tinygroup.tinydb.operator.TransactionOperator;
import org.tinygroup.tinydb.spring.BatchPreparedStatementSetterImpl;
import org.tinygroup.tinydb.spring.SqlParamValuesBatchStatementSetterImpl;
import org.tinygroup.tinydb.spring.TinydbResultExtractor;
import org.tinygroup.tinydb.sql.impl.StatementTransformComposite;

public class DBSpringBaseOperator extends StatementTransformComposite implements TransactionOperator {

	private Dialect dialect;
	private TransactionStatus status;
	private PlatformTransactionManager transactionManager;
	private TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
	private static Logger logger = LoggerFactory.getLogger(DBSpringBaseOperator.class);
	protected JdbcTemplate jdbcTemplate;
	
	private UUID uuid;
	
	public DBSpringBaseOperator(){
		uuid = UUID.randomUUID();
	}
	
	public DBSpringBaseOperator(JdbcTemplate jdbcTemplate,Configuration configuration) {
		super(configuration);
		this.jdbcTemplate = jdbcTemplate;
		uuid = UUID.randomUUID();
	}
	
	public UUID getUniqueCode() {
		return uuid;
	}

	public BeanDbNameConverter getBeanDbNameConverter() {
		return beanDbNameConverter;
	}

	public void setBeanDbNameConverter(BeanDbNameConverter beanDbNameConverter) {
		this.beanDbNameConverter = beanDbNameConverter;
	}

	public Dialect getDialect() {
		return dialect;
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate=jdbcTemplate;
	}
	

	public int account(String sql) throws TinyDbException {
		try {
			int ret = 0;
			SqlRowSet sqlRowset = jdbcTemplate.queryForRowSet(sql);
			if (sqlRowset.next()) {
				ret = sqlRowset.getInt(1);
			}
			return ret;
		} catch (DataAccessException e) {
			throw new TinyDbException(e);
		}
	}


	public int executeByList(String sql, List<Object> parameters,
			List<Integer> dataTypes) throws TinyDbException {
		Object[] params = checkNullParamList(parameters);
		try {
			operatorDebugLog(sql, params);
			int[] types = getDataTypes(dataTypes);
			if (types != null && types.length > 0) {
				return jdbcTemplate.update(sql, params, types);
			} else {
				return jdbcTemplate.update(sql, parameters.toArray());
			}
		} catch (DataAccessException e) {
			operatorErrorLog(sql, e, params);
			throw new TinyDbException(e);
		}

	}

	public int executeBySqlParameterValues(String sql,
			SqlParameterValue[] values) throws TinyDbException {
		Object[] params = convertToObject(values);
		try {
			operatorDebugLog(sql, params);
			return jdbcTemplate.update(sql, values);
		} catch (DataAccessException e) {
			operatorErrorLog(sql, e, params);
			throw new TinyDbException(e);
		}
	}

	public int executeBySqlParameterValue(String sql, SqlParameterValue value)
			throws TinyDbException {
		SqlParameterValue[] values = new SqlParameterValue[1];
		values[0] = value;
		Object[] params = convertToObject(values);
		try {
			operatorDebugLog(sql, params);
			return jdbcTemplate.update(sql, values);
		} catch (DataAccessException e) {
			operatorErrorLog(sql, e, params);
			throw new TinyDbException(e);
		}
	}

	/**
	 * 
	 * 根据参数中sql语句进行批处理操作
	 * 
	 * @param sql
	 * @param parameters
	 * @param dataTypes
	 *            key代表未赋参数值的sql语句 value代表sql语句中的参数列表信息
	 * @throws TinyDbException
	 */
	protected int[] executeBatchOperator(String sql,
			List<List<Object>> parameters, int[] columnTypes)
			throws TinyDbException {
		try {
			operatorDebugLogNoParam(sql);
			return jdbcTemplate.batchUpdate(sql,
					new BatchPreparedStatementSetterImpl(parameters,
							columnTypes));
		} catch (DataAccessException e) {
			operatorErrorLogNoParam(sql, e);
			throw new TinyDbException(e);
		}
	}

	protected int[] executeBatchOperator(String[] sqls) throws TinyDbException {
		try {
			logger.logMessage(LogLevel.DEBUG, "DbOperatorId:[{0}],执行sql列表:{1}",
					uuid.toString(), sqlsToString(sqls));
			return jdbcTemplate.batchUpdate(sqls);
		} catch (DataAccessException e) {
			logger.errorMessage("DbOperatorId:[{0}],执行错误的sql列表:{1}", e,
					uuid.toString(), sqlsToString(sqls));
			throw new TinyDbException(e);
		}
	}

	/**
	 * 
	 * 根据参数中sql语句进行批处理操作
	 * 
	 * @param sql
	 * @param parameters
	 *            key代表未赋参数值的sql语句 value代表sql语句中的参数列表信息
	 * @throws TinyDbException
	 */
	protected int[] executeBatchBySqlParamterValues(String sql,
			List<SqlParameterValue[]> parameters) throws TinyDbException {
		try {
			operatorDebugLogNoParam(sql);
			return jdbcTemplate.batchUpdate(sql,
					new SqlParamValuesBatchStatementSetterImpl(parameters));
		} catch (DataAccessException e) {
			operatorErrorLogNoParam(sql, e);
			throw new TinyDbException(e);
		}
	}

	public List<Bean> findBeansForPage(String sql, String beanType,
			String schema, int start, int limit) throws TinyDbException {
		if (supportsLimit()) {
			return findBeansForDialectPage(sql, beanType, schema, start, limit);
		} else {
			return findBeansForCursorPage(sql, beanType, schema, start, limit);
		}
	}

	@SuppressWarnings("unchecked")
	protected List<Bean> findBeansForCursorPage(String sql, String beanType,
			String schema, int start, int limit) throws TinyDbException {
		try {
			operatorDebugLogNoParam(sql);
			List<Bean> beans = (List<Bean>) jdbcTemplate.query(sql,
					new TinydbResultExtractor(beanType, schema, start, limit,
							beanDbNameConverter));
			return beans;
		} catch (DataAccessException e) {
			operatorErrorLogNoParam(sql, e);
			throw new TinyDbException(e);
		}
	}

	@SuppressWarnings("unchecked")
	protected List<Bean> findBeansForDialectPage(String sql, String beanType,
			String schema, int start, int limit) throws TinyDbException {
		String tempSql = sql;
		tempSql = getLimitString(sql, start, limit);
		try {
			operatorDebugLogNoParam(tempSql);
			List<Bean> beans = (List<Bean>) jdbcTemplate.query(tempSql,
					new TinydbResultExtractor(beanType, schema,
							beanDbNameConverter));
			return beans;
		} catch (DataAccessException e) {
			operatorErrorLogNoParam(tempSql, e);
			throw new TinyDbException(e);
		}

	}

	public List<Bean> findBeansByListForPage(String sql, String beanType,
			String schema, int start, int limit, List<Object> parameters)
			throws TinyDbException {
		if (supportsLimit()) {
			return findBeansByListForDialectPage(sql, beanType, schema, start,
					limit, parameters);
		} else {
			return findBeansByListForCursorPage(sql, beanType, schema, start,
					limit, parameters);
		}
	}

	@SuppressWarnings("unchecked")
	protected List<Bean> findBeansByListForDialectPage(String sql,
			String beanType, String schema, int start, int limit,
			List<Object> parameters) throws TinyDbException {
		String tempSql = sql;
		tempSql = getLimitString(sql, start, limit);
		Object[] params = deleteNullParamList(parameters);
		try {
			operatorDebugLog(tempSql, params);
			List<Bean> beans = (List<Bean>) jdbcTemplate.query(tempSql, params,
					new TinydbResultExtractor(beanType, schema,
							beanDbNameConverter));
			return beans;
		} catch (DataAccessException e) {
			operatorErrorLog(tempSql, e, params);
			throw new TinyDbException(e);
		}

	}

	@SuppressWarnings("unchecked")
	protected List<Bean> findBeansByListForCursorPage(String sql,
			String beanType, String schema, int start, int limit,
			List<Object> parameters) throws TinyDbException {
		Object[] params = deleteNullParamList(parameters);
		try {
			operatorDebugLog(sql, params);
			List<Bean> beans = (List<Bean>) jdbcTemplate.query(sql, params,
					new TinydbResultExtractor(beanType, schema, start, limit,
							beanDbNameConverter));
			return beans;
		} catch (DataAccessException e) {
			operatorErrorLog(sql, e, params);
			throw new TinyDbException(e);
		}

	}

	public List<Bean> findBeansByMapForPage(String sql, String beanType,
			String schema, int start, int limit,
			Map<String, Object> parameters, List<Integer> dataTypes)
			throws TinyDbException {
		if (supportsLimit()) {
			return findBeansByMapForDialectPage(sql, beanType, schema, start,
					limit, parameters, dataTypes);
		} else {
			return findBeansByMapForCursorPage(sql, beanType, schema, start,
					limit, parameters, dataTypes);
		}
	}

	protected List<Bean> findBeansByMapForDialectPage(String sql,
			String beanType, String schema, int start, int limit,
			Map<String, Object> parameters, List<Integer> dataTypes)
			throws TinyDbException {
		String tempSql = sql;
		tempSql = getLimitString(sql, start, limit);
		return findBeansByMap(tempSql, beanType, schema, parameters, dataTypes);
	}

	@SuppressWarnings("unchecked")
	protected List<Bean> findBeansByMapForCursorPage(String sql,
			String beanType, String schema, int start, int limit,
			Map<String, Object> parameters, List<Integer> dataTypes)
			throws TinyDbException {
		StringBuffer buf = new StringBuffer();
		List<Object> paramList = getParamArray(sql, parameters, buf);
		String tempSql=buf.toString();
		Object[] params = checkNullParamList(paramList);
		try {
			operatorDebugLog(tempSql, params);
			int[] types = getDataTypes(dataTypes);
			if (types != null && types.length > 0) {
				return (List<Bean>) jdbcTemplate.query(tempSql, params, types, new TinydbResultExtractor(beanType,
						schema, start, limit, beanDbNameConverter));
			} else {
				return (List<Bean>) jdbcTemplate.query(tempSql, params, new TinydbResultExtractor(beanType, schema,
						start, limit, beanDbNameConverter));
			}
		} catch (DataAccessException e) {
			operatorErrorLog(tempSql, e, params);
			throw new TinyDbException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Bean> findBeans(String sql, String beanType, String schema)
			throws TinyDbException {
		try {
			operatorDebugLogNoParam(sql);
			return (List<Bean>) jdbcTemplate.query(sql,
					new TinydbResultExtractor(beanType, schema,
							beanDbNameConverter));
		} catch (DataAccessException e) {
			operatorErrorLogNoParam(sql, e);
			throw new TinyDbException(e);
		}

	}

	@SuppressWarnings("unchecked")
	public List<Bean> findBeans(String sql, String beanType, String schema,
			Object... parameters) throws TinyDbException {
		try {
			operatorDebugLog(sql, parameters);
			return (List<Bean>) jdbcTemplate.query(sql, parameters,
					new TinydbResultExtractor(beanType, schema,
							beanDbNameConverter));
		} catch (DataAccessException e) {
			operatorErrorLog(sql, e, parameters);
			throw new TinyDbException(e);
		}

	}

	@SuppressWarnings("unchecked")
	public List<Bean> findBeansByList(String sql, String beanType,
			String schema, List<Object> parameters) throws TinyDbException {
		Object[] params = checkNullParamList(parameters);
		try {
			operatorDebugLog(sql, params);
			return (List<Bean>) jdbcTemplate.query(sql, params,
					new TinydbResultExtractor(beanType, schema,
							beanDbNameConverter));
		} catch (DataAccessException e) {
			operatorErrorLog(sql, e, params);
			throw new TinyDbException(e);
		}

	}

	@SuppressWarnings("rawtypes")
	public Object queryObject(String sql, String beanType, String schema,
			Object... parameters) throws TinyDbException {
		try {
			operatorDebugLog(sql, parameters);
			List results = (List) jdbcTemplate.query(sql, parameters,
					new TinydbResultExtractor(beanType, schema,
							beanDbNameConverter));
			return DataAccessUtils.requiredSingleResult(results);
		} catch (DataAccessException e) {
			operatorErrorLog(sql, e, parameters);
			throw new TinyDbException(e);
		}
	}

	private boolean supportsLimit() {
		if (dialect == null) {
			return false;
		}
		return dialect.supportsLimit();
	}

	private String getLimitString(String sql, int start, int limit) {
		return dialect.getLimitString(sql, start, limit);
	}

	@SuppressWarnings("unchecked")
	public List<Bean> findBeansByMap(String sql, String beanType,
			String schema, Map<String, Object> parameters,
			List<Integer> dataTypes) throws TinyDbException {
		StringBuffer buf = new StringBuffer();
		List<Object> paramList = getParamArray(sql, parameters, buf);
		String tempSql=buf.toString();
		Object[] params = checkNullParamList(paramList);
		try {
			operatorDebugLog(tempSql, params);
			int[] types = getDataTypes(dataTypes);
			if (types != null && types.length > 0) {
				return (List<Bean>) jdbcTemplate.query(tempSql, params,
						types, new TinydbResultExtractor(beanType, schema,
								beanDbNameConverter));
			} else {
				return (List<Bean>) jdbcTemplate.query(tempSql, params,
						new TinydbResultExtractor(beanType, schema,
								beanDbNameConverter));
			}
		} catch (DataAccessException e) {
			operatorErrorLog(tempSql, e, params);
			throw new TinyDbException(e);
		}

	}

	@SuppressWarnings("rawtypes")
	public Object queryObjectByMap(String sql, String beanType, String schema,
			Map<String, Object> parameters, List<Integer> dataTypes)
			throws TinyDbException {
		StringBuffer buf = new StringBuffer();
		List<Object> paramList = getParamArray(sql, parameters, buf);
		String tempSql=buf.toString();
		Object[] params = checkNullParamList(paramList);
		try {
			operatorDebugLog(tempSql, params);
			int[] types = getDataTypes(dataTypes);
			if (types != null && types.length > 0) {
				List results = (List) jdbcTemplate.query(tempSql,
						params, types, new TinydbResultExtractor(
								beanType, schema, beanDbNameConverter));
				return DataAccessUtils.requiredSingleResult(results);
			} else {
				List results = (List) jdbcTemplate.query(tempSql,
						params, new TinydbResultExtractor(beanType,
								schema, beanDbNameConverter));
				return DataAccessUtils.requiredSingleResult(results);
			}

		} catch (DataAccessException e) {
			operatorErrorLog(tempSql, e, params);
			throw new TinyDbException(e);
		}

	}

	public int executeByMap(String sql, Map<String, Object> parameters,
			List<Integer> dataTypes) throws TinyDbException {
		StringBuffer buf = new StringBuffer();
		List<Object> paramList = getParamArray(sql, parameters, buf);
		String tempSql=buf.toString();
		Object[] params = checkNullParamList(paramList);
		try {
			operatorDebugLog(tempSql, params);
			int[] types = getDataTypes(dataTypes);
			if (types != null && types.length > 0) {
				return jdbcTemplate.update(tempSql, params,
						types);
			} else {
				return jdbcTemplate.update(tempSql, params);
			}
		} catch (DataAccessException e) {
			operatorErrorLog(tempSql, e, params);
			throw new TinyDbException(e);
		}

	}

	public int executeByArray(String sql, Object... parameters)
			throws TinyDbException {
		try {
			operatorDebugLog(sql, parameters);
			return jdbcTemplate.update(sql, parameters);
		} catch (DataAccessException e) {
			operatorErrorLog(sql, e, parameters);
			throw new TinyDbException(e);
		}

	}

	public int queryForInt(String sql, Object... parameters)
			throws TinyDbException {
		try {
			operatorDebugLog(sql, parameters);
			return jdbcTemplate.queryForInt(sql, parameters);
		} catch (DataAccessException e) {
			operatorErrorLog(sql, e, parameters);
			throw new TinyDbException(e);
		}
	}

	public int queryForIntByList(String sql, List<Object> parameters)
			throws TinyDbException {
		Object[] params = checkNullParamList(parameters);
		try {
			operatorDebugLog(sql, params);
			return jdbcTemplate.queryForInt(sql, params);
		} catch (DataAccessException e) {
			operatorErrorLog(sql, e, params);
			throw new TinyDbException(e);
		}

	}

	public int queryForIntByMap(String sql, Map<String, Object> parameters)
			throws TinyDbException {
		StringBuffer buf = new StringBuffer();
		List<Object> paramList = getParamArray(sql, parameters, buf);
		String tempSql=buf.toString();
		Object[] params = checkNullParamList(paramList);
		try {
			operatorDebugLog(tempSql, params);
			return jdbcTemplate.queryForInt(tempSql, params);
		} catch (DataAccessException e) {
			operatorErrorLog(tempSql, e, params);
			throw new TinyDbException(e);
		}
	}

	private int[] getDataTypes(List<Integer> dataTypes) {
		int[] types = null;
		if (dataTypes != null) {
			types = new int[dataTypes.size()];
			for (int i = 0; i < dataTypes.size(); i++) {
				types[i] = dataTypes.get(i);
			}
		}
		return types;
	}

	protected List<Object> getParamArray(String sql,
			Map<String, Object> parameters, StringBuffer buf) {
		ArrayList<Object> paraList = new ArrayList<Object>();
		String patternStr = "([\"](.*?)[\"])|([\'](.*?)[\'])|([@][a-zA-Z_$][\\w$]*)";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(sql);
		int curpos = 0;
		while (matcher.find()) {
			String replaceStr = matcher.group();
			String variable = replaceStr.substring(1, replaceStr.length());
			if (!replaceStr.startsWith("\"") && !replaceStr.startsWith("'")
					&& parameters != null && parameters.containsKey(variable)) {
				buf.append(sql.substring(curpos, matcher.start()));
				curpos = matcher.end();
				paraList.add(parameters.get(variable));
				buf.append("?");
			}
			continue;
		}
		buf.append(sql.substring(curpos));
		return paraList;
	}

	public void beginTransaction() {
		if (status == null || status.isCompleted()) {
			status = this.getTransactionManager().getTransaction(
					transactionDefinition);
			if (status.isNewTransaction()) {
				logger.logMessage(LogLevel.INFO, "DbOperatorId:[{0}],开启新事务",uuid.toString());
			} else {
				logger.logMessage(LogLevel.INFO, "DbOperatorId:[{0}],未开启新事务,将使用之前开启的事务",uuid.toString());
			}

		}
	}

	public void commitTransaction() {
		if (status != null && !status.isCompleted()) {
			logger.logMessage(LogLevel.INFO, "DbOperatorId:[{0}],提交事务",uuid.toString());
			this.getTransactionManager().commit(status);
		}
	}

	public void rollbackTransaction() {
		if (status != null && !status.isCompleted()) {
			logger.logMessage(LogLevel.INFO, "DbOperatorId:[{0}],事务将回滚",uuid.toString());
			this.getTransactionManager().rollback(status);
		}
	}

	public PlatformTransactionManager getTransactionManager() {
		if (transactionManager == null) {
			transactionManager = new DataSourceTransactionManager(
					jdbcTemplate.getDataSource());
		}
		return transactionManager;
	}

	public void setTransactionManager(
			PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public TransactionDefinition getTransactionDefinition() {
		return transactionDefinition;
	}

	public void setTransactionDefinition(
			TransactionDefinition transactionDefinition) {
		this.transactionDefinition = transactionDefinition;
	}

	public Object execute(TransactionCallBack callback) {
	    beginTransaction();
		Object result = null;
		try {
			result = callback.callBack(status);
			commitTransaction();
		} catch (Exception ex) {
			rollbackTransaction();
		}
		return result;
	}

	/**
	 * Params to string.
	 * 
	 * @param objects
	 *            the objects
	 * @return the string
	 */
	private String paramsToString(Object[] objects) {
		if (objects == null) {
			return "no parameters";
		}
		StringBuilder builder = new StringBuilder();
		for (Object object : objects) {
			builder.append("," + object);
		}
		if (builder.length() <= 1)
			return "";
		return builder.toString().substring(1);
	}

	private String sqlsToString(Object[] objects) {
		if (objects == null) {
			return "no sql";
		}
		StringBuilder builder = new StringBuilder();
		for (Object object : objects) {
			builder.append("," + object);
		}
		if (builder.length() <= 1)
			return "";
		return builder.toString().substring(1);
	}

	private Object[] convertToObject(SqlParameterValue[] values) {
		if (ArrayUtil.isEmptyArray(values)) {
			return new Object[0];
		}
		Object[] params = new Object[values.length];
		for (int i = 0; i < values.length; i++) {
			params[i] = values[i].getValue();
		}
		return params;
	}

	protected void operatorErrorLog(String sql, DataAccessException exception,
			Object... parameters) {
		logger.errorMessage("DbOperatorId:[{0}],执行的错误sql:[{1}],参数:[{2}]",
				exception, uuid.toString(), sql,
				paramsToString(parameters));
	}

	protected void operatorDebugLog(String sql, Object... parameters) {
		logger.logMessage(LogLevel.DEBUG,
				"DbOperatorId:[{0}],当前执行的sql:[{1}],参数:[{2}]", uuid.toString(),
				sql, paramsToString(parameters));
	}

	protected void operatorErrorLogNoParam(String sql, DataAccessException e) {
		logger.errorMessage("DbOperatorId:[{0}],批处理执行错误的sql:[{1}]",
				e, uuid.toString(), sql);
	}

	protected void operatorDebugLogNoParam(String sql) {
		logger.logMessage(LogLevel.DEBUG,
				"DbOperatorId:[{0}],当前批处理执行的sql:[{1}]", uuid.toString(), sql);
	}
	
	protected Object[] checkNullParamList(List<Object> parameters) {
		if(parameters==null){
			parameters=new ArrayList<Object>();
		}
		Object[] params=parameters.toArray();
		return params;
	}
	
	//不保留空元素
	protected Object[] deleteNullParamList(List<Object> parameters) {
		ArrayList<Object> list=new ArrayList<Object>();
		if(parameters!=null){
		   for(Object o:parameters){	
			 if(!ObjectUtil.isEmptyObject(o)){
				list.add(o);
			 }
		   }
		}
		Object[] params=list.toArray();
		return params;
	}

}
