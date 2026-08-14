package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.Pesquisador;
import com.example.apesc.model.enums.*;
import com.example.apesc.repository.PesquisadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PesquisadorValidationTest {

    @Mock
    private PesquisadorRepository pesquisadorRepository;

    private PesquisadorValidation validation;

    @BeforeEach
    void setUp() {
        validation = new PesquisadorValidation(pesquisadorRepository);
    }

    private Pesquisador pesquisadorValido() {
        Pesquisador p = new Pesquisador();
        p.setNome("Maria Aparecida Silva");
        p.setDataNascimento(LocalDate.of(1990, 1, 1));
        p.setGenero(Generos.FEMININO);
        p.setEmail("maria@email.com");
        p.setCpf("123.456.789-30");
        p.setNacionalidade(Nacionalidade.BRASILEIRA);
        p.setNumeroTelefone("48999998888");
        p.setLogradouro("Rua das Flores");
        p.setNumeroCasa("123");
        p.setBairro("Centro");
        p.setCidade("Florianópolis");
        p.setEstado(Estados.SC);
        p.setCep("88010000");
        p.setNivelEducacional(NivelEducacional.ENSINO_SUPERIOR);
        p.setProfissao("Historiadora");
        p.setAssuntoPesquisa("Ocupação do litoral catarinense");
        p.setFinalidadePesquisa("Dissertação de mestrado");
        p.setPeriodoEstudo(Set.of(PeriodoEstudo.REPUBLICANO));
        p.setAreaEstudo(Set.of(AreaEstudo.HISTORIA_REGIONAL));
        return p;
    }

    private void assertValidationError(Runnable action, ErrorConstants esperado, HttpStatus statusEsperado) {
        assertThatThrownBy(action::run)
                .isInstanceOf(CustomException.class)
                .satisfies(ex -> {
                    CustomException custom = (CustomException) ex;
                    assertThat(custom.getDescription()).isEqualTo(esperado);
                    assertThat(custom.getHttpStatus()).isEqualTo(statusEsperado);
                });
    }

    // ---------- validateSave: caminho feliz ----------

    @Test
    void validateSave_naoDeveLancarExcecaoQuandoTodosOsCamposValidos() {
        when(pesquisadorRepository.existsByCpf(any())).thenReturn(false);

        assertThatCode(() -> validation.validateSave(pesquisadorValido())).doesNotThrowAnyException();
    }

    @Test
    void validateSave_deveNormalizarCpfRemovendoPontuacao() {
        when(pesquisadorRepository.existsByCpf(any())).thenReturn(false);
        Pesquisador pesquisador = pesquisadorValido();
        pesquisador.setCpf("123.456.789-30");

        validation.validateSave(pesquisador);

        assertThat(pesquisador.getCpf()).isEqualTo("12345678930");
    }

    @Test
    void validateSave_deveConsultarCpfDuplicadoJaNormalizado() {
        when(pesquisadorRepository.existsByCpf("12345678930")).thenReturn(false);
        Pesquisador pesquisador = pesquisadorValido();
        pesquisador.setCpf("123.456.789-30");

        validation.validateSave(pesquisador);

        org.mockito.Mockito.verify(pesquisadorRepository).existsByCpf("12345678930");
    }

    @Test
    void validateSave_deveLancarConflitoQuandoCpfJaExiste() {
        when(pesquisadorRepository.existsByCpf(any())).thenReturn(true);

        assertValidationError(
                () -> validation.validateSave(pesquisadorValido()),
                ErrorConstants.CPF_DUPLICADO,
                HttpStatus.CONFLICT
        );
    }

    @Test
    void validateSave_naoDeveExigirEmailPreenchido() {
        when(pesquisadorRepository.existsByCpf(any())).thenReturn(false);
        Pesquisador pesquisador = pesquisadorValido();
        pesquisador.setEmail(null);

        assertThatCode(() -> validation.validateSave(pesquisador)).doesNotThrowAnyException();
    }

    @Test
    void validateSave_deveRejeitarEmailComFormatoInvalido() {
        Pesquisador pesquisador = pesquisadorValido();
        pesquisador.setEmail("nao-e-um-email");

        assertValidationError(
                () -> validation.validateSave(pesquisador),
                ErrorConstants.EMAIL_INVALID,
                HttpStatus.BAD_REQUEST
        );
    }

    // ---------- validateSave: campos obrigatórios ----------

    private static Stream<Arguments> camposObrigatorios() {
        return Stream.of(
                Arguments.of((Consumer<Pesquisador>) p -> p.setNome(null), ErrorConstants.NOME_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setNome("   "), ErrorConstants.NOME_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setDataNascimento(null), ErrorConstants.DATA_NASCIMENTO_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setGenero(null), ErrorConstants.GENERO_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setCpf(null), ErrorConstants.CPF_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setCpf("  "), ErrorConstants.CPF_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setNacionalidade(null), ErrorConstants.NACIONALIDADE_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setNumeroTelefone(null), ErrorConstants.NUMERO_TELEFONE_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setLogradouro(null), ErrorConstants.LOGRADOURO_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setNumeroCasa(null), ErrorConstants.NUMERO_CASA_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setBairro(null), ErrorConstants.BAIRRO_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setCidade(null), ErrorConstants.CIDADE_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setEstado(null), ErrorConstants.ESTADO_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setCep(null), ErrorConstants.CEP_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setNivelEducacional(null), ErrorConstants.NIVEL_EDUCACIONAL_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setProfissao(null), ErrorConstants.PROFISSAO_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setAssuntoPesquisa(null), ErrorConstants.ASSUNTO_PESQUISA_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setFinalidadePesquisa(null), ErrorConstants.FINALIDADE_PESQUISA_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setPeriodoEstudo(Set.of()), ErrorConstants.PERIODO_ESTUDO_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setPeriodoEstudo(null), ErrorConstants.PERIODO_ESTUDO_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setAreaEstudo(Set.of()), ErrorConstants.AREA_ESTUDO_REQUIRED),
                Arguments.of((Consumer<Pesquisador>) p -> p.setAreaEstudo(null), ErrorConstants.AREA_ESTUDO_REQUIRED)
        );
    }

    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("camposObrigatorios")
    void validateSave_deveRejeitarCampoObrigatorioAusente(Consumer<Pesquisador> mutador, ErrorConstants erroEsperado) {
        Pesquisador pesquisador = pesquisadorValido();
        mutador.accept(pesquisador);

        assertValidationError(() -> validation.validateSave(pesquisador), erroEsperado, HttpStatus.BAD_REQUEST);
    }

    // ---------- validateUpdate ----------

    @Test
    void validateUpdate_deveLancarQuandoIdForNulo() {
        Pesquisador pesquisador = pesquisadorValido();
        pesquisador.setId(null);

        assertValidationError(
                () -> validation.validateUpdate(pesquisador),
                ErrorConstants.INVALID_ID,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateUpdate_naoDeveLancarExcecaoQuandoTudoValido() {
        when(pesquisadorRepository.existsByCpfAndIdNot(any(), anyLong())).thenReturn(false);
        Pesquisador pesquisador = pesquisadorValido();
        pesquisador.setId(1L);

        assertThatCode(() -> validation.validateUpdate(pesquisador)).doesNotThrowAnyException();
    }

    @Test
    void validateUpdate_deveNormalizarCpfEIgnorarOProprioRegistroNaChecagemDeDuplicidade() {
        when(pesquisadorRepository.existsByCpfAndIdNot("12345678930", 7L)).thenReturn(false);
        Pesquisador pesquisador = pesquisadorValido();
        pesquisador.setId(7L);
        pesquisador.setCpf("123.456.789-30");

        validation.validateUpdate(pesquisador);

        assertThat(pesquisador.getCpf()).isEqualTo("12345678930");
        org.mockito.Mockito.verify(pesquisadorRepository).existsByCpfAndIdNot("12345678930", 7L);
    }

    @Test
    void validateUpdate_deveLancarConflitoQuandoCpfPertenceAOutroRegistro() {
        when(pesquisadorRepository.existsByCpfAndIdNot(any(), anyLong())).thenReturn(true);
        Pesquisador pesquisador = pesquisadorValido();
        pesquisador.setId(1L);

        assertValidationError(
                () -> validation.validateUpdate(pesquisador),
                ErrorConstants.CPF_DUPLICADO,
                HttpStatus.CONFLICT
        );
    }

    // ---------- validateDelete ----------

    @Test
    void validateDelete_deveLancarQuandoIdForNulo() {
        assertValidationError(
                () -> validation.validateDelete(null),
                ErrorConstants.INVALID_ID,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateDelete_deveLancarQuandoPesquisadorNaoExiste() {
        when(pesquisadorRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        assertValidationError(
                () -> validation.validateDelete(99L),
                ErrorConstants.ID_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void validateDelete_naoDeveLancarExcecaoQuandoPesquisadorExiste() {
        when(pesquisadorRepository.findById(1L)).thenReturn(java.util.Optional.of(pesquisadorValido()));

        assertThatCode(() -> validation.validateDelete(1L)).doesNotThrowAnyException();
    }
}
