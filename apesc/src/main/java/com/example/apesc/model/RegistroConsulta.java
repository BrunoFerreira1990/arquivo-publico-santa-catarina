package com.example.apesc.model;

import com.example.apesc.model.enums.TipoConsulta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "registro_consulta")
@EntityListeners(AuditingEntityListener.class)
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
    @Column(name = "data_registro", updatable = false)
    private LocalDateTime dataRegistro;

    // Preenchida manualmente pelo service, só quando o registro é de fato
    // atualizado (não usa @LastModifiedDate porque essa anotação também
    // dispara na criação, igualando data_atualizacao a data_registro).
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_atualizacao_id")
    private Funcionario funcionarioAtualizacao;

}
