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

import org.tinygroup.tinysqldsl.expression.BinaryExpression;
import org.tinygroup.tinysqldsl.expression.Expression;

/**
 * 实例化BinaryExpression的回调接口
 *
 * @author renhui
 *
 */
public interface ExpressionInstanceCallBack {
    /**
     * 是否要对原生值进行格式化
     * @param value
     * @return
     */
	Object format(Object value);
	/**
	 * 根据参数实例化二元表达式
	 * @param leftExpression
	 * @param rightExpression
	 * @return
	 */
    BinaryExpression instance(Expression leftExpression,
                              Expression rightExpression);
}