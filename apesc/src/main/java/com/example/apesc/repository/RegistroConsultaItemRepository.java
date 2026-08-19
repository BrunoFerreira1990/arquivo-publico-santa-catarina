package com.example.apesc.repository;

import com.example.apesc.model.RegistroConsultaItem;
import com.example.apesc.model.enums.TipoConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

// Mesma regra de duplicidade em todos os 6 metodos abaixo (pesquisador + dataPesquisa
// + tipoConsulta + o acervo do tipo especifico + periodo + quantidade, ignorando
// funcionario) — cada metodo so troca qual coluna de acervo eh comparada, ja que
// todos os 6 tipos vivem na mesma tabela fisica.
public interface RegistroConsultaItemRepository extends JpaRepository<RegistroConsultaItem, Long> {

    @Query("SELECT CASE WHEN COUNT(item) > 0 THEN true ELSE false END FROM RegistroConsultaItem item " +
           "WHERE item.registroConsulta.pesquisador.id = :pesquisadorId " +
           "AND item.registroConsulta.dataPesquisa = :dataPesquisa " +
           "AND item.registroConsulta.tipoConsulta = :tipoConsulta " +
           "AND item.acervoDocumental.id = :acervoId " +
           "AND item.periodo = :periodo " +
           "AND item.quantidade = :quantidade " +
           "AND (:idRegistroAtual IS NULL OR item.registroConsulta.id <> :idRegistroAtual)")
    boolean existsDuplicadoDocumental(
            @Param("pesquisadorId") Long pesquisadorId,
            @Param("dataPesquisa") LocalDate dataPesquisa,
            @Param("tipoConsulta") TipoConsulta tipoConsulta,
            @Param("acervoId") Long acervoId,
            @Param("periodo") String periodo,
            @Param("quantidade") Integer quantidade,
            @Param("idRegistroAtual") Long idRegistroAtual
    );

    @Query("SELECT CASE WHEN COUNT(item) > 0 THEN true ELSE false END FROM RegistroConsultaItem item " +
           "WHERE item.registroConsulta.pesquisador.id = :pesquisadorId " +
           "AND item.registroConsulta.dataPesquisa = :dataPesquisa " +
           "AND item.registroConsulta.tipoConsulta = :tipoConsulta " +
           "AND item.acervoDocumentalProcessos.id = :acervoId " +
           "AND item.periodo = :periodo " +
           "AND item.quantidade = :quantidade " +
           "AND (:idRegistroAtual IS NULL OR item.registroConsulta.id <> :idRegistroAtual)")
    boolean existsDuplicadoDocumentalProcessos(
            @Param("pesquisadorId") Long pesquisadorId,
            @Param("dataPesquisa") LocalDate dataPesquisa,
            @Param("tipoConsulta") TipoConsulta tipoConsulta,
            @Param("acervoId") Long acervoId,
            @Param("periodo") String periodo,
            @Param("quantidade") Integer quantidade,
            @Param("idRegistroAtual") Long idRegistroAtual
    );

    @Query("SELECT CASE WHEN COUNT(item) > 0 THEN true ELSE false END FROM RegistroConsultaItem item " +
           "WHERE item.registroConsulta.pesquisador.id = :pesquisadorId " +
           "AND item.registroConsulta.dataPesquisa = :dataPesquisa " +
           "AND item.registroConsulta.tipoConsulta = :tipoConsulta " +
           "AND item.acervoIconografico.id = :acervoId " +
           "AND item.periodo = :periodo " +
           "AND item.quantidade = :quantidade " +
           "AND (:idRegistroAtual IS NULL OR item.registroConsulta.id <> :idRegistroAtual)")
    boolean existsDuplicadoIconografico(
            @Param("pesquisadorId") Long pesquisadorId,
            @Param("dataPesquisa") LocalDate dataPesquisa,
            @Param("tipoConsulta") TipoConsulta tipoConsulta,
            @Param("acervoId") Long acervoId,
            @Param("periodo") String periodo,
            @Param("quantidade") Integer quantidade,
            @Param("idRegistroAtual") Long idRegistroAtual
    );

    @Query("SELECT CASE WHEN COUNT(item) > 0 THEN true ELSE false END FROM RegistroConsultaItem item " +
           "WHERE item.registroConsulta.pesquisador.id = :pesquisadorId " +
           "AND item.registroConsulta.dataPesquisa = :dataPesquisa " +
           "AND item.registroConsulta.tipoConsulta = :tipoConsulta " +
           "AND item.acervoCartografico.id = :acervoId " +
           "AND item.periodo = :periodo " +
           "AND item.quantidade = :quantidade " +
           "AND (:idRegistroAtual IS NULL OR item.registroConsulta.id <> :idRegistroAtual)")
    boolean existsDuplicadoCartografico(
            @Param("pesquisadorId") Long pesquisadorId,
            @Param("dataPesquisa") LocalDate dataPesquisa,
            @Param("tipoConsulta") TipoConsulta tipoConsulta,
            @Param("acervoId") Long acervoId,
            @Param("periodo") String periodo,
            @Param("quantidade") Integer quantidade,
            @Param("idRegistroAtual") Long idRegistroAtual
    );

    @Query("SELECT CASE WHEN COUNT(item) > 0 THEN true ELSE false END FROM RegistroConsultaItem item " +
           "WHERE item.registroConsulta.pesquisador.id = :pesquisadorId " +
           "AND item.registroConsulta.dataPesquisa = :dataPesquisa " +
           "AND item.registroConsulta.tipoConsulta = :tipoConsulta " +
           "AND item.bibliotecaLivrosPeriodicos.id = :acervoId " +
           "AND item.periodo = :periodo " +
           "AND item.quantidade = :quantidade " +
           "AND (:idRegistroAtual IS NULL OR item.registroConsulta.id <> :idRegistroAtual)")
    boolean existsDuplicadoBibliotecaLivrosPeriodicos(
            @Param("pesquisadorId") Long pesquisadorId,
            @Param("dataPesquisa") LocalDate dataPesquisa,
            @Param("tipoConsulta") TipoConsulta tipoConsulta,
            @Param("acervoId") Long acervoId,
            @Param("periodo") String periodo,
            @Param("quantidade") Integer quantidade,
            @Param("idRegistroAtual") Long idRegistroAtual
    );

    @Query("SELECT CASE WHEN COUNT(item) > 0 THEN true ELSE false END FROM RegistroConsultaItem item " +
           "WHERE item.registroConsulta.pesquisador.id = :pesquisadorId " +
           "AND item.registroConsulta.dataPesquisa = :dataPesquisa " +
           "AND item.registroConsulta.tipoConsulta = :tipoConsulta " +
           "AND item.bibliotecaApoio.id = :acervoId " +
           "AND item.periodo = :periodo " +
           "AND item.quantidade = :quantidade " +
           "AND (:idRegistroAtual IS NULL OR item.registroConsulta.id <> :idRegistroAtual)")
    boolean existsDuplicadoBibliotecaApoio(
            @Param("pesquisadorId") Long pesquisadorId,
            @Param("dataPesquisa") LocalDate dataPesquisa,
            @Param("tipoConsulta") TipoConsulta tipoConsulta,
            @Param("acervoId") Long acervoId,
            @Param("periodo") String periodo,
            @Param("quantidade") Integer quantidade,
            @Param("idRegistroAtual") Long idRegistroAtual
    );
}
