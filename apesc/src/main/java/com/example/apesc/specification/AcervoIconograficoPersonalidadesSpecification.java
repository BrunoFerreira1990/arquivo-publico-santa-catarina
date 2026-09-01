package com.example.apesc.specification;

import com.example.apesc.model.AcervoIconograficoPersonalidades;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class AcervoIconograficoPersonalidadesSpecification {

    public static Specification<AcervoIconograficoPersonalidades> searchByFields(AcervoIconograficoPersonalidadesSearchFilter filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (isPresent(filtro.nome())) {
                predicates.add(cb.like(cb.lower(root.get("nome")), like(filtro.nome())));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean isPresent(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static String like(String s) {
        return "%" + s.toLowerCase() + "%";
    }
}
