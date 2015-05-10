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

import org.tinygroup.template.function.AbstractTemplateFunction;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.loader.StringResourceLoader;

/**
 * @author Boilit
 * @see
 */
public final class TinyTemplateBold {
    static class BoldFunction extends AbstractTemplateFunction {

        public BoldFunction() {
            super("bold");
        }

        public String getBindingTypes() {
            return "java.lang.String";
        }

        public Object execute(Template template,TemplateContext context, Object... parameters) throws TemplateException {
            return "<b>" + parameters[0].toString() + "</b>";
        }
    }

    public static void main(String[] args) throws TemplateException {
        TemplateEngine engine = new TemplateEngineDefault();
        engine.addTemplateFunction(new BoldFunction());
        ResourceLoader<String> resourceLoader=new StringResourceLoader();
        engine.addResourceLoader(resourceLoader);
        Template template = resourceLoader.createTemplate("${format('Hello,%s','world').bold()}");
        template.render();
    }
}
