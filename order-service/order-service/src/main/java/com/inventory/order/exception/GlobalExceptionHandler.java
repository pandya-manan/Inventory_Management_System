package com.inventory.order.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleOrderNotFound(OrderNotFoundException ex,
                                             HttpServletRequest request) {

        return new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductNotFound(ProductNotFoundException ex,
                                               HttpServletRequest request) {

        return new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(InventoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInventoryError(InventoryException ex,
                                              HttpServletRequest request) {

        return new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex,
                                          HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                message,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneric(Exception ex,
                                       HttpServletRequest request) {

        return new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something went wrong",
                request.getRequestURI()
        );
    }
}