package com.example.apesc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "biblioteca_livros_periodicos")
public class BibliotecaLivrosPeriodicos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_documento_id", nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "subtitulo")
    private String subtitulo;

    @Column(name = "autores")
    private String autores;

    @Column(name = "editora")
    private String editora;

    @Column(name = "edicao")
    private String edicao;

    @Column(name = "ano")
    private String ano;

    @Column(name = "quantidade_exemplar")
    private Integer quantidadeExemplar;

    @Column(name = "classificacao")
    private String classificacao;

    @Column(name = "localizacao")
    private String localizacao;

    @Column(name = "disponibilidade")
    private Boolean disponibilidade;

}
