package org.tinygroup.cepcoreremoteimpl.exception;

public class TinyRequestTimeOutException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7691906957621909375L;

	public TinyRequestTimeOutException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TinyRequestTimeOutException(Throwable cause) {
		super(cause);
	}
	
	public TinyRequestTimeOutException(String message) {
		super(message);
	}
}
