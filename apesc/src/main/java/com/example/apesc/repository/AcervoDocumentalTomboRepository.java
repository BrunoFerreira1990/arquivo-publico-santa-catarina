package com.example.apesc.repository;

import com.example.apesc.model.AcervoDocumentalTombo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AcervoDocumentalTomboRepository extends JpaRepository<AcervoDocumentalTombo, Long> {

    List<AcervoDocumentalTombo> findByAcervoDocumentalId(Long acervoDocumentalId);

    boolean existsByAcervoDocumentalIdAndNumeroTombo(Long acervoDocumentalId, Integer numeroTombo);

    boolean existsByAcervoDocumentalIdAndNumeroTomboAndIdNot(Long acervoDocumentalId, Integer numeroTombo, Long id);

    @Query("SELECT t FROM AcervoDocumentalTombo t JOIN FETCH t.acervoDocumental WHERE t.id = :id")
    Optional<AcervoDocumentalTombo> findByIdWithRelations(@Param("id") Long id);
}
