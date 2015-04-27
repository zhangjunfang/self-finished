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

import java.io.IOException;
import java.io.Reader;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.jessma.dao.AbstractSessionMgr;
import org.jessma.dao.SessionMgr;
import org.jessma.dao.TransIsoLevel;
import org.jessma.util.BeanHelper;
import org.jessma.util.GeneralHelper;
import org.jessma.util.PackageHelper;
import org.jessma.util.PackageHelper.ClassFilter;


/**
 * 
 * MyBatis Session 管理器
 *
 */
public class MyBatisSessionMgr extends AbstractSessionMgr<SqlSession>
{
	/** 默认 MyBatis 配置文件 */
	public static final String DEFAULT_CONFIG_FILE = "mybatis.cfg.xml";
	public static final String DEFAULT_ENVIRONMENT = null;
	/** 当前 MyBatis 配置环境 */
	private String environment	= DEFAULT_ENVIRONMENT;
	/** Sql Session Factory 工厂对象 */
	private SqlSessionFactory sessionFactory;
	/** SQL Mapper 接口包名正则表达式 */
	private String pattern;
	/** Session 对象的 auto commit 属性  */
	private final ThreadLocal<Boolean> localAutoCommit			= new ThreadLocal<Boolean>();
	/** Session 对象的 executor type 属性  */
	private final ThreadLocal<ExecutorType> localExecutorType	= new ThreadLocal<ExecutorType>();
	
	/** 
	 * 初始化 
	 * 
	 * @param args <br>	[0]	: mybatis_cfg_file （默认：{@link MyBatisSessionMgr#DEFAULT_CONFIG_FILE}）<br>
	 * 					[1]	: environment （默认：{@link MyBatisSessionMgr#DEFAULT_ENVIRONMENT}）<br>
	 * 					[2]	: pattern （SQL Mapper 接口包名正则表达式，用于自动扫描 SQL Mapper 接口）
	 * @throws InvalidParameterException
	 * @throws SqlSessionException
	 * 
	*/
	@Override
	public void initialize(String ... args)
	{
		if(args.length == 0)
			initialize();
		else if(args.length == 1)
			initialize(args[0]);
		else if(args.length == 2)
			initialize(args[0], args[1]);
		else if(args.length == 3)
			initialize(args[0], args[1], args[2]);
		else
			throw new InvalidParameterException("MyBatisSessionMgr initialize fail (invalid paramers)");
	}
	
	/**
	 * 
	 * 使用默认的 MyBatis 配置文件及其默认配置环境配置 Session Factory
	 * 
	 * @throws SqlSessionException	: 配置失败时抛出该异常
	 * 
	 */
	public void initialize()
	{
		initialize(DEFAULT_CONFIG_FILE);
	}

	/**
	 * 
	 * 使用特定的 MyBatis 配置文件及其默认配置环配置 Session Factory
	 * 
	 * @param mybatis_cfg_file		: 配置文件路径 
	 * @throws SqlSessionException	: 配置失败时抛出该异常
	 * 
	 */
	public void initialize(String mybatis_cfg_file)
	{
		initialize(mybatis_cfg_file, DEFAULT_ENVIRONMENT);
	}
	
	/**
	 * 
	 * 使用特定的 MyBatis 配置文件和特定配置环境配置 Session Factory
	 * 
	 * @param mybatis_cfg_file		: 配置文件路径 
	 * @param env					: 配置环境名称 
	 * @throws SqlSessionException	: 配置失败时抛出该异常
	 * 
	 */
	public void initialize(String mybatis_cfg_file, String env)
	{
		initialize(mybatis_cfg_file, env, null);
	}
	
	/**
	 * 
	 * 使用特定的 MyBatis 配置文件和特定配置环境配置 Session Factory，并自动扫描特定包下的 SQL Mapper 接口
	 * 
	 * @param mybatis_cfg_file		: 配置文件路径 
	 * @param env					: 配置环境名称 
	 * @throws SqlSessionException	: 配置失败时抛出该异常
	 * 
	 */
	public void initialize(String mybatis_cfg_file, String env, String packages)
	{
		configFile	= GeneralHelper.isStrNotEmpty(mybatis_cfg_file) ? mybatis_cfg_file : DEFAULT_CONFIG_FILE;
		environment	= GeneralHelper.isStrNotEmpty(env) ? env : DEFAULT_ENVIRONMENT;
		pattern		= packages;
		
		try
		{
			loadDefalutTransIsoLevel();
		}
		catch(SqlSessionException e)
		{
			unInitialize();
			throw e;
		}
	}
	
	/**
	 * 
	 * 关闭 Session Factory
	 * 
	 */
	@Override
	public void unInitialize()
	{
		sessionFactory = null;
		super.unInitialize();
	}

	/** 参考：{@link AbstractSessionMgr#loadDefalutTransIsoLevel()} */
	@Override
	protected void loadDefalutTransIsoLevel()
	{
		try
		{
			SqlSession session		= getSession();
			Connection conn			= session.getConnection();
			int level				= conn.getTransactionIsolation();
			defaultTransIsoLevel	= TransIsoLevel.fromInt(level);
		}
		catch(SQLException e)
		{
			throw new SqlSessionException(e);
		}
		finally
		{
			closeSession();
		}
	}
	
	/** 参考：{@link SessionMgr#setSessionTransIsoLevel(TransIsoLevel)} */
	@Override
	public void setSessionTransIsoLevel(TransIsoLevel level)
	{
		try
		{
			SqlSession session		= getSession();
			Connection conn			= session.getConnection();
			
			conn.setTransactionIsolation(level.toInt());
		}
		catch(SQLException e)
		{
			throw new SqlSessionException(e);
		}
	}
	
