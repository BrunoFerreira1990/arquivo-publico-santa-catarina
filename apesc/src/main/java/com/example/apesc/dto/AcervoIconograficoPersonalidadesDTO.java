package com.example.apesc.dto;

import com.example.apesc.model.AcervoIconograficoPersonalidades;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcervoIconograficoPersonalidadesDTO {

    private Long id;
    private String nome;

    public AcervoIconograficoPersonalidades toEntity() {
        AcervoIconograficoPersonalidades entity = new AcervoIconograficoPersonalidades();
        entity.setId(this.id);
        entity.setNome(this.nome);
        return entity;
    }

    public static AcervoIconograficoPersonalidadesDTO fromEntity(AcervoIconograficoPersonalidades entity) {
        if (entity == null) return null;
        return new AcervoIconograficoPersonalidadesDTO(entity.getId(), entity.getNome());
    }
}
