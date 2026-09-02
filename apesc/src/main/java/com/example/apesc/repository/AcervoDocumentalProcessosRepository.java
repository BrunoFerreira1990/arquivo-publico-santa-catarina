package com.example.apesc.repository;

import com.example.apesc.model.AcervoDocumentalProcessos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AcervoDocumentalProcessosRepository extends JpaRepository<AcervoDocumentalProcessos, Long>, JpaSpecificationExecutor<AcervoDocumentalProcessos> {

    boolean existsByAcervoDocumentalIdAndCaixaIdentificacao(Long acervoDocumentalId, String caixaIdentificacao);

    boolean existsByAcervoDocumentalIdAndCaixaIdentificacaoAndIdNot(Long acervoDocumentalId, String caixaIdentificacao, Long id);

    @Query("SELECT p FROM AcervoDocumentalProcessos p JOIN FETCH p.acervoDocumental")
    List<AcervoDocumentalProcessos> findAllWithRelations();
}
