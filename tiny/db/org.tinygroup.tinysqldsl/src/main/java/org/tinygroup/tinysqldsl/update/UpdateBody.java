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
package org.tinygroup.tinysqldsl.update;

import java.util.List;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.base.Column;
import org.tinygroup.tinysqldsl.base.SelectBody;
import org.tinygroup.tinysqldsl.base.StatementBody;
import org.tinygroup.tinysqldsl.base.Table;
import org.tinygroup.tinysqldsl.expression.Expression;
import org.tinygroup.tinysqldsl.formitem.FromItem;
import org.tinygroup.tinysqldsl.select.Join;
import org.tinygroup.tinysqldsl.util.DslUtil;

/**
 * The update statement.
 */
public class UpdateBody implements StatementBody {

	private List<Table> tables;
	private Expression where;
	private List<Column> columns;
	private List<Expression> expressions;
	private FromItem fromItem;
	private List<Join> joins;
	private SelectBody selectBody;
	private boolean useColumnsBrackets = true;
	private boolean useSelect = false;

	public List<Table> getTables() {
		return tables;
	}

	public Expression getWhere() {
		return where;
	}

	public void setTables(List<Table> list) {
		tables = list;
	}

	public void setWhere(Expression expression) {
		where = expression;
	}

	public List<Column> getColumns() {
		return columns;
	}

	/**
	 * The {@link Expression}s in this update (as 'a' and 'b' in UPDATE
	 * col1='a', col2='b')
	 * 
	 * @return a list of {@link Expression}s
	 */
	public List<Expression> getExpressions() {
		return expressions;
	}

	public void setColumns(List<Column> list) {
		columns = list;
	}

	public void setExpressions(List<Expression> list) {
		expressions = list;
	}

	public FromItem getFromItem() {
		return fromItem;
	}

	public void setFromItem(FromItem fromItem) {
		this.fromItem = fromItem;
	}

	public List<Join> getJoins() {
		return joins;
	}

	public void setJoins(List<Join> joins) {
		this.joins = joins;
	}

	public SelectBody getSelectBody() {
		return selectBody;
	}

	public void setSelectBody(SelectBody selectBody) {
		this.selectBody = selectBody;
	}

	public boolean isUseColumnsBrackets() {
		return useColumnsBrackets;
	}

	public void setUseColumnsBrackets(boolean useColumnsBrackets) {
		this.useColumnsBrackets = useColumnsBrackets;
	}

	public boolean isUseSelect() {
		return useSelect;
	}

	public void setUseSelect(boolean useSelect) {
		this.useSelect = useSelect;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("UPDATE ");
		b.append(DslUtil.getStringList(getTables(), true, false)).append(
				" SET ");

		if (!useSelect) {
			for (int i = 0; i < getColumns().size(); i++) {
				if (i != 0) {
					b.append(", ");
				}
				b.append(columns.get(i)).append(" = ");
				b.append(expressions.get(i));
			}
		} else {
			if (useColumnsBrackets) {
				b.append("(");
			}
			for (int i = 0; i < getColumns().size(); i++) {
				if (i != 0) {
					b.append(", ");
				}
				b.append(columns.get(i));
			}
			if (useColumnsBrackets) {
				b.append(")");
			}
			b.append(" = ");
			b.append("(").append(selectBody).append(")");
		}

		if (fromItem != null) {
			b.append(" FROM ").append(fromItem);
			if (joins != null) {
				for (Join join : joins) {
					if (join.isSimple()) {
						b.append(", ").append(join);
					} else {
						b.append(" ").append(join);
					}
				}
			}
		}

		if (where != null) {
			b.append(" WHERE ");
			b.append(where);
		}
		return b.toString();
	}

	public void builder(StatementSqlBuilder builder) {
		StringBuilder buffer = builder.getStringBuilder();
		buffer.append("UPDATE ")
				.append(DslUtil.getStringList(getTables(), true, false))
				.append(" SET ");

		if (!isUseSelect()) {
			for (int i = 0; i < getColumns().size(); i++) {
				Column column = getColumns().get(i);
				buffer.append(column.getFullyQualifiedName()).append(" = ");

				Expression expression = getExpressions().get(i);
				expression.builder(builder);
				if (i < getColumns().size() - 1) {
					buffer.append(", ");
				}
			}
		} else {
			if (isUseColumnsBrackets()) {
				buffer.append("(");
			}
			for (int i = 0; i < getColumns().size(); i++) {
				if (i != 0) {
					buffer.append(", ");
				}
				Column column = getColumns().get(i);
				buffer.append(column.getFullyQualifiedName());
			}
			if (isUseColumnsBrackets()) {
				buffer.append(")");
			}
			buffer.append(" = ");
			buffer.append("(");
			SelectBody selectBody = getSelectBody();
			selectBody.builder(builder);
			buffer.append(")");
		}

		if (getFromItem() != null) {
			buffer.append(" FROM ").append(getFromItem());
			if (getJoins() != null) {
				for (Join join : getJoins()) {
					if (join.isSimple()) {
						buffer.append(", ").append(join);
					} else {
						buffer.append(" ").append(join);
					}
				}
			}
		}

		if (getWhere() != null) {
			buffer.append(" WHERE ");
			getWhere().builder(builder);
		}

	}
}
