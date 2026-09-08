package com.example.apesc.security;

import com.example.apesc.exception.ErrorConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Serializa erros de segurança (401/403) no mesmo formato de corpo usado pelo
 * {@code GlobalExceptionHandler}, para o cliente ter uma resposta consistente.
 */
final class SecurityErrorWriter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private SecurityErrorWriter() {
    }

    static void write(
        HttpServletResponse response,
        HttpServletRequest request,
        HttpStatus status,
        ErrorConstants error
    ) throws IOException {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", error.name());
        body.put("message", error.getDescription());
        body.put("object", null);
        body.put("path", request.getRequestURI());

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(MAPPER.writeValueAsString(body));
    }
}
