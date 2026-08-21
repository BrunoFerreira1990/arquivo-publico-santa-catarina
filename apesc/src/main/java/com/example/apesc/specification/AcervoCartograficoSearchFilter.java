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
}
