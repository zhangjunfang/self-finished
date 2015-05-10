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

import org.tinygroup.template.function.AbstractBindTemplateFunction;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.loader.StringResourceLoader;

import java.util.Locale;

/**
 * Created by luoguo on 2014/6/7.
 */
public class TemplateTestCase {
    static class I18nvi implements I18nVisitor {

        public Locale getLocale(TemplateContext context) {
            return Locale.getDefault();
        }

        public String getI18nMessage(TemplateContext context, String key) {
            return key.toUpperCase();
        }
    }

    static class StringBoldFunction extends AbstractBindTemplateFunction {

        public StringBoldFunction() {
            super("bold", "java.lang.String");
        }


        public Object execute(Template template,TemplateContext context,Object... parameters) throws TemplateException {
            String obj = (String) parameters[0];
            return "<b>" + obj + "</b>";
        }
    }

    public static void main(String[] args) throws TemplateException {
        final TemplateEngine engine = new TemplateEngineDefault();
        engine.addTemplateFunction(new StringBoldFunction());
        engine.setI18nVisitor(new I18nvi());
        StringResourceLoader resourceLoader = new StringResourceLoader();
        engine.addResourceLoader(resourceLoader);
        Template template = resourceLoader.createTemplate("${getResourceContent()}");
        template.render();
        template = resourceLoader.createTemplate("${'abc'.equals('a')}");
        template.render();
        template = resourceLoader.createTemplate("${fmt('add%sinfo',3)}");
        template.render();
    }
}
