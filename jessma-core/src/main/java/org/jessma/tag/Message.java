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

package org.jessma.tag;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.jessma.mvc.Action;
import org.jessma.util.GeneralHelper;

/** I18N 消息标签类 <br/>
 * 
 * <ul>
 * 	<li>key		- 消息键 </li>
 * 	<li>res		- 资源 Bundle </li>
 * 	<li>locale	- 语言区域 </li>
 * 	<li>escape	- 是否过滤 XML 特殊字符 </li>
 * 	<li>p0-p9	- 消息参数 </li>
 * 	<li>params	- 消息参数数组 </li>
 * </ul>
 * 
 */
public class Message extends SimpleTagSupport
{
	private String key;
	private String res;
	private String locale;
	private boolean escape = true;
	
	private Object p0;
	private Object p1;
	private Object p2;
	private Object p3;
	private Object p4;
	private Object p5;
	private Object p6;
	private Object p7;
	private Object p8;
	private Object p9;
	
	private Object params;
	
	/** 输出标签内容 */
	@Override
	public void doTag() throws JspException, IOException
	{
		PageContext context	= (PageContext)getJspContext();
		Locale __locale		= null;
		
		if(GeneralHelper.isStrEmpty(locale))
			__locale = (Locale)context.findAttribute(Action.Constant.I18N_ATTR_LOCALE);
		else
			__locale = GeneralHelper.getAvailableLocale(locale);
		
		if(__locale == null)
			__locale = context.getRequest().getLocale();
		
		if(GeneralHelper.isStrEmpty(res))
		{
			res = (String)getJspContext().getAttribute(	Action.Constant.APP_ATTR_DEFAULT_APP_BUNDLE,
														PageContext.APPLICATION_SCOPE	);	
			if(res == null)
				res = Action.Constant.DEFAULT_APP_BUNDLE;
		}
		
		Object[] p = null;
		
		if(params == null)
			p = new Object[] {p0, p1, p2, p3, p4, p5, p6, p7, p8, p9};
		else
			p = GeneralHelper.object2Array(params);

		String msg		= GeneralHelper.getResourceMessage(__locale, res, key, p);
		if(escape) msg	= GeneralHelper.escapeXML(msg);
		
		getJspContext().getOut().print(msg);
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public void setRes(String res)
	{
		this.res = res;
	}

	public void setLocale(String locale)
	{
		this.locale = locale;
	}
	
	public void setEscape(boolean escape)
	{
		this.escape = escape;
	}
	
	public void setP0(Object p0)
	{
		this.p0 = p0;
	}

	public void setP1(Object p1)
	{
		this.p1 = p1;
	}

	public void setP2(Object p2)
	{
		this.p2 = p2;
	}

	public void setP3(Object p3)
	{
		this.p3 = p3;
	}

	public void setP4(Object p4)
	{
		this.p4 = p4;
	}

	public void setP5(Object p5)
	{
		this.p5 = p5;
	}

	public void setP6(Object p6)
	{
		this.p6 = p6;
	}

	public void setP7(Object p7)
	{
		this.p7 = p7;
	}

	public void setP8(Object p8)
	{
		this.p8 = p8;
	}

	public void setP9(Object p9)
	{
		this.p9 = p9;
	}

	public void setParams(Object params)
	{
		this.params = params;
	}
}
