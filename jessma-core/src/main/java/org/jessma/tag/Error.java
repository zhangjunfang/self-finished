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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.jessma.mvc.Action;
import org.jessma.mvc.ActionSupport;
import org.jessma.util.GeneralHelper;

/** Action 错误消息标签类 <br/>
 * 
 * <ul>
 * 	<li>key			- 消息键 </li>
 * 	<li>index		- 某个键下的第 index 个消息（从 0 开始），如果设置为 -1 则获取该键下的所有消息 </li>
 * 	<li>element		- 使用 DIV, SPAN 或 UL/OL 元素显示消息（默认：SPAN） </li>
 * 	<li>escape		- 是否过滤 XML 特殊字符 </li>
 * 	<li>cssClass	- DIV, SPAN 或 UL/OL 的 class 属性 </li>
 * 	<li>cssStyle	- DIV, SPAN 或 UL/OL 的 style 属性 </li>
 * 	<li>cssAlign	- DIV 的 align 属性（只对 DIV 有效） </li>
 * 	<li>cssId		- DIV, SPAN 或 UL/OL 的 id 属性 </li>
 * 	<li>cssDir		- DIV, SPAN 或 UL/OL 的 dir 属性 </li>
 * 	<li>cssLang		- DIV, SPAN 或 UL/OL 的 lang 属性 </li>
 * 	<li>cssTitle	- DIV, SPAN 或 UL/OL 的 title 属性 </li>
 * 	<li>cssType		- UL/OL 的 type 属性（只对 UL/OL 有效） </li>
 * 	<li>cssCompact	- UL/OL 的 compact 属性（只对 UL/OL 有效） </li>
 * 	<li>cssStart	- OL 的 start 属性（只对 OL 有效） </li>
 * 	<li>cssAttrs	- DIV, SPAN 或 UL/OL 的自由定义属性（如：cssAttrs="class='myclass' onclick='myonclick()'"） </li>
 * </ul>
 * 
 */
public class Error extends SimpleTagSupport
{
	private String key;
	private int index = -1;
	private String element;
	private boolean escape = true;
	
	private String cssClass;
	private String cssStyle;
	private String cssAlign;
	private String cssId;
	private String cssDir;
	private String cssLang;
	private String cssTitle;
	private String cssType;
	private String cssCompact;
	private String cssStart;
	private String cssAttrs;
	
	/** 输出标签内容 */
	@Override
	public void doTag() throws JspException, IOException
	{
		PageContext context	= (PageContext)getJspContext();
		List<String> list	= new ArrayList<String>();

		fillErrorList(context, list);

		if(list.isEmpty()) return;
		
		Element e	= Element.fromString(element);
		String html	= e.write(this, list);
		
		getJspContext().getOut().print(html);
	}

	private void fillErrorList(PageContext context, List<String> list)
	{
		ActionSupport action = (ActionSupport)context.findAttribute(Action.Constant.REQ_ATTR_ACTION);
		if(action == null) return;
		
		if(GeneralHelper.isStrEmpty(key))
		{
			Map<String, List<String>> errors = action.getAllErrors();
			
			if(index == -1)
			{
				Collection<List<String>> col = errors.values();
				for(List<String> err : col)
					list.addAll(err);
			}
			else
			{
				Set<String> set = errors.keySet();
				for(String key : set)
				{
					String value = action.getError(key, index);
					if(GeneralHelper.isStrNotEmpty(value))
						list.add(value);
				}
			}
		}
		else
		{
			if(index == -1)
				list.addAll(action.getErrors(key));
			else
			{
				String value = action.getError(key, index);
				if(GeneralHelper.isStrNotEmpty(value))
					list.add(value);
			}
		}
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public void setElement(String element)
	{
		this.element = element;
	}

	public void setEscape(boolean escape)
	{
		this.escape = escape;
	}

	public void setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
	}

	public void setCssStyle(String cssStyle)
	{
		this.cssStyle = cssStyle;
	}

	public void setCssAlign(String cssAlign)
	{
		this.cssAlign = cssAlign;
	}

	public void setCssId(String cssId)
	{
		this.cssId = cssId;
	}

	public void setCssDir(String cssDir)
	{
		this.cssDir = cssDir;
	}

	public void setCssLang(String cssLang)
	{
		this.cssLang = cssLang;
	}

