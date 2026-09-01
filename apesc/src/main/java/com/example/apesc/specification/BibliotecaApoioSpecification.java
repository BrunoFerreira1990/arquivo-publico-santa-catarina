package com.example.apesc.specification;

import com.example.apesc.model.BibliotecaApoio;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class BibliotecaApoioSpecification {

    public static Specification<BibliotecaApoio> searchByFields(BibliotecaApoioSearchFilter filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (isPresent(filtro.tipoDocumentoNome())) {
                predicates.add(cb.like(
                        cb.lower(root.join("tipoDocumento").get("nomeDocumento")),
                        like(filtro.tipoDocumentoNome())
                ));
            }

            if (isPresent(filtro.entidadeProdutoraNome())) {
                predicates.add(cb.like(
                        cb.lower(root.join("entidadeProdutora").get("nome")),
                        like(filtro.entidadeProdutoraNome())
                ));
            }

            if (isPresent(filtro.titulo())) {
                predicates.add(cb.like(cb.lower(root.get("titulo")), like(filtro.titulo())));
            }

            if (isPresent(filtro.periodo())) {
                predicates.add(cb.like(cb.lower(root.get("periodo")), like(filtro.periodo())));
            }

            if (filtro.quantidadeVolume() != null) {
                predicates.add(cb.equal(root.get("quantidadeVolume"), filtro.quantidadeVolume()));
            }

            if (isPresent(filtro.identificador())) {
                predicates.add(cb.like(cb.lower(root.get("identificador")), like(filtro.identificador())));
            }

            if (isPresent(filtro.localizacao())) {
                predicates.add(cb.like(cb.lower(root.get("localizacao")), like(filtro.localizacao())));
            }

            if (filtro.disponibilidade() != null) {
                predicates.add(cb.equal(root.get("disponibilidade"), filtro.disponibilidade()));
            }

            // Fetch pra evitar LazyInitializationException no mapeamento pro DTO (roda
            // fora da transacao — open-in-view=false). Ambos os relacionamentos sao
            // obrigatorios na entidade (optional = false).
            root.fetch("tipoDocumento");
            root.fetch("entidadeProdutora");

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
