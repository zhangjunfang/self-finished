package org.tinygroup.exception;

/**
 * 错误号翻译接口
 * @author renhui
 *
 */

public interface ErrorCodeTranslator {
    /**
     * 异常号翻译方法
     * @return
     */
	public String translate(CommonError commonError);
	
	/**
	 * 显示要进行翻译的异常号
	 * @return
	 */
	public String getErrorCode(CommonError commonError);
	
}
