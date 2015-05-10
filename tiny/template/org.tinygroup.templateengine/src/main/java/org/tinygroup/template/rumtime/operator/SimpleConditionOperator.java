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

import org.tinygroup.commons.tools.ArrayUtil;
import org.tinygroup.commons.tools.Enumerator;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by luoguo on 2014/6/5.
 */
public class SimpleConditionOperator extends TwoOperator {


    protected Object operation(Object left, Object right) {
        //如果为空返回right
        if (left == null) {
            return right;
        }
        if (left instanceof Collection) {
            Collection collection = (Collection) left;
            if (collection.size() == 0) {
                return right;
            }
        }
        if (left instanceof Map) {
            Map map = (Map) left;
            if (map.size() == 0) {
                return right;
            }
        }
        if (left.getClass().isArray() &&ArrayUtil.arrayLength(left) == 0) {
                return right;
        }
        if (left instanceof Iterator) {
            Iterator iterator= (Iterator) left;
            if ( iterator.hasNext()) {
                return right;
            }
        }
        if (left instanceof Enumerator) {
            Enumerator enumerator= (Enumerator) left;
            if ( enumerator.hasMoreElements()) {
                return right;
            }
        }
        return left;
    }

    public String getOperation() {
        return "?:";
    }

}
