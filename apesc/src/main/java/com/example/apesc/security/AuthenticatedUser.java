package com.example.apesc.security;

/**
 * Identidade do funcionário autenticado, reconstruída a partir das claims do
 * token JWT a cada requisição (sem consulta ao banco). Fica disponível como
 * principal em {@code SecurityContextHolder} e via {@code @AuthenticationPrincipal}.
 */
public record AuthenticatedUser(Long id, String email, String nome) {
}
