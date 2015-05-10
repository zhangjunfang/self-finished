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
import org.tinygroup.weblayer.config.TinyProcessorConfigInfo;
import org.tinygroup.weblayer.config.TinyProcessorConfigInfos;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

/**
 * tiny processor处理器配置管理对象
 * 
 * @author renhui
 * 
 */
public class TinyProcessorConfigManager implements Configuration {

	public static final String TINY_PROCESSOR_CONFIGMANAGER = "tinyProcessorConfigManager";

	private static final String TINY_PROCESSOR_NODE_PATH = "/application/tiny-processors";

	private Map<String, TinyProcessorConfigInfo> processorMap = new HashMap<String, TinyProcessorConfigInfo>();

	private List<TinyProcessorConfigInfo> processorConfigs=new ArrayList<TinyProcessorConfigInfo>();
	
	private XmlNode applicationConfig;

	private XmlNode componentConfig;

	private static Logger logger = LoggerFactory
			.getLogger(TinyProcessorConfigManager.class);

	public void addConfig(TinyProcessorConfigInfos configInfos) {
		List<TinyProcessorConfigInfo> configList = configInfos.getConfigInfos();
		for (TinyProcessorConfigInfo configInfo : configList) {
			String name = configInfo.getConfigName();
			if (StringUtil.isBlank(name)) {
				logger.logMessage(LogLevel.WARN,
						"please set tiny processor name in name or id property");
			}
			if (processorMap.containsKey(name)) {
				logger.logMessage(
						LogLevel.WARN,
						"already exist processor name:[{0}],please reset the processor name",
						name);
			} else {
				processorMap.put(name, configInfo);
				processorConfigs.add(configInfo);
			}
		}
	}

	public void removeConfig(TinyProcessorConfigInfos configInfos) {
		List<TinyProcessorConfigInfo> configList = configInfos.getConfigInfos();
		for (TinyProcessorConfigInfo configInfo : configList) {
			String name = configInfo.getConfigName();
			processorMap.remove(name);
			processorConfigs.remove(configInfo);
		}
	}

	public XmlNode getApplicationConfig() {
		return applicationConfig;
	}

	public String getApplicationNodePath() {
		return TINY_PROCESSOR_NODE_PATH;
	}

	public String getComponentConfigPath() {
		return "/tinyprocessor.config.xml";
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		this.applicationConfig = applicationConfig;
		this.componentConfig = componentConfig;
	}

	private void combineConfig(List<XmlNode> combineList) {
		XStream stream = XStreamFactory.getXStream("weblayer");
		for (XmlNode xmlNode : combineList) {
			TinyProcessorConfigInfos filterConfigInfos = (TinyProcessorConfigInfos) stream
					.fromXML(xmlNode.toString());
			List<TinyProcessorConfigInfo> configInfos = filterConfigInfos
					.getConfigInfos();
			for (TinyProcessorConfigInfo configInfo : configInfos) {
				String name = configInfo.getConfigName();
				if (processorMap.containsKey(name)) {
					TinyProcessorConfigInfo originalInfo = processorMap
							.get(name);
					logger.logMessage(LogLevel.DEBUG,
							"processor name:[{0}] combine [{1}] with [{2}]",
							name, originalInfo, configInfo);
					originalInfo.combine(configInfo);
				} else {
					processorMap.put(name, configInfo);
					processorConfigs.add(configInfo);
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

	public TinyProcessorConfigInfo getProcessorConfig(String processorName) {
		return processorMap.get(processorName);
	}
	
	public List<TinyProcessorConfigInfo> getProcessorConfigs(){
		return processorConfigs;
	}
}
