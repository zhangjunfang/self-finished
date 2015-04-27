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

package org.jessma.ext.spring;

import java.beans.PropertyDescriptor;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.jessma.mvc.Action;
import org.jessma.mvc.ActionExecutor;
import org.jessma.mvc.ActionFilter;
import org.jessma.mvc.ActionSupport;
import org.jessma.util.BeanHelper;
import org.jessma.util.CoupleKey;
import org.jessma.util.GeneralHelper;
import org.jessma.util.http.HttpHelper;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/** 解析 {@link SpringBean} 和 {@link SpringBeans} 的 {@link ActionFilter} */
public class SpringInjectFilter implements ActionFilter
{
	private WebApplicationContext context;
	private ContextLoaderListener listener;
	private ServletContext servletContext;
	private Map<CoupleKey<Class<?>, Method>, SpringAttr[]> springMap;
	
	@Override
	public void init()
	{
		servletContext	= HttpHelper.getServletContext();
		springMap		= new HashMap<CoupleKey<Class<?>, Method>, SpringAttr[]>();
		context			= WebApplicationContextUtils.getWebApplicationContext(servletContext);

		if(context == null)
		{
			listener = new ContextLoaderListener();
			listener.contextInitialized(new ServletContextEvent(servletContext));
			context  = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		}
	}

	@Override
	public void destroy()
	{
		if(listener != null)
			listener.contextDestroyed(new ServletContextEvent(servletContext));
		
		springMap		= null;
		servletContext	= null;
		listener		= null;
		context			= null;
	}

	@Override
	public String doFilter(ActionExecutor executor) throws Exception
	{
		Action action = executor.getAction();
		Method method = executor.getEntryMethod();
		
		CoupleKey<Class<?>, Method> key = new CoupleKey<Class<?>, Method>(action.getClass(), method);

		checkSpringMap(executor, action, key);	
		tryInject(action, key);

		return executor.invoke();
	}

	private void checkSpringMap(ActionExecutor executor, Action action, CoupleKey<Class<?>, Method> key) throws Exception
	{
		if(!springMap.containsKey(key))
		{
			List<SpringAttr> springAttrList			= new ArrayList<SpringAttr>();
			Map<String, SpringBean> springBeanMap	= parseSpringBeans(executor, action, key);
			
			parseSpringAttrs(executor, action, springAttrList, springBeanMap);
			tryPutSpringMap(key, springAttrList);
		}
	}

	private void parseSpringAttrs(ActionExecutor executor, Action action, List<SpringAttr> springAttrList, Map<String, SpringBean> springBeanMap) throws Exception
	{
		Set<Entry<String, SpringBean>> entries = springBeanMap.entrySet();
		
		for(Entry<String, SpringBean> entry : entries)
			parseSpringAttr(executor, action, entry, springAttrList);
	}

	private void tryInject(Action action, CoupleKey<Class<?>, Method> key)
	{
		SpringAttr[] springAttrs = springMap.get(key);
		
		if(springAttrs != null)
		{
			for(SpringAttr springAttr : springAttrs)
				springAttr.inject(action);
		}
	}

	private void tryPutSpringMap(CoupleKey<Class<?>, Method> key, List<SpringAttr> springAttrList)
	{
		SpringAttr[] springAttrs = springAttrList.isEmpty() ? null : springAttrList.toArray(new SpringAttr[springAttrList.size()]);
		GeneralHelper.syncTryPut(springMap, key, springAttrs);
	}

	private void parseSpringAttr(ActionExecutor executor, Action action, Entry<String, SpringBean> entry, List<SpringAttr> springAttrList) throws Exception
	{
		String attr				= entry.getKey();
		SpringBean springBean	= entry.getValue();
		
		if(attr == null)
			return;		
		
		Class<? extends Action> actionClass = action.getClass();
		Class<?> stopClass = ActionSupport.class.isAssignableFrom(actionClass) ?
							 ActionSupport.class : Object.class;
		
		SpringAttr springAttr = new SpringAttr(attr);
		
		parseMode(executor, springBean, springAttr);
		parsePropertyOrField(executor, actionClass, stopClass, springAttr);

		springAttrList.add(springAttr);
	}

