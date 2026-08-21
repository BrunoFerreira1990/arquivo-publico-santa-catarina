package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoCartografico;
import com.example.apesc.model.AcervoDocumental;
import com.example.apesc.model.Funcionario;
import com.example.apesc.model.Pesquisador;
import com.example.apesc.model.RegistroConsulta;
import com.example.apesc.model.RegistroConsultaItem;
import com.example.apesc.model.enums.TipoConsulta;
import com.example.apesc.repository.RegistroConsultaItemRepository;
import com.example.apesc.repository.RegistroConsultaRepository;
import com.example.apesc.specification.RegistroConsultaSearchFilter;
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
import java.util.HashSet;
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

    @Mock
    private RegistroConsultaItemRepository registroConsultaItemRepository;

    private RegistroConsultaValidation validation;

    @BeforeEach
    void setUp() {
        validation = new RegistroConsultaValidation(registroConsultaRepository, registroConsultaItemRepository);
    }

    // Registro valido com exatamente 1 item documental, pra manter os casos de teste
    // comparaveis com o comportamento anterior (1 registro = 1 acervo).
    private RegistroConsulta registroValido() {
        RegistroConsulta registro = new RegistroConsulta();

        Pesquisador pesquisador = new Pesquisador();
        pesquisador.setId(1L);
        registro.setPesquisador(pesquisador);

        registro.setDataPesquisa(LocalDate.of(2026, 8, 14));
        registro.setTipoConsulta(TipoConsulta.PRESENCIAL);

        AcervoDocumental acervo = new AcervoDocumental();
        acervo.setId(1L);

        RegistroConsultaItem item = new RegistroConsultaItem();
        item.setAcervoDocumental(acervo);
        item.setPeriodo("Manhã");
        item.setQuantidade(3);
        registro.addItem(item);

        Funcionario funcionario = new Funcionario();
        funcionario.setId(1L);
        registro.setFuncionario(funcionario);

        return registro;
    }

    private RegistroConsultaItem primeiroItem(RegistroConsulta registro) {
        return registro.getItens().iterator().next();
    }

    private RegistroConsultaItem umItemCartografico() {
        AcervoCartografico acervo = new AcervoCartografico();
        acervo.setId(9L);

        RegistroConsultaItem item = new RegistroConsultaItem();
        item.setAcervoCartografico(acervo);
        item.setPeriodo("1900-1910");
        item.setQuantidade(2);
        return item;
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
        when(registroConsultaItemRepository.existsDuplicadoDocumental(
                any(), any(), any(), any(), any(), any(), isNull())).thenReturn(false);

        assertThatCode(() -> validation.validateSave(registroValido())).doesNotThrowAnyException();
    }

    // ---------- validateSave: duplicidade ----------

    @Test
    void validateSave_deveLancarConflitoQuandoJaExisteItemIdentico() {
        when(registroConsultaItemRepository.existsDuplicadoDocumental(
                any(), any(), any(), any(), any(), any(), isNull())).thenReturn(true);

        assertValidationError(
                () -> validation.validateSave(registroValido()),
                ErrorConstants.REGISTRO_CONSULTA_DUPLICADO,
                HttpStatus.CONFLICT
        );
    }

    @Test
    void validateSave_naoDeveConsiderarOFuncionarioNaComparacaoDeDuplicidade() {
        when(registroConsultaItemRepository.existsDuplicadoDocumental(
                any(), any(), any(), any(), any(), any(), any())).thenReturn(false);

        validation.validateSave(registroValido());

        // o funcionarioId nao entra como argumento da checagem — so os campos que
        // descrevem a consulta em si. Isso e o que garante que uma pessoa diferente
        // registrando os mesmos dados tambem seja pega como duplicidade.
        verify(registroConsultaItemRepository).existsDuplicadoDocumental(
                eq(1L), eq(LocalDate.of(2026, 8, 14)), eq(TipoConsulta.PRESENCIAL),
                eq(1L), eq("Manhã"), eq(3), isNull());
    }

    @Test
    void validateSave_naoDeveConsultarDuplicidadeQuandoCampoObrigatorioAusente() {
        RegistroConsulta registro = registroValido();
        primeiroItem(registro).setPeriodo(null);

        assertThatThrownBy(() -> validation.validateSave(registro)).isInstanceOf(CustomException.class);

        verify(registroConsultaItemRepository, never())
                .existsDuplicadoDocumental(any(), any(), any(), any(), any(), any(), any());
        verify(registroConsultaItemRepository, never())
                .existsDuplicadoCartografico(any(), any(), any(), any(), any(), any(), any());
    }

    // ---------- validateSave: campos obrigatórios ----------

    private static Stream<Arguments> camposObrigatorios() {
        return Stream.of(
                Arguments.of((Consumer<RegistroConsulta>) r -> r.setPesquisador(null), ErrorConstants.PESQUISADOR_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.getPesquisador().setId(null), ErrorConstants.PESQUISADOR_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.setDataPesquisa(null), ErrorConstants.DATA_PESQUISA_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.setTipoConsulta(null), ErrorConstants.TIPO_CONSULTA_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.setItens(new HashSet<>()), ErrorConstants.ITENS_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.getItens().iterator().next().setAcervoDocumental(null), ErrorConstants.ACERVO_ITEM_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.getItens().iterator().next().getAcervoDocumental().setId(null), ErrorConstants.ACERVO_ITEM_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.getItens().iterator().next().setPeriodo(null), ErrorConstants.PERIODO_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.getItens().iterator().next().setPeriodo("   "), ErrorConstants.PERIODO_REQUIRED),
                Arguments.of((Consumer<RegistroConsulta>) r -> r.getItens().iterator().next().setQuantidade(null), ErrorConstants.QUANTIDADE_REQUIRED),
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

    @Test
    void validateSave_deveRejeitarItemComOsDoisTiposDeAcervoAoMesmoTempo() {
        RegistroConsulta registro = registroValido();
        AcervoCartografico cartografico = new AcervoCartografico();
        cartografico.setId(9L);
        primeiroItem(registro).setAcervoCartografico(cartografico); // ja tem acervoDocumental do registroValido()

        assertValidationError(
                () -> validation.validateSave(registro),
                ErrorConstants.ACERVO_ITEM_TIPO_AMBIGUO,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateSave_deveRejeitarOMesmoAcervoDocumentalRepetidoNoMesmoRegistro() {
        RegistroConsulta registro = registroValido();

        AcervoDocumental mesmoAcervo = new AcervoDocumental();
        mesmoAcervo.setId(primeiroItem(registro).getAcervoDocumental().getId());

        RegistroConsultaItem itemRepetido = new RegistroConsultaItem();
        itemRepetido.setAcervoDocumental(mesmoAcervo);
        itemRepetido.setPeriodo("Tarde");
        itemRepetido.setQuantidade(1);
        registro.addItem(itemRepetido);

        assertValidationError(
                () -> validation.validateSave(registro),
                ErrorConstants.ACERVO_DOCUMENTAL_DUPLICADO_NO_REGISTRO,
                HttpStatus.BAD_REQUEST
        );
    }

    // ---------- validateSave: itens cartograficos, misturados com documentais ----------

    @Test
    void validateSave_devePermitirRegistroSoComItemCartograficoSemItemDocumental() {
        RegistroConsulta registro = registroValido();
        registro.setItens(new HashSet<>());
        registro.addItem(umItemCartografico());

        when(registroConsultaItemRepository.existsDuplicadoCartografico(
                any(), any(), any(), any(), any(), any(), isNull())).thenReturn(false);

        assertThatCode(() -> validation.validateSave(registro)).doesNotThrowAnyException();
        verify(registroConsultaItemRepository, never())
                .existsDuplicadoDocumental(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void validateSave_devePermitirRegistroComItemDocumentalEItemCartograficoMisturados() {
        RegistroConsulta registro = registroValido();
        registro.addItem(umItemCartografico());

        when(registroConsultaItemRepository.existsDuplicadoDocumental(
                any(), any(), any(), any(), any(), any(), isNull())).thenReturn(false);
        when(registroConsultaItemRepository.existsDuplicadoCartografico(
                any(), any(), any(), any(), any(), any(), isNull())).thenReturn(false);

        assertThatCode(() -> validation.validateSave(registro)).doesNotThrowAnyException();
        verify(registroConsultaItemRepository).existsDuplicadoDocumental(any(), any(), any(), any(), any(), any(), isNull());
        verify(registroConsultaItemRepository).existsDuplicadoCartografico(any(), any(), any(), any(), any(), any(), isNull());
    }

    @Test
    void validateSave_deveLancarConflitoQuandoItemCartograficoJaExisteIdentico() {
        RegistroConsulta registro = registroValido();
        registro.addItem(umItemCartografico());

        // nao estuba existsDuplicadoDocumental: Mockito ja retorna false por padrao pra
        // metodo boolean nao estubado, e a ordem de iteracao de um Set nao eh garantida
        // (esse item pode ou nao chegar a ser checado antes do cartografico abaixo).
        when(registroConsultaItemRepository.existsDuplicadoCartografico(
                any(), any(), any(), any(), any(), any(), isNull())).thenReturn(true);

        assertValidationError(
                () -> validation.validateSave(registro),
                ErrorConstants.REGISTRO_CONSULTA_DUPLICADO,
                HttpStatus.CONFLICT
        );
    }

    @Test
    void validateSave_deveRejeitarOMesmoAcervoCartograficoRepetidoNoMesmoRegistro() {
        RegistroConsulta registro = registroValido();
        registro.addItem(umItemCartografico());

        // segundo item com o MESMO acervoId (9L), mas periodo/quantidade diferentes —
        // precisa ser um objeto distinto (equals/hashCode diferente) pra nao ser
        // silenciosamente descartado pelo Set antes mesmo de chegar na validacao.
        AcervoCartografico mesmoAcervo = new AcervoCartografico();
        mesmoAcervo.setId(9L);
        RegistroConsultaItem itemRepetido = new RegistroConsultaItem();
        itemRepetido.setAcervoCartografico(mesmoAcervo);
        itemRepetido.setPeriodo("Outro periodo");
        itemRepetido.setQuantidade(5);
        registro.addItem(itemRepetido);

        assertValidationError(
                () -> validation.validateSave(registro),
                ErrorConstants.ACERVO_CARTOGRAFICO_DUPLICADO_NO_REGISTRO,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateSave_deveLancarQuandoNaoHaItemDocumentalNemCartografico() {
        RegistroConsulta registro = registroValido();
        registro.setItens(new HashSet<>());

        assertValidationError(
                () -> validation.validateSave(registro),
                ErrorConstants.ITENS_REQUIRED,
                HttpStatus.BAD_REQUEST
        );
    }

    // ---------- semConsulta: dispensa a exigencia de pelo menos 1 item ----------

    @Test
    void validateSave_devePermitirRegistroSemItensQuandoSemConsultaEhTrue() {
        RegistroConsulta registro = registroValido();
        registro.setItens(new HashSet<>());
        registro.setSemConsulta(true);

        assertThatCode(() -> validation.validateSave(registro)).doesNotThrowAnyException();
        verify(registroConsultaItemRepository, never())
                .existsDuplicadoDocumental(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void validateSave_deveContinuarExigindoItemQuandoSemConsultaEhFalseOuNulo() {
        RegistroConsulta registroFalse = registroValido();
        registroFalse.setItens(new HashSet<>());
        registroFalse.setSemConsulta(false);

        assertValidationError(
                () -> validation.validateSave(registroFalse),
                ErrorConstants.ITENS_REQUIRED,
                HttpStatus.BAD_REQUEST
        );

        RegistroConsulta registroNulo = registroValido();
        registroNulo.setItens(new HashSet<>());
        registroNulo.setSemConsulta(null);

        assertValidationError(
                () -> validation.validateSave(registroNulo),
                ErrorConstants.ITENS_REQUIRED,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateSave_deveValidarItensNormalmenteMesmoComSemConsultaTrueSeItensForemEnviados() {
        RegistroConsulta registro = registroValido();
        registro.setSemConsulta(true);
        // registroValido() ja vem com 1 item documental — semConsulta=true nao
        // isenta esse item de ser validado normalmente (ex.: periodo ausente).
        primeiroItem(registro).setPeriodo(null);

        assertValidationError(
                () -> validation.validateSave(registro),
                ErrorConstants.PERIODO_REQUIRED,
                HttpStatus.BAD_REQUEST
        );
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
        // o unico item identico no banco e do proprio registro (mesmo ID) -> repositorio simula a exclusao.
        when(registroConsultaItemRepository.existsDuplicadoDocumental(
                any(), any(), any(), any(), any(), any(), eq(1L))).thenReturn(false);

        assertThatCode(() -> validation.validateUpdate(registro)).doesNotThrowAnyException();
    }

    @Test
    void validateUpdate_deveLancarConflitoQuandoOutroRegistroComIdDiferenteEhIdentico() {
        RegistroConsulta registro = registroValido();
        registro.setId(1L);
        // existe item identico em outro registro (ID diferente).
        when(registroConsultaItemRepository.existsDuplicadoDocumental(
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
        when(registroConsultaItemRepository.existsDuplicadoDocumental(
                any(), any(), any(), any(), any(), any(), any())).thenReturn(false);

        validation.validateUpdate(registro);

        verify(registroConsultaItemRepository).existsDuplicadoDocumental(
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

    // ---------- validateSearch: dataPesquisaInicio/dataPesquisaFim so juntas ou nenhuma ----------

    private RegistroConsultaSearchFilter filtroComIntervalo(LocalDate inicio, LocalDate fim) {
        return new RegistroConsultaSearchFilter(
                null, inicio, fim, null, null, null, null,
                null, null, null, null,
                null, null, null,
                null, null, null, null,
                null, null, null,
                null, null, null, null
        );
    }

    @Test
    void validateSearch_naoDeveLancarQuandoNenhumaDataDeIntervaloEhInformada() {
        assertThatCode(() -> validation.validateSearch(filtroComIntervalo(null, null))).doesNotThrowAnyException();
    }

    @Test
    void validateSearch_naoDeveLancarQuandoAsDuasDatasDeIntervaloSaoInformadas() {
        assertThatCode(() -> validation.validateSearch(
                filtroComIntervalo(LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 31))
        )).doesNotThrowAnyException();
    }

    @Test
    void validateSearch_deveLancarQuandoSoDataPesquisaInicioEhInformada() {
        assertValidationError(
                () -> validation.validateSearch(filtroComIntervalo(LocalDate.of(2026, 8, 1), null)),
                ErrorConstants.DATA_PESQUISA_INTERVALO_INCOMPLETO,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateSearch_deveLancarQuandoSoDataPesquisaFimEhInformada() {
        assertValidationError(
                () -> validation.validateSearch(filtroComIntervalo(null, LocalDate.of(2026, 8, 31))),
                ErrorConstants.DATA_PESQUISA_INTERVALO_INCOMPLETO,
                HttpStatus.BAD_REQUEST
        );
    }
}
