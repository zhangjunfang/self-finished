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

import org.tinygroup.tinysqldsl.expression.BinaryExpression;
import org.tinygroup.tinysqldsl.expression.Expression;


public abstract class OldOracleJoinBinaryExpression extends BinaryExpression implements SupportsOldOracleJoinSyntax {

    private int oldOracleJoinSyntax = NO_ORACLE_JOIN;

    private int oraclePriorPosition = NO_ORACLE_PRIOR;


    public OldOracleJoinBinaryExpression(Expression leftExpression,
                                         Expression rightExpression, boolean not) {
        super(leftExpression, rightExpression, not);
    }


    public OldOracleJoinBinaryExpression(Expression leftExpression,
                                         Expression rightExpression) {
        super(leftExpression, rightExpression);
    }


    public void setOldOracleJoinSyntax(int oldOracleJoinSyntax) {
        this.oldOracleJoinSyntax = oldOracleJoinSyntax;
        if (oldOracleJoinSyntax < 0 || oldOracleJoinSyntax > 2) {
            throw new IllegalArgumentException("unknown join type for oracle found (type=" + oldOracleJoinSyntax + ")");
        }
    }


    public String toString() {
        return (isNot() ? "NOT " : "") + (oraclePriorPosition == ORACLE_PRIOR_START ? "PRIOR " : "")
                + getLeftExpression() + (oldOracleJoinSyntax == ORACLE_JOIN_RIGHT ? "(+)" : "") + " "
                + getStringExpression() + " " + (oraclePriorPosition == ORACLE_PRIOR_END ? "PRIOR " : "")
                + getRightExpression() + (oldOracleJoinSyntax == ORACLE_JOIN_LEFT ? "(+)" : "");
    }


    public int getOldOracleJoinSyntax() {
        return oldOracleJoinSyntax;
    }


    public int getOraclePriorPosition() {
        return oraclePriorPosition;
    }


    public void setOraclePriorPosition(int oraclePriorPosition) {
        this.oraclePriorPosition = oraclePriorPosition;
    }
}
