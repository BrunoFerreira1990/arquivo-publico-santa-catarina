package com.example.apesc.service.documentaryarchives;

import com.example.apesc.model.AcervoDocumental;

import java.util.List;
import java.util.Optional;

public interface AcervoDocumentalService    {

    AcervoDocumental save(AcervoDocumental acervoDocumental);

    List<AcervoDocumental> findAll();

    Optional<AcervoDocumental> findById(Long id);

    void delete(Long id);

    List<AcervoDocumental> findByTipoDocumento(Long tipoDocumentoId);

    AcervoDocumental update(AcervoDocumental acervoDocumental);
}
