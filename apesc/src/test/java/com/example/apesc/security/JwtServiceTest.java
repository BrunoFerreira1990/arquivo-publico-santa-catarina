package com.example.apesc.security;

import com.example.apesc.model.Funcionario;
import com.example.apesc.model.Permissoes;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String SECRET = "test-secret-test-secret-test-secret-0123456789";

    private JwtService jwtService;
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        SecretKey key = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        jwtService = new JwtService(new NimbusJwtEncoder(new ImmutableSecret<>(key)), 3600L);
        jwtDecoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }

    private FuncionarioUserDetails userDetails() {
        Permissoes permissoes = new Permissoes();
        permissoes.setNomeRegra("Administrador Geral");

        Funcionario f = new Funcionario();
        f.setId(42L);
        f.setNome("Ana Souza");
        f.setEmail("ana@apesc.local");
        f.setSenha("$2a$10$hash");
        f.setPermissoes(permissoes);
        return new FuncionarioUserDetails(f);
    }

    @Test
    void generateToken_deveEmitirTokenComClaimsDoFuncionario() {
        String token = jwtService.generateToken(userDetails());

        Jwt jwt = jwtDecoder.decode(token);
        assertThat(jwt.getSubject()).isEqualTo("ana@apesc.local");
        assertThat(jwt.getClaimAsString("nome")).isEqualTo("Ana Souza");
        assertThat(((Number) jwt.getClaim("uid")).longValue()).isEqualTo(42L);
        assertThat(jwt.getClaimAsStringList("authorities")).containsExactly("ROLE_ADMINISTRADOR_GERAL");
        assertThat(jwt.getExpiresAt()).isNotNull();
        assertThat(jwt.getIssuedAt()).isNotNull();
    }

    @Test
    void generateToken_semPermissao_deveEmitirTokenSemAutoridades() {
        Funcionario f = new Funcionario();
        f.setId(1L);
        f.setNome("Sem Regra");
        f.setEmail("sem@apesc.local");
        f.setSenha("$2a$10$hash");

        String token = jwtService.generateToken(new FuncionarioUserDetails(f));

        Jwt jwt = jwtDecoder.decode(token);
        assertThat(jwt.getClaimAsStringList("authorities")).isEqualTo(List.of());
    }
}
