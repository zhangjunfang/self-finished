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
 * Session 管理器
 *
 * @param <S>	: 数据库连接类型
 */
public interface SessionMgr<S>
{
	/**
	 * 
	 * 初始化
	 * 
	 * @param args	: 自定义初始参数
	*/
	void initialize(String ... args);
	/** 注销 */
	void unInitialize();
	/** 开始事务 */
	void beginTransaction();
	/** 提交事务 */
	void commit();
	/** 回滚事务 */
	void rollback();
	/** 关闭数据库连接对象 */
	void closeSession();
	/** 获取数据库连接对象，如果不存在则创建并返回新对象 */
	S getSession();
	/** 获取数据库连接对象，如果不存在则返回 null */
	S currentSession();
	/** 获取当前配置文件 */
	String getConfigFile();
	/** 获取 {@link SessionMgr} 的默认事务隔离级别 */
	TransIsoLevel getDefalutTransIsoLevel();
	/** 设置当前线程相关的数据库连接对象的事务级别 */
	void setSessionTransIsoLevel(final TransIsoLevel level);
	/** 检查 {@link SessionMgr} 是否正在执行 */
	boolean isInvoking();
	/** 设置 {@link SessionMgr} 的执行状态 */
	void setInvoking(boolean value);
}
