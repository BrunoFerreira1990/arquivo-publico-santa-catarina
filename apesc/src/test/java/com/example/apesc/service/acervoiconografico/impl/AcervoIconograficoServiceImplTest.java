package com.example.apesc.service.acervoiconografico.impl;

import com.example.apesc.model.AcervoIconografico;
import com.example.apesc.model.AcervoIconograficoAssuntos;
import com.example.apesc.model.AcervoIconograficoPersonalidades;
import com.example.apesc.model.TipoDocumento;
import com.example.apesc.repository.AcervoIconograficoAssuntosRepository;
import com.example.apesc.repository.AcervoIconograficoPersonalidadesRepository;
import com.example.apesc.repository.AcervoIconograficoRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import com.example.apesc.specification.AcervoIconograficoSearchFilter;
import com.example.apesc.util.AcervoIconograficoValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcervoIconograficoServiceImplTest {

    @Mock
    private AcervoIconograficoRepository acervoIconograficoRepository;

    @Mock
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Mock
    private AcervoIconograficoAssuntosRepository assuntosRepository;

    @Mock
    private AcervoIconograficoPersonalidadesRepository personalidadesRepository;

    @Mock
    private AcervoIconograficoValidation acervoIconograficoValidation;

    private AcervoIconograficoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AcervoIconograficoServiceImpl(
                acervoIconograficoRepository, tipoDocumentoRepository, assuntosRepository, personalidadesRepository, acervoIconograficoValidation);
    }

    private TipoDocumento umTipoDocumento(Long id, String nome) {
        TipoDocumento tipo = new TipoDocumento();
        tipo.setId(id);
        tipo.setNomeDocumento(nome);
        return tipo;
    }

    private AcervoIconograficoAssuntos umAssunto(Long id, String descricao) {
        AcervoIconograficoAssuntos assunto = new AcervoIconograficoAssuntos();
        assunto.setId(id);
        assunto.setDescricao(descricao);
        return assunto;
    }

    private AcervoIconograficoPersonalidades umaPersonalidade(Long id, String nome) {
        AcervoIconograficoPersonalidades personalidade = new AcervoIconograficoPersonalidades();
        personalidade.setId(id);
        personalidade.setNome(nome);
        return personalidade;
    }

    private AcervoIconografico umAcervo(Long tipoDocumentoId, Long assuntoId) {
        AcervoIconografico acervo = new AcervoIconografico();
        acervo.setTipoDocumento(umTipoDocumento(tipoDocumentoId, null));
        acervo.setCodigoIdentificacao("FOTO-001");
        acervo.setTitulo("Visita ao museu");
        acervo.setAssuntos(new HashSet<>(Set.of(umAssunto(assuntoId, null))));
        return acervo;
    }

    @Test
    void save_deveValidarERehidratarTipoDocumentoEAssuntosAntesDeSalvar() {
        AcervoIconografico acervo = umAcervo(1L, 1L);
        TipoDocumento tipoCompleto = umTipoDocumento(1L, "Fotografia");
        AcervoIconograficoAssuntos assuntoCompleto = umAssunto(1L, "Cultura");
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(tipoCompleto));
        when(assuntosRepository.findAllById(List.of(1L))).thenReturn(List.of(assuntoCompleto));
        when(acervoIconograficoRepository.save(acervo)).thenAnswer(inv -> inv.getArgument(0));

        AcervoIconografico resultado = service.save(acervo);

        verify(acervoIconograficoValidation).validateSave(acervo, acervoIconograficoRepository, tipoDocumentoRepository, assuntosRepository, personalidadesRepository);
        assertThat(resultado.getTipoDocumento()).isEqualTo(tipoCompleto);
        assertThat(resultado.getAssuntos()).containsExactly(assuntoCompleto);
    }

    @Test
    void save_deveRehidratarPersonalidadesQuandoInformadas() {
        AcervoIconografico acervo = umAcervo(1L, 1L);
        acervo.setPersonalidades(new HashSet<>(Set.of(umaPersonalidade(5L, null))));
        AcervoIconograficoPersonalidades personalidadeCompleta = umaPersonalidade(5L, "Nereu Ramos");
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(umTipoDocumento(1L, "Fotografia")));
        when(assuntosRepository.findAllById(List.of(1L))).thenReturn(List.of(umAssunto(1L, "Cultura")));
        when(personalidadesRepository.findAllById(List.of(5L))).thenReturn(List.of(personalidadeCompleta));
        when(acervoIconograficoRepository.save(acervo)).thenAnswer(inv -> inv.getArgument(0));

        AcervoIconografico resultado = service.save(acervo);

        assertThat(resultado.getPersonalidades()).containsExactly(personalidadeCompleta);
    }

    @Test
    void save_naoDeveConsultarPersonalidadesQuandoAusentes() {
        AcervoIconografico acervo = umAcervo(1L, 1L);
        acervo.setPersonalidades(null);
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(umTipoDocumento(1L, "Fotografia")));
        when(assuntosRepository.findAllById(List.of(1L))).thenReturn(List.of(umAssunto(1L, "Cultura")));
        when(acervoIconograficoRepository.save(acervo)).thenAnswer(inv -> inv.getArgument(0));

        service.save(acervo);

        verify(personalidadesRepository, never()).findAllById(any());
    }

    @Test
    void save_naoDeveChamarRepositorioQuandoValidacaoFalhar() {
        AcervoIconografico acervo = umAcervo(1L, 1L);
        doThrow(new RuntimeException("inválido")).when(acervoIconograficoValidation)
                .validateSave(acervo, acervoIconograficoRepository, tipoDocumentoRepository, assuntosRepository, personalidadesRepository);

        assertThrows(RuntimeException.class, () -> service.save(acervo));

        verify(acervoIconograficoRepository, never()).save(any());
    }

    @Test
    void findAllWithRelations_deveDelegarParaORepositorio() {
        List<AcervoIconografico> esperado = List.of(umAcervo(1L, 1L));
        when(acervoIconograficoRepository.findAllWithRelations()).thenReturn(esperado);

        assertThat(service.findAllWithRelations()).isEqualTo(esperado);
    }

    @Test
    void search_deveConstruirSpecificationEDelegarParaORepositorio() {
        List<AcervoIconografico> esperado = List.of(umAcervo(1L, 1L));
        when(acervoIconograficoRepository.findAll(any(Specification.class))).thenReturn(esperado);
        AcervoIconograficoSearchFilter filtro = new AcervoIconograficoSearchFilter(
                "Fotografia", null, null, null, null, null, null, null);

        List<AcervoIconografico> resultado = service.search(filtro);

        verify(acervoIconograficoRepository).findAll(any(Specification.class));
        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    void findByTipoDocumento_deveDelegarParaORepositorio() {
        List<AcervoIconografico> esperado = List.of(umAcervo(1L, 1L));
        when(acervoIconograficoRepository.findByTipoDocumentoId(1L)).thenReturn(esperado);

        assertThat(service.findByTipoDocumento(1L)).isEqualTo(esperado);
    }

    @Test
    void update_deveValidarERehidratarAntesDeSalvar() {
        AcervoIconografico acervo = umAcervo(1L, 1L);
        acervo.setId(1L);
        TipoDocumento tipoCompleto = umTipoDocumento(1L, "Fotografia");
        AcervoIconograficoAssuntos assuntoCompleto = umAssunto(1L, "Cultura");
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(tipoCompleto));
        when(assuntosRepository.findAllById(List.of(1L))).thenReturn(List.of(assuntoCompleto));
        when(acervoIconograficoRepository.save(acervo)).thenAnswer(inv -> inv.getArgument(0));

        AcervoIconografico resultado = service.update(acervo);

        verify(acervoIconograficoValidation).validateUpdate(acervo, acervoIconograficoRepository, tipoDocumentoRepository, assuntosRepository, personalidadesRepository);
        assertThat(resultado.getTipoDocumento()).isEqualTo(tipoCompleto);
    }

    @Test
    void delete_deveValidarEDelegarParaORepositorio() {
        service.delete(1L);

        verify(acervoIconograficoValidation).validateDelete(1L, acervoIconograficoRepository);
        verify(acervoIconograficoRepository).deleteById(1L);
    }

    @Test
    void delete_naoDeveChamarRepositorioQuandoValidacaoFalhar() {
        doThrow(new RuntimeException("não encontrado")).when(acervoIconograficoValidation).validateDelete(99L, acervoIconograficoRepository);

        assertThrows(RuntimeException.class, () -> service.delete(99L));

        verify(acervoIconograficoRepository, never()).deleteById(any());
    }
}
