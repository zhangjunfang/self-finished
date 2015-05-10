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

/**
 * Created by luoguo on 2014/6/8.
 */
public abstract class SingleOperator extends AbstractOperator {

    public int getParameterCount() {
        return 1;
    }

    public Object operation(Object... parameter) throws TemplateException {
        return operation(parameter[0]);
    }

    protected abstract Object operation(Object var) throws TemplateException;

    protected UnsupportedOperationException getUnsupportedOperationException(Object object) {
        throw new UnsupportedOperationException("类型" + object.getClass().getName() + "不支持" + "+" + getOperation() + "操作");
    }
}
