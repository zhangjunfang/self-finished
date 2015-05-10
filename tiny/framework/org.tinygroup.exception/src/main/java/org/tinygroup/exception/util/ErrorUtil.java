package org.tinygroup.exception.util;

import org.tinygroup.exception.AbstractErrorCode;
import org.tinygroup.exception.CommonError;
import org.tinygroup.exception.ErrorCode;
import org.tinygroup.exception.ErrorContext;
import org.tinygroup.exception.constant.ErrorLevel;
import org.tinygroup.exception.constant.ErrorType;

/**
 * 标准错误码工具类。
 * 
 * <p>
 * 使用标准错误码时，必须配置ErrorUtil的bean。否则获取不到错误位置信息。
 * 
 */
public class ErrorUtil {

	/** 系统名称 */
	private static String appName;

	// ~~~ 构造方法

	private ErrorUtil() {
	}

	/**
	 * V1版本错误码构造函数。
	 * 
	 * @param errorType
	 *            错误类型。（1：框架级系统错误 、2:框架扩展系统错误,3：业务错误、4：第三方错误） 
	 * @param
	 *            errorLevel 错误级别。（1：信息、2：警告、3：错误、4：严重错误）
	 * @param errorScene
	 *            错误场景。（业务事件码）
	 * @param errorSpecific
	 *            错误编码。（错误明细编码，错误场景内唯一）
	 * @param errorPrefix
	 *            错误码前缀。（按厂商分配）
	 * @return 错误码实例。
	 */
	public static ErrorCode makeErrorCode(String errorType, String errorLevel,
			int errorScene, int errorSpecific, String errorPrefix) {
		return new ErrorCode(ErrorType.find(errorType), ErrorLevel.find(errorLevel), errorScene, errorSpecific,
				errorPrefix);
	}

	/**
	 * 创建一个CommonError
	 * 
	 * @param errorLevel
	 * @param errorType
	 * @param errorScene
	 * @param errorSpecific
	 * @param message
	 * @return
	 */
	public static CommonError makeError(AbstractErrorCode errorCode, String message) {

		CommonError error = new CommonError();
		error.setErrorCode(errorCode);
		error.setErrorMsg(message);
		error.setErrorSource(getAppName());
		return error;
	}

	/**
	 * 创建一个CommonError
	 * 
	 * @param errorLevel
	 * @param errorType
	 * @param errorScene
	 * @param errorSpecific
	 * @param message
	 * @param location
	 * @return
	 */
	public static CommonError makeError(AbstractErrorCode errorCode, String message,
			String location) {

		CommonError error = new CommonError();
		error.setErrorCode(errorCode);
		error.setErrorMsg(message);
		error.setErrorSource(location);
		return error;
	}

	/**
	 * 增加一个error到errorContext中
	 * 
	 * @param error
	 * @return
	 */
	public static ErrorContext addError(CommonError error) {
		return addError(null, error);
	}

	/**
	 * 增加一个error到errorContext中
	 * 
	 * @param context
	 * @param error
	 * @return
	 */
	public static ErrorContext addError(ErrorContext context, CommonError error) {

		if (context == null) {
			context = new ErrorContext();
		}

		if (error == null) {
			return context;
		}

		context.addError(error);

		return context;
	}

	/**
	 * 创建并且增加一个Error到errorContext中
	 * 
	 * @param context
	 * @param errorCode
	 * @param message
	 * @return
	 */
	public static ErrorContext makeAndAddError(ErrorContext context,
			AbstractErrorCode errorCode, String message) {

		CommonError error = makeError(errorCode, message);
		context = addError(context, error);

		return context;
	}

	/**
	 * 创建并且增加一个Error到新的errorContext中
	 * 
	 * @param errorCode
	 * @param message
	 * @return
	 */
	public static ErrorContext makeAndAddError(AbstractErrorCode errorCode,
			String message) {

		CommonError error = makeError(errorCode, message);
		ErrorContext context = addError(error);

		return context;
	}

	/**
	 * 获取系统名称
	 * 
	 * @return
	 */
	public static String getAppName() {

		if (null == appName || "".equals(appName)) {
			return "unknown";
		}

		return appName;
	}

	/**
	 * Setter method for property <tt>appName</tt>.
	 * 
	 * @param appName
	 *            value to be assigned to property appName
	 */
	public static void setAppName(String appName) {
		ErrorUtil.appName = appName;
	}
}
