package com.example.apesc.controller;

import com.example.apesc.dto.AcervoIconograficoDTO;
import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoIconografico;
import com.example.apesc.model.AcervoIconograficoAssuntos;
import com.example.apesc.model.TipoDocumento;
import com.example.apesc.service.acervoiconografico.AcervoIconograficoService;
import com.example.apesc.specification.AcervoIconograficoSearchFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AcervoIconograficoController.class)
@AutoConfigureMockMvc(addFilters = false)
class AcervoIconograficoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AcervoIconograficoService acervoIconograficoService;

    private AcervoIconografico umAcervo(Long id, String titulo) {
        AcervoIconografico acervo = new AcervoIconografico();
        acervo.setId(id);
        TipoDocumento tipo = new TipoDocumento();
        tipo.setId(14L);
        tipo.setNomeDocumento("Fotografia");
        acervo.setTipoDocumento(tipo);
        acervo.setCodigoIdentificacao("FOTO-001");
        acervo.setTitulo(titulo);
        acervo.setLocalizacao("Caixa 01");
        acervo.setAno("1938");
        AcervoIconograficoAssuntos assunto = new AcervoIconograficoAssuntos();
        assunto.setId(1L);
        assunto.setDescricao("Cultura");
        acervo.setAssuntos(new HashSet<>(Set.of(assunto)));
        return acervo;
    }

    @Test
    void save_deveRetornar201ComAcervoCriado() throws Exception {
        when(acervoIconograficoService.save(any(AcervoIconografico.class))).thenReturn(umAcervo(1L, "Visita ao museu"));

        mockMvc.perform(post("/api/acervo-iconografico")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(AcervoIconograficoDTO.fromEntity(umAcervo(null, "Visita ao museu")))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Visita ao museu"))
                .andExpect(jsonPath("$.assuntos", hasSize(1)));
    }

    @Test
    void save_deveRetornar409QuandoCodigoIdentificacaoDuplicado() throws Exception {
        when(acervoIconograficoService.save(any(AcervoIconografico.class)))
                .thenThrow(new CustomException(ErrorConstants.CODIGO_IDENTIFICACAO_DUPLICADO, HttpStatus.CONFLICT));

        mockMvc.perform(post("/api/acervo-iconografico")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(AcervoIconograficoDTO.fromEntity(umAcervo(null, "Visita ao museu")))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CODIGO_IDENTIFICACAO_DUPLICADO"));
    }

    @Test
    void listAll_deveRetornar200ComListaDeAcervos() throws Exception {
        when(acervoIconograficoService.findAllWithRelations())
                .thenReturn(List.of(umAcervo(1L, "Visita ao museu"), umAcervo(2L, "Festival cultural")));

        mockMvc.perform(get("/api/acervo-iconografico"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void search_deveRepassarTodosOsParametrosParaOServico() throws Exception {
        when(acervoIconograficoService.search(any(AcervoIconograficoSearchFilter.class)))
                .thenReturn(List.of(umAcervo(1L, "Visita ao museu")));

        mockMvc.perform(get("/api/acervo-iconografico/search")
                        .param("tipoDocumentoNome", "fotografia")
                        .param("codigoIdentificacao", "FOTO-001")
                        .param("titulo", "museu")
                        .param("localizacao", "caixa")
                        .param("localidade", "florian")
                        .param("ano", "1938")
                        .param("assuntos", "cultura")
                        .param("personalidades", "ramos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(acervoIconograficoService).search(eq(new AcervoIconograficoSearchFilter(
                "fotografia", "FOTO-001", "museu", "caixa", "florian", "1938", "cultura", "ramos")));
    }

    @Test
    void search_semParametrosDeveDelegarFiltroTodoNulo() throws Exception {
        when(acervoIconograficoService.search(any(AcervoIconograficoSearchFilter.class))).thenReturn(List.of());

        mockMvc.perform(get("/api/acervo-iconografico/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(acervoIconograficoService).search(eq(new AcervoIconograficoSearchFilter(
                null, null, null, null, null, null, null, null)));
    }

    @Test
    void update_deveRetornar200ComAcervoAtualizado() throws Exception {
        when(acervoIconograficoService.update(any(AcervoIconografico.class))).thenReturn(umAcervo(1L, "Visita ao museu histórico"));

        mockMvc.perform(patch("/api/acervo-iconografico/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(AcervoIconograficoDTO.fromEntity(umAcervo(1L, "Visita ao museu histórico")))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Visita ao museu histórico"));
    }

    @Test
    void delete_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/acervo-iconografico/1"))
                .andExpect(status().isNoContent());

        verify(acervoIconograficoService).delete(1L);
    }

    @Test
    void findByTipoDocumento_deveRetornar200ComListaFiltrada() throws Exception {
        when(acervoIconograficoService.findByTipoDocumento(14L)).thenReturn(List.of(umAcervo(1L, "Visita ao museu")));

        mockMvc.perform(get("/api/acervo-iconografico/tipo-documento/14"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getById_naoDeveExistirMais() throws Exception {
        mockMvc.perform(get("/api/acervo-iconografico/1"))
                .andExpect(status().isMethodNotAllowed());
    }
}
