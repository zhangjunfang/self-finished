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

import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;

import java.util.Arrays;
import java.util.Formatter;

/**
 * Created by luoguo on 2014/6/9.
 */
public class FormatterTemplateFunction extends AbstractTemplateFunction {

    public FormatterTemplateFunction() {
        super("fmt,format,formatter");
    }

    public Object execute(Template template, TemplateContext context, Object... parameters) throws TemplateException {
        Formatter formatter = new Formatter();
        if (parameters.length == 0 || !(parameters[0] instanceof String)) {
            notSupported(parameters);
        }
        String formatString = parameters[0].toString();

        Object[] objects = Arrays.copyOfRange(parameters, 1, parameters.length);
        return formatter.format(formatString, objects);
    }
}

