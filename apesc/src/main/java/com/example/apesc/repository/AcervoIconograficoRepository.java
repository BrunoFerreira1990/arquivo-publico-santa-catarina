package com.example.apesc.repository;

import com.example.apesc.model.AcervoIconografico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AcervoIconograficoRepository extends JpaRepository<AcervoIconografico, Long>, JpaSpecificationExecutor<AcervoIconografico> {

    List<AcervoIconografico> findByTipoDocumentoId(Long tipoDocumentoId);

    boolean existsByCodigoIdentificacao(String codigoIdentificacao);

    boolean existsByCodigoIdentificacaoAndIdNot(String codigoIdentificacao, Long id);

    // DISTINCT evita duplicar a linha de AcervoIconografico no resultado quando o
    // item tem mais de 1 assunto/personalidade (join 1:N com as tabelas associativas).
    @Query("SELECT DISTINCT a FROM AcervoIconografico a JOIN FETCH a.tipoDocumento LEFT JOIN FETCH a.assuntos LEFT JOIN FETCH a.personalidades")
    List<AcervoIconografico> findAllWithRelations();
}
