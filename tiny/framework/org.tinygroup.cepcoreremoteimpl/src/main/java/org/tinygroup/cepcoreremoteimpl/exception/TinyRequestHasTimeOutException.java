package org.tinygroup.cepcoreremoteimpl.exception;

public class TinyRequestHasTimeOutException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7691906957621909375L;

	public TinyRequestHasTimeOutException(String message, Throwable cause) {
		super(message, cause);
	}
	public TinyRequestHasTimeOutException(String eventId, String serviceId) {
		super("响应对应的eventId:" + eventId + ",serviceId:" + serviceId + "不存在，或已被超时处理");
	}

	public TinyRequestHasTimeOutException(String eventId, String serviceId,
			Throwable cause) {
		super("响应对应的eventId:" + eventId + ",serviceId:" + serviceId + "不存在，或已被超时处理",
				cause);
	}

	public TinyRequestHasTimeOutException(Throwable cause) {
		super(cause);
	}

	public TinyRequestHasTimeOutException(String message) {
		super(message);
	}
}
