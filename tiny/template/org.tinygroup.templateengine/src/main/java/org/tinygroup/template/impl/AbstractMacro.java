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
package org.tinygroup.template.impl;

import org.tinygroup.context.Context;
import org.tinygroup.template.*;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽象宏
 * Created by luoguo on 2014/6/6.
 */
public abstract class AbstractMacro implements Macro {
    private Macro bodyContentMacro;
    private String name;
    private List<String> parameterNames = new ArrayList<String>();
    private List<EvaluateExpression> parameterDefaultValues = new ArrayList<EvaluateExpression>();
    private TemplateEngine templateEngine;

    public AbstractMacro(String name) {
        this.name = name;
    }

    public AbstractMacro(String name, Macro bodyContentMacro) {
        this.name = name;
        this.bodyContentMacro = bodyContentMacro;
    }
    protected Macro getMacro(TemplateContext $context) {
        Macro $macro;
        $macro= getBodyContentMacro();
        if($macro==null){
            $macro=(Macro)$context.getItemMap().get("bodyContent");
        }
        if($macro==null){
            Context context=$context;
            while(context.getParent()!=null){
                if(context.get("bodyContent")!=null) {
                    $macro=(Macro)context.getItemMap().get("bodyContent");
                }
                context=context.getParent();
            }
        }
        return $macro;
    }
    public Macro getBodyContentMacro() {
        return bodyContentMacro;
    }

    public AbstractMacro(String name, List<String> parameterNames, List<EvaluateExpression> parameterDefaultValues) {
        this(name);
        this.parameterNames = parameterNames;
        this.parameterDefaultValues = parameterDefaultValues;
    }


    protected void addParameter(String parameterName, EvaluateExpression defaultValue) {
        parameterNames.add(parameterName);
        parameterDefaultValues.add(defaultValue);
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void setParameterNames(List<String> parameterNames) {
        this.parameterNames = parameterNames;
    }

    public List<EvaluateExpression> getParameterDefaultValues() {
        return parameterDefaultValues;
    }

    public void setParameterDefaultValues(List<EvaluateExpression> parameterDefaultValues) {
        this.parameterDefaultValues = parameterDefaultValues;
    }

    protected void init(String name) {
        this.name = name;
    }

    protected void write(Writer writer, Object object) throws IOException {
        if (object != null) {
            writer.write(object.toString());
        }
    }

    public void render(Template template, TemplateContext pageContext, TemplateContext context, Writer writer) throws TemplateException {
        try {
            for (int i = 0; i < parameterNames.size(); i++) {
                Object value = context.get(parameterNames.get(i));
                //如果没有传值且有默认值
                if (value == null && parameterDefaultValues.get(i) != null) {
                    context.put(parameterNames.get(i), parameterDefaultValues.get(i).evaluate(context));
                }
            }
            renderMacro(template, pageContext, context, writer);
        } catch (IOException e) {
            throw new TemplateException(e);
        }
    }

    protected abstract void renderMacro(Template template, TemplateContext pageContext, TemplateContext context, Writer writer) throws IOException, TemplateException;


    public String getName() {
        return name;
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }

    public String getParameterName(int index) {
        if (index < parameterNames.size()) {
            return parameterNames.get(index);
        }
        return null;
    }
}
