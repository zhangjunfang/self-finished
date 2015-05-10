package org.tinygroup.jdbctemplatedslsession.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.tinygroup.jdbctemplatedslsession.RowMapperHolder;

/**
 * SingleColumnRowMapper的选择器
 * @author renhui
 *
 */
public class SingleColumnRowMapperHolder implements RowMapperHolder{

	public boolean isMatch(Class requiredType) {
		return String.class.equals(requiredType)||Number.class.isAssignableFrom(requiredType);
	}

	public RowMapper getRowMapper(Class requiredType) {
		return new SingleColumnRowMapper(requiredType);
	}

}
