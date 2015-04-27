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

import org.apache.tools.ant.Task;

/** 压缩 / 解压执行器抽象类 */
abstract public class TaskExecutor
{	
	protected String source;
	protected String target;
	protected String includes;
	protected String excludes;
	
	private Exception cause;
	
	/** 获取任务对象 */
	abstract protected Task getTask();
	
	/** 执行压缩或解压任务
	 * 
	 * @return	成功返回 true，失败返回 false，如果失败可通过 {@link TaskExecutor#getCause()} 获取失败原因
	 * 
	 */
	public boolean execute()
	{
		cause = null;
		
		try
		{
			Task task = getTask();
			task.execute();
		}
		catch(Exception e)
		{
			cause = e;
			return false;
		}
		
		return true;
	}
	
	/**
	 * 构造函数
	 * 
	 * @param source	: 输入文件或文件夹。（输出文件或文件夹通过默认规则生成）
	 * */
	public TaskExecutor(String source)
	{
		this(source, null);
	}
	
	/**
	 * 构造函数
	 * 
	 * @param source	: 输入文件或文件夹
	 * @param target	: 输出文件或文件夹，如果 target 参数为 null，则通过默认规则生成输出文件或文件夹
	 * */
	public TaskExecutor(String source, String target)
	{
		this.source	= source;
		this.target	= target;
	}
	
	/** 获取输入文件或文件夹 */
	public String getSource()
	{
		return source;
	}

	/** 设置输入文件或文件夹 */
	public void setSource(String source)
	{
		this.source = source;
	}

	/** 获取输出文件或文件夹 */
	public String getTarget()
	{
		return target;
	}

	/** 设置输出文件或文件夹 */
	public void setTarget(String target)
	{
		this.target = target;
	}

	/** 获取包含文件的规则表达式 */
	public String getIncludes()
	{
		return includes;
	}

	/** 设置包含文件的规则表达式 */
	public void setIncludes(String includes)
	{
		this.includes = includes;
	}

	/** 获取排除文件的规则表达式 */
	public String getExcludes()
	{
		return excludes;
	}

	/** 设置排除文件的规则表达式 */
	public void setExcludes(String excludes)
	{
		this.excludes = excludes;
	}

	/** 获取压缩或解压任务的失败原因 */
	public Exception getCause()
	{
		return cause;
	}
}
