package com.example.apesc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "empregado")
public class Empregado extends Pessoa {

    @Column(name = "numero_matricula")
    private String numeroMatricula;

    @Column(name = "cargo")
    private String cargo;

    @Column(name = "setor")
    private String setor;

}
