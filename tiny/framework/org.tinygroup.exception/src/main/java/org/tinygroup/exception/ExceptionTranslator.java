package org.tinygroup.exception;


/**
 * 异常翻译
 * @author renhui
 *
 */
public interface ExceptionTranslator {

    /**
     * 	把exception对应的错误码翻译成对应的错误信息
     * @param exception
     * @return
     */
	public String translateException(BaseRuntimeException exception);
	
}
