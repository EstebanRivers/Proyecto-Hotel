package com.proyecto.commons.exceptions;

public class CredencialesInvalidasException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CredencialesInvalidasException(String message) {
        super(message);
    }
}
