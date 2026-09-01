package com.example.apesc.service.acervoiconograficopersonalidades.impl;

import com.example.apesc.model.AcervoIconograficoPersonalidades;
import com.example.apesc.repository.AcervoIconograficoPersonalidadesRepository;
import com.example.apesc.specification.AcervoIconograficoPersonalidadesSearchFilter;
import com.example.apesc.util.AcervoIconograficoPersonalidadesValidation;
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
class AcervoIconograficoPersonalidadesServiceImplTest {

    @Mock
    private AcervoIconograficoPersonalidadesRepository personalidadesRepository;

    @Mock
    private AcervoIconograficoPersonalidadesValidation personalidadesValidation;

    private AcervoIconograficoPersonalidadesServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AcervoIconograficoPersonalidadesServiceImpl(personalidadesRepository, personalidadesValidation);
    }

    private AcervoIconograficoPersonalidades umaPersonalidade(Long id, String nome) {
        AcervoIconograficoPersonalidades personalidade = new AcervoIconograficoPersonalidades();
        personalidade.setId(id);
        personalidade.setNome(nome);
        return personalidade;
    }

    @Test
    void save_deveValidarEDelegarParaORepositorio() {
        AcervoIconograficoPersonalidades personalidade = umaPersonalidade(null, "Nereu Ramos");
        AcervoIconograficoPersonalidades salva = umaPersonalidade(1L, "Nereu Ramos");
        when(personalidadesRepository.save(personalidade)).thenReturn(salva);

        AcervoIconograficoPersonalidades resultado = service.save(personalidade);

        verify(personalidadesValidation).validateSave(personalidade, personalidadesRepository);
        verify(personalidadesRepository).save(personalidade);
        assertThat(resultado).isEqualTo(salva);
    }

    @Test
    void save_naoDeveChamarRepositorioQuandoValidacaoFalhar() {
        AcervoIconograficoPersonalidades personalidade = umaPersonalidade(null, "Nereu Ramos");
        doThrow(new RuntimeException("inválido")).when(personalidadesValidation).validateSave(personalidade, personalidadesRepository);

        assertThrows(RuntimeException.class, () -> service.save(personalidade));

        verify(personalidadesRepository, never()).save(any());
    }

    @Test
    void findAll_deveDelegarParaORepositorio() {
        List<AcervoIconograficoPersonalidades> esperado = List.of(umaPersonalidade(1L, "Nereu Ramos"));
        when(personalidadesRepository.findAll()).thenReturn(esperado);

        List<AcervoIconograficoPersonalidades> resultado = service.findAll();

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    void search_deveConstruirSpecificationEDelegarParaORepositorio() {
        List<AcervoIconograficoPersonalidades> esperado = List.of(umaPersonalidade(1L, "Nereu Ramos"));
        when(personalidadesRepository.findAll(any(Specification.class))).thenReturn(esperado);

        List<AcervoIconograficoPersonalidades> resultado = service.search(new AcervoIconograficoPersonalidadesSearchFilter("ramos"));

        verify(personalidadesRepository).findAll(any(Specification.class));
        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    void update_deveValidarEDelegarParaORepositorio() {
        AcervoIconograficoPersonalidades personalidade = umaPersonalidade(1L, "Nereu de Campos Ramos");
        when(personalidadesRepository.save(personalidade)).thenReturn(personalidade);

        AcervoIconograficoPersonalidades resultado = service.update(personalidade);

        verify(personalidadesValidation).validateUpdate(personalidade, personalidadesRepository);
        verify(personalidadesRepository).save(personalidade);
        assertThat(resultado).isEqualTo(personalidade);
    }

    @Test
    void delete_deveValidarEDelegarParaORepositorio() {
        service.delete(1L);

        verify(personalidadesValidation).validateDelete(1L, personalidadesRepository);
        verify(personalidadesRepository).deleteById(1L);
    }

    @Test
    void delete_naoDeveChamarRepositorioQuandoValidacaoFalhar() {
        doThrow(new RuntimeException("não encontrado")).when(personalidadesValidation).validateDelete(99L, personalidadesRepository);

        assertThrows(RuntimeException.class, () -> service.delete(99L));

        verify(personalidadesRepository, never()).deleteById(any());
    }
}
