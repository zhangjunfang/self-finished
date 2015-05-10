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
package org.tinygroup.jsqlparser.statement.alter;

import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.StatementVisitor;
import org.tinygroup.jsqlparser.statement.create.table.ColDataType;

/**
 *
 * @author toben
 */
public class Alter implements Statement {
	private Table table;
	private String columnName;
	private ColDataType dataType;

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public ColDataType getDataType() {
		return dataType;
	}

	public void setDataType(ColDataType dataType) {
		this.dataType = dataType;
	}


	public void accept(StatementVisitor statementVisitor) {
		statementVisitor.visit(this);
	}


	public String toString() {
		return "ALTER TABLE " + table.getFullyQualifiedName() + " ADD COLUMN " + columnName + " " + dataType.toString();
	}
}
