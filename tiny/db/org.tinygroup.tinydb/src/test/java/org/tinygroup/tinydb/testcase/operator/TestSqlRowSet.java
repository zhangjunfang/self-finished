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
package org.tinygroup.tinydb.testcase.operator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.test.BaseTest;

public class TestSqlRowSet extends BaseTest{
	
	
	public void testGetSqlRowSet() throws TinyDbException{
		
		Bean[] beans = getBeans(20);
		beans=getOperator().batchInsert(beans);
		String sql="select * from animal";
		SqlRowSet rowSet= getOperator().getSqlRowSet(sql);
		if(rowSet.next()){
			assertEquals("testSql", rowSet.getString("name"));
		}
		sql="select * from animal where name=?";
		rowSet= getOperator().getSqlRowSet(sql,"testSql");
		if(rowSet.next()){
			assertEquals("testSql", rowSet.getString("name"));
		}
		List<Object> paramList=new ArrayList<Object>();
		paramList.add("testSql");
		rowSet=getOperator().getSqlRowSet(sql, paramList);
		if(rowSet.next()){
			assertEquals("testSql", rowSet.getString("name"));
		}
		sql="select * from animal where name=@name";
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("name", "testSql");
		rowSet=getOperator().getSqlRowSet(sql, paramMap);
		if(rowSet.next()){
			assertEquals("testSql", rowSet.getString("name"));
		}
		getOperator().batchDelete(beans);
	}
	

}
