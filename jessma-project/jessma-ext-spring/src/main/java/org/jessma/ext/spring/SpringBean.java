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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 自动装配 Spring Bean 的注解类<br> 
 * 
 * 1、同时指定 {@link SpringBean#name()} 和 {@link SpringBean#type()}：按 Bean 名称和类型注入<br>
 * 2、只指定 {@link SpringBean#name()}：按 Bean 名称注入<br>
 * 3、只指定 {@link SpringBean#type()}：按 Bean 类型注入<br>
 * 4、{@link SpringBean#name()} 和 {@link SpringBean#type()} 都不指定：按 Bean 名称注入，其中 Bean 名称与 {@link SpringBean#value()} 一致
 * 
 */
@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SpringBean
{
	/** 待注入的属性或成员变量的名称 */
	String value();
	/** Bean 类型 */
	Class<?> type() default Object.class;
	/** Bean 名称 */
	String name() default "";
}
