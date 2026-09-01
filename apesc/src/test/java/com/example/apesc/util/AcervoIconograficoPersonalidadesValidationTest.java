package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoIconograficoPersonalidades;
import com.example.apesc.repository.AcervoIconograficoPersonalidadesRepository;
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
class AcervoIconograficoPersonalidadesValidationTest {

    @Mock
    private AcervoIconograficoPersonalidadesRepository repository;

    private AcervoIconograficoPersonalidadesValidation validation;

    @BeforeEach
    void setUp() {
        validation = new AcervoIconograficoPersonalidadesValidation();
    }

    private AcervoIconograficoPersonalidades umaPersonalidade(String nome) {
        AcervoIconograficoPersonalidades personalidade = new AcervoIconograficoPersonalidades();
        personalidade.setNome(nome);
        return personalidade;
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
    void validateSave_naoDeveLancarExcecaoQuandoNomeValidoENaoDuplicado() {
        when(repository.findByNomeIgnoreCase(any())).thenReturn(List.of());

        assertThatCode(() -> validation.validateSave(umaPersonalidade("Nereu Ramos"), repository)).doesNotThrowAnyException();
    }

    @Test
    void validateSave_deveAplicarTrimECapitalizarCadaPalavra() {
        when(repository.findByNomeIgnoreCase("Joao Da Silva")).thenReturn(List.of());
        AcervoIconograficoPersonalidades personalidade = umaPersonalidade("  joao da silva  ");

        validation.validateSave(personalidade, repository);

        assertThat(personalidade.getNome()).isEqualTo("Joao Da Silva");
    }

    @Test
    void validateSave_deveDeixarRestanteDaPalavraEmMinusculoAoCapitalizar() {
        when(repository.findByNomeIgnoreCase("Getulio Vargas")).thenReturn(List.of());
        AcervoIconograficoPersonalidades personalidade = umaPersonalidade("GETULIO VARGAS");

        validation.validateSave(personalidade, repository);

        assertThat(personalidade.getNome()).isEqualTo("Getulio Vargas");
    }

    @Test
    void validateSave_deveLancarQuandoNomeNulo() {
        assertValidationError(
                () -> validation.validateSave(umaPersonalidade(null), repository),
                ErrorConstants.PERSONALIDADE_REQUIRED,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateSave_deveLancarQuandoNomeEmBranco() {
        assertValidationError(
                () -> validation.validateSave(umaPersonalidade("   "), repository),
                ErrorConstants.PERSONALIDADE_REQUIRED,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateSave_deveLancarConflitoQuandoNomeJaExisteIgnorandoCase() {
        AcervoIconograficoPersonalidades existente = umaPersonalidade("Nereu Ramos");
        existente.setId(1L);
        when(repository.findByNomeIgnoreCase("Nereu Ramos")).thenReturn(List.of(existente));

        assertValidationError(
                () -> validation.validateSave(umaPersonalidade("nereu ramos"), repository),
                ErrorConstants.PERSONALIDADE_DUPLICADA,
                HttpStatus.CONFLICT
        );
    }

    // ---------- validateUpdate ----------

    @Test
    void validateUpdate_deveLancarQuandoIdNulo() {
        assertValidationError(
                () -> validation.validateUpdate(umaPersonalidade("Nereu Ramos"), repository),
                ErrorConstants.INVALID_ID,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void validateUpdate_deveLancarQuandoNaoEncontrado() {
        AcervoIconograficoPersonalidades personalidade = umaPersonalidade("Nereu Ramos");
        personalidade.setId(99L);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertValidationError(
                () -> validation.validateUpdate(personalidade, repository),
                ErrorConstants.ID_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void validateUpdate_naoDeveLancarQuandoDuplicataEncontradaPertenceAoProprioRegistro() {
        AcervoIconograficoPersonalidades personalidade = umaPersonalidade("Nereu Ramos");
        personalidade.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(personalidade));
        when(repository.findByNomeIgnoreCase("Nereu Ramos")).thenReturn(List.of(personalidade));

        assertThatCode(() -> validation.validateUpdate(personalidade, repository)).doesNotThrowAnyException();
    }

    @Test
    void validateUpdate_deveLancarConflitoQuandoNomePertenceAOutroRegistro() {
        AcervoIconograficoPersonalidades personalidade = umaPersonalidade("Nereu Ramos");
        personalidade.setId(1L);
        AcervoIconograficoPersonalidades outro = umaPersonalidade("Nereu Ramos");
        outro.setId(2L);
        when(repository.findById(1L)).thenReturn(Optional.of(personalidade));
        when(repository.findByNomeIgnoreCase("Nereu Ramos")).thenReturn(List.of(outro));

        assertValidationError(
                () -> validation.validateUpdate(personalidade, repository),
                ErrorConstants.PERSONALIDADE_DUPLICADA,
                HttpStatus.CONFLICT
        );
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
        when(repository.findById(1L)).thenReturn(Optional.of(umaPersonalidade("Nereu Ramos")));

        assertThatCode(() -> validation.validateDelete(1L, repository)).doesNotThrowAnyException();
    }
}
