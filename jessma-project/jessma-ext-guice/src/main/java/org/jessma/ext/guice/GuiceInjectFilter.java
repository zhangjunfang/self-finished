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

package org.jessma.ext.guice;

import java.beans.PropertyDescriptor;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jessma.mvc.Action;
import org.jessma.mvc.ActionExecutor;
import org.jessma.mvc.ActionFilter;
import org.jessma.mvc.ActionSupport;
import org.jessma.util.BeanHelper;
import org.jessma.util.CoupleKey;
import org.jessma.util.GeneralHelper;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

/** 解析 {@link GuiceBean} 和 {@link GuiceBeans} 的 {@link ActionFilter} */
public class GuiceInjectFilter implements ActionFilter
{
	private Injector injector;
	private Map<CoupleKey<Class<?>, Method>, GuiceAttr[]> guiceMap;
	
	@Override
	public void init()
	{
		guiceMap = new HashMap<CoupleKey<Class<?>, Method>, GuiceAttr[]>();
		injector = Guice.createInjector(configStage(), configModules());
	}

	@Override
	public void destroy()
	{
		guiceMap = null;
		injector = null;
	}
	
	/** 获取 Injector */
	protected Injector getInjector()
	{
		return injector;
	}
	
	/** 配置 Guice Injector 的 {@link Stage}，（默认：{@link Stage#DEVELOPMENT}）<br>
	 * 
	 * 子类可改写本方法，使用自定义 {@link Stage}
	 */
	protected Stage configStage()
	{
		return Stage.DEVELOPMENT;
	}
	
	/** 配置 Guice Injector 的 {@link Module} 集合，（默认：空集合）<br>
	 * 
	 * 子类可改写本方法，使用自定义 {@link Module} 集合
	 */
	protected Collection<Module> configModules()
	{
		return new ArrayList<Module>();
	}

	@Override
	public String doFilter(ActionExecutor executor) throws Exception
	{
		Action action = executor.getAction();
		Method method = executor.getEntryMethod();
		
		CoupleKey<Class<?>, Method> key = new CoupleKey<Class<?>, Method>(action.getClass(), method);

		checkGuiceMap(executor, action, key);	
		tryInject(action, key);

		return executor.invoke();
	}

	private void checkGuiceMap(ActionExecutor executor, Action action, CoupleKey<Class<?>, Method> key) throws Exception
	{
		if(!guiceMap.containsKey(key))
		{
			List<GuiceAttr> guiceAttrList		= new ArrayList<GuiceAttr>();
			Map<String, GuiceBean> guiceBeanMap	= parseGuiceBeans(executor, action, key);
			
			parseGuiceAttrs(executor, action, guiceAttrList, guiceBeanMap);
			tryPutGuiceMap(key, guiceAttrList);
		}
	}

	private void parseGuiceAttrs(ActionExecutor executor, Action action, List<GuiceAttr> guiceAttrList, Map<String, GuiceBean> guiceBeanMap) throws Exception
	{
		Set<Entry<String, GuiceBean>> entries = guiceBeanMap.entrySet();
		
		for(Entry<String, GuiceBean> entry : entries)
			parseGuiceAttr(executor, action, entry, guiceAttrList);
	}

	private void tryInject(Action action, CoupleKey<Class<?>, Method> key)
	{
		GuiceAttr[] guiceAttrs = guiceMap.get(key);
		
		if(guiceAttrs != null)
		{
			for(GuiceAttr guiceAttr : guiceAttrs)
				guiceAttr.inject(action);
		}
	}

	private void tryPutGuiceMap(CoupleKey<Class<?>, Method> key, List<GuiceAttr> guiceAttrList)
	{
		GuiceAttr[] guiceAttrs = guiceAttrList.isEmpty() ? null : guiceAttrList.toArray(new GuiceAttr[guiceAttrList.size()]);
		GeneralHelper.syncTryPut(guiceMap, key, guiceAttrs);
	}

