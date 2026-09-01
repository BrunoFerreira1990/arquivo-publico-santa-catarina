package com.example.apesc.specification;

import com.example.apesc.model.AcervoIconografico;
import com.example.apesc.model.AcervoIconograficoAssuntos;
import com.example.apesc.model.AcervoIconograficoPersonalidades;
import com.example.apesc.model.TipoDocumento;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class AcervoIconograficoSpecification {

    @SuppressWarnings("unchecked")
    public static Specification<AcervoIconografico> searchByFields(AcervoIconograficoSearchFilter filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Fetch pra evitar LazyInitializationException no mapeamento pro DTO (roda
            // fora da transacao — open-in-view=false). Em tipoDocumento (ManyToOne) o
            // Fetch tambem serve de Join pra filtrar sem problema. Ja em assuntos e
            // personalidades (seus ManyToMany, coleções) NAO se pode reaproveitar o
            // mesmo Fetch como Join de filtro: o WHERE restringiria as linhas do JOIN
            // e o item voltaria com a colecao incompleta (so o elemento que bateu no
            // filtro, nao todos os vinculados). Por isso usam um Join separado, so
            // pra filtrar, deixando o Fetch livre pra carregar a colecao inteira.
            root.fetch("assuntos", JoinType.LEFT);
            root.fetch("personalidades", JoinType.LEFT);
            Fetch<AcervoIconografico, TipoDocumento> tipoDocumentoFetch = root.fetch("tipoDocumento");
            Join<AcervoIconografico, TipoDocumento> tipoDocumentoJoin = (Join<AcervoIconografico, TipoDocumento>) tipoDocumentoFetch;

            if (isPresent(filtro.tipoDocumentoNome())) {
                predicates.add(cb.like(cb.lower(tipoDocumentoJoin.get("nomeDocumento")), like(filtro.tipoDocumentoNome())));
            }

            if (isPresent(filtro.codigoIdentificacao())) {
                predicates.add(cb.like(cb.lower(root.get("codigoIdentificacao")), like(filtro.codigoIdentificacao())));
            }

            if (isPresent(filtro.titulo())) {
                predicates.add(cb.like(cb.lower(root.get("titulo")), like(filtro.titulo())));
            }

            if (isPresent(filtro.localizacao())) {
                predicates.add(cb.like(cb.lower(root.get("localizacao")), like(filtro.localizacao())));
            }

            if (isPresent(filtro.localidade())) {
                predicates.add(cb.like(cb.lower(root.get("localidade")), like(filtro.localidade())));
            }

            if (isPresent(filtro.ano())) {
                predicates.add(cb.like(cb.lower(root.get("ano")), like(filtro.ano())));
            }

            if (isPresent(filtro.assuntos())) {
                Join<AcervoIconografico, AcervoIconograficoAssuntos> assuntosFiltro = root.join("assuntos", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(assuntosFiltro.get("descricao")), like(filtro.assuntos())));
            }

            if (isPresent(filtro.personalidades())) {
                Join<AcervoIconografico, AcervoIconograficoPersonalidades> personalidadesFiltro = root.join("personalidades", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(personalidadesFiltro.get("nome")), like(filtro.personalidades())));
            }

            // DISTINCT evita duplicar a linha de AcervoIconografico no resultado quando o
            // item tem mais de 1 assunto/personalidade (join 1:N com as tabelas associativas).
            query.distinct(true);

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
