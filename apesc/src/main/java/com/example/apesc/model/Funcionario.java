package com.example.apesc.model;

import com.example.apesc.model.enums.Generos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "empregado")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "genero")
    private Generos genero;

    @Column(name = "email")
    private String email;

    @Column(name = "numero_matricula")
    private String numeroMatricula;

    @Column(name = "cargo")
    private String cargo;

    @Column(name = "setor")
    private String setor;

    @OneToMany(mappedBy = "funcionario")
    @JsonIgnore
    private List<RegistroConsulta> registroConsultas = new ArrayList<>();

    @OneToMany(mappedBy = "responsavelRestauracao")
    @JsonIgnore
    private List<DiagnosticoRestauracao> diagnosticosRealizados = new ArrayList<>();

    @OneToMany(mappedBy = "responsavelRestauracao")
    @JsonIgnore
    private List<ProcedimentoRestauracao> procedimentosRealizados = new ArrayList<>();

}
