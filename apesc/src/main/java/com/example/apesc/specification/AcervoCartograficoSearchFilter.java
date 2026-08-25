package com.example.apesc.specification;

// Agrupa os parametros do GET /acervo-cartografico/search num objeto so. Todos os
// campos sao opcionais. Bindado direto da query string via @ModelAttribute no
// controller (Spring suporta binding em record desde o 6.1) — mesmo padrao usado em
// RegistroConsultaSearchFilter.
public record AcervoCartograficoSearchFilter(

        // Busca pelo NOME do tipo de documento (ex.: "Mapa"), nao pelo id.
        String tipoDocumentoNome,
        String codigoIdentificacao,
        String localidade,
        String ano,
        Boolean disponibilidade,
        // Busca pelo NOME da entidade produtora, nao pelo id.
        String entidadeProdutoraNome
) {
    // Remove espacos em branco no inicio/fim de cada parametro digitado na busca.
    public AcervoCartograficoSearchFilter {
        tipoDocumentoNome = trim(tipoDocumentoNome);
        codigoIdentificacao = trim(codigoIdentificacao);
        localidade = trim(localidade);
        ano = trim(ano);
        entidadeProdutoraNome = trim(entidadeProdutoraNome);
    }

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }
}
