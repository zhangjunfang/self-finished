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
package org.tinygroup.tinysqldsl.transform;

import org.tinygroup.tinysqldsl.expression.Expression;
import org.tinygroup.tinysqldsl.expression.JdbcNamedParameter;
import org.tinygroup.tinysqldsl.expression.JdbcParameter;

/**
 * 默认转成JdbcParameter类型的表达式
 * @author renhui
 *
 */
public class JdbcParameterExpressionTransform implements ExpressionTransform {

    public Expression transform(Object value) {
        if (value instanceof JdbcNamedParameter) {
            return new JdbcParameter();
        }
        if (value instanceof JdbcParameter) {
            return (JdbcParameter) value;
        }
        if (value instanceof Expression) {
            return (Expression) value;
        }
        return new JdbcParameter();
    }

    public boolean isParameterExpression(Expression expression) {
        if (expression instanceof JdbcParameter) {
            return true;
        }
        return false;
    }

}
