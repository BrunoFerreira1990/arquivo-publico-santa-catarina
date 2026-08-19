package com.example.apesc.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(
        name = "registro_consulta_item",
        uniqueConstraints = {
                // Cada UNIQUE eh so 2 colunas (nao as 6 juntas) de proposito: em SQL, NULL
                // nunca "bate" com outro NULL numa constraint UNIQUE. Como cada linha so
                // preenche UMA das seis FKs de acervo (as outras cinco ficam null), isso
                // garante que duplicidade so eh barrada dentro do mesmo tipo, sem linhas de
                // tipos diferentes colidirem entre si so por compartilharem colunas nulas.
                @UniqueConstraint(name = "uk_rc_item_documental", columnNames = {"registro_consulta_id", "acervo_documental_id"}),
                @UniqueConstraint(name = "uk_rc_item_documental_processos", columnNames = {"registro_consulta_id", "acervo_documental_processos_id"}),
                @UniqueConstraint(name = "uk_rc_item_iconografico", columnNames = {"registro_consulta_id", "acervo_iconografico_id"}),
                @UniqueConstraint(name = "uk_rc_item_cartografico", columnNames = {"registro_consulta_id", "acervo_cartografico_id"}),
                @UniqueConstraint(name = "uk_rc_item_biblioteca_livros", columnNames = {"registro_consulta_id", "biblioteca_livros_periodicos_id"}),
                @UniqueConstraint(name = "uk_rc_item_biblioteca_apoio", columnNames = {"registro_consulta_id", "biblioteca_apoio_id"})
        }
)
// Garante, no banco, que cada linha referencia exatamente 1 dos 6 tipos de acervo —
// nunca mais de um, nunca nenhum. Usa contagem via CASE (em vez da forma OR-de-ANDs)
// porque com 6 colunas a forma explicita ficaria enorme.
@Check(constraints =
        "(CASE WHEN acervo_documental_id IS NOT NULL THEN 1 ELSE 0 END) + " +
        "(CASE WHEN acervo_documental_processos_id IS NOT NULL THEN 1 ELSE 0 END) + " +
        "(CASE WHEN acervo_iconografico_id IS NOT NULL THEN 1 ELSE 0 END) + " +
        "(CASE WHEN acervo_cartografico_id IS NOT NULL THEN 1 ELSE 0 END) + " +
        "(CASE WHEN biblioteca_livros_periodicos_id IS NOT NULL THEN 1 ELSE 0 END) + " +
        "(CASE WHEN biblioteca_apoio_id IS NOT NULL THEN 1 ELSE 0 END) = 1"
)
public class RegistroConsultaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Exclui do equals/hashCode/toString pra nao entrar em recursao infinita com
    // RegistroConsulta.itens (que referencia de volta este item).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registro_consulta_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private RegistroConsulta registroConsulta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acervo_documental_id")
    private AcervoDocumental acervoDocumental;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acervo_documental_processos_id")
    private AcervoDocumentalProcessos acervoDocumentalProcessos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acervo_iconografico_id")
    private AcervoIconografico acervoIconografico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acervo_cartografico_id")
    private AcervoCartografico acervoCartografico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "biblioteca_livros_periodicos_id")
    private BibliotecaLivrosPeriodicos bibliotecaLivrosPeriodicos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "biblioteca_apoio_id")
    private BibliotecaApoio bibliotecaApoio;

    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "periodo")
    private String periodo;

}
