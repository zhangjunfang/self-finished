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

package org.jessma.util.archive;

import org.apache.tools.ant.taskdefs.Expand;
import org.jessma.util.GeneralHelper;


/** ZIP 解压执行器类 */
public class UnZipper extends UnArchiver
{
	/** 默认文件名编码 */
	public static final String DEFAULT_ENCODING = GeneralHelper.IS_WINDOWS_PLATFORM ? "GBK" : GeneralHelper.DEFAULT_ENCODING;

	private String encoding = DEFAULT_ENCODING;

	public UnZipper(String source)
	{
		super(source);
	}
	
	public UnZipper(String source, String target)
	{
		super(source, target);
	}
	
	/** 获取文件名编码 */
	public String getEncoding()
	{
		return encoding;
	}

	/** 设置文件名编码 */
	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}
	
	/** 获取解压任务对象 */
	@Override
	protected Expand getExpand()
	{
		Expand expand = new Expand();
		
		if(encoding != null)
			expand.setEncoding(encoding);
		
		return expand;
	}

}
