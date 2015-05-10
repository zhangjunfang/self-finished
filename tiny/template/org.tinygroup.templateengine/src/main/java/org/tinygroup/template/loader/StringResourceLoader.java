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
package org.tinygroup.template.loader;

import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateException;

/**
 * Created by luoguo on 2014/6/9.
 */
public class StringResourceLoader extends AbstractResourceLoader<String> {
    public StringResourceLoader() {
        super(null, null, null);
    }

    /**
     * 字符串内存型的，不支持根据路径载入
     *
     * @param path
     * @return
     * @throws TemplateException
     */

    protected Template loadTemplateItem(String path) throws TemplateException {
        return null;
    }


    protected Template loadLayout(String path) throws TemplateException {
        return null;
    }

    protected Template loadMacroLibrary(String path) throws TemplateException {
        return null;
    }

    public boolean isModified(String path) {
        return false;
    }

    /**
     * 字符串内存型的，不支持根据路径载入
     *
     * @param path
     * @param encode
     * @return
     */
    public String getResourceContent(String path, String encode) {
        return null;
    }

    public Template createTemplate(String stringTemplateMaterial) throws TemplateException {
        Template template = ResourceCompilerUtils.compileResource(StringResourceLoader.class.getClassLoader(), stringTemplateMaterial, getTemplateEngine().getEngineId(),getRandomPath());
        //这里没有调用putTemplate是避免内存泄露
        template.setTemplateEngine(getTemplateEngine());
        return template;
    }

    public Template createLayout(String templateMaterial) throws TemplateException {
        throw new TemplateException("Not supported.");
    }

    public Template createMacroLibrary(String templateMaterial) throws TemplateException {
        throw new TemplateException("Not supported.");
    }

    private String getRandomPath() {
        return "C" + System.nanoTime();
    }
}
