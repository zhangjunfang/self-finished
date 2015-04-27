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

package org.jessma.dao.hbn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.type.Type;
import org.jessma.dao.AbstractFacade;
import org.jessma.util.KV;

/**
 * 
 * @author Kingfisher
 * 
 * 功能：封装Session的数据库访问方法，支持以下查询 / 更新操作：
 * 	1、HQL 查询 / 更新
 * 	2、QBC 查询 / 更新
 * 	3、SQL 查询 / 更新
 *
 * 异常处理：操作失败时，所有方法都抛出 DAOException
 * 
 * 使用方法：
 * 	1、用自己实现的DAO类继承于本类，能得到相应的便利方法操作 Hibernate
 * 	2、在自己实现的数据访问 Facade 类中继承或直接使用本类，能得到相应的便利方法操作 Hibernate
 * 
 */
@SuppressWarnings("unchecked")
public abstract class HibernateFacade extends AbstractFacade<HibernateSessionMgr, Session>
{
	protected HibernateFacade(HibernateSessionMgr mgr)
	{
		super(mgr);
	}
	
	/* ************************************************************************************************** */
	/* ********************************************** 业务方法 ********************************************* */
	
	/**
	 * 
	 * 根据指定的实体对象插入记录
	 * 
	 * @param o	: 要插入的对象
	 * @return	: 主键值
	 * 
	 */
	protected <T> Serializable save(T o)
	{
		return getSession().save(o);
	}

	/**
	 * 
	 * 根据指定的实体对象列表插入记录
	 * 
	 * @param objs	: 要插入的对象列表
	 * @return		: 主键值列表
	 * 
	 */
	protected <T> List<Serializable> save(List<T> objs)
	{
		List<Serializable> ids = new ArrayList<Serializable>(objs.size());
		
		for(int i = 0; i < objs.size(); i++)
			ids.set(i, save(objs.get(i)));
		
		return ids;
	}

	/**
	 * 
	 * 根据指定的实体对象更新记录
	 * 
	 * @param o	: 要更新的对象
	 * 
	 */
	protected <T> void update(T o)
	{
		getSession().update(o);
	}

	/**
	 * 
	 * 根据指定的实体对象列表更新记录
	 * 
	 * @param objs	: 要更新的对象列表
	 * 
	 */
	protected <T> void update(List<T> objs)
	{
		for(Object o : objs)
			update(o);
	}

	/**
	 * 
	 * 根据指定的实体对象列表插入或更新记录
	 * 
	 * @param o	: 要插入或更新的对象
	 * 
	 */
	protected <T> void saveOrUpdate(T o)
	{
		getSession().saveOrUpdate(o);
	}

	/**
	 * 
	 * 根据指定的实体对象列表插入或更新记录
	 * 
	 * @param objs	: 要插入或更新的对象列表
	 * 
	 */
	protected <T> void saveOrUpdate(List<T> objs)
	{
		for(Object o : objs)
			saveOrUpdate(o);
	}

	/**
	 * 
	 * 根据指定的实体对象删除记录
	 * 
	 * @param o	: 要删除的对象
	 * 
	 */
	protected <T> void delete(T o)
	{
		getSession().delete(o);
	}

	/**
	 * 
	 * 根据指定的实体对象列表删除记录
	 * 
	 * @param objs	: 要删除的对象列表
	 * 
	 */
	protected <T> void delete(List<T> objs)
	{
		for(Object o : objs)
			delete(o);
	}

	/**
	 * 
	 * 把指定的实体对象同步到数据库
	 * 
	 * @param o	: 要同步的对象
	 * @return	: 同步后的对象
	 * 
	 */
	protected <T> T merge(T o)
	{
		return (T)getSession().merge(o);
	}
	
	/**
	 * 
	 * 把指定的实体对象列表同步到数据库
	 * 
	 * @param objs	: 要同步的对象列表
	 * @return		: 同步后的对象列表
	 * 
	 */
	protected <T> List<T> merge(List<T> objs)
	{
		List<T> os = new ArrayList<T>(objs.size());
		
		for(int i = 0; i < objs.size(); i++)
			os.set(i, merge(objs.get(i)));
		
		return os;
	}
	
