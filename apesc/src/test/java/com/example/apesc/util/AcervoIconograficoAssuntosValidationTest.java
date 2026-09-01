package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoIconograficoAssuntos;
import com.example.apesc.repository.AcervoIconograficoAssuntosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcervoIconograficoAssuntosValidationTest {

    @Mock
    private AcervoIconograficoAssuntosRepository repository;

    private AcervoIconograficoAssuntosValidation validation;

    @BeforeEach
    void setUp() {
        validation = new AcervoIconograficoAssuntosValidation();
    }

    private AcervoIconograficoAssuntos umAssunto(String descricao) {
        AcervoIconograficoAssuntos assunto = new AcervoIconograficoAssuntos();
        assunto.setDescricao(descricao);
        return assunto;
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

    // ---------- validateSave ----------

    @Test
    void validateSave_naoDeveLancarExcecaoQuandoDescricaoValidaENaoDuplicada() {
        when(repository.findByDescricaoIgnoreCase(any())).thenReturn(List.of());

        assertThatCode(() -> validation.validateSave(umAssunto("Cultura"), repository)).doesNotThrowAnyException();
    }

    @Test
    void validateSave_deveAplicarTrimNaDescricaoAntesDeValidarEPersistir() {
        when(repository.findByDescricaoIgnoreCase("Cultura")).thenReturn(List.of());
        AcervoIconograficoAssuntos assunto = umAssunto("  Cultura  ");

        validation.validateSave(assunto, repository);

        assertThat(assunto.getDescricao()).isEqualTo("Cultura");
    }

    @Test
    void validateSave_deveLancarQuandoDescricaoNula() {
        assertValidationError(
                () -> validation.validateSave(umAssunto(null), repository),
                ErrorConstants.ASSUNTO_REQUIRED,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateSave_deveLancarQuandoDescricaoEmBranco() {
        assertValidationError(
                () -> validation.validateSave(umAssunto("   "), repository),
                ErrorConstants.ASSUNTO_REQUIRED,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateSave_deveLancarConflitoQuandoDescricaoJaExisteIgnorandoCase() {
        AcervoIconograficoAssuntos existente = umAssunto("Cultura");
        existente.setId(1L);
        when(repository.findByDescricaoIgnoreCase("cultura")).thenReturn(List.of(existente));

        assertValidationError(
                () -> validation.validateSave(umAssunto("cultura"), repository),
                ErrorConstants.ASSUNTO_DUPLICADO,
                HttpStatus.CONFLICT
        );
    }

    // ---------- validateUpdate ----------

    @Test
    void validateUpdate_deveLancarQuandoIdNulo() {
        assertValidationError(
                () -> validation.validateUpdate(umAssunto("Cultura"), repository),
                ErrorConstants.INVALID_ID,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateUpdate_deveLancarQuandoNaoEncontrado() {
        AcervoIconograficoAssuntos assunto = umAssunto("Cultura");
        assunto.setId(99L);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertValidationError(
                () -> validation.validateUpdate(assunto, repository),
                ErrorConstants.ID_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void validateUpdate_naoDeveLancarQuandoDuplicataEncontradaPertenceAoProprioRegistro() {
        AcervoIconograficoAssuntos assunto = umAssunto("Cultura");
        assunto.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(assunto));
        when(repository.findByDescricaoIgnoreCase("Cultura")).thenReturn(List.of(assunto));

        assertThatCode(() -> validation.validateUpdate(assunto, repository)).doesNotThrowAnyException();
    }

    @Test
    void validateUpdate_deveLancarConflitoQuandoDescricaoPertenceAOutroRegistro() {
        AcervoIconograficoAssuntos assunto = umAssunto("Cultura");
        assunto.setId(1L);
        AcervoIconograficoAssuntos outro = umAssunto("Cultura");
        outro.setId(2L);
        when(repository.findById(1L)).thenReturn(Optional.of(assunto));
        when(repository.findByDescricaoIgnoreCase("Cultura")).thenReturn(List.of(outro));

        assertValidationError(
                () -> validation.validateUpdate(assunto, repository),
                ErrorConstants.ASSUNTO_DUPLICADO,
                HttpStatus.CONFLICT
        );
    }

    @Test
    void validateUpdate_deveAplicarTrimNaDescricaoAntesDeValidar() {
        AcervoIconograficoAssuntos assunto = umAssunto("  Cultura Popular  ");
        assunto.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(assunto));
        when(repository.findByDescricaoIgnoreCase("Cultura Popular")).thenReturn(List.of());

        validation.validateUpdate(assunto, repository);

        assertThat(assunto.getDescricao()).isEqualTo("Cultura Popular");
    }

    // ---------- validateDelete ----------

    @Test
    void validateDelete_deveLancarQuandoIdNulo() {
        assertValidationError(
                () -> validation.validateDelete(null, repository),
                ErrorConstants.INVALID_ID,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateDelete_deveLancarQuandoNaoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertValidationError(
                () -> validation.validateDelete(99L, repository),
                ErrorConstants.ID_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void validateDelete_naoDeveLancarQuandoEncontrado() {
        when(repository.findById(1L)).thenReturn(Optional.of(umAssunto("Cultura")));

        assertThatCode(() -> validation.validateDelete(1L, repository)).doesNotThrowAnyException();
    }
}
