package com.example.apesc.controller;

import com.example.apesc.dto.AcervoIconograficoAssuntosDTO;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.exception.CustomException;
import com.example.apesc.model.AcervoIconograficoAssuntos;
import com.example.apesc.service.acervoiconograficoassuntos.AcervoIconograficoAssuntosService;
import com.example.apesc.specification.AcervoIconograficoAssuntosSearchFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AcervoIconograficoAssuntosController.class)
class AcervoIconograficoAssuntosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AcervoIconograficoAssuntosService assuntosService;

    private AcervoIconograficoAssuntos umAssunto(Long id, String descricao) {
        AcervoIconograficoAssuntos assunto = new AcervoIconograficoAssuntos();
        assunto.setId(id);
        assunto.setDescricao(descricao);
        return assunto;
    }

    @Test
    void save_deveRetornar201ComAssuntoCriado() throws Exception {
        when(assuntosService.save(any(AcervoIconograficoAssuntos.class))).thenReturn(umAssunto(1L, "Cultura"));

        mockMvc.perform(post("/api/acervo-iconografico/assuntos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(AcervoIconograficoAssuntosDTO.fromEntity(umAssunto(null, "Cultura")))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Cultura"));
    }

    @Test
    void save_deveRetornar409QuandoDescricaoDuplicada() throws Exception {
        when(assuntosService.save(any(AcervoIconograficoAssuntos.class)))
                .thenThrow(new CustomException(ErrorConstants.ASSUNTO_DUPLICADO, HttpStatus.CONFLICT));

        mockMvc.perform(post("/api/acervo-iconografico/assuntos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(AcervoIconograficoAssuntosDTO.fromEntity(umAssunto(null, "Cultura")))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("ASSUNTO_DUPLICADO"));
    }

    @Test
    void listAll_deveRetornar200ComListaDeAssuntos() throws Exception {
        when(assuntosService.findAll()).thenReturn(List.of(umAssunto(1L, "Cultura"), umAssunto(2L, "Economia")));

        mockMvc.perform(get("/api/acervo-iconografico/assuntos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void search_deveRepassarDescricaoParaOServico() throws Exception {
        when(assuntosService.search(any(AcervoIconograficoAssuntosSearchFilter.class)))
                .thenReturn(List.of(umAssunto(1L, "Cultura")));

        mockMvc.perform(get("/api/acervo-iconografico/assuntos/search").param("descricao", "cult"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].descricao").value("Cultura"));

        verify(assuntosService).search(eq(new AcervoIconograficoAssuntosSearchFilter("cult")));
    }

    @Test
    void update_deveRetornar200ComAssuntoAtualizado() throws Exception {
        when(assuntosService.update(any(AcervoIconograficoAssuntos.class))).thenReturn(umAssunto(1L, "Cultura Popular"));

        mockMvc.perform(patch("/api/acervo-iconografico/assuntos/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(AcervoIconograficoAssuntosDTO.fromEntity(umAssunto(1L, "Cultura Popular")))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Cultura Popular"));
    }

    @Test
    void delete_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/acervo-iconografico/assuntos/1"))
                .andExpect(status().isNoContent());

        verify(assuntosService).delete(1L);
    }
}
