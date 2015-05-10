package org.tinygroup.tinysqldsl.expression.relational;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.expression.Expression;

public class IsEmptyExpression implements Expression {

	private Expression leftExpression;
	private boolean not = false;

	public IsEmptyExpression(Expression leftExpression) {
		this(leftExpression, false);
	}

	public IsEmptyExpression(Expression leftExpression, boolean not) {
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
		return leftExpression + ((not) ? "<>" : "=") + "''";
	}

	public void builder(StatementSqlBuilder builder) {
		StringBuilder buffer = builder.getStringBuilder();
		getLeftExpression().builder(builder);
		if (isNot()) {
			buffer.append("<>''");
		} else {
			buffer.append("=''");
		}
	}
}
