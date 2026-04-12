package com.example.apesc.dto;

import com.example.apesc.model.DiagnosticoRestauracao;
import com.example.apesc.model.Funcionario;
import com.example.apesc.model.AcervoDocumental;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticoRestauracaoDTO {

    private Integer id;
    private Long responsavelRestauracaoId;
    private LocalDate dataDiagnostico;
    private Integer numeroDocumento;
    private Long acervoDocumentalId;
    private String autor;
    private String titulo;
    private String local;
    private String editor;
    private String tecnica;
    private Integer numeroFolhas;
    private String dimensoes;
    private Integer mes;
    private Integer ano;
    private String volume;
    private String fasciculo;
    private Integer numero;
    private String estadoConservacao;
    private String numeroRegistro;
    private String data;
    private String entidadeProcedencia;
    private String enderecoProcedencia;
    private String contatoProcedencia;
    private String telefone;
    private Boolean documento;
    private Boolean livro;
    private Boolean folheto;
    private Boolean mapa;
    private Boolean jornal;
    private Boolean revista;
    private Boolean fotografia;
    private boolean albumFotografico;
    private Boolean encadernacao;

    public DiagnosticoRestauracao toEntity() {
        DiagnosticoRestauracao entity = new DiagnosticoRestauracao();
        entity.setId(this.id);
        
        if (this.responsavelRestauracaoId != null) {
            Funcionario func = new Funcionario();
            func.setId(this.responsavelRestauracaoId);
            entity.setResponsavelRestauracao(func);
        }
        
        if (this.acervoDocumentalId != null) {
            AcervoDocumental acervo = new AcervoDocumental();
            acervo.setId(this.acervoDocumentalId);
            entity.setAcervoDocumental(acervo);
        }

        entity.setDataDiagnostico(this.dataDiagnostico);
        entity.setNumeroDocumento(this.numeroDocumento);
        entity.setAutor(this.autor);
        entity.setTitulo(this.titulo);
        entity.setLocal(this.local);
        entity.setEditor(this.editor);
        entity.setTecnica(this.tecnica);
        entity.setNumeroFolhas(this.numeroFolhas);
        entity.setDimensoes(this.dimensoes);
        entity.setMes(this.mes);
        entity.setAno(this.ano);
        entity.setVolume(this.volume);
        entity.setFasciculo(this.fasciculo);
        entity.setNumero(this.numero);
        entity.setEstadoConservacao(this.estadoConservacao);
        entity.setNumeroRegistro(this.numeroRegistro);
        entity.setData(this.data);
        entity.setEntidadeProcedencia(this.entidadeProcedencia);
        entity.setEnderecoProcedencia(this.enderecoProcedencia);
        entity.setContatoProcedencia(this.contatoProcedencia);
        entity.setTelefone(this.telefone);
        entity.setDocumento(this.documento);
        entity.setLivro(this.livro);
        entity.setFolheto(this.folheto);
        entity.setMapa(this.mapa);
        entity.setJornal(this.jornal);
        entity.setRevista(this.revista);
        entity.setFotografia(this.fotografia);
        entity.setAlbumFotografico(this.albumFotografico);
        entity.setEncadernacao(this.encadernacao);

        return entity;
    }

    public static DiagnosticoRestauracaoDTO fromEntity(DiagnosticoRestauracao entity) {
        if (entity == null) return null;
        return new DiagnosticoRestauracaoDTO(
            entity.getId(),
            entity.getResponsavelRestauracao() != null ? entity.getResponsavelRestauracao().getId() : null,
            entity.getDataDiagnostico(),
            entity.getNumeroDocumento(),
            entity.getAcervoDocumental() != null ? entity.getAcervoDocumental().getId() : null,
            entity.getAutor(),
            entity.getTitulo(),
            entity.getLocal(),
            entity.getEditor(),
            entity.getTecnica(),
            entity.getNumeroFolhas(),
            entity.getDimensoes(),
            entity.getMes(),
            entity.getAno(),
            entity.getVolume(),
            entity.getFasciculo(),
            entity.getNumero(),
            entity.getEstadoConservacao(),
            entity.getNumeroRegistro(),
            entity.getData(),
            entity.getEntidadeProcedencia(),
            entity.getEnderecoProcedencia(),
            entity.getContatoProcedencia(),
            entity.getTelefone(),
            entity.getDocumento(),
            entity.getLivro(),
            entity.getFolheto(),
            entity.getMapa(),
            entity.getJornal(),
            entity.getRevista(),
            entity.getFotografia(),
            entity.isAlbumFotografico(),
            entity.getEncadernacao()
        );
    }
}