	/**
	 * 
	 * 根据主键获取实体对象
	 * 
	 * @param objClass	: 要加载的对象类型
	 * @param id		: 主键
	 * @return			: 加载的对象，如果找不到则返回 NULL
	 * 
	 */
	protected <T> T get(Class<T> objClass, Serializable id)
	{
		return (T)getSession().get(objClass, id);
	}
	
	/**
	 * 
	 * 根据主键列表获取实体对象列表
	 * 
	 * @param objClass	: 要加载的对象类型
	 * @param ids		: 主键列表
	 * @return			: 加载的对象列表，如果找不到则对应的列表项为 NULL
	 * 
	 */
	protected <T> List<T> get(Class<T> objClass, List<Serializable> ids)
	{
		List<T> os = new ArrayList<T>(ids.size());
	
		for(int i = 0; i < ids.size(); i++)
			os.set(i, get(objClass, ids.get(i)));
		
		return os;
	}
	
	/**
	 * 
	 * 根据主键获取实体对象
	 * 
	 * @param objClass	: 要加载的对象类型
	 * @param id		: 主键
	 * @return			: 加载的对象，如果找不到则抛出异常
	 * 
	 */
	protected <T> T load(Class<T> objClass, Serializable id)
	{
		return (T)getSession().load(objClass, id);
	}
	
	/**
	 * 
	 * 根据主键列表获取实体对象列表
	 * 
	 * @param objClass	: 要加载的对象类型
	 * @param ids		: 主键列表
	 * @return			: 加载的对象列表，如果其中一个找不到则抛出异常
	 * 
	 */
	protected <T> List<T> load(Class<T> objClass, List<Serializable> ids)
	{
		List<T> os = new ArrayList<T>(ids.size());
	
		for(int i = 0; i < ids.size(); i++)
			os.set(i, load(objClass, ids.get(i)));
		
		return os;
	}
	
	/**
	 * 
	 * 清除缓存中的实体对象
	 * 
	 * @param o	: 要清除的对象
	 * 
	 */
	protected <T> void evict(T o)
	{
		getSession().evict(o);
	}
	
	/**
	 * 
	 * 清除缓存中的实体对象
	 * 
	 * @param objs	: 要清除的对象列表
	 * 
	 */
	protected <T> void evict(List<T> objs)
	{
		for(Object o : objs)
			evict(o);
	}
	
	/** 清空 {@link Session} 缓存 */
	protected void clear()
	{
		getSession().clear();
	}
	
	/** 刷新 {@link Session} 缓存 */
	protected void flush()
	{
		getSession().flush();
	}
	
	/**
	 * 
	 * 执行HQL查询
	 * 
	 * @param hql		: HQL 查询语句
	 * @param params	: 查询参数
	 * @return		: 查询结果
	 * 
	 */
	protected <T> List<T> hqlQuery(String hql, Object ... params)
	{
		return hqlQuery(0, 0, hql, params);
	}

	/**
	 * 
	 * 根据起始记录和最大记录数执行HQL查询
	 * 
	 * @param firstResult	: 起始记录索引
	 * @param maxResults	: 最大记录数
	 * @param hql			: HQL 查询语句
	 * @param params		: 查询参数
	 * @return				: 查询结果
	 * 
	 */
	protected <T> List<T> hqlQuery(int firstResult, int maxResults, String hql, Object ... params)
	{
		Query query = getSession().createQuery(hql);
		
		for(int i = 0; i < params.length; i++)
			query.setParameter(i, params[i]);
		
		if(firstResult > 0)
			query.setFirstResult(firstResult);
		if(maxResults > 0)
			query.setMaxResults(maxResults);
		
		return query.list();
	}

