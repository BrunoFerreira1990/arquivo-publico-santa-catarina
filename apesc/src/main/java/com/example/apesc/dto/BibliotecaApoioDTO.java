package com.example.apesc.dto;

import com.example.apesc.model.BibliotecaApoio;
import com.example.apesc.model.EntidadeProdutora;
import com.example.apesc.model.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BibliotecaApoioDTO {

    private Long id;
    private Long tipoDocumentoId;
    private String tipoDocumentoNome;
    private Long entidadeProdutoraId;
    private String entidadeProdutoraNome;
    private String titulo;
    private String periodo;
    private Integer quantidadeVolume;
    private String identificador;
    private String localizacao;
    private Boolean disponibilidade;

    public BibliotecaApoio toEntity() {
        BibliotecaApoio entity = new BibliotecaApoio();
        entity.setId(this.id);

        if (this.tipoDocumentoId != null) {
            TipoDocumento tipoDocumento = new TipoDocumento();
            tipoDocumento.setId(this.tipoDocumentoId);
            entity.setTipoDocumento(tipoDocumento);
        }

        if (this.entidadeProdutoraId != null) {
            EntidadeProdutora entidadeProdutora = new EntidadeProdutora();
            entidadeProdutora.setId(this.entidadeProdutoraId);
            entity.setEntidadeProdutora(entidadeProdutora);
        }

        entity.setTitulo(this.titulo);
        entity.setPeriodo(this.periodo);
        entity.setQuantidadeVolume(this.quantidadeVolume);
        entity.setIdentificador(this.identificador);
        entity.setLocalizacao(this.localizacao);
        entity.setDisponibilidade(this.disponibilidade);
        return entity;
    }

    public static BibliotecaApoioDTO fromEntity(BibliotecaApoio entity) {
        if (entity == null) return null;
        return new BibliotecaApoioDTO(
            entity.getId(),
            entity.getTipoDocumento() != null ? entity.getTipoDocumento().getId() : null,
            entity.getTipoDocumento() != null ? entity.getTipoDocumento().getNomeDocumento() : null,
            entity.getEntidadeProdutora() != null ? entity.getEntidadeProdutora().getId() : null,
            entity.getEntidadeProdutora() != null ? entity.getEntidadeProdutora().getNome() : null,
            entity.getTitulo(),
            entity.getPeriodo(),
            entity.getQuantidadeVolume(),
            entity.getIdentificador(),
            entity.getLocalizacao(),
            entity.getDisponibilidade()
        );
    }
}
