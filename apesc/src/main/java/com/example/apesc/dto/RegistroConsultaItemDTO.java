package com.example.apesc.dto;

import com.example.apesc.model.AcervoCartografico;
import com.example.apesc.model.AcervoDocumental;
import com.example.apesc.model.RegistroConsultaItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Representa 1 item consultado, de QUALQUER tipo de acervo. O cliente preenche
// exatamente um dos dois campos de acervo (acervoDocumentalId OU
// acervoCartograficoId) — a validacao (RegistroConsultaValidation) garante isso.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroConsultaItemDTO {

    private Long id;
    private Long acervoDocumentalId;
    private Long acervoCartograficoId;
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

        if (this.acervoCartograficoId != null) {
            AcervoCartografico acervo = new AcervoCartografico();
            acervo.setId(this.acervoCartograficoId);
            entity.setAcervoCartografico(acervo);
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
            entity.getAcervoCartografico() != null ? entity.getAcervoCartografico().getId() : null,
            entity.getQuantidade(),
            entity.getPeriodo()
        );
    }
}