	/**
	 * 
	 * 根据命名参数执行HQL查询
	 * 
	 * @param hql		: HQL 查询语句
	 * @param params	: 查询参数（命名参数：参数名称－参数值）
	 * @return			: 查询结果
	 * 
	 */
	protected <T> List<T> hqlQuery2(String hql, KV<String, Object> ... params)
	{
		return hqlQuery2(0, 0, hql, params);
	}

	/**
	 * 
	 * 根据起始记录、最大记录数和命名参数执行HQL查询
	 * 
	 * @param firstResult	: 起始记录索引
	 * @param maxResults	: 最大记录数
	 * @param hql			: HQL 查询语句
	 * @param params		: 查询参数（命名参数：参数名称－参数值）
	 * @return				: 查询结果
	 * 
	 */
	protected <T> List<T> hqlQuery2(int firstResult, int maxResults, String hql, KV<String, Object> ... params)
	{
		Query query = getSession().createQuery(hql);
		
		for(KV<String, Object> kv : params)
			query.setParameter(kv.getKey(), kv.getValue());
		
		if(firstResult > 0)
			query.setFirstResult(firstResult);
		if(maxResults > 0)
			query.setMaxResults(maxResults);
		
		return query.list();
	}

	/**
	 * 
	 * 执行HQL更新
	 * 
	 * @param hql			: HQL更新语句
	 * @param params		: 查询参数
	 * @return				: 执行结果（影响的行数）
	 * 
	 */
	protected int hqlUpdate(String hql, Object ... params)
	{
		Query query = getSession().createQuery(hql);
		
		for(int i = 0; i < params.length; i++)
			query.setParameter(i, params[i]);
		
		return query.executeUpdate();
	}
	
	/**
	 * 
	 * 根据命名参数执行HQL更新
	 * 
	 * @param hql			: HQL更新语句
	 * @param params		: 查询参数（命名参数：参数名称－参数值）
	 * @return				: 执行结果（影响的行数）
	 * 
	 */
	protected int hqlUpdate2(String hql, KV<String, Object> ... params)
	{
		Query query = getSession().createQuery(hql);
		
		for(KV<String, Object> kv : params)
			query.setParameter(kv.getKey(), kv.getValue());
		
		return query.executeUpdate();
	}

	/**
	 * 
	 * 执行命名查询
	 * 
	 * @param queryName		: 查询名称
	 * @param params		: 查询参数
	 * @return				: 查询结果
	 * 
	 */
	protected <T> List<T> namedQuery(String queryName, Object ... params)
	{
		return namedQuery(0, 0, queryName, params);
	}
	
	/**
	 * 
	 * 根据起始记录和最大记录数执行命名查询
	 * 
	 * @param firstResult	: 起始记录索引
	 * @param maxResults	: 最大记录数
	 * @param queryName		: 查询名称
	 * @param params		: 查询参数
	 * @return				: 查询结果
	 * 
	 */
	protected <T> List<T> namedQuery(int firstResult, int maxResults, String queryName, Object ... params)
	{
		Query query = getSession().getNamedQuery(queryName);
		
		for(int i = 0; i < params.length; i++)
			query.setParameter(i, params[i]);
		if(firstResult > 0)
			query.setFirstResult(firstResult);
		if(maxResults > 0)
			query.setMaxResults(maxResults);
		
		return query.list();
	}
	
	
	/**
	 * 
	 * 根据命名参数执行命名查询
	 * 
	 * @param queryName		: 查询名称
	 * @param params		: 查询参数（命名参数：参数名称－参数值）
	 * @return				: 查询结果
	 * 
	 */
	protected <T> List<T> namedQuery2(String queryName, KV<String, Object> ... params)
	{
		return namedQuery2(0, 0, queryName, params);
	}
	
