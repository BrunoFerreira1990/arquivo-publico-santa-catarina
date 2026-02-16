package com.example.apesc.repository;

import com.example.apesc.model.AcervoDocumental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AcervoDocumentalRepository extends JpaRepository<AcervoDocumental, Long> {

    List<AcervoDocumental> findByTipoDocumentoId(Long tipoDocumentoId);
}
