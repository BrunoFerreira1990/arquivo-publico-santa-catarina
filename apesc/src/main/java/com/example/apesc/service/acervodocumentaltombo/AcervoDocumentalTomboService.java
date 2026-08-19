package com.example.apesc.service.acervodocumentaltombo;

import com.example.apesc.model.AcervoDocumentalTombo;

import java.util.List;
import java.util.Optional;

public interface AcervoDocumentalTomboService {

    AcervoDocumentalTombo save(AcervoDocumentalTombo tombo);

    List<AcervoDocumentalTombo> findByAcervoDocumento(Long acervoDocumentalId);

    Optional<AcervoDocumentalTombo> findByIdWithRelations(Long id);

    AcervoDocumentalTombo update(AcervoDocumentalTombo tombo);

    void delete(Long id);
}
