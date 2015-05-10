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
import org.tinygroup.template.loader.FileObjectResourceLoader;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.FileObjectProcessor;
import org.tinygroup.vfs.VFS;
import org.tinygroup.vfs.impl.filter.FileNameFileObjectFilter;

/**
 * Created by luoguo on 2014/6/7.
 */
public class JetTemplateTestCase {
    public static void main(String[] args) throws TemplateException {
        final TemplateEngine engine = new TemplateEngineDefault();
        FileObjectResourceLoader jetSample = new FileObjectResourceLoader("jetx", null, null, "src/test/resources");
        engine.addResourceLoader(jetSample);
        FileObject fileObject = VFS.resolveFile("src/test/resources");
        fileObject.foreach(new FileNameFileObjectFilter(".*\\.jetx", true), new FileObjectProcessor() {

            public void process(FileObject fileObject) {
                try {
                    System.out.println("\n" + fileObject.getPath());
                    engine.renderTemplate(fileObject.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
