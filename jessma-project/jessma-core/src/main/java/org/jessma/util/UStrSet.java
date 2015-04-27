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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * 
 * 所有值均为大写字母字符串的 {@link HashSet}
 *
 */
@SuppressWarnings("serial")
public class UStrSet extends HashSet<String>
{
	@Override
	public boolean add(String value)
	{
		return super.add(value.toUpperCase());
	}
	
	@Override
	public boolean remove(Object key)
	{
		key = key.toString().toUpperCase();
		return super.remove(key);
	}
	
	@Override
	public boolean contains(Object key)
	{
		key = key.toString().toUpperCase();
		return super.contains(key);
	}
	
	@Override
	public boolean retainAll(Collection<?> c)
	{
		ArrayList<String> list = new ArrayList<String>(c.size());
		
		for(Object o : c)
			list.add(o.toString().toUpperCase());

		return super.retainAll(list);
	}

}
