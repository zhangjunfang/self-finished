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

package org.jessma.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.jessma.dao.AbstractFacade;

/**
 * 
 * MyBatis Facade 实现类
 *
 */
public abstract class MyBatisFacade extends AbstractFacade<MyBatisSessionMgr, SqlSession>
{
	protected MyBatisFacade(MyBatisSessionMgr mgr)
	{
		super(mgr);
	}

	/* ************************************************************************************************** */
	/* ******************************************* 业务方法 ******************************************* */

	/** 查找单个对象 */
	protected Object selectOne(String statement)
	{
		return getSession().selectOne(statement);
	}

	/** 查找单个对象 */
	protected Object selectOne(String statement, Object parameter)
	{
		return getSession().selectOne(statement, parameter);
	}

	/** 查找多个对象集合 */
	protected Map<?, ?> selectMap(String statement, String mapKey)
	{
		return getSession().selectMap(statement, mapKey);
	}

	/** 查找多个对象集合 */
	protected Map<?, ?> selectMap(String statement, Object parameter, String mapKey)
	{
		return getSession().selectMap(statement, parameter, mapKey);
	}

	/** 查找多个对象集合 */
	protected Map<?, ?> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds)
	{
		return getSession().selectMap(statement, parameter, mapKey, rowBounds);
	}

	/** 查找对象集合 */
	protected List<?> selectList(String statement)
	{
		return getSession().selectList(statement);
	}

	/** 查找对象集合 */
	protected List<?> selectList(String statement, Object parameter)
	{
		return getSession().selectList(statement, parameter);
	}

	/** 查找对象集合 */
	protected List<?> selectList(String statement, Object parameter, RowBounds rowBounds)
	{
		return getSession().selectList(statement, parameter, rowBounds);
	}

	/** 执行查询 */
	protected void select(String statement, ResultHandler handler)
	{
		getSession().select(statement, handler);
	}

	/** 执行查询 */
	protected void select(String statement, Object parameter, ResultHandler handler)
	{
		getSession().select(statement, parameter, handler);
	}

	/** 执行查询 */
	protected void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler)
	{
		getSession().select(statement, parameter, rowBounds, handler);
	}

	/** 执行插入 */
	protected int insert(String statement)
	{
		return getSession().insert(statement);
	}

	/** 执行插入 */
	protected int insert(String statement, Object parameter)
	{
		return getSession().insert(statement, parameter);
	}

	/** 执行更新 */
	protected int update(String statement)
	{
		return getSession().update(statement);
	}

	/** 执行更新 */
	protected int update(String statement, Object parameter)
	{
		return getSession().update(statement, parameter);
	}

	/** 执行删除 */
	protected int delete(String statement)
	{
		return getSession().delete(statement);
	}

	/** 执行删除 */
	protected int delete(String statement, Object parameter)
	{
		return getSession().delete(statement, parameter);
	}

	/** 获取 type 类型的 Mapper */
	protected <T> T getMapper(Class<T> type)
	{
		return getSession().getMapper(type);
	}

	/** 清空缓存 */
	protected void clearCache()
	{
		getSession().clearCache();
	}

	/** 把当前 {@link SqlSession} 的 {@link ExecutorType} 设置为默认值 */
	protected void changeSessionExecutorTypeToDefault()
	{
		getManager().changeSessionExecutorType(null);
	}

	/** 把当前 {@link SqlSession} 的 {@link ExecutorType} 设置为 {@link ExecutorType#SIMPLE} */
	protected void changeSessionExecutorTypeToSimple()
	{
		getManager().changeSessionExecutorType(ExecutorType.SIMPLE);
	}

	/** 把当前 {@link SqlSession} 的 {@link ExecutorType} 设置为 {@link ExecutorType#REUSE} */
	protected void changeSessionExecutorTypeToReuse()
	{
		getManager().changeSessionExecutorType(ExecutorType.REUSE);
	}

	/** 把当前 {@link SqlSession} 的 {@link ExecutorType} 设置为 {@link ExecutorType#BATCH} */
	protected void changeSessionExecutorTypeToBatch()
	{
		getManager().changeSessionExecutorType(ExecutorType.BATCH);
	}
}
