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

import java.io.File;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jessma.dao.SessionMgr;
import org.jessma.util.GeneralHelper;
import org.jessma.util.LogUtil;
import org.slf4j.Logger;

/**
 * 
 * 系统配置类，保存程序系统配置。
 *
 */
@SuppressWarnings("rawtypes")
public class AppConfig
{
	private Map<String, SessionMgr> sessionMgrs = new HashMap<String, SessionMgr>();
	private UserConfigParser userCfgParser;
	private AppLifeCycleListener appListener;
	
	private String confFile;
	
	private static final Logger jessMALogger = LogUtil.getJessMALogger();
	private static final AppConfig instance	 = new AppConfig();
	
	private AppConfig()
	{
		
	}
	
	/**
	 * 
	 * 获取所有 {@link SessionMgr} 
	 * 
	 * @return	  名称 -> 实例
	 * 
	 */
	public static final Map<String, SessionMgr> getSessionManagers()
	{
		return instance.sessionMgrs;
	}
	
	/**
	 * 
	 * 获取指定名称的 {@link SessionMgr} 
	 * 
	 * @param name	: {@link SessionMgr} 名称
	 * @return	  	: {@link SessionMgr} 实例
	 * 
	 */
	public static final SessionMgr getSessionManager(String name)
	{
		return instance.sessionMgrs.get(name);
	}

	static final void sendStartupNotice(ServletContext context, ServletContextEvent sce)
	{
		if(instance.appListener != null)
			instance.appListener.onStartup(context, sce);
	}
	
	static final void sendShutdownNotice(ServletContext context, ServletContextEvent sce)
	{
		if(instance.appListener != null)
			instance.appListener.onShutdown(context, sce);
	}

	static void initialize(String file)
	{
		instance.confFile = file;
		instance.loadConfig();
	}

	private void loadConfig()
	{
		try
		{
			Element root = getRootElement();
			Element sys	 = getSystemElement(root);
			
			loadSessionMgrs(sys);		
			loadUserConfig(root, sys);
			loadAppListener(sys);
		}
		catch(Exception e)
		{
			throw new RuntimeException("load application configuration fail", e);
		}
	}

	private Element getRootElement() throws DocumentException
	{
		SAXReader sr = new SAXReader();
		Document doc = sr.read(new File(confFile));
		Element root = doc.getRootElement();
		
		return root;
	}

	private Element getSystemElement(Element root)
	{
		Element sys = root.element("system");
		
		if(sys == null)
			throw new RuntimeException("'system' element not found");
		
		return sys;
	}

