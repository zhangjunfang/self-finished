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

import org.jessma.ext.rest.RestResult;
import org.jessma.mvc.Action;

import com.thoughtworks.xstream.XStream;

/** XML 和 JSON 渲染器基类 */
public abstract class XStreamRenderer implements Renderer
{
	protected abstract XStream createXStreamObj();
	
	/** 输出渲染结果<br>
	 * 
	 * 1、输出时把 restResult 作为根对象，同时会处理 restResult 及其 model 属性的 XStream 相关注解。
	 * 因此，可以为 model 类添加  XStream 相关注解来定制输出内容。<br>
	 * 2、返回值：{@link Action#NONE}
	 * 
	 */
	@Override
	public String render(HttpServletRequest request, HttpServletResponse response, RestResult restResult) throws IOException
	{
		XStream xs = createXStreamObj();
		
		xs.setMode(XStream.NO_REFERENCES);
		xs.processAnnotations(restResult.getClass());
		
		Object model = restResult.getModel();
		if(model != null) xs.processAnnotations(model.getClass());

		xs.toXML(restResult, response.getWriter());
		
		return Action.NONE;
	}
}