	/**
	 * 
	 * 根据起始记录最大记录数和命名参数执行命名查询
	 * 
	 * @param firstResult	: 起始记录索引
	 * @param maxResults	: 最大记录数
	 * @param queryName		: 查询名称
	 * @param params		: 查询参数（命名参数：参数名称－参数值）
	 * @return				: 查询结果
	 * 
	 */
	protected <T> List<T> namedQuery2(int firstResult, int maxResults, String queryName, KV<String, Object> ... params)
	{
		Query query = getSession().getNamedQuery(queryName);
		
		for(KV<String, Object> kv : params)
			query.setParameter(kv.getKey(), kv.getValue());
		
		if(firstResult > 0)
			query.setFirstResult(firstResult);
		if(maxResults > 0)
			query.setMaxResults(maxResults);
		
		return query.list();
	}
	
	/**
	 * 
	 * 执行命名更新
	 * 
	 * @param queryName		: 查询名称
	 * @param params		: 查询参数
	 * @return				: 更新结果（影响的行数）
	 * 
	 */
	protected int namedUpdate(String queryName, Object ... params)
	{
		Query query = getSession().getNamedQuery(queryName);
		
		for(int i = 0; i < params.length; i++)
			query.setParameter(i, params[i]);
		
		return query.executeUpdate();
	}
	
	/**
	 * 
	 * 根据命名参数执行命名更新
	 * 
	 * @param queryName		: 查询名称
	 * @param params		: 查询参数（命名参数：参数名称－参数值）
	 * @return				: 更新结果（影响的行数）
	 * 
	 */
	protected int namedUpdate2(String queryName, KV<String, Object> ... params)
	{
		Query query = getSession().getNamedQuery(queryName);
		
		for(KV<String, Object> kv : params)
			query.setParameter(kv.getKey(), kv.getValue());
		
		return query.executeUpdate();
	}
	
	/**
	 * 
	 * 执行QBC查询
	 * 
	 * @param cls			: 要查询的实体类
	 * @param criterions	: 查询条件
	 * @return				: 查询结果
	 * 
	 */
	protected <T> List<T> qbcQuery(Class<?> cls, Criterion ... criterions)
	{
		return qbcQuery(0, 0, cls, criterions);
	}
	
	/**
	 * 
	 * 根据起始记录和最大记录数执行QBC查询
	 * 
	 * @param cls			: 要查询的实体类
	 * @param criterions	: 查询条件
	 * @return				: 查询结果
	 * 
	 */	
	protected <T> List<T> qbcQuery(int firstResult, int maxResults, Class<?> cls, Criterion ... criterions)
	{
		return qbcQuery(firstResult, maxResults, cls, (Order[])null, criterions);
	}
	
	/**
	 * 
	 * 执行QBC查询，并对结果排序
	 * 
	 * @param cls			: 要查询的实体类
	 * @param orders		: 排序策略
	 * @param criterions	: 查询条件
	 * @return				: 查询结果
	 * 
	 */
	protected <T> List<T> qbcQuery(Class<?> cls, Order[] orders, Criterion ... criterions)
	{
		return qbcQuery(0, 0, cls, (Order[])orders, criterions);
	}
	
	/**
	 * 
	 * 根据起始记录和最大记录数执行QBC查询，并对结果排序
	 * 
	 * @param firstResult	: 起始记录索引
	 * @param maxResults	: 最大记录数
	 * @param cls			: 要查询的实体类
	 * @param orders		: 排序策略
	 * @param criterions	: 查询条件
	 * @return				: 查询结果
	 * 
	 */
	protected <T> List<T> qbcQuery(int firstResult, int maxResults, Class<?> cls, Order[] orders, Criterion ... criterions)
	{
		Criteria criteria = getSession().createCriteria(cls);
		
		for(Criterion c : criterions)
			criteria.add(c);

		if(orders != null)
		{
			for(Order o : orders)
				criteria.addOrder(o);
		}
		
		if(firstResult > 0)
			criteria.setFirstResult(firstResult);
		if(maxResults > 0)
			criteria.setMaxResults(maxResults);
		
		return criteria.list();
	}
	
	/**
	 * 
	 * 执行SQL查询
	 * 
	 * @param sql		: SQL 查询语句
	 * @param params	: 查询参数
	 * @return			: 查询结果
	 * 
	 */
	protected <T> List<T> sqlQuery(String sql, Object ... params)
	{
		return sqlQuery(0, 0, sql, params);
	}
	
