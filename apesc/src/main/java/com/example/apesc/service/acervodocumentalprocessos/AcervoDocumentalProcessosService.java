package com.example.apesc.service.acervodocumentalprocessos;

import com.example.apesc.model.AcervoDocumentalProcessos;
import com.example.apesc.specification.AcervoDocumentalProcessosSearchFilter;

import java.util.List;

public interface AcervoDocumentalProcessosService {

    AcervoDocumentalProcessos save(AcervoDocumentalProcessos processo);

    List<AcervoDocumentalProcessos> findAllWithRelations();

    List<AcervoDocumentalProcessos> search(AcervoDocumentalProcessosSearchFilter filtro);

    AcervoDocumentalProcessos update(AcervoDocumentalProcessos processo);

    void delete(Long id);
}
