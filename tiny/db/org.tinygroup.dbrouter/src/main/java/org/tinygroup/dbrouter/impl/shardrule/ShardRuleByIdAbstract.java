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
package org.tinygroup.dbrouter.impl.shardrule;

import org.tinygroup.cache.Cache;
import org.tinygroup.dbrouter.ShardRule;
import org.tinygroup.dbrouter.cache.CacheKey;
import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.dbrouter.config.Shard;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.dbrouter.util.DbRouterUtil;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.delete.Delete;
import org.tinygroup.jsqlparser.statement.insert.Insert;
import org.tinygroup.jsqlparser.statement.select.Select;
import org.tinygroup.jsqlparser.statement.update.Update;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by luoguo on 13-12-17.
 */
public abstract class ShardRuleByIdAbstract implements ShardRule {
	/**
	 * 余数
	 */
	@XStreamAsAttribute
	private long remainder;
	/**
	 * 表名
	 */
	@XStreamAsAttribute
	@XStreamAlias("table-name")
	private String tableName;
	/**
	 * 主键字段
	 */
	@XStreamAsAttribute
	@XStreamAlias("primary-key-field-name")
	private String primaryKeyFieldName;

	public ShardRuleByIdAbstract() {

	}

	public ShardRuleByIdAbstract(String tableName, String primaryKeyFieldName,
			int remainder) {
		this.tableName = tableName;
		this.primaryKeyFieldName = primaryKeyFieldName;
		this.remainder = remainder;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPrimaryKeyFieldName() {
		return primaryKeyFieldName;
	}

	public void setPrimaryKeyFieldName(String primaryKeyFieldName) {
		this.primaryKeyFieldName = primaryKeyFieldName;
	}

	public long getRemainder() {
		return remainder;
	}

	public void setRemainder(int remainder) {
		this.remainder = remainder;
	}

	public boolean isMatch(Partition partition, Shard shard, String sql,
			Object... preparedParams) {
		Statement statement = RouterManagerBeanFactory.getManager()
				.getSqlStatement(sql);
		if (DbRouterUtil.isSelect(statement)) {
			Cache cache = RouterManagerBeanFactory.getManager().getCache();
			CacheKey cacheKey = new CacheKey();
			cacheKey.update(tableName);
			cacheKey.update(primaryKeyFieldName);
			cacheKey.update(remainder);
			cacheKey.update(sql);
			for (Object param : preparedParams) {
				cacheKey.update(param);
			}
			Boolean match = null;
			try {
				match = (Boolean) cache.get(cacheKey.toString());
			} catch (Exception e) {
			}
			if (match == null) {
				match = shardRuleMatch(statement, partition, preparedParams);
				cache.put(cacheKey.toString(), match);
			}
			return match;
		}
		return shardRuleMatch(statement, partition, preparedParams);

	}

	private boolean shardRuleMatch(Statement statement, Partition partition,
			Object... preparedParams) {
		ShardRuleMatchWithId matchWithId = new ShardRuleMatchWithId(remainder,
				tableName, primaryKeyFieldName, partition, preparedParams);
		if (statement instanceof Insert) {
			return matchWithId.insertMatch(statement);
		} else if (statement instanceof Delete) {
			return matchWithId.deleteMatch(statement);
		} else if (statement instanceof Update) {
			return matchWithId.updateMatch(statement);
		} else if (statement instanceof Select) {
			return matchWithId.selectMatch(statement);
		}
		return false;
	}
}
