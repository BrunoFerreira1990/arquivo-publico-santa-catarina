package com.example.apesc.dto;

import com.example.apesc.model.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocumentoDTO {

    private Long id;
    private String nomeDocumento;

    public TipoDocumento toEntity() {
        TipoDocumento entity = new TipoDocumento();
        entity.setId(this.id);
        entity.setNomeDocumento(this.nomeDocumento);
        return entity;
    }

    public static TipoDocumentoDTO fromEntity(TipoDocumento entity) {
        if (entity == null) return null;
        return new TipoDocumentoDTO(
            entity.getId(),
            entity.getNomeDocumento()
        );
    }
}