	// 注册数据库 Session 管理器
	@SuppressWarnings("unchecked")
	private void loadSessionMgrs(Element sys) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		Element sms = sys.element("database-session-managers");
		if(sms == null)
			jessMALogger.warn("'database-session-managers' element not found");
		else
		{
			List<Element> mgrs = sms.elements("manager");
			if(mgrs.size() == 0)
				jessMALogger.warn("none of DATABASE SESSION MANAGER found");
			else
			{				
				for(Element e : mgrs)
				{
					String name = e.attributeValue("name");
					String clazz = e.attributeValue("class");
					
					if(GeneralHelper.isStrEmpty(name) || GeneralHelper.isStrEmpty(clazz))
						throw new RuntimeException("manager's 'name' or 'class' attribute not valid");
					
					String[] params;
					Element iniargs = e.element("initialize-args");
					if(iniargs == null)
						params = new String[0];
					else
					{
						List<Element> args = iniargs.elements("arg");
						params = new String[args.size()];
						
						for(int i = 0; i < args.size(); i++)
							params[i] = args.get(i).getTextTrim();
					}
					
					jessMALogger.info(String.format("register DATABASE SESSION MANAGER '%s (%s)'", name, clazz));
					
					SessionMgr mgr = (SessionMgr)(Class.forName(clazz).newInstance());
					mgr.initialize(params);
					sessionMgrs.put(name, mgr);	
				}
			}
		}
	}

	// 注册 UserConfigParser
	private void loadUserConfig(Element root, Element sys) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		Element parser = sys.element("user-config-parser");
		if(parser != null)
		{
			String clazz = parser.attributeValue("class");
			if(!GeneralHelper.isStrEmpty(clazz))
			{
				jessMALogger.info(String.format("register USER CONFIG PARSER '%s'", clazz));
				userCfgParser = (UserConfigParser)(Class.forName(clazz).newInstance());
				
				parseUserConfig(root);
			}
		}
	}

	// 解析用户配置信息
	private void parseUserConfig(Element root)
	{
		jessMALogger.info("parse user configuration");
		Element user = root.element("user");
		userCfgParser.parse(user);			
	}

	// 注册 AppLifeCycleListener
	private void loadAppListener(Element sys) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		Element listener = sys.element("app-life-cycle-listener");
		if(listener != null)
		{
			String clazz = listener.attributeValue("class");
			if(!GeneralHelper.isStrEmpty(clazz))
			{
				jessMALogger.info(String.format("register APP LIFE CYCLE LISTENER '%s'", clazz));
				appListener = (AppLifeCycleListener)(Class.forName(clazz).newInstance());
			}
		}
	}
	
	static void unInitialize()
	{
		instance.cleanup();
		deregisterDrivers();	
	}

	// 注销数据库 Session 管理器
	private void cleanup()
	{
		Set<Map.Entry<String, SessionMgr>> mgrs = sessionMgrs.entrySet();
		for(Map.Entry<String, SessionMgr> mgr : mgrs)
		{
			jessMALogger.info(String.format("unregister DATABASE SESSION MANAGER '%s'", mgr.getKey()));
			mgr.getValue().unInitialize();
		}
		
		sessionMgrs		= null;
		userCfgParser	= null;
		appListener		= null;
	}

	// 卸载数据库驱动程序
	private static void deregisterDrivers()
	{
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while(drivers.hasMoreElements())
		{
			Driver driver = drivers.nextElement();
			try
			{
				jessMALogger.info(String.format("unregister JDBC DRIVER '%s'", driver));
				DriverManager.deregisterDriver(driver);
			}
			catch(SQLException e)
			{
				LogUtil.exception(e, String.format("unregister JDBC DRIVER '%s' fail", driver), true);
			}
		}
	}
	
	/* **************************************************************************************************** */
	//										Reload User Config												//
	/* **************************************************************************************************** */

	/** 获取应用程序配置对象 {@linkplain AppConfig} 实例 */
	public static final AppConfig instance()
	{
		return instance;
	}
	
	/** 重新加载应用程序配置文件的 &lt;user&gt; 节点<br>
	 * <ul>
	 * <li>本方法只重新加载 &lt;user&gt; 节点，不重新加载 &lt;system&gt; 节点，
	 * 因此如果更改了 &lt;system&gt; 节点的配置信息，必须重启应用程序才能使更改生效</li>
	 * <li>本方法会再次调用由 &lt;system&gt;/&lt;user-config-parser&gt; 节点定义的 {@linkplain UserConfigParser}
	 * 的 {@linkplain UserConfigParser#parse(Element) parse(Element)} 方法重新加载 &lt;user&gt; 节点</li>
	 * </ul>
	 * 
	 * @param delay			: 执行重新加载操作的延时时间（毫秒），指定一个延时时间是为了确保所有当前正在处理的请求都执行完毕后才执行重新加载操作
	 * @throws Exception	: 加载失败抛出该异常
	 * 
	 */
	synchronized public void reloadUserConfig(long delay) throws Exception
	{
		if(userCfgParser != null)
		{
			try
			{
				GeneralHelper.waitFor(delay);
				
				Element root = getRootElement();
				parseUserConfig(root);
			}
			catch(Exception e)
			{
				throw e;
			}
		}
	}
}
