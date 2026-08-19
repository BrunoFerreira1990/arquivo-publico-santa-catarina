package com.example.apesc.repository;

import com.example.apesc.model.AcervoDocumentalProcessos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AcervoDocumentalProcessosRepository extends JpaRepository<AcervoDocumentalProcessos, Long> {

    List<AcervoDocumentalProcessos> findByAcervoDocumentalId(Long acervoDocumentalId);

    boolean existsByAcervoDocumentalIdAndCaixaIdentificacao(Long acervoDocumentalId, String caixaIdentificacao);

    boolean existsByAcervoDocumentalIdAndCaixaIdentificacaoAndIdNot(Long acervoDocumentalId, String caixaIdentificacao, Long id);

    @Query("SELECT p FROM AcervoDocumentalProcessos p JOIN FETCH p.acervoDocumental")
    List<AcervoDocumentalProcessos> findAllWithRelations();

    @Query("SELECT p FROM AcervoDocumentalProcessos p JOIN FETCH p.acervoDocumental WHERE p.id = :id")
    Optional<AcervoDocumentalProcessos> findByIdWithRelations(@Param("id") Long id);
}
