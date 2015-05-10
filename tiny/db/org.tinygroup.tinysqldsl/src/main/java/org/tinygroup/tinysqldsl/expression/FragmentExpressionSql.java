package org.tinygroup.tinysqldsl.expression;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.base.FragmentSql;

/**
 * 表达式的sql片段
 * 
 * @author renhui
 */
public class FragmentExpressionSql extends FragmentSql implements Expression {

	public FragmentExpressionSql(String fragment) {
		super(fragment);
	}

	public void builder(StatementSqlBuilder builder) {
		builder.appendSql(getFragment());
	}

}
