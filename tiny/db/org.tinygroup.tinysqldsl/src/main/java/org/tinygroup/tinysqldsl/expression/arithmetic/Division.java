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
package org.tinygroup.tinysqldsl.expression.arithmetic;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;
import org.tinygroup.tinysqldsl.expression.BinaryExpression;
import org.tinygroup.tinysqldsl.expression.Expression;

/**
 * 除法
 * 
 * @author renhui
 * 
 */
public class Division extends BinaryExpression {

	public Division(Expression leftExpression, Expression rightExpression) {
		super(leftExpression, rightExpression);
	}

	public String getStringExpression() {
		return "/";
	}

	public void builder(StatementSqlBuilder builder) {
		builder.visitBinaryExpression(this, " / ");
	}
}
