package com.example.apesc.model;

import com.example.apesc.model.enums.TipoConsulta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
    private Pesquisador pesquisador;

    @Column(name = "data_pesquisa", nullable = false)
    private LocalDate dataPesquisa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_consulta")
    private TipoConsulta tipoConsulta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acervo_documental_id", nullable = false)
    private AcervoDocumental acervoDocumental;

    @Column(name = "periodo")
    private String periodo;

    @Column(name = "quantidade")
    private Integer quantidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @CreatedDate
    @Column(name = "data_registro")
    private LocalDateTime dataRegistro;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_atualizacao_id")
    private Funcionario funcionarioAtualizacao;

}
