package org.tinygroup.exception.service;

import java.util.Locale;

import org.tinygroup.context.Context;
import org.tinygroup.exception.TinyBizRuntimeException;

/**
 * 
 * @author renhui
 *
 */
public class ServiceException extends TinyBizRuntimeException {

	public ServiceException(String errorCode, Context context, Locale locale) {
		super(errorCode, context, locale);
	}

	public ServiceException(String errorCode, Context context) {
		super(errorCode, context);
	}

	public ServiceException(String errorCode, Object... params) {
		super(errorCode, params);
	}

	public ServiceException(String errorCode, String defaultErrorMsg,
			Context context, Locale locale) {
		super(errorCode, defaultErrorMsg, context, locale);
	}

	public ServiceException(String errorCode, String defaultErrorMsg,
			Locale locale, Object... params) {
		super(errorCode, defaultErrorMsg, locale, params);
	}

	public ServiceException(String errorCode, String defaultErrorMsg,
			Locale locale, Throwable throwable, Object... params) {
		super(errorCode, defaultErrorMsg, locale, throwable, params);
	}

	public ServiceException(String errorCode, String defaultErrorMsg,
			Object... params) {
		super(errorCode, defaultErrorMsg, params);
	}

	public ServiceException(String errorCode, String defaultErrorMsg,
			Throwable throwable, Object... params) {
		super(errorCode, defaultErrorMsg, throwable, params);
	}

	
}
