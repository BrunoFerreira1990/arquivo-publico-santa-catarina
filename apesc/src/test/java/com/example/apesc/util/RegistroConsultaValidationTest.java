package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoDocumental;
import com.example.apesc.model.Funcionario;
import com.example.apesc.model.Pesquisador;
import com.example.apesc.model.RegistroConsulta;
import com.example.apesc.model.enums.TipoConsulta;
import com.example.apesc.repository.RegistroConsultaRepository;
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
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistroConsultaValidationTest {

    @Mock
    private RegistroConsultaRepository registroConsultaRepository;

    private RegistroConsultaValidation validation;

    @BeforeEach
    void setUp() {
        validation = new RegistroConsultaValidation(registroConsultaRepository);
    }

    private RegistroConsulta registroValido() {
        RegistroConsulta registro = new RegistroConsulta();

        Pesquisador pesquisador = new Pesquisador();
        pesquisador.setId(1L);
        registro.setPesquisador(pesquisador);

        registro.setDataPesquisa(LocalDate.of(2026, 8, 14));
        registro.setTipoConsulta(TipoConsulta.PRESENCIAL);

        AcervoDocumental acervo = new AcervoDocumental();
        acervo.setId(1L);
        registro.setAcervoDocumental(acervo);

        registro.setPeriodo("Manhã");
        registro.setQuantidade(3);

        Funcionario funcionario = new Funcionario();
        funcionario.setId(1L);
        registro.setFuncionario(funcionario);

        return registro;
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
    void validateSave_naoDeveLancarExcecaoQuandoTodosOsCamposValidosENaoHaDuplicidade() {
        when(registroConsultaRepository.existsDuplicado(
                any(), any(), any(), any(), any(), any(), isNull())).thenReturn(false);

        assertThatCode(() -> validation.validateSave(registroValido())).doesNotThrowAnyException();
    }

    // ---------- validateSave: duplicidade ----------

    @Test
    void validateSave_deveLancarConflitoQuandoJaExisteRegistroIdentico() {
        when(registroConsultaRepository.existsDuplicado(
                any(), any(), any(), any(), any(), any(), isNull())).thenReturn(true);

        assertValidationError(
                () -> validation.validateSave(registroValido()),
                ErrorConstants.REGISTRO_CONSULTA_DUPLICADO,
                HttpStatus.CONFLICT
        );
    }

    @Test
    void validateSave_naoDeveConsiderarOFuncionarioNaComparacaoDeDuplicidade() {
        when(registroConsultaRepository.existsDuplicado(
                any(), any(), any(), any(), any(), any(), any())).thenReturn(false);

        validation.validateSave(registroValido());

        // o funcionarioId nao entra como argumento da checagem — so os campos que
        // descrevem a consulta em si. Isso e o que garante que uma pessoa diferente
        // registrando os mesmos dados tambem seja pega como duplicidade.
        verify(registroConsultaRepository).existsDuplicado(
                eq(1L), eq(LocalDate.of(2026, 8, 14)), eq(TipoConsulta.PRESENCIAL),
                eq(1L), eq("Manhã"), eq(3), isNull());
    }

    @Test
    void validateSave_naoDeveConsultarDuplicidadeQuandoCampoObrigatorioAusente() {
        RegistroConsulta registro = registroValido();
        registro.setPeriodo(null);

        assertThatThrownBy(() -> validation.validateSave(registro)).isInstanceOf(CustomException.class);

        verify(registroConsultaRepository, never())
                .existsDuplicado(any(), any(), any(), any(), any(), any(), any());
    }

    // ---------- validateSave: campos obrigatórios ----------

    private static Stream<Arguments> camposObrigatorios() {
        return Stream.of(
                Arguments.of((Consumer<RegistroConsulta>) r -> r.setPesquisador(null), ErrorConstants.PESQUISADOR_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.getPesquisador().setId(null), ErrorConstants.PESQUISADOR_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.setDataPesquisa(null), ErrorConstants.DATA_PESQUISA_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.setTipoConsulta(null), ErrorConstants.TIPO_CONSULTA_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.setAcervoDocumental(null), ErrorConstants.ACERVO_DOCUMENTAL_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.getAcervoDocumental().setId(null), ErrorConstants.ACERVO_DOCUMENTAL_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.setPeriodo(null), ErrorConstants.PERIODO_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.setPeriodo("   "), ErrorConstants.PERIODO_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.setQuantidade(null), ErrorConstants.QUANTIDADE_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.setFuncionario(null), ErrorConstants.FUNCIONARIO_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.getFuncionario().setId(null), ErrorConstants.FUNCIONARIO_REQUIRED)
        );
    }

    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("camposObrigatorios")
    void validateSave_deveRejeitarCampoObrigatorioAusente(Consumer<RegistroConsulta> mutador, ErrorConstants erroEsperado) {
        RegistroConsulta registro = registroValido();
        mutador.accept(registro);

        assertValidationError(() -> validation.validateSave(registro), erroEsperado, HttpStatus.BAD_REQUEST);
    }

    // ---------- validateUpdate ----------

    @Test
    void validateUpdate_deveLancarQuandoIdForNulo() {
        RegistroConsulta registro = registroValido();
        registro.setId(null);

        assertValidationError(
                () -> validation.validateUpdate(registro),
                ErrorConstants.INVALID_ID,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateUpdate_devePermitirSalvarAsMesmasInformacoesDeVoltaNoProprioRegistro() {
        RegistroConsulta registro = registroValido();
        registro.setId(1L);
        // o unico registro identico no banco e o proprio (mesmo ID) -> repositorio simula a exclusao.
        when(registroConsultaRepository.existsDuplicado(
                any(), any(), any(), any(), any(), any(), eq(1L))).thenReturn(false);

        assertThatCode(() -> validation.validateUpdate(registro)).doesNotThrowAnyException();
    }

    @Test
    void validateUpdate_deveLancarConflitoQuandoOutroRegistroComIdDiferenteEhIdentico() {
        RegistroConsulta registro = registroValido();
        registro.setId(1L);
        // existe outro registro (ID diferente) com os mesmos dados.
        when(registroConsultaRepository.existsDuplicado(
                any(), any(), any(), any(), any(), any(), eq(1L))).thenReturn(true);

        assertValidationError(
                () -> validation.validateUpdate(registro),
                ErrorConstants.REGISTRO_CONSULTA_DUPLICADO,
                HttpStatus.CONFLICT
        );
    }

    @Test
    void validateUpdate_deveExcluirOProprioIdDaBuscaDeDuplicidade() {
        RegistroConsulta registro = registroValido();
        registro.setId(7L);
        when(registroConsultaRepository.existsDuplicado(
                any(), any(), any(), any(), any(), any(), any())).thenReturn(false);

        validation.validateUpdate(registro);

        verify(registroConsultaRepository).existsDuplicado(
                eq(1L), eq(LocalDate.of(2026, 8, 14)), eq(TipoConsulta.PRESENCIAL),
                eq(1L), eq("Manhã"), eq(3), eq(7L));
    }

    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("camposObrigatorios")
    void validateUpdate_deveRejeitarCampoObrigatorioAusente(Consumer<RegistroConsulta> mutador, ErrorConstants erroEsperado) {
        RegistroConsulta registro = registroValido();
        registro.setId(1L);
        mutador.accept(registro);

        assertValidationError(() -> validation.validateUpdate(registro), erroEsperado, HttpStatus.BAD_REQUEST);
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
    void validateDelete_deveLancarQuandoRegistroNaoExiste() {
        when(registroConsultaRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        assertValidationError(
                () -> validation.validateDelete(99L),
                ErrorConstants.ID_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void validateDelete_naoDeveLancarExcecaoQuandoRegistroExiste() {
        when(registroConsultaRepository.findById(1L)).thenReturn(java.util.Optional.of(registroValido()));

        assertThatCode(() -> validation.validateDelete(1L)).doesNotThrowAnyException();
    }
}
