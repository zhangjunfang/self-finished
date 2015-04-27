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

package org.jessma.mvc;

/** 异步任务启动器接口，用于自定义启动异步任务所使用的线程 */
public interface AsyncTaskLauncher
{
	/** 异步任务启动方法<br>
	 * 
	 * 实现类需在本方法中用独立线程或线程池来启动任务运行者（taskRunner）
	 * 
	 * @param taskRunner	: 待启动的任务运行者，它内部会调用 {@linkplain AsyncTask#run()} 来运行异步任务
	 */
	void start(Runnable taskRunner);
}
