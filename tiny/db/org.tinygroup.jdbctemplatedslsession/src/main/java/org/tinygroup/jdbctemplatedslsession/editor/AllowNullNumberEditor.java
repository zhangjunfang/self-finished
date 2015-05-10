package org.tinygroup.jdbctemplatedslsession.editor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.propertyeditors.CustomNumberEditor;

/**
 * 基本类型允许是null值，如果是空值，转换成基本类型的默认值
 * @author renhui
 *
 */
public class AllowNullNumberEditor extends CustomNumberEditor {

	private boolean allowEmpty;
	private Class numberClass;

	private static Map<Class, Number> DEFAULT = new HashMap<Class, Number>();
	static {
		DEFAULT.put(Byte.class, new Byte("0"));
		DEFAULT.put(Short.class, new Short("0"));
		DEFAULT.put(Integer.class, new Integer("0"));
		DEFAULT.put(Long.class, new Long("0"));
		DEFAULT.put(Float.class, new Float("0.0"));
		DEFAULT.put(Double.class, new Double("0.0"));
	}

	public AllowNullNumberEditor(Class numberClass, boolean allowEmpty)
			throws IllegalArgumentException {
		super(numberClass, allowEmpty);
		this.allowEmpty = allowEmpty;
		this.numberClass = numberClass;
	}

	@Override
	public void setValue(Object value) {
		if (allowEmpty && value == null) {
			super.setValue(DEFAULT.get(numberClass));
		}else{
			super.setValue(value);
		}
	}

}
