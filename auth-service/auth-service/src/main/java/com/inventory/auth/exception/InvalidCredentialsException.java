package com.inventory.auth.exception;

@SuppressWarnings("serial")
public class InvalidCredentialsException extends RuntimeException{
	
	public InvalidCredentialsException(String message)
	{
		super(message);
	}

}
