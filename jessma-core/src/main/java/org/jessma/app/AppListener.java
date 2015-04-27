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
import javax.servlet.ServletContextListener;

import org.jessma.util.GeneralHelper;
import org.jessma.util.LogUtil;

/**
 * 
 * 应用程序启动监听器，实现 {@link ServletContextListener} 接口。
 *
 */
public class AppListener implements ServletContextListener
{
	private static final String APP_CONFIG_FILE_KEY		= "app-config-file";
	private static final String DEFAULT_APP_CONFIG_FILE	= "app-config.xml";
	
	private ServletContext context;
		
	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		context = sce.getServletContext();
		
		String appConfigFile = context.getInitParameter(APP_CONFIG_FILE_KEY);
		if(GeneralHelper.isStrEmpty(appConfigFile))
			appConfigFile	= DEFAULT_APP_CONFIG_FILE;

		appConfigFile		= GeneralHelper.getClassResourcePath(AppListener.class, appConfigFile);
		
		try
		{
			LogUtil.logServerStartup(this);
			LogUtil.getJessMALogger().info("load application configuration");
			
			/* 加载程序配置 */
			AppConfig.initialize(appConfigFile);
			
			/* 发送程序启动通知 */
			AppConfig.sendStartupNotice(context, sce);
		}
		catch(Exception e)
		{
			String msg = "application startup fail";
			LogUtil.exception(e, msg, true);
			
			throw new RuntimeException(msg, e);
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		try
		{
			/* 发送程序停止通知 */
			AppConfig.sendShutdownNotice(context, sce);

			/* 卸载程序资源 */
			AppConfig.unInitialize();

			LogUtil.logServerShutdown(this);
		}
		catch(Exception e)
		{
			String msg = "application shutdown exception";
			LogUtil.exception(e, msg, true);

			throw new RuntimeException(msg, e);
		}
	}
}
