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
package org.tinygroup.config.impl;

import org.tinygroup.commons.tools.ValueUtil;
import org.tinygroup.config.Configuration;
import org.tinygroup.config.ConfigurationLoader;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.PathFilter;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luoguo on 2014/5/14.
 */
public class ConfigurationManagerImpl implements org.tinygroup.config.ConfigurationManager {
    private static Logger logger = LoggerFactory.getLogger(ConfigurationManagerImpl.class);
    private ConfigurationLoader configurationLoader;
    private XmlNode applicationConfiguration;
    private Map<String, XmlNode> componentConfigurationMap = new HashMap<String, XmlNode>();
    private Collection<Configuration> configurationList;
    private Map<String, String> configuration = new HashMap<String, String>();

    public void setConfigurationLoader(ConfigurationLoader configurationLoader) {
        this.configurationLoader = configurationLoader;
    }

    public void setApplicationConfiguration(XmlNode applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    public void setComponentConfigurationMap(Map<String, XmlNode> componentConfigurationMap) {
        this.componentConfigurationMap = componentConfigurationMap;
    }

    public void setComponentConfiguration(String key, XmlNode componentConfiguration) {
        componentConfigurationMap.put(key, componentConfiguration);
    }

    public XmlNode getApplicationConfiguration() {
        return applicationConfiguration;
    }

    public Map<String, XmlNode> getComponentConfigurationMap() {
        return componentConfigurationMap;
    }

    public XmlNode getComponentConfiguration(String key) {
        return componentConfigurationMap.get(key);
    }

    public void distributeConfiguration() {
        if (configurationList != null) {
            logger.logMessage(LogLevel.INFO, "正在分发应用配置信息...");
            PathFilter<XmlNode> pathFilter = new PathFilter<XmlNode>(applicationConfiguration);
            for (Configuration configuration : configurationList) {
                XmlNode componentConfig = componentConfigurationMap.get(configuration.getComponentConfigPath());
                XmlNode appConfig = null;
                if (configuration.getApplicationNodePath() != null) {
                    appConfig = pathFilter.findNode(configuration.getApplicationNodePath());
                }
                configuration.config(appConfig, componentConfig);
            }
            logger.logMessage(LogLevel.INFO, "应用配置信息分发完毕");
        }

    }

    public void setConfigurationList(Collection<Configuration> configurationList) {
        this.configurationList = configurationList;
    }

    public <T> void setConfiguration(String key, String value) {
        configuration.put(key, value);
    }

    public <T> T getConfiguration(Class<T> type, String key, T defaultValue) {
        String value =  configuration.get(key);
        if (value == null ||"".equals(value)) {
           return defaultValue;
        }
        return (T) ValueUtil.getValue(value, type.getName());
    }

    public void loadConfiguration() {
        if (configurationLoader != null) {
            setApplicationConfiguration(configurationLoader.loadApplicationConfiguration());
            setComponentConfigurationMap(configurationLoader.loadComponentConfiguration());
        }
    }

	public Map<String, String> getConfiguration() {
		return configuration;
	}
}
