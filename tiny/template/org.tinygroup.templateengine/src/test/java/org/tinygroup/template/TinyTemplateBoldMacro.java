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
package org.tinygroup.template;

import org.tinygroup.template.impl.AbstractBlockMacro;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.loader.StringResourceLoader;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Boilit
 * @see
 */
public final class TinyTemplateBoldMacro {
    static class BoldMacro extends AbstractBlockMacro {
        public BoldMacro() {
            super("bold");
        }

        protected void renderHeader(Template template, TemplateContext context, Writer writer) throws IOException, TemplateException {
            writer.write("<b>");
        }

        protected void renderFooter(Template template, TemplateContext context, Writer writer) throws IOException, TemplateException {
            writer.write("</b>");
        }
    }
    public static void main(String[] args) throws TemplateException {
        TemplateEngine engine = new TemplateEngineDefault();
        engine.registerMacro(new BoldMacro());
        ResourceLoader<String> resourceLoader=new StringResourceLoader();
        engine.addResourceLoader(resourceLoader);
        Template template=resourceLoader.createTemplate("#@bold()HelloWorld.#end");
        template.render();
    }
}