	public void setCssTitle(String cssTitle)
	{
		this.cssTitle = cssTitle;
	}
	
	public void setCssType(String cssType)
	{
		this.cssType = cssType;
	}
	
	public void setCssCompact(String cssCompact)
	{
		this.cssCompact = cssCompact;
	}
	
	public void setCssStart(String cssStart)
	{
		this.cssStart = cssStart;
	}

	public void setCssAttrs(String cssAttrs)
	{
		this.cssAttrs = cssAttrs;
	}
	
	enum Element
	{
		SPAN,
		
		DIV
		{
			@Override
			protected void writeAttrs(Error tag, StringBuilder sb)
			{
				if(tag.cssAlign != null) sb.append(" align=\"").append(tag.cssAlign).append("\"");
				super.writeAttrs(tag, sb);
			}
		},
		
		UL
		{
			@Override
			protected void writeAttrs(Error tag, StringBuilder sb)
			{
				if(tag.cssType		!= null)	sb.append(" type=\"")	.append(tag.cssType).append("\"");
				if(tag.cssCompact	!= null)	sb.append(" compact=\"").append(tag.cssCompact).append("\"");
				super.writeAttrs(tag, sb);
			}
			
			@Override
			protected void writeBody(Error tag, List<String> list, StringBuilder sb)
			{
				writeBodyLI(tag, list, sb);
			}
		},
		
		OL
		{
			@Override
			protected void writeAttrs(Error tag, StringBuilder sb)
			{
				if(tag.cssType		!= null)	sb.append(" type=\"")	.append(tag.cssType).append("\"");
				if(tag.cssCompact	!= null)	sb.append(" compact=\"").append(tag.cssCompact).append("\"");
				if(tag.cssStart		!= null)	sb.append(" start=\"").append(tag.cssStart).append("\"");
				super.writeAttrs(tag, sb);
			}
			
			@Override
			protected void writeBody(Error tag, List<String> list, StringBuilder sb)
			{
				writeBodyLI(tag, list, sb);
			}
		};
		
		static final Element DEFAULT = SPAN;
				
		@Override
		public String toString()
		{
			return super.toString().toLowerCase();
		}
		
		static Element fromString(String name)
		{
			if(GeneralHelper.isStrEmpty(name))
				return DEFAULT;
			
			Element e = valueOf(name.toUpperCase());
			if(e == null) e = DEFAULT;
			
			return e;
		}
		
		String write(Error tag, List<String> list)
		{
			StringBuilder sb = new StringBuilder();
			
			sb.append("<").append(this);
			writeAttrs(tag, sb);
			sb.append(">");
			writeBody(tag, list, sb);
			sb.append("</").append(this).append(">");
			
			return sb.toString();
		}
		
		protected void writeAttrs(Error tag, StringBuilder sb)
		{
			if(tag.cssClass		!= null)	sb.append(" class=\"")	.append(tag.cssClass).append("\"");
			if(tag.cssStyle		!= null)	sb.append(" style=\"")	.append(tag.cssStyle).append("\"");
			if(tag.cssId		!= null)	sb.append(" id=\"")		.append(tag.cssId).append("\"");
			if(tag.cssDir		!= null)	sb.append(" dir=\"")	.append(tag.cssDir).append("\"");
			if(tag.cssLang		!= null)	sb.append(" lang=\"")	.append(tag.cssLang).append("\"");
			if(tag.cssTitle		!= null)	sb.append(" title=\"")	.append(tag.cssTitle).append("\"");
			if(tag.cssAttrs		!= null)	sb.append(" ")			.append(tag.cssAttrs);
		}
		
		protected void writeBody(Error tag, List<String> list, StringBuilder sb)
		{
			writeStyleBodyBR(tag, list, sb);
		}
		
		protected void writeStyleBodyBR(Error tag, List<String> list, StringBuilder sb)
		{
			for(int i = 0; i < list.size(); i++)
			{
				if(i != 0) sb.append("<br/>");
				
				String msg			= list.get(i);
				if(tag.escape) msg  = GeneralHelper.escapeXML(msg);
				
				sb.append(msg);
			}
		}
		
		protected void writeBodyLI(Error tag, List<String> list, StringBuilder sb)
		{
			for(String msg : list)
			{
				if(tag.escape) msg = GeneralHelper.escapeXML(msg);					
				sb.append("<li>").append(msg).append("</li>");
			}
		}
	}
}
