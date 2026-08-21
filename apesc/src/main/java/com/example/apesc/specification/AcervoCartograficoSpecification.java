package com.example.apesc.specification;

import com.example.apesc.model.AcervoCartografico;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class AcervoCartograficoSpecification {

    public static Specification<AcervoCartografico> searchByFields(AcervoCartograficoSearchFilter filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (isPresent(filtro.tipoDocumentoNome())) {
                predicates.add(cb.like(
                        cb.lower(root.join("tipoDocumento").get("nomeDocumento")),
                        like(filtro.tipoDocumentoNome())
                ));
            }

            if (isPresent(filtro.codigoIdentificacao())) {
                predicates.add(cb.like(
                        cb.lower(root.get("codigoIdentificacao")),
                        like(filtro.codigoIdentificacao())
                ));
            }

            if (isPresent(filtro.localidade())) {
                predicates.add(cb.like(
                        cb.lower(root.get("localidade")),
                        like(filtro.localidade())
                ));
            }

            if (isPresent(filtro.ano())) {
                predicates.add(cb.equal(root.get("ano"), filtro.ano()));
            }

            if (filtro.disponibilidade() != null) {
                predicates.add(cb.equal(root.get("disponibilidade"), filtro.disponibilidade()));
            }

            if (isPresent(filtro.entidadeProdutoraNome())) {
                predicates.add(cb.like(
                        cb.lower(root.join("entidadeProdutora", JoinType.LEFT).get("nome")),
                        like(filtro.entidadeProdutoraNome())
                ));
            }

            // Fetch pra evitar LazyInitializationException no mapeamento pro DTO (roda
            // fora da transacao — open-in-view=false). entidadeProdutora eh LEFT porque
            // e opcional na entidade (ver AcervoCartograficoRepository.findAllWithRelations).
            root.fetch("tipoDocumento");
            root.fetch("entidadeProdutora", JoinType.LEFT);

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
