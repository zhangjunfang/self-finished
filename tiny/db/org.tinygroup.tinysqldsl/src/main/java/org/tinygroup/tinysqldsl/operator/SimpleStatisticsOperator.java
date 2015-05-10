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
package org.tinygroup.tinysqldsl.operator;

import org.tinygroup.tinysqldsl.expression.Expression;
import org.tinygroup.tinysqldsl.expression.Function;
import org.tinygroup.tinysqldsl.expression.relational.ExpressionList;

/**
 * 统计操作实现
 *
 * @author renhui
 *
 */
public abstract class SimpleStatisticsOperator implements StatisticsOperator,
        Expression {

    public Function sum() {
        return new Function("sum", ExpressionList.expressionList(this));
    }

    public Function count() {
        return new Function("count", ExpressionList.expressionList(this));
    }

    public Function avg() {
        return new Function("avg", ExpressionList.expressionList(this));
    }

    public Function max() {
        return new Function("max", ExpressionList.expressionList(this));
    }

    public Function min() {
        return new Function("min", ExpressionList.expressionList(this));
    }

}
