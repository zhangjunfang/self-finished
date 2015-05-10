package org.tinygroup.tinydb.config;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * bean的查询配置
 * @author renhui
 *
 */
@XStreamAlias("bean-query-configs")
public class BeanQueryConfigs {
	@XStreamImplicit
	private List<BeanQueryConfig> queryConfigs;

	public List<BeanQueryConfig> getQueryConfigs() {
		if(queryConfigs==null){
			queryConfigs=new ArrayList<BeanQueryConfig>();
		}
		return queryConfigs;
	}

	public void setQueryConfigs(List<BeanQueryConfig> queryConfigs) {
		this.queryConfigs = queryConfigs;
	}
}
