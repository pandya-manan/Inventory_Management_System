package com.inventory.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(InventoryNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleNotFound(InventoryNotFoundException ex,HttpServletRequest request)
	{
		return new ErrorResponse(LocalDateTime.now(),HttpStatus.NOT_FOUND.value(),ex.getMessage(),request.getRequestURI());
	}
	
	@ExceptionHandler(InsufficientStockException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleStockError(InsufficientStockException ex,HttpServletRequest request)
	{
		return new ErrorResponse(LocalDateTime.now(),HttpStatus.BAD_REQUEST.value(),ex.getMessage(),request.getRequestURI());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleValidation(MethodArgumentNotValidException ex,HttpServletRequest request)
	{
		String message = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();
		
		return new ErrorResponse(LocalDateTime.now(),HttpStatus.BAD_REQUEST.value(),message,request.getRequestURI());
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleGeneric(Exception ex,HttpServletRequest request)
	{
		return new ErrorResponse(LocalDateTime.now(),HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage(),request.getRequestURI());
	}
	
	@ExceptionHandler(ProductNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleProductNotFound(ProductNotFoundException ex, HttpServletRequest request)
	{
		return new ErrorResponse(LocalDateTime.now(),HttpStatus.NOT_FOUND.value(),ex.getMessage(),request.getRequestURI());
	}

}
