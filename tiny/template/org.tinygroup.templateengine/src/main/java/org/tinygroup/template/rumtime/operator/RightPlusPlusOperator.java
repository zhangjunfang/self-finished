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

import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.rumtime.O;
import org.tinygroup.template.rumtime.OperatorWithContext;

/**
 * Created by luoguo on 2014/6/8.
 */
public class RightPlusPlusOperator implements OperatorWithContext {


    public Object operation(TemplateContext context, String name, Object value) throws TemplateException {
        Object v = context.get(name.toString());
        if (v != null) {
            context.put(name.toString(), O.e("+", v, 1));
            return v;
        } else {
            return name;
        }
    }

    public String getOperation() {
        return "++";
    }

}
