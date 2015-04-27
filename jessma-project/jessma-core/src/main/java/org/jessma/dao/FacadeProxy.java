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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

import org.jessma.util.BeanHelper;
import org.jessma.util.CoupleKey;
import org.jessma.util.GeneralHelper;

/**
 * 
 * Facade 代理类
 *
 */
public class FacadeProxy
{
	/**
	 * 
	 * 获取 daoClass 的自动提交事务代理对象。
	 * 其中，{@link SessionMgr} 由 daoClass 的默认构造函数提供。
	 * 
	 * @param daoClass	: 被代理的数据库访问类对象
	 * @return			     与 daoClass 有相同接口的代理对象
	 * 
	 */
	public static final <F extends AbstractFacade<M, S>, M extends SessionMgr<S>, S> F getAutoCommitProxy(Class<F> daoClass)
	{
		return getProxy(daoClass, true);
	}
	
	/**
	 * 
	 * 获取 daoClass 的自动提交事务代理对象。
	 * 其中，{@link SessionMgr} 由 mgr 指定。
	 * 
	 * @param daoClass	: 被代理的数据库访问类对象
	 * @param mgr		: {@link SessionMgr} 
	 * @return			     与 daoClass 有相同接口的代理对象
	 * 
	 */
	public static final <F extends AbstractFacade<M, S>, M extends SessionMgr<S>, S> F getAutoCommitProxy(Class<F> daoClass, M mgr)
	{
		return getProxy(daoClass, mgr, true);
	}
	
	/**
	 * 
	 * 获取 daoClass 的手动提交事务代理对象。
	 * 其中，{@link SessionMgr} 由 daoClass 的默认构造函数提供。
	 * 
	 * @param daoClass	: 被代理的数据库访问类对象
	 * @return			     与 daoClass 有相同接口的代理对象
	 * 
	 */
	public static final <F extends AbstractFacade<M, S>, M extends SessionMgr<S>, S> F getManualCommitProxy(Class<F> daoClass)
	{
		return getProxy(daoClass, false);
	}
	
	/**
	 * 
	 * 获取 daoClass 的手动提交事务代理对象。
	 * 其中，{@link SessionMgr} 由 mgr 指定。
	 * 
	 * @param daoClass	: 被代理的数据库访问类对象
	 * @param mgr		: {@link SessionMgr} 
	 * @return			     与 daoClass 有相同接口的代理对象
	 * 
	 */
	public static final <F extends AbstractFacade<M, S>, M extends SessionMgr<S>, S> F getManualCommitProxy(Class<F> daoClass, M mgr)
	{
		return getProxy(daoClass, mgr, false);
	}

	/**
	 * 
	 * 获取 daoClass 的手动提交事务代理对象。
	 * 其中，{@link SessionMgr} 由 mgr 指定。
	 * 
	 * @param daoClass	: 被代理的数据库访问类对象
	 * @param level		: {@link TransIsoLevel}
	 * @return			     与 daoClass 有相同接口的代理对象
	 * 
	 */
	public static final <F extends AbstractFacade<M, S>, M extends SessionMgr<S>, S> F getManualCommitProxy(Class<F> daoClass, TransIsoLevel level)
	{
		return getProxy(daoClass, null, false, level);
	}

	/**
	 * 
	 * 获取 daoClass 的手动提交事务代理对象。
	 * 其中，{@link SessionMgr} 由 mgr 指定。
	 * 
	 * @param daoClass	: 被代理的数据库访问类对象
	 * @param mgr		: {@link SessionMgr} 
	 * @param level		: {@link TransIsoLevel}
	 * @return			     与 daoClass 有相同接口的代理对象
	 * 
	 */
	public static final <F extends AbstractFacade<M, S>, M extends SessionMgr<S>, S> F getManualCommitProxy(Class<F> daoClass, M mgr, TransIsoLevel level)
	{
		return getProxy(daoClass, mgr, false, level);
	}

	/**
	 * 
	 * 获取 daoClass 的代理对象。
	 * 其中，{@link SessionMgr} 由 daoClass 的默认构造函数提供，是否自动提交事务 transactional 指定。
	 * 
	 * @param daoClass		: 被代理的数据库访问类对象
	 * @param autoCommit	: 是否自动提交事务（为了提高执行效率，对于只执行查询的访问通常采用自动提交事务）
	 * @return				     与 daoClass 有相同接口的代理对象
	 * 
	 */
	public static final <F extends AbstractFacade<M, S>, M extends SessionMgr<S>, S> F getProxy(Class<F> daoClass, boolean autoCommit)
	{
		return getProxy(daoClass, null, autoCommit);
	}
	
