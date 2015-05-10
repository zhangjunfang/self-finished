package org.tinygroup.exception;

import java.io.Serializable;

import org.tinygroup.exception.constant.ErrorLevel;
import org.tinygroup.exception.constant.ErrorType;

/**
 * 错误码抽象类
 * 
 * @author renhui
 */
public abstract class AbstractErrorCode implements Serializable,ErrorCodeParser {
	public static int PREFIX = 0, VERSION = 1, TYPE = 2, LEVEL = 3, SCENE = 4,
			NUMBER = 5;

	/**
     *
     */
	private static final long serialVersionUID = 4604190154626896337L;

	/**
	 * 默认错误前缀，TE表示TinyError的意思
	 */
	protected static final String DEFAULT_PREFIX = "TE";

	/**
	 * 统一错误规范默认版本
	 */
	protected static final String DEFAULT_VERSION = "1";
	/**
	 * 错误前缀
	 */
	protected String errorPrefix = DEFAULT_PREFIX;

	/**
	 * 错误规范版本，错误规范的版本不同，表示某些位数的长度不同
	 */
	protected String version = DEFAULT_VERSION;

	/**
	 * 错误类型<code>ErrorTypes</code>定义
	 */
	protected ErrorType errorType;

	/**
	 * 错误级别,见<code>ErrorLevels</code>定义
	 */
	protected ErrorLevel errorLevel;

	/**
	 * 错误场景
	 */
	protected int errorScene;

	/**
	 * 具体错误码
	 */
	protected int errorNumber;

	/**
	 * @return
	 */
	protected abstract int[] getFieldLength();

	protected abstract String getErrorCodeFormatString();

	protected  abstract AbstractErrorCode internalParse(char[] chars);
	
	public AbstractErrorCode() {
		super();
	}

	public AbstractErrorCode(String version, ErrorType errorType,
			ErrorLevel errorLevel, int errorScene, int errorNumber,
			String errorPrefix) {
		assertLength(PREFIX, errorPrefix);
		assertLength(VERSION, version);
		assertLength(SCENE, errorScene + "");
		assertLength(NUMBER, errorNumber + "");
		this.errorPrefix = errorPrefix;
		this.version = version;
		this.errorType = errorType;
		this.errorLevel = errorLevel;
		this.errorScene = errorScene;
		this.errorNumber = errorNumber;
	}

	public AbstractErrorCode parse(String errorCode) {
		char[] chars = errorCode.toCharArray();
		this.errorPrefix = "" + chars[0] + chars[1];
		this.version = "" + chars[2];
		this.errorType = ErrorType.find(chars[3] + "");
		this.errorLevel = ErrorLevel.find(chars[4] + "");
		return internalParse(chars);
	}

	protected void assertLength(int field, String errorPrefix) {
		if (errorPrefix == null
				|| errorPrefix.length() != getFieldLength()[field]) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(getErrorCodeFormatString(), errorPrefix, version,
				errorType.getType(), errorLevel.getLevel(), errorScene,
				errorNumber);
	}

	public String getErrorPrefix() {
		return errorPrefix;
	}

	public void setErrorPrefix(String errorPrefix) {
		this.errorPrefix = errorPrefix;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}

	public ErrorLevel getErrorLevel() {
		return errorLevel;
	}

	public void setErrorLevel(ErrorLevel errorLevel) {
		this.errorLevel = errorLevel;
	}

	public int getErrorScene() {
		return errorScene;
	}

	public void setErrorScene(int errorScene) {
		this.errorScene = errorScene;
	}

	public int getErrorNumber() {
		return errorNumber;
	}

	public void setErrorNumber(int errorNumber) {
		this.errorNumber = errorNumber;
	}
	
}
