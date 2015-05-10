package org.tinygroup.nettyremote.Exception;

public class TinyRemoteConnectException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7691906957621909375L;

	public TinyRemoteConnectException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TinyRemoteConnectException(Throwable cause) {
		super(cause);
	}
	
	public TinyRemoteConnectException(String message) {
		super(message);
	}
}
