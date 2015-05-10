package org.tinygroup.jdbctemplatedslsession.rowmapper;

import org.springframework.beans.BeanWrapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.tinygroup.jdbctemplatedslsession.editor.AllowNullNumberEditor;

/**
 * 
 * @author renhui
 * 
 */
public class TinyBeanPropertyRowMapper extends BeanPropertyRowMapper {

	public TinyBeanPropertyRowMapper(Class requiredType) {
		super(requiredType);
	}

	@Override
	protected void initBeanWrapper(BeanWrapper bw) {
		bw.registerCustomEditor(byte.class, new AllowNullNumberEditor(
				Byte.class, true));
		bw.registerCustomEditor(short.class, new AllowNullNumberEditor(
				Short.class, true));
		bw.registerCustomEditor(int.class, new AllowNullNumberEditor(
				Integer.class, true));
		bw.registerCustomEditor(long.class, new AllowNullNumberEditor(
				Long.class, true));
		bw.registerCustomEditor(float.class, new AllowNullNumberEditor(
				Float.class, true));
		bw.registerCustomEditor(double.class, new AllowNullNumberEditor(
				Double.class, true));
	}

}
