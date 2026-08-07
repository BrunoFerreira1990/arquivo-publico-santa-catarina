package com.example.apesc.specification;

import com.example.apesc.model.AcervoDocumental;
import com.example.apesc.model.enums.NaturezaTransacao;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class AcervoDocumentalSpecification {

    public static Specification<AcervoDocumental> searchByFields(
            String tipoDocumentoNome,
            String entidadeProdutoraNome,
            String entidadeReceptoraNome,
            NaturezaTransacao naturezaTransacao) {
        
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (tipoDocumentoNome != null && !tipoDocumentoNome.trim().isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.join("tipoDocumento").get("nomeDocumento")),
                    "%" + tipoDocumentoNome.toLowerCase() + "%"
                ));
            }

            if (entidadeProdutoraNome != null && !entidadeProdutoraNome.trim().isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.join("entidadeProdutora").get("nome")),
                    "%" + entidadeProdutoraNome.toLowerCase() + "%"
                ));
            }

            if (entidadeReceptoraNome != null && !entidadeReceptoraNome.trim().isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.join("entidadeReceptora").get("nome")),
                    "%" + entidadeReceptoraNome.toLowerCase() + "%"
                ));
            }

            if (naturezaTransacao != null) {
                predicates.add(cb.equal(root.get("naturezaTransacao"), naturezaTransacao));
            }

            // Add JOIN FETCH to avoid LazyInitializationException
            root.fetch("tipoDocumento");
            root.fetch("entidadeProdutora");
            root.fetch("entidadeReceptora", jakarta.persistence.criteria.JoinType.LEFT);

            // Use AND to match only the specified fields
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
