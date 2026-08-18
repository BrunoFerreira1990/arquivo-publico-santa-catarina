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
                // Cada UNIQUE eh so 2 colunas (nao as 3 juntas) de proposito: em SQL, NULL
                // nunca "bate" com outro NULL numa constraint UNIQUE. Como cada linha so
                // preenche UMA das duas FKs de acervo (a outra fica null), isso garante que
                // duplicidade so eh barrada dentro do mesmo tipo (2 linhas com o mesmo
                // acervo_documental_id pro mesmo registro), sem as linhas de tipos
                // diferentes colidirem entre si so por ambas terem a outra coluna null.
                @UniqueConstraint(name = "uk_registro_consulta_item_documental", columnNames = {"registro_consulta_id", "acervo_documental_id"}),
                @UniqueConstraint(name = "uk_registro_consulta_item_cartografico", columnNames = {"registro_consulta_id", "acervo_cartografico_id"})
        }
)
// Garante, no banco, que cada linha referencia exatamente 1 tipo de acervo — nunca
// os dois ao mesmo tempo, nunca nenhum. Ver decisao de arquitetura: tabela
// associativa unica com multiplas FKs nullable, uma por tipo de acervo.
@Check(constraints = "(acervo_documental_id IS NOT NULL AND acervo_cartografico_id IS NULL) " +
                      "OR (acervo_documental_id IS NULL AND acervo_cartografico_id IS NOT NULL)")
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
    @JoinColumn(name = "acervo_cartografico_id")
    private AcervoCartografico acervoCartografico;

    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "periodo")
    private String periodo;

}
