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
import java.util.ArrayList;
import java.util.List;

import org.jessma.dao.AbstractFacade;
import org.jessma.util.KV;


/**
 * 
 * JDBC Facade 实现类
 *
 */
public abstract class JdbcFacade extends AbstractFacade<AbstractJdbcSessionMgr, Connection>
{
	protected JdbcFacade(AbstractJdbcSessionMgr mgr)
	{
		super(mgr);
	}

	/* ************************************************************************************************** */
	/* ******************************************* 业务方法 ******************************************* */

	/**
	 * 
	 * 执行 SQL 查询。
	 * 
	 * @param sql			: 查询语句
	 * @param params		: 查询参数
	 * @return				  结果集
	 * 
	 */
	protected List<Object[]> query(String sql, Object ... params)
	{
		return query(false, sql, params);
	}
	
	/**
	 * 
	 * 执行 SQL 查询。
	 * 
	 * @param isCallable	: 是否使用 CallableStatment 执行查询
	 * @param sql			: 查询语句
	 * @param params		: 查询参数
	 * @return				  结果集
	 * 
	 */
	protected List<Object[]> query(boolean isCallable, String sql, Object ... params)
	{
		List<Object[]> result = new ArrayList<Object[]>();
		
		ResultSet rs = null;
		PreparedStatement pst = null;

		try
		{
			Connection conn = getSession();
			pst = JdbcUtil.prepareStatement(conn, sql, isCallable);
			JdbcUtil.setParameters(pst, params);
			rs = pst.executeQuery();
			int cols = rs.getMetaData().getColumnCount();
			
			while(rs.next())
			{
				Object[] objs = new Object[cols];
    			for(int i = 0; i < cols; i++)
    				objs[i] = rs.getObject(i + 1);
    				
    			result.add(objs);
			}
		}
		catch (SQLException e)
		{
			throw new JdbcException(e);
		}
		finally
		{
			JdbcUtil.closeSqlObject(pst, rs);
		}
		
		return result;
	}
	
	/**
	 * 
	 * 执行存储过程。
	 * 
	 * @param sql			: 查询语句
	 * @param inParams		: 输入参数
	 * @return				  执行结果（成功或失败）
	 * 
	 */
	protected boolean call(String sql, Object ... inParams)
	{
		return call(1, new int[0], sql, inParams).getKey();
	}
	
	/**
	 * 
	 * 执行存储过程。
	 * 
	 * @param outParamPos	: 输出参数的位置（从 1 开始计算）
	 * @param outParaType	: 输出参数类型（参考：java.sql.Types）
	 * @param sql			: 查询语句
	 * @param inParams		: 输入参数
	 * @return				  执行结果（Key: 成功或失败，Value: 如果成功则包含输出参数）
	 * 
	 */
	protected KV<Boolean, Object> call(int outParamPos, int outParaType, String sql, Object ... inParams)
	{
		KV<Boolean, Object[]> keys = call(outParamPos, new int[] {outParaType}, sql, inParams);
		Object[] values = keys.getValue();
		Object value = values != null && values.length > 0 ? values[0] : null;
		
		return new KV<Boolean, Object>(keys.getKey(), value);
	}
	
	/**
	 * 
	 * 执行存储过程。
	 * 
	 * @param outParamStartPos	: 输出参数的起始位置（从 1 开始计算）
	 * @param outParamTypes		: 输出参数类型集合（参考：java.sql.Types）
	 * @param sql				: 查询语句
	 * @param inParams			: 输入参数
	 * @return				  	  执行结果（Key: 成功或失败，Value: 如果成功则包含输出参数集合）
	 * 
	 */
	protected KV<Boolean, Object[]> call(int outParamStartPos, int[] outParamTypes, String sql, Object ... inParams)
	{
		KV<Boolean, Object[]> result = new KV<Boolean, Object[]>();
		
		ResultSet rs = null;
		CallableStatement cst = null;

		try
		{
			Connection conn = getSession();
			cst = (CallableStatement)JdbcUtil.prepareStatement(conn, sql, true);
			int inputParameterStartPosition = outParamStartPos == 1 ? outParamStartPos + outParamTypes.length : 1;
			JdbcUtil.setInputParameters(cst, inputParameterStartPosition, inParams);
			JdbcUtil.registerOutputParameters(cst, outParamStartPos, outParamTypes);
			
			result.setKey(cst.execute());
			
			if(result.getKey() && outParamTypes.length > 0)
			{
				rs = cst.getResultSet();
				
				if(rs.next())
				{
					Object[] objs = new Object[outParamTypes.length];
	    			for(int i = 0; i < objs.length; i++)
	    				objs[i] = rs.getObject(i + outParamStartPos);
	    			
	    			result.setValue(objs);
				}
			}
		}
		catch (SQLException e)
		{
			throw new JdbcException(e);
		}
		finally
		{
			JdbcUtil.closeSqlObject(cst, rs);
		}
		
		return result;
	}
	
	/**
	 * 
	 * 执行 SQL 更新。
	 * 
	 * @param sql		: 查询语句
	 * @param params	: 查询参数
	 * @return			  更新结果
	 * 
	 */
	protected int update(String sql, Object ... params)
	{
		return update(false, sql, params);
	}
	
	/**
	 * 
	 * 执行 SQL 更新。
	 * 
	 * @param isCallable	: 是否使用 CallableStatment 执行更新
	 * @param sql			: 查询语句
	 * @param params		: 查询参数
	 * @return				  更新结果
	 * 
	 */
	protected int update(boolean isCallable, String sql, Object ... params)
	{
		int effect = 0;
		PreparedStatement pst = null;

		try
		{
			Connection conn = getSession();
			pst = JdbcUtil.prepareStatement(conn, sql, isCallable);
			JdbcUtil.setParameters(pst, params);
			effect = pst.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new JdbcException(e);
		}
		finally
		{
			JdbcUtil.closeSqlObject(pst);
		}
	
		return effect;
	}

