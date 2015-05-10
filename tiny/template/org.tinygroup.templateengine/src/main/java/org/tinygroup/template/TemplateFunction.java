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

/**
 * 模板函数扩展
 * Created by luoguo on 2014/6/9.
 */
public interface TemplateFunction {
    /**
     * 绑定到类型上，使之成为这些类型的成员函数，如果有多个可以用半角的逗号分隔
     * 比如：此方法返回“java.lang.Integer”，表示可以在模板语言中的Integer类型的对象integer
     * 用“integer.someFunction(...)”的方式调用此方法
     * 为了方便扩展，这方法也可以返回多个类型，比如：“java.lang.Integer,java.lang.Long”，
     * 就表示同时给Integer和Long类型添加此扩展方法
     *
     * @return
     */
    String getBindingTypes();

    /**
     * 返回函数名，如果有多个名字，则用逗号分隔
     *
     * @return
     */
    String getNames();

    /**
     * 设置模板引擎
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

    /**
     * 执行函数体
     *
     * @param parameters
     * @return
     */
    Object execute(Template template, TemplateContext context, Object... parameters) throws TemplateException;
}