	/**
	 * 
	 * 获取 daoClass 的代理对象。
	 * 其中，{@link SessionMgr} 由 mgr 指定，是否自动提交事务由 autoCommit 参数指定。
	 * 
	 * @param daoClass		: 被代理的数据库访问类对象
	 * @param mgr			: {@link SessionMgr} 
	 * @param autoCommit	: 是否自动提交事务（为了提高执行效率，对于只执行查询的访问通常采用自动提交事务）
	 * @return				     与 daoClass 有相同接口的代理对象
	 * 
	 */
	public static final <F extends AbstractFacade<M, S>, M extends SessionMgr<S>, S> F getProxy(Class<F> daoClass, M mgr, boolean autoCommit)
	{
		return getProxy(daoClass, mgr, autoCommit, TransIsoLevel.DEFAULT);
	}
	
	/**
	 * 
	 * 获取 daoClass 的代理对象。
	 * 其中，{@link SessionMgr} 由 mgr 指定，是否自动提交事务由 autoCommit 参数指定。
	 * 
	 * @param daoClass		: 被代理的数据库访问类对象
	 * @param mgr			: {@link SessionMgr} 
	 * @param autoCommit	: 是否自动提交事务（为了提高执行效率，对于只执行查询的访问通常采用自动提交事务）
	 * @param level			: {@link TransIsoLevel}
	 * @return				     与 daoClass 有相同接口的代理对象
	 * 
	 */
	public static final <F extends AbstractFacade<M, S>, M extends SessionMgr<S>, S> F getProxy(Class<F> daoClass, M mgr, boolean autoCommit, TransIsoLevel level)
	{
		Callback intercepter = new ProxyInterceptor(autoCommit, level);
		return getProxy(daoClass, mgr, intercepter);
	}

	@SuppressWarnings("unchecked")
	private static final <F extends AbstractFacade<M, S>, M extends SessionMgr<S>, S> F getProxy(Class<F> daoClass, M mgr, Callback intercepter)
	{
		Enhancer en = new Enhancer();
		
		en.setSuperclass(daoClass);
		en.setCallbackFilter(InterceptFilter.INSTANCE);
		Callback[] callbacks = {NoOp.INSTANCE, intercepter};
		en.setCallbacks(callbacks);
		en.setInterceptDuringConstruction(false);
		
		Class<?>[] argTypes	= mgr != null ? new Class<?>[] {mgr.getClass()}	: new Class<?>[] {};
		Object[] args		= mgr != null ? new Object[] {mgr}				: new Object[] {};

		return (F)en.create(argTypes, args);
	}
	
	/** 函数回调过滤器 */
	private static class InterceptFilter implements CallbackFilter
	{
		private static final InterceptFilter INSTANCE	= new InterceptFilter();
		private static final Set<Method> FILTER_METHODS	= BeanHelper.getAllMethods(AbstractFacade.class);
		
		@Override
		public final int accept(Method method)
		{
			if(FILTER_METHODS.contains(method))
				return 0;
			
			return 1;
		}
	}
	
	private static final Object intercept(Object dao, Method method, Object[] args, MethodProxy proxy, SessionMgr<?> mgr, boolean autoCommit, TransIsoLevel transLevel) throws Throwable
	{
		Object result = null;

		if(mgr.isInvoking())
			result = proxy.invokeSuper(dao, args);
		else
		{
			mgr.setInvoking(true);
			
			TransIsoLevel defLevel	= mgr.getDefalutTransIsoLevel();
			boolean alterTransLevel	= (!autoCommit && transLevel != TransIsoLevel.DEFAULT && transLevel != defLevel);
			
        	try
        	{
        		if(alterTransLevel)	mgr.setSessionTransIsoLevel(transLevel);
        		if(!autoCommit)		mgr.beginTransaction();
        		result = proxy.invokeSuper(dao, args);
        		if(!autoCommit)		mgr.commit();
        	}
        	catch(Exception e)
        	{
        		if(!autoCommit) {try{mgr.rollback();} catch (Exception ex) {}}
        		throw new DAOException(e);
        	}
        	finally
        	{
        		if(alterTransLevel) try {mgr.setSessionTransIsoLevel(defLevel);} catch (Exception ex) {}
        		try {mgr.closeSession();} catch (Exception ex) {}
        		
        		mgr.setInvoking(false);
        	}
		}

		return result;
	}

	/** 事务回调拦截器 */
	private static class ProxyInterceptor implements MethodInterceptor
	{
		private TransAttr transAttr;

