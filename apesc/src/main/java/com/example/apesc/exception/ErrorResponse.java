package com.example.apesc.exception;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

    public static void validator(
        final Object object,
        final ErrorConstants message,
        final HttpStatus status) {
            
            if (object == null || object.toString().isEmpty()) {
                throw new CustomException(object, message, status);
            }
        
    }

    public static void validator(
        final List<?> objects,
        final ErrorConstants message,
        final HttpStatus status) {
            if (objects == null || objects.isEmpty()) {
                throw new CustomException(objects, message, status);
            }
    }
    
    public static void validator(
        final Optional<?> object,
        final ErrorConstants message,
        final HttpStatus status) {
            
            if (object.isEmpty()) {
                throw new CustomException(object, message, status);
            }
        
    }
    
}
