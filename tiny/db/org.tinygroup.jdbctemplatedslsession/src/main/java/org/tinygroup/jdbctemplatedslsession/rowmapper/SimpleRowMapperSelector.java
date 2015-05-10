package org.tinygroup.jdbctemplatedslsession.rowmapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.tinygroup.jdbctemplatedslsession.RowMapperHolder;
import org.tinygroup.jdbctemplatedslsession.RowMapperSelector;

/**
 * 简单的RowMapper选择器
 * @author renhui
 *
 */
public class SimpleRowMapperSelector implements RowMapperSelector {
	
	private List<RowMapperHolder> holders=new ArrayList<RowMapperHolder>();
	
	public SimpleRowMapperSelector(){
		holders.add(new SingleColumnRowMapperHolder());
		holders.add(new BeanPropertyRowMapperHolder());
	}

	public RowMapper rowMapperSelector(Class requiredType) {
		for (RowMapperHolder holder : holders) {
			if(holder.isMatch(requiredType)){
				return holder.getRowMapper(requiredType);
			}
		}
	    throw new RuntimeException(String.format("类型:%s,获取不到相应的RowMapper实例", requiredType));
	}

	public List<RowMapperHolder> getHolders() {
		return holders;
	}

	public void setHolders(List<RowMapperHolder> holders) {
		this.holders = holders;
	}
	
}
