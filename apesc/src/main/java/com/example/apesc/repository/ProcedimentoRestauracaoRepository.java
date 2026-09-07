package com.example.apesc.repository;

import com.example.apesc.model.ProcedimentoRestauracao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcedimentoRestauracaoRepository extends JpaRepository<ProcedimentoRestauracao, Long> {

    boolean existsByDiagnosticoRestauracaoId(Integer diagnosticoRestauracaoId);

    boolean existsByDiagnosticoRestauracaoIdAndIdNot(Integer diagnosticoRestauracaoId, Integer id);

    // numeroDocumento nao existe em ProcedimentoRestauracao — mora no DiagnosticoRestauracao
    // (relacao 1:1), entao a busca navega pela associacao.
    Optional<ProcedimentoRestauracao> findByDiagnosticoRestauracao_NumeroDocumento(Integer numeroDocumento);
}
