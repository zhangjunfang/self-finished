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

package org.jessma.ext.rest;

import org.jessma.ext.rest.renderer.RenderType;

/** REST 请求上下文 */
public class RestContext
{
	RequestType requestType;
	String requestPath;
	RenderType renderType;
	
	public RestContext()
	{
		
	}
	
	public RestContext(RequestType requestType, String requestPath, RenderType renderType)
	{
		this.requestType = requestType;
		this.requestPath = requestPath;
		this.renderType = renderType;
	}

	@Override
	public String toString()
	{
		return String.format("{%s, %s, %s}", requestType, renderType, requestPath);
	}

	/** 获取请求类型（GET/POST/PUT/DELETE） */
	public RequestType getRequestType()
	{
		return requestType;
	}

	/** 获取请求路径（不包括 Entity 部分） */
	public String getRequestPath()
	{
		return requestPath;
	}

	/** 获取视图类型（HTML/XML/JSON） */
	public RenderType getRenderType()
	{
		return renderType;
	}
}
