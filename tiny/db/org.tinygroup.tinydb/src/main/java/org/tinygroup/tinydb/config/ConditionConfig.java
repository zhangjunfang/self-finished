package org.tinygroup.tinydb.config;

import org.tinygroup.commons.tools.StringUtil;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("condition-config")
public class ConditionConfig {
	@XStreamAlias("property-name")
	@XStreamAsAttribute
	private String propertyName;
	@XStreamAlias("condition-mode")
	@XStreamAsAttribute
	private String conditionMode;
	private static final String DEFAUTL_CONDITION_MODE="equals";
	
	public ConditionConfig() {
		super();
	}
	public ConditionConfig(String propertyName, String conditionMode) {
		super();
		this.propertyName = propertyName;
		this.conditionMode = conditionMode;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getConditionMode() {
		if(StringUtil.isBlank(conditionMode)){
			conditionMode=DEFAUTL_CONDITION_MODE;
		}
		return conditionMode;
	}
	public void setConditionMode(String conditionMode) {
		this.conditionMode = conditionMode;
	}
	
}
