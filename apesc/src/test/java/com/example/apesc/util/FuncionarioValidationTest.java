package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.Funcionario;
import com.example.apesc.model.enums.Generos;
import com.example.apesc.repository.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FuncionarioValidationTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    private FuncionarioValidation validation;

    @BeforeEach
    void setUp() {
        validation = new FuncionarioValidation(funcionarioRepository);
    }

    private Funcionario funcionarioValido() {
        Funcionario f = new Funcionario();
        f.setNome("Ana Souza");
        f.setDataNascimento(LocalDate.of(1990, 1, 1));
        f.setGenero(Generos.FEMININO);
        f.setEmail("ana@apesc.local");
        f.setSenha("senha12345");
        f.setNumeroMatricula("MAT-001");
        f.setCargo("Arquivista");
        f.setSetor("Arquivo");
        return f;
    }

    @Test
    void validateSave_deveAceitarFuncionarioValido() {
        assertThatCode(() -> validation.validateSave(funcionarioValido())).doesNotThrowAnyException();
    }

    @Test
    void validateSave_deveExigirSenha() {
        Funcionario f = funcionarioValido();
        f.setSenha(null);

        assertThatThrownBy(() -> validation.validateSave(f))
            .isInstanceOfSatisfying(CustomException.class, ex ->
                org.assertj.core.api.Assertions.assertThat(ex.getDescription()).isEqualTo(ErrorConstants.SENHA_REQUIRED));
    }

    @Test
    void validateSave_deveRecusarSenhaCurta() {
        Funcionario f = funcionarioValido();
        f.setSenha("1234567");

        assertThatThrownBy(() -> validation.validateSave(f))
            .isInstanceOfSatisfying(CustomException.class, ex ->
                org.assertj.core.api.Assertions.assertThat(ex.getDescription()).isEqualTo(ErrorConstants.SENHA_INVALIDA));
    }

    @Test
    void validateSave_deveRecusarEmailDuplicado() {
        when(funcionarioRepository.existsByEmailIgnoreCase("ana@apesc.local")).thenReturn(true);

        assertThatThrownBy(() -> validation.validateSave(funcionarioValido()))
            .isInstanceOfSatisfying(CustomException.class, ex -> {
                org.assertj.core.api.Assertions.assertThat(ex.getDescription()).isEqualTo(ErrorConstants.EMAIL_DUPLICADO);
                org.assertj.core.api.Assertions.assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.CONFLICT);
            });
    }

    @Test
    void validateUpdate_senhaAusente_deveSerAceita() {
        lenient().when(funcionarioRepository.existsByEmailIgnoreCaseAndIdNot("ana@apesc.local", 1L)).thenReturn(false);
        Funcionario f = funcionarioValido();
        f.setId(1L);
        f.setSenha(null);

        assertThatCode(() -> validation.validateUpdate(f)).doesNotThrowAnyException();
    }

    @Test
    void validateUpdate_senhaCurta_deveSerRecusada() {
        lenient().when(funcionarioRepository.existsByEmailIgnoreCaseAndIdNot("ana@apesc.local", 1L)).thenReturn(false);
        Funcionario f = funcionarioValido();
        f.setId(1L);
        f.setSenha("abc");

        assertThatThrownBy(() -> validation.validateUpdate(f))
            .isInstanceOfSatisfying(CustomException.class, ex ->
                org.assertj.core.api.Assertions.assertThat(ex.getDescription()).isEqualTo(ErrorConstants.SENHA_INVALIDA));
    }
}
