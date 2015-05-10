package org.tinygroup.exception;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.commons.tools.Assert;

/**
 * 创建errorcode的工厂类
 * 
 * @author renhui
 * 
 */
public class ErrorCodeFactory {

	private static List<ErrorCodeParser> codeParsers = new ArrayList<ErrorCodeParser>();

	static {
		codeParsers.add(new ErrorCode());
		codeParsers.add(new ErrorCodeLength16());
	}

	public static void addCodeParser(ErrorCodeParser errorCodeParser) {
		codeParsers.add(errorCodeParser);
	}

	public static AbstractErrorCode parseErrorCode(String errorCode) {
		Assert.assertNotNull(errorCode, "errorCode must not be null");
		ErrorCodeParser parser = findParser(errorCode);
		return parser.parse(errorCode);
	}

	private static ErrorCodeParser findParser(String errorCodeStr) {
		for (ErrorCodeParser errorCode : codeParsers) {
			if (errorCode.isLengthMatch(errorCodeStr.length())) {
				return errorCode;
			}
		}
		throw new IllegalArgumentException(String.format(
				"未找到错误码:%s,对应的错误码解析对象", errorCodeStr));
	}
}
