package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoIconografico;
import com.example.apesc.model.AcervoIconograficoAssuntos;
import com.example.apesc.model.AcervoIconograficoPersonalidades;
import com.example.apesc.model.TipoDocumento;
import com.example.apesc.repository.AcervoIconograficoAssuntosRepository;
import com.example.apesc.repository.AcervoIconograficoPersonalidadesRepository;
import com.example.apesc.repository.AcervoIconograficoRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcervoIconograficoValidationTest {

    @Mock
    private AcervoIconograficoRepository acervoRepository;

    @Mock
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Mock
    private AcervoIconograficoAssuntosRepository assuntosRepository;

    @Mock
    private AcervoIconograficoPersonalidadesRepository personalidadesRepository;

    private AcervoIconograficoValidation validation;

    @BeforeEach
    void setUp() {
        validation = new AcervoIconograficoValidation();
    }

    private TipoDocumento umTipoDocumento(Long id) {
        TipoDocumento tipo = new TipoDocumento();
        tipo.setId(id);
        tipo.setNomeDocumento("Fotografia");
        return tipo;
    }

    private AcervoIconograficoAssuntos umAssunto(Long id) {
        AcervoIconograficoAssuntos assunto = new AcervoIconograficoAssuntos();
        assunto.setId(id);
        assunto.setDescricao("Cultura");
        return assunto;
    }

    private AcervoIconograficoPersonalidades umaPersonalidade(Long id) {
        AcervoIconograficoPersonalidades personalidade = new AcervoIconograficoPersonalidades();
        personalidade.setId(id);
        personalidade.setNome("Nereu Ramos");
        return personalidade;
    }

    private AcervoIconografico umAcervoValido() {
        AcervoIconografico acervo = new AcervoIconografico();
        acervo.setTipoDocumento(umTipoDocumento(1L));
        acervo.setCodigoIdentificacao("FOTO-001");
        acervo.setTitulo("Visita ao museu");
        acervo.setLocalizacao("Caixa 01");
        acervo.setLocalidade("Florianopolis");
        acervo.setAno("1938");
        acervo.setAssuntos(new HashSet<>(Set.of(umAssunto(1L))));
        return acervo;
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

    private void validarSave(AcervoIconografico acervo) {
        validation.validateSave(acervo, acervoRepository, tipoDocumentoRepository, assuntosRepository, personalidadesRepository);
    }

    private void validarUpdate(AcervoIconografico acervo) {
        validation.validateUpdate(acervo, acervoRepository, tipoDocumentoRepository, assuntosRepository, personalidadesRepository);
    }

    // ---------- validateSave: caminho feliz ----------

    @Test
    void validateSave_naoDeveLancarExcecaoQuandoTudoValido() {
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(umTipoDocumento(1L)));
        when(assuntosRepository.findById(1L)).thenReturn(Optional.of(umAssunto(1L)));
        when(acervoRepository.existsByCodigoIdentificacao("FOTO-001")).thenReturn(false);

        assertThatCode(() -> validarSave(umAcervoValido())).doesNotThrowAnyException();
    }

    @Test
    void validateSave_deveAplicarTrimECapitalizarPrimeiraLetraDoTituloELocalidade() {
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(umTipoDocumento(1L)));
        when(assuntosRepository.findById(1L)).thenReturn(Optional.of(umAssunto(1L)));
        when(acervoRepository.existsByCodigoIdentificacao("FOTO-001")).thenReturn(false);
        AcervoIconografico acervo = umAcervoValido();
        acervo.setTitulo("  visita ao museu historico  ");
        acervo.setLocalidade("  florianopolis  ");

        validarSave(acervo);

        assertThat(acervo.getTitulo()).isEqualTo("Visita ao museu historico");
        assertThat(acervo.getLocalidade()).isEqualTo("Florianopolis");
    }

    @Test
    void validateSave_deveAplicarTrimSemCapitalizarCodigoLocalizacaoEAno() {
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(umTipoDocumento(1L)));
        when(assuntosRepository.findById(1L)).thenReturn(Optional.of(umAssunto(1L)));
        when(acervoRepository.existsByCodigoIdentificacao("FOTO-001")).thenReturn(false);
        AcervoIconografico acervo = umAcervoValido();
        acervo.setCodigoIdentificacao("  FOTO-001  ");
        acervo.setLocalizacao("  caixa 01  ");
        acervo.setAno("  1938  ");

        validarSave(acervo);

        assertThat(acervo.getCodigoIdentificacao()).isEqualTo("FOTO-001");
        assertThat(acervo.getLocalizacao()).isEqualTo("caixa 01");
        assertThat(acervo.getAno()).isEqualTo("1938");
    }

    // ---------- validateSave: tipo de documento ----------

    @Test
    void validateSave_deveLancarQuandoTipoDocumentoNulo() {
        AcervoIconografico acervo = umAcervoValido();
        acervo.setTipoDocumento(null);

        assertValidationError(() -> validarSave(acervo), ErrorConstants.TIPO_DOCUMENTO_REQUIRED, HttpStatus.BAD_REQUEST);
    }

    @Test
    void validateSave_deveLancarQuandoTipoDocumentoNaoEncontrado() {
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.empty());

        assertValidationError(() -> validarSave(umAcervoValido()), ErrorConstants.TIPO_DOCUMENTO_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    // ---------- validateSave: campos obrigatorios simples ----------

    private static Stream<Arguments> camposObrigatoriosBasicos() {
        return Stream.of(
                Arguments.of((Consumer<AcervoIconografico>) a -> a.setCodigoIdentificacao(null), ErrorConstants.CODIGO_IDENTIFICACAO_REQUIRED),
                Arguments.of((Consumer<AcervoIconografico>) a -> a.setCodigoIdentificacao("   "), ErrorConstants.CODIGO_IDENTIFICACAO_REQUIRED),
                Arguments.of((Consumer<AcervoIconografico>) a -> a.setTitulo(null), ErrorConstants.TITULO_REQUIRED),
                Arguments.of((Consumer<AcervoIconografico>) a -> a.setTitulo("   "), ErrorConstants.TITULO_REQUIRED),
                Arguments.of((Consumer<AcervoIconografico>) a -> a.setLocalizacao(null), ErrorConstants.LOCALIZACAO_REQUIRED),
                Arguments.of((Consumer<AcervoIconografico>) a -> a.setLocalizacao("   "), ErrorConstants.LOCALIZACAO_REQUIRED),
                Arguments.of((Consumer<AcervoIconografico>) a -> a.setAno(null), ErrorConstants.ANO_REQUIRED),
                Arguments.of((Consumer<AcervoIconografico>) a -> a.setAno("   "), ErrorConstants.ANO_REQUIRED)
        );
    }

    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("camposObrigatoriosBasicos")
    void validateSave_deveRejeitarCampoObrigatorioAusente(Consumer<AcervoIconografico> mutador, ErrorConstants erroEsperado) {
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(umTipoDocumento(1L)));
        AcervoIconografico acervo = umAcervoValido();
        mutador.accept(acervo);

        assertValidationError(() -> validarSave(acervo), erroEsperado, HttpStatus.BAD_REQUEST);
    }

    // ---------- validateSave: assuntos (obrigatorio) ----------

    @Test
    void validateSave_deveLancarQuandoAssuntosNulo() {
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(umTipoDocumento(1L)));
        AcervoIconografico acervo = umAcervoValido();
        acervo.setAssuntos(null);

        assertValidationError(() -> validarSave(acervo), ErrorConstants.ASSUNTO_ICONOGRAFICO_REQUIRED, HttpStatus.BAD_REQUEST);
    }

    @Test
    void validateSave_deveLancarQuandoAssuntosVazio() {
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(umTipoDocumento(1L)));
        AcervoIconografico acervo = umAcervoValido();
        acervo.setAssuntos(Set.of());

        assertValidationError(() -> validarSave(acervo), ErrorConstants.ASSUNTO_ICONOGRAFICO_REQUIRED, HttpStatus.BAD_REQUEST);
    }

    @Test
    void validateSave_deveLancarQuandoAssuntoInformadoNaoExisteNoCatalogo() {
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(umTipoDocumento(1L)));
        when(assuntosRepository.findById(1L)).thenReturn(Optional.empty());

        assertValidationError(() -> validarSave(umAcervoValido()), ErrorConstants.ASSUNTO_ICONOGRAFICO_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @Test
    void validateSave_deveLancarQuandoAssuntoSemId() {
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(umTipoDocumento(1L)));
        AcervoIconografico acervo = umAcervoValido();
        acervo.setAssuntos(new HashSet<>(Set.of(umAssunto(null))));

        assertValidationError(() -> validarSave(acervo), ErrorConstants.ASSUNTO_ICONOGRAFICO_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    // ---------- validateSave: personalidades (opcional) ----------

    @Test
    void validateSave_devePermitirPersonalidadesNulas() {
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(umTipoDocumento(1L)));
        when(assuntosRepository.findById(1L)).thenReturn(Optional.of(umAssunto(1L)));
        when(acervoRepository.existsByCodigoIdentificacao("FOTO-001")).thenReturn(false);
        AcervoIconografico acervo = umAcervoValido();
        acervo.setPersonalidades(null);

        assertThatCode(() -> validarSave(acervo)).doesNotThrowAnyException();
    }

    @Test
    void validateSave_devePermitirPersonalidadesVazias() {
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(umTipoDocumento(1L)));
        when(assuntosRepository.findById(1L)).thenReturn(Optional.of(umAssunto(1L)));
        when(acervoRepository.existsByCodigoIdentificacao("FOTO-001")).thenReturn(false);
        AcervoIconografico acervo = umAcervoValido();
        acervo.setPersonalidades(Set.of());

        assertThatCode(() -> validarSave(acervo)).doesNotThrowAnyException();
    }

    @Test
    void validateSave_deveLancarQuandoPersonalidadeInformadaNaoExisteNoCatalogo() {
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(umTipoDocumento(1L)));
        when(assuntosRepository.findById(1L)).thenReturn(Optional.of(umAssunto(1L)));
        when(personalidadesRepository.findById(5L)).thenReturn(Optional.empty());
        AcervoIconografico acervo = umAcervoValido();
        acervo.setPersonalidades(new HashSet<>(Set.of(umaPersonalidade(5L))));

        assertValidationError(() -> validarSave(acervo), ErrorConstants.PERSONALIDADE_ICONOGRAFICA_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @Test
    void validateSave_naoDeveLancarQuandoPersonalidadeInformadaExisteNoCatalogo() {
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(umTipoDocumento(1L)));
        when(assuntosRepository.findById(1L)).thenReturn(Optional.of(umAssunto(1L)));
        when(personalidadesRepository.findById(5L)).thenReturn(Optional.of(umaPersonalidade(5L)));
        when(acervoRepository.existsByCodigoIdentificacao("FOTO-001")).thenReturn(false);
        AcervoIconografico acervo = umAcervoValido();
        acervo.setPersonalidades(new HashSet<>(Set.of(umaPersonalidade(5L))));

        assertThatCode(() -> validarSave(acervo)).doesNotThrowAnyException();
    }

    // ---------- validateSave: duplicidade de codigo ----------

    @Test
    void validateSave_deveLancarConflitoQuandoCodigoIdentificacaoDuplicado() {
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(umTipoDocumento(1L)));
        when(assuntosRepository.findById(1L)).thenReturn(Optional.of(umAssunto(1L)));
        when(acervoRepository.existsByCodigoIdentificacao("FOTO-001")).thenReturn(true);

        assertValidationError(() -> validarSave(umAcervoValido()), ErrorConstants.CODIGO_IDENTIFICACAO_DUPLICADO, HttpStatus.CONFLICT);
    }

    // ---------- validateUpdate ----------

    @Test
    void validateUpdate_deveLancarQuandoIdNulo() {
        AcervoIconografico acervo = umAcervoValido();
        acervo.setId(null);

        assertValidationError(() -> validarUpdate(acervo), ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
    }

    @Test
    void validateUpdate_deveLancarQuandoNaoEncontrado() {
        AcervoIconografico acervo = umAcervoValido();
        acervo.setId(99L);
        when(acervoRepository.findById(99L)).thenReturn(Optional.empty());

        assertValidationError(() -> validarUpdate(acervo), ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @Test
    void validateUpdate_naoDeveLancarQuandoTudoValidoEIgnoraOProprioCodigoNaDuplicidade() {
        AcervoIconografico acervo = umAcervoValido();
        acervo.setId(1L);
        when(acervoRepository.findById(1L)).thenReturn(Optional.of(acervo));
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(umTipoDocumento(1L)));
        when(assuntosRepository.findById(1L)).thenReturn(Optional.of(umAssunto(1L)));
        when(acervoRepository.existsByCodigoIdentificacaoAndIdNot("FOTO-001", 1L)).thenReturn(false);

        assertThatCode(() -> validarUpdate(acervo)).doesNotThrowAnyException();
    }

    @Test
    void validateUpdate_deveLancarConflitoQuandoCodigoPertenceAOutroRegistro() {
        AcervoIconografico acervo = umAcervoValido();
        acervo.setId(1L);
        when(acervoRepository.findById(1L)).thenReturn(Optional.of(acervo));
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(umTipoDocumento(1L)));
        when(assuntosRepository.findById(1L)).thenReturn(Optional.of(umAssunto(1L)));
        when(acervoRepository.existsByCodigoIdentificacaoAndIdNot("FOTO-001", 1L)).thenReturn(true);

        assertValidationError(() -> validarUpdate(acervo), ErrorConstants.CODIGO_IDENTIFICACAO_DUPLICADO, HttpStatus.CONFLICT);
    }

    // ---------- validateDelete ----------

    @Test
    void validateDelete_deveLancarQuandoIdNulo() {
        assertValidationError(() -> validation.validateDelete(null, acervoRepository), ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
    }

    @Test
    void validateDelete_deveLancarQuandoNaoEncontrado() {
        when(acervoRepository.findById(99L)).thenReturn(Optional.empty());

        assertValidationError(() -> validation.validateDelete(99L, acervoRepository), ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @Test
    void validateDelete_naoDeveLancarQuandoEncontrado() {
        when(acervoRepository.findById(1L)).thenReturn(Optional.of(umAcervoValido()));

        assertThatCode(() -> validation.validateDelete(1L, acervoRepository)).doesNotThrowAnyException();
    }
}
