package com.example.apesc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "acervo_cartografico")
public class AcervoCartografico {

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

    @Column(name = "dimensao")
    private String dimensao;

    @Column(name = "localizacao")
    private String localizacao;

    @Column(name = "localidade")
    private String localidade;

    @Column(name = "quantidade_volume")
    private Integer quantidadeVolume;

    @Column(name = "disponibilidade")
    private Boolean disponibilidade;

}
