package org.tinygroup.cache.application;

import org.tinygroup.application.AbstractApplicationProcessor;
import org.tinygroup.cache.CacheManager;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 关闭缓存的应用处理器
 * @author renhui
 *
 */
public class CacheShutDownApplicationProcessor extends
		AbstractApplicationProcessor {
	
	private CacheManager cacheManager;
	
	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void start() {

	}

	public void stop() {
		cacheManager.clearCaches();//清除缓存中的内容
		cacheManager.removeCaches();//把缓存从管理器中移除
		cacheManager.shutDown();//关闭缓存管理器
	}

	public String getApplicationNodePath() {
		return null;
	}

	public String getComponentConfigPath() {
		return null;
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {

	}

	public XmlNode getComponentConfig() {
		return null;
	}

	public XmlNode getApplicationConfig() {
		return null;
	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}

}
