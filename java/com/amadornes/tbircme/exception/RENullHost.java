package com.amadornes.tbircme.exception;

public class RENullHost extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getMessage() {
		return "Host is null!";
	}
	
}
