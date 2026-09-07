package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoCartografico;
import com.example.apesc.model.AcervoDocumentalProcessos;
import com.example.apesc.model.AcervoDocumentalTombo;
import com.example.apesc.model.AcervoIconografico;
import com.example.apesc.model.BibliotecaApoio;
import com.example.apesc.model.BibliotecaLivrosPeriodicos;
import com.example.apesc.model.DiagnosticoRestauracao;
import com.example.apesc.model.Funcionario;
import com.example.apesc.repository.AcervoCartograficoRepository;
import com.example.apesc.repository.AcervoDocumentalProcessosRepository;
import com.example.apesc.repository.AcervoDocumentalTomboRepository;
import com.example.apesc.repository.AcervoIconograficoRepository;
import com.example.apesc.repository.BibliotecaApoioRepository;
import com.example.apesc.repository.BibliotecaLivrosPeriodicosRepository;
import com.example.apesc.repository.DiagnosticoRestauracaoRepository;
import com.example.apesc.repository.FuncionarioRepository;
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
class DiagnosticoRestauracaoValidationTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private AcervoDocumentalTomboRepository acervoDocumentalTomboRepository;

    @Mock
    private AcervoDocumentalProcessosRepository acervoDocumentalProcessosRepository;

    @Mock
    private AcervoCartograficoRepository acervoCartograficoRepository;

    @Mock
    private AcervoIconograficoRepository acervoIconograficoRepository;

    @Mock
    private BibliotecaLivrosPeriodicosRepository bibliotecaLivrosPeriodicosRepository;

    @Mock
    private BibliotecaApoioRepository bibliotecaApoioRepository;

    @Mock
    private DiagnosticoRestauracaoRepository diagnosticoRestauracaoRepository;

    private DiagnosticoRestauracaoValidation validation;

    @BeforeEach
    void setUp() {
        validation = new DiagnosticoRestauracaoValidation(
                funcionarioRepository, acervoDocumentalTomboRepository, acervoDocumentalProcessosRepository,
                acervoCartograficoRepository, acervoIconograficoRepository, bibliotecaLivrosPeriodicosRepository,
                bibliotecaApoioRepository);

        // baseline usado pelo diagnosticoValido(): funcionario 1L e tombo 2L existem.
        // lenient porque nem todo teste chega a consultar os dois (ex.: campo
        // obrigatorio ausente antes deles na ordem de validacao).
        lenient().when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(new Funcionario()));
        lenient().when(acervoDocumentalTomboRepository.findById(2L)).thenReturn(Optional.of(new AcervoDocumentalTombo()));
    }

    private DiagnosticoRestauracao diagnosticoValido() {
        DiagnosticoRestauracao diagnostico = new DiagnosticoRestauracao();
        diagnostico.setNumeroDocumento(90001);

        Funcionario responsavel = new Funcionario();
        responsavel.setId(1L);
        diagnostico.setResponsavelRestauracao(responsavel);

        diagnostico.setDataDiagnostico(LocalDate.of(2026, 1, 10));

        AcervoDocumentalTombo tombo = new AcervoDocumentalTombo();
        tombo.setId(2L);
        diagnostico.setAcervoDocumentalTombo(tombo);

        return diagnostico;
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
        when(diagnosticoRestauracaoRepository.existsByNumeroDocumento(90001)).thenReturn(false);

        assertThatCode(() -> validation.validateSave(diagnosticoValido(), diagnosticoRestauracaoRepository))
                .doesNotThrowAnyException();
    }

    @Test
    void validateSave_deveLancarConflitoQuandoNumeroDocumentoJaCadastrado() {
        when(diagnosticoRestauracaoRepository.existsByNumeroDocumento(90001)).thenReturn(true);

        assertValidationError(
                () -> validation.validateSave(diagnosticoValido(), diagnosticoRestauracaoRepository),
                ErrorConstants.NUMERO_DOCUMENTO_DUPLICADO,
                HttpStatus.CONFLICT
        );
    }

    // ---------- validateSave: campos obrigatorios ----------

    private static Stream<Arguments> camposObrigatorios() {
        return Stream.of(
                Arguments.of((Consumer<DiagnosticoRestauracao>) d -> d.setNumeroDocumento(null), ErrorConstants.NUMERO_DOCUMENTO_REQUIRED),
                Arguments.of((Consumer<DiagnosticoRestauracao>) d -> d.setResponsavelRestauracao(null), ErrorConstants.FUNCIONARIO_REQUIRED),
                Arguments.of((Consumer<DiagnosticoRestauracao>) d -> d.getResponsavelRestauracao().setId(null), ErrorConstants.FUNCIONARIO_REQUIRED),
                Arguments.of((Consumer<DiagnosticoRestauracao>) d -> d.setDataDiagnostico(null), ErrorConstants.DATA_DIAGNOSTICO_REQUIRED),
                Arguments.of((Consumer<DiagnosticoRestauracao>) d -> d.setAcervoDocumentalTombo(null), ErrorConstants.DIAGNOSTICO_RESTAURACAO_ACERVO_REQUIRED)
        );
    }

    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("camposObrigatorios")
    void validateSave_deveRejeitarCampoObrigatorioAusente(Consumer<DiagnosticoRestauracao> mutador, ErrorConstants erroEsperado) {
        DiagnosticoRestauracao diagnostico = diagnosticoValido();
        mutador.accept(diagnostico);

        assertValidationError(
                () -> validation.validateSave(diagnostico, diagnosticoRestauracaoRepository),
                erroEsperado,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateSave_deveLancarNotFoundQuandoResponsavelNaoExiste() {
        DiagnosticoRestauracao diagnostico = diagnosticoValido();
        diagnostico.getResponsavelRestauracao().setId(99L);
        when(funcionarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertValidationError(
                () -> validation.validateSave(diagnostico, diagnosticoRestauracaoRepository),
                ErrorConstants.FUNCIONARIO_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    // ---------- validateSave: as 6 FKs polimorficas de acervo ----------

    @Test
    void validateSave_deveLancarNotFoundQuandoAcervoDocumentalTomboNaoExiste() {
        DiagnosticoRestauracao diagnostico = diagnosticoValido();
        diagnostico.getAcervoDocumentalTombo().setId(99L);
        when(acervoDocumentalTomboRepository.findById(99L)).thenReturn(Optional.empty());

        assertValidationError(
                () -> validation.validateSave(diagnostico, diagnosticoRestauracaoRepository),
                ErrorConstants.ACERVO_DOCUMENTAL_TOMBO_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void validateSave_deveLancarNotFoundQuandoAcervoDocumentalProcessosNaoExiste() {
        DiagnosticoRestauracao diagnostico = diagnosticoValido();
        diagnostico.setAcervoDocumentalTombo(null);
        AcervoDocumentalProcessos processos = new AcervoDocumentalProcessos();
        processos.setId(9L);
        diagnostico.setAcervoDocumentalProcessos(processos);
        when(acervoDocumentalProcessosRepository.findById(9L)).thenReturn(Optional.empty());

        assertValidationError(
                () -> validation.validateSave(diagnostico, diagnosticoRestauracaoRepository),
                ErrorConstants.ACERVO_DOCUMENTAL_PROCESSOS_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void validateSave_deveLancarNotFoundQuandoAcervoCartograficoNaoExiste() {
        DiagnosticoRestauracao diagnostico = diagnosticoValido();
        diagnostico.setAcervoDocumentalTombo(null);
        AcervoCartografico cartografico = new AcervoCartografico();
        cartografico.setId(9L);
        diagnostico.setAcervoCartografico(cartografico);
        when(acervoCartograficoRepository.findById(9L)).thenReturn(Optional.empty());

        assertValidationError(
                () -> validation.validateSave(diagnostico, diagnosticoRestauracaoRepository),
                ErrorConstants.ACERVO_CARTOGRAFICO_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void validateSave_deveLancarNotFoundQuandoAcervoIconograficoNaoExiste() {
        DiagnosticoRestauracao diagnostico = diagnosticoValido();
        diagnostico.setAcervoDocumentalTombo(null);
        AcervoIconografico iconografico = new AcervoIconografico();
        iconografico.setId(9L);
        diagnostico.setAcervoIconografico(iconografico);
        when(acervoIconograficoRepository.findById(9L)).thenReturn(Optional.empty());

        assertValidationError(
                () -> validation.validateSave(diagnostico, diagnosticoRestauracaoRepository),
                ErrorConstants.ACERVO_ICONOGRAFICO_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void validateSave_deveLancarNotFoundQuandoBibliotecaLivrosPeriodicosNaoExiste() {
        DiagnosticoRestauracao diagnostico = diagnosticoValido();
        diagnostico.setAcervoDocumentalTombo(null);
        BibliotecaLivrosPeriodicos biblioteca = new BibliotecaLivrosPeriodicos();
        biblioteca.setId(9L);
        diagnostico.setBibliotecaLivrosPeriodicos(biblioteca);
        when(bibliotecaLivrosPeriodicosRepository.findById(9L)).thenReturn(Optional.empty());

        assertValidationError(
                () -> validation.validateSave(diagnostico, diagnosticoRestauracaoRepository),
                ErrorConstants.BIBLIOTECA_LIVROS_PERIODICOS_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void validateSave_deveLancarNotFoundQuandoBibliotecaApoioNaoExiste() {
        DiagnosticoRestauracao diagnostico = diagnosticoValido();
        diagnostico.setAcervoDocumentalTombo(null);
        BibliotecaApoio biblioteca = new BibliotecaApoio();
        biblioteca.setId(9L);
        diagnostico.setBibliotecaApoio(biblioteca);
        when(bibliotecaApoioRepository.findById(9L)).thenReturn(Optional.empty());

        assertValidationError(
                () -> validation.validateSave(diagnostico, diagnosticoRestauracaoRepository),
                ErrorConstants.BIBLIOTECA_APOIO_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void validateSave_devePermitirMaisDeUmaFkDeAcervoPreenchidaAoMesmoTempo() {
        // "pelo menos uma", nao "exatamente uma" — diferente do RegistroConsultaItem.
        DiagnosticoRestauracao diagnostico = diagnosticoValido();
        AcervoCartografico cartografico = new AcervoCartografico();
        cartografico.setId(5L);
        diagnostico.setAcervoCartografico(cartografico);
        when(acervoCartograficoRepository.findById(5L)).thenReturn(Optional.of(cartografico));
        when(diagnosticoRestauracaoRepository.existsByNumeroDocumento(90001)).thenReturn(false);

        assertThatCode(() -> validation.validateSave(diagnostico, diagnosticoRestauracaoRepository))
                .doesNotThrowAnyException();
    }

    // ---------- validateUpdate ----------

    @Test
    void validateUpdate_deveLancarQuandoIdForNulo() {
        DiagnosticoRestauracao diagnostico = diagnosticoValido();
        diagnostico.setId(null);

        assertValidationError(
                () -> validation.validateUpdate(diagnostico, diagnosticoRestauracaoRepository),
                ErrorConstants.INVALID_ID,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateUpdate_deveLancarQuandoDiagnosticoNaoExiste() {
        DiagnosticoRestauracao diagnostico = diagnosticoValido();
        diagnostico.setId(1);
        when(diagnosticoRestauracaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertValidationError(
                () -> validation.validateUpdate(diagnostico, diagnosticoRestauracaoRepository),
                ErrorConstants.ID_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void validateUpdate_naoDeveLancarQuandoValido() {
        DiagnosticoRestauracao diagnostico = diagnosticoValido();
        diagnostico.setId(1);
        when(diagnosticoRestauracaoRepository.findById(1L)).thenReturn(Optional.of(diagnostico));
        when(diagnosticoRestauracaoRepository.existsByNumeroDocumentoAndIdNot(90001, 1)).thenReturn(false);

        assertThatCode(() -> validation.validateUpdate(diagnostico, diagnosticoRestauracaoRepository))
                .doesNotThrowAnyException();
    }

    @Test
    void validateUpdate_deveLancarConflitoQuandoOutroRegistroTemMesmoNumeroDocumento() {
        DiagnosticoRestauracao diagnostico = diagnosticoValido();
        diagnostico.setId(1);
        when(diagnosticoRestauracaoRepository.findById(1L)).thenReturn(Optional.of(diagnostico));
        when(diagnosticoRestauracaoRepository.existsByNumeroDocumentoAndIdNot(90001, 1)).thenReturn(true);

        assertValidationError(
                () -> validation.validateUpdate(diagnostico, diagnosticoRestauracaoRepository),
                ErrorConstants.NUMERO_DOCUMENTO_DUPLICADO,
                HttpStatus.CONFLICT
        );
    }

    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("camposObrigatorios")
    void validateUpdate_deveRejeitarCampoObrigatorioAusente(Consumer<DiagnosticoRestauracao> mutador, ErrorConstants erroEsperado) {
        DiagnosticoRestauracao diagnostico = diagnosticoValido();
        diagnostico.setId(1);
        mutador.accept(diagnostico);
        when(diagnosticoRestauracaoRepository.findById(1L)).thenReturn(Optional.of(diagnostico));

        assertValidationError(
                () -> validation.validateUpdate(diagnostico, diagnosticoRestauracaoRepository),
                erroEsperado,
                HttpStatus.BAD_REQUEST
        );
    }

    // ---------- validateDelete ----------

    @Test
    void validateDelete_deveLancarQuandoIdForNulo() {
        assertValidationError(
                () -> validation.validateDelete(null, diagnosticoRestauracaoRepository),
                ErrorConstants.INVALID_ID,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateDelete_deveLancarQuandoDiagnosticoNaoExiste() {
        when(diagnosticoRestauracaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertValidationError(
                () -> validation.validateDelete(99L, diagnosticoRestauracaoRepository),
                ErrorConstants.ID_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void validateDelete_naoDeveLancarQuandoDiagnosticoExiste() {
        when(diagnosticoRestauracaoRepository.findById(1L)).thenReturn(Optional.of(diagnosticoValido()));

        assertThatCode(() -> validation.validateDelete(1L, diagnosticoRestauracaoRepository))
                .doesNotThrowAnyException();
    }
}
