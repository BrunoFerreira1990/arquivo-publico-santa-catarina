package com.example.apesc.repository;

import com.example.apesc.model.RegistroConsulta;
import com.example.apesc.model.enums.TipoConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface RegistroConsultaRepository extends JpaRepository<RegistroConsulta, Long> {
    boolean existsByPesquisadorId(Long pesquisadorId);

    // Considera "exatamente igual" a combinacao de todos os campos de negocio,
    // restrita a registros cadastrados (dataRegistro) dentro do mesmo dia —
    // impede duplo cadastro acidental sem bloquear repeticoes legitimas em dias diferentes.
    @Query("SELECT CASE WHEN COUNT(rc) > 0 THEN true ELSE false END FROM RegistroConsulta rc " +
           "WHERE rc.pesquisador.id = :pesquisadorId " +
           "AND rc.dataPesquisa = :dataPesquisa " +
           "AND rc.tipoConsulta = :tipoConsulta " +
           "AND rc.acervoDocumental.id = :acervoDocumentalId " +
           "AND rc.periodo = :periodo " +
           "AND rc.quantidade = :quantidade " +
           "AND rc.funcionario.id = :funcionarioId " +
           "AND rc.dataRegistro BETWEEN :inicioDoDia AND :fimDoDia")
    boolean existsDuplicadoNoMesmoDia(
            @Param("pesquisadorId") Long pesquisadorId,
            @Param("dataPesquisa") LocalDate dataPesquisa,
            @Param("tipoConsulta") TipoConsulta tipoConsulta,
            @Param("acervoDocumentalId") Long acervoDocumentalId,
            @Param("periodo") String periodo,
            @Param("quantidade") Integer quantidade,
            @Param("funcionarioId") Long funcionarioId,
            @Param("inicioDoDia") LocalDateTime inicioDoDia,
            @Param("fimDoDia") LocalDateTime fimDoDia
    );
}
