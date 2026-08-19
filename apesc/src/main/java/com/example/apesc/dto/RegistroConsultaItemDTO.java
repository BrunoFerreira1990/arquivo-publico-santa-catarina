package com.example.apesc.dto;

import com.example.apesc.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Representa 1 item consultado, de QUALQUER um dos 6 tipos de acervo. O cliente
// preenche exatamente um dos 6 campos de acervo — a validacao (RegistroConsultaValidation)
// garante isso, espelhando o @Check que a tabela tem no banco.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroConsultaItemDTO {

    private Long id;
    private Long acervoDocumentalId;
    private Long acervoDocumentalProcessosId;
    private Long acervoIconograficoId;
    private Long acervoCartograficoId;
    private Long bibliotecaLivrosPeriodicosId;
    private Long bibliotecaApoioId;
    private Integer quantidade;
    private String periodo;

    public RegistroConsultaItem toEntity() {
        RegistroConsultaItem entity = new RegistroConsultaItem();
        entity.setId(this.id);

        if (this.acervoDocumentalId != null) {
            AcervoDocumental acervo = new AcervoDocumental();
            acervo.setId(this.acervoDocumentalId);
            entity.setAcervoDocumental(acervo);
        }

        if (this.acervoDocumentalProcessosId != null) {
            AcervoDocumentalProcessos processo = new AcervoDocumentalProcessos();
            processo.setId(this.acervoDocumentalProcessosId);
            entity.setAcervoDocumentalProcessos(processo);
        }

        if (this.acervoIconograficoId != null) {
            AcervoIconografico acervo = new AcervoIconografico();
            acervo.setId(this.acervoIconograficoId);
            entity.setAcervoIconografico(acervo);
        }

        if (this.acervoCartograficoId != null) {
            AcervoCartografico acervo = new AcervoCartografico();
            acervo.setId(this.acervoCartograficoId);
            entity.setAcervoCartografico(acervo);
        }

        if (this.bibliotecaLivrosPeriodicosId != null) {
            BibliotecaLivrosPeriodicos livro = new BibliotecaLivrosPeriodicos();
            livro.setId(this.bibliotecaLivrosPeriodicosId);
            entity.setBibliotecaLivrosPeriodicos(livro);
        }

        if (this.bibliotecaApoioId != null) {
            BibliotecaApoio apoio = new BibliotecaApoio();
            apoio.setId(this.bibliotecaApoioId);
            entity.setBibliotecaApoio(apoio);
        }

        entity.setQuantidade(this.quantidade);
        entity.setPeriodo(this.periodo);

        return entity;
    }

    public static RegistroConsultaItemDTO fromEntity(RegistroConsultaItem entity) {
        if (entity == null) return null;
        return new RegistroConsultaItemDTO(
            entity.getId(),
            entity.getAcervoDocumental() != null ? entity.getAcervoDocumental().getId() : null,
            entity.getAcervoDocumentalProcessos() != null ? entity.getAcervoDocumentalProcessos().getId() : null,
            entity.getAcervoIconografico() != null ? entity.getAcervoIconografico().getId() : null,
            entity.getAcervoCartografico() != null ? entity.getAcervoCartografico().getId() : null,
            entity.getBibliotecaLivrosPeriodicos() != null ? entity.getBibliotecaLivrosPeriodicos().getId() : null,
            entity.getBibliotecaApoio() != null ? entity.getBibliotecaApoio().getId() : null,
            entity.getQuantidade(),
            entity.getPeriodo()
        );
    }
}
