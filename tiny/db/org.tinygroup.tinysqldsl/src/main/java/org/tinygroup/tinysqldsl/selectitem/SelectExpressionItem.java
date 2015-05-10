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
package org.tinygroup.tinysqldsl.selectitem;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.base.Alias;
import org.tinygroup.tinysqldsl.expression.Expression;

/**
 * An expression as in "SELECT expr1 AS EXPR"
 */
public class SelectExpressionItem implements SelectItem {

	private Expression expression;
	private Alias alias;

	public SelectExpressionItem() {
	}

	public SelectExpressionItem(Expression expression) {
		this.expression = expression;
	}

	public Alias getAlias() {
		return alias;
	}

	public Expression getExpression() {
		return expression;
	}

	public void setAlias(Alias alias) {
		this.alias = alias;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	public String toString() {
		return expression + ((alias != null) ? alias.toString() : "");
	}

	public void builder(StatementSqlBuilder builder) {
		getExpression().builder(builder);
		if (getAlias() != null) {
			builder.appendSql(getAlias().toString());
		}
	}
}
