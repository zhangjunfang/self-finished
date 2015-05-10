package org.tinygroup.exception.dao;

import java.util.List;

import org.tinygroup.exception.pojo.Custom;

public interface CustomDao {

	public Custom insertCustom(Custom custom);

	public int updateCustom(Custom custom);

	public int deleteCustom(String id);

	public Custom getCustomById(String id);

	public List<Custom> queryCustom(Custom custom);

}
