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
package org.tinygroup.tinysqldsl.expression.relational;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.expression.Expression;

public class IsNullExpression implements Expression {

	private Expression leftExpression;
	private boolean not = false;

	public IsNullExpression(Expression leftExpression) {
		this(leftExpression, false);
	}

	public IsNullExpression(Expression leftExpression, boolean not) {
		super();
		this.leftExpression = leftExpression;
		this.not = not;
	}

	public Expression getLeftExpression() {
		return leftExpression;
	}

	public boolean isNot() {
		return not;
	}

	public void setLeftExpression(Expression expression) {
		leftExpression = expression;
	}

	public void setNot(boolean b) {
		not = b;
	}

	public String toString() {
		return leftExpression + " IS " + ((not) ? "NOT " : "") + "NULL";
	}

	public void builder(StatementSqlBuilder builder) {
		StringBuilder buffer = builder.getStringBuilder();
		getLeftExpression().builder(builder);
		if (isNot()) {
			buffer.append(" IS NOT NULL");
		} else {
			buffer.append(" IS NULL");
		}
	}
}
