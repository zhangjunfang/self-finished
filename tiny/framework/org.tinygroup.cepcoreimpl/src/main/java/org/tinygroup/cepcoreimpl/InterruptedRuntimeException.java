package org.tinygroup.cepcoreimpl;

public class InterruptedRuntimeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5955287660430613244L;
	InterruptedException e;
	public InterruptedRuntimeException(InterruptedException e) {
		this.e = e ;
	}

}
