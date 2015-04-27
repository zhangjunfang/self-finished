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

package org.jessma.app;

import java.beans.PropertyDescriptor;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jessma.dao.AbstractFacade;
import org.jessma.dao.DaoBean;
import org.jessma.dao.DaoBeans;
import org.jessma.dao.FacadeProxy;
import org.jessma.dao.SessionMgr;
import org.jessma.mvc.AbstractActionFilter;
import org.jessma.mvc.Action;
import org.jessma.mvc.ActionExecutor;
import org.jessma.mvc.ActionFilter;
import org.jessma.mvc.ActionSupport;
import org.jessma.util.BeanHelper;
import org.jessma.util.CoupleKey;
import org.jessma.util.GeneralHelper;


/** 解析 {@link DaoBean} 和 {@link DaoBeans} 的 {@link ActionFilter} */
public class DaoInjectFilter extends AbstractActionFilter
{
	private Map<CoupleKey<Class<?>, Method>, DaoAttr[]> daoMap;
	
	@Override
	public void init()
	{
		daoMap = new HashMap<CoupleKey<Class<?>, Method>, DaoAttr[]>();
	}
	
	@Override
	public void destroy()
	{
		daoMap = null;
	}
	
	@Override
	public String doFilter(ActionExecutor executor) throws Exception
	{
		Action action = executor.getAction();
		Method method = executor.getEntryMethod();
		
		CoupleKey<Class<?>, Method> key = new CoupleKey<Class<?>, Method>(action.getClass(), method);

		checkDaoMap(executor, action, key);	
		tryInject(action, key);

		return executor.invoke();
	}

	private void checkDaoMap(ActionExecutor executor, Action action, CoupleKey<Class<?>, Method> key) throws Exception
	{
		if(!daoMap.containsKey(key))
		{
			List<DaoAttr> daoAttrList		= new ArrayList<DaoAttr>();
			Map<String, DaoBean> daoBeanMap = parseDaoBeans(executor, action, key);
			
			parseDaoAttrs(executor, action, daoAttrList, daoBeanMap);
			tryPutDaoMap(key, daoAttrList);
		}
	}

	private void parseDaoAttrs(ActionExecutor executor, Action action, List<DaoAttr> daoAttrList, Map<String, DaoBean> daoBeanMap) throws Exception
	{
		Set<Entry<String, DaoBean>> entries = daoBeanMap.entrySet();
		
		for(Entry<String, DaoBean> entry : entries)
			parseDaoAttr(executor, action, entry, daoAttrList);
	}

	private void tryInject(Action action, CoupleKey<Class<?>, Method> key)
	{
		DaoAttr[] daoAttrs = daoMap.get(key);
		
		if(daoAttrs != null)
		{
			for(DaoAttr daoAttr : daoAttrs)
				daoAttr.inject(action);
		}
	}

	private void tryPutDaoMap(CoupleKey<Class<?>, Method> key, List<DaoAttr> daoAttrList)
	{
		DaoAttr[] daoAttrs = daoAttrList.isEmpty() ? null : daoAttrList.toArray(new DaoAttr[daoAttrList.size()]);
		GeneralHelper.syncTryPut(daoMap, key, daoAttrs);
	}

	@SuppressWarnings("rawtypes")
	private void parseDaoAttr(ActionExecutor executor, Action action, Entry<String, DaoBean> entry, List<DaoAttr> daoAttrList) throws Exception
	{
		String name		= entry.getKey();
		DaoBean daoBean	= entry.getValue();
		String mgrName	= daoBean.mgrName();
		
		Class<? extends AbstractFacade> daoClass = daoBean.daoClass();
		Class<? extends Action> actionClass		 = action.getClass();
		
		Class<?> stopClass = ActionSupport.class.isAssignableFrom(actionClass) ?
							 ActionSupport.class : Object.class;
		
		DaoAttr daoAttr	= new DaoAttr(name);
		
		parsePropertyOrField(executor, actionClass, stopClass, daoAttr);
		parseDaoClass(executor, daoClass, daoAttr);
		parseSessionMgr(executor, mgrName, daoAttr);

		daoAttrList.add(daoAttr);
	}

	private Map<String, DaoBean> parseDaoBeans(ActionExecutor executor, Action action, CoupleKey<Class<?>, Method> key) throws Exception
	{
		Map<String, DaoBean> daoBeanMap	= new HashMap<String, DaoBean>();
		
		analysisDaoBeans(executor, action, key.getKey2(), daoBeanMap);
		
		if(daoBeanMap.isEmpty())
			analysisDaoBeans(executor, action, key.getKey1(), daoBeanMap);
		
		return daoBeanMap;
	}
	
