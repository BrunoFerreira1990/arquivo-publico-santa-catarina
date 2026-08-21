package com.example.apesc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "acervo_documental_tombo")
public class AcervoDocumentalTombo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "acervo_documental_id", nullable = false)
    private AcervoDocumental acervoDocumental;

    @Column(name = "numero_tombo")
    private Integer numeroTombo;

    @Column(name = "periodo", nullable = false)
    private String periodo;

    @Column(name = "sem_consulta")
    private Boolean semConsulta;

}
