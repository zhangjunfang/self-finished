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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/** 自动装配 DAO Bean 的注解类 */
@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("rawtypes")
public @interface DaoBean
{
	/** 待注入的属性或成员变量的名称<br>
	 * （默认：注入 Action 定义中第一个 Dao 类型的属性或成员变量） */
	String value() default "";
	/** 待注入的 DAO 类型，如果该类不能实例化类则注入失败<br>
	 * （默认：待注入的属性或成员变量的类型） */
	Class<? extends AbstractFacade> daoClass() default AbstractFacade.class;
	/** app-config.xml 中定义的 {@link SessionMgr} 名称，
	 * 对应 {@link FacadeProxy#create(Class, SessionMgr)} 的第二个参数<br>
	 * （默认：DAO 的默认 {@link SessionMgr}，效果等同于调用
	 * {@link FacadeProxy#create(Class)}） */
	String mgrName() default "";
}
