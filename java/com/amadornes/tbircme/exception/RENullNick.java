package com.amadornes.tbircme.exception;

public class RENullNick extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getMessage() {
		return "Nick is null!";
	}
	
}
