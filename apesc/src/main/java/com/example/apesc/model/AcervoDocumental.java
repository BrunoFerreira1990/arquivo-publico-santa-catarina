package com.example.apesc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "acervo_documental")
public class AcervoDocumental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_documento_id", nullable = false)
    private TipoDocumento tipoDocumento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entidade_produtora_id", nullable = false)
    private EntidadeProdutora entidadeProdutora;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "entidade_receptora_id", nullable = true)
    private EntidadeProdutora entidadeReceptora;

    @Column(name = "natureza_transacao")
    private String naturezaTransacao;

    @Column(name = "periodo")
    private String periodo;

    @Column(name = "estante")
    private String estante;

    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "disponibilidade")
    private Boolean disponibilidade;

    @OneToMany(mappedBy = "acervoDocumental")
    private List<DiagnosticoRestauracao> diagnosticoRestauracao = new ArrayList<>();

}
