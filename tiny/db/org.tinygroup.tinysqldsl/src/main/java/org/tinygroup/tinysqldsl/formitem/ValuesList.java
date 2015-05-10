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
package org.tinygroup.tinysqldsl.formitem;

import java.util.Iterator;
import java.util.List;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.base.Alias;
import org.tinygroup.tinysqldsl.expression.relational.ExpressionList;
import org.tinygroup.tinysqldsl.expression.relational.MultiExpressionList;
import org.tinygroup.tinysqldsl.util.DslUtil;

/**
 * values子句
 * @author renhui
 *
 */
public class ValuesList implements FromItem {

	private Alias alias;
	private MultiExpressionList multiExpressionList;
	private boolean noBrackets = false;
	private List<String> columnNames;

	public ValuesList() {
	}

	public ValuesList(MultiExpressionList multiExpressionList) {
		this.multiExpressionList = multiExpressionList;
	}

	public Alias getAlias() {
		return alias;
	}

	public void setAlias(Alias alias) {
		this.alias = alias;
	}

	public MultiExpressionList getMultiExpressionList() {
		return multiExpressionList;
	}

	public void setMultiExpressionList(MultiExpressionList multiExpressionList) {
		this.multiExpressionList = multiExpressionList;
	}

	public boolean isNoBrackets() {
		return noBrackets;
	}

	public void setNoBrackets(boolean noBrackets) {
		this.noBrackets = noBrackets;
	}

	public String toString() {
		StringBuilder b = new StringBuilder();

		b.append("(VALUES ");
		for (Iterator<ExpressionList> it = getMultiExpressionList()
				.getExprList().iterator(); it.hasNext();) {
			b.append(DslUtil.getStringList(it.next().getExpressions(), true,
					!isNoBrackets()));
			if (it.hasNext()) {
				b.append(", ");
			}
		}
		b.append(")");
		if (alias != null) {
			b.append(alias.toString());

			if (columnNames != null) {
				b.append("(");
				for (Iterator<String> it = columnNames.iterator(); it.hasNext();) {
					b.append(it.next());
					if (it.hasNext()) {
						b.append(", ");
					}
				}
				b.append(")");
			}
		}
		return b.toString();
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public void builder(StatementSqlBuilder builder) {
		builder.appendSql(toString());
	}
}
