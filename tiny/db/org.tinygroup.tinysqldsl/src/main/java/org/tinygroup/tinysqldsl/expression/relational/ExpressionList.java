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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.expression.Expression;
import org.tinygroup.tinysqldsl.util.DslUtil;

/**
 * A list of expressions, as in SELECT A FROM TAB WHERE B IN (expr1,expr2,expr3)
 */
public class ExpressionList implements ItemsList {

	private List<Expression> expressions;

	private String comma = ",";

	private boolean useBrackets = true;

	private boolean useComma = true;

	public ExpressionList() {
		expressions = new ArrayList<Expression>();
	}

	public ExpressionList(List<Expression> expressions) {
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
		boolean useBracketsInExprList = builder.isUseBracketsInExprList();
		StringBuilder buffer = builder.getStringBuilder();
		if (useBracketsInExprList) {
			buffer.append("(");
		}
		for (Iterator<Expression> iter = getExpressions()
				.iterator(); iter.hasNext();) {
			Expression expression = iter.next();
			expression.builder(builder);
			if (iter.hasNext()) {
				buffer.append(", ");
			}
		}
		if (useBracketsInExprList) {
			buffer.append(")");
		}
	}
}
