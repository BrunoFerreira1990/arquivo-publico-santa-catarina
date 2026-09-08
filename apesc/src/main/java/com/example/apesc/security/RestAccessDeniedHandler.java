package com.example.apesc.security;

import com.example.apesc.exception.ErrorConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * Resposta 403 (em JSON) quando o funcionário está autenticado mas não possui
 * a autoridade exigida pelo recurso.
 */
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException {
        SecurityErrorWriter.write(
            response,
            request,
            HttpStatus.FORBIDDEN,
            ErrorConstants.NAO_AUTORIZADO
        );
    }
}
