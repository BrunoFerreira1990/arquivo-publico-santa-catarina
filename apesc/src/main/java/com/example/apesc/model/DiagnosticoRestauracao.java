package com.example.apesc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "diagnostico_restauracao")
public class DiagnosticoRestauracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false, unique = true)
    private Funcionario responsavelRestauracao;

    @Column(name = "data_diagnostico")
    private LocalDate dataDiagnostico;

    @Column(name = "numero_documento")
    private Integer numeroDocumento;

    @ManyToOne
    @JoinColumn(name = "documento_id", nullable = false)
    private AcervoDocumental acervoDocumental;

    @Column(name = "autor")
    private String autor;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "local")
    private String local;

    @Column(name = "editor")
    private String editor;

    @Column(name = "tecnica")
    private String tecnica;

    @Column(name = "numero_folhas")
    private Integer numeroFolhas;

    @Column(name = "dimensoes")
    private String dimensoes;

    @Column(name = "mes")
    private Integer mes;

    @Column(name = "ano")
    private Integer ano;

    @Column(name = "volume")
    private String volume;

    @Column(name = "fasciculo")
    private String fasciculo;

    @Column(name = "folhas")
    private Integer numero;

    @Column(name = "estado_conservacao")
    private String estadoConservacao;

    @Column(name = "numero_registro")
    private String numeroRegistro;

    @Column(name = "data")
    private String data;

    @Column(name = "entidade_procedencia")
    private String entidadeProcedencia;

    @Column(name = "endereco_procedencia")
    private String enderecoProcedencia;

    @Column(name = "contato_procedencia")
    private String contatoProcedencia;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "documento")
    private Boolean documento;

    @Column(name = "livro")
    private Boolean livro;

    @Column(name = "folheto")
    private Boolean folheto;

    @Column(name = "mapa")
    private Boolean mapa;

    @Column(name = "jornal")
    private Boolean jornal;

    @Column(name = "revista")
    private Boolean revista;

    @Column(name = "fotografia")
    private Boolean fotografia;

    @Column(name = "album_fotografico")
    private boolean albumFotografico;

    @Column(name = "encadernacao")
    private Boolean encadernacao;


    @OneToOne(mappedBy = "diagnosticoRestauracao", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProcedimentoRestauracao procedimentoRestauracao;

    @PreRemove
    private void removeProcedimentoRestauracao() {
        if (procedimentoRestauracao != null) {
            procedimentoRestauracao.setDiagnosticoRestauracao(null);
        }
    }

    public void setProcedimentoRestauracao(ProcedimentoRestauracao procedimento) {
        if (procedimento == null) {
            if (this.procedimentoRestauracao != null) {
                this.procedimentoRestauracao.setDiagnosticoRestauracao(null);
            }
        } else {
            procedimento.setDiagnosticoRestauracao(this);
        }
        this.procedimentoRestauracao = procedimento;
    }

}
