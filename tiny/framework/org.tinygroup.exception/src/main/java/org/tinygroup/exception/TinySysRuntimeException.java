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

import org.tinygroup.context.Context;

import java.util.Locale;

/**
 * 框架系统运行期异常,建议直接使用BaseRuntimeException
 * @author renhui
 *
 */
@Deprecated
public class TinySysRuntimeException extends BaseRuntimeException {

	private static final long serialVersionUID = 5006685550353539837L;

	public TinySysRuntimeException(String errorCode, Context context,
			Locale locale) {
		super(errorCode, context, locale);
	}

	public TinySysRuntimeException(String errorCode, Context context) {
		super(errorCode, context);
	}

	public TinySysRuntimeException(String errorCode, Object... params) {
		super(errorCode, params);
	}

	public TinySysRuntimeException(String errorCode, String defaultErrorMsg,
			Context context, Locale locale) {
		super(errorCode, defaultErrorMsg, context, locale);
	}

	public TinySysRuntimeException(String errorCode, String defaultErrorMsg,
			Locale locale, Object... params) {
		super(errorCode, defaultErrorMsg, locale, params);
	}

	public TinySysRuntimeException(String errorCode, String defaultErrorMsg,
			Locale locale, Throwable throwable, Object... params) {
		super(errorCode, defaultErrorMsg, locale, throwable, params);
	}

	public TinySysRuntimeException(String errorCode, String defaultErrorMsg,
			Object... params) {
		super(errorCode, defaultErrorMsg, params);
	}
	
	public TinySysRuntimeException(String errorCode, Throwable throwable,
			Object... params) {
		super(errorCode, throwable, params);
	}

	public TinySysRuntimeException(String errorCode, String defaultErrorMsg,
			Throwable throwable, Object... params) {
		super(errorCode, defaultErrorMsg, throwable, params);
	}

	public TinySysRuntimeException() {
		super();
	}

	public TinySysRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public TinySysRuntimeException(String message) {
		super(message);
	}

	public TinySysRuntimeException(Throwable cause) {
		super(cause);
	}
	
}
