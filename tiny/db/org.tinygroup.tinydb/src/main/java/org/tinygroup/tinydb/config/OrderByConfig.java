package org.tinygroup.tinydb.config;

import org.tinygroup.commons.tools.StringUtil;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 
 * @author renhui
 *
 */
@XStreamAlias("order-by-config")
public class OrderByConfig {
    
	@XStreamAlias("property-name")
	@XStreamAsAttribute
	private String propertyName;
	
	@XStreamAsAttribute
	@XStreamAlias("order-mode")
	String orderMode;
	private static final String DEFAULT_ORDER_MODE="asc";
	
	public OrderByConfig() {
		super();
	}

	public OrderByConfig(String propertyName, String orderMode) {
		super();
		this.propertyName = propertyName;
		this.orderMode = orderMode;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getOrderMode() {
		if(StringUtil.isBlank(orderMode)){
			orderMode=DEFAULT_ORDER_MODE;
		}
		return orderMode;
	}

	public void setOrderMode(String orderMode) {
		this.orderMode = orderMode;
	}
	
}
