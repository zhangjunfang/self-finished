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

import org.tinygroup.tinysqldsl.expression.Expression;


/**
 * OrderBy元素
 */
public class OrderByElement {

    public enum NullOrdering {
        NULLS_FIRST,
        NULLS_LAST
    }

    private Expression expression;
    private boolean asc = true;
    private NullOrdering nullOrdering;
    private boolean ascDesc = false;

    public OrderByElement() {
        super();
    }

    public OrderByElement(Expression expression, boolean asc) {
        super();
        this.expression = expression;
        this.asc = asc;
    }

    public static OrderByElement asc(Expression expression) {
        return new OrderByElement(expression, true);
    }

    public static OrderByElement desc(Expression expression) {
        return new OrderByElement(expression, false);
    }

    public boolean isAsc() {
        return asc;
    }

    public NullOrdering getNullOrdering() {
        return nullOrdering;
    }

    public void setNullOrdering(NullOrdering nullOrdering) {
        this.nullOrdering = nullOrdering;
    }

    public void setAsc(boolean b) {
        asc = b;
    }

    public void setAscDescPresent(boolean b) {
        ascDesc = b;
    }

    public boolean isAscDescPresent() {
        return ascDesc;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }


    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(expression.toString());

        if (!asc) {
            b.append(" DESC");
        } else if (ascDesc) {
            b.append(" ASC");
        }

        if (nullOrdering != null) {
            b.append(' ');
            b.append(nullOrdering == NullOrdering.NULLS_FIRST ? "NULLS FIRST" : "NULLS LAST");
        }
        return b.toString();
    }
}
