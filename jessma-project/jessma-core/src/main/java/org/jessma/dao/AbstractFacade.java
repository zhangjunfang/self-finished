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

package org.jessma.dao;

/**
 * 
 * Facade 抽象基类
 *
 * @param <M>	: {@link SessionMgr}
 * @param <S>	: 数据库连接类型
 */
public abstract class AbstractFacade<M extends SessionMgr<S>, S>
{
	private M manager;
	
	protected AbstractFacade(M mgr)
	{
		manager = mgr;
	}
	
	/** 获取关联的 {@link SessionMgr} */
	public M getManager()
	{
		return manager;
	}
	
	/** 获取数据库连接 */
	protected S getSession()
	{
		return manager.getSession();
	}
}
