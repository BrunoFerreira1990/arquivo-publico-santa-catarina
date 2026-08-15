package com.example.apesc.repository;

import com.example.apesc.model.RegistroConsulta;
import com.example.apesc.model.enums.TipoConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface RegistroConsultaRepository extends JpaRepository<RegistroConsulta, Long> {
    boolean existsByPesquisadorId(Long pesquisadorId);

    // Considera "exatamente igual" a combinacao dos campos de negocio que descrevem
    // a consulta em si (pesquisador, dataPesquisa, tipoConsulta, acervoDocumental,
    // periodo, quantidade) — de proposito SEM comparar o funcionario, pra pegar o
    // caso de uma pessoa diferente registrar sem querer a mesma consulta ja existente.
    // Sem restricao de dia (dataPesquisa ja esta entre os campos comparados).
    // idAtual exclui o proprio registro da busca: nulo no create (nada a excluir),
    // preenchido no update (permite salvar as mesmas informacoes de volta nele
    // mesmo, so bloqueia se OUTRO registro, com ID diferente, for identico).
    @Query("SELECT CASE WHEN COUNT(rc) > 0 THEN true ELSE false END FROM RegistroConsulta rc " +
           "WHERE rc.pesquisador.id = :pesquisadorId " +
           "AND rc.dataPesquisa = :dataPesquisa " +
           "AND rc.tipoConsulta = :tipoConsulta " +
           "AND rc.acervoDocumental.id = :acervoDocumentalId " +
           "AND rc.periodo = :periodo " +
           "AND rc.quantidade = :quantidade " +
           "AND (:idAtual IS NULL OR rc.id <> :idAtual)")
    boolean existsDuplicado(
            @Param("pesquisadorId") Long pesquisadorId,
            @Param("dataPesquisa") LocalDate dataPesquisa,
            @Param("tipoConsulta") TipoConsulta tipoConsulta,
            @Param("acervoDocumentalId") Long acervoDocumentalId,
            @Param("periodo") String periodo,
            @Param("quantidade") Integer quantidade,
            @Param("idAtual") Long idAtual
    );
}
