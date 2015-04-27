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

import java.util.HashMap;

/**
 * 
 * 所有键均为小写字母字符串的 {@link HashMap}
 *
 * @param <V>	: 任意类型
 */
@SuppressWarnings("serial")
public class LStrMap<V> extends HashMap<String, V>
{
	@Override
	public V put(String key, V value)
	{
		return super.put(key.toLowerCase(), value);
	}
	
	@Override
	public V get(Object key)
	{
		key = key.toString().toLowerCase();
		return super.get(key);
	}
	
	@Override
	public V remove(Object key)
	{
		key = key.toString().toLowerCase();
		return super.remove(key);
	}
	
	@Override
	public boolean containsKey(Object key)
	{
		key = key.toString().toLowerCase();
		return super.containsKey(key);
	}
}
