package org.tinygroup.jdbctemplatedslsession;

import org.springframework.jdbc.core.RowMapper;

/**
 * RowMapper实例持有接口
 * @author renhui
 *
 */
public interface RowMapperHolder {
	/**
	 * 根据类型进行匹配
	 * @param requiredType
	 * @return
	 */
	public boolean isMatch(Class requiredType);
	
	/**
	 * 返回该选择器对应的RowMapper
	 * @param requiredType
	 * @return
	 */
	public RowMapper getRowMapper(Class requiredType);

}
