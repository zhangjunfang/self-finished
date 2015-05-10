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

/**
 * A "BETWEEN" expr1 expr2 statement
 */
public class Between implements Expression {

	private Expression leftExpression;
	private boolean not = false;
	private Expression betweenExpressionStart;
	private Expression betweenExpressionEnd;

	public Between(Expression leftExpression,
			Expression betweenExpressionStart, Expression betweenExpressionEnd) {
		this(leftExpression, betweenExpressionStart, betweenExpressionEnd,
				false);
	}

	public Between(Expression leftExpression,
			Expression betweenExpressionStart, Expression betweenExpressionEnd,
			boolean not) {
		super();
		this.leftExpression = leftExpression;
		this.betweenExpressionStart = betweenExpressionStart;
		this.betweenExpressionEnd = betweenExpressionEnd;
		this.not = not;
	}

	public Expression getBetweenExpressionEnd() {
		return betweenExpressionEnd;
	}

	public Expression getBetweenExpressionStart() {
		return betweenExpressionStart;
	}

	public Expression getLeftExpression() {
		return leftExpression;
	}

	public boolean isNot() {
		return not;
	}

	public void setBetweenExpressionEnd(Expression expression) {
		betweenExpressionEnd = expression;
	}

	public void setBetweenExpressionStart(Expression expression) {
		betweenExpressionStart = expression;
	}

	public void setLeftExpression(Expression expression) {
		leftExpression = expression;
	}

	public void setNot(boolean b) {
		not = b;
	}

	public String toString() {
		return leftExpression + " " + (not ? "NOT " : "") + "BETWEEN "
				+ betweenExpressionStart + " AND " + betweenExpressionEnd;
	}


	public void builder(StatementSqlBuilder builder) {
		getLeftExpression().builder(builder);
		StringBuilder buffer = builder.getStringBuilder();
		if (isNot()) {
			buffer.append(" NOT");
		}

		buffer.append(" BETWEEN ");
		getBetweenExpressionStart().builder(builder);
		buffer.append(" AND ");
		getBetweenExpressionEnd().builder(builder);
	}
}
