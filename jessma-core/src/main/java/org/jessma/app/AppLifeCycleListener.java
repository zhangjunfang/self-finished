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

package org.jessma.app;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/** 应用程序生命周期监听器接口 */
public interface AppLifeCycleListener
{
	/**
	 *  程序启动通知
	 *  
	 *  @param context	: 应用程序的 {@link ServletContext}
	 *  @param sce	: 事件参数 {@link ServletContextEvent}
	 *  
	*/
	void onStartup(ServletContext context, ServletContextEvent sce);

	/**
	 *  程序关闭通知
	 *  
	 *  @param context	: 应用程序的 {@link ServletContext}
	 *  @param sce	: 事件参数 {@link ServletContextEvent}
	 *  
	*/
	void onShutdown(ServletContext context, ServletContextEvent sce);

}
