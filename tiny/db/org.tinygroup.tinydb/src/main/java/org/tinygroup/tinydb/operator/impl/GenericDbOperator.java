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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlRowSetResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.database.connect.ConnectionTrace;
import org.tinygroup.database.connect.ConnectionTraceProvider;
import org.tinygroup.tinydb.Configuration;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.operator.DBOperator;

public class GenericDbOperator<K> extends BeanDBSqlQueryOperator<K> implements
		DBOperator<K> {
	

	public GenericDbOperator() {
		super();
	}

	public GenericDbOperator(JdbcTemplate jdbcTemplate,Configuration configuration) {
		super(jdbcTemplate,configuration);
	}

	public SqlRowSet getSqlRowSet(String sql) throws TinyDbException {
		try {
			operatorDebugLogNoParam(sql);
			return (SqlRowSet) jdbcTemplate.query(sql,
					new SqlRowSetResultSetExtractor());
		} catch (DataAccessException e) {
			operatorErrorLogNoParam(sql, e);
			throw new TinyDbException(e.getRootCause());
		}
	}

	public SqlRowSet getSqlRowSet(String sql, Object... parameters)
			throws TinyDbException {
		try {
			operatorDebugLog(sql, parameters);
			return (SqlRowSet) jdbcTemplate.query(sql, parameters,
					new SqlRowSetResultSetExtractor());
		} catch (DataAccessException e) {
			operatorErrorLog(sql, e, parameters);
			throw new TinyDbException(e.getRootCause());
		}
	}

	public SqlRowSet getSqlRowSet(String sql, List<Object> parameters)
			throws TinyDbException {
		Object[] params = checkNullParamList(parameters);
		try {
			operatorDebugLog(sql, params);
			return (SqlRowSet) jdbcTemplate.query(sql, params,
					new SqlRowSetResultSetExtractor());
		} catch (DataAccessException e) {
			operatorErrorLog(sql, e, params);
			throw new TinyDbException(e.getRootCause());
		}
	}

	public SqlRowSet getSqlRowSet(String sql, Map<String, Object> parameters)
			throws TinyDbException {
		StringBuffer buf=new StringBuffer();
		List<Object> paramList=getParamArray(sql, parameters, buf);
		Object[] params = checkNullParamList(paramList);
		String tempSql=buf.toString();
		try {
			operatorDebugLog(tempSql, params);
			return (SqlRowSet) jdbcTemplate.query(tempSql, params,
					new SqlRowSetResultSetExtractor());
		} catch (DataAccessException e) {
			operatorErrorLog(tempSql, e, params);
			throw new TinyDbException(e.getRootCause());
		}
	}

	public Collection<ConnectionTrace> queryAllActiveConnection() {
		
		DataSource dataSource = jdbcTemplate.getDataSource();
		if(dataSource instanceof ConnectionTraceProvider){
			ConnectionTraceProvider provider = (ConnectionTraceProvider) dataSource;
			return provider.getConnectionTraces();
		}
		return CollectionUtil.createArrayList();
	}
}
