package com.quickbase.cityservice;

public class ServiceError extends Exception {
	private static final long serialVersionUID = -1087707794204873700L;
	
	public ServiceError(Exception cause) {
		super(cause);
	}
	
	public ServiceError(String message) {
		super(message);
	}
}
