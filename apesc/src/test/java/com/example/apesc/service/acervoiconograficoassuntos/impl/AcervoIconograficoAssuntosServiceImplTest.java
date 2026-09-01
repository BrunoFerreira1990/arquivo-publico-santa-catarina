package com.example.apesc.service.acervoiconograficoassuntos.impl;

import com.example.apesc.model.AcervoIconograficoAssuntos;
import com.example.apesc.repository.AcervoIconograficoAssuntosRepository;
import com.example.apesc.specification.AcervoIconograficoAssuntosSearchFilter;
import com.example.apesc.util.AcervoIconograficoAssuntosValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcervoIconograficoAssuntosServiceImplTest {

    @Mock
    private AcervoIconograficoAssuntosRepository assuntosRepository;

    @Mock
    private AcervoIconograficoAssuntosValidation assuntosValidation;

    private AcervoIconograficoAssuntosServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AcervoIconograficoAssuntosServiceImpl(assuntosRepository, assuntosValidation);
    }

    private AcervoIconograficoAssuntos umAssunto(Long id, String descricao) {
        AcervoIconograficoAssuntos assunto = new AcervoIconograficoAssuntos();
        assunto.setId(id);
        assunto.setDescricao(descricao);
        return assunto;
    }

    @Test
    void save_deveValidarEDelegarParaORepositorio() {
        AcervoIconograficoAssuntos assunto = umAssunto(null, "Cultura");
        AcervoIconograficoAssuntos salvo = umAssunto(1L, "Cultura");
        when(assuntosRepository.save(assunto)).thenReturn(salvo);

        AcervoIconograficoAssuntos resultado = service.save(assunto);

        verify(assuntosValidation).validateSave(assunto, assuntosRepository);
        verify(assuntosRepository).save(assunto);
        assertThat(resultado).isEqualTo(salvo);
    }

    @Test
    void save_naoDeveChamarRepositorioQuandoValidacaoFalhar() {
        AcervoIconograficoAssuntos assunto = umAssunto(null, "Cultura");
        doThrow(new RuntimeException("inválido")).when(assuntosValidation).validateSave(assunto, assuntosRepository);

        assertThrows(RuntimeException.class, () -> service.save(assunto));

        verify(assuntosRepository, never()).save(any());
    }

    @Test
    void findAll_deveDelegarParaORepositorio() {
        List<AcervoIconograficoAssuntos> esperado = List.of(umAssunto(1L, "Cultura"));
        when(assuntosRepository.findAll()).thenReturn(esperado);

        List<AcervoIconograficoAssuntos> resultado = service.findAll();

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    void search_deveConstruirSpecificationEDelegarParaORepositorio() {
        List<AcervoIconograficoAssuntos> esperado = List.of(umAssunto(1L, "Cultura"));
        when(assuntosRepository.findAll(any(Specification.class))).thenReturn(esperado);

        List<AcervoIconograficoAssuntos> resultado = service.search(new AcervoIconograficoAssuntosSearchFilter("cult"));

        verify(assuntosRepository).findAll(any(Specification.class));
        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    void update_deveValidarEDelegarParaORepositorio() {
        AcervoIconograficoAssuntos assunto = umAssunto(1L, "Cultura Popular");
        when(assuntosRepository.save(assunto)).thenReturn(assunto);

        AcervoIconograficoAssuntos resultado = service.update(assunto);

        verify(assuntosValidation).validateUpdate(assunto, assuntosRepository);
        verify(assuntosRepository).save(assunto);
        assertThat(resultado).isEqualTo(assunto);
    }

    @Test
    void delete_deveValidarEDelegarParaORepositorio() {
        service.delete(1L);

        verify(assuntosValidation).validateDelete(1L, assuntosRepository);
        verify(assuntosRepository).deleteById(1L);
    }

    @Test
    void delete_naoDeveChamarRepositorioQuandoValidacaoFalhar() {
        doThrow(new RuntimeException("não encontrado")).when(assuntosValidation).validateDelete(99L, assuntosRepository);

        assertThrows(RuntimeException.class, () -> service.delete(99L));

        verify(assuntosRepository, never()).deleteById(any());
    }
}