	/**
	 * 
	 * 执行 SQL 更新并返回自增字段值。
	 * 
	 * @param sql		: 查询语句
	 * @param params	: 查询参数
	 * @return			  更新结果（Key: 影响的行数，Value: 返回的自增字段值集合）
	 * 
	 */
	protected KV<Integer, List<Object>> updateAndGenerateKeys(String sql, Object ... params)
	{
		KV<Integer, List<Object[]>> keys = updateAndGenerateKeys(null, sql, params);
		
		List<Object[]> values = keys.getValue();
		KV<Integer, List<Object>> result = new KV<Integer, List<Object>>(keys.getKey(), null);
		
		if(values != null && values.size() > 0)
		{
			List<Object> v = new ArrayList<Object>();
			result.setValue(v);
			
			for(Object[] objs : values)
				v.add(objs[0]);
		}
		
		return result;
	}

	/**
	 * 
	 * 执行 SQL 更新并返回自增字段值。
	 * 
	 * @param keyColIndexes	: 自增字段索引集合
	 * @param sql			: 查询语句
	 * @param params		: 查询参数
	 * @return				  更新结果（Key: 影响的行数，Value: 返回的自增字段值集合）
	 * 
	 */
	protected KV<Integer, List<Object[]>> updateAndGenerateKeys(int[] keyColIndexes, String sql, Object ... params)
	{
		KV<Integer, List<Object[]>> result = new KV<Integer, List<Object[]>>();
		
		ResultSet rs = null;
		PreparedStatement pst = null;

		try
		{
			Connection conn = getSession();
			
			if(keyColIndexes == null)
				pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			else
				pst = conn.prepareStatement(sql, keyColIndexes);
			
			JdbcUtil.setParameters(pst, params);
			result.setKey(pst.executeUpdate());
			
			if(result.getKey() > 0)
			{
				List<Object[]> value = new ArrayList<Object[]>();
				result.setValue(value);

				rs = pst.getGeneratedKeys();
				while(rs.next())
				{					
					if(keyColIndexes == null)
						value.add(new Object[] {rs.getObject(1)});
					else
					{
						Object[] keys = new Object[keyColIndexes.length];
						
						for(int i = 0; i < keys.length; i++)
							keys[i] = rs.getObject(i + 1);
						
						value.add(keys);
					}
				}
			}
		}
		catch (SQLException e)
		{
			throw new JdbcException(e);
		}
		finally
		{
			JdbcUtil.closeSqlObject(pst, rs);
		}
		
		return result;
	}

	/**
	 * 
	 * 执行 SQL 更新。
	 * 
	 * @param sql		: 查询语句
	 * @param params	: 查询参数（List 的每一个元素组成一个 batch）
	 * @return			  影响的行数集
	 * 
	 */
	protected int[] updateBatch(String sql, List<Object[]> params)
	{
		return updateBatch(false, sql, params);
	}
	
	/**
	 * 
	 * 执行 SQL 更新。
	 * 
	 * @param sql			: 查询语句
	 * @param isCallable	: 是否使用 CallableStatment 执行查询
	 * @param params		: 查询参数（List 的每一个元素组成一个 batch）
	 * @return				  影响的行数集
	 * 
	 */
	protected int[] updateBatch(boolean isCallable, String sql, List<Object[]> params)
	{
		return updateBatch(isCallable, sql, params, 0);
	}
	
	/**
	 * 
	 * 执行 SQL 更新。
	 * 
	 * @param sql			: 查询语句
	 * @param params		: 查询参数（List 的每一个元素组成一个 batch）
	 * @param batchSize	: 分批执行 batchUpdate 时，每批 batch 的数目（默认：params 的元素个数）
	 * @return				  影响的行数集
	 * 
	 */
	protected int[] updateBatch(String sql, List<Object[]> params, int batchSize)
	{
		return updateBatch(false, sql, params, batchSize);
	}
	
	/**
	 * 
	 * 执行 SQL 更新。
	 * 
	 * @param isCallable	: 是否使用 CallableStatment 执行查询
	 * @param sql			: 查询语句
	 * @param params		: 查询参数（List 的每一个元素组成一个 batch）
	 * @param batchSize	: 分批执行 batchUpdate 时，每批 batch 的数目（默认：params 的元素个数）
	 * @return				  影响的行数集
	 * 
	 */
	protected int[] updateBatch(boolean isCallable, String sql, List<Object[]> params, int batchSize)
	{
		PreparedStatement pst = null;
		
		int i			= 0;
		int start		= 0;
		int size		= params.size();
		int[] effect	= new int[size];
		
		if(size == 0) return effect;
		if(batchSize <= 0) batchSize = size;

		try
		{
			Connection conn = getSession();
			pst = JdbcUtil.prepareStatement(conn, sql, isCallable);
			
			while(i < size)
			{
				Object[] objects = params.get(i);
				JdbcUtil.setParameters(pst, objects);
				pst.addBatch();
				
				if(++i % batchSize == 0)
				{
					System.arraycopy(pst.executeBatch(), 0, effect, start, i - start);
					pst.clearBatch();
					start = i; 
				}
			}
			
			if(i % batchSize != 0)
				System.arraycopy(pst.executeBatch(), 0, effect, start, i - start);
		}
		catch (SQLException e)
		{
			throw new JdbcException(e);
		}
		finally
		{
			JdbcUtil.closeSqlObject(pst);
		}
	
		return effect;
	}
}
