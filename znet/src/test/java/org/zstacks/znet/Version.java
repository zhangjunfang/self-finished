/**
 * 
 */
package org.zstacks.znet;

import java.io.Serializable;
import java.util.Calendar;

/**
 * @author ocean
 *
 */
public class Version implements Serializable {

	private static final long serialVersionUID = -5935776119004478079L;
   
	private  String  number;
	
	private  Calendar  calendar;

	public Version() {
		super();
	}

	public Version(String number, Calendar calendar) {
		super();
		this.number = number;
		this.calendar = calendar;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	@Override
	public String toString() {
		return number;
	}
	
}
