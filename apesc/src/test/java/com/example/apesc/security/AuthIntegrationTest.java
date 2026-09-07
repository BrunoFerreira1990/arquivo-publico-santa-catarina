package com.example.apesc.security;

import com.example.apesc.dto.LoginRequestDTO;
import com.example.apesc.model.Funcionario;
import com.example.apesc.model.Permissoes;
import com.example.apesc.model.enums.Generos;
import com.example.apesc.repository.FuncionarioRepository;
import com.example.apesc.repository.PermissoesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Transactional
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PermissoesRepository permissoesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void seed() {
        funcionarioRepository.deleteAll();
        permissoesRepository.deleteAll();

        Permissoes regra = new Permissoes();
        regra.setNomeRegra("Administrador");
        regra = permissoesRepository.save(regra);

        Funcionario f = new Funcionario();
        f.setNome("Ana Souza");
        f.setDataNascimento(LocalDate.of(1990, 1, 1));
        f.setGenero(Generos.FEMININO);
        f.setEmail("ana@apesc.local");
        f.setSenha(passwordEncoder.encode("senha12345"));
        f.setNumeroMatricula("MAT-001");
        f.setCargo("Arquivista");
        f.setSetor("Arquivo");
        f.setPermissoes(regra);
        funcionarioRepository.save(f);
    }

    private String login(String email, String senha) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new LoginRequestDTO(email, senha))))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
    }

    @Test
    void login_valido_permiteAcessarRecursoProtegido() throws Exception {
        String token = login("ana@apesc.local", "senha12345");
        assertThat(token).isNotBlank();

        mockMvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ana@apesc.local"))
                .andExpect(jsonPath("$.autoridades[0]").value("ROLE_ADMINISTRADOR"));
    }

    @Test
    void login_senhaErrada_retorna401() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new LoginRequestDTO("ana@apesc.local", "errada"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("CREDENCIAIS_INVALIDAS"));
    }

    @Test
    void recursoProtegido_semToken_retorna401() throws Exception {
        mockMvc.perform(get("/api/funcionario"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("NAO_AUTENTICADO"));
    }

    @Test
    void recursoProtegido_tokenInvalido_retorna401() throws Exception {
        mockMvc.perform(get("/api/funcionario").header("Authorization", "Bearer nao-e-um-jwt"))
                .andExpect(status().isUnauthorized());
    }
}
