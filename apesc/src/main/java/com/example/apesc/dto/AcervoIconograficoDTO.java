package com.example.apesc.dto;

import com.example.apesc.model.AcervoIconografico;
import com.example.apesc.model.AcervoIconograficoAssuntos;
import com.example.apesc.model.AcervoIconograficoPersonalidades;
import com.example.apesc.model.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcervoIconograficoDTO {

    private Long id;
    private Long tipoDocumentoId;
    private String tipoDocumentoNome;
    private String codigoIdentificacao;
    private String titulo;
    private String localizacao;
    private String localidade;
    private Boolean disponibilidade;
    private String ano;
    private List<Long> assuntoIds;
    private List<String> assuntos;
    private List<Long> personalidadeIds;
    private List<String> personalidades;

    public AcervoIconografico toEntity() {
        AcervoIconografico entity = new AcervoIconografico();
        entity.setId(this.id);

        if (this.tipoDocumentoId != null) {
            TipoDocumento tipoDocumento = new TipoDocumento();
            tipoDocumento.setId(this.tipoDocumentoId);
            entity.setTipoDocumento(tipoDocumento);
        }

        entity.setCodigoIdentificacao(this.codigoIdentificacao);
        entity.setTitulo(this.titulo);
        entity.setLocalizacao(this.localizacao);
        entity.setLocalidade(this.localidade);
        entity.setDisponibilidade(this.disponibilidade);
        entity.setAno(this.ano);

        if (this.assuntoIds != null) {
            entity.setAssuntos(this.assuntoIds.stream().map(assuntoId -> {
                AcervoIconograficoAssuntos assunto = new AcervoIconograficoAssuntos();
                assunto.setId(assuntoId);
                return assunto;
            }).collect(Collectors.toSet()));
        }

        if (this.personalidadeIds != null) {
            entity.setPersonalidades(this.personalidadeIds.stream().map(personalidadeId -> {
                AcervoIconograficoPersonalidades personalidade = new AcervoIconograficoPersonalidades();
                personalidade.setId(personalidadeId);
                return personalidade;
            }).collect(Collectors.toSet()));
        }

        return entity;
    }

    public static AcervoIconograficoDTO fromEntity(AcervoIconografico entity) {
        if (entity == null) return null;
        return new AcervoIconograficoDTO(
            entity.getId(),
            entity.getTipoDocumento() != null ? entity.getTipoDocumento().getId() : null,
            entity.getTipoDocumento() != null ? entity.getTipoDocumento().getNomeDocumento() : null,
            entity.getCodigoIdentificacao(),
            entity.getTitulo(),
            entity.getLocalizacao(),
            entity.getLocalidade(),
            entity.getDisponibilidade(),
            entity.getAno(),
            entity.getAssuntos() != null
                ? entity.getAssuntos().stream().map(AcervoIconograficoAssuntos::getId).collect(Collectors.toList())
                : null,
            entity.getAssuntos() != null
                ? entity.getAssuntos().stream().map(AcervoIconograficoAssuntos::getDescricao).collect(Collectors.toList())
                : null,
            entity.getPersonalidades() != null
                ? entity.getPersonalidades().stream().map(AcervoIconograficoPersonalidades::getId).collect(Collectors.toList())
                : null,
            entity.getPersonalidades() != null
                ? entity.getPersonalidades().stream().map(AcervoIconograficoPersonalidades::getNome).collect(Collectors.toList())
                : null
        );
    }
}
