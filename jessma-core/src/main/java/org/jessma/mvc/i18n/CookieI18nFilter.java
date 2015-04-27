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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.jessma.mvc.Action;
import org.jessma.mvc.ActionExecutor;
import org.jessma.util.GeneralHelper;
import org.jessma.util.http.HttpHelper;

/** 基于 {@link Cookie} 的国际化拦截器类 */
public class CookieI18nFilter extends AbstractI18nFilter
{
	@Override
	protected Locale parseLocale(ActionExecutor executor)
	{
		Locale __locale				= null;
		HttpServletRequest request	= executor.getRequest();
		String locale				= HttpHelper.getCookieValue(request, Action.Constant.I18N_ATTR_LOCALE);
		
		if(locale != null)
			__locale = GeneralHelper.getAvailableLocale(locale);
		
		return __locale;
	}
}
