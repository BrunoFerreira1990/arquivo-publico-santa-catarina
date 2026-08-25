package com.example.apesc.specification;

import com.example.apesc.model.enums.TipoConsulta;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

// Agrupa os parametros do GET /registro-consulta/search num objeto so — sao 23
// campos ao todo (5 do proprio registro + os campos "buscaveis" de cada um dos 6
// tipos de acervo), o que estouraria a legibilidade de um metodo com parametros
// posicionais. Todos os campos sao opcionais. Bindado direto da query string via
// @ModelAttribute no controller (Spring suporta binding em record desde o 6.1).
public record RegistroConsultaSearchFilter(

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataPesquisa,
        // Intervalo de datas — convive com dataPesquisa (data exata) acima; cada um
        // resolve um caso de uso diferente. Se os dois vierem preenchidos ao mesmo
        // tempo, as condicoes se combinam com AND (pode gerar zero resultado se
        // forem inconsistentes entre si — isso e esperado, nao e um bug).
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataPesquisaInicio,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataPesquisaFim,
        String nomePesquisador,
        String nomeFuncionario,
        TipoConsulta tipoConsulta,
        Boolean semConsulta,

        // acervo_documental
        String tipoDocumentoNomeDocumental,
        String entidadeProdutoraNomeDocumental,
        String entidadeReceptoraNomeDocumental,

        // acervo_documental_processos
        String nomeProcessoDocumentalProcessos,

        // acervo_iconografico
        String tituloIconografico,
        String codigoIconografico,
        String tipoDocumentoNomeIconografico,

        // acervo_cartografico
        String tituloCartografico,
        String codigoCartografico,
        String tipoDocumentoNomeCartografico,
        String entidadeProdutoraNomeCartografico,

        // biblioteca_livros_periodicos
        String tituloBibliotecaLivros,
        String autoresBibliotecaLivros,
        String tipoDocumentoNomeBibliotecaLivros,

        // biblioteca_apoio
        String tituloBibliotecaApoio,
        String identificadorBibliotecaApoio,
        String tipoDocumentoNomeBibliotecaApoio,
        String entidadeProdutoraNomeBibliotecaApoio
) {
    // Remove espacos em branco no inicio/fim de cada parametro digitado na busca.
    public RegistroConsultaSearchFilter {
        nomePesquisador = trim(nomePesquisador);
        nomeFuncionario = trim(nomeFuncionario);
        tipoDocumentoNomeDocumental = trim(tipoDocumentoNomeDocumental);
        entidadeProdutoraNomeDocumental = trim(entidadeProdutoraNomeDocumental);
        entidadeReceptoraNomeDocumental = trim(entidadeReceptoraNomeDocumental);
        nomeProcessoDocumentalProcessos = trim(nomeProcessoDocumentalProcessos);
        tituloIconografico = trim(tituloIconografico);
        codigoIconografico = trim(codigoIconografico);
        tipoDocumentoNomeIconografico = trim(tipoDocumentoNomeIconografico);
        tituloCartografico = trim(tituloCartografico);
        codigoCartografico = trim(codigoCartografico);
        tipoDocumentoNomeCartografico = trim(tipoDocumentoNomeCartografico);
        entidadeProdutoraNomeCartografico = trim(entidadeProdutoraNomeCartografico);
        tituloBibliotecaLivros = trim(tituloBibliotecaLivros);
        autoresBibliotecaLivros = trim(autoresBibliotecaLivros);
        tipoDocumentoNomeBibliotecaLivros = trim(tipoDocumentoNomeBibliotecaLivros);
        tituloBibliotecaApoio = trim(tituloBibliotecaApoio);
        identificadorBibliotecaApoio = trim(identificadorBibliotecaApoio);
        tipoDocumentoNomeBibliotecaApoio = trim(tipoDocumentoNomeBibliotecaApoio);
        entidadeProdutoraNomeBibliotecaApoio = trim(entidadeProdutoraNomeBibliotecaApoio);
    }

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }
}
