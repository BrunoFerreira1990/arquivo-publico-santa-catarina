package com.example.apesc.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final Object object;

    private final ErrorConstants description;

    private final HttpStatus httpStatus;
    
    public CustomException(
        final Object object, 
        final ErrorConstants description,
        final HttpStatus httpStatus) {
        this.object = object;
        this.description = description;
        this.httpStatus = httpStatus;
    }

   public CustomException(
    final ErrorConstants errorDescription,
    final HttpStatus errorStatusCode) {
        this.object = null;
        this.description = errorDescription;
        this.httpStatus = errorStatusCode;
    }
}
