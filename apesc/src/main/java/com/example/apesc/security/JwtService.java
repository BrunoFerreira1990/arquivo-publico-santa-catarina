package com.example.apesc.security;

import com.example.apesc.model.Funcionario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Emite tokens JWT assinados (HMAC-SHA256) para funcionários autenticados.
 * O token é auto-contido: carrega id, nome e autoridades, evitando consulta
 * ao banco na validação de cada requisição.
 */
@Service
public class JwtService {

    static final String CLAIM_ID = "uid";
    static final String CLAIM_NOME = "nome";
    static final String CLAIM_AUTHORITIES = "authorities";

    private static final String ISSUER = "apesc";

    private final JwtEncoder jwtEncoder;
    private final long expirationSeconds;

    public JwtService(
        JwtEncoder jwtEncoder,
        @Value("${app.jwt.expiration:3600}") long expirationSeconds
    ) {
        this.jwtEncoder = jwtEncoder;
        this.expirationSeconds = expirationSeconds;
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }

    public String generateToken(FuncionarioUserDetails userDetails) {
        Funcionario funcionario = userDetails.getFuncionario();
        Instant now = Instant.now();

        List<String> authorities = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer(ISSUER)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expirationSeconds))
            .subject(funcionario.getEmail())
            .claim(CLAIM_ID, funcionario.getId())
            .claim(CLAIM_NOME, funcionario.getNome())
            .claim(CLAIM_AUTHORITIES, authorities)
            .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
