package com.example.apesc.specification;

// Agrupa os parametros do GET /api/acervo-iconografico/personalidades/search num
// objeto so. Bindado direto da query string via @ModelAttribute no controller —
// mesmo padrao usado em BibliotecaApoioSearchFilter.
public record AcervoIconograficoPersonalidadesSearchFilter(
        // Busca por parte do nome da personalidade (ex.: "silva" acha "Joao da Silva").
        String nome
) {
    // Remove espacos em branco no inicio/fim do parametro digitado na busca.
    public AcervoIconograficoPersonalidadesSearchFilter {
        nome = trim(nome);
    }

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }
}
