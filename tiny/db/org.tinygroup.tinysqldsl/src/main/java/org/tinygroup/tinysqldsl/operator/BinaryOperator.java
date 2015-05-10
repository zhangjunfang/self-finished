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

import org.tinygroup.tinysqldsl.base.Condition;

/**
 * 二元操作接口
 *
 * @author renhui
 */
public interface BinaryOperator {
    /**
     * 等于
     *
     * @param value
     * @return
     */
    Condition eq(Object value);

    /**
     * 等于
     *
     * @param value
     * @return
     */
    Condition equal(Object value);

    /**
     * 介于两个值中间
     *
     * @param start
     * @param end
     * @return
     */
    Condition between(Object start, Object end);
    
    /**
     * 介于两个值中间
     *
     * @param start
     * @param end
     * @return
     */
    Condition notBetween(Object start, Object end);

    /**
     * 不等于
     *
     * @param value
     * @return
     */
    Condition neq(Object value);

    /**
     * 不等于
     *
     * @param value
     * @return
     */
    Condition notEqual(Object value);

    /**
     * 大于
     *
     * @param value
     * @return
     */
    Condition gt(Object value);

    /**
     * 大于
     *
     * @param value
     * @return
     */
    Condition greaterThan(Object value);

    /**
     * 大于等于
     *
     * @param value
     * @return
     */
    Condition gte(Object value);

    /**
     * 大于等于
     *
     * @param value
     * @return
     */
    Condition greaterThanEqual(Object value);

    /**
     * 小于
     *
     * @param value
     * @return
     */
    Condition lt(Object value);

    /**
     * 小于
     *
     * @param value
     * @return
     */
    Condition lessThan(Object value);

    /**
     * 小于等于
     *
     * @param value
     * @return
     */
    Condition lessThanEqual(Object value);

    /**
     * 小于等于
     *
     * @param value
     * @return
     */

    Condition lte(Object value);

    /**
     * 是否为空
     *
     * @return
     */
    Condition isNull();

    /**
     * 是否不为空
     *
     * @return
     */
    Condition isNotNull();
    
    /**
     * 是否为空字符串
     *
     * @return
     */
    Condition isEmpty();

    /**
     * 是否为非字符串
     *
     * @return
     */
    Condition isNotEmpty();

    /**
     * 包含
     *
     * @param value
     * @return
     */
    Condition like(String value);

    /**
     * 不包含
     *
     * @param value
     * @return
     */
    Condition notLike(String value);

    /**
     * 不包含
     *
     * @param value
     * @return
     */
    Condition leftLike(String value);

    Condition rightLike(String value);

    Condition notLeftLike(String value);

    Condition notRightLike(String value);
    /**
     * 支持in表达式
     * @param values
     * @return
     */
    Condition in(Object...values);
    
    Condition notIn(Object...values);
}
