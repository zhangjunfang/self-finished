package org.tinygroup.dbrouter.impl.shardrule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.cache.Cache;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.dbrouter.ShardRule;
import org.tinygroup.dbrouter.cache.CacheKey;
import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.dbrouter.config.Shard;
import org.tinygroup.dbrouter.config.TableMapping;
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
 * 区间 shard
 * 
 * @author zhangjian
 *         <p/>
 *         shard处理数据的区间
 */
public class ShardRuleBySectionDifferentSchema implements ShardRule {

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
	 * 区间集合
	 */
	@XStreamAlias("sections")
	private List<Section> sections = new ArrayList<Section>();

	private Section[] sectionArray;
	/**
	 * 表名
	 */
	@XStreamAlias("table-mappings")
	private List<TableMapping> tableMappings;
	private transient Map<String, String> tableMappingMap = null;

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
		Statement statement = RouterManagerBeanFactory.getManager()
				.getSqlStatement(sql);
		if (DbRouterUtil.isSelect(statement)) {
			Cache cache = RouterManagerBeanFactory.getManager().getCache();
			CacheKey cacheKey = new CacheKey();
			cacheKey.update(tableName);
			cacheKey.update(fieldName);
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

	private boolean shardRuleMatch(Statement statement, Partition partition,
			Object... preparedParams) {
		ShardRuleMatchWithSections matchWithId = new ShardRuleMatchWithSections(
				sections, tableName, fieldName, partition, preparedParams);
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

	public String getReplacedSql(Partition partition, Shard shard, String sql) {
		if (!CollectionUtil.isEmpty(getTableMappings())) {
			return DbRouterUtil.transformSqlWithTableName(sql,
					getTableMappingMap());
		}
		return sql;
	}

	public Map<String, String> getTableMappingMap() {
		if (tableMappings != null && tableMappingMap == null) {
			tableMappingMap = new HashMap<String, String>();
			for (TableMapping mapping : tableMappings) {
				tableMappingMap.put(mapping.getTableName(),
						mapping.getShardTableName());
			}
		}
		return tableMappingMap;
	}

	public String getTableName() {
		return tableName;
	}

	public List<TableMapping> getTableMappings() {
		return tableMappings;
	}

}