		private ProxyInterceptor(boolean autoCommit, TransIsoLevel transLevel)
		{
			this.transAttr = new TransAttr(autoCommit, transLevel);
		}
		
		@Override
		@SuppressWarnings("rawtypes")
		public final Object intercept(Object dao, Method method, Object[] args, MethodProxy proxy) throws Throwable
		{
			AbstractFacade facade	= (AbstractFacade)dao;
			SessionMgr<?> mgr		= facade.getManager();
			
			if(mgr.isInvoking())
				return proxy.invokeSuper(dao, args);

			return FacadeProxy.intercept(dao, method, args, proxy, mgr, transAttr.autoCommit, transAttr.transLevel);
		}
	}
	
	private static class TransAttr
	{
		private static final TransAttr DEFAULT = new TransAttr();
		
		private boolean autoCommit;
		private TransIsoLevel transLevel;
		
		private TransAttr()
		{
			this(false, TransIsoLevel.DEFAULT);
		}
		
		private TransAttr(boolean autoCommit, TransIsoLevel transLevel)
		{
			this.autoCommit = autoCommit;
			this.transLevel = transLevel;
		}
	}
	
	/* **************************************************************************************************** */
	//											新式 DAO 操作方法												//
	/* **************************************************************************************************** */
	
	/**
	 * 
	 * 获取 daoClass 的代理对象。
	 * 其中，{@link SessionMgr} 由 daoClass 的默认构造函数提供。
	 * 
	 * @param daoClass		: 被代理的数据库访问类对象
	 * @return				     与 daoClass 有相同接口的代理对象
	 * 
	 */
	public static final <F extends AbstractFacade<M, S>, M extends SessionMgr<S>, S> F create(Class<F> daoClass)
	{
		return create(daoClass, (M)null);
	}
	
	/**
	 * 
	 * 获取 daoClass 的代理对象。
	 * 其中，{@link SessionMgr} 由 mgr 指定。
	 * 
	 * @param daoClass		: 被代理的数据库访问类对象
	 * @param mgr			: {@link SessionMgr} 
	 * @return				     与 daoClass 有相同接口的代理对象
	 * 
	 */
	public static final <F extends AbstractFacade<M, S>, M extends SessionMgr<S>, S> F create(Class<F> daoClass, M mgr)
	{
		return getProxy(daoClass, mgr, ProxyInterceptor2.INSTANCE);
	}

	/** 事务回调拦截器 */
	private static class ProxyInterceptor2 implements MethodInterceptor
	{
		private static final ProxyInterceptor2 INSTANCE = new ProxyInterceptor2();
		private static final Map<CoupleKey<Class<?>, Method>, TransAttr> TRANS_ATTR_MAP = new HashMap<CoupleKey<Class<?>, Method>, TransAttr>();
		
		@Override
		@SuppressWarnings("rawtypes")
		public final Object intercept(Object dao, Method method, Object[] args, MethodProxy proxy) throws Throwable
		{
			AbstractFacade facade	= (AbstractFacade)dao;
			SessionMgr<?> mgr		= facade.getManager();
			
			if(mgr.isInvoking())
				return proxy.invokeSuper(dao, args);
			
			Class<?> superClass = dao.getClass().getSuperclass();
			CoupleKey<Class<?>, Method> key = new CoupleKey<Class<?>, Method>(superClass, method);
			
			checkTransAttrMap(key);
			
			TransAttr transAttr = TRANS_ATTR_MAP.get(key);
			return FacadeProxy.intercept(dao, method, args, proxy, mgr, transAttr.autoCommit, transAttr.transLevel);
		}

		private static final void checkTransAttrMap(CoupleKey<Class<?>, Method> key)
		{
			if(!TRANS_ATTR_MAP.containsKey(key))
			{
				TransAttr transAttr	= null;
				Transaction trans	= key.getKey2().getAnnotation(Transaction.class);
				
				if(trans == null)
					trans = key.getKey1().getAnnotation(Transaction.class);
				if(trans == null)
					transAttr = TransAttr.DEFAULT;
				else
					transAttr = new TransAttr(!trans.value(), trans.level());
				
				GeneralHelper.syncTryPut(TRANS_ATTR_MAP, key, transAttr);
			}
		}
	}

	/* **************************************************************************************************** */
	//											自定义事务执行方法											//
	/* **************************************************************************************************** */

