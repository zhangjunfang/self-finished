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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 定义 Action Result 的注解类 */
@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Result
{
	/** Result Name（默认：{@link Action#SUCCESS}） */
	String value() default Action.SUCCESS;
	/** Result Type（默认：{@link Action.ResultType#DEFAULT}）<br>
	 * 默认规则：<br>
	 * &nbsp;&nbsp;1、如果 {@link Result#value()} == {@link Action#NONE}，则  {@link Result#type()} 为 {@link Action.ResultType#FINISH}<br>
	 * &nbsp;&nbsp;2、如果  {@link Result#value()} != {@link Action#NONE}，则  {@link Result#type()} 为 {@link Action.ResultType#DISPATCH}<br>
	 */
	Action.ResultType type() default Action.ResultType.DEFAULT;
	/** Result 目标地址（默认：""） */
	String path() default "";
}
