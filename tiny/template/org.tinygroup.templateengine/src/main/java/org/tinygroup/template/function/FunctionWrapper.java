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
import org.tinygroup.template.rumtime.U;

import java.lang.reflect.Method;

/**
 * Created by luoguo on 2014/6/9.
 */
public class FunctionWrapper extends AbstractFunctionWrapper {
    private Method method;

    public FunctionWrapper(String functionName, Class clazz, Method method) {
        super(functionName);
        try {
            this.method = method;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.method = method;
    }

    public FunctionWrapper(String functionName, Class clazz, String methodName) {
        super(functionName);
        try {
            this.method = clazz.getMethod(methodName, U.getParameterTypes(clazz, methodName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FunctionWrapper(String functionName, String className, String methodName) {
        super(functionName);
        try {
            Class<?> clazz = Class.forName(className);
            this.method = clazz.getMethod(methodName, U.getParameterTypes(clazz, methodName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FunctionWrapper(String functionName, Object object, String methodName) {
        super(functionName);
        try {
            method = object.getClass().getMethod(methodName, U.getParameterTypes(object.getClass(), methodName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Object execute(Template template,TemplateContext context,Object... parameters) throws TemplateException {
        try {
            return method.invoke(null, parameters);
        } catch (Exception e) {
            throw new TemplateException(e);
        }
    }

}
