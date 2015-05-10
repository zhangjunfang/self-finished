package org.tinygroup.exception.impl;

import org.tinygroup.exception.BaseRuntimeException;
import org.tinygroup.exception.ErrorContext;
import org.tinygroup.exception.ExceptionTranslator;

/**
 * 默认的异常翻译器
 * 
 * @author renhui
 * 
 */
public class DefaultExceptionTranslator implements ExceptionTranslator {
	SimpleErrorCodeTranslator simpleErrorCodeTranslator;

	public SimpleErrorCodeTranslator getSimpleErrorCodeTranslator() {
		return simpleErrorCodeTranslator;
	}

	public void setSimpleErrorCodeTranslator(SimpleErrorCodeTranslator simpleErrorCodeTranslator) {
		this.simpleErrorCodeTranslator = simpleErrorCodeTranslator;
	}


	public String translateException(BaseRuntimeException exception) {
		ErrorContext errorContext = BaseRuntimeException
				.getErrorContext(exception);
		//TODO 检查这里的逻辑
		return simpleErrorCodeTranslator.translate(errorContext.fetchCurrentError());
	}

}
