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

/** 数值范围类 */
public class Range<T extends Number>
{
	private T begin;
	private T end;
	
	public Range()
	{
		
	}
	
	public Range(T begin, T end)
	{
		this.begin	= begin;
		this.end	= end;
	}

	public T getBegin()
	{
		return begin;
	}

	public void setBegin(T begin)
	{
		this.begin = begin;
	}

	public T getEnd()
	{
		return end;
	}

	public void setEnd(T end)
	{
		this.end = end;
	}
	
	public byte byteSize()
	{
		return (byte)(end.byteValue() - begin.byteValue());
	}
	
	public short shortSize()
	{
		return (short)(end.shortValue() - begin.shortValue());
	}
	
	public int intSize()
	{
		return end.intValue() - begin.intValue();
	}
	
	public long longSize()
	{
		return end.longValue() - begin.longValue();
	}
	
	public float floatSize()
	{
		return end.floatValue() - begin.floatValue();
	}
	
	public double doubleSize()
	{
		return end.doubleValue() - begin.doubleValue();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if(obj instanceof Range)
		{
			Range<?> other = (Range<?>)obj;
			
			if(begin == other.begin && end == other.end)
				return true;	
			
			if(begin != null && !begin.equals(other.begin))
				return false;
			else if(begin == null && other.begin != null)
				return false;
			
			if(end != null && !end.equals(other.end))
				return false;
			else if(end == null && other.end != null)
				return false;
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return (begin != null ? begin.hashCode() : 0) ^ (end != null ? end.hashCode() : 0);
	}
	
	@Override
	public String toString()
	{
		return String.format("{%s - %s}", begin, end);
	}
}
