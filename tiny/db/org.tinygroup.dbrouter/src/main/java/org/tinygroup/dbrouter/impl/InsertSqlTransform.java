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
package org.tinygroup.dbrouter.impl;

import java.io.Serializable;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.tinygroup.dbrouter.config.Shard;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.insert.Insert;

public class InsertSqlTransform {
	
	private Insert originalInsert;
	
	private Shard firstShard;
	
	private DatabaseMetaData metaData;
	
	public InsertSqlTransform(Insert originalInsert,
			Shard firstShard, DatabaseMetaData metaData) {
		super();
		this.originalInsert = originalInsert;
		this.firstShard = firstShard;
		this.metaData = metaData;
	}
	
	public ColumnInfo getPrimaryColumn() throws SQLException{
		Table table = originalInsert.getTable();
		String tableName = table.getName();
		String queryTableName = tableName;
		ResultSet rs = null;
		ColumnInfo columnInfo = null;
		try {
			rs = metaData.getPrimaryKeys(null, null, tableName);
			if (rs.next()) {
				columnInfo=getPrimaryKeys(rs,table,queryTableName);
			} else {
				rs.close();// 先关闭上次查询的resultset
				queryTableName = tableName.toUpperCase();
				rs = metaData.getPrimaryKeys(null, null, queryTableName);
				if (rs.next()) {
					columnInfo=getPrimaryKeys(rs,table,queryTableName);
				}
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return columnInfo;
	}
	
	private  ColumnInfo getPrimaryKeys(ResultSet rs,Table table,String realTableName) throws SQLException {
		String primaryKey = rs.getString("COLUMN_NAME");
		ResultSet typeRs = metaData.getColumns(null, null, realTableName,
				primaryKey);
		ColumnInfo columnInfo=new ColumnInfo();
		columnInfo.setColumn(new Column(table, primaryKey));
		try {
			if (typeRs.next()) {
				 int dataType = typeRs.getInt("DATA_TYPE");
				 columnInfo.setDataType(dataType);
			}
		} finally {
			if (typeRs != null) {
				typeRs.close();
			}
		}
	    return columnInfo;
	}


	
	public class ColumnInfo implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -8427552991960248458L;
		private Column column;
		private int dataType;
		public Column getColumn() {
			return column;
		}
		public void setColumn(Column column) {
			this.column = column;
		}
		public int getDataType() {
			return dataType;
		}
		public void setDataType(int dataType) {
			this.dataType = dataType;
		}
		public String getColumnName() {
			if(column!=null){
				return column.getColumnName();
			}
			return null;
		}
	}

}
