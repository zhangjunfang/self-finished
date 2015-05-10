/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.exception;

import org.tinygroup.commons.i18n.LocaleUtil;
import org.tinygroup.commons.tools.ExceptionUtil;
import org.tinygroup.context.Context;
import org.tinygroup.exception.util.ErrorUtil;
import org.tinygroup.i18n.I18nMessage;
import org.tinygroup.i18n.I18nMessageFactory;

import java.util.List;
import java.util.Locale;

public class BaseRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -1141168272047460629L;
	private static I18nMessage i18nMessage = I18nMessageFactory
			.getI18nMessages();// 需要在启动的时候注入进来
	private String errorMsg;

	private AbstractErrorCode errorCode;

	@Override
	public String getMessage() {
		return errorMsg;
	}

	public AbstractErrorCode getErrorCode() {
		return errorCode;
	}

	public static void setI18nMessage(I18nMessage i18nMessage) {
		BaseRuntimeException.i18nMessage = i18nMessage;
	}

	public BaseRuntimeException(String errorCode, Object... params) {
		this(errorCode, "", LocaleUtil.getContext().getLocale(), params);
	}

	public BaseRuntimeException(String errorCode, String defaultErrorMsg,
			Object... params) {
		this(errorCode, defaultErrorMsg, LocaleUtil.getContext().getLocale(),
				params);
	}

	public BaseRuntimeException(String errorCode, String defaultErrorMsg,
			Locale locale, Object... params) {
		String errorMsg = i18nMessage.getMessage(errorCode, locale,
				defaultErrorMsg, params);
		this.errorCode = ErrorCodeFactory.parseErrorCode(errorCode);
		this.errorMsg = errorMsg;
	}

	public BaseRuntimeException(String errorCode, Throwable throwable,
			Object... params) {
		this(errorCode, "", LocaleUtil.getContext().getLocale(), throwable,
				params);
	}

	public BaseRuntimeException(String errorCode, String defaultErrorMsg,
			Throwable throwable, Object... params) {
		this(errorCode, defaultErrorMsg, LocaleUtil.getContext().getLocale(),
				throwable, params);
	}

	public BaseRuntimeException(String errorCode, String defaultErrorMsg,
			Locale locale, Throwable throwable, Object... params) {
		super(throwable);
		String errorMsg = i18nMessage.getMessage(errorCode, locale,
				defaultErrorMsg, params);
		this.errorCode = ErrorCodeFactory.parseErrorCode(errorCode);
		this.errorMsg = errorMsg;
	}

	public BaseRuntimeException(String errorCode, Context context, Locale locale) {
		this(errorCode, "", context, locale);
	}

	public BaseRuntimeException(String errorCode, String defaultErrorMsg,
			Context context, Locale locale) {
		String errorMsg = i18nMessage.getMessage(errorCode, defaultErrorMsg,
				context, locale);
		this.errorMsg = errorMsg;
		this.errorCode = ErrorCodeFactory.parseErrorCode(errorCode);
	}

	public BaseRuntimeException(String errorCode, Context context) {
		this(errorCode, "", context, LocaleUtil.getContext().getLocale());
	}

	public BaseRuntimeException() {
		super();
	}

	public BaseRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseRuntimeException(String message) {
		super(message);

	}

	public BaseRuntimeException(Throwable cause) {
		super(cause);
	}

	public static ErrorContext getErrorContext(Throwable throwable) {
		ErrorContext errorContext = new ErrorContext();
		List<Throwable> causes = ExceptionUtil.getCauses(throwable, true);
		for (Throwable cause : causes) {
			if (cause instanceof BaseRuntimeException) {
				BaseRuntimeException exception = (BaseRuntimeException) cause;
				ErrorUtil.makeAndAddError(errorContext,
						exception.getErrorCode(), exception.getMessage());
			}
		}
		return errorContext;
	}

	/**
	 * Check whether this exception contains an exception of the given type:
	 * either it is of the given class itself or it contains a nested cause of
	 * the given type.
	 * 
	 * @param exType
	 *            the exception type to look for
	 * @return whether there is a nested exception of the specified type
	 */
	public boolean contains(Class exType) {
		if (exType == null) {
			return false;
		}
		if (exType.isInstance(this)) {
			return true;
		}
		Throwable cause = getCause();
		if (cause == this) {
			return false;
		}
		if (cause instanceof BaseRuntimeException) {
			return ((BaseRuntimeException) cause).contains(exType);
		} else {
			while (cause != null) {
				if (exType.isInstance(cause)) {
					return true;
				}
				if (cause.getCause() == cause) {
					break;
				}
				cause = cause.getCause();
			}
			return false;
		}
	}

}
