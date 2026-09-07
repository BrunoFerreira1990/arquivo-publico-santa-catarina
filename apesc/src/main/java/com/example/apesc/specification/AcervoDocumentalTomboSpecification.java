package com.example.apesc.specification;

import com.example.apesc.model.AcervoDocumental;
import com.example.apesc.model.AcervoDocumentalTombo;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class AcervoDocumentalTomboSpecification {

    public static Specification<AcervoDocumentalTombo> searchByFields(AcervoDocumentalTomboSearchFilter filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.numeroTombo() != null) {
                predicates.add(cb.equal(root.get("numeroTombo"), filtro.numeroTombo()));
            }

            if (isPresent(filtro.periodo())) {
                predicates.add(cb.like(cb.lower(root.get("periodo")), like(filtro.periodo())));
            }

            if (filtro.semConsulta() != null) {
                predicates.add(cb.equal(root.get("semConsulta"), filtro.semConsulta()));
            }

            // Filtro pelo acervo documental vinculado e feito por JOIN nos NOMES que
            // constam nele (tipo de documento / entidade produtora / entidade
            // receptora), no mesmo padrao de AcervoDocumentalProcessosSpecification.
            if (temFiltroAcervoDocumental(filtro)) {
                Join<AcervoDocumentalTombo, AcervoDocumental> acervo = root.join("acervoDocumental");

                if (isPresent(filtro.tipoDocumentoNome())) {
                    predicates.add(cb.like(
                            cb.lower(acervo.join("tipoDocumento").get("nomeDocumento")),
                            like(filtro.tipoDocumentoNome())
                    ));
                }
                if (isPresent(filtro.entidadeProdutoraNome())) {
                    predicates.add(cb.like(
                            cb.lower(acervo.join("entidadeProdutora").get("nome")),
                            like(filtro.entidadeProdutoraNome())
                    ));
                }
                if (isPresent(filtro.entidadeReceptoraNome())) {
                    predicates.add(cb.like(
                            cb.lower(acervo.join("entidadeReceptora", JoinType.LEFT).get("nome")),
                            like(filtro.entidadeReceptoraNome())
                    ));
                }
            }

            // Fetch pra evitar LazyInitializationException no mapeamento pro DTO (roda
            // fora da transacao — open-in-view=false). O relacionamento e obrigatorio
            // na entidade (optional = false).
            root.fetch("acervoDocumental");

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean temFiltroAcervoDocumental(AcervoDocumentalTomboSearchFilter f) {
        return isPresent(f.tipoDocumentoNome())
                || isPresent(f.entidadeProdutoraNome())
                || isPresent(f.entidadeReceptoraNome());
    }

    private static boolean isPresent(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static String like(String s) {
        return "%" + s.toLowerCase() + "%";
    }
}