	/**
	 * 
	 * 执行SQL查询
	 * 
	 * @param sql		: SQL 查询语句
	 * @param clazz		: 要绑定的查询实体
	 * @param params	: 查询参数
	 * @return			: 查询结果
	 * 
	 */
	protected <T> List<T> sqlQuery(String sql, Class<?> clazz, Object ... params)
	{
		KV<String, Object>[] kvs	= new KV[1];
		kvs[0]	= new KV<String, Object>(null, clazz);
		
		return sqlQuery3(sql, kvs, params);
	}

	/**
	 * 
	 * 根据起始记录和最大记录数执行SQL查询
	 * 
	 * @param firstResult	: 起始记录索引
	 * @param maxResults	: 最大记录数
	 * @param sql			: SQL 查询语句
	 * @param params		: 查询参数
	 * @return				: 查询结果
	 * 
	 */
	protected <T> List<T> sqlQuery(int firstResult, int maxResults, String sql, Object ... params)
	{
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		
		for(int i = 0; i < params.length; i++)
			sqlQuery.setParameter(i, params[i]);
		
		if(firstResult > 0)
			sqlQuery.setFirstResult(firstResult);
		if(maxResults > 0)
			sqlQuery.setMaxResults(maxResults);
		
		return sqlQuery.list();
	}

	/**
	 * 
	 * 根据起始记录和最大记录数执行SQL查询
	 * 
	 * @param firstResult	: 起始记录索引
	 * @param maxResults	: 最大记录数
	 * @param sql			: SQL 查询语句
	 * @param clazz			: 要绑定的查询实体
	 * @param params		: 查询参数
	 * @return				: 查询结果
	 * 
	 */
	protected <T> List<T> sqlQuery(int firstResult, int maxResults, String sql, Class<?> clazz, Object ... params)
	{
		KV<String, Object>[] kvs	= new KV[1];
		kvs[0]	= new KV<String, Object>(null, clazz);
		
		return sqlQuery4(firstResult, maxResults, sql, kvs, (KV<String, String>[])null, params);
	}

	/**
	 * 
	 * 执行SQL查询，并以指定的Scalars格式返回查询结果
	 * 
	 * @param sql		: SQL 查询语句
	 * @param scalars	: 查询结果转换标量集
	 * @param params	: 查询参数
	 * @return			: 查询结果
	 * 
	 */
	protected <T> List<T> sqlQuery2(String sql, KV<String, Type>[] scalars, Object ... params)
	{
		return sqlQuery2(0, 0, sql, (KV<String, Type>[])scalars, params);
	}
	
	/**
	 * 
	 * 根据起始记录和最大记录数执行SQL查询，并以指定的Scalars格式返回查询结果
	 * 
	 * @param firstResult	: 起始记录索引
	 * @param maxResults	: 最大记录数
	 * @param sql			: SQL 查询语句
	 * @param scalars		: 查询结果转换标量集
	 * @param params		: 查询参数
	 * @return				: 查询结果
	 * 
	 */
	protected <T> List<T> sqlQuery2(int firstResult, int maxResults, String sql, KV<String, Type>[] scalars, Object ... params)
	{
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		
		for(int i = 0; i < params.length; i++)
			sqlQuery.setParameter(i, params[i]);
		
		if(scalars != null)
		{
			for(KV<String, Type>scalar : scalars)
			{	
				String key	= scalar.getKey();
				Type value	= scalar.getValue();
				
				if(value != null)
					sqlQuery.addScalar(key, value);
				else
					sqlQuery.addScalar(key);
			}
		}
		
		if(firstResult > 0)
			sqlQuery.setFirstResult(firstResult);
		if(maxResults > 0)
			sqlQuery.setMaxResults(maxResults);
		
		return sqlQuery.list();
	}
	
