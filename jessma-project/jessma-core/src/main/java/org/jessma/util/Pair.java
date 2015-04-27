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

/** 值对公共类：<br>
 * 
 * {@link Pair#first} - 第一个值 <br>
 * {@link Pair#second} - 第二个值
 * 
 * */
public class Pair<F extends Object, S extends Object>
{	
	private F first;
	private S second;
	
	public Pair()
	{
	}
	
	public Pair(F first)
	{
		set(first, null);
	}

	public Pair(F first, S second)
	{
		set(first, second);
	}
	
	public Pair(Pair<F, S> other)
	{
		set(other.first, other.second);
	}

	public F getFirst()
	{
		return first;
	}

	public void setFirst(F first)
	{
		this.first = first;
	}

	public S getSecond()
	{
		return second;
	}

	public void setSecond(S second)
	{
		this.second = second;
	}
	
	public void set(F first, S second)
	{
		this.first	= first;
		this.second	= second;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if(obj instanceof Pair)
		{
			Pair<?, ?> other = (Pair<?, ?>)obj;
			
			if(first == other.first && second == other.second)
				return true;	
			
			if(first != null && !first.equals(other.first))
				return false;
			else if(first == null && other.first != null)
				return false;
			
			if(second != null && !second.equals(other.second))
				return false;
			else if(second == null && other.second != null)
				return false;
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return (first != null ? first.hashCode() : 0) ^ (second != null ? second.hashCode() : 0);
	}
	
	@Override
	public String toString()
	{
		return String.format("{%s, %s}", first, second);
	}

}
