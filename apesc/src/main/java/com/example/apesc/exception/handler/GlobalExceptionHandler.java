package com.example.apesc.exception.handler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.apesc.exception.CustomException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        final MethodArgumentNotValidException ex,
        final HttpHeaders headers,
        final HttpStatusCode status,
        final WebRequest request
    ) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getDefaultMessage())
            .collect(Collectors.joining("; "));

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", "VALIDATION_ERROR");
        body.put("message", mensagem.isEmpty() ? "Requisição inválida" : mensagem);
        body.put("object", null);
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(
        final CustomException ex,
        final WebRequest request
    ) {
        
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", ex.getHttpStatus().value());
        body.put("error", ex.getDescription() != null ? ex.getDescription().name() : "UNKNOWN_ERROR");
        body.put("message", ex.getDescription() != null ? ex.getDescription().getDescription() : "Unexpected error");
        body.put("object", ex.getObject());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, ex.getHttpStatus());
    }

}
