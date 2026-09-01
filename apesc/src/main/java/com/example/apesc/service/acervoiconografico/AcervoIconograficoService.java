package com.example.apesc.service.acervoiconografico;

import com.example.apesc.model.AcervoIconografico;
import com.example.apesc.specification.AcervoIconograficoSearchFilter;

import java.util.List;

public interface AcervoIconograficoService {

    AcervoIconografico save(AcervoIconografico acervo);

    List<AcervoIconografico> findAllWithRelations();

    List<AcervoIconografico> search(AcervoIconograficoSearchFilter filtro);

    List<AcervoIconografico> findByTipoDocumento(Long tipoDocumentoId);

    AcervoIconografico update(AcervoIconografico acervo);

    void delete(Long id);
}
