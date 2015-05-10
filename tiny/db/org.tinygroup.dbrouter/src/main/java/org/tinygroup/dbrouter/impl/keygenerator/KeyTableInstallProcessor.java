package org.tinygroup.dbrouter.impl.keygenerator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.tinygroup.dbrouter.config.DataSourceConfig;
import org.tinygroup.dbrouter.config.KeyTable;
import org.tinygroup.dbrouter.exception.DbrouterRuntimeException;
import org.tinygroup.dbrouter.util.DbRouterUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class KeyTableInstallProcessor {

	private String tableName;
	
	private KeyTable keyTable;
	
	private DataSourceConfig dataSourceConfig;
	
	private static Logger logger = LoggerFactory
	.getLogger(KeyTableInstallProcessor.class);
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public KeyTable getKeyTable() {
		return keyTable;
	}

	public void setKeyTable(KeyTable keyTable) {
		this.keyTable = keyTable;
	}

	public DataSourceConfig getDataSourceConfig() {
		return dataSourceConfig;
	}

	public void setDataSourceConfig(DataSourceConfig dataSourceConfig) {
		this.dataSourceConfig = dataSourceConfig;
	}

	public int getOrder() {
		return 0;
	}

	public void process(String language) { 
		Connection conn=null;
		try{
			conn = DbRouterUtil.createConnection(dataSourceConfig);
			if(!checkTableExist(language,conn)){
				createkeyTable(conn);
				logger.logMessage(LogLevel.DEBUG, "创建主键表{0}成功.", tableName);
			}else{
				logger.logMessage(LogLevel.DEBUG, "主键表{0}已经存在，无需创建.", tableName);
			}
		}catch(SQLException e){
			throw new DbrouterRuntimeException(String.format("创建主键表%s发生异常:",tableName),e);
		}finally{
			
			if(conn!=null){
			   try {
				 conn.close();
			   } catch (SQLException e) {
				
			   }
			}
		}
	}
	
	private boolean checkTableExist(String language,Connection conn) throws SQLException{
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet resultset = null;
		try{
			resultset = meta.getTables(conn.getCatalog(), meta.getUserName(),
					tableName, new String[] { "TABLE" });
			
			return resultset.next();
		}finally{
			if(resultset!=null){
			   resultset.close();
			}
		}
	}
	
	private void createkeyTable(Connection conn) throws SQLException{
		Statement st =null;
		try{
			st = conn.createStatement();
			st.execute(keyTable.getRealSql(tableName));
		}finally{
			if(st!=null){
				  try {
				     st.close();
				  } catch (SQLException e) {
						
				  }
			}
		}
		
	}

	public List<String> getDealSqls(String language, Connection con)
			throws SQLException {
		List<String> sqls=new ArrayList<String>();
		sqls.add(keyTable.getRealSql(tableName));
		return sqls;
	}

}
