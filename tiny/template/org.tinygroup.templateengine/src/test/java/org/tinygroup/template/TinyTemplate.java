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

import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.loader.FileObjectResourceLoader;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Boilit
 * @see
 */
public final class TinyTemplate {
    public static void main(String[] args) throws TemplateException {
        TemplateEngine engine = new TemplateEngineDefault();
        engine.setCacheEnabled(true);
        TemplateContext context = new TemplateContextDefault();
        context.put("outputEncoding", "GBK");
        context.put("items", StockModel.dummyItems());
        FileObjectResourceLoader html = new FileObjectResourceLoader("html", null,null, "D:\\gitpart3\\ebm\\src\\main\\resources\\templates");
        html.setCheckModified(false);
        engine.addResourceLoader(html);
        long start = System.currentTimeMillis();
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {

            }

            @Override
            public void flush() throws IOException {

            }

            @Override
            public void close() throws IOException {

            }
        };
        for (int i = 0; i < 200000; i++) {
            engine.renderTemplate("/tiny.html", context, writer);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

}
