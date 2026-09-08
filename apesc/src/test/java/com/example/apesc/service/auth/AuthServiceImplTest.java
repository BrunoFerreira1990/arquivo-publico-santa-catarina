package com.example.apesc.service.auth;

import com.example.apesc.dto.LoginRequestDTO;
import com.example.apesc.dto.LoginResponseDTO;
import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.Funcionario;
import com.example.apesc.model.Permissoes;
import com.example.apesc.security.FuncionarioUserDetails;
import com.example.apesc.security.JwtService;
import com.example.apesc.service.auth.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    private AuthServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AuthServiceImpl(authenticationManager, jwtService);
    }

    private Funcionario funcionario() {
        Permissoes permissoes = new Permissoes();
        permissoes.setNomeRegra("Administrador");

        Funcionario f = new Funcionario();
        f.setId(7L);
        f.setNome("Ana Souza");
        f.setEmail("ana@apesc.local");
        f.setSenha("$2a$10$hash");
        f.setPermissoes(permissoes);
        return f;
    }

    @Test
    void login_deveAutenticarEGerarToken() {
        FuncionarioUserDetails userDetails = new FuncionarioUserDetails(funcionario());
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtService.generateToken(userDetails)).thenReturn("token-jwt");
        when(jwtService.getExpirationSeconds()).thenReturn(3600L);

        LoginResponseDTO resposta = service.login(new LoginRequestDTO(" ana@apesc.local ", "senha12345"));

        assertThat(resposta.getToken()).isEqualTo("token-jwt");
        assertThat(resposta.getTipo()).isEqualTo("Bearer");
        assertThat(resposta.getExpiraEmSegundos()).isEqualTo(3600L);
        assertThat(resposta.getFuncionarioId()).isEqualTo(7L);
        assertThat(resposta.getEmail()).isEqualTo("ana@apesc.local");
        assertThat(resposta.getAutoridades()).containsExactly("ROLE_ADMINISTRADOR");

        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
            ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        org.mockito.Mockito.verify(authenticationManager).authenticate(captor.capture());
        assertThat(captor.getValue().getPrincipal()).isEqualTo("ana@apesc.local");
    }

    @Test
    void login_deveLancar401QuandoCredenciaisInvalidas() {
        when(authenticationManager.authenticate(any()))
            .thenThrow(new BadCredentialsException("ruim"));

        assertThatThrownBy(() -> service.login(new LoginRequestDTO("ana@apesc.local", "errada")))
            .isInstanceOfSatisfying(CustomException.class, ex -> {
                assertThat(ex.getDescription()).isEqualTo(ErrorConstants.CREDENCIAIS_INVALIDAS);
                assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
            });
    }
}
