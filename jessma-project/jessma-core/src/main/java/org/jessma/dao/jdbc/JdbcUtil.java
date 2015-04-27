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

package org.jessma.dao.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/**
 * 
 * JDBC 帮助类
 *
 */
public class JdbcUtil
{
	/** 关闭 {@link Connection}、{@link Statement}、{@link ResultSet} 等 JDBC SQL 对象 */
	public static final void closeSqlObject(Object ... sqlObjs)
	{
		try
		{
        		for(Object obj : sqlObjs)
        		{
        			if(obj == null)
        				continue;
        			
               			if(obj instanceof Connection)
        				((Connection)obj).close();
               			else if(obj instanceof Statement)				// PreparedStatement and CallableStatement are included !!
        				((Statement)obj).close();
               			else if(obj instanceof ResultSet)				// RowSet is included !!
        				((ResultSet)obj).close();
               			// else if(obj instanceof PreparedStatement)	// no need
               			// 	((PreparedStatement)obj).close();
              			// else if(obj instanceof CallableStatement)	// no need
               			// 	((CallableStatement)obj).close();
               			// else if(obj instanceof RowSet)				// no need
               			// 	((RowSet)obj).close();
         		}
		}
		catch(SQLException e)
		{
			throw new JdbcException(e);
		}
	}
	
	/**
	 * 
	 * 生成 {@link PreparedStatement} 或 {@link CallableStatement} 
	 * 
	 * @param conn			: {@link Connection} 对象
	 * @param sql			: SQL 语句
	 * @param isCallable	: 是否生成 {@link CallableStatement} 
	 * @return				  {@link PreparedStatement} 或 {@link CallableStatement}
	 * @throws SQLException
	 */
	public static final PreparedStatement prepareStatement(Connection conn, String sql, boolean isCallable) throws SQLException
	{
		if(!isCallable)
			return conn.prepareStatement(sql);
		else
			return conn.prepareCall(sql);
	}

	/**
	 * 
	 * 设置查询参数
	 * 
	 * @param pst		: {@link PreparedStatement}
	 * @param params	: 查询参数
	 * @throws SQLException
	 */
	public static final void setParameters(PreparedStatement pst, Object ... params) throws SQLException
	{
		setInputParameters(pst, 1, params);
	}
	
	/**
	 * 
	 * 设置输入参数
	 * 
	 * @param pst		: {@link PreparedStatement}
	 * @param startPos	: 起始索引
	 * @param params	: 查询参数
	 * @throws SQLException
	 */
	public static final void setInputParameters(PreparedStatement pst, int startPos, Object ... params) throws SQLException
	{
		for(int i = 0; i < params.length; i++)
		{
			Object o = params[i];
			
			if(o != null)
				pst.setObject(i + startPos, o);
			else
				pst.setNull(i + startPos, java.sql.Types.NULL);
		}
	}
	
	/**
	 * 
	 * 注册输出参数
	 * 
	 * @param cst		: {@link CallableStatement}
	 * @param startPos	: 起始索引
	 * @param types		: 参数类型数组（参考：{@link Types}）
	 * @throws SQLException
	 */
	public static final void registerOutputParameters(CallableStatement cst, int startPos, int[] types) throws SQLException
	{
		for(int i = 0; i < types.length; i++)
		{
			int t = types[i];
			cst.registerOutParameter(i + startPos, t);
		}
	}
}
