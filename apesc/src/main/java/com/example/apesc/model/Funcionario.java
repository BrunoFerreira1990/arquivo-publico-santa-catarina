package com.example.apesc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "empregado")
public class Funcionario extends Pessoa {

    @Column(name = "numero_matricula")
    private String numeroMatricula;

    @Column(name = "cargo")
    private String cargo;

    @Column(name = "setor")
    private String setor;

    @OneToMany(mappedBy = "empregado")
    private List<RegistroConsulta> registroConsultas = new ArrayList<>();

    @OneToMany(mappedBy = "responsavelRestauracao")
    private List<DiagnosticoRestauracao> diagnosticosRealizados = new ArrayList<>();

    @OneToMany(mappedBy = "responsavelRestauracao")
    private List<ProcedimentoRestauracao> procedimentosRealizados = new ArrayList<>();

}
