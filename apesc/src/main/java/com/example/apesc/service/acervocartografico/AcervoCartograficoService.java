package com.example.apesc.service.acervocartografico;

import com.example.apesc.model.AcervoCartografico;

import java.util.List;
import java.util.Optional;

public interface AcervoCartograficoService {

    AcervoCartografico save(AcervoCartografico acervoCartografico);

    List<AcervoCartografico> findAllWithRelations();

    Optional<AcervoCartografico> findByIdWithRelations(Long id);

    Optional<AcervoCartografico> findById(Long id);

    void delete(Long id);

    List<AcervoCartografico> findByTipoDocumento(Long tipoDocumentoId);

    AcervoCartografico update(AcervoCartografico acervoCartografico);
}
