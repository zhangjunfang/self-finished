package org.tinygroup.cepcoreremoteimpl.exception;

public class TinyRequestException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7691906957621909375L;

	public TinyRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public TinyRequestException(String eventId, String serviceId,
			Throwable cause) {
		super("eventId:" + eventId + ",serviceId:" + serviceId + "发生异常", cause);
	}

	public TinyRequestException(Throwable cause) {
		super(cause);
	}

	public TinyRequestException(String message) {
		super(message);
	}
}
