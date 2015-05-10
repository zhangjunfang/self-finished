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
package org.tinygroup.config;

import org.tinygroup.xmlparser.node.XmlNode;

import java.util.Collection;
import java.util.Map;

/**
 * 应用配置管理器
 *
 * @author luoguo
 */
public interface ConfigurationManager {

    /**
     * 设置配置加载器
     *
     * @param configurationLoader
     */
    void setConfigurationLoader(ConfigurationLoader configurationLoader);

    void setApplicationConfiguration(XmlNode applicationConfiguration);

    void setComponentConfigurationMap(Map<String, XmlNode> componentConfigurationMap);

    void setComponentConfiguration(String key, XmlNode componentConfiguration);

    XmlNode getApplicationConfiguration();

    Map<String, XmlNode> getComponentConfigurationMap();

    XmlNode getComponentConfiguration(String key);

    /**
     * 分发应用配置<br>
     * 应用配置会促使配置管理器把配置信息推送到配置订阅者
     */
    void distributeConfiguration();


    void setConfigurationList(Collection<Configuration> configurationList);
    /**
     * 设置KeyValue形式的值
     *
     * @param key
     * @param value
     */
    <T> void setConfiguration(String key, String value);

    Map<String,String> getConfiguration();
    /**
     * @param type
     * @param key
     * @param defaultValue
     * @param <T>
     * @return
     */
    <T> T getConfiguration(Class<T> type, String key, T defaultValue);
}
