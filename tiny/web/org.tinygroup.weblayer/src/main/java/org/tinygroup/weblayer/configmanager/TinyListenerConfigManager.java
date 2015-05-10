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
package org.tinygroup.weblayer.configmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionListener;

import org.tinygroup.config.Configuration;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.config.BasicConfigInfo;
import org.tinygroup.weblayer.config.TinyListenerConfigInfo;
import org.tinygroup.weblayer.config.TinyListenerConfigInfos;
import org.tinygroup.weblayer.listener.impl.ListenerBuilderSupport;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

/**
 * tiny listener处理器配置管理对象
 * 
 * @author renhui
 */
public class TinyListenerConfigManager implements Configuration {

	private static final String TINY_LISTENER_NODE_PATH = "/application/tiny-listeners";

	private Map<String, BasicConfigInfo> basicConfigMap = new HashMap<String, BasicConfigInfo>();
	private Map<String, List<BasicConfigInfo>> configMap = new HashMap<String, List<BasicConfigInfo>>();
	private ListenerBuilderSupport support=new ListenerBuilderSupport();
	private XmlNode applicationConfig;
	private XmlNode componentConfig;

	private static Logger logger = LoggerFactory
			.getLogger(TinyListenerConfigManager.class);
	

	public void addConfig(TinyListenerConfigInfos configInfos) {
		List<TinyListenerConfigInfo> configList = configInfos
				.getListenerConfigInfos();
		for (TinyListenerConfigInfo configInfo : configList) {
			addListenerConfig(configInfo.getServletContextListenerConfigs());
			addListenerConfig(configInfo
					.getServletContextAttributeListenerConfigs());
			addListenerConfig(configInfo
					.getServletRequestAttributeListenerConfigs());
			addListenerConfig(configInfo.getServletRequestListenerConfigs());
			addListenerConfig(configInfo.getSessionActivationListenerConfigs());
			addListenerConfig(configInfo.getSessionAttributeListenerConfigs());
			addListenerConfig(configInfo.getSessionBindingListenerConfigs());
			addListenerConfig(configInfo.getSessionListenerConfigs());
		}

	}

	private <T extends BasicConfigInfo> void addListenerConfig(List<T> configs) {
		for (BasicConfigInfo config : configs) {
			if (basicConfigMap.containsKey(config.getConfigName())) {
				logger.logMessage(
						LogLevel.WARN,
						"already exist listener name:[{0}],please reset the listener name",
						config.getConfigName());
			} else {
				basicConfigMap.put(config.getConfigName(), config);
				collectConfigByBeanName(config);
			}
		}
	}

	private <T extends BasicConfigInfo> void removeListenerConfig(
			List<T> configs) {
		for (BasicConfigInfo config : configs) {
			basicConfigMap.remove(config.getConfigName());
			removeConfigByBeanName(config);
		}
	}

	/**
	 * 
	 * @param configInfo
	 * @param config
	 */
	private void collectConfigByBeanName(BasicConfigInfo config) {
		List<BasicConfigInfo> configInfos = configMap.get(config
				.getConfigBeanName());
		if (configInfos == null) {
			configInfos = new ArrayList<BasicConfigInfo>();
			configMap.put(config.getConfigBeanName(), configInfos);
		}
		configInfos.add(config);
	}

	/**
	 * 
	 * @param configInfo
	 * @param config
	 */
	private void removeConfigByBeanName(BasicConfigInfo config) {
		List<BasicConfigInfo> configInfos = configMap.get(config
				.getConfigBeanName());
		if (configInfos != null) {
			configInfos.remove(config);
		}
	}

	public void removeConfig(TinyListenerConfigInfos configInfos) {
		List<TinyListenerConfigInfo> configList = configInfos
				.getListenerConfigInfos();
		for (TinyListenerConfigInfo configInfo : configList) {
			removeListenerConfig(configInfo
					.getServletContextAttributeListenerConfigs());
			removeListenerConfig(configInfo
					.getServletContextAttributeListenerConfigs());
			removeListenerConfig(configInfo
					.getSessionActivationListenerConfigs());
			removeListenerConfig(configInfo
					.getSessionAttributeListenerConfigs());
			removeListenerConfig(configInfo.getSessionBindingListenerConfigs());
			removeListenerConfig(configInfo.getSessionListenerConfigs());
			removeListenerConfig(configInfo
					.getServletRequestAttributeListenerConfigs());
			removeListenerConfig(configInfo.getServletRequestListenerConfigs());
		}

	}

	public XmlNode getApplicationConfig() {
		return applicationConfig;
	}

	public String getApplicationNodePath() {
		return TINY_LISTENER_NODE_PATH;
	}

	public String getComponentConfigPath() {
		return "/tinylistener.config.xml";
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		this.applicationConfig = applicationConfig;
		this.componentConfig = componentConfig;
	}

	private void combineConfig(List<XmlNode> combineList) {
		XStream stream = XStreamFactory.getXStream("weblayer");
		for (XmlNode xmlNode : combineList) {
			TinyListenerConfigInfos listenerConfigInfos = (TinyListenerConfigInfos) stream
					.fromXML(xmlNode.toString());
			addConfig(listenerConfigInfos);
		}
	}

	/**
	 * 合并应用配置和组件配置
	 */
	public void combineConfig() {
		List<XmlNode> combineList = new ArrayList<XmlNode>();
		if (componentConfig != null) {
			combineList.add(componentConfig);
		}
		if (applicationConfig != null) {
			combineList.add(applicationConfig);
		}
		combineConfig(combineList);
	}

	public XmlNode getComponentConfig() {
		return componentConfig;
	}

	/**
	 * 根据配置进行实例化
	 */
	public void newInstance() {
		support.listenerInstanceBuilder(configMap);
	}


	public List<ServletContextListener> getContextListeners() {
		return support.getContextListeners();
	}

	public List<ServletContextAttributeListener> getContextAttributeListeners() {
		return support.getContextAttributeListeners();
	}

	public List<HttpSessionListener> getSessionListeners() {
		return support.getSessionListeners();
	}

	public List<HttpSessionAttributeListener> getSessionAttributeListeners() {
		return support.getSessionAttributeListeners();
	}

	public List<HttpSessionActivationListener> getSessionActivationListeners() {
		return support.getSessionActivationListeners();
	}

	public List<HttpSessionBindingListener> getSessionBindingListeners() {
		return support.getSessionBindingListeners();
	}

	public List<ServletRequestAttributeListener> getRequestAttributeListeners() {
		return support.getRequestAttributeListeners();
	}

	public List<ServletRequestListener> getRequestListeners() {
		return support.getRequestListeners();
	}

}
