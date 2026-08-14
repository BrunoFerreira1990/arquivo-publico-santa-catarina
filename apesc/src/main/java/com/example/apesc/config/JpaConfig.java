package com.example.apesc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// Separado da classe principal (ApescApplication) de propósito: manter @EnableJpaAuditing
// direto no @SpringBootApplication faz o bean de auditoria tentar subir em qualquer slice
// de teste (ex.: @WebMvcTest), mesmo sem nenhuma entidade JPA carregada nesse contexto,
// e quebra com "JPA metamodel must not be empty".
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
