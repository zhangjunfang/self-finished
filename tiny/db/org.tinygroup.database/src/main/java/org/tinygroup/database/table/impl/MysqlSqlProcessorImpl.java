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
package org.tinygroup.database.table.impl;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.util.DataBaseUtil;

public class MysqlSqlProcessorImpl extends SqlProcessorImpl {

	protected String getDatabaseType() {
		return "mysql";
	}

	protected String appendIncrease() {
		return " auto_increment ";
	}

	public boolean checkTableExist(Table table, String catalog,
			DatabaseMetaData metadata) {
		ResultSet r = null;
		try {
			String schema = DataBaseUtil.getSchema(table, metadata);
			r = metadata.getTables(catalog, schema,
					table.getNameWithOutSchema(), new String[] { "TABLE" });

			if (r.next()) {
				return true;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			DataBaseUtil.closeResultSet(r);
		}

		return false;
	}


	protected String getQueryForeignSql(Table table,String schema) {
		 String sql = "SELECT c.COLUMN_NAME, tc.CONSTRAINT_NAME,fc.REFERENCED_TABLE_NAME,kcu.REFERENCED_COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS c"
			+ " LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE kcu ON kcu.TABLE_SCHEMA = c.TABLE_SCHEMA"
			+ " AND kcu.TABLE_NAME = c.TABLE_NAME"
			+ " AND kcu.COLUMN_NAME = c.COLUMN_NAME"
			+ " LEFT JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc ON tc.CONSTRAINT_SCHEMA = kcu.CONSTRAINT_SCHEMA"
			+ " AND tc.CONSTRAINT_NAME = kcu.CONSTRAINT_NAME"
			+ " LEFT JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS fc ON kcu.CONSTRAINT_SCHEMA = fc.CONSTRAINT_SCHEMA"
			+ " AND kcu.CONSTRAINT_NAME = fc.CONSTRAINT_NAME"
			+ " where tc.CONSTRAINT_TYPE='FOREIGN KEY' and c.table_name='"
			+ table.getName()
			+ "' and c.table_schema='"
			+ schema + "'";
		 return sql;
	}

	protected String createNotNullSql(String tableName, String fieldName,String tableDataType) {
		return String.format(
				"ALTER TABLE %s CHANGE %s %s %s NOT NULL",
				tableName, fieldName, fieldName, tableDataType);
	}

	protected String createNullSql(String tableName, String fieldName,String tableDataType) {
		return String.format(
				"ALTER TABLE %s CHANGE %s %s %s NULL", tableName,
				fieldName, fieldName, tableDataType);
	}

	protected String createAlterTypeSql(String tableName, String fieldName,
			String tableDataType) {
		return String.format(
				"ALTER TABLE %s CHANGE %s %s %s", tableName,
				fieldName, fieldName, tableDataType);
	}


}