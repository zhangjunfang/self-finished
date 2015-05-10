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
package org.tinygroup.tinydb.dialect.impl;

import org.tinygroup.database.util.DataBaseUtil;

public class OracleDialect extends AbstractSequenceDialcet{
	

	public boolean supportsLimit() {
		return true;
	}

	public String getLimitString(String sql, int offset, int limit) {
		StringBuffer pagingSelect = new StringBuffer();
		int start=offset;
		if (offset == 0){
			start = 1;
		}	
		pagingSelect
				.append("select * from ( select row_.*, rownum jres_db_rownum_ from ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) row_ where rownum <=" + (start + limit - 1)
				+ ") where jres_db_rownum_ >=" + start);

		return pagingSelect.toString();
	}


	public String getCurrentDate() {
		return "select  to_char(sysdate,'YYYY-MM-DD HH24:MI:SS') from dual";
	}

	public String buildSqlFuction(String sql) {
		return functionProcessor.getFuntionSql(sql, DataBaseUtil.DB_TYPE_ORACLE);
	}
	
	protected String getSequenceQuery() {
		return "select " + getIncrementerName() + ".nextval from dual";
	}
}
