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

import java.security.InvalidParameterException;
import java.sql.Connection;

/** 事务隔离级别 */
public enum TransIsoLevel
{
	/** 数据库驱动默认事务隔离级别 */
	DEFAULT
	{
		/** 返回: 0 */
		@Override
		public int toInt()
		{
			return Connection.TRANSACTION_NONE;
		}
	},
	/** 允许脏读、不可重复读和幻读 */
	READ_UNCOMMITTED
	{
		/** 返回: 1 */
		@Override
		public int toInt()
		{
			return Connection.TRANSACTION_READ_UNCOMMITTED;
		}
	},
	/** 禁止脏读，但允许不可重复读和幻读 */
	READ_COMMITTED
	{
		/** 返回: 2 */
		@Override
		public int toInt()
		{
			return Connection.TRANSACTION_READ_COMMITTED;
		}
	},
	/** 禁止脏读和不可重复读，但允许幻读 */
	REPEATABLE_READ
	{
		/** 返回: 4 */
		@Override
		public int toInt()
		{
			return Connection.TRANSACTION_REPEATABLE_READ;
		}
	},
	/** 禁止脏读、不可重复读和幻读 */
	SERIALIZABLE
	{
		/** 返回: 8 */
		@Override
		public int toInt()
		{
			return Connection.TRANSACTION_SERIALIZABLE;
		}
	};

	/** 事务隔离级别枚举对象转换为整数值 */
	public abstract int toInt();

	/** 整数值转换为事务隔离级别枚举对象
	 * 
	 * @param level			<br>			: 0 - {@link TransIsoLevel#DEFAULT}<br>
	 * 										: 1 - {@link TransIsoLevel#READ_UNCOMMITTED}<br>
	 * 										: 2 - {@link TransIsoLevel#READ_COMMITTED}<br>
	 * 										: 4 - {@link TransIsoLevel#REPEATABLE_READ}<br>
	 * 										: 8 - {@link TransIsoLevel#SERIALIZABLE}<br>
	 * @throws InvalidParameterException	: 找不到对应的事务隔离级别枚举对象时抛出异常
	 *  
	 */
	public static final TransIsoLevel fromInt(int level)
	{
		for(TransIsoLevel lv : TransIsoLevel.values())
		{
			if(lv.toInt() == level)
				return lv;
		}
		
		throw new InvalidParameterException("invalid paramer value");
	}
}
