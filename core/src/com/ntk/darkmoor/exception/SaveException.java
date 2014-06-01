package com.ntk.darkmoor.exception;


public class SaveException extends BaseException {

	public SaveException(String errorCause) {
		super(errorCause);
		// TODO Auto-generated constructor stub
	}

	public SaveException(Exception e) {
		super(e.getMessage());
		this.t = e;
	}

}
