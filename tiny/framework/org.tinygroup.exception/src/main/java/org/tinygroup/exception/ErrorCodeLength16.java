package org.tinygroup.exception;

import org.tinygroup.exception.constant.ErrorLevel;
import org.tinygroup.exception.constant.ErrorType;

/**
 * 16位长度的错误码
 * 
 * @author renhui
 * 
 */
public class ErrorCodeLength16 extends AbstractErrorCode {

	/** 未知系统异常 */
	public static final String UNKNOWN_ERROR = "TE11399999999999";

	/** 未知系统异常 */
	public static final String UNKNOWN_SYSTEM_ERROR = "TE11399999999999";

	/** 未知扩展系统异常 */
	public static final String UNKNOWN_EXT_ERROR = "TE12399999999999";
	/** 未知业务异常 */
	public static final String UNKNOWN_BIZ_ERROR = "TE13399999999999";

	/** 未知第三方异常 */
	public static final String UNKNOWN_THIRD_PARTY_ERROR = "TE14399999999999";

	// 这里是定义长度
	private static final int[] FIELD_LENGTH = { 2, 1, 1, 1, 8, 3 };

	private static final String VERSION = "2";
	/**
	 * 
	 */
	private static final long serialVersionUID = -2894313536708132240L;

	public ErrorCodeLength16() {
		super();
	}

	public ErrorCodeLength16(ErrorType errorType, ErrorLevel errorLevel,
			int errorScene, int errorNumber, String errorPrefix) {
		super(VERSION, errorType, errorLevel, errorScene, errorNumber,
				errorPrefix);
	}

	@Override
	protected int[] getFieldLength() {
		return FIELD_LENGTH;
	}

	@Override
	protected String getErrorCodeFormatString() {
		return "%2s%1s%1d%1d%08d%03d";
	}

	protected AbstractErrorCode internalParse(char[] chars) {
		this.errorScene = Integer.valueOf("" + chars[5] + chars[6] + chars[7]
				+ chars[8] + chars[9] + chars[10] + chars[11] + chars[12]);
		this.errorNumber = Integer.valueOf("" + chars[13] + chars[14]
				+ chars[15]);
		return new ErrorCodeLength16(errorType, errorLevel, errorScene, errorNumber, errorPrefix);
	}

	public boolean isLengthMatch(int errorCodeLength) {
		return 16==errorCodeLength;
	}

}
