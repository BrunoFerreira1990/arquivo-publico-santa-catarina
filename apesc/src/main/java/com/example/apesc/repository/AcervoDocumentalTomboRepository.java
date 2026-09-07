package com.example.apesc.repository;

import com.example.apesc.model.AcervoDocumentalTombo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AcervoDocumentalTomboRepository extends JpaRepository<AcervoDocumentalTombo, Long>, JpaSpecificationExecutor<AcervoDocumentalTombo> {

    List<AcervoDocumentalTombo> findByAcervoDocumentalId(Long acervoDocumentalId);

    boolean existsByNumeroTombo(Integer numeroTombo);

    boolean existsByNumeroTomboAndIdNot(Integer numeroTombo, Long id);
}
