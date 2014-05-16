package com.amadornes.tbircme.exception;

public class RENotConnected extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getMessage() {
		return "Connection not open!";
	}
	
}
