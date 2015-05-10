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

import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * 模板
 * Created by luoguo on 2014/6/4.
 */
public interface Template extends TemplateContextOperator {
    /**
     * 返回宏的内容
     *
     * @return
     */
    Map<String, Macro> getMacroMap();

    /**
     * 返回宏文件中引入模板的顺序
     *
     * @return
     */
    List<String> getImportPathList();

    /**
     * 进行渲染
     *
     * @param writer
     */
    void render(TemplateContext context, Writer writer) throws TemplateException;

    void render(TemplateContext context) throws TemplateException;

    void render() throws TemplateException;

    /**
     * 返回宏对应的路径
     *
     * @return
     */
    String getPath();

    /**
     * 设置对应的模板引擎
     *
     * @param templateEngine
     */
    void setTemplateEngine(TemplateEngine templateEngine);

    /**
     * 返回模板引擎
     *
     * @return
     */
    TemplateEngine getTemplateEngine();
}
