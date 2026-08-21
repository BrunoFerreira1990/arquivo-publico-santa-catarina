package com.example.apesc.specification;

import com.example.apesc.model.BibliotecaLivrosPeriodicos;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class BibliotecaLivrosPeriodicosSpecification {

    public static Specification<BibliotecaLivrosPeriodicos> searchByFields(BibliotecaLivrosPeriodicosSearchFilter filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (isPresent(filtro.tipoDocumentoNome())) {
                predicates.add(cb.like(
                        cb.lower(root.join("tipoDocumento").get("nomeDocumento")),
                        like(filtro.tipoDocumentoNome())
                ));
            }

            if (isPresent(filtro.titulo())) {
                predicates.add(cb.like(cb.lower(root.get("titulo")), like(filtro.titulo())));
            }

            if (isPresent(filtro.subtitulo())) {
                predicates.add(cb.like(cb.lower(root.get("subtitulo")), like(filtro.subtitulo())));
            }

            if (isPresent(filtro.autores())) {
                predicates.add(cb.like(cb.lower(root.get("autores")), like(filtro.autores())));
            }

            if (isPresent(filtro.editora())) {
                predicates.add(cb.like(cb.lower(root.get("editora")), like(filtro.editora())));
            }

            if (isPresent(filtro.ano())) {
                predicates.add(cb.equal(root.get("ano"), filtro.ano()));
            }

            if (isPresent(filtro.classificacao())) {
                predicates.add(cb.like(cb.lower(root.get("classificacao")), like(filtro.classificacao())));
            }

            // Fetch pra evitar LazyInitializationException no mapeamento pro DTO (roda
            // fora da transacao — open-in-view=false).
            root.fetch("tipoDocumento");

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
