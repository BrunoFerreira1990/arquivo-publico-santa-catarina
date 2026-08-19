package com.example.apesc.service.acervodocumentalprocessos;

import com.example.apesc.model.AcervoDocumentalProcessos;

import java.util.List;
import java.util.Optional;

public interface AcervoDocumentalProcessosService {

    AcervoDocumentalProcessos save(AcervoDocumentalProcessos processo);

    List<AcervoDocumentalProcessos> findAllWithRelations();

    Optional<AcervoDocumentalProcessos> findByIdWithRelations(Long id);

    List<AcervoDocumentalProcessos> findByAcervoDocumento(Long acervoDocumentalId);

    AcervoDocumentalProcessos update(AcervoDocumentalProcessos processo);

    void delete(Long id);
}
