package org.tinygroup.exception.dao;

import java.util.Locale;

import org.tinygroup.context.Context;
import org.tinygroup.exception.TinyBizRuntimeException;

/**
 * 
 * @author renhui
 *
 */
public class DaoException extends TinyBizRuntimeException {

	public DaoException(String errorCode, Context context, Locale locale) {
		super(errorCode, context, locale);
	}

	public DaoException(String errorCode, Context context) {
		super(errorCode, context);
	}

	public DaoException(String errorCode, Object... params) {
		super(errorCode, params);
	}

	public DaoException(String errorCode, String defaultErrorMsg,
			Context context, Locale locale) {
		super(errorCode, defaultErrorMsg, context, locale);
	}

	public DaoException(String errorCode, String defaultErrorMsg,
			Locale locale, Object... params) {
		super(errorCode, defaultErrorMsg, locale, params);
	}

	public DaoException(String errorCode, String defaultErrorMsg,
			Locale locale, Throwable throwable, Object... params) {
		super(errorCode, defaultErrorMsg, locale, throwable, params);
	}

	public DaoException(String errorCode, String defaultErrorMsg,
			Object... params) {
		super(errorCode, defaultErrorMsg, params);
	}

	public DaoException(String errorCode, String defaultErrorMsg,
			Throwable throwable, Object... params) {
		super(errorCode, defaultErrorMsg, throwable, params);
	}

	
}