	private void parseGuiceAttr(ActionExecutor executor, Action action, Entry<String, GuiceBean> entry, List<GuiceAttr> guiceAttrList) throws Exception
	{
		String attr			= entry.getKey();
		GuiceBean guiceBean	= entry.getValue();
		
		if(attr == null)
			return;		
		
		Class<? extends Action> actionClass = action.getClass();
		Class<?> stopClass = ActionSupport.class.isAssignableFrom(actionClass) ?
							 ActionSupport.class : Object.class;
		
		GuiceAttr guiceAttr = new GuiceAttr(attr, guiceBean.type());
		parsePropertyOrField(executor, actionClass, stopClass, guiceAttr);

		guiceAttrList.add(guiceAttr);
	}

	private Map<String, GuiceBean> parseGuiceBeans(ActionExecutor executor, Action action, CoupleKey<Class<?>, Method> key) throws Exception
	{
		Map<String, GuiceBean> guiceBeanMap	= new HashMap<String, GuiceBean>();
		
		analysisGuiceBeans(executor, action, key.getKey2(), guiceBeanMap);
		
		if(guiceBeanMap.isEmpty())
			analysisGuiceBeans(executor, action, key.getKey1(), guiceBeanMap);
		
		return guiceBeanMap;
	}
	
	private void parsePropertyOrField(ActionExecutor executor, Class<? extends Action> actionClass, Class<?> stopClass, GuiceAttr guiceAttr) throws Exception
	{
		PropertyDescriptor pd	= BeanHelper.getPropDescByName(actionClass, stopClass, guiceAttr.attr);
		Method setter			= BeanHelper.getPropertyWriteMethod(pd);
		
		if(setter != null)
		{
			guiceAttr.property = pd;
			
			if(guiceAttr.type == Object.class)
				guiceAttr.type = setter.getReturnType();
		}
		else
		{							
			Field field = BeanHelper.getInstanceFiledByName(actionClass, stopClass, guiceAttr.attr);
			
			if(field != null)
			{
				guiceAttr.field = field;
				
				if(guiceAttr.type == Object.class)
					guiceAttr.type = field.getType();
			}
		}
		
		if(guiceAttr.property == null && guiceAttr.field == null)
			throwParseException(executor, null, String.format("no property or field named '%s'", guiceAttr.attr));
	}

	private void analysisGuiceBeans(ActionExecutor executor, Action action, AnnotatedElement element, Map<String, GuiceBean> guiceBeanMap) throws Exception
	{
		GuiceBean guiceBean = element.getAnnotation(GuiceBean.class);
		
		if(guiceBean != null)
			GeneralHelper.tryPut(guiceBeanMap, guiceBean.value(), guiceBean);
		
		GuiceBeans guiceBeans = element.getAnnotation(GuiceBeans.class);
		
		if(guiceBeans != null)
		{
			GuiceBean[] guiceBeanArr = guiceBeans.value();
			
			if(guiceBeanArr.length == 0)
				guiceBeanMap.put(null, null);
			else
			{
				for(GuiceBean guiceBean2 : guiceBeanArr)
					GeneralHelper.tryPut(guiceBeanMap, guiceBean2.value(), guiceBean2);
			}
		}
	}
	
	private static final void throwParseException(ActionExecutor executor, String name, String cause) throws Exception
	{
		String msg;
		if(GeneralHelper.isStrNotEmpty(name))
			msg =	String.format("Parse @GuiceBean / @GuiceBeans fail '%s#%s()' ['%s'] -> %s", 
					executor.getAction().getClass().getName(),
					executor.getEntryMethod().getName(), name, cause);
		else
			msg =	String.format("Parse @GuiceBean / @GuiceBeans fail '%s#%s()' -> %s", 
    				executor.getAction().getClass().getName(),
    				executor.getEntryMethod().getName(), cause);
			
		throw new RuntimeException(msg);
	}

	private class GuiceAttr
	{
		String attr;
		PropertyDescriptor property;
		Field field;
		Class<?> type;
		
		public GuiceAttr(String attr, Class<?> type)
		{
			this.attr = attr;
			this.type = type;
		}
		
		private boolean inject(Action action)
		{
			Object bean = injector.getInstance(type);
			
			if(property != null)
				return BeanHelper.setProperty(action, property, bean);
			else if(field != null)
				return BeanHelper.setFieldValue(action, field, bean);
			
			return false;
		}
	}
}
