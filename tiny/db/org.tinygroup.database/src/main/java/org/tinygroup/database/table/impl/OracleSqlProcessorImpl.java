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

import org.tinygroup.database.config.table.Table;

public class OracleSqlProcessorImpl extends SqlProcessorImpl {

	protected String getDatabaseType() {
		return "oracle";
	}

	protected String getQueryForeignSql(Table table,String schema) {
		String sql = "Select a.constraint_name CONSTRAINT_NAME,"
				+ "a.column_name  COLUMN_NAME,"
				+ "b.table_name  REFERENCED_TABLE_NAME,"
				+ "b.column_name REFERENCED_COLUMN_NAME"
				+ " From (Select a.owner,a.constraint_name,"
				+ "b.table_name,b.column_name,a.r_constraint_name"
				+ " From user_constraints a, user_cons_columns b"
				+ " Where a.constraint_type = 'R'"
				+ " And a.constraint_name = b.constraint_name) a,"
				+ "(Select Distinct a.r_constraint_name, b.table_name, b.column_name"
				+ " From user_constraints a, user_cons_columns b"
				+ " Where a.constraint_type = 'R'"
				+ " And a.r_constraint_name = b.constraint_name) b"
				+ " Where a.r_constraint_name = b.r_constraint_name"
				+ " and a.table_name ='" + table.getName().toUpperCase()
				+ "' and a.owner='" + schema.toUpperCase() + "'";
		return sql;
	}

	protected String createNotNullSql(String tableName, String fieldName,String tableDataType) {
		return String.format("ALTER TABLE %s MODIFY %s NOT NULL", tableName,
				fieldName);
	}

	protected String createNullSql(String tableName, String fieldName,String tableDataType) {
		return String.format("ALTER TABLE %s MODIFY %s NULL", tableName,
				fieldName);
	}

	protected String createAlterTypeSql(String tableName, String fieldName,
			String tableDataType) {
		return String.format("ALTER TABLE %s MODIFY %s %s ", tableName,
				fieldName, tableDataType);
	}
}
