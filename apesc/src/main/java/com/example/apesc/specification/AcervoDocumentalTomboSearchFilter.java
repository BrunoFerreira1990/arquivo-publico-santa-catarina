package com.example.apesc.specification;

// Agrupa os parametros do GET /acervo-documental/tombo/search num objeto so.
// Todos os campos sao opcionais. Bindado direto da query string via @ModelAttribute
// no controller — mesmo padrao usado em AcervoDocumentalProcessosSearchFilter.
public record AcervoDocumentalTomboSearchFilter(

        // Numero do tombo (busca exata — e um identificador numerico, nao texto).
        Integer numeroTombo,

        // Filtra pelo acervo documental vinculado atraves dos NOMES que constam nele
        // (nao pelo id), no mesmo padrao dos outros searches — ver
        // AcervoDocumentalSpecification / AcervoDocumentalProcessosSpecification.
        String tipoDocumentoNome,
        String entidadeProdutoraNome,
        String entidadeReceptoraNome,

        // Periodo do proprio registro tombo (busca parcial).
        String periodo,

        Boolean semConsulta
) {
    // Remove espacos em branco no inicio/fim de cada parametro de texto digitado na busca.
    public AcervoDocumentalTomboSearchFilter {
        tipoDocumentoNome = trim(tipoDocumentoNome);
        entidadeProdutoraNome = trim(entidadeProdutoraNome);
        entidadeReceptoraNome = trim(entidadeReceptoraNome);
        periodo = trim(periodo);
    }

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }
}
