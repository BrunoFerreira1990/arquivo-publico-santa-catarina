package com.example.apesc.controller;

import com.example.apesc.dto.AcervoIconograficoPersonalidadesDTO;
import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoIconograficoPersonalidades;
import com.example.apesc.service.acervoiconograficopersonalidades.AcervoIconograficoPersonalidadesService;
import com.example.apesc.specification.AcervoIconograficoPersonalidadesSearchFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

@WebMvcTest(AcervoIconograficoPersonalidadesController.class)
@AutoConfigureMockMvc(addFilters = false)
class AcervoIconograficoPersonalidadesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AcervoIconograficoPersonalidadesService personalidadesService;

    private AcervoIconograficoPersonalidades umaPersonalidade(Long id, String nome) {
        AcervoIconograficoPersonalidades personalidade = new AcervoIconograficoPersonalidades();
        personalidade.setId(id);
        personalidade.setNome(nome);
        return personalidade;
    }

    @Test
    void save_deveRetornar201ComPersonalidadeCriada() throws Exception {
        when(personalidadesService.save(any(AcervoIconograficoPersonalidades.class))).thenReturn(umaPersonalidade(1L, "Nereu Ramos"));

        mockMvc.perform(post("/api/acervo-iconografico/personalidades")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(AcervoIconograficoPersonalidadesDTO.fromEntity(umaPersonalidade(null, "nereu ramos")))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Nereu Ramos"));
    }

    @Test
    void save_deveRetornar409QuandoNomeDuplicado() throws Exception {
        when(personalidadesService.save(any(AcervoIconograficoPersonalidades.class)))
                .thenThrow(new CustomException(ErrorConstants.PERSONALIDADE_DUPLICADA, HttpStatus.CONFLICT));

        mockMvc.perform(post("/api/acervo-iconografico/personalidades")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(AcervoIconograficoPersonalidadesDTO.fromEntity(umaPersonalidade(null, "Nereu Ramos")))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("PERSONALIDADE_DUPLICADA"));
    }

    @Test
    void listAll_deveRetornar200ComListaDePersonalidades() throws Exception {
        when(personalidadesService.findAll()).thenReturn(List.of(umaPersonalidade(1L, "Nereu Ramos"), umaPersonalidade(2L, "Celso Ramos")));

        mockMvc.perform(get("/api/acervo-iconografico/personalidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void search_deveRepassarNomeParaOServico() throws Exception {
        when(personalidadesService.search(any(AcervoIconograficoPersonalidadesSearchFilter.class)))
                .thenReturn(List.of(umaPersonalidade(1L, "Nereu Ramos")));

        mockMvc.perform(get("/api/acervo-iconografico/personalidades/search").param("nome", "ramos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome").value("Nereu Ramos"));

        verify(personalidadesService).search(eq(new AcervoIconograficoPersonalidadesSearchFilter("ramos")));
    }

    @Test
    void update_deveRetornar200ComPersonalidadeAtualizada() throws Exception {
        when(personalidadesService.update(any(AcervoIconograficoPersonalidades.class))).thenReturn(umaPersonalidade(1L, "Nereu de Campos Ramos"));

        mockMvc.perform(patch("/api/acervo-iconografico/personalidades/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(AcervoIconograficoPersonalidadesDTO.fromEntity(umaPersonalidade(1L, "Nereu de Campos Ramos")))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nereu de Campos Ramos"));
    }

    @Test
    void delete_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/acervo-iconografico/personalidades/1"))
                .andExpect(status().isNoContent());

        verify(personalidadesService).delete(1L);
    }
}
