package com.example.apesc.dto;

import com.example.apesc.model.Permissoes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissoesDTO {

    private Long id;
    private String nomeRegra;

    public Permissoes toEntity() {
        Permissoes entity = new Permissoes();
        entity.setId(this.id);
        entity.setNomeRegra(this.nomeRegra);
        return entity;
    }

    public static PermissoesDTO fromEntity(Permissoes entity) {
        if (entity == null) return null;
        return new PermissoesDTO(entity.getId(), entity.getNomeRegra());
    }
}
