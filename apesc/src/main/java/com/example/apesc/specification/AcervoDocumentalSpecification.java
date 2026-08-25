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

        // Remove espacos em branco no inicio/fim de cada parametro digitado na busca.
        final String tipoDocumentoNomeFiltro = trim(tipoDocumentoNome);
        final String entidadeProdutoraNomeFiltro = trim(entidadeProdutoraNome);
        final String entidadeReceptoraNomeFiltro = trim(entidadeReceptoraNome);

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (tipoDocumentoNomeFiltro != null && !tipoDocumentoNomeFiltro.isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.join("tipoDocumento").get("nomeDocumento")),
                    "%" + tipoDocumentoNomeFiltro.toLowerCase() + "%"
                ));
            }

            if (entidadeProdutoraNomeFiltro != null && !entidadeProdutoraNomeFiltro.isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.join("entidadeProdutora").get("nome")),
                    "%" + entidadeProdutoraNomeFiltro.toLowerCase() + "%"
                ));
            }

            if (entidadeReceptoraNomeFiltro != null && !entidadeReceptoraNomeFiltro.isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.join("entidadeReceptora").get("nome")),
                    "%" + entidadeReceptoraNomeFiltro.toLowerCase() + "%"
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

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }
}
