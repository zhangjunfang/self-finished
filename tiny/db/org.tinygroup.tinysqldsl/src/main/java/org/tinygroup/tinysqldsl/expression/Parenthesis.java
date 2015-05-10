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
package org.tinygroup.tinysqldsl.expression;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;

/**
 * It represents an expression like "(" expression ")"
 */
public class Parenthesis implements Expression {

	private Expression expression;
	private boolean not = false;

	public Parenthesis(Expression expression) {
		this(expression, false);
	}

	public Parenthesis(Expression expression, boolean not) {
		super();
		this.expression = expression;
		this.not = not;
	}

	public Expression getExpression() {
		return expression;
	}

	public void setNot() {
		not = true;
	}

	public boolean isNot() {
		return not;
	}

	public String toString() {
		return (not ? "NOT " : "") + "(" + expression + ")";
	}

	public void builder(StatementSqlBuilder builder) {
		StringBuilder buffer = builder.getStringBuilder();
		if (isNot()) {
			buffer.append(" NOT ");
		}

		buffer.append("(");
		getExpression().builder(builder);
		buffer.append(")");
	}
}
