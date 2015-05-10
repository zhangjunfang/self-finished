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

import org.tinygroup.commons.file.IOUtils;
import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateException;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by luoguo on 2014/6/11.
 */
public class ClassLoaderResourceLoader extends AbstractResourceLoader<String> {
    private ClassLoader classLoader;

    public ClassLoaderResourceLoader(String templateExtName, String layoutExtName, String macroLibraryExtName) {
        super(templateExtName, layoutExtName, macroLibraryExtName);
        classLoader = ClassLoaderResourceLoader.class.getClassLoader();
    }

    protected Template loadTemplateItem(String path) throws TemplateException {
        return createTemplate(path);
    }


    public ClassLoaderResourceLoader(String templateExtName, String layoutExtName,String macroLibraryExtName, URL[] urls) throws TemplateException {
        super(templateExtName, layoutExtName, macroLibraryExtName);
        classLoader = URLClassLoader.newInstance(urls);
    }

    public ClassLoaderResourceLoader(String templateExtName, String layoutExtName,String macroLibraryExtName, ClassLoader classLoader) {
        super(templateExtName, layoutExtName,macroLibraryExtName);
        if (classLoader != null) {
            this.classLoader = classLoader;
        } else {
            this.classLoader = ClassLoaderResourceLoader.class.getClassLoader();
        }
    }



    public String getResourceContent(String path, String encode) throws TemplateException {
        try {
            InputStream inputStream = classLoader.getResourceAsStream(path);
            return IOUtils.readFromInputStream(inputStream, encode);
        } catch (Exception e) {
            throw new TemplateException(e);
        }
    }


    public Template createTemplate(String path) throws TemplateException {
            String className = ResourceCompilerUtils.getClassNameGetter().getClassName(path).getClassName();
        try {
            Template template = null;
            template = (Template) classLoader.loadClass(className).newInstance();
            addTemplate(template);
            return template;
        } catch (InstantiationException e) {
            throw new TemplateException(e);
        } catch (IllegalAccessException e) {
            throw new TemplateException(e);
        } catch (ClassNotFoundException e) {
            return null;
        }

    }
}
