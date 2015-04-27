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
import org.apache.tools.ant.taskdefs.Tar;
import org.apache.tools.ant.taskdefs.Tar.TarCompressionMethod;
import org.apache.tools.ant.taskdefs.Tar.TarFileSet;

/** TAR 归档执行器类 */
public class Tarrer extends Archiver
{
	/** TAR 文件后缀名 */
	public static final String SUFFIX = ".tar";
	/** TAR 压缩模式 */
	protected static final String[] COMPRESS_MODES = {"none", "gzip", "bzip2"};

	public Tarrer(String source)
	{
		super(source);
	}
	
	public Tarrer(String source, String target)
	{
		super(source, target);
	}
	
	private TarCompressionMethod getCompressionMethod()
	{
		TarCompressionMethod method = new TarCompressionMethod();
		method.setValue(getCompressionMode());
		
		return method;
	}
	
	/** 获取 TAR 文件压缩模式 */
	protected String getCompressionMode()
	{
		return COMPRESS_MODES[0];
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
		Tar tar			= new Tar();
		
		tar.setProject(project);
		createSourceFileSet(tar);

		tar.setDestFile(getTargetFile());
		tar.setCompression(getCompressionMethod());
		
		return tar;
	}

	private TarFileSet createSourceFileSet(Tar tar)
	{
		File f			= new File(source);
		TarFileSet fs	= tar.createTarFileSet();
		
		fillFileSetAttributes(fs, f);
		
		return fs;
	}
}