	/**
	 * 执行自定义事务<br>
	 * JessMA 的事务是 DAO 层事务，也就是说当外部调用某个 DAO 方法时，该方法作为一个事务单元执行。
	 * 但在一些特殊情形下可能需要在 DAO 方法外部执行事务（例如：作为一个事务单元调用多个 DAO 对象的多个方法），
	 * 此时可以创建一个自定义事务（{@linkplain CustomTransaction}），并调用 FacadeProxy 的 executeCustomTransaction(...) 来执行该自定义事务。自定义事务执行规则：<br>
	 * <ul>
	 * <li>FacadeProxy.executeCustomTransaction(...) 一般在 DAO 方法外部调用</li>
	 * <li>FacadeProxy.executeCustomTransaction(...) 也可以在 DAO 方法内部调用，但如果调用它的 DAO 方法与它拥有共同的 SessionMgr，则事务属性由该 DAO 方法控制</li>
	 * <li>{@linkplain CustomTransaction} 的事务入口方法  {@linkplain CustomTransaction#execute(SessionMgr) execute(SessionMgr)} 作为一个事务单元</li>
	 * <li>{@linkplain CustomTransaction#execute(SessionMgr)} 方法中可以用 new 方式直接创建的 DAO 对象，也可以用 {@linkplain FacadeProxy} 的代理方法
	 * （create() / getXxxCommitProxy()）创建 DAO 对象，无论采用哪种方式创建的 DAO 对象，DAO 对象的事务属性均由 FacadeProxy.executeCustomTransaction(...) 指定</li>
	 * </ul>
	 * 
	 * @param mgr			: {@link SessionMgr}
	 * @param trans			: 自定义事务 
	 * 
	 */
	public static final <M extends SessionMgr<S>, S> void executeCustomTransaction(M mgr, CustomTransaction<M, S> trans) throws DAOException
	{
		executeCustomTransaction(mgr, TransIsoLevel.DEFAULT, trans);
	}
	
	/**
	 * 执行自定义事务<br>
	 * JessMA 的事务是 DAO 层事务，也就是说当外部调用某个 DAO 方法时，该方法作为一个事务单元执行。
	 * 但在一些特殊情形下可能需要在 DAO 方法外部执行事务（例如：作为一个事务单元调用多个 DAO 对象的多个方法），
	 * 此时可以创建一个自定义事务（{@linkplain CustomTransaction}），并调用 FacadeProxy 的 executeCustomTransaction(...) 来执行该自定义事务。自定义事务执行规则：<br>
	 * <ul>
	 * <li>FacadeProxy.executeCustomTransaction(...) 一般在 DAO 方法外部调用</li>
	 * <li>FacadeProxy.executeCustomTransaction(...) 也可以在 DAO 方法内部调用，但如果调用它的 DAO 方法与它拥有共同的 SessionMgr，则事务属性由该 DAO 方法控制</li>
	 * <li>{@linkplain CustomTransaction} 的事务入口方法  {@linkplain CustomTransaction#execute(SessionMgr) execute(SessionMgr)} 作为一个事务单元</li>
	 * <li>{@linkplain CustomTransaction#execute(SessionMgr)} 方法中可以用 new 方式直接创建的 DAO 对象，也可以用 {@linkplain FacadeProxy} 的代理方法
	 * （create() / getXxxCommitProxy()）创建 DAO 对象，无论采用哪种方式创建的 DAO 对象，DAO 对象的事务属性均由 FacadeProxy.executeCustomTransaction(...) 指定</li>
	 * </ul>
	 * 
	 * @param mgr			: {@link SessionMgr}
	 * @param level			: 事务隔离级别 
	 * @param trans			: 自定义事务 
	 * 
	 */
	public static final <M extends SessionMgr<S>, S> void executeCustomTransaction(M mgr, TransIsoLevel level, CustomTransaction<M, S> trans) throws DAOException
	{
		if(mgr.isInvoking())
			trans.execute(mgr);
		else
		{
			mgr.setInvoking(true);
			
			TransIsoLevel defLevel	= mgr.getDefalutTransIsoLevel();
			boolean alterTransLevel	= (level != TransIsoLevel.DEFAULT && level != defLevel);
			
	    	try
	    	{
	    		if(alterTransLevel)
	    			mgr.setSessionTransIsoLevel(level);
	    		
	    		mgr.beginTransaction();
	    		trans.execute(mgr);
	    		mgr.commit();
	    	}
	    	catch(Exception e)
	    	{
	    		{try{mgr.rollback();} catch (Exception ex) {}}
	    		throw new DAOException(e);
	    	}
	    	finally
	    	{
	    		if(alterTransLevel) try {mgr.setSessionTransIsoLevel(defLevel);} catch (Exception ex) {}
	    		try {mgr.closeSession();} catch (Exception ex) {}
	    		
	    		mgr.setInvoking(false);
	    	}
		}
	}
	
}