	/**
	 * 
	 * 获取绑定到当前线程的 {@link SqlSession} 实例。
	 * 如果该实例已存在则返回当前实例，否则创建一个自动提交的 {@link SqlSession}。
	 * 
	 * @throws SqlSessionException	: 获取 Session 实例失败时抛出该异常
	 * 
	 */
	@Override
	public final SqlSession getSession()
	{
		return getSession(true);
	}
	
	/**
	 * 
	 * 获取绑定到当前线程的 {@link SqlSession} 实例。
	 * 如果该实例已存在则返回当前实例，否则创建一个 autoCommit 的 {@link SqlSession}。
	 * 
	 * @throws SqlSessionException	: 获取 Session 实例失败时抛出该异常
	 * 
	 */
	public final SqlSession getSession(boolean autoCommit) 
	{
		return getSession(null, autoCommit);
	}
	
	/**
	 * 
	 * 获取绑定到当前线程的 {@link SqlSession} 实例。
	 * 如果该实例已存在则返回当前实例，否则创建一个 type / autoCommit 类型 的 {@link SqlSession}。
	 * 
	 * @throws SqlSessionException	: 获取 Session 实例失败时抛出该异常
	 * 
	 */
	public final SqlSession getSession(ExecutorType type, boolean autoCommit) 
	{
		SqlSession session = localSession.get();

		if(session == null)
		{
			if(sessionFactory == null)
				buildSessionFactory();
			
			if(type == null)
			{
				type = localExecutorType.get();
				
    			if(type == null)
    				type = getDefaultExecutorType();
			}
			
			session = sessionFactory.openSession(type, autoCommit);
			
			localSession.set(session);
			localAutoCommit.set(autoCommit);
			localExecutorType.set(type);
		}

		return session;
	}
	
	/** 创建 {@link SqlSessionFactory} */
	private void buildSessionFactory()
	{
		synchronized(this)
		{
			if(sessionFactory == null)
			{
				try
				{
					Reader reader	= Resources.getResourceAsReader(configFile);
					sessionFactory	= new SqlSessionFactoryBuilder().build(reader, environment);
					
					if(GeneralHelper.isStrNotEmpty(pattern))
					{
						Set<String> packages = PackageHelper.getPackages(pattern);
					
						for(String pkg : packages)
						{
							Set<Class<?>> entities = PackageHelper.getClasses(pkg, false, new ClassFilter()
							{				
								@Override
								public boolean accept(Class<?> clazz)
								{
									if(!BeanHelper.isPublicInterface(clazz))
										return false;
									
									return true;
								}
							});
							
							Configuration cfg = sessionFactory.getConfiguration();
							
							for(Class<?> clazz : entities)
							{
								if(!cfg.hasMapper(clazz))
									cfg.addMapper(clazz);
							}
						}
					}
				}
				catch(IOException e)
				{
					throw new SqlSessionException(e);
				}
			}
		}
	}

	/** 把当前 {@link SqlSession} 的 {@link ExecutorType} 设置为 {@literal type} */
	public void changeSessionExecutorType(ExecutorType type)
	{
		SqlSession session = localSession.get();
		
		if(session == null)
			localExecutorType.set(type);
		else
		{
			if(type == null)
				type = getDefaultExecutorType();
			
			ExecutorType currentType = localExecutorType.get();
			
			if(type != currentType)
			{
				SqlSession newSession = sessionFactory.openSession(type, session.getConnection());
				
				session.clearCache();
				localSession.set(newSession);
				localExecutorType.set(type);
			}
		}
	}

	/**
	 * 
	 * 关闭当前线程的 {@link SqlSession} 实例
	 * 
	 */
	@Override
	public final void closeSession()
	{
		SqlSession session = localSession.get();
		
		localSession.set(null);
		localAutoCommit.set(null);
		localExecutorType.set(null);

		if(session != null)
		{
			session.close();
		}
	}
		
	/**
	 * 
	 * 开始新事务。
	 * 
	 */
	@Override
	public final void beginTransaction()
	{
		getSession(false);
	}
	
	/**
	 * 
	 * 提交事务
	 * 
	 */
	@Override
	public final void commit()
	{
		commit(true);
	}
	
	/**
	 * 
	 * 提交事务
	 * 
	 */
	public final void commit(boolean force)
	{
		SqlSession session = localSession.get();
		
		if(session != null)
			session.commit(force);
	}
	
	/**
	 * 
	 * 回滚事务
	 * 
	 */
	@Override
	public final void rollback()
	{
		rollback(true);
	}
	
	/**
	 * 
	 * 回滚事务
	 * 
	 */
	public final void rollback(boolean force)
	{
		SqlSession session = localSession.get();
		
		if(session != null)
			session.rollback(force);
	}
	
	/**
	 * 
	 * 获取当前的 {@link SqlSessionFactory} 实例
	 * 
	 */
	public final SqlSessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	/**
	 * 
	 * 获取 {@link SqlSessionFactory} 的 {@link Configuration}
	 * 
	 */
	public final Configuration getConfiguration()
	{
		return sessionFactory.getConfiguration();
	}

	/**
	 * 
	 * 获取 {@link SqlSessionFactory} 的默认 {@link ExecutorType}
	 * 
	 */
	public final ExecutorType getDefaultExecutorType()
	{
		return getConfiguration().getDefaultExecutorType();
	}

	/**
	 * 
	 * 获取当前 {@link SqlSession} 的 {@link Connection}
	 * 
	 */
	public final Connection getConnection()
	{
		Connection conn = null;
		SqlSession session = localSession.get();
		
		if(session != null)
			conn = session.getConnection();
		
		return conn;
	}

}