	/**
	 * 
	 * 执行SQL查询，并根据Entities返回查询结果
	 * 
	 * @param entities	: 要绑定的查询实体集合
	 * @param sql		: SQL 查询语句
	 * @param params	: 查询参数
	 * @return			: 查询结果
	 * 
	 */
	protected <T> List<T> sqlQuery3(String sql, KV<String, Object>[] entities, Object... params)
	{
		return sqlQuery3(0, 0, sql, (KV<String, Object>[])entities, params);
	}
	
	/**
	 * 
	 * 执行SQL查询，并根据Entities返回查询结果
	 * 
	 * @param firstResult	: 起始记录索引
	 * @param maxResults	: 最大记录数
	 * @param entities		: 要绑定的查询实体集合
	 * @param sql			: SQL 查询语句
	 * @param params		: 查询参数
	 * @return				: 查询结果
	 * 
	 */
	protected <T> List<T> sqlQuery3(int firstResult, int maxResults, String sql, KV<String, Object>[] entities, Object... params)
	{
		return sqlQuery4(firstResult, maxResults,sql, (KV<String, Object>[])entities, (KV<String, String>[])null, params);
	}
	
	/**
	 * 
	 * 执行SQL查询，并根据Entities和Joins返回查询结果
	 * 
	 * @param sql		: SQL 查询语句
	 * @param entities	: 要绑定的查询实体集合
	 * @param joins		: 要绑定的连接实体集合
	 * @param params	: 查询参数
	 * @return			: 查询结果
	 * 
	 */
	protected <T> List<T> sqlQuery4(String sql, KV<String, Object>[] entities, KV<String, String>[] joins, Object... params)
	{
		return sqlQuery4(0, 0, sql, (KV<String, Object>[])entities, (KV<String, String>[])joins, params);
	}
	
	/**
	 * 
	 * 根据起始记录和最大记录数执行SQL查询，并根据Entities和Joins返回查询结果
	 * 
	 * @param firstResult	: 起始记录索引
	 * @param maxResults	: 最大记录数
	 * @param sql			: SQL 查询语句
	 * @param entities		: 要绑定的查询实体集合
	 * @param joins			: 要绑定的连接实体集合
	 * @param params		: 查询参数
	 * @return				: 查询结果
	 * 
	 */
	protected <T> List<T> sqlQuery4(int firstResult, int maxResults, String sql, KV<String, Object>[] entities, KV<String, String>[] joins, Object... params)
	{
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		
		for(int i = 0; i < params.length; i++)
			sqlQuery.setParameter(i, params[i]);
		
		if(entities != null)
		{
			for(int i = 0; i < entities.length; i++)
			{
				KV<String, Object> entity = entities[i];
				
				String key		= entity.getKey();
				Object value	= entity.getValue();
				Class<?> v1		= (value instanceof Class) ? (Class<?>)value : null;
				String v2		= v1 == null ? (String)value : null;
				
				if(key == null || key.length() == 0)
				{
					if(v1 != null)
						sqlQuery.addEntity(v1);
					else
						sqlQuery.addEntity(v2);
				}
				else
				{
					if(v1 != null)
						sqlQuery.addEntity(key, v1);
					else
						sqlQuery.addEntity(key, v2);
				}
			}
		}
		
		if(joins != null)
		{
			for(KV<String, String> join : joins)
				sqlQuery.addJoin(join.getKey(), (String)join.getValue());
		}
		
		if(firstResult > 0)
			sqlQuery.setFirstResult(firstResult);
		if(maxResults > 0)
			sqlQuery.setMaxResults(maxResults);
		
		return sqlQuery.list();
	}
	
	/**
	 * 
	 * 执行SQL更新
	 * 
	 * @param sql		: SQL 更新语句
	 * @param params	: 查询参数
	 * @return			: 更新结果
	 * 
	 */
	protected int sqlUpdate(String sql, Object ... params)
	{
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		
		for(int i = 0; i < params.length; i++)
			sqlQuery.setParameter(i, params[i]);
		
		return sqlQuery.executeUpdate();
	}
}
