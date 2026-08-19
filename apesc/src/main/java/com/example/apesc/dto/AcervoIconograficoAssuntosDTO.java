package com.example.apesc.dto;

import com.example.apesc.model.AcervoIconograficoAssuntos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcervoIconograficoAssuntosDTO {

    private Long id;
    private String assuntos;

    public AcervoIconograficoAssuntos toEntity() {
        AcervoIconograficoAssuntos entity = new AcervoIconograficoAssuntos();
        entity.setId(this.id);
        entity.setAssuntos(this.assuntos);
        return entity;
    }

    public static AcervoIconograficoAssuntosDTO fromEntity(AcervoIconograficoAssuntos entity) {
        if (entity == null) return null;
        return new AcervoIconograficoAssuntosDTO(entity.getId(), entity.getAssuntos());
    }
}
