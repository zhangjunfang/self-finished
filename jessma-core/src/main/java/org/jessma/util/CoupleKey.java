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

package org.jessma.util;

/** 两个元素组成的 Map/Set Key 包装类 */
public class CoupleKey<K1, K2>
{
	private K1 key1;
	private K2 key2;
	
	public CoupleKey()
	{
	}
	
	public CoupleKey(K1 key1, K2 key2)
	{
		this.key1 = key1;
		this.key2 = key2;
	}
	
	@Override
	public int hashCode()
	{
		int c1 = key1 != null ? key1.hashCode() : 0;
		int c2 = key2 != null ? key2.hashCode() : 0;
		
		return c1 ^ c2;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if(obj instanceof CoupleKey)
		{
			CoupleKey other = (CoupleKey)obj;
			
			if(key1 != null)
			{
				if(!key1.equals(other.key1))
					return false;
			}
			else if(other.key1 != null)
				return false;
			
			if(key2 != null)
				return key2.equals(other.key2);
			else
				return other.key2 == null;
		}
		
		return false;
	}

	public K1 getKey1()
	{
		return key1;
	}

	public void setKey1(K1 key1)
	{
		this.key1 = key1;
	}

	public K2 getKey2()
	{
		return key2;
	}

	public void setKey2(K2 key2)
	{
		this.key2 = key2;
	}
	
}
