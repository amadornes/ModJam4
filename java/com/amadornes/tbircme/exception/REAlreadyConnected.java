package com.amadornes.tbircme.exception;

public class REAlreadyConnected extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getMessage() {
		return "Connection already open!";
	}
	
}
