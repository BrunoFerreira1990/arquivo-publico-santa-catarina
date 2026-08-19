package com.example.apesc.dto;

import com.example.apesc.model.AcervoDocumental;
import com.example.apesc.model.AcervoDocumentalTombo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcervoDocumentalTomboDTO {

    private Long id;
    private Long acervoDocumentalId;
    private Integer numeroTombo;
    private String periodo;

    public AcervoDocumentalTombo toEntity() {
        AcervoDocumentalTombo entity = new AcervoDocumentalTombo();
        entity.setId(this.id);

        if (this.acervoDocumentalId != null) {
            AcervoDocumental acervo = new AcervoDocumental();
            acervo.setId(this.acervoDocumentalId);
            entity.setAcervoDocumental(acervo);
        }

        entity.setNumeroTombo(this.numeroTombo);
        entity.setPeriodo(this.periodo);
        return entity;
    }

    public static AcervoDocumentalTomboDTO fromEntity(AcervoDocumentalTombo entity) {
        if (entity == null) return null;
        return new AcervoDocumentalTomboDTO(
            entity.getId(),
            entity.getAcervoDocumental() != null ? entity.getAcervoDocumental().getId() : null,
            entity.getNumeroTombo(),
            entity.getPeriodo()
        );
    }
}
