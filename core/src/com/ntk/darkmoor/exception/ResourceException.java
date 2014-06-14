package com.ntk.darkmoor.exception;


public class ResourceException extends BaseException {

	public ResourceException(String errorCause) {
		super(errorCause);
	}

	public ResourceException(Exception e) {
		super(e);
	}

}
