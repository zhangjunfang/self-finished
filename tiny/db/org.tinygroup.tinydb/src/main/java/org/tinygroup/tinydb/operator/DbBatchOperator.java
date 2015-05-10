/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.tinydb.operator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.exception.TinyDbException;

/**
 * DB相关的批量操作
 * 
 * @author luoguo
 * 
 */
public interface DbBatchOperator<K> {
	// 下面是根据数组

	Bean[] batchInsert(Bean[] beans) throws TinyDbException;

	/**
	 * 
	 * 可以分批次进行批处理操作
	 * 
	 * @param beans
	 *            批处理的所有记录
	 * @param batchSize
	 *            一次批处理最大数量
	 * @return
	 */
	Bean[] batchInsert(Bean[] beans, int batchSize) throws TinyDbException;

	int[] batchUpdate(Bean[] beans) throws TinyDbException;

	void batchUpdate(Bean[] beans, int batchSize) throws TinyDbException;

	int[] batchDelete(Bean[] beans) throws TinyDbException;

	void batchDelete(Bean[] beans, int batchSize) throws TinyDbException;

	int[] deleteById(K[] beanIds, String beanType) throws TinyDbException;

	Bean[] getBeansById(K[] beanIds, String beanTyoe) throws TinyDbException;

	Bean[] getBeans(Bean bean) throws TinyDbException;

	// 下面是根据集合
	Bean[] batchInsert(Collection<Bean> beans) throws TinyDbException;

	Bean[] batchInsert(Collection<Bean> beans, int batchSize)
			throws TinyDbException;

	int[] batchUpdate(Collection<Bean> beans) throws TinyDbException;

	void batchUpdate(Collection<Bean> beans, int batchSize)
			throws TinyDbException;

	int[] batchDelete(Collection<Bean> beans) throws TinyDbException;

	void batchDelete(Collection<Bean> beans, int batchSize)
			throws TinyDbException;

	int[] deleteById(Collection<K> beanIds, String beanType)
			throws TinyDbException;

	Bean[] getBeansById(Collection<K> beanIds, String beanType)
			throws TinyDbException;

	/**
	 * 
	 * 处理的bean类型可以不一样，非批处理实现方式
	 * 
	 * @param beans
	 * @return
	 */
	Bean[] insertBean(Bean[] beans) throws TinyDbException;

	int[] updateBean(Bean[] beans) throws TinyDbException;

	int[] deleteBean(Bean[] beans) throws TinyDbException;

	/**
	 * 批量执行sql语句
	 * 
	 * @param sqls
	 * @return
	 * @throws TinyDbException
	 */
	public int[] executeBatch(List<String> sqls) throws TinyDbException;

	public int[] executeBatchByList(String sql, List<List<Object>> parameters)
			throws TinyDbException;
	
	public int[] executeBatchByList(String sql, List<List<Object>> parameters,int[] columnTypes)
	        throws TinyDbException;

	public int[] executeBatchByMap(String sql,
			List<Map<String, Object>> parameters) throws TinyDbException;
	
}
