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

import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.loader.ClassLoaderResourceLoader;

import java.io.File;
import java.net.URL;

/**
 * Created by luoguo on 2014/6/7.
 */
public class TemplateTestCase1 {

    public static void main(String[] args) throws Exception {
        URL[] urls = {new File("C:\\Users\\luoguo\\AppData\\Local\\Temp\\ttl").toURI().toURL()};
        final TemplateEngine engine = new TemplateEngineDefault();
        engine.addResourceLoader(new ClassLoaderResourceLoader("jetx", null, null, urls));
        engine.renderTemplate("template/jet/constant-number.jetx");
    }
}