	private Map<String, SpringBean> parseSpringBeans(ActionExecutor executor, Action action, CoupleKey<Class<?>, Method> key) throws Exception
	{
		Map<String, SpringBean> springBeanMap	= new HashMap<String, SpringBean>();
		
		analysisSpringBeans(executor, action, key.getKey2(), springBeanMap);
		
		if(springBeanMap.isEmpty())
			analysisSpringBeans(executor, action, key.getKey1(), springBeanMap);
		
		return springBeanMap;
	}
	
	private void parseMode(ActionExecutor executor, SpringBean springBean, SpringAttr springAttr)
	{
		String name		= springBean.name();
		Class<?> type	= springBean.type();
		
		if(GeneralHelper.isStrNotEmpty(name) && type != Object.class)
		{
			springAttr.name	= name;
			springAttr.type	= type;
			springAttr.mode	= Mode.BOTH;
		}
		else if(GeneralHelper.isStrNotEmpty(name))
		{
			springAttr.name	= name;
			springAttr.mode	= Mode.NAME;
			
		}
		else if(type != Object.class)
		{
			springAttr.type	= type;
			springAttr.mode	= Mode.TYPE;
		}
		else
		{
			springAttr.name	= springAttr.attr;
			springAttr.mode	= Mode.NAME;			
		}
	}

	private void parsePropertyOrField(ActionExecutor executor, Class<? extends Action> actionClass, Class<?> stopClass, SpringAttr springAttr) throws Exception
	{
		PropertyDescriptor pd	= BeanHelper.getPropDescByName(actionClass, stopClass, springAttr.attr);
		Method setter			= BeanHelper.getPropertyWriteMethod(pd);
		
		if(setter != null)
			springAttr.property = pd;
		else
		{							
			Field field = BeanHelper.getInstanceFiledByName(actionClass, stopClass, springAttr.attr);
			
			if(field != null)
				springAttr.field = field;
		}
		
		if(springAttr.property == null && springAttr.field == null)
			throwParseException(executor, null, String.format("no property or field named '%s'", springAttr.attr));
	}

	private void analysisSpringBeans(ActionExecutor executor, Action action, AnnotatedElement element, Map<String, SpringBean> springBeanMap) throws Exception
	{
		SpringBean springBean = element.getAnnotation(SpringBean.class);
		
		if(springBean != null)
			GeneralHelper.tryPut(springBeanMap, springBean.value(), springBean);
		
		SpringBeans springBeans = element.getAnnotation(SpringBeans.class);
		
		if(springBeans != null)
		{
			SpringBean[] springBeanArr = springBeans.value();
			
			if(springBeanArr.length == 0)
				springBeanMap.put(null, null);
			else
			{
				for(SpringBean springBean2 : springBeanArr)
					GeneralHelper.tryPut(springBeanMap, springBean2.value(), springBean2);
			}
		}
	}
	
	private static final void throwParseException(ActionExecutor executor, String name, String cause) throws Exception
	{
		String msg;
		if(GeneralHelper.isStrNotEmpty(name))
			msg =	String.format("Parse @SpringBean / @SpringBeans fail '%s#%s()' ['%s'] -> %s", 
					executor.getAction().getClass().getName(),
					executor.getEntryMethod().getName(), name, cause);
		else
			msg =	String.format("Parse @SpringBean / @SpringBeans fail '%s#%s()' -> %s", 
    				executor.getAction().getClass().getName(),
    				executor.getEntryMethod().getName(), cause);
			
		throw new RuntimeException(msg);
	}
	
	private static enum Mode
	{
		NAME,
		TYPE,
		BOTH;
	}

	private class SpringAttr
	{
		String attr;
		PropertyDescriptor property;
		Field field;
		String name;
		Class<?> type;
		Mode mode;
		
		public SpringAttr(String attr)
		{
			this.attr = attr;
		}
		
		private boolean inject(Action action)
		{
			Object bean = null;
			
			switch(mode)
			{
			case NAME:
				bean = context.getBean(name);
				break;
			case TYPE:
				bean = context.getBean(type);
				break;
			case BOTH:
				bean = context.getBean(name, type);
				break;
			default:
				assert false;
			}
			
			if(property != null)
				return BeanHelper.setProperty(action, property, bean);
			else if(field != null)
				return BeanHelper.setFieldValue(action, field, bean);
			
			return false;
		}
	}
}
