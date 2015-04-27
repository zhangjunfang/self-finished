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

package org.jessma.mvc;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;

/** Bean 自动验证器接口 */
public interface BeanValidator
{
	/** 初始化方法 */
	void init();
	/** 清理方法 */
	void destroy();
	/**
	 * 自动验证方法<br/>
	 * 如果失败则不执行手工验证方法和 Action 入口方法, 并立刻定向到 {@link Action#INPUT} 视图 
	 */
	Set<ConstraintViolation<Object>> validate(final Object bean, final Class<?>[] groups, final String bundle, final Locale locale);
	
	/** 验证器属性帮助类 */
	public static class ValidatorKey
	{
		private String bundle;
		private Locale locale;
		private Class<?>[] groups;
		
		public ValidatorKey()
		{

		}
		
		public ValidatorKey(Class<?>[] groups, String bundle, Locale locale)
		{
			this.bundle = bundle;
			this.locale = locale;
			this.groups = groups;
		}
		
		@Override
		public int hashCode()
		{
			return Arrays.hashCode(groups) ^ bundle.hashCode() ^ locale.hashCode();
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if(obj instanceof ValidatorKey)
			{
				ValidatorKey other = (ValidatorKey)obj;
				
				return	bundle.equals(other.bundle)	&&
						locale.equals(other.locale)	&&
						Arrays.equals(groups, other.groups);
			}
			
			return false;
		}

		public Class<?>[] getGroups()
		{
			return groups;
		}

		public void setGroups(Class<?>[] groups)
		{
			this.groups = groups;
		}

		public String getBundle()
		{
			return bundle;
		}

		public void setBundle(String bundle)
		{
			this.bundle = bundle;
		}

		public Locale getLocale()
		{
			return locale;
		}

		public void setLocale(Locale locale)
		{
			this.locale = locale;
		}
	}
}
