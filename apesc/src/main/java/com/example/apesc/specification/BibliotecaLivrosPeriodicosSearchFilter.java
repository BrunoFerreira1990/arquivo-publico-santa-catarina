package com.example.apesc.specification;

// Agrupa os parametros do GET /biblioteca-livros-periodicos/search num objeto so. Todos os
// campos sao opcionais. Bindado direto da query string via @ModelAttribute no
// controller — mesmo padrao usado em AcervoCartograficoSearchFilter.
public record BibliotecaLivrosPeriodicosSearchFilter(

        // Busca pelo NOME do tipo de documento (ex.: "Livro"), nao pelo id.
        String tipoDocumentoNome,
        String titulo,
        String subtitulo,
        String autores,
        String editora,
        String ano,
        String classificacao
) {
    // Remove espacos em branco no inicio/fim de cada parametro digitado na busca.
    public BibliotecaLivrosPeriodicosSearchFilter {
        tipoDocumentoNome = trim(tipoDocumentoNome);
        titulo = trim(titulo);
        subtitulo = trim(subtitulo);
        autores = trim(autores);
        editora = trim(editora);
        ano = trim(ano);
        classificacao = trim(classificacao);
    }

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }
}
