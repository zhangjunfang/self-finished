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
package org.tinygroup.dbrouter.factory;

import java.io.InputStream;

import org.tinygroup.cache.Cache;
import org.tinygroup.cache.jcs.JcsCache;
import org.tinygroup.dbrouter.RouterManager;
import org.tinygroup.factory.BeanFactory;
import org.tinygroup.factory.Factory;
import org.tinygroup.factory.config.Beans;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

/**
 * 功能说明:创建集群管理对象的类
 * 
 * 开发人员: renhui <br>
 * 开发时间: 2013-12-25 <br>
 * <br>
 */
public final class RouterManagerBeanFactory {

	public static final String ROUTER_MANAGER = "routerManager";
	private static Factory factory;
	private static RouterManager manager;
	private static Logger logger = LoggerFactory
			.getLogger(RouterManagerBeanFactory.class);
	private static String DEFAULT_REGION = "dbrouter";

	private static final String DEFAULT_ROUTER_BEANS_XML = "/defaultbeans.xml";

	private static final String CUSTOM_ROUTER_BEANS_XML = "/custombeans.xml";

	static {
		factory = BeanFactory.getFactory();
		XStream xStream = XStreamFactory.getXStream();
		String beansFile = CUSTOM_ROUTER_BEANS_XML;
		InputStream inputStream = RouterManagerBeanFactory.class
				.getResourceAsStream(CUSTOM_ROUTER_BEANS_XML);
		if (inputStream == null) {
			inputStream = RouterManagerBeanFactory.class
					.getResourceAsStream(DEFAULT_ROUTER_BEANS_XML);
			beansFile = DEFAULT_ROUTER_BEANS_XML;
		}
		logger.logMessage(LogLevel.INFO, "加载Bean配置文件{}开始...", beansFile);
		try {
			Beans beans = (Beans) xStream.fromXML(inputStream);
			factory.addBeans(beans);
			factory.init();
			logger.logMessage(LogLevel.INFO, "加载Bean配置文件{}结束。", beansFile);
		} catch (Exception e) {
			logger.errorMessage("加载Bean配置文件{}时发生错误", e, beansFile);
		}
	}

	private RouterManagerBeanFactory() {

	}

	public static RouterManager getManager() {
		return getManager(DEFAULT_REGION);
	}

	public static RouterManager getManager(String region) {
		return getManager(region, null);
	}

	public static RouterManager getManager(String region, Cache cache) {
		if (manager == null) {
			manager = factory.getBean(ROUTER_MANAGER);
			Cache orignalCache = manager.getCache();
			if (cache == null && orignalCache == null) {
				cache = new JcsCache();
			} else if (cache == null && orignalCache != null) {
				cache = orignalCache;
			}
			cache.init(region);
			manager.setCache(cache);
		}
		return manager;
	}

}
