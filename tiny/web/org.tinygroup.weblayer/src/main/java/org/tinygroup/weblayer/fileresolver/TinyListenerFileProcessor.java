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
package org.tinygroup.weblayer.fileresolver;

import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.weblayer.config.TinyListenerConfigInfos;
import org.tinygroup.weblayer.configmanager.TinyListenerConfigManager;
import org.tinygroup.weblayer.configmanager.TinyListenerConfigManagerHolder;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

/**
 * 搜索tinylistener的文件处理器
 * 
 * 
 */
public class TinyListenerFileProcessor extends AbstractFileProcessor {

	private static final String LISTENER_EXT_FILENAMES = ".tinylisteners.xml";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(LISTENER_EXT_FILENAMES);
	}

	public void process() {
		XStream stream = XStreamFactory.getXStream("weblayer");
		TinyListenerConfigManager configManager = TinyListenerConfigManagerHolder
				.getInstance();
		for (FileObject fileObject : deleteList) {
			logger.log(LogLevel.INFO, "正在移除tiny-listener描述文件：<{}>",
					fileObject.getAbsolutePath());
			TinyListenerConfigInfos oldConfigs = (TinyListenerConfigInfos) caches
					.get(fileObject.getAbsolutePath());
			if (oldConfigs != null) {
				configManager.removeConfig(oldConfigs);
				caches.remove(fileObject.getAbsolutePath());
			}
			logger.log(LogLevel.INFO, "移除tiny-listener描述文件：<{}>结束",
					fileObject.getAbsolutePath());
		}

		for (FileObject fileObject : changeList) {
			TinyListenerConfigInfos oldConfigs = (TinyListenerConfigInfos) caches
					.get(fileObject.getAbsolutePath());
			if (oldConfigs != null) {
				configManager.removeConfig(oldConfigs);
			}
			logger.log(LogLevel.INFO, "找到tiny-listener描述文件：<{}>",
					fileObject.getAbsolutePath());
			TinyListenerConfigInfos configInfos = (TinyListenerConfigInfos) stream
					.fromXML(fileObject.getInputStream());
			configManager.addConfig(configInfos);
			caches.put(fileObject.getAbsolutePath(), configInfos);
		}
		configManager.combineConfig();// 混合组件与应用配置
		configManager.newInstance();// 监听器实例化
	}

}
