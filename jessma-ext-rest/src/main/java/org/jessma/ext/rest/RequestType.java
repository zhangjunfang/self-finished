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

package org.jessma.ext.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.jessma.util.BeanHelper;


/** REST 请求类型：(GET/POST/PUT/DELETE) <br>
 * 
 * 注：一般浏览器只支持 GET 和 PUT 请求，如果要发送
 * PUT 或 DELETE 请求，可以在发送请求时加上请求参数
 *  "__rest_method={请求类型}" 来模拟。
 */
public enum RequestType
{
	GET
	{
		@Override
		public Class<Get> toAnnotationClass()
		{
			return Get.class;
		}
	},
	
	POST
	{
		@Override
		public Class<Post> toAnnotationClass()
		{
			return Post.class;
		}		
	},
	
	PUT
	{
		@Override
		public Class<Put> toAnnotationClass()
		{
			return Put.class;
		}
	},
	
	DELETE
	{
		@Override
		public Class<Delete> toAnnotationClass()
		{
			return Delete.class;
		}
	};	
	
	public abstract Class<? extends Annotation> toAnnotationClass();
	
	@Override
	public String toString()
	{
		return super.toString().toUpperCase();
	}
	
	public static final RequestType fromAnnotation(Annotation annotation)
	{
		return fromAnnotationClass(annotation.annotationType());
	}
	
	public static final RequestType fromAnnotationClass(Class<? extends Annotation> annClazz)
	{
		String value = annClazz.getSimpleName().toUpperCase();
		return RequestType.valueOf(value);
	}
	
	@SuppressWarnings("unchecked")
	public static final Class<? extends Annotation>[] toAnnotationClasses()
	{
		RequestType[] values = values();
		Class<? extends Annotation>[] types = new Class[values.length];
		
		for(int i = 0; i < values.length; i++)
			types[i] = values[i].toAnnotationClass();
		
		return types;
	}
	
	public static final String[] getAnnotationValue(Annotation annotation)
	{
		final String PROP_VALUE = "value";
		
		try
		{
			Class<?> clazz	= annotation.annotationType();
			Method method	= clazz.getMethod(PROP_VALUE);
			
			return BeanHelper.invokeMethod(annotation, method);
		}
		catch(NoSuchMethodException e)
		{
			throw new RestException(e);
		}
	}
}
