package com.example.apesc.model;

import com.example.apesc.model.enums.ConsultationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "registro_consulta")
public class RegistroConsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pesquisador_id", nullable = false)
    private Pesquisador pesquisadorId;

    @Column(name = "data_pesquisa")
    private LocalDate dataPesquisa;

    @Column(name = "tipo_consulta")
    private ConsultationType tipoConsulta;

    @Column(name = "acervo_documental_id")
    private String acervoDocumentalId;

    @Column(name = "periodo")
    private String periodo;

    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "empregado_id")
    private String empregadoId;

    @Column(name = "data_registro")
    private LocalDateTime dataRegistro;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "empregado_atualizacao_id")
    private String empregadoAtualizacaoId;

}
