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

package org.jessma.mvc;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Queue;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jessma.util.BeanHelper;

/** Action 执行器，在 {@link ActionFilter} 的 doFilter() 方法中使用 */
public class ActionExecutor
{
	private Queue<ActionFilter> filters;
	private Action action;
	private Method method;
	private ServletContext context;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	@SuppressWarnings("unchecked")
	ActionExecutor(LinkedList<ActionFilter> filters, Action action, Method method, ServletContext context, HttpServletRequest request, HttpServletResponse response)
	{
		this.action		= action;
		this.method		= method;
		this.context	= context;
		this.request	= request;
		this.response	= response;
		this.filters	= (Queue<ActionFilter>)filters.clone();
	}
	
	/** 
	 * 调用 {@link ActionFilter} 拦截器堆栈中的下一个拦截器的 doFilter() 方法或
	 * 调用 {@link Action} 的 execute() 方法 
	*/
	public String invoke() throws Exception
	{
		if(filters.isEmpty())
			return execute(action, method);
		else
		{
			ActionFilter filter = filters.poll();
			return filter.doFilter(this);
		}
	}

	static final String execute(Action action, Method method) throws Exception
	{
		boolean valid = true;
		
		if(action instanceof ActionSupport && ActionSupport.isBeanValidationEnable())
		{
			ActionSupport asp = (ActionSupport)action;
			
			if(asp.isAutoValidation())
				valid = asp.validateFormBean();
		}

		if(valid)	valid = action.validate();
		if(!valid)	return Action.INPUT;
		
		return BeanHelper.invokeMethod(action, method);
	}
	
	/** 获取当前被拦截的 {@link Action} 对象 */
	public Action getAction()
	{
		return action;
	}
	
	/** 获取被拦截 {@link Action} 的入口方法 */
	public Method getEntryMethod()
	{
		return method;
	}

	/** 获取 {@link ServletContext} 对象 */
	public ServletContext getServletContext()
	{
		return context;
	}

	/** 获取 {@link HttpServletRequest} 对象 */
	public HttpServletRequest getRequest()
	{
		return request;
	}

	/** 获取 {@link HttpServletResponse} 对象 */
	public HttpServletResponse getResponse()
	{
		return response;
	}
}
