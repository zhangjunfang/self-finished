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

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.Configuration;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.config.TinyFilterConfigInfo;
import org.tinygroup.weblayer.config.TinyFilterConfigInfos;
import org.tinygroup.weblayer.config.TinyWrapperFilterConfigInfo;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

/**
 * tiny filter处理器配置管理对象
 * 
 * @author renhui
 */
public class TinyFilterConfigManager implements Configuration {

	public static final String TINY_FILTER_CONFIGMANAGER = "tinyFilterConfigManager";
	private static final String TINY_FILTER_NODE_PATH = "/application/tiny-filters";

	private Map<String, TinyFilterConfigInfo> filterConfigMap = new HashMap<String, TinyFilterConfigInfo>();
	private List<TinyFilterConfigInfo> filterConfigs = new ArrayList<TinyFilterConfigInfo>();
	private List<TinyWrapperFilterConfigInfo> wrapperFilterConfigs = new ArrayList<TinyWrapperFilterConfigInfo>();
	private XmlNode applicationConfig;
	private XmlNode componentConfig;

	private static Logger logger = LoggerFactory
			.getLogger(TinyFilterConfigManager.class);

	public void addConfig(TinyFilterConfigInfos configInfos) {
		List<TinyFilterConfigInfo> configList = configInfos.getConfigs();
		for (TinyFilterConfigInfo configInfo : configList) {
			String name = configInfo.getConfigName();
			if (StringUtil.isBlank(name)) {
				logger.logMessage(LogLevel.WARN,
						"please set tiny filter name in name or id property");
			}
			if (filterConfigMap.containsKey(name)) {
				logger.logMessage(
						LogLevel.WARN,
						"already exist filter name:[{0}],please reset the filter name",
						name);
			} else {
				if (configInfo instanceof TinyWrapperFilterConfigInfo) {
					wrapperFilterConfigs
							.add((TinyWrapperFilterConfigInfo) configInfo);
				} else {
					filterConfigs.add(configInfo);
				}
				filterConfigMap.put(name, configInfo);

			}
		}

	}

	public void removeConfig(TinyFilterConfigInfos configInfos) {
		List<TinyFilterConfigInfo> configList = configInfos.getConfigs();
		for (TinyFilterConfigInfo configInfo : configList) {
			String name = configInfo.getConfigName();
			filterConfigMap.remove(name);
			if (configInfo instanceof TinyWrapperFilterConfigInfo) {
				wrapperFilterConfigs
						.remove((TinyWrapperFilterConfigInfo) configInfo);
			} else {
				filterConfigs.remove(configInfo);
			}
		}

	}

	public XmlNode getApplicationConfig() {
		return applicationConfig;
	}

	public String getApplicationNodePath() {
		return TINY_FILTER_NODE_PATH;
	}

	public String getComponentConfigPath() {
		return "/tinyfilter.config.xml";
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		this.applicationConfig = applicationConfig;
		this.componentConfig = componentConfig;
	}

	private void combineConfig(List<XmlNode> combineList) {
		XStream stream = XStreamFactory.getXStream("weblayer");
		for (XmlNode xmlNode : combineList) {
			TinyFilterConfigInfos filterConfigInfos = (TinyFilterConfigInfos) stream
					.fromXML(xmlNode.toString());
			List<TinyFilterConfigInfo> configInfos = filterConfigInfos
					.getConfigs();
			for (TinyFilterConfigInfo configInfo : configInfos) {
				String name = configInfo.getConfigName();
				if (filterConfigMap.containsKey(name)) {
					TinyFilterConfigInfo originalInfo = filterConfigMap
							.get(name);
					logger.logMessage(LogLevel.DEBUG,
							"filter name:[{0}] combine [{1}] with [{2}]", name,
							originalInfo, configInfo);
					originalInfo.combine(configInfo);
				} else {
					if (configInfo instanceof TinyWrapperFilterConfigInfo) {
						wrapperFilterConfigs
								.add((TinyWrapperFilterConfigInfo) configInfo);
					} else {
						filterConfigs.add(configInfo);
					}
					filterConfigMap.put(name, configInfo);
				}
			}
		}
	}
	
	/**
	 * 合并应用配置和组件配置
	 */
	public void combineConfig(){
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

	public TinyFilterConfigInfo getFilterConfig(String filterName) {
		return filterConfigMap.get(filterName);
	}

	public List<TinyFilterConfigInfo> getFilterConfigs() {
		return filterConfigs;
	}

	public List<TinyWrapperFilterConfigInfo> getWrapperFilterConfigs() {
		return wrapperFilterConfigs;
	}

}
