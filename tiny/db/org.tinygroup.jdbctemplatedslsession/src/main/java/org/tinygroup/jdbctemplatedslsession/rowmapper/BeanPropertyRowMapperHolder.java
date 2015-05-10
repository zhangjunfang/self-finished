package org.tinygroup.jdbctemplatedslsession.rowmapper;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.tinygroup.jdbctemplatedslsession.RowMapperHolder;

/**
 * BeanPropertyRowMapper的选择器
 * @author renhui
 *
 */
public class BeanPropertyRowMapperHolder implements RowMapperHolder {

	public boolean isMatch(Class requiredType) {
		try {
			BeanUtils.instantiateClass(requiredType);
		} catch (BeanInstantiationException e) {
			return false;
		}
		return true;
	}

	public RowMapper getRowMapper(Class requiredType) {
		return new TinyBeanPropertyRowMapper(requiredType);
	}

}
