package com.inventory.auth.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleUserNotFound(UserNotFoundException userEx,HttpServletRequest request)
	{
		return new ErrorResponse(LocalDateTime.now(),HttpStatus.NOT_FOUND.value(),userEx.getMessage(),request.getRequestURI());
	}
	
	@ExceptionHandler(InvalidCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorResponse handleInvalidCredentials(InvalidCredentialsException invalidEx, HttpServletRequest request)
	{
		return new ErrorResponse(LocalDateTime.now(),HttpStatus.UNAUTHORIZED.value(),invalidEx.getMessage(),request.getRequestURI());
	}

}
