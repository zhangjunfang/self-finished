/**
 * Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 * <p/>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.gnu.org/licenses/gpl.html
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.tinysqldsl.base;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.tinysqldsl.expression.Expression;
import org.tinygroup.tinysqldsl.operator.ColumnOperator;
import org.tinygroup.tinysqldsl.selectitem.Distinct;
import org.tinygroup.tinysqldsl.selectitem.SelectItem;

/**
 * 列
 */
public class Column extends ColumnOperator implements Expression,
		MultiPartName, SelectItem {
	/**
	 * 表名
	 */
	private Table table;
	/**
	 * 列名
	 */
	private String columnName;
	/**
	 * 别名
	 */
	private Alias alias;

	public Column() {
	}

	public Column(Table table, String columnName) {
		this.table = table;
		this.columnName = columnName;
	}

	public Column(Table table, String columnName, String alias) {
		this(table, columnName);
		this.alias = new Alias(alias);
	}

	public Column(String columnName) {
		this(null, columnName);
	}

	public Distinct distinct() {
		return new Distinct(this);
	}

	public Value value(Object value) {
		return new Value(this, value);
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String string) {
		columnName = string;
	}

	public Alias getAlias() {
		return alias;
	}

	public void setAlias(Alias alias) {
		this.alias = alias;
	}

	public String getFullyQualifiedName() {
		StringBuilder fqn = new StringBuilder();

		if (table != null) {
			fqn.append(table.getReffenceName());
		}
		if (fqn.length() > 0) {
			fqn.append('.');
		}
		if (columnName != null) {
			fqn.append(columnName);
		}
		return fqn.toString();
	}

	public String toString() {
		return getFullyQualifiedName()
				+ ((alias != null) ? alias.toString() : "");
	}


	public void builder(StatementSqlBuilder builder) {
		String tableName = getTableName();
		String result = "";
		if (!StringUtil.isBlank(tableName)) {
			result += tableName + ".";
		}
		result += columnName;
		builder.appendSql(result);
	}

	private String getTableName() {
		String tableName = null;
		if (table != null) {
			if (table.getAlias() != null) {
				tableName = table.getAlias().getName();
			} else {
				tableName = table.getFullyQualifiedName();
			}
		}
		return tableName;
	}

}