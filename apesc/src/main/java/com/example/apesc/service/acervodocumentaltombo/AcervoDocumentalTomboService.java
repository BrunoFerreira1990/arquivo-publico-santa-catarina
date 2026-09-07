package com.example.apesc.service.acervodocumentaltombo;

import com.example.apesc.model.AcervoDocumentalTombo;
import com.example.apesc.specification.AcervoDocumentalTomboSearchFilter;

import java.util.List;

public interface AcervoDocumentalTomboService {

    AcervoDocumentalTombo save(AcervoDocumentalTombo tombo);

    List<AcervoDocumentalTombo> findByAcervoDocumento(Long acervoDocumentalId);

    List<AcervoDocumentalTombo> search(AcervoDocumentalTomboSearchFilter filtro);

    AcervoDocumentalTombo update(AcervoDocumentalTombo tombo);

    void delete(Long id);
}
