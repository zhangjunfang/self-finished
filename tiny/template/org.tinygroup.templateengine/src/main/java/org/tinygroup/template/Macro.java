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

import org.tinygroup.template.impl.EvaluateExpression;

import java.io.Writer;
import java.util.List;

/**
 * 宏，就可以理解为一个对方法，它有输入参数，但没有输出参数，而是直接针对Writer对象进行内容输出
 * Created by luoguo on 2014/6/6.
 */
public interface Macro {
    /**
     * 返回宏的名字
     *
     * @return
     */
    String getName();

    /**
     * 返回宏的参数名称
     *
     * @return
     */
    List<String> getParameterNames();

    /**
     * 返回指定索引的参数名，如果越界，则返回null
     *
     * @param index
     * @return
     */
    String getParameterName(int index);

    /**
     * 返回宏的参数的默认值
     *
     * @return
     */
    List<EvaluateExpression> getParameterDefaultValues();

    /**
     * 设置模板引擎
     *
     * @param templateEngine
     */
    void setTemplateEngine(TemplateEngine templateEngine);

    /**
     * 获得模板引擎
     *
     * @return
     */
    TemplateEngine getTemplateEngine();

    /**
     * 进行渲染
     *
     * @param context
     * @param writer
     */
    void render(Template template, TemplateContext pageContext, TemplateContext context, Writer writer) throws TemplateException;
}
