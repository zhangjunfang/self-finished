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

import javax.servlet.ServletException;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.webcontext.DefaultWebContext;

/**
 * tinyfilter的抽象实现
 * 
 * @author renhui
 * 
 */
public abstract class AbstractTinyFilter implements TinyFilter {
	protected String filterName;

	protected TinyFilterConfig tinyFilterConfig;

	protected static Logger logger = LoggerFactory
			.getLogger(AbstractTinyFilter.class);

	public void initTinyFilter(TinyFilterConfig config) throws ServletException {
		this.tinyFilterConfig = config;
		customInit();
	}
    /**
     * 由客户自定义初始化
     */
	protected abstract void customInit();

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public String getFilterName() {
		return filterName;
	}


	public void destroyTinyFilter() {

	}

	public boolean isMatch(String url) {
		return tinyFilterConfig.isMatch(url);
	}

	public WebContext wrapContext(WebContext wrappedContext) {
		WebContext context = getAlreadyWrappedContext(wrappedContext);
		initContext(context);
		return context;
	}

	protected void initContext(WebContext context) {

	}

	/**
	 * 
	 * 返回已经包装的上下文
	 * 
	 * @param wrappedContext
	 * @return
	 */
	protected WebContext getAlreadyWrappedContext(WebContext wrappedContext) {
		return new DefaultWebContext(wrappedContext);
	}

	protected String get(String param) {
		return tinyFilterConfig.getInitParameter(param);
	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}

}
