/*
 * Copyright Bruce Liang (ldcsaa@gmail.com)
 *
 * Version	: JessMA 3.5.1
 * Author	: Bruce Liang
 * Website	: http://www.jessma.org
 * Project	: http://www.oschina.net/p/portal-basic
 * Blog		: http://www.cnblogs.com/ldcsaa
 * WeiBo	: http://weibo.com/u/1402935851
 * QQ Group	: 75375912
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jessma.mvc.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.jessma.mvc.Action;
import org.jessma.mvc.ActionExecutor;
import org.jessma.mvc.ActionFilter;

/** 国际化拦截器基类 */
abstract public class AbstractI18nFilter implements ActionFilter
{
	abstract protected Locale parseLocale(ActionExecutor executor);
	
	@Override
	public void init()
	{
	}

	@Override
	public void destroy()
	{
	}

	@Override
	public String doFilter(ActionExecutor executor) throws Exception
	{
		HttpServletRequest request	= executor.getRequest();
		Locale __locale				= (Locale)request.getAttribute(Action.Constant.I18N_ATTR_LOCALE);
		
		if(__locale == null)
		{
			__locale = parseLocale(executor);
			
			if(__locale != null)
				request.setAttribute(Action.Constant.I18N_ATTR_LOCALE, __locale);
		}
		
		return executor.invoke();
	}
}
