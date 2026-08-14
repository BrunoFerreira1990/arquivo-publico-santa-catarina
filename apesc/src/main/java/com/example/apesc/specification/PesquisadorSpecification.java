package com.example.apesc.specification;

import com.example.apesc.model.Pesquisador;
import com.example.apesc.util.CommonUtils;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class PesquisadorSpecification {

    public static Specification<Pesquisador> searchByFields(String nome, String cpf) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nome != null && !nome.trim().isEmpty()) {
                predicates.add(cb.like(
                        cb.lower(root.get("nome")),
                        "%" + nome.toLowerCase() + "%"
                ));
            }

            if (cpf != null && !cpf.trim().isEmpty()) {
                // Compara só os dígitos dos dois lados, pra achar tanto CPF salvo com
                // pontuação (registros antigos) quanto CPF salvo só com números
                Expression<String> cpfSemPontuacao = cb.function(
                        "replace", String.class,
                        cb.function("replace", String.class, root.get("cpf"), cb.literal("."), cb.literal("")),
                        cb.literal("-"), cb.literal("")
                );
                predicates.add(cb.equal(cpfSemPontuacao, CommonUtils.digitsOnly(cpf)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
