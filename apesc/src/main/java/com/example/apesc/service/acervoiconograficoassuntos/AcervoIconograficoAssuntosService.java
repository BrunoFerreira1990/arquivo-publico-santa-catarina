package com.example.apesc.service.acervoiconograficoassuntos;

import com.example.apesc.model.AcervoIconograficoAssuntos;
import com.example.apesc.specification.AcervoIconograficoAssuntosSearchFilter;

import java.util.List;

public interface AcervoIconograficoAssuntosService {

    AcervoIconograficoAssuntos save(AcervoIconograficoAssuntos assunto);

    List<AcervoIconograficoAssuntos> findAll();

    List<AcervoIconograficoAssuntos> search(AcervoIconograficoAssuntosSearchFilter filtro);

    AcervoIconograficoAssuntos update(AcervoIconograficoAssuntos assunto);

    void delete(Long id);
}
