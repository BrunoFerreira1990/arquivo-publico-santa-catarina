package com.example.apesc.repository;

import com.example.apesc.model.AcervoCartografico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AcervoCartograficoRepository extends JpaRepository<AcervoCartografico, Long> {

    List<AcervoCartografico> findByTipoDocumentoId(Long tipoDocumentoId);

    boolean existsByCodigoIdentificacao(String codigoIdentificacao);

    boolean existsByCodigoIdentificacaoAndIdNot(String codigoIdentificacao, Long id);

    @Query("SELECT a FROM AcervoCartografico a JOIN FETCH a.tipoDocumento")
    List<AcervoCartografico> findAllWithRelations();

    @Query("SELECT a FROM AcervoCartografico a JOIN FETCH a.tipoDocumento WHERE a.id = :id")
    Optional<AcervoCartografico> findByIdWithRelations(@Param("id") Long id);
}
