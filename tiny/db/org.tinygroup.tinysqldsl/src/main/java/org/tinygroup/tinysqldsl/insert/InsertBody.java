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
package org.tinygroup.tinysqldsl.insert;

import java.util.Iterator;
import java.util.List;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.base.Column;
import org.tinygroup.tinysqldsl.base.SelectBody;
import org.tinygroup.tinysqldsl.base.StatementBody;
import org.tinygroup.tinysqldsl.base.Table;
import org.tinygroup.tinysqldsl.expression.relational.ItemsList;
import org.tinygroup.tinysqldsl.selectitem.SelectExpressionItem;
import org.tinygroup.tinysqldsl.util.DslUtil;

/**
 * Insert语句
 */
public class InsertBody implements StatementBody {
	/**
	 * 要插入的表
	 */
	private Table table;
	/**
	 * 要插入的字段列表
	 */
	private List<Column> columns;
	/**
	 * 要插入的值列表
	 */
	private ItemsList itemsList;
	private boolean useValues = true;
	private SelectBody selectBody;
	private boolean useSelectBrackets = false;
	private boolean returningAllColumns = false;

	private List<SelectExpressionItem> returningExpressionList = null;

	public Table getTable() {
		return table;
	}

	public void setTable(Table name) {
		table = name;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> list) {
		columns = list;
	}

	public ItemsList getItemsList() {
		return itemsList;
	}

	public void setItemsList(ItemsList list) {
		itemsList = list;
	}

	public boolean isUseValues() {
		return useValues;
	}

	public void setUseValues(boolean useValues) {
		this.useValues = useValues;
	}

	public boolean isReturningAllColumns() {
		return returningAllColumns;
	}

	public void setReturningAllColumns(boolean returningAllColumns) {
		this.returningAllColumns = returningAllColumns;
	}

	public List<SelectExpressionItem> getReturningExpressionList() {
		return returningExpressionList;
	}

	public void setReturningExpressionList(
			List<SelectExpressionItem> returningExpressionList) {
		this.returningExpressionList = returningExpressionList;
	}

	public SelectBody getSelectBody() {
		return selectBody;
	}

	public void setSelectBody(SelectBody selectBody) {
		this.selectBody = selectBody;
	}

	public boolean isUseSelectBrackets() {
		return useSelectBrackets;
	}

	public void setUseSelectBrackets(boolean useSelectBrackets) {
		this.useSelectBrackets = useSelectBrackets;
	}

	public String toString() {
		StringBuilder sql = new StringBuilder();

		sql.append("INSERT INTO ");
		sql.append(table).append(" ");
		if (columns != null) {
			sql.append(DslUtil.getStringList(columns, true, true)).append(" ");
		}

		if (useValues) {
			sql.append(" VALUES");
		}

		if (itemsList != null) {
			sql.append(itemsList);
		}

		if (useSelectBrackets) {
			sql.append("(");
		}
		if (selectBody != null) {
			sql.append(selectBody);
		}
		if (useSelectBrackets) {
			sql.append(")");
		}

		if (isReturningAllColumns()) {
			sql.append(" RETURNING *");
		} else if (getReturningExpressionList() != null) {
			sql.append(" RETURNING ").append(
					DslUtil.getStringList(getReturningExpressionList(), true,
							false));
		}

		return sql.toString();
	}

	public void builder(StatementSqlBuilder builder) {
		StringBuilder buffer = builder.getStringBuilder();
		buffer.append("INSERT INTO ");
		buffer.append(getTable().getFullyQualifiedName());
		if (getColumns() != null) {
			buffer.append(" (");
			for (Iterator<Column> iter = getColumns().iterator(); iter
					.hasNext();) {
				Column column = iter.next();
				buffer.append(column.getColumnName());
				if (iter.hasNext()) {
					buffer.append(", ");
				}
			}
			buffer.append(")");
		}
		if (useValues) {
			buffer.append(" VALUES");
		}
		if (getItemsList() != null) {
			getItemsList().builder(builder);
		}

		if (getSelectBody() != null) {
			buffer.append(" ");
			if (isUseSelectBrackets()) {
				buffer.append("(");
			}
			getSelectBody().builder(builder);
			if (isUseSelectBrackets()) {
				buffer.append(")");
			}
		}

		if (isReturningAllColumns()) {
			buffer.append(" RETURNING *");
		} else if (getReturningExpressionList() != null) {
			buffer.append(" RETURNING ");
			for (Iterator<SelectExpressionItem> iter = getReturningExpressionList()
					.iterator(); iter.hasNext();) {
				buffer.append(iter.next().toString());
				if (iter.hasNext()) {
					buffer.append(", ");
				}
			}
		}
	}

}
