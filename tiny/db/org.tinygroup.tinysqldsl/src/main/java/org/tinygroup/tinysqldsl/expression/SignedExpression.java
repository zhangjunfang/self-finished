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
 * It represents a "-" or "+" before an expression
 */
public class SignedExpression implements Expression {

	private char sign;
	private Expression expression;

	public SignedExpression(char sign, Expression expression) {
		setSign(sign);
		setExpression(expression);
	}

	public char getSign() {
		return sign;
	}

	public final void setSign(char sign) {
		this.sign = sign;
		if (sign != '+' && sign != '-') {
			throw new IllegalArgumentException(
					"illegal sign character, only + - allowed");
		}
	}

	public Expression getExpression() {
		return expression;
	}

	public final void setExpression(Expression expression) {
		this.expression = expression;
	}

	public String toString() {
		return getSign() + expression.toString();
	}

	public void builder(StatementSqlBuilder builder) {
		StringBuilder buffer = builder.getStringBuilder();
		buffer.append(getSign());
		getExpression().builder(builder);
	}
}
