package global;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.jessma.app.AppLifeCycleListener;
import org.jessma.util.LogUtil;
import org.slf4j.Logger;

public class MyLifeCycleListener implements AppLifeCycleListener
{
	Logger logger = LogUtil.getJessMALogger();
	
	@Override
	public void onStartup(ServletContext context, ServletContextEvent sce)
	{
		logger.info(this.getClass().getName() + " -> onStartup()");
		
		// 加载基础数据缓存
		Cache cache = Cache.getInstance();
		cache.loadBasicData();
		// 把缓存对象设置为全局 Attribute
		context.setAttribute(Cache.CACHE_KEY, cache);
	}

	@Override
	public void onShutdown(ServletContext context, ServletContextEvent sce)
	{
		// 删除缓存对象全局 Attribute
		context.removeAttribute(Cache.CACHE_KEY);
		// 卸载基础数据缓存
		Cache cache = Cache.getInstance();
		cache.unloadBasicData();
		
		logger.info(this.getClass().getName() + " -> onShutdown()");
	}
}
