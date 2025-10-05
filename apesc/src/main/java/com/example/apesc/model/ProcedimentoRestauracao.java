package com.example.apesc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "procedimento_restauracao")
public class ProcedimentoRestauracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "diagnostico_restauracao_id", unique = true)
    private DiagnosticoRestauracao diagnosticoRestauracao;

    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false, unique = true)
    private Funcionario responsavelRestauracao;
    
    @Column(name = "data_saida")
    private LocalDate dataSaida;
    
    @Column(name = "estado_conservacao")
    private String estadoConservacao;

    @Column(name = "manchas_agua")
    private Boolean manchasDeAgua;

    @Column(name = "fungos")
    private Boolean fungos;

    @Column(name = "lama")
    private Boolean lama;

    @Column(name = "ferrugem")
    private Boolean ferrugem;

    @Column(name = "buracos")
    private Boolean buracos;

    @Column(name = "fita_adesiva")
    private Boolean fitaAdesiva;

    @Column(name = "oxidacao_tinta")
    private Boolean oxidacaoTinta;

    @Column(name = "insetos")
    private Boolean insetos;

    @Column(name = "dobras")
    private Boolean dobras;

    @Column(name = "gordura")
    private Boolean gordura;

    @Column(name = "ondulacoes")
    private Boolean ondulacoes;

    @Column(name = "durex")
    private Boolean durex;

    @Column(name = "rasgos_cortes")
    private Boolean rasigosCortes;

    @Column(name = "sujidades")
    private Boolean sujidades;

    @Column(name = "cola")
    private Boolean cola;

    @Column(name = "perda_capa")
    private Boolean perdaCapa;

    @Column(name = "perda_pigmento")
    private Boolean perdaPigmento;

    @Column(name = "escurecimento")
    private Boolean escurecimento;

    @Column(name = "queimaduras")
    private Boolean queimaduras;

    @Column(name = "perda_folhas")
    private Boolean perdaFolhas;

    @Column(name = "outros", columnDefinition = "TEXT")
    private String outros;

    @Column(name = "desmonte", columnDefinition = "TEXT")
    private String desmonte;

    @Column(name = "limpeza_mecanica", columnDefinition = "TEXT")
    private String limpezaMecanica;
    
    @Column(name = "banho", columnDefinition = "TEXT")
    private String banho;
    
    @Column(name = "teste_quimico", columnDefinition = "TEXT")
    private String testeQuimico;
    
    @Column(name = "reconstituicao", columnDefinition = "TEXT")
    private String reconstituicao;
    
    @Column(name = "velatura")
    private Integer velatura;
    
    @Column(name = "encolagem")
    private Integer encolagem;
    
    @Column(name = "reforco")
    private Integer reforco;
    
    @Column(name = "carcela")
    private Integer carcela;
    
    @Column(name = "restauracao")
    private Integer restauracao;
    
    @Column(name = "obturacao")
    private Integer obturacao;
    
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

}
