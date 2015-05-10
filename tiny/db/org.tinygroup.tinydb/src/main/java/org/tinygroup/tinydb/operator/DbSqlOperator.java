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

import java.util.List;
import java.util.Map;

import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.Pager;
import org.tinygroup.tinydb.exception.TinyDbException;

/**
 * DB相关的批量操作
 *
 * @author luoguo
 */
public interface DbSqlOperator<K> {

    // 下面是根据SQL
    Bean[] getBeans(String sql)throws TinyDbException;
    
    Bean[] getBeans(String sql, Object... parameters)throws TinyDbException;

    Bean[] getBeans(String sql, List<Object> parameters)throws TinyDbException;

    Bean[] getBeans(String sql, Map<String, Object> parameters)throws TinyDbException;
    
    Bean[] getPageBeans(String sql, int start, int limit)throws TinyDbException;
    
    Bean[] getCursorPageBeans(String sql, int start, int limit)throws TinyDbException;
    
    Bean[] getDialectPageBeans(String sql, int start, int limit)throws TinyDbException;
    
    Bean[] getPageBeans(String sql, int start, int limit,Object... parameters)throws TinyDbException;
    
    Bean[] getCursorPageBeans(String sql, int start, int limit,Object... parameters)throws TinyDbException;
    
    Bean[] getDialectPageBeans(String sql, int start, int limit,Object... parameters)throws TinyDbException;
    
    Bean[] getPageBeans(String sql, int start, int limit,List<Object> parameters)throws TinyDbException;
    
    Bean[] getCursorPageBeans(String sql, int start, int limit,List<Object> parameters)throws TinyDbException;
    
    Bean[] getDialectPageBeans(String sql, int start, int limit,List<Object> parameters)throws TinyDbException;
    
    Bean[] getPageBeans(String sql, int start, int limit,Map<String, Object> parameters)throws TinyDbException;
    
    Bean[] getCursorPageBeans(String sql, int start, int limit,Map<String, Object> parameters)throws TinyDbException;
    
    Bean[] getDialectPageBeans(String sql, int start, int limit,Map<String, Object> parameters)throws TinyDbException;

    // 读取单一个值
    Bean getSingleValue(String sql)throws TinyDbException;

    Bean getSingleValue(String sql, Map<String, Object> parameters)throws TinyDbException;

    Bean getSingleValue(String sql, Object... parameters)throws TinyDbException;

    Bean getSingleValue(String sql, List<Object> parameters)throws TinyDbException;

    /**
     * 执行带参数的sql语句
     *
     * @param sql
     * @param parameters
     * @return
     */
    int execute(String sql, Map<String, Object> parameters)throws TinyDbException;

    /**
     * 执行带参数的sql语句
     *
     * @param sql
     * @param parameters
     * @return
     */
    int execute(String sql, Object... parameters)throws TinyDbException;

    int execute(String sql, List<Object> parameters)throws TinyDbException;

    //查询总记录数
    int account(String sql, Object... parameters)throws TinyDbException;

    int account(String sql, List<Object> parameters)throws TinyDbException;

    int account(String sql, Map<String, Object> parameters)throws TinyDbException;
    /**
     * 根据bean对象查询该bean对象的记录数
     * @param bean
     * @return
     */
    int account(Bean bean)throws TinyDbException;
    
    //返回分页结果的分页接口
    Pager getPager(String sql, int start, int limit)throws TinyDbException;
    
    Pager getPager(String sql, int start, int limit,Object... parameters)throws TinyDbException;
    
    Pager getPager(String sql, int start, int limit,List<Object> parameters)throws TinyDbException;
    
    Pager getPager(String sql, int start, int limit,Map<String, Object> parameters)throws TinyDbException;
    
    /**
	 * 分页查询bean
	 * 
	 * @param bean
	 * @param start
	 * @param limit
	 * @return
	 */
	Bean[] getBeans(Bean bean, int start, int limit) throws TinyDbException;
    
    /**
	 * 分页查询bean
	 * 
	 * @param bean
	 * @param start
	 * @param limit
	 * @return
	 */
	Pager getPager(Bean bean, int start, int limit) throws TinyDbException;
}
