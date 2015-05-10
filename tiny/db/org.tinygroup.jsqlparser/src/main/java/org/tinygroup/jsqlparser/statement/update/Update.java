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
package org.tinygroup.jsqlparser.statement.update;

import java.util.List;

import org.tinygroup.jsqlparser.expression.Expression;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.StatementVisitor;
import org.tinygroup.jsqlparser.statement.select.FromItem;
import org.tinygroup.jsqlparser.statement.select.Join;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;
import org.tinygroup.jsqlparser.statement.select.Select;

/**
 * The update statement.
 */
public class Update implements Statement {

	private List<Table> tables;
	private Expression where;
	private List<Column> columns;
	private List<Expression> expressions;
	private FromItem fromItem;
	private List<Join> joins;
	private Select select;
	private boolean useColumnsBrackets = true;
	private boolean useSelect = false;

	
	public void accept(StatementVisitor statementVisitor) {
		statementVisitor.visit(this);
	}

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

	/**
	 * The {@link org.tinygroup.jsqlparser.schema.Column}s in this update (as col1 and
	 * col2 in UPDATE col1='a', col2='b')
	 *
	 * @return a list of {@link org.tinygroup.jsqlparser.schema.Column}s
	 */
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

	public Select getSelect() {
	        return select;
    	}
	
    	public void setSelect(Select select) {
	        this.select = select;
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
		b.append(PlainSelect.getStringList(getTables(), true, false)).append(" SET ");
		
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
			b.append("(").append(select).append(")");
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
}
