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

package org.jessma.ext.rest.renderer;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jessma.ext.rest.Rest;
import org.jessma.ext.rest.RestResult;


/** HTML 渲染器 */
public class HtmlRenderer implements Renderer
{
	@Override
	public String getContentType()
	{
		return null;
	}

	/** 输出渲染结果<br>
	 * 
	 * 1、当 restResult 的 model 属性不为 null 时，把 model 设置为 Request Attribute: {@linkplain Rest.Constant#REQ_ATTR_REST_MODEL REQ_ATTR_REST_MODEL} <br>
	 * 2、返回值：restResult 的 result 属性
	 * 
	 */
	@Override
	public String render(HttpServletRequest request, HttpServletResponse response, RestResult restResult) throws IOException
	{
		Object model = restResult.getModel();
		if(model != null) request.setAttribute(Rest.Constant.REQ_ATTR_REST_MODEL, model);
		
		return restResult.getResult();
	}
}
