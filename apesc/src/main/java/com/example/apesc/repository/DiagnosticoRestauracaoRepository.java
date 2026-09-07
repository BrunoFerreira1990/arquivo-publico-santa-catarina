package com.example.apesc.repository;

import com.example.apesc.model.DiagnosticoRestauracao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiagnosticoRestauracaoRepository extends JpaRepository<DiagnosticoRestauracao, Long> {

    boolean existsByNumeroDocumento(Integer numeroDocumento);

    boolean existsByNumeroDocumentoAndIdNot(Integer numeroDocumento, Integer id);

    Optional<DiagnosticoRestauracao> findByNumeroDocumento(Integer numeroDocumento);
}
