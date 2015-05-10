package org.tinygroup.jdbctemplatedslsession;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.jdbctemplatedslsession.rowmapper.SimpleRowMapperSelector;
import org.tinygroup.tinysqldsl.ComplexSelect;
import org.tinygroup.tinysqldsl.Delete;
import org.tinygroup.tinysqldsl.DslSession;
import org.tinygroup.tinysqldsl.Insert;
import org.tinygroup.tinysqldsl.Select;
import org.tinygroup.tinysqldsl.Update;

/**
 * DslSqlSession接口的jdbctemplate版实现
 * 
 * @author renhui
 * 
 */
public class SimpleDslSession implements DslSession {

	private JdbcTemplate jdbcTemplate = new JdbcTemplate();
	private RowMapperSelector selector = new SimpleRowMapperSelector();

	public SimpleDslSession(DataSource dataSource) {
		jdbcTemplate.setDataSource(dataSource);
	}

	public RowMapperSelector getSelector() {
		return selector;
	}

	public void setSelector(RowMapperSelector selector) {
		this.selector = selector;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public int execute(Insert insert) {
		return jdbcTemplate.update(insert.sql(), insert.getValues().toArray());
	}

	public int execute(Update update) {
		return jdbcTemplate.update(update.sql(), update.getValues().toArray());
	}

	public int execute(Delete delete) {
		return jdbcTemplate.update(delete.sql(), delete.getValues().toArray());
	}

	@SuppressWarnings("unchecked")
	public <T> T fetchOneResult(Select select, Class<T> requiredType) {
		return (T) jdbcTemplate.queryForObject(select.sql(), select.getValues()
				.toArray(), selector.rowMapperSelector(requiredType));
	}

	@SuppressWarnings("unchecked")
	public <T> T[] fetchArray(Select select, Class<T> requiredType) {
		List<T> records = fetchList(select, requiredType);
		if (!CollectionUtil.isEmpty(records)) {
			return (T[]) records.toArray();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> fetchList(Select select, Class<T> requiredType) {
		return jdbcTemplate.query(select.toString(), select.getValues()
				.toArray(), selector.rowMapperSelector(requiredType));
	}

	@SuppressWarnings("unchecked")
	public <T> T[] fetchArray(ComplexSelect complexSelect, Class<T> requiredType) {
		List<T> records = fetchList(complexSelect, requiredType);
		if (!CollectionUtil.isEmpty(records)) {
			return (T[]) records.toArray();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> fetchList(ComplexSelect complexSelect,
			Class<T> requiredType) {
		return jdbcTemplate.query(complexSelect.toString(), complexSelect
				.getValues().toArray(), selector
				.rowMapperSelector(requiredType));
	}

	@SuppressWarnings("unchecked")
	public <T> T fetchOneResult(ComplexSelect complexSelect,
			Class<T> requiredType) {
		return (T) jdbcTemplate.queryForObject(complexSelect.sql(),
				complexSelect.getValues().toArray(),
				selector.rowMapperSelector(requiredType));
	}

}
