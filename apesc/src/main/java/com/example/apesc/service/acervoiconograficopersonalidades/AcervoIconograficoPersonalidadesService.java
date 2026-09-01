package com.example.apesc.service.acervoiconograficopersonalidades;

import com.example.apesc.model.AcervoIconograficoPersonalidades;
import com.example.apesc.specification.AcervoIconograficoPersonalidadesSearchFilter;

import java.util.List;

public interface AcervoIconograficoPersonalidadesService {

    AcervoIconograficoPersonalidades save(AcervoIconograficoPersonalidades personalidade);

    List<AcervoIconograficoPersonalidades> findAll();

    List<AcervoIconograficoPersonalidades> search(AcervoIconograficoPersonalidadesSearchFilter filtro);

    AcervoIconograficoPersonalidades update(AcervoIconograficoPersonalidades personalidade);

    void delete(Long id);
}
