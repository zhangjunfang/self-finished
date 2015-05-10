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
package org.tinygroup.weblayer;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * tiny servlet 处理器的抽象实现
 * 
 * @author renhui
 * 
 */
public abstract class AbstractTinyProcessor implements TinyProcessor {

	protected String processorName;

	protected TinyProcessorConfig tinyProcessorConfig;

	protected static Logger logger = LoggerFactory
			.getLogger(AbstractTinyProcessor.class);

	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}

	public String getProcessorName() {
		return processorName;
	}

	public void init(TinyProcessorConfig tinyProcessorConfig)
			throws ServletException {
		this.tinyProcessorConfig = tinyProcessorConfig;
		customInit();
	}

	protected abstract void customInit() throws ServletException;

	public void destroy() {

	}

	public boolean isMatch(String urlString) {
		return tinyProcessorConfig.isMatch(urlString);
	}

	public void process(String urlString, WebContext context)
			throws ServletException, IOException {
		reallyProcess(urlString, context);
	}
	
	protected String get(String param) {
		return tinyProcessorConfig.getInitParameter(param);
	}
	
	protected Map<String, String> getInitParamMap() {
		return tinyProcessorConfig.getParameterMap();
	}

	/**
	 * tinyprocessor的逻辑处理方法，由子类来完成
	 * 
	 * @param urlString
	 * @param context
	 */
	public abstract void reallyProcess(String urlString, WebContext context)
			throws ServletException, IOException;
}
