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

public class OracleHierarchicalExpression implements Expression {

	private Expression startExpression;
	private Expression connectExpression;
	private boolean noCycle = false;

	public OracleHierarchicalExpression() {
		super();
	}

	public OracleHierarchicalExpression(Expression startExpression,
			Expression connectExpression, boolean noCycle) {
		super();
		this.startExpression = startExpression;
		this.connectExpression = connectExpression;
		this.noCycle = noCycle;
	}

	public Expression getStartExpression() {
		return startExpression;
	}

	public void setStartExpression(Expression startExpression) {
		this.startExpression = startExpression;
	}

	public Expression getConnectExpression() {
		return connectExpression;
	}

	public void setConnectExpression(Expression connectExpression) {
		this.connectExpression = connectExpression;
	}

	public boolean isNoCycle() {
		return noCycle;
	}

	public void setNoCycle(boolean noCycle) {
		this.noCycle = noCycle;
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		if (startExpression != null) {
			b.append(" START WITH ").append(startExpression.toString());
		}
		b.append(" CONNECT BY ");
		if (isNoCycle()) {
			b.append("NOCYCLE ");
		}
		b.append(connectExpression.toString());
		return b.toString();
	}

	public void builder(StatementSqlBuilder builder) {
		builder.appendSql(toString());
	}
}
