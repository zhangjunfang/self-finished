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

import java.util.Map;

/**
 * 用于载入模板
 * Created by luoguo on 2014/6/9.
 */
public interface ResourceLoader<T> {
    /**
     * 是否检查模板是否被修改过
     *
     * @param checkModified
     */
    void setCheckModified(boolean checkModified);

    /**
     * 确定某个路径对应的文件是否被修改
     *
     * @param path
     * @return
     */
    boolean isModified(String path);

    /**
     * 清置修改状态
     * @param path
     */
    void resetModified(String path);
    /**
     * 返回路径
     *
     * @param templatePath
     * @return
     * @throws TemplateException
     */
    String getLayoutPath(String templatePath);

    /**
     * 返回模板对象，如果不存在则返回null
     *
     * @param path
     * @return
     */
    Template getTemplate(String path) throws TemplateException;

    /**
     * 返回布局对象
     *
     * @param path
     * @return
     * @throws TemplateException
     */
    Template getLayout(String path) throws TemplateException;

    /**
     * 返回宏库文件
     *
     * @param path
     * @return
     * @throws TemplateException
     */
    Template getMacroLibrary(String path) throws TemplateException;

    /**
     * 获取资源对应的文本
     *
     * @param path
     * @return
     */
    String getResourceContent(String path, String encode) throws TemplateException;

    /**
     * 添加模板对象
     *
     * @param template
     * @return
     */
    ResourceLoader addTemplate(Template template) throws TemplateException;

    /**
     * 创建并注册模板
     *
     * @param templateMaterial
     * @return
     */
    Template createTemplate(T templateMaterial) throws TemplateException;

    /**
     * 返回注入模板引擎
     *
     * @param templateEngine
     */
    void setTemplateEngine(TemplateEngine templateEngine);

    /**
     * 返回类加载器
     *
     * @return
     */
    ClassLoader getClassLoader();

    /**
     * 设置类加载器
     *
     * @param classLoader
     */
    void setClassLoader(ClassLoader classLoader);

    /**
     * 获取流程引擎
     *
     * @return
     */
    TemplateEngine getTemplateEngine();


    /**
     * 返回模板文件的扩展
     *
     * @return
     */
    String getTemplateExtName();

    /**
     * 返回布局文件的扩展名
     *
     * @return
     */
    String getLayoutExtName();

    /**
     * 返回仓库中的所有资源
     *
     * @return
     */
    Map<String, Template> getRepositories();
}
