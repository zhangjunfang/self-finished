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

public class InExpression implements Expression, SupportsOldOracleJoinSyntax {

	private Expression leftExpression;
	private ItemsList leftItemsList;
	private ItemsList rightItemsList;
	private boolean not = false;

	private int oldOracleJoinSyntax = NO_ORACLE_JOIN;
	
	public InExpression(Expression leftExpression, ItemsList rightItemsList) {
		this(leftExpression, rightItemsList, false);
	}

	public InExpression(Expression leftExpression, ItemsList rightItemsList,
			boolean not) {
		super();
		this.leftExpression = leftExpression;
		this.rightItemsList = rightItemsList;
		this.not = not;
	}

	public InExpression(ItemsList leftItemsList, ItemsList rightItemsList) {
		this(leftItemsList, rightItemsList, false);
	}

	public InExpression(ItemsList leftItemsList, ItemsList rightItemsList,
			boolean not) {
		super();
		this.leftItemsList = leftItemsList;
		this.rightItemsList = rightItemsList;
		this.not = not;
	}

	public void setOldOracleJoinSyntax(int oldOracleJoinSyntax) {
		this.oldOracleJoinSyntax = oldOracleJoinSyntax;
		if (oldOracleJoinSyntax < 0 || oldOracleJoinSyntax > 1) {
			throw new IllegalArgumentException(
					"unexpected join type for oracle found with IN (type="
							+ oldOracleJoinSyntax + ")");
		}
	}

	public int getOldOracleJoinSyntax() {
		return oldOracleJoinSyntax;
	}

	public ItemsList getRightItemsList() {
		return rightItemsList;
	}

	public Expression getLeftExpression() {
		return leftExpression;
	}

	public boolean isNot() {
		return not;
	}
	
	public void setLeftExpression(Expression leftExpression) {
		this.leftExpression = leftExpression;
	}
	public void setLeftItemsList(ItemsList leftItemsList) {
		this.leftItemsList = leftItemsList;
	}
	public void setRightItemsList(ItemsList rightItemsList) {
		this.rightItemsList = rightItemsList;
	}
	public void setNot(boolean not) {
		this.not = not;
	}
	public ItemsList getLeftItemsList() {
		return leftItemsList;
	}

	private String getLeftExpressionString() {
		return leftExpression
				+ (oldOracleJoinSyntax == ORACLE_JOIN_RIGHT ? "(+)" : "");
	}

	public String toString() {
		return (leftExpression == null ? leftItemsList
				: getLeftExpressionString())
				+ " "
				+ ((not) ? "NOT " : "")
				+ "IN " + rightItemsList + "";
	}

	public int getOraclePriorPosition() {
		return SupportsOldOracleJoinSyntax.NO_ORACLE_PRIOR;
	}

	public void setOraclePriorPosition(int priorPosition) {
		if (priorPosition != SupportsOldOracleJoinSyntax.NO_ORACLE_PRIOR) {
			throw new IllegalArgumentException(
					"unexpected prior for oracle found");
		}
	}

	public void builder(StatementSqlBuilder builder) {
		StringBuilder buffer = builder.getStringBuilder();
		if (getLeftExpression() == null) {
			getLeftItemsList().builder(builder);
		} else {
			getLeftExpression().builder(builder);
			if (getOldOracleJoinSyntax() == SupportsOldOracleJoinSyntax.ORACLE_JOIN_RIGHT) {
				buffer.append("(+)");
			}
		}
		if (isNot()) {
			buffer.append(" NOT");
		}
		buffer.append(" IN ");
		getRightItemsList().builder(builder);
	}
}
