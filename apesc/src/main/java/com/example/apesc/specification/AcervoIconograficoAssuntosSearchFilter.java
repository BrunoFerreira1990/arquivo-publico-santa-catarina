package com.example.apesc.specification;

// Agrupa os parametros do GET /api/acervo-iconografico/assuntos/search num objeto
// so. Bindado direto da query string via @ModelAttribute no controller — mesmo
// padrao usado em BibliotecaApoioSearchFilter.
public record AcervoIconograficoAssuntosSearchFilter(
        // Busca por parte da descricao do assunto (ex.: "hist" acha "Historia Politica").
        String descricao
) {
    // Remove espacos em branco no inicio/fim do parametro digitado na busca.
    public AcervoIconograficoAssuntosSearchFilter {
        descricao = trim(descricao);
    }

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }
}
