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
package org.tinygroup.logger;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.ILoggerFactory;
import org.tinygroup.logger.impl.LoggerImpl;

/**
 * 日志工厂，用于获取Logger实例。
 * 
 * @author luoguo
 * 
 */
public final class LoggerFactory {

	private static ThreadLocal<Map<String, Object>> threadVariableMap = new ThreadLocal<Map<String, Object>>();

	private static ThreadLocal<LogLevel> threadLogLevel = new ThreadLocal<LogLevel>();

	private static Map<String, Logger> loggers = new HashMap<String, Logger>();

	private LoggerFactory() {
	}

	public ILoggerFactory getILoggerFactory() {
		return org.slf4j.LoggerFactory.getILoggerFactory();
	}

	public static Logger getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
	}

	public static Logger getLogger(String name) {
		if (loggers.containsKey(name)) {
			return loggers.get(name);
		}
		LoggerImpl loggerImpl = new LoggerImpl(
				org.slf4j.LoggerFactory.getLogger(name));
		loggers.put(name, loggerImpl);
		return loggerImpl;
	}

	public synchronized static void putThreadVariable(String key, String value) {
		Map<String, Object> valueMap = threadVariableMap.get();
		if (valueMap == null) {
			valueMap = new HashMap<String, Object>();
			threadVariableMap.set(valueMap);
		}
		if (valueMap.size() <= 10000) {
			valueMap.put(key, value);
		}
	}

	public synchronized static Map<String, Object> getThreadVariableMap() {
		return threadVariableMap.get();
	}

	public synchronized static LogLevel getThreadLogLevel() {
		return threadLogLevel.get();
	}

	public synchronized static void setThreadLogLevel(LogLevel logLevel) {
		threadLogLevel.set(logLevel);
	}

	/**
	 * 移除保存在日志工厂类中的日志对象
	 */
	public static void clearAllLoggers() {
		loggers.clear();
		threadVariableMap.set(null);
		threadLogLevel.set(null);
	}
}
