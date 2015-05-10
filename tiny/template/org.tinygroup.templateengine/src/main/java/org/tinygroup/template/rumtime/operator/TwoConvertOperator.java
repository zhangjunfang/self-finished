/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.template.rumtime.operator;

import org.tinygroup.template.TemplateException;
import org.tinygroup.template.rumtime.O;

/**
 * Created by luoguo on 2014/6/6.
 */
public abstract class TwoConvertOperator extends TwoOperator {

    public Object operation(Object... parameter) throws TemplateException {
        Object left = parameter[0];
        Object right = parameter[1];
        if (O.isNumber(left.getClass()) && O.isNumber(right.getClass())) {
            //如果两个都是数字类型
            return operateNumber(left, right, left.getClass(), right.getClass());
        } else {
            return operation(left, right);
        }
    }

    private Object operateNumber(Object left, Object right, Class type1, Class type2) {
        Object leftObject = left, rightObject = right;
        if (!type1.equals(type2)) {
            if (O.compare(type1, type2) > 0) {
                rightObject = O.convert(rightObject, type2, type1);
            } else {
                leftObject = O.convert(leftObject, type1, type2);
            }
        }
        return operation(leftObject, rightObject);
    }


}
