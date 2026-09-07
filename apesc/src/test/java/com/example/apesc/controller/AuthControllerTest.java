package com.example.apesc.controller;

import com.example.apesc.dto.LoginRequestDTO;
import com.example.apesc.dto.LoginResponseDTO;
import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    void login_deveRetornar200ComToken() throws Exception {
        LoginResponseDTO resposta = new LoginResponseDTO(
            "token-jwt", "Bearer", 3600, 7L, "Ana Souza", "ana@apesc.local", List.of("ROLE_ADMINISTRADOR"));
        when(authService.login(any(LoginRequestDTO.class))).thenReturn(resposta);

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new LoginRequestDTO("ana@apesc.local", "senha12345"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-jwt"))
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.email").value("ana@apesc.local"))
                .andExpect(jsonPath("$.autoridades[0]").value("ROLE_ADMINISTRADOR"));
    }

    @Test
    void login_deveRetornar401QuandoCredenciaisInvalidas() throws Exception {
        when(authService.login(any(LoginRequestDTO.class)))
            .thenThrow(new CustomException(ErrorConstants.CREDENCIAIS_INVALIDAS, HttpStatus.UNAUTHORIZED));

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new LoginRequestDTO("ana@apesc.local", "errada"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("CREDENCIAIS_INVALIDAS"));
    }

    @Test
    void login_deveRetornar400QuandoCorpoInvalido() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new LoginRequestDTO("  ", ""))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }
}
