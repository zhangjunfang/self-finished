package org.tinygroup.database.config.table;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("index-field")
public class IndexField {

	@XStreamAsAttribute
	private String field;  //字段名
	
	@XStreamAsAttribute
	private String direction;  //asc,desc

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
	
}
