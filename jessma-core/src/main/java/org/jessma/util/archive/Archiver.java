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

import org.apache.tools.ant.types.FileSet;

/** 压缩执行器基础类 */
abstract public class Archiver extends TaskExecutor
{
	/** 获取压缩文件的后缀名 */
	abstract public String getSuffix();
	
	public Archiver(String source)
	{
		super(source);
	}
	
	public Archiver(String source, String target)
	{
		super(source, target);
	}

	/** 获取输出文件的 {@link File} 对象 */
	protected File getTargetFile()
	{
		if(target != null)
			return new File(target);
		
		return new File(source + getSuffix());
	}
	
	/** 填充 {@link FileSet} 对象属性 */
	protected void fillFileSetAttributes(FileSet fs, File f)
	{
		if(f.isDirectory())
			fs.setDir(f);
		else
			fs.setFile(f);
		
		if(includes != null)
			fs.setIncludes(includes);
		if(excludes != null)
			fs.setExcludes(excludes);
	}

}
