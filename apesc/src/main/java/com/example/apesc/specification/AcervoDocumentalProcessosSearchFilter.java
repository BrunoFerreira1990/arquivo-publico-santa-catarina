package com.example.apesc.specification;

// Agrupa os parametros do GET /acervo-documental/processos/search num objeto so.
// Todos os campos sao opcionais. Bindado direto da query string via @ModelAttribute
// no controller — mesmo padrao usado em BibliotecaApoioSearchFilter.
public record AcervoDocumentalProcessosSearchFilter(

        // Filtra pelo acervo documental vinculado atraves dos NOMES que constam nele
        // (nao pelo id), no mesmo padrao dos outros searches — ver
        // AcervoDocumentalSpecification / RegistroConsultaSpecification.
        String tipoDocumentoNome,
        String entidadeProdutoraNome,
        String entidadeReceptoraNome,
        String caixaIdentificacao,
        String localizacao,
        String nome,
        String data,
        String identificacaoPasta,
        Boolean disponibilidade
) {
    // Remove espacos em branco no inicio/fim de cada parametro de texto digitado na busca.
    public AcervoDocumentalProcessosSearchFilter {
        tipoDocumentoNome = trim(tipoDocumentoNome);
        entidadeProdutoraNome = trim(entidadeProdutoraNome);
        entidadeReceptoraNome = trim(entidadeReceptoraNome);
        caixaIdentificacao = trim(caixaIdentificacao);
        localizacao = trim(localizacao);
        nome = trim(nome);
        data = trim(data);
        identificacaoPasta = trim(identificacaoPasta);
    }

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }
}
