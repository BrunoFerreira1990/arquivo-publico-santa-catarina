package com.example.apesc.security;

import com.example.apesc.exception.ErrorConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Resposta 401 (em JSON, no mesmo formato do {@code GlobalExceptionHandler})
 * quando um recurso protegido é acessado sem autenticação válida.
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws java.io.IOException {
        SecurityErrorWriter.write(
            response,
            request,
            HttpStatus.UNAUTHORIZED,
            ErrorConstants.NAO_AUTENTICADO
        );
    }
}
