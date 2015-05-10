package org.tinygroup.dbrouter.context;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.tinygroup.dbrouter.RouterManager;
import org.tinygroup.dbrouter.config.DataSourceConfig;
import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.dbrouter.config.Shard;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.dbrouter.util.OrderByProcessor;
import org.tinygroup.dbrouter.util.OrderByProcessor.OrderByValues;
import org.tinygroup.dbrouter.util.SortOrder;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;
import org.tinygroup.jsqlparser.statement.select.Select;
import org.tinygroup.jsqlparser.statement.select.SelectBody;

/**
 * ResultSet的执行上下文
 * @author renhui
 *
 */
public class ResultSetExecutor {

	private ResultSet resultSet;

	private String executeSql;

	private String originalSql;

	private boolean isAfterLast;

	private boolean isBeforeFirst;

	private OrderByProcessor orderByProcessor;

	private Shard shard;
	
	private Partition partition;
	
	private Router router;

	private RouterManager routerManager = RouterManagerBeanFactory.getManager();

	public ResultSetExecutor(ResultSet resultSet, String executeSql,
			String originalSql, Shard shard,Partition partition,Router router) throws SQLException {
		super();
		this.resultSet = resultSet;
		this.executeSql = executeSql;
		this.originalSql = originalSql;
		this.shard = shard;
		this.router=router;
		this.partition=partition;
		org.tinygroup.jsqlparser.statement.Statement sqlStatement = routerManager
				.getSqlStatement(executeSql);
		if (sqlStatement instanceof Select) {
			Select select = (Select) sqlStatement;
			SelectBody body = select.getSelectBody();
			if (body instanceof PlainSelect) {
				PlainSelect plainSelect = (PlainSelect) body;
				orderByProcessor = new OrderByProcessor(plainSelect, resultSet);
			}
		} else {
			throw new RuntimeException("must be a query sql");
		}
	}

	public SortOrder getSortOrder() {
		if (orderByProcessor == null) {
			return null;
		}
		return orderByProcessor.getSortOrder();
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public String getExecuteSql() {
		return executeSql;
	}

	public boolean[] getOrderTypes() {
		if (orderByProcessor != null) {
			return orderByProcessor.getOrderTypes();
		}
		return null;
	}

	public int[] getOrderByIndexs() {
		if (orderByProcessor != null) {
			return orderByProcessor.getOrderByIndexs();
		}
		return null;
	}

	public boolean next() throws SQLException {
		if (!isAfterLast) {
			isBeforeFirst=false;
			return resultSet.next();
		}
		return false;

	}

	public boolean previous() throws SQLException {
		if (!isBeforeFirst) {
			isAfterLast=false;
			return resultSet.previous();
		}
		return false;
	}

	public OrderByValues getOrderByValuesFromResultSet() throws SQLException {
		orderByProcessor.setValues(resultSet);
		return orderByProcessor.getValueCache();
	}

	public OrderByValues getValueCache() {
		return orderByProcessor.getValueCache();
	}

	public void setValueCache(OrderByValues valueCache) {
		orderByProcessor.setValueCache(valueCache);
	}

	public boolean isAfterLast() {
		return isAfterLast;
	}

	public void setAfterLast(boolean isAfterLast) {
		this.isAfterLast = isAfterLast;
	}

	public boolean isBeforeFirst() {
		return isBeforeFirst;
	}

	public void setBeforeFirst(boolean isBeforeFirst) {
		this.isBeforeFirst = isBeforeFirst;
	}

	public void beforeFirst() throws SQLException {
		resultSet.beforeFirst();
		orderByProcessor.clearValueCache();
		isBeforeFirst = true;
		isAfterLast = false;
	}

	public void afterLast() throws SQLException {
		resultSet.afterLast();
		orderByProcessor.clearValueCache();
		isAfterLast = true;
		isBeforeFirst = false;
	}

	public void first() throws SQLException {
		resultSet.first();
		orderByProcessor.clearValueCache();
		isAfterLast = false;
		isBeforeFirst = false;
	}

	public void last() throws SQLException {
		resultSet.last();
		orderByProcessor.clearValueCache();
		isAfterLast = false;
		isBeforeFirst = false;
	}

	public Shard getShard() {
		return shard;
	}
	

	public Partition getPartition() {
		return partition;
	}

	public String getOriginalSql() {
		return originalSql;
	}

	public Router getRouter() {
		return router;
	}

	public DataSourceConfig getDataSourceConfig(){
		return router.getDataSourceConfig(shard.getDataSourceId());
	}
	
	
}