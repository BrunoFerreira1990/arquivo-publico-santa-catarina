package com.example.apesc.specification;

// Agrupa os parametros do GET /api/acervo-iconografico/search num objeto so. Todos
// os campos sao opcionais. Bindado direto da query string via @ModelAttribute no
// controller — mesmo padrao usado em BibliotecaApoioSearchFilter.
public record AcervoIconograficoSearchFilter(
        // Busca pelo NOME do tipo de documento (ex.: "Fotografia"), nao pelo id.
        String tipoDocumentoNome,
        String codigoIdentificacao,
        String titulo,
        String localizacao,
        String localidade,
        String ano,
        // Busca por parte da descricao de qualquer assunto vinculado.
        String assuntos,
        // Busca por parte do nome de qualquer personalidade vinculada.
        String personalidades
) {
    // Remove espacos em branco no inicio/fim de cada parametro de texto digitado na busca.
    public AcervoIconograficoSearchFilter {
        tipoDocumentoNome = trim(tipoDocumentoNome);
        codigoIdentificacao = trim(codigoIdentificacao);
        titulo = trim(titulo);
        localizacao = trim(localizacao);
        localidade = trim(localidade);
        ano = trim(ano);
        assuntos = trim(assuntos);
        personalidades = trim(personalidades);
    }

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }
}
