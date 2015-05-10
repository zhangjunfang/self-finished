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
package org.tinygroup.weblayer.filter;

import java.io.IOException;

import javax.servlet.ServletException;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.session.SessionConfiguration;
import org.tinygroup.weblayer.webcontext.session.SessionConfiguration.ConfigImpl;
import org.tinygroup.weblayer.webcontext.session.impl.SessionWebContextImpl;

/**
 * 增强的Session框架，可将session中的对象保存到cookie、数据库或其它存储中。
 * 
 * @author renhui
 * 
 */
public class SessionTinyFilter extends AbstractTinyFilter {

	private static final String SESSION_CONFIGURATION_BEAN_NAME = "sessionConfiguration";

	private ConfigImpl config ;
	
	
	@Override
	protected void customInit() {
		SessionConfiguration sessionConfiguration=BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBean(
				SESSION_CONFIGURATION_BEAN_NAME);
		config=sessionConfiguration.getSessionConfig();
	}
	
	
	public void preProcess(WebContext context) throws ServletException, IOException {

	}

	
	public void postProcess(WebContext context) throws ServletException, IOException {
		SessionWebContextImpl session = (SessionWebContextImpl) context;
		session.commit();
	}

	public WebContext getAlreadyWrappedContext(WebContext wrappedContext) {
		return new SessionWebContextImpl(wrappedContext, config);
	}

	public int getOrder() {
		return SESSION_FILTER_PRECEDENCE;
	}

}
