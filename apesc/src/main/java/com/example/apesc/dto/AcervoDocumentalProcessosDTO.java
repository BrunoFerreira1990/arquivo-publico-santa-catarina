package com.example.apesc.dto;

import com.example.apesc.model.AcervoDocumental;
import com.example.apesc.model.AcervoDocumentalProcessos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcervoDocumentalProcessosDTO {

    private Long id;
    private Long acervoDocumentalId;
    private String caixaIdentificacao;
    private String localizacao;
    private String nomeProcesso;
    private String data;
    private String identificacaoPasta;
    private Boolean disponibilidade;

    public AcervoDocumentalProcessos toEntity() {
        AcervoDocumentalProcessos entity = new AcervoDocumentalProcessos();
        entity.setId(this.id);

        if (this.acervoDocumentalId != null) {
            AcervoDocumental acervo = new AcervoDocumental();
            acervo.setId(this.acervoDocumentalId);
            entity.setAcervoDocumental(acervo);
        }

        entity.setCaixaIdentificacao(this.caixaIdentificacao);
        entity.setLocalizacao(this.localizacao);
        entity.setNomeProcesso(this.nomeProcesso);
        entity.setData(this.data);
        entity.setIdentificacaoPasta(this.identificacaoPasta);
        entity.setDisponibilidade(this.disponibilidade);
        return entity;
    }

    public static AcervoDocumentalProcessosDTO fromEntity(AcervoDocumentalProcessos entity) {
        if (entity == null) return null;
        return new AcervoDocumentalProcessosDTO(
            entity.getId(),
            entity.getAcervoDocumental() != null ? entity.getAcervoDocumental().getId() : null,
            entity.getCaixaIdentificacao(),
            entity.getLocalizacao(),
            entity.getNomeProcesso(),
            entity.getData(),
            entity.getIdentificacaoPasta(),
            entity.getDisponibilidade()
        );
    }
}
