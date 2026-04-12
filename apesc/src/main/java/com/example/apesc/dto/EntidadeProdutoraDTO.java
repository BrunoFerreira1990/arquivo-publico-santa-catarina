package com.example.apesc.dto;

import com.example.apesc.model.EntidadeProdutora;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntidadeProdutoraDTO {

    private Long id;
    private String nome;
    private String abreviacao;

    public EntidadeProdutora toEntity() {
        EntidadeProdutora entity = new EntidadeProdutora();
        entity.setId(this.id);
        entity.setNome(this.nome);
        entity.setAbreviacao(this.abreviacao);
        return entity;
    }

    public static EntidadeProdutoraDTO fromEntity(EntidadeProdutora entity) {
        if (entity == null) return null;
        return new EntidadeProdutoraDTO(
            entity.getId(),
            entity.getNome(),
            entity.getAbreviacao()
        );
    }
}
