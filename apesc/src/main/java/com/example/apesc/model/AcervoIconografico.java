package com.example.apesc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "acervo_iconografico")
public class AcervoIconografico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_documento_id", nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(name = "codigo_identificacao")
    private String codigoIdentificacao;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "localizacao")
    private String localizacao;

    @Column(name = "disponibilidade")
    private Boolean disponibilidade;

    @Column(name = "ano", nullable = false)
    private String ano;

    // N:N de verdade — uma foto pode ter varios assuntos, um assunto pode estar em
    // varias fotos. Tabela associativa "acervo_iconografico_assunto_vinculo" (nome
    // de proposito diferente do catalogo "acervo_iconografico_assuntos", pra nao
    // confundir singular/plural). Sem colunas extras na relacao, entao @ManyToMany
    // puro resolve — nao precisa de entidade Java propria pra tabela associativa.
    @ManyToMany
    @JoinTable(
            name = "acervo_iconografico_assunto_vinculo",
            joinColumns = @JoinColumn(name = "acervo_iconografico_id"),
            inverseJoinColumns = @JoinColumn(name = "assunto_id")
    )
    private Set<AcervoIconograficoAssuntos> assuntos = new HashSet<>();

}
