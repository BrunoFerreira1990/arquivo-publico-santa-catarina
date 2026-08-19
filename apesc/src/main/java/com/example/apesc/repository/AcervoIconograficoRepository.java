package com.example.apesc.repository;

import com.example.apesc.model.AcervoIconografico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AcervoIconograficoRepository extends JpaRepository<AcervoIconografico, Long> {

    List<AcervoIconografico> findByTipoDocumentoId(Long tipoDocumentoId);

    boolean existsByCodigoIdentificacao(String codigoIdentificacao);

    boolean existsByCodigoIdentificacaoAndIdNot(String codigoIdentificacao, Long id);

    // DISTINCT evita duplicar a linha de AcervoIconografico no resultado quando o
    // item tem mais de 1 assunto (join 1:N com a tabela associativa).
    @Query("SELECT DISTINCT a FROM AcervoIconografico a JOIN FETCH a.tipoDocumento LEFT JOIN FETCH a.assuntos")
    List<AcervoIconografico> findAllWithRelations();

    @Query("SELECT DISTINCT a FROM AcervoIconografico a JOIN FETCH a.tipoDocumento LEFT JOIN FETCH a.assuntos WHERE a.id = :id")
    Optional<AcervoIconografico> findByIdWithRelations(@Param("id") Long id);
}
