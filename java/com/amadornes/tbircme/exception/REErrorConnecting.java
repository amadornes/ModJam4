package com.amadornes.tbircme.exception;

public class REErrorConnecting extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String host;
	private int port;
	
	public REErrorConnecting(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	@Override
	public String getMessage() {
		return "There was an error when trying to connect to " + host + " on port " + port + "!";
	}
	
}
