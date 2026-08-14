package com.example.apesc.controller;

import com.example.apesc.model.Pesquisador;
import com.example.apesc.model.enums.*;
import com.example.apesc.service.researchers.PesquisadorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PesquisadorController.class)
class PesquisadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PesquisadorService pesquisadorService;

    private Pesquisador umPesquisador(Long id, String nome) {
        Pesquisador p = new Pesquisador();
        p.setId(id);
        p.setNome(nome);
        p.setDataNascimento(LocalDate.of(1990, 1, 1));
        p.setGenero(Generos.FEMININO);
        p.setCpf("12345678930");
        p.setNacionalidade(Nacionalidade.BRASILEIRA);
        p.setNumeroTelefone("48999998888");
        p.setLogradouro("Rua das Flores");
        p.setNumeroCasa("123");
        p.setBairro("Centro");
        p.setCidade("Florianópolis");
        p.setEstado(Estados.SC);
        p.setCep("88010000");
        p.setNivelEducacional(NivelEducacional.ENSINO_SUPERIOR);
        p.setProfissao("Historiadora");
        p.setAssuntoPesquisa("Assunto");
        p.setFinalidadePesquisa("Finalidade");
        p.setPeriodoEstudo(Set.of(PeriodoEstudo.REPUBLICANO));
        p.setAreaEstudo(Set.of(AreaEstudo.HISTORIA_REGIONAL));
        return p;
    }

    private String jsonDoPesquisador(Long id, String nome) throws Exception {
        return objectMapper.writeValueAsString(
                com.example.apesc.dto.PesquisadorDTO.fromEntity(umPesquisador(id, nome))
        );
    }

    @Test
    void save_deveRetornar201ComPesquisadorCriado() throws Exception {
        Pesquisador salvo = umPesquisador(1L, "Bruno Ferreira");
        when(pesquisadorService.save(any(Pesquisador.class))).thenReturn(salvo);

        mockMvc.perform(post("/api/pesquisador")
                        .contentType("application/json")
                        .content(jsonDoPesquisador(null, "Bruno Ferreira")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Bruno Ferreira"));
    }

    @Test
    void findById_deveRetornar200QuandoEncontrado() throws Exception {
        when(pesquisadorService.findById(1L)).thenReturn(umPesquisador(1L, "Bruno Ferreira"));

        mockMvc.perform(get("/api/pesquisador/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Bruno Ferreira"));
    }

    @Test
    void findById_deveRetornar404QuandoNaoEncontrado() throws Exception {
        when(pesquisadorService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/pesquisador/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByName_deveRetornar200ComPrimeiroResultadoQuandoEncontrado() throws Exception {
        when(pesquisadorService.findByNome("Bruno"))
                .thenReturn(List.of(umPesquisador(1L, "Bruno Ferreira"), umPesquisador(2L, "Bruno Souza")));

        mockMvc.perform(get("/api/pesquisador/nome/Bruno"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void findByName_deveRetornar404QuandoListaVazia() throws Exception {
        when(pesquisadorService.findByNome("Inexistente")).thenReturn(List.of());

        mockMvc.perform(get("/api/pesquisador/nome/Inexistente"))
                .andExpect(status().isNotFound());
    }

    @Test
    void search_deveRepassarParametrosNomeECpfParaOServico() throws Exception {
        when(pesquisadorService.search(eq("bruno"), eq("12345678930")))
                .thenReturn(List.of(umPesquisador(1L, "Bruno Ferreira")));

        mockMvc.perform(get("/api/pesquisador/search").param("nome", "bruno").param("cpf", "12345678930"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].nome").value("Bruno Ferreira"));

        verify(pesquisadorService).search("bruno", "12345678930");
    }

    @Test
    void search_devePermitirParametrosOpcionaisAusentes() throws Exception {
        when(pesquisadorService.search(isNull(), isNull())).thenReturn(List.of());

        mockMvc.perform(get("/api/pesquisador/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(0)));
    }

    @Test
    void update_deveRetornar200ComPesquisadorAtualizado() throws Exception {
        Pesquisador atualizado = umPesquisador(1L, "Bruno Ferreira Atualizado");
        when(pesquisadorService.update(any(Pesquisador.class))).thenReturn(atualizado);

        mockMvc.perform(patch("/api/pesquisador/1")
                        .contentType("application/json")
                        .content(jsonDoPesquisador(1L, "Bruno Ferreira Atualizado")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Bruno Ferreira Atualizado"));
    }

    @Test
    void delete_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/pesquisador/1"))
                .andExpect(status().isNoContent());

        verify(pesquisadorService).delete(1L);
    }
}
