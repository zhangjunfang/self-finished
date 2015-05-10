package org.tinygroup.jdbctemplatedslsession;

import org.springframework.jdbc.core.RowMapper;

/**
 * 根据class类型选择RowMapper
 * @author renhui
 *
 */
public interface RowMapperSelector {
    /**
     * 根据class类型，获取RowMapper
     * @param requiredType
     * @return
     */
	RowMapper rowMapperSelector(Class requiredType);
}
