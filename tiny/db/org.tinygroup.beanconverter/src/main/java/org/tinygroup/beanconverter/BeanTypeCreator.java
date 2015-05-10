package org.tinygroup.beanconverter;

import org.tinygroup.context2object.TypeCreator;
import org.tinygroup.tinydb.Bean;

/**
 * bean对象的Creator
 * @author renhui
 *
 */
public class BeanTypeCreator implements TypeCreator<Bean> {

	public Class<Bean> getType() {
		return Bean.class;
	}

	public Bean getInstance() {
		return new Bean();
	}
	
}
