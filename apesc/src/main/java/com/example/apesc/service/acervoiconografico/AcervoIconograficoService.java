package com.example.apesc.service.acervoiconografico;

import com.example.apesc.model.AcervoIconografico;

import java.util.List;
import java.util.Optional;

public interface AcervoIconograficoService {

    AcervoIconografico save(AcervoIconografico acervo);

    List<AcervoIconografico> findAllWithRelations();

    Optional<AcervoIconografico> findByIdWithRelations(Long id);

    List<AcervoIconografico> findByTipoDocumento(Long tipoDocumentoId);

    AcervoIconografico update(AcervoIconografico acervo);

    void delete(Long id);
}
