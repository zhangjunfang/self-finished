package org.tinygroup.dbrouter.context;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.dbrouter.config.Shard;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class RealStatementExecutor {
	private Statement realStatement;
	private String executeSql;
	private String originalSql;
	private Shard shard;
	private Partition partition;
	private Router router;
	private Object[] values;
	private Logger logger = LoggerFactory.getLogger(RealStatementExecutor.class);

	public RealStatementExecutor(Statement realStatement, String executeSql,
			String originalSql, Shard shard, Partition partition,Router router,Object[] values) {
		super();
		this.realStatement = realStatement;
		this.executeSql = executeSql;
		this.originalSql = originalSql;
		this.partition = partition;
		this.shard = shard;
		this.router=router;
		this.values=values;
	}

	public void addBatch() throws SQLException {
		if (realStatement instanceof PreparedStatement) {
			PreparedStatement prepared = (PreparedStatement) realStatement;
			prepared.addBatch();
		}else{
			realStatement.addBatch(executeSql);
		}
	}

	public Statement getRealStatement() {
		return realStatement;
	}

	public String getExecuteSql() {
		return executeSql;
	}

	public String getOriginalSql() {
		return originalSql;
	}

	public Shard getShard() {
		return shard;
	}

	public Partition getPartition() {
		return partition;
	}
	

	public Router getRouter() {
		return router;
	}
	
	public Object[] getValues() {
		return values;
	}

	public ResultSet executeQuery() throws SQLException {
		logger.logMessage(LogLevel.DEBUG, "分片:{0},原生sql:{1}", shard.getId(), originalSql);
		if (realStatement instanceof PreparedStatement) {
			PreparedStatement prepared = (PreparedStatement) realStatement;
			return prepared.executeQuery();
		}
		logger.logMessage(LogLevel.DEBUG, "分片:{0},真实执行的sql:{1}", shard.getId(), executeSql);
		return realStatement.executeQuery(executeSql);

	}

	public int executeUpdate() throws SQLException {
		logger.logMessage(LogLevel.DEBUG, "分片:{0},原生sql:{1}", shard.getId(), originalSql);
		if (realStatement instanceof PreparedStatement) {
			PreparedStatement prepared = (PreparedStatement) realStatement;
			return prepared.executeUpdate();
		}
		logger.logMessage(LogLevel.DEBUG, "分片:{0},真实执行的sql:{1}", shard.getId(), executeSql);
		return realStatement.executeUpdate(executeSql);
	}
}
