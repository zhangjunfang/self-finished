/*
 * Copyright Bruce Liang (ldcsaa@gmail.com)
 *
 * Version	: JessMA 3.5.1
 * Author	: Bruce Liang
 * Website	: http://www.jessma.org
 * Project	: http://www.oschina.net/p/portal-basic
 * Blog		: http://www.cnblogs.com/ldcsaa
 * WeiBo	: http://weibo.com/u/1402935851
 * QQ Group	: 75375912
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jessma.mvc;

/** {@link Action} 过滤器接口
 * 
 * 拦截 {@link Action} 的 execute() 方法，进行前置或后置处理。
 * 也可以进行短路处理，屏蔽 execute() 方法
 *  
*/
public interface ActionFilter
{
	/** 初始化方法，程序启动时调用 */
	void init();
	/** 清理方法，程序关闭时调用 */
	void destroy();
	/** 拦截方法 */
	String doFilter(ActionExecutor executor) throws Exception;
}
