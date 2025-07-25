package com.example.apesc.model;

import com.example.apesc.model.enums.*;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "pesquisador")
public class Pesquisador extends Pessoa {

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "nacionalidade")
    private Nacionalidade nacionalidade;

    @Column(name = "numeroTelefone")
    private String numeroTelefone;

    @Column(name = "logradouro")
    private String logradouro;

    @Column(name = "numero_casa")
    private String numeroCasa;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private Estados estado;

    @Column(name = "cep")
    private String cep;

    @Column(name = "nivel_educacional")
    private NivelEducacional NivelEducacional;

    @Column(name = "instituicao_ensino")
    private String instituicaoEnsino;

    @Column(name = "curso")
    private String curso;

    @Column(name = "profissao")
    private String profissao;

    @Column(name = "assunto_pesquisa")
    private String assuntoPesquisa;

    @Column(name = "finalidade_pesquisa")
    private String finalidadePesquisa;

    @ElementCollection
    @CollectionTable(
            name = "pesquisador_periodo_estudo",
            joinColumns = @JoinColumn(name = "pesquisador_id")
    )
    @Column(name = "periodo_estudo")
    @Enumerated(EnumType.STRING)
    private Set<PeriodoEstudo> periodoEstudo = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "pesquisador_area_estudo",
            joinColumns = @JoinColumn(name = "pesquisador_id")
    )
    @Column(name = "area_estudo")
    @Enumerated(EnumType.STRING)
    private Set<AreaEstudo> areaEstudo = new HashSet<>();

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @LastModifiedDate
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "pesquisador", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RegistroConsulta> registroConsultas = new HashSet<>();

}

