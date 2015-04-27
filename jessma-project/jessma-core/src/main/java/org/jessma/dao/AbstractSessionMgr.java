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
 * SessionMgr 抽象基类，实现 {@link SessionMgr} 接口
 *
 * @param <S>	: 数据库连接类型
 */
public abstract class AbstractSessionMgr<S> implements SessionMgr<S>
{
	/** 配置文件 */
	protected String configFile;
	/** 默认事务隔离级别 */
	protected TransIsoLevel defaultTransIsoLevel;
	/** 线程局部 Session 对象 */
	protected final ThreadLocal<S> localSession = new ThreadLocal<S>();
	/** SessionMgr 线程局部执行状态 */
	private ThreadLocal<Boolean> invoking = new ThreadLocal<Boolean>();
	
	/** 加载 {@link SessionMgr} 的默认事务隔离级别 */
	abstract protected void loadDefalutTransIsoLevel();
	
	/** 参考：{@link SessionMgr#unInitialize()} */
	@Override
	public void unInitialize()
	{
		localSession.remove();
		invoking.remove();
	}
	
	/** 参考：{@link SessionMgr#getDefalutTransIsoLevel()} */
	@Override
	public TransIsoLevel getDefalutTransIsoLevel()
	{
		return defaultTransIsoLevel;
	}

	/** 参考：{@link SessionMgr#currentSession()} */
	@Override
	public S currentSession()
	{
		return localSession.get();
	}
	
	/** 参考：{@link SessionMgr#getConfigFile()} */
	@Override
	public String getConfigFile()
	{
		return configFile;
	}
	
	/** 参考：{@link SessionMgr#isInvoking()} */
	@Override
	public boolean isInvoking()
	{
		return Boolean.TRUE == invoking.get();
	}
	
	/** 参考：{@link SessionMgr#setInvoking(boolean)} */
	@Override
	public void setInvoking(boolean value)
	{
		invoking.set(Boolean.valueOf(value));
	}
}