	@SuppressWarnings("rawtypes")
	private void parseSessionMgr(ActionExecutor executor, String mgrName, DaoAttr daoAttr) throws Exception
	{
		if(GeneralHelper.isStrEmpty(mgrName))
			daoAttr.mgr = null;
		else
		{
			SessionMgr mgr = AppConfig.getSessionManager(mgrName);
			
			if(mgr != null)
				daoAttr.mgr = mgr;
			else
				throwParseException(executor, daoAttr.name, String.format("Session Manager named '%s' not found", mgrName));
		}
		
		if(daoAttr.mgr != null)
		{
    		Class<?> clazz = daoAttr.daoClass;
    		
    		while(clazz.getSuperclass() != AbstractFacade.class)
    			clazz = clazz.getSuperclass();
    		
    		Type type = clazz.getGenericSuperclass();
    		
    		if(type instanceof ParameterizedType)
    		{
    			Class<?> paramClazz = (Class<?>)(((ParameterizedType)type).getActualTypeArguments()[0]);
    			
    			if(!paramClazz.isAssignableFrom(daoAttr.mgr.getClass()))
    			{
    				String cause =	String.format("DAO class (%s) does not match SessionMgr '%s' (%s)", 
    								daoAttr.daoClass.getName(), mgrName, daoAttr.mgr.getClass().getName());
    				
    				throwParseException(executor, daoAttr.name, cause);
    			}
    		}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void parseDaoClass(ActionExecutor executor, Class<? extends AbstractFacade> daoClass, DaoAttr daoAttr) throws Exception
	{
		if(!BeanHelper.isPublicClass(daoClass) || daoClass == AbstractFacade.class)
		{
			daoClass =  (Class<? extends AbstractFacade>)
						((daoAttr.property != null)			? 
						daoAttr.property.getPropertyType()	: 
						daoAttr.field.getType());
		}
		
		if(BeanHelper.isPublicClass(daoClass) && daoClass != AbstractFacade.class)
			daoAttr.daoClass = daoClass;
		else
			throwParseException(executor, daoAttr.name, String.format("Dao Class '%s' can not be instantiated", daoClass.getName()));
	}

	private void parsePropertyOrField(ActionExecutor executor, Class<? extends Action> actionClass, Class<?> stopClass, DaoAttr daoAttr) throws Exception
	{
		PropertyDescriptor pd	= BeanHelper.getPropDescByName(actionClass, stopClass, daoAttr.name);
		Method setter			= BeanHelper.getPropertyWriteMethod(pd);
		
		if(setter != null)
		{
			if(AbstractFacade.class.isAssignableFrom(pd.getPropertyType()))
				daoAttr.property = pd;
			else
				throwParseException(executor, daoAttr.name, "invalid type of property");
		}
		else
		{							
			Field field = BeanHelper.getInstanceFiledByName(actionClass, stopClass, daoAttr.name);
			
			if(field != null)
			{
				if(AbstractFacade.class.isAssignableFrom(field.getType()))
					daoAttr.field = field;
				else
					throwParseException(executor, daoAttr.name, "invalid type of field");
			}
		}
		
		if(daoAttr.property == null && daoAttr.field == null)
			throwParseException(executor, null, String.format("no property or field named '%s'", daoAttr.name));
	}

	private void putDaoBeanMap(ActionExecutor executor, Action action, DaoBean daoBean, boolean single, Map<String, DaoBean> daoBeanMap) throws Exception
	{
		String name = daoBean.value();
		
		if(GeneralHelper.isStrNotEmpty(name))
		{
			GeneralHelper.tryPut(daoBeanMap, name, daoBean);
			return;
		}
		
		boolean found		= false;
		Class<?> clazz		= action.getClass();
		Class<?> stopClass	= ActionSupport.class.isAssignableFrom(clazz) ? 
							  ActionSupport.class : Object.class;

		LOOP:
			
		do
		{
			Field[] fields = clazz.getDeclaredFields();
			
			for(Field field : fields)
			{
				if	(
						AbstractFacade.class.isAssignableFrom(field.getType()) && 
						BeanHelper.isInstanceNotFinalField(field)
					)
				{
					found	= true;
					name	= field.getName();
					
					GeneralHelper.tryPut(daoBeanMap, name, daoBean);

					if(single)
						break LOOP;
				}
			}
			
			clazz = clazz.getSuperclass();
			
		} while(clazz != null && clazz != stopClass);
		
		if(!found)
			throwParseException(executor, null, String.format("%s with default annotation param 'value' can not be deduced exactly",
								single ? "@DaoBean" : "@DaoBeans"));
	}

	private void analysisDaoBeans(ActionExecutor executor, Action action, AnnotatedElement element, Map<String, DaoBean> daoBeanMap) throws Exception
	{
		DaoBean daoBean = element.getAnnotation(DaoBean.class);
		
		if(daoBean != null)
			putDaoBeanMap(executor, action, daoBean, true, daoBeanMap);
		
		DaoBeans daoBeans = element.getAnnotation(DaoBeans.class);
		
		if(daoBeans != null)
		{
			DaoBean[] daoBeanArr = daoBeans.value();
					
			for(DaoBean daoBean2 : daoBeanArr)
				putDaoBeanMap(executor, action, daoBean2, false, daoBeanMap);
		}
	}
	
	private static final void throwParseException(ActionExecutor executor, String name, String cause) throws Exception
	{
		String msg;
		if(GeneralHelper.isStrNotEmpty(name))
			msg =	String.format("Parse @DaoBean / @DaoBeans fail '%s#%s()' ['%s'] -> %s", 
					executor.getAction().getClass().getName(),
					executor.getEntryMethod().getName(), name, cause);
		else
			msg =	String.format("Parse @DaoBean / @DaoBeans fail '%s#%s()' -> %s", 
    				executor.getAction().getClass().getName(),
    				executor.getEntryMethod().getName(), cause);
			
		
		throw new RuntimeException(msg);
	}

	@SuppressWarnings("rawtypes")
	static private class DaoAttr
	{
		String name;
		PropertyDescriptor property;
		Field field;
		Class<? extends AbstractFacade> daoClass;
		SessionMgr mgr;
		
		public DaoAttr(String name)
		{
			this.name = name;
		}
		
		@SuppressWarnings("unchecked")
		private boolean inject(Action action)
		{
			AbstractFacade dao = FacadeProxy.create(daoClass, mgr);
			
			if(property != null)
				return BeanHelper.setProperty(action, property, dao);
			else if(field != null)
				return BeanHelper.setFieldValue(action, field, dao);
			
			return false;
		}
	}
}
