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
package org.tinygroup.fileresolver;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.tinygroup.config.Configuration;
import org.tinygroup.vfs.FileObject;

/**
 * 文件查找器
 * 
 * @author luoguo
 */
public interface FileResolver extends Configuration {

	String BEAN_NAME = "fileResolver";

	/**
	 * 返回所有的文件处理器
	 * 
	 * @return
	 */
	List<FileProcessor> getFileProcessorList();

	/**
	 * 返回当前FileResolver要扫描的文件列表
	 * 
	 * @return
	 */
	List<String> getScanningPaths();

	/**
	 * 手工添加扫描的匹配列表，如果有包含列表，则按包含列表
	 * 
	 * @param pattern
	 */
	void addIncludePathPattern(String pattern);

	Map<String, Pattern> getIncludePathPatternMap();

	/**
	 * 添加扫描的路径
	 * 
	 * @param fileObject
	 */
	void addResolveFileObject(FileObject fileObject);

	void addResolvePath(String path);

	void addResolvePath(List<String> paths);
	
	void removeResolvePath(String path);

	/**
	 * 增加文件处理器
	 * 
	 * @param fileProcessor
	 */
	void addFileProcessor(FileProcessor fileProcessor);

	void setClassLoader(ClassLoader classLoader);

	ClassLoader getClassLoader();

	/**
	 * 开始找文件
	 */
	void resolve();

	/**
	 * 开始找文件
	 */
	void refresh();

	/**
	 * 获取文件处理的线程数目
	 * 
	 * @return
	 */
	int getFileProcessorThreadNumber();

	/**
	 * 设置文件处理的线程数目
	 * 
	 * @param threadNum
	 */
	void setFileProcessorThreadNumber(int threadNum);

	void addChangeLisenter(ChangeListener lisenter);

	List<ChangeListener> getChangeListeners();
	/**
	 * 触发 ChangeLisenters
	 * 
	 */
	void change();
}
