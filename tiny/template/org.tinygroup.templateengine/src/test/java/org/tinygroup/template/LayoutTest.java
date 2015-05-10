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

import org.tinygroup.template.impl.AbstractTemplate;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.loader.ClassLoaderResourceLoader;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by luoguo on 2014/6/13.
 */
public class LayoutTest {
    public static void main(String[] args) throws TemplateException {
        TemplateEngine engine=new TemplateEngineDefault();
        ClassLoaderResourceLoader resourceLoader = new ClassLoaderResourceLoader("page", "layout", "macro");
        engine.addResourceLoader(resourceLoader);
        resourceLoader.addTemplate(new Layout1());
        resourceLoader.addTemplate(new Layout2());
        resourceLoader.addTemplate(new Template1());
        engine.renderTemplate("/aaa/a.page");
    }
}

class Template1 extends AbstractTemplate {
   
    protected void renderContent(TemplateContext $context, Writer $writer) throws IOException, TemplateException {
        $writer.write("Hello");
    }

    public String getPath() {
        return "/aaa/a.page";
    }
}

class Layout1 extends AbstractTemplate {
    protected void renderContent(TemplateContext $context, Writer $writer) throws IOException, TemplateException {
        $writer.write("<b>");
        $writer.write($context.get("pageContent").toString());
        $writer.write("</b>");
    }

    public String getPath() {
        return "/aaa/a.layout";
    }
}

class Layout2 extends AbstractTemplate {
    @Override
    protected void renderContent(TemplateContext $context, Writer $writer) throws IOException, TemplateException {
        $writer.write("<div>");
        $writer.write($context.get("pageContent").toString());
        $writer.write("</div>");
    }

    public String getPath() {
        return "/a.layout";
    }
}
