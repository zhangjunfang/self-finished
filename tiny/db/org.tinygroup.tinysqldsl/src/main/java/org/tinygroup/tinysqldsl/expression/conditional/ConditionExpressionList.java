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
package org.tinygroup.tinysqldsl.expression.conditional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.expression.Expression;
import org.tinygroup.tinysqldsl.expression.relational.ExpressionList;
import org.tinygroup.tinysqldsl.util.DslUtil;

/**
 * 多个条件组成的表达式列表
 * 
 * @author renhui
 * 
 */
public class ConditionExpressionList implements Expression {
	/**
	 * 表达式列表
	 */
	private List<Expression> expressions;
	/**
	 * 多个表达式之间的连接符，比如and、or
	 */
	private String comma = ",";

	/**
	 * 表达式之间是否包含括号
	 */
	private boolean useBrackets = true;
	/**
	 * 是否用连接符
	 */
	private boolean useComma = true;

	public ConditionExpressionList() {
		expressions = new ArrayList<Expression>();
	}

	public ConditionExpressionList(List<Expression> expressions) {
		this.expressions = expressions;
	}

	public List<Expression> getExpressions() {
		return expressions;
	}

	public String getComma() {
		return comma;
	}

	public void setComma(String comma) {
		this.comma = comma;
	}

	public boolean isUseBrackets() {
		return useBrackets;
	}

	public void setUseBrackets(boolean useBrackets) {
		this.useBrackets = useBrackets;
	}

	public boolean isUseComma() {
		return useComma;
	}

	public void setUseComma(boolean useComma) {
		this.useComma = useComma;
	}

	public void setExpressions(List<Expression> list) {
		expressions = list;
	}

	public void addExpression(Expression expression) {
		if (expressions == null) {
			expressions = new ArrayList<Expression>();
		}
		expressions.add(expression);
	}

	public static ExpressionList expressionList(Expression expr) {
		return new ExpressionList(Collections.singletonList(expr));
	}

	public String toString() {
		return DslUtil.getStringList(expressions, useComma, useBrackets, comma);
	}

	public void builder(StatementSqlBuilder builder) {
		builder.appendSql(toString());
	}

}
