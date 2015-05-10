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
package org.tinygroup.template.function;

import org.tinygroup.template.Macro;
import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.rumtime.U;
import org.tinygroup.threadgroup.ExceptionCallBack;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by luoguo on 2014/6/9.
 */
public class GetFunction extends AbstractTemplateFunction {

    public GetFunction() {
        super("get");
    }

    public String getBindingTypes() {
        return "java.lang.Object";
    }

    public Object execute(Template template, TemplateContext context, Object... parameters) throws TemplateException {
        Object object = parameters[0];
        Object indexObject = parameters[1];
        try {
            return U.a(object, indexObject);
        }catch (Exception e) {
            return U.c(template, context, object, "get", object, indexObject);
        }
    }
}

