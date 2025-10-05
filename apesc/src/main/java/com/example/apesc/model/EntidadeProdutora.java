package com.example.apesc.model;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "entidade_produtora")
public class EntidadeProdutora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "abreviacao")
    private String abreviacao;

    @OneToMany(mappedBy = "entidadeProdutora")
    private List<AcervoDocumental> acervoDocumental = new ArrayList()<>();

    @OneToMany(mappedBy = "entidadeReceptora")
    private List<AcervoDocumental> acervoDocumentalReceptora = new ArrayList<>();

}
