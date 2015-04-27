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
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.jessma.util.GeneralHelper;


/** ZIP 压缩执行器类 */
public class Zipper extends Archiver
{
	/** ZIP 文件后缀名 */
	public static final String SUFFIX = ".zip";
	/** 默认文件名编码 */
	public static final String DEFAULT_ENCODING = GeneralHelper.IS_WINDOWS_PLATFORM ? "GBK" : GeneralHelper.DEFAULT_ENCODING;

	private String comment;
	private String encoding = DEFAULT_ENCODING;

	
	public Zipper(String source)
	{
		super(source);
	}
	
	public Zipper(String source, String target)
	{
		super(source, target);
	}
	
	/** 获取压缩文件描述 */
	public String getComment()
	{
		return comment;
	}

	/** 设置压缩文件描述 */
	public void setComment(String comment)
	{
		this.comment = comment;
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

	/** 获取压缩文件后缀名 */
	@Override
	public String getSuffix()
	{
		return SUFFIX;
	}
	
	/** 获取压缩任务对象 */
	@Override
	protected Task getTask()
	{
		Project project	= new Project();
		Zip zip			= new Zip();
		FileSet src		= getSourceFileSet();
		
		src.setProject(project);
		zip.setProject(project);
		zip.setDestFile(getTargetFile());
		zip.addFileset(src);
		
		if(encoding != null)
			zip.setEncoding(encoding);
		if(comment != null)
			zip.setComment(comment);
		
		return zip;
	}

	private FileSet getSourceFileSet()
	{
		File f		= new File(source);
		FileSet fs	= new FileSet();
			
		fillFileSetAttributes(fs, f);
		
		return fs;
	}

}
