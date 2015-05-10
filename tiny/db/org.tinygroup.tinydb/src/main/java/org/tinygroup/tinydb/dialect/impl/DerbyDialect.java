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
/*
 * 系统名称: JRES 应用快速开发企业套件
 * 模块名称: JRES
 * 文件名称: MySQLDialect.java
 * 软件版权: 恒生电子股份有限公司
 * 修改记录:
 * 修改日期      修改人员                     修改说明
 * ========    =======  ============================================
 *             
 * ========    =======  ============================================
 */
package org.tinygroup.tinydb.dialect.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.tinygroup.database.util.DataBaseUtil;

/**
 * The Class MySQLDialect.
 */
public class DerbyDialect extends AbstractColumnDialcet {

	/** The default for dummy name */
	private static final String DEFAULT_DUMMY_NAME = "dummy";

	/** The name of the dummy column used for inserts */
	private String dummyName = DEFAULT_DUMMY_NAME;

	/** The current cache of values */
	private int[] valueCache;

	/** The next id to serve from the value cache */
	private int nextValueIndex = -1;

	/**
	 * Instantiates a new my sql dialect.
	 */
	public DerbyDialect() {
	}

	/**
	 * getLimitString.
	 * 
	 * @param sql
	 *            String
	 * @param offset
	 *            int
	 * @param limit
	 *            int
	 * @return String
	 */
	public String getLimitString(String sql, int offset, int limit) {
		return getLimitStringVersion106(sql, offset, limit);
	}

	private String getLimitStringVersion106(String sql, int offset, int limit) {
		int start=offset;
		if(offset<0){
			start=0;
		}
		if(start>0){
			start=start-1;
		}
		StringBuffer pagingSelect = new StringBuffer();
		pagingSelect.append(sql);
		pagingSelect.append(" OFFSET ").append(start).append(" ROWS ")
				.append(" FETCH NEXT ").append(limit).append("  ROWS ONLY  ");
		return pagingSelect.toString();
	}

	/**
	 * supportsLimit.
	 * 
	 * @return boolean
	 */
	public boolean supportsLimit() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hundsun.jres.interfaces.db.dialect.IDialect#getAutoIncreaseKeySql()
	 */
	public int getNextKey() {
		if (this.nextValueIndex < 0 || this.nextValueIndex >= getCacheSize()) {
			/*
			* Need to use straight JDBC code because we need to make sure that the insert and select
			* are performed on the same connection (otherwise we can't be sure that last_insert_id()
			* returned the correct value)
			*/
			Connection con = DataSourceUtils.getConnection(getDataSource());
			Statement stmt = null;
			try {
				stmt = con.createStatement();
				DataSourceUtils.applyTransactionTimeout(stmt, getDataSource());
				this.valueCache = new int[getCacheSize()];
				this.nextValueIndex = 0;
				for (int i = 0; i < getCacheSize(); i++) {
					stmt.executeUpdate("insert into " + getIncrementerName() + " (" + getDummyName() + ") values(null)");
					ResultSet rs = stmt.executeQuery("select IDENTITY_VAL_LOCAL() from " + getIncrementerName());
					try {
						if (!rs.next()) {
							throw new DataAccessResourceFailureException("IDENTITY_VAL_LOCAL() failed after executing an update");
						}
						this.valueCache[i] = rs.getInt(1);
					}
					finally {
						JdbcUtils.closeResultSet(rs);
					}
				}
				long maxValue = this.valueCache[(this.valueCache.length - 1)];
				stmt.executeUpdate("delete from " + getIncrementerName() + " where " + getColumnName() + " < " + maxValue);
			}
			catch (SQLException ex) {
				throw new DataAccessResourceFailureException("Could not obtain IDENTITY value", ex);
			}
			finally {
				JdbcUtils.closeStatement(stmt);
				DataSourceUtils.releaseConnection(con, getDataSource());
			}
		}
		return this.valueCache[this.nextValueIndex++];
		
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hundsun.jres.interfaces.db.dialect.IDialect#getCurrentDate()
	 */
	public String getCurrentDate() {
		return "select now()";
	}

	public String buildSqlFuction(String sql) {
		return functionProcessor.getFuntionSql(sql, DataBaseUtil.DB_TYPE_DERBY);
	}

	/**
	 * Set the name of the dummy column.
	 */
	public void setDummyName(String dummyName) {
		this.dummyName = dummyName;
	}

	/**
	 * Return the name of the dummy column.
	 */
	public String getDummyName() {
		return this.dummyName;
	}

}
