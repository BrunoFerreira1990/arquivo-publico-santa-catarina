package com.example.apesc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "biblioteca_apoio")
public class BibliotecaApoio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_documento_id", nullable = false)
    private TipoDocumento tipoDocumento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orgao_produtor_id", nullable = false)
    private EntidadeProdutora entidadeProdutora;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "periodo")
    private String periodo;

    @Column(name = "quantidade_volume")
    private Integer quantidadeVolume;

    @Column(name = "identificador")
    private String identificador;

    @Column(name = "localizacao")
    private String localizacao;

    @Column(name = "disponibilidade")
    private Boolean disponibilidade;

}
