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

import com.thoughtworks.xstream.annotations.XStreamAlias;

/** REST 处理结果，由 REST 请求处理方法返回 */
@XStreamAlias(RestResult.DEFAULT_ROOT_NAME)
public class RestResult
{
	/** XML 结果视图的默认根元素名称 */
	public static final String DEFAULT_ROOT_NAME	= "rest";
	
	private String result;
	private Object model;
	
	public RestResult(String result)
	{
		this(result, null);
	}
	
	public RestResult(String result, Object model)
	{
		this.result	= result;
		this.model	= model;
	}

	/** 获取 Action Result */
	public String getResult()
	{
		return result;
	}

	/** 获取 REST 模型对象 */
	public Object getModel()
	{
		return model;
	}
}
