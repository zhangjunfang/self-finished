package org.tinygroup.tinydb.config;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * bean的查询配置
 * @author renhui
 *
 */
@XStreamAlias("bean-query-config")
public class BeanQueryConfig {
    @XStreamAsAttribute
    @XStreamAlias("bean-type")
	private String beanType;
    @XStreamImplicit
    private List<ConditionConfig> conditionConfigs;
    @XStreamImplicit
    private List<GroupByConfig> groupByConfigs;
    @XStreamImplicit
    private List<OrderByConfig> orderByConfigs;

	public String getBeanType() {
		return beanType;
	}

	public void setBeanType(String beanType) {
		this.beanType = beanType;
	}

	public List<ConditionConfig> getConditionConfigs() {
		if(conditionConfigs==null){
			conditionConfigs=new ArrayList<ConditionConfig>();
		}
		return conditionConfigs;
	}

	public void setConditionConfigs(List<ConditionConfig> conditionConfigs) {
		this.conditionConfigs = conditionConfigs;
	}

	public List<GroupByConfig> getGroupByConfigs() {
		if(groupByConfigs==null){
			groupByConfigs=new ArrayList<GroupByConfig>();
		}
		return groupByConfigs;
	}

	public void setGroupByConfigs(List<GroupByConfig> groupByConfigs) {
		this.groupByConfigs = groupByConfigs;
	}

	public List<OrderByConfig> getOrderByConfigs() {
		if(orderByConfigs==null){
			orderByConfigs=new ArrayList<OrderByConfig>();
		}
		return orderByConfigs;
	}

	public void setOrderByConfigs(List<OrderByConfig> orderByConfigs) {
		this.orderByConfigs = orderByConfigs;
	}

}
