package com.example.apesc.specification;

// Agrupa os parametros do GET /biblioteca-apoio/search num objeto so. Todos os
// campos sao opcionais. Bindado direto da query string via @ModelAttribute no
// controller — mesmo padrao usado em BibliotecaLivrosPeriodicosSearchFilter.
public record BibliotecaApoioSearchFilter(

        // Busca pelo NOME do tipo de documento (ex.: "Relatorios"), nao pelo id.
        String tipoDocumentoNome,
        // Busca pelo NOME da entidade produtora (ex.: "Governo"), nao pelo id.
        String entidadeProdutoraNome,
        String titulo,
        String periodo,
        Integer quantidadeVolume,
        String identificador,
        String localizacao,
        Boolean disponibilidade
) {
    // Remove espacos em branco no inicio/fim de cada parametro de texto digitado na busca.
    public BibliotecaApoioSearchFilter {
        tipoDocumentoNome = trim(tipoDocumentoNome);
        entidadeProdutoraNome = trim(entidadeProdutoraNome);
        titulo = trim(titulo);
        periodo = trim(periodo);
        identificador = trim(identificador);
        localizacao = trim(localizacao);
    }

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }
}
