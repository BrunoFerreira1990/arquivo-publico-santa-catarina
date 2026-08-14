package com.example.apesc.repository;

import com.example.apesc.model.Pesquisador;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PesquisadorRepository extends JpaRepository<Pesquisador, Long>, JpaSpecificationExecutor<Pesquisador> {

    // Compara só os dígitos dos dois lados, pra achar tanto CPF salvo com pontuação
    // (registros antigos) quanto CPF salvo só com números (a partir da normalização em PesquisadorValidation)
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Pesquisador p WHERE REPLACE(REPLACE(p.cpf, '.', ''), '-', '') = :cpf")
    boolean existsByCpf(@Param("cpf") String cpf);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Pesquisador p WHERE REPLACE(REPLACE(p.cpf, '.', ''), '-', '') = :cpf AND p.id <> :id")
    boolean existsByCpfAndIdNot(@Param("cpf") String cpf, @Param("id") Long id);
}
