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

import org.tinygroup.template.ResourceLoader;
import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.TemplateException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象模板加载器
 * Created by luoguo on 2014/6/9.
 */
public abstract class AbstractResourceLoader<T> implements ResourceLoader<T> {
    private boolean checkModified = false;
    private ClassLoader classLoader;
    private Map<String, Template> repositories = new ConcurrentHashMap<String, Template>();
    private TemplateEngine templateEngine;
    private String templateExtName;
    private String layoutExtName;
    private String macroLibraryExtName;

    public void setCheckModified(boolean checkModified) {
        this.checkModified = checkModified;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    public AbstractResourceLoader(String templateExtName, String layoutExtName, String macroLibraryExtName) {
        this.templateExtName = "." + templateExtName;
        this.layoutExtName = "." + layoutExtName;
        this.macroLibraryExtName = "." + macroLibraryExtName;
        setClassLoader(getClass().getClassLoader());
    }


    public String getLayoutPath(String templatePath) {
        if (templatePath.endsWith(templateExtName)) {
            return templatePath.substring(0, templatePath.length() - templateExtName.length()) + layoutExtName;
        }
        return null;
    }


    public String getTemplateExtName() {
        return templateExtName;
    }


    public String getLayoutExtName() {
        return layoutExtName;
    }

    public String getMacroLibraryExtName() {
        return macroLibraryExtName;
    }

    public void setMacroLibraryExtName(String macroLibraryExtName) {
        this.macroLibraryExtName = macroLibraryExtName;
    }

    public Template getTemplate(String path) throws TemplateException {
        return getTemplateItem(path, templateExtName);
    }

    private Template getTemplateItem(String path, String templateExtName) throws TemplateException {
        if (!path.endsWith(templateExtName)) {
            return null;
        }
        Template template = repositories.get(path);
        if (template == null || checkModified && isModified(path)) {
            template = loadTemplateItem(path);
            if (template != null) {
                resetModified(path);
            }
        }
        return template;
    }

    public boolean isModified(String path) {
        return false;
    }

    public void resetModified(String path) {

    }

    public Template getLayout(String path) throws TemplateException {
        return getTemplateItem(path, layoutExtName);
    }

    public Template getMacroLibrary(String path) throws TemplateException {
        return getTemplateItem(path, macroLibraryExtName);
    }

    protected abstract Template loadTemplateItem(String path) throws TemplateException;

    public ResourceLoader addTemplate(Template template) throws TemplateException {
        return addTemplateItem(template);
    }

    private ResourceLoader addTemplateItem(Template template) {
        repositories.put(template.getPath(), template);
        template.setTemplateEngine(templateEngine);
        template.getTemplateContext().setParent(templateEngine.getTemplateContext());
        return this;
    }

    public Map<String, Template> getRepositories() {
        return repositories;
    }

    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }
}
