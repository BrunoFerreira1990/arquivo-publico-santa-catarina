package com.example.apesc.model;

import com.example.apesc.model.enums.TipoConsulta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    // Quando true, dispensa a exigencia de pelo menos 1 item de acervo (ver
    // RegistroConsultaValidation) — cobre o caso de um registro que existe mas nao
    // envolveu consulta a nenhum acervo. Tratado como false quando null.
    @Column(name = "sem_consulta")
    private Boolean semConsulta;

    // Um registro de consulta pode envolver varios itens de acervo (documental,
    // cartografico, ...), cada um com sua propria quantidade/periodo. Tabela
    // associativa UNICA (registro_consulta_item), com uma FK nullable por tipo de
    // acervo — cada linha preenche exatamente uma delas (ver @Check na entidade).
    @OneToMany(mappedBy = "registroConsulta", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<RegistroConsultaItem> itens = new HashSet<>();

    public void addItem(RegistroConsultaItem item) {
        itens.add(item);
        item.setRegistroConsulta(this);
    }

    public void removeItem(RegistroConsultaItem item) {
        itens.remove(item);
        item.setRegistroConsulta(null);
    }

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
