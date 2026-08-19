package com.example.apesc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "acervo_documental_processos")
public class AcervoDocumentalProcessos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "acervo_documental_id", nullable = false)
    private AcervoDocumental acervoDocumental;

    @Column(name = "caixa_identificacao")
    private String caixaIdentificacao;

    @Column(name = "localizacao")
    private String localizacao;

    @Column(name = "nome_processo")
    private String nomeProcesso;

    @Column(name = "data")
    private String data;

    @Column(name = "identificacao_pasta")
    private String identificacaoPasta;

    @Column(name = "disponibilidade")
    private Boolean disponibilidade;

}
