package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.DiagnosticoRestauracao;
import com.example.apesc.model.Funcionario;
import com.example.apesc.model.ProcedimentoRestauracao;
import com.example.apesc.repository.DiagnosticoRestauracaoRepository;
import com.example.apesc.repository.FuncionarioRepository;
import com.example.apesc.repository.ProcedimentoRestauracaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcedimentoRestauracaoValidationTest {

    @Mock
    private DiagnosticoRestauracaoRepository diagnosticoRestauracaoRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private ProcedimentoRestauracaoRepository procedimentoRestauracaoRepository;

    private ProcedimentoRestauracaoValidation validation;

    @BeforeEach
    void setUp() {
        validation = new ProcedimentoRestauracaoValidation(diagnosticoRestauracaoRepository, funcionarioRepository);

        // baseline usado pelo procedimentoValido(): diagnostico 10L e funcionario 1L
        // existem. lenient porque nem todo teste chega a consultar os dois (ex.:
        // campo obrigatorio ausente antes deles na ordem de validacao).
        lenient().when(diagnosticoRestauracaoRepository.findById(10L)).thenReturn(Optional.of(new DiagnosticoRestauracao()));
        lenient().when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(new Funcionario()));
    }

    private ProcedimentoRestauracao procedimentoValido() {
        ProcedimentoRestauracao procedimento = new ProcedimentoRestauracao();

        DiagnosticoRestauracao diagnostico = new DiagnosticoRestauracao();
        diagnostico.setId(10);
        procedimento.setDiagnosticoRestauracao(diagnostico);

        Funcionario restaurador = new Funcionario();
        restaurador.setId(1L);
        procedimento.setResponsavelRestauracao(restaurador);

        procedimento.setDataSaida(LocalDate.of(2026, 1, 25));

        return procedimento;
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
    void validateSave_naoDeveLancarQuandoTudoValido() {
        when(procedimentoRestauracaoRepository.existsByDiagnosticoRestauracaoId(10)).thenReturn(false);

        assertThatCode(() -> validation.validateSave(procedimentoValido(), procedimentoRestauracaoRepository))
                .doesNotThrowAnyException();
    }

    @Test
    void validateSave_deveLancarConflitoQuandoDiagnosticoJaPossuiProcedimento() {
        when(procedimentoRestauracaoRepository.existsByDiagnosticoRestauracaoId(10)).thenReturn(true);

        assertValidationError(
                () -> validation.validateSave(procedimentoValido(), procedimentoRestauracaoRepository),
                ErrorConstants.DIAGNOSTICO_RESTAURACAO_JA_POSSUI_PROCEDIMENTO,
                HttpStatus.CONFLICT
        );
    }

    // ---------- validateSave: campos obrigatorios ----------

    private static Stream<Arguments> camposObrigatorios() {
        return Stream.of(
                Arguments.of((Consumer<ProcedimentoRestauracao>) p -> p.setDiagnosticoRestauracao(null), ErrorConstants.DIAGNOSTICO_RESTAURACAO_REQUIRED),
                Arguments.of((Consumer<ProcedimentoRestauracao>) p -> p.getDiagnosticoRestauracao().setId(null), ErrorConstants.DIAGNOSTICO_RESTAURACAO_REQUIRED),
                Arguments.of((Consumer<ProcedimentoRestauracao>) p -> p.setResponsavelRestauracao(null), ErrorConstants.FUNCIONARIO_REQUIRED),
                Arguments.of((Consumer<ProcedimentoRestauracao>) p -> p.getResponsavelRestauracao().setId(null), ErrorConstants.FUNCIONARIO_REQUIRED),
                Arguments.of((Consumer<ProcedimentoRestauracao>) p -> p.setDataSaida(null), ErrorConstants.DATA_SAIDA_REQUIRED)
        );
    }

    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("camposObrigatorios")
    void validateSave_deveRejeitarCampoObrigatorioAusente(Consumer<ProcedimentoRestauracao> mutador, ErrorConstants erroEsperado) {
        ProcedimentoRestauracao procedimento = procedimentoValido();
        mutador.accept(procedimento);

        assertValidationError(
                () -> validation.validateSave(procedimento, procedimentoRestauracaoRepository),
                erroEsperado,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateSave_deveLancarNotFoundQuandoDiagnosticoNaoExiste() {
        ProcedimentoRestauracao procedimento = procedimentoValido();
        procedimento.getDiagnosticoRestauracao().setId(99);
        when(diagnosticoRestauracaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertValidationError(
                () -> validation.validateSave(procedimento, procedimentoRestauracaoRepository),
                ErrorConstants.DIAGNOSTICO_RESTAURACAO_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void validateSave_deveLancarNotFoundQuandoRestauradorNaoExiste() {
        ProcedimentoRestauracao procedimento = procedimentoValido();
        procedimento.getResponsavelRestauracao().setId(99L);
        when(funcionarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertValidationError(
                () -> validation.validateSave(procedimento, procedimentoRestauracaoRepository),
                ErrorConstants.FUNCIONARIO_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    // ---------- validateUpdate ----------

    @Test
    void validateUpdate_deveLancarQuandoIdForNulo() {
        ProcedimentoRestauracao procedimento = procedimentoValido();
        procedimento.setId(null);

        assertValidationError(
                () -> validation.validateUpdate(procedimento, procedimentoRestauracaoRepository),
                ErrorConstants.INVALID_ID,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateUpdate_deveLancarQuandoProcedimentoNaoExiste() {
        ProcedimentoRestauracao procedimento = procedimentoValido();
        procedimento.setId(1);
        when(procedimentoRestauracaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertValidationError(
                () -> validation.validateUpdate(procedimento, procedimentoRestauracaoRepository),
                ErrorConstants.ID_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void validateUpdate_naoDeveLancarQuandoValido() {
        ProcedimentoRestauracao procedimento = procedimentoValido();
        procedimento.setId(1);
        when(procedimentoRestauracaoRepository.findById(1L)).thenReturn(Optional.of(procedimento));
        when(procedimentoRestauracaoRepository.existsByDiagnosticoRestauracaoIdAndIdNot(10, 1)).thenReturn(false);

        assertThatCode(() -> validation.validateUpdate(procedimento, procedimentoRestauracaoRepository))
                .doesNotThrowAnyException();
    }

    @Test
    void validateUpdate_deveLancarConflitoQuandoOutroProcedimentoJaUsaEsseDiagnostico() {
        ProcedimentoRestauracao procedimento = procedimentoValido();
        procedimento.setId(1);
        when(procedimentoRestauracaoRepository.findById(1L)).thenReturn(Optional.of(procedimento));
        when(procedimentoRestauracaoRepository.existsByDiagnosticoRestauracaoIdAndIdNot(10, 1)).thenReturn(true);

        assertValidationError(
                () -> validation.validateUpdate(procedimento, procedimentoRestauracaoRepository),
                ErrorConstants.DIAGNOSTICO_RESTAURACAO_JA_POSSUI_PROCEDIMENTO,
                HttpStatus.CONFLICT
        );
    }

    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("camposObrigatorios")
    void validateUpdate_deveRejeitarCampoObrigatorioAusente(Consumer<ProcedimentoRestauracao> mutador, ErrorConstants erroEsperado) {
        ProcedimentoRestauracao procedimento = procedimentoValido();
        procedimento.setId(1);
        mutador.accept(procedimento);
        when(procedimentoRestauracaoRepository.findById(1L)).thenReturn(Optional.of(procedimento));

        assertValidationError(
                () -> validation.validateUpdate(procedimento, procedimentoRestauracaoRepository),
                erroEsperado,
                HttpStatus.BAD_REQUEST
        );
    }

    // ---------- validateDelete ----------

    @Test
    void validateDelete_deveLancarQuandoIdForNulo() {
        assertValidationError(
                () -> validation.validateDelete(null, procedimentoRestauracaoRepository),
                ErrorConstants.INVALID_ID,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateDelete_deveLancarQuandoProcedimentoNaoExiste() {
        when(procedimentoRestauracaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertValidationError(
                () -> validation.validateDelete(99L, procedimentoRestauracaoRepository),
                ErrorConstants.ID_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void validateDelete_naoDeveLancarQuandoProcedimentoExiste() {
        when(procedimentoRestauracaoRepository.findById(1L)).thenReturn(Optional.of(procedimentoValido()));

        assertThatCode(() -> validation.validateDelete(1L, procedimentoRestauracaoRepository))
                .doesNotThrowAnyException();
    }
}
