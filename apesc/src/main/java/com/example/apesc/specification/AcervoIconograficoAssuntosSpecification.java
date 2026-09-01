package com.example.apesc.specification;

import com.example.apesc.model.AcervoIconograficoAssuntos;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class AcervoIconograficoAssuntosSpecification {

    public static Specification<AcervoIconograficoAssuntos> searchByFields(AcervoIconograficoAssuntosSearchFilter filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (isPresent(filtro.descricao())) {
                predicates.add(cb.like(cb.lower(root.get("descricao")), like(filtro.descricao())));
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
