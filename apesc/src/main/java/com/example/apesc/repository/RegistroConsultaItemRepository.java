package com.example.apesc.repository;

import com.example.apesc.model.RegistroConsultaItem;
import com.example.apesc.model.enums.TipoConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface RegistroConsultaItemRepository extends JpaRepository<RegistroConsultaItem, Long> {

    // Mesma regra de duplicidade de sempre (pesquisador + dataPesquisa + tipoConsulta +
    // acervo + periodo + quantidade, ignorando funcionario), agora com a tabela unica:
    // uma query filtra por acervo_documental_id, a outra por acervo_cartografico_id —
    // igual antes, so que as duas leem da mesma tabela fisica.
    @Query("SELECT CASE WHEN COUNT(item) > 0 THEN true ELSE false END FROM RegistroConsultaItem item " +
           "WHERE item.registroConsulta.pesquisador.id = :pesquisadorId " +
           "AND item.registroConsulta.dataPesquisa = :dataPesquisa " +
           "AND item.registroConsulta.tipoConsulta = :tipoConsulta " +
           "AND item.acervoDocumental.id = :acervoDocumentalId " +
           "AND item.periodo = :periodo " +
           "AND item.quantidade = :quantidade " +
           "AND (:idRegistroAtual IS NULL OR item.registroConsulta.id <> :idRegistroAtual)")
    boolean existsDuplicadoDocumental(
            @Param("pesquisadorId") Long pesquisadorId,
            @Param("dataPesquisa") LocalDate dataPesquisa,
            @Param("tipoConsulta") TipoConsulta tipoConsulta,
            @Param("acervoDocumentalId") Long acervoDocumentalId,
            @Param("periodo") String periodo,
            @Param("quantidade") Integer quantidade,
            @Param("idRegistroAtual") Long idRegistroAtual
    );

    @Query("SELECT CASE WHEN COUNT(item) > 0 THEN true ELSE false END FROM RegistroConsultaItem item " +
           "WHERE item.registroConsulta.pesquisador.id = :pesquisadorId " +
           "AND item.registroConsulta.dataPesquisa = :dataPesquisa " +
           "AND item.registroConsulta.tipoConsulta = :tipoConsulta " +
           "AND item.acervoCartografico.id = :acervoCartograficoId " +
           "AND item.periodo = :periodo " +
           "AND item.quantidade = :quantidade " +
           "AND (:idRegistroAtual IS NULL OR item.registroConsulta.id <> :idRegistroAtual)")
    boolean existsDuplicadoCartografico(
            @Param("pesquisadorId") Long pesquisadorId,
            @Param("dataPesquisa") LocalDate dataPesquisa,
            @Param("tipoConsulta") TipoConsulta tipoConsulta,
            @Param("acervoCartograficoId") Long acervoCartograficoId,
            @Param("periodo") String periodo,
            @Param("quantidade") Integer quantidade,
            @Param("idRegistroAtual") Long idRegistroAtual
    );
}
