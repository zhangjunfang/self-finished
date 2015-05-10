package org.tinygroup.dbrouter.context;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.dbrouter.RouterManager;
import org.tinygroup.dbrouter.StatementProcessor;
import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.dbrouter.util.DbRouterUtil;
import org.tinygroup.dbrouter.util.ParamObjectBuilder;

/**
 * statement执行的上下文
 * 
 * @author renhui
 * 
 */
public class StatementExecuteContext {

	protected RouterManager routerManager = RouterManagerBeanFactory
			.getManager();
	private Connection tinyConnection;
	private Statement tinyStatement;
	private String orignalSql;
	private Router router;
	private Partition partition;
	private ParamObjectBuilder builder;
	private boolean isRead;
	private List<RealStatementExecutor> statements;
	private List<ResultSetExecutor> resultSetExecutors;
	private StatementProcessor statementProcessor;

	private void readJudge() {
		if (!StringUtil.isBlank(orignalSql)) {
			if (DbRouterUtil.isQuerySql(orignalSql)) {
				isRead = true;
			}
		}
	}

	public Connection getTinyConnection() {
		return tinyConnection;
	}

	public Statement getTinyStatement() {
		return tinyStatement;
	}

	/**
	 * 是查询操作
	 * 
	 * @return
	 */
	public boolean isRead() {
		return isRead;
	}

	public void setTinyConnection(Connection tinyConnection) {
		this.tinyConnection = tinyConnection;
	}

	public void setTinyStatement(Statement tinyStatement) {
		this.tinyStatement = tinyStatement;
	}

	public Partition getPartition() {
		return partition;
	}

	public void setPartition(Partition partition) {
		this.partition = partition;
	}

	public String getOrignalSql() {
		return orignalSql;
	}

	public void setOrignalSql(String orignalSql) {
		this.orignalSql = orignalSql;
		readJudge();
	}

	public List<RealStatementExecutor> getRealStatements() {
		return statements;
	}

	public void setStatements(List<RealStatementExecutor> statements) {
		this.statements = statements;
	}

	public StatementProcessor getStatementProcessor() {
		return statementProcessor;
	}

	public void setStatementProcessor(StatementProcessor statementProcessor) {
		this.statementProcessor = statementProcessor;
	}

	public List<ResultSetExecutor> getResultSetExecutors() {
		return resultSetExecutors;
	}

	public void setResultSetExecutors(List<ResultSetExecutor> resultSetExecutors) {
		this.resultSetExecutors = resultSetExecutors;
	}

	public Router getRouter() {
		return router;
	}

	public void setRouter(Router router) {
		this.router = router;
	}

	public ParamObjectBuilder getBuilder() {
		return builder;
	}

	public void setBuilder(ParamObjectBuilder builder) {
		this.builder = builder;
	}

}
