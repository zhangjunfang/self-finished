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
package org.tinygroup.tinysqldsl.select;

import org.tinygroup.tinysqldsl.base.Column;
import org.tinygroup.tinysqldsl.expression.Expression;
import org.tinygroup.tinysqldsl.formitem.FromItem;
import org.tinygroup.tinysqldsl.util.DslUtil;

import java.util.List;

/**
 * A join clause
 */
public class Join {

    private boolean outer = false;
    private boolean right = false;
    private boolean left = false;
    private boolean natural = false;
    private boolean full = false;
    private boolean inner = false;
    private boolean simple = false;
    private boolean cross = false;
    private FromItem rightItem;
    private Expression onExpression;
    private List<Column> usingColumns;

    public Join(FromItem rightItem, Expression onExpression) {
        super();
        this.rightItem = rightItem;
        this.onExpression = onExpression;
    }

    public static Join simpleJoin(FromItem rightItem, Expression onExpression) {
        Join join = newJoin(rightItem, onExpression);
        join.simple = true;
        return join;
    }

    public static Join rightOutJoin(FromItem rightItem, Expression onExpression) {
        Join join = newJoin(rightItem, onExpression);
        join.outer = true;
        join.right = true;
        return join;
    }

    public static Join leftOutJoin(FromItem rightItem, Expression onExpression) {
        Join join = newJoin(rightItem, onExpression);
        join.outer = true;
        join.left = true;
        return join;
    }

    public static Join fullOutJoin(FromItem rightItem, Expression onExpression) {
        Join join = newJoin(rightItem, onExpression);
        join.outer = true;
        join.full = true;
        return join;
    }

    public static Join rightJoin(FromItem rightItem,
                                 Expression onExpression) {
        Join join = newJoin(rightItem, onExpression);
        join.inner = true;
        join.right = true;
        return join;
    }

    public static Join leftJoin(FromItem rightItem, Expression onExpression) {
        Join join = newJoin(rightItem, onExpression);
        join.inner = true;
        join.left = true;
        return join;
    }

    public static Join crossJoin(FromItem rightItem,
                                 Expression onExpression) {
        Join join = newJoin(rightItem, onExpression);
        join.inner = true;
        join.cross = true;
        return join;
    }

    public static Join fullJoin(FromItem rightItem, Expression onExpression) {
        Join join = newJoin(rightItem, onExpression);
        join.inner = true;
        join.full = true;
        return join;
    }

    public static Join newJoin(FromItem rightItem, Expression onExpression) {
        Join join = new Join(rightItem, onExpression);
        return join;
    }

    /**
     * Whether is a tab1,tab2 join
     *
     * @return true if is a "tab1,tab2" join
     */
    public boolean isSimple() {
        return simple;
    }

    public void setSimple(boolean b) {
        simple = b;
    }

    /**
     * Whether is a "INNER" join
     *
     * @return true if is a "INNER" join
     */
    public boolean isInner() {
        return inner;
    }

    public void setInner(boolean b) {
        inner = b;
    }

    /**
     * Whether is a "OUTER" join
     *
     * @return true if is a "OUTER" join
     */
    public boolean isOuter() {
        return outer;
    }

    public void setOuter(boolean b) {
        outer = b;
    }

    /**
     * Whether is a "LEFT" join
     *
     * @return true if is a "LEFT" join
     */
    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean b) {
        left = b;
    }

    /**
     * Whether is a "RIGHT" join
     *
     * @return true if is a "RIGHT" join
     */
    public boolean isRight() {
        return right;
    }

    public void setRight(boolean b) {
        right = b;
    }

    /**
     * Whether is a "NATURAL" join
     *
     * @return true if is a "NATURAL" join
     */
    public boolean isNatural() {
        return natural;
    }

    public void setNatural(boolean b) {
        natural = b;
    }

    /**
     * Whether is a "FULL" join
     *
     * @return true if is a "FULL" join
     */
    public boolean isFull() {
        return full;
    }

    public void setFull(boolean b) {
        full = b;
    }

    public boolean isCross() {
        return cross;
    }

    public void setCross(boolean cross) {
        this.cross = cross;
    }

    /**
     * Returns the right item of the join
     */
    public FromItem getRightItem() {
        return rightItem;
    }

    public void setRightItem(FromItem item) {
        rightItem = item;
    }

    /**
     * Returns the "ON" expression (if any)
     */
    public Expression getOnExpression() {
        return onExpression;
    }

    public void setOnExpression(Expression expression) {
        onExpression = expression;
    }

    /**
     * Returns the "USING" list of
     * {@link org.tinygroup.jsqlparser.schema.Column}s (if any)
     */
    public List<Column> getUsingColumns() {
        return usingColumns;
    }

    public void setUsingColumns(List<Column> list) {
        usingColumns = list;
    }

    public String toString() {
        if (isSimple()) {
            return "" + rightItem;
        } else {
            String type = "";

            if (isRight()) {
                type += "RIGHT ";
            } else if (isNatural()) {
                type += "NATURAL ";
            } else if (isFull()) {
                type += "FULL ";
            } else if (isLeft()) {
                type += "LEFT ";
            } else if (isCross()) {
                type += "CROSS ";
            }

            if (isOuter()) {
                type += "OUTER ";
            }

            return type
                    + "JOIN "
                    + rightItem
                    + ((onExpression != null) ? " ON " + onExpression + "" : "")
                    + DslUtil
                    .getFormattedList(usingColumns, "USING", true, true);
        }

    }
}
