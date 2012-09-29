package com.zoostudio.exception;

public class ZooLocationException extends Exception {
	private String mess;
	public ZooLocationException(String mess) {
		this.mess =  mess;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void printStackTrace() {
		super.printStackTrace();
	}
	
	@Override
	public Throwable initCause(Throwable throwable) {
		throwable = new Throwable(mess);
		return super.initCause(throwable);
	}
}
