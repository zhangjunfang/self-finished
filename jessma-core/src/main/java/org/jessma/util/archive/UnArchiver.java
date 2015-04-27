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

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.types.PatternSet;

/** 解压执行器基础类 */
abstract public class UnArchiver extends TaskExecutor
{
	boolean overwrite = true;
	
	public UnArchiver(String source)
	{
		super(source);
	}
	
	public UnArchiver(String source, String target)
	{
		super(source, target);
	}
	
	/** 获取执行解压的 {@link Expand} 对象 */
	abstract protected Expand getExpand();

	/** 获取覆盖输出文件标识 */
	public boolean isOverwrite()
	{
		return overwrite;
	}

	/** 设置覆盖输出文件标识 */
	public void setOverwrite(boolean overWrite)
	{
		this.overwrite = overWrite;
	}
	
	/** 获取输入文件的 {@link File} 对象 */
	protected File getSourceFile()
	{
		return new File(source);
	}

	/** 获取输出文件夹的 {@link File} 对象 */
	protected File getTargetDir()
	{
		if(target != null)
			return new File(target);
		
		return getSourceFile().getParentFile();
	}
	
	/** 获取过滤输出文件的 {@link PatternSet} 对象 */
	protected PatternSet getPatternSet()
	{
		PatternSet ps = null;
		
		if(includes != null || excludes != null)
		{
			ps = new PatternSet();
			if(includes != null)
				ps.setIncludes(includes);
			if(excludes != null)
				ps.setExcludes(excludes);
		}
		
		return ps;
	}
	
	/** 获取解压任务对象 */
	@Override
	protected Task getTask()
	{
		Project project	= new Project();
		Expand expand	= getExpand();
		PatternSet ps	= getPatternSet();

		expand.setProject(project);
		expand.setSrc(getSourceFile());
		expand.setDest(getTargetDir());
		expand.setOverwrite(isOverwrite());
		
		if(ps != null)
		{
			ps.setProject(project);
			expand.addPatternset(ps);
		}

		return expand;
	}
}
