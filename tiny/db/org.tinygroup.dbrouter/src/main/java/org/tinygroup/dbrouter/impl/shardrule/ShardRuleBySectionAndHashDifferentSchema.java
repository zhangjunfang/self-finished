package org.tinygroup.dbrouter.impl.shardrule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.tinygroup.cache.Cache;
import org.tinygroup.dbrouter.ShardRule;
import org.tinygroup.dbrouter.cache.CacheKey;
import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.dbrouter.config.Shard;
import org.tinygroup.dbrouter.exception.DbrouterRuntimeException;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.dbrouter.util.ConsistentHash;
import org.tinygroup.dbrouter.util.DbRouterUtil;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.delete.Delete;
import org.tinygroup.jsqlparser.statement.insert.Insert;
import org.tinygroup.jsqlparser.statement.select.Select;
import org.tinygroup.jsqlparser.statement.update.Update;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 区间 shard+ hash
 * 
 * @author zhangjian
 */
public class ShardRuleBySectionAndHashDifferentSchema implements ShardRule {
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
	@XStreamAlias("field-Name")
	private String fieldName;
	/**
	 * 主键字段处理表达式，截取字符串
	 */
	@XStreamAsAttribute
	private String expression;
	/**
	 * 区间集合
	 */
	@XStreamAlias("sections")
	private List<Section> sections = new ArrayList<Section>();

	private Section[] sectionArray;

	private String targetTableName;
	/**
	 * 区间的hash节点数 ,配置规则 sectionTableStartindex,sectiontables
	 */
	@XStreamAsAttribute
	@XStreamAlias("table-param")
	private String tableParam;
	private transient ConsistentHash<String> consistentHash;

	private void sort(List<Section> sections) {
		if (null == sectionArray) {
			Collections.sort(sections, new SectionComparator());
			sectionArray = sections.toArray(new Section[0]);
		}
	}

	class SectionComparator implements Comparator<Section> {
		public int compare(Section o1, Section o2) {
			return (o1.getStart() == o2.getStart() ? 0 : (o1.getStart() < o2
					.getStart()) ? -1 : 1);
		}

	}

	public boolean isMatch(Partition partition, Shard shard, String sql,
			Object... preparedParams) {
		sort(sections);
		inittargetTable(shard);
		initHash();
		Statement statement = RouterManagerBeanFactory.getManager()
				.getSqlStatement(sql);
		if (DbRouterUtil.isSelect(statement)) {
			Cache cache = RouterManagerBeanFactory.getManager().getCache();
			CacheKey cacheKey = new CacheKey();
			cacheKey.update(fieldName);
			cacheKey.update(tableName);
			cacheKey.update(targetTableName);
			cacheKey.update(sectionArray);
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

	private void inittargetTable(Shard shard) {
		Map<String, String> tableMapping = shard.getTableMappingMap();
		if (null == targetTableName && null != tableMapping) {
			targetTableName = tableMapping.get(tableName);
			targetTableName = (null == targetTableName || ""
					.equals(targetTableName.trim())) ? tableName
					: targetTableName;
		} else {
			if (null == targetTableName || "".equals(targetTableName.trim())) {
				targetTableName = tableName;
			}
		}
	}

	private boolean shardRuleMatch(Statement statement, Partition partition,
			Object... preparedParams) {

		ShardRuleMatchWithSectionAndHash matchWithId = new ShardRuleMatchWithSectionAndHash(
				sections, tableName, targetTableName, consistentHash,
				fieldName, expression, partition, preparedParams);
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

	private void initHash() {
		if (null == consistentHash) {
			Collection<String> nodes = new ArrayList<String>();
			if (null != tableParam && !"".equals(tableParam.trim())) {
				String[] params = tableParam.split(",");
				int tableIndex = Integer.parseInt(params[0]);
				for (int i = 0; i < Integer.parseInt(params[1]); i++) {
					nodes.add(tableName + tableIndex);
					tableIndex++;
				}
			} else {
				if (null != targetTableName
						&& !"".equals(targetTableName.trim())) {
					nodes.add(targetTableName);
				} else {
					nodes.add(tableName);
				}
			}

			if (!nodes.contains(targetTableName)) {
				throw new DbrouterRuntimeException("ShardRule["
						+ this.toString() + "] error");
			}

			consistentHash = new ConsistentHash<String>(nodes);
		}
	}

	public String getReplacedSql(Partition partition, Shard shard, String sql) {
		if (null != shard.getTableMappingMap()) {
			return DbRouterUtil.transformSqlWithTableName(sql,
					shard.getTableMappingMap());
		}
		return sql;
	}

	@Override
	public String toString() {
		return "ShardRuleBySectionAndHashDifferentSchema [tableName="
				+ tableName + ", fieldName=" + fieldName + ", expression="
				+ expression + ", sections=" + sections + ", targetTableName="
				+ targetTableName + ", tableParam=" + tableParam + "]";
	}

}
