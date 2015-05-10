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
package org.tinygroup.template.function;

import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.TemplateFunction;

/**
 * Created by luoguo on 2014/6/9.
 */
public abstract class AbstractFunctionWrapper implements TemplateFunction {
    private final String name;
    private TemplateEngine templateEngine;
    private String bindingTypes;

    public AbstractFunctionWrapper(String name) {
        this.name = name;
    }


    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }


    public String getBindingTypes() {
        return bindingTypes;
    }

    protected void setBindingTypes(String bindingTypes) {
        this.bindingTypes = bindingTypes;
    }


    public String getNames() {
        return name;
    }


    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }


}
