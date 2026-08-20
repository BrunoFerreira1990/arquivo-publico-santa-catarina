package com.example.apesc.specification;

import com.example.apesc.model.*;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class RegistroConsultaSpecification {

    public static Specification<RegistroConsulta> searchByFields(RegistroConsultaSearchFilter filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.dataPesquisa() != null) {
                predicates.add(cb.equal(root.get("dataPesquisa"), filtro.dataPesquisa()));
            }

            if (filtro.dataPesquisaInicio() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataPesquisa"), filtro.dataPesquisaInicio()));
            }

            if (filtro.dataPesquisaFim() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataPesquisa"), filtro.dataPesquisaFim()));
            }

            // Busca pelo nome do pesquisador/funcionario eh feita via JOIN, na mesma
            // query — nao eh um "get por id" separado.
            if (isPresent(filtro.nomePesquisador())) {
                predicates.add(cb.like(cb.lower(root.join("pesquisador").get("nome")), like(filtro.nomePesquisador())));
            }

            if (isPresent(filtro.nomeFuncionario())) {
                predicates.add(cb.like(cb.lower(root.join("funcionario").get("nome")), like(filtro.nomeFuncionario())));
            }

            if (filtro.tipoConsulta() != null) {
                predicates.add(cb.equal(root.get("tipoConsulta"), filtro.tipoConsulta()));
            }

            if (filtro.semConsulta() != null) {
                predicates.add(cb.equal(root.get("semConsulta"), filtro.semConsulta()));
            }

            // Cada tipo de acervo abre o SEU PROPRIO join pra "itens", separado dos
            // outros tipos. Se todos compartilhassem o mesmo join, filtrar por 2 tipos
            // diferentes ao mesmo tempo (ex.: titulo do cartografico E do
            // iconografico) exigiria as duas condicoes batendo na MESMA linha de
            // item — impossivel, ja que cada item so tem 1 tipo preenchido. Com joins
            // separados, cada filtro checa a existencia de ALGUM item daquele tipo
            // que bata, independente dos outros — permitindo, por exemplo, "registro
            // que tem um item documental do orgao X E um item cartografico do ano Y".
            if (temFiltroDocumental(filtro)) {
                Join<RegistroConsultaItem, AcervoDocumental> documental =
                        root.join("itens", JoinType.LEFT).join("acervoDocumental", JoinType.LEFT);

                if (isPresent(filtro.tipoDocumentoNomeDocumental())) {
                    predicates.add(cb.like(cb.lower(documental.join("tipoDocumento").get("nomeDocumento")), like(filtro.tipoDocumentoNomeDocumental())));
                }
                if (isPresent(filtro.entidadeProdutoraNomeDocumental())) {
                    predicates.add(cb.like(cb.lower(documental.join("entidadeProdutora").get("nome")), like(filtro.entidadeProdutoraNomeDocumental())));
                }
                if (isPresent(filtro.entidadeReceptoraNomeDocumental())) {
                    predicates.add(cb.like(cb.lower(documental.join("entidadeReceptora", JoinType.LEFT).get("nome")), like(filtro.entidadeReceptoraNomeDocumental())));
                }
            }

            if (isPresent(filtro.nomeProcessoDocumentalProcessos())) {
                Join<RegistroConsultaItem, AcervoDocumentalProcessos> processos =
                        root.join("itens", JoinType.LEFT).join("acervoDocumentalProcessos", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(processos.get("nomeProcesso")), like(filtro.nomeProcessoDocumentalProcessos())));
            }

            if (temFiltroIconografico(filtro)) {
                Join<RegistroConsultaItem, AcervoIconografico> iconografico =
                        root.join("itens", JoinType.LEFT).join("acervoIconografico", JoinType.LEFT);

                if (isPresent(filtro.tituloIconografico())) {
                    predicates.add(cb.like(cb.lower(iconografico.get("titulo")), like(filtro.tituloIconografico())));
                }
                if (isPresent(filtro.codigoIconografico())) {
                    predicates.add(cb.like(cb.lower(iconografico.get("codigoIdentificacao")), like(filtro.codigoIconografico())));
                }
                if (isPresent(filtro.tipoDocumentoNomeIconografico())) {
                    predicates.add(cb.like(cb.lower(iconografico.join("tipoDocumento").get("nomeDocumento")), like(filtro.tipoDocumentoNomeIconografico())));
                }
            }

            if (temFiltroCartografico(filtro)) {
                Join<RegistroConsultaItem, AcervoCartografico> cartografico =
                        root.join("itens", JoinType.LEFT).join("acervoCartografico", JoinType.LEFT);

                if (isPresent(filtro.tituloCartografico())) {
                    predicates.add(cb.like(cb.lower(cartografico.get("titulo")), like(filtro.tituloCartografico())));
                }
                if (isPresent(filtro.codigoCartografico())) {
                    predicates.add(cb.like(cb.lower(cartografico.get("codigoIdentificacao")), like(filtro.codigoCartografico())));
                }
                if (isPresent(filtro.tipoDocumentoNomeCartografico())) {
                    predicates.add(cb.like(cb.lower(cartografico.join("tipoDocumento").get("nomeDocumento")), like(filtro.tipoDocumentoNomeCartografico())));
                }
                if (isPresent(filtro.entidadeProdutoraNomeCartografico())) {
                    predicates.add(cb.like(cb.lower(cartografico.join("entidadeProdutora", JoinType.LEFT).get("nome")), like(filtro.entidadeProdutoraNomeCartografico())));
                }
            }

            if (temFiltroBibliotecaLivros(filtro)) {
                Join<RegistroConsultaItem, BibliotecaLivrosPeriodicos> livros =
                        root.join("itens", JoinType.LEFT).join("bibliotecaLivrosPeriodicos", JoinType.LEFT);

                if (isPresent(filtro.tituloBibliotecaLivros())) {
                    predicates.add(cb.like(cb.lower(livros.get("titulo")), like(filtro.tituloBibliotecaLivros())));
                }
                if (isPresent(filtro.autoresBibliotecaLivros())) {
                    predicates.add(cb.like(cb.lower(livros.get("autores")), like(filtro.autoresBibliotecaLivros())));
                }
                if (isPresent(filtro.tipoDocumentoNomeBibliotecaLivros())) {
                    predicates.add(cb.like(cb.lower(livros.join("tipoDocumento").get("nomeDocumento")), like(filtro.tipoDocumentoNomeBibliotecaLivros())));
                }
            }

            if (temFiltroBibliotecaApoio(filtro)) {
                Join<RegistroConsultaItem, BibliotecaApoio> apoio =
                        root.join("itens", JoinType.LEFT).join("bibliotecaApoio", JoinType.LEFT);

                if (isPresent(filtro.tituloBibliotecaApoio())) {
                    predicates.add(cb.like(cb.lower(apoio.get("titulo")), like(filtro.tituloBibliotecaApoio())));
                }
                if (isPresent(filtro.identificadorBibliotecaApoio())) {
                    predicates.add(cb.like(cb.lower(apoio.get("identificador")), like(filtro.identificadorBibliotecaApoio())));
                }
                if (isPresent(filtro.tipoDocumentoNomeBibliotecaApoio())) {
                    predicates.add(cb.like(cb.lower(apoio.join("tipoDocumento").get("nomeDocumento")), like(filtro.tipoDocumentoNomeBibliotecaApoio())));
                }
                if (isPresent(filtro.entidadeProdutoraNomeBibliotecaApoio())) {
                    predicates.add(cb.like(cb.lower(apoio.join("entidadeProdutora").get("nome")), like(filtro.entidadeProdutoraNomeBibliotecaApoio())));
                }
            }

            // Fetch dos itens pra evitar LazyInitializationException no mapeamento pro
            // DTO (roda fora da transacao — open-in-view=false). DISTINCT porque fetch
            // de colecao 1:N duplica a linha do pai por item encontrado.
            root.fetch("itens", JoinType.LEFT);
            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean temFiltroDocumental(RegistroConsultaSearchFilter f) {
        return isPresent(f.tipoDocumentoNomeDocumental())
                || isPresent(f.entidadeProdutoraNomeDocumental())
                || isPresent(f.entidadeReceptoraNomeDocumental());
    }

    private static boolean temFiltroIconografico(RegistroConsultaSearchFilter f) {
        return isPresent(f.tituloIconografico())
                || isPresent(f.codigoIconografico())
                || isPresent(f.tipoDocumentoNomeIconografico());
    }

    private static boolean temFiltroCartografico(RegistroConsultaSearchFilter f) {
        return isPresent(f.tituloCartografico())
                || isPresent(f.codigoCartografico())
                || isPresent(f.tipoDocumentoNomeCartografico())
                || isPresent(f.entidadeProdutoraNomeCartografico());
    }

    private static boolean temFiltroBibliotecaLivros(RegistroConsultaSearchFilter f) {
        return isPresent(f.tituloBibliotecaLivros())
                || isPresent(f.autoresBibliotecaLivros())
                || isPresent(f.tipoDocumentoNomeBibliotecaLivros());
    }

    private static boolean temFiltroBibliotecaApoio(RegistroConsultaSearchFilter f) {
        return isPresent(f.tituloBibliotecaApoio())
                || isPresent(f.identificadorBibliotecaApoio())
                || isPresent(f.tipoDocumentoNomeBibliotecaApoio())
                || isPresent(f.entidadeProdutoraNomeBibliotecaApoio());
    }

    private static boolean isPresent(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static String like(String s) {
        return "%" + s.toLowerCase() + "%";
    }
}
