package com.example.apesc.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Lê o header {@code Authorization: Bearer <token>}, valida a assinatura/expiração
 * e popula o {@code SecurityContext} com a identidade e as autoridades contidas
 * nas claims. Requisições sem token seguem adiante e são barradas depois pelas
 * regras de autorização (ou pelo entry point 401).
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtDecoder jwtDecoder;

    public JwtAuthenticationFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String token = extractToken(request);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Jwt jwt = jwtDecoder.decode(token);
                authenticate(jwt, request);
            } catch (JwtException ex) {
                // Token inválido/expirado: segue sem autenticação; o acesso a
                // recursos protegidos será negado com 401 pelo entry point.
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(Jwt jwt, HttpServletRequest request) {
        List<String> authorityNames = jwt.getClaimAsStringList(JwtService.CLAIM_AUTHORITIES);
        List<SimpleGrantedAuthority> authorities = authorityNames == null
            ? List.of()
            : authorityNames.stream().map(SimpleGrantedAuthority::new).toList();

        AuthenticatedUser principal = new AuthenticatedUser(
            jwt.getClaim(JwtService.CLAIM_ID) == null ? null : ((Number) jwt.getClaim(JwtService.CLAIM_ID)).longValue(),
            jwt.getSubject(),
            jwt.getClaimAsString(JwtService.CLAIM_NOME)
        );

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(principal, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length()).trim();
        }
        return null;
    }
}
