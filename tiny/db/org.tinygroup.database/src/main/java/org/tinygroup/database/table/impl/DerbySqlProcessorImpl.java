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


public class DerbySqlProcessorImpl extends SqlProcessorImpl {

	protected String getDatabaseType() {
		return "derby";
	}

	protected String appendIncrease() {
		return " GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) ";
	}
	
	protected String createNotNullSql(String tableName, String fieldName,
			String tableDataType) {
		return String.format("ALTER TABLE %s ALTER  %s NOT NULL",
				tableName, fieldName);
	}

	protected String createNullSql(String tableName, String fieldName,
			String tableDataType) {
		return String.format("ALTER TABLE %s ALTER  %s NULL",
				tableName, fieldName);
	}

	protected String createAlterTypeSql(String tableName, String fieldName,
			String tableDataType) {
		return String.format("ALTER TABLE %s ALTER %s SET DATA TYPE %s", tableName,fieldName,tableDataType);
	}
	
}
