package com.example.apesc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "acervo_documental")
public class AcervoDocumental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer tipoDocumentoId;

    private String orgaoProdutorId;

    private String orgaoReceptorId;

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

}
