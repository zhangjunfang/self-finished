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

import org.tinygroup.template.Macro;
import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;

import java.io.IOException;
import java.io.Writer;

/**
 * 抽象宏
 * Created by luoguo on 2014/6/6.
 */
public abstract class AbstractBlockMacro extends AbstractMacro {
    public AbstractBlockMacro(String name) {
        super(name);
    }


    protected void renderMacro(Template template, TemplateContext pageContext, TemplateContext context, Writer writer) throws IOException, TemplateException {
        renderHeader(template, context, writer);
        Macro macro = (Macro) context.getItemMap().get("bodyContent");
        if (macro != null) {
            macro.render(template,pageContext, context, writer);
        }
        renderFooter(template, context, writer);
    }

    protected abstract void renderHeader(Template template, TemplateContext context, Writer writer) throws IOException, TemplateException;

    protected abstract void renderFooter(Template template, TemplateContext context, Writer writer) throws IOException, TemplateException;

}
