package org.tinygroup.tinydb.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("group-by-config")
public class GroupByConfig {

	@XStreamAlias("property-name")
	@XStreamAsAttribute
	private String propertyName;
	

	public GroupByConfig() {
		super();
	}

	public GroupByConfig(String propertyName) {
		super();
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
}
