/**
 * 
 */
package com.transilink.znet;

import java.io.Serializable;
import java.util.Calendar;

/**
 * @author ocean
 *
 */
public class Group implements Serializable {

	private static final long serialVersionUID = 5511128043480860427L;

	private String name;

	private Calendar calendar;

	public Group() {
		super();
	}

	public Group(String name, Calendar calendar) {
		super();
		this.name = name;
		this.calendar = calendar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	@Override
	public String toString() {
		return name;
	}
}
