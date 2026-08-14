package com.example.apesc.service.researchers;

import com.example.apesc.model.Pesquisador;
import com.example.apesc.repository.PesquisadorRepository;
import com.example.apesc.util.PesquisadorValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PesquisadorServiceImplTest {

    @Mock
    private PesquisadorRepository pesquisadorRepository;

    @Mock
    private PesquisadorValidation pesquisadorValidation;

    private PesquisadorServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PesquisadorServiceImpl(pesquisadorRepository, pesquisadorValidation);
    }

    private Pesquisador umPesquisador(Long id) {
        Pesquisador p = new Pesquisador();
        p.setId(id);
        p.setNome("Maria Aparecida Silva");
        return p;
    }

    @Test
    void save_deveValidarEDelegarParaORepositorio() {
        Pesquisador pesquisador = umPesquisador(null);
        Pesquisador salvo = umPesquisador(1L);
        when(pesquisadorRepository.save(pesquisador)).thenReturn(salvo);

        Pesquisador resultado = service.save(pesquisador);

        verify(pesquisadorValidation).validateSave(pesquisador);
        verify(pesquisadorRepository).save(pesquisador);
        assertThat(resultado).isEqualTo(salvo);
    }

    @Test
    void save_naoDeveChamarRepositorioQuandoValidacaoFalhar() {
        Pesquisador pesquisador = umPesquisador(null);
        doThrow(new RuntimeException("inválido")).when(pesquisadorValidation).validateSave(pesquisador);

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> service.save(pesquisador));

        verify(pesquisadorRepository, never()).save(any());
    }

    @Test
    void update_deveValidarEDelegarParaORepositorio() {
        Pesquisador pesquisador = umPesquisador(1L);
        when(pesquisadorRepository.save(pesquisador)).thenReturn(pesquisador);

        Pesquisador resultado = service.update(pesquisador);

        verify(pesquisadorValidation).validateUpdate(pesquisador);
        verify(pesquisadorRepository).save(pesquisador);
        assertThat(resultado).isEqualTo(pesquisador);
    }

    @Test
    void findById_deveRetornarPesquisadorQuandoEncontrado() {
        Pesquisador pesquisador = umPesquisador(1L);
        when(pesquisadorRepository.findById(1L)).thenReturn(Optional.of(pesquisador));

        Pesquisador resultado = service.findById(1L);

        assertThat(resultado).isEqualTo(pesquisador);
    }

    @Test
    void findById_deveRetornarNuloQuandoNaoEncontrado() {
        when(pesquisadorRepository.findById(99L)).thenReturn(Optional.empty());

        Pesquisador resultado = service.findById(99L);

        assertThat(resultado).isNull();
    }

    @Test
    void findByNome_deveDelegarParaORepositorio() {
        List<Pesquisador> esperado = List.of(umPesquisador(1L), umPesquisador(2L));
        when(pesquisadorRepository.findByNome("Maria")).thenReturn(esperado);

        List<Pesquisador> resultado = service.findByNome("Maria");

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    void search_deveConstruirSpecificationEDelegarParaORepositorio() {
        List<Pesquisador> esperado = List.of(umPesquisador(1L));
        when(pesquisadorRepository.findAll(any(Specification.class))).thenReturn(esperado);

        List<Pesquisador> resultado = service.search("bruno", "12345678930");

        verify(pesquisadorRepository).findAll(any(Specification.class));
        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    void delete_deveDelegarParaORepositorio() {
        service.delete(1L);

        verify(pesquisadorRepository).deleteById(1L);
    }
}
