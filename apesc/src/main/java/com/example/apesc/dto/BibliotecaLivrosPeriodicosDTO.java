package com.example.apesc.dto;

import com.example.apesc.model.BibliotecaLivrosPeriodicos;
import com.example.apesc.model.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BibliotecaLivrosPeriodicosDTO {

    private Long id;
    private Long tipoDocumentoId;
    private String tipoDocumentoNome;
    private String titulo;
    private String subtitulo;
    private String autores;
    private String editora;
    private String edicao;
    private String ano;
    private Integer quantidadeExemplar;
    private String classificacao;
    private String localizacao;
    private Boolean disponibilidade;

    public BibliotecaLivrosPeriodicos toEntity() {
        BibliotecaLivrosPeriodicos entity = new BibliotecaLivrosPeriodicos();
        entity.setId(this.id);

        if (this.tipoDocumentoId != null) {
            TipoDocumento tipoDocumento = new TipoDocumento();
            tipoDocumento.setId(this.tipoDocumentoId);
            entity.setTipoDocumento(tipoDocumento);
        }

        entity.setTitulo(this.titulo);
        entity.setSubtitulo(this.subtitulo);
        entity.setAutores(this.autores);
        entity.setEditora(this.editora);
        entity.setEdicao(this.edicao);
        entity.setAno(this.ano);
        entity.setQuantidadeExemplar(this.quantidadeExemplar);
        entity.setClassificacao(this.classificacao);
        entity.setLocalizacao(this.localizacao);
        entity.setDisponibilidade(this.disponibilidade);
        return entity;
    }

    public static BibliotecaLivrosPeriodicosDTO fromEntity(BibliotecaLivrosPeriodicos entity) {
        if (entity == null) return null;
        return new BibliotecaLivrosPeriodicosDTO(
            entity.getId(),
            entity.getTipoDocumento() != null ? entity.getTipoDocumento().getId() : null,
            entity.getTipoDocumento() != null ? entity.getTipoDocumento().getNomeDocumento() : null,
            entity.getTitulo(),
            entity.getSubtitulo(),
            entity.getAutores(),
            entity.getEditora(),
            entity.getEdicao(),
            entity.getAno(),
            entity.getQuantidadeExemplar(),
            entity.getClassificacao(),
            entity.getLocalizacao(),
            entity.getDisponibilidade()
        );
    }
}
