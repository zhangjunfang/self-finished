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

/** REST 视图类型 */
public enum RenderType
{
	HTML
	{
		private Renderer DEFAULT_RENDERER = new HtmlRenderer();
		
		@Override
		public String toMimeType()
		{
			return "text/html";
		}

		@Override
		public Renderer getDefaultRenenderer()
		{
			return DEFAULT_RENDERER;
		}
	},
	
	XML
	{
		private Renderer DEFAULT_RENDERER = new XmlRenderer();

		@Override
		public String toMimeType()
		{
			return "application/xml";
		}
		
		@Override
		public Renderer getDefaultRenenderer()
		{
			return DEFAULT_RENDERER;
		}
	},

	JSON
	{
		private Renderer DEFAULT_RENDERER = new JsonRenderer();

		@Override
		public String toMimeType()
		{
			return "application/x-json";
		}
		
		@Override
		public Renderer getDefaultRenenderer()
		{
			return DEFAULT_RENDERER;
		}
	};
	
	@Override
	public String toString()
	{
		return super.toString().toLowerCase();
	}
	
	/** 获取视图的 MIME 类型 */
	public abstract String toMimeType();
	/** 获取视图的默认渲染器 */
	public abstract Renderer getDefaultRenenderer();
}
