package com.example.apesc.specification;

import com.example.apesc.model.RegistroConsulta;
import com.example.apesc.model.enums.TipoConsulta;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RegistroConsultaSpecification {

    public static Specification<RegistroConsulta> searchByFields(
            LocalDate dataPesquisa,
            String nomePesquisador,
            String nomeFuncionario,
            TipoConsulta tipoConsulta,
            Boolean semConsulta) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dataPesquisa != null) {
                predicates.add(cb.equal(root.get("dataPesquisa"), dataPesquisa));
            }

            // Busca pelo nome do pesquisador eh feita via JOIN com Pesquisador, na
            // mesma query — nao eh um "get por id" separado, eh um LIKE direto em
            // pesquisador.nome dentro do SELECT de registro_consulta.
            if (nomePesquisador != null && !nomePesquisador.trim().isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.join("pesquisador").get("nome")),
                    "%" + nomePesquisador.toLowerCase() + "%"
                ));
            }

            if (nomeFuncionario != null && !nomeFuncionario.trim().isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.join("funcionario").get("nome")),
                    "%" + nomeFuncionario.toLowerCase() + "%"
                ));
            }

            if (tipoConsulta != null) {
                predicates.add(cb.equal(root.get("tipoConsulta"), tipoConsulta));
            }

            if (semConsulta != null) {
                predicates.add(cb.equal(root.get("semConsulta"), semConsulta));
            }

            // Fetch dos itens pra evitar LazyInitializationException no mapeamento pro
            // DTO (que roda fora da transacao — open-in-view=false). DISTINCT porque
            // fetch de colecao 1:N duplica a linha do pai por item encontrado.
            root.fetch("itens", JoinType.LEFT);
            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
