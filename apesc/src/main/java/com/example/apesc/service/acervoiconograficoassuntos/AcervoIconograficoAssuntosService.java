package com.example.apesc.service.acervoiconograficoassuntos;

import com.example.apesc.model.AcervoIconograficoAssuntos;

import java.util.List;
import java.util.Optional;

public interface AcervoIconograficoAssuntosService {

    AcervoIconograficoAssuntos save(AcervoIconograficoAssuntos assunto);

    List<AcervoIconograficoAssuntos> findAll();

    Optional<AcervoIconograficoAssuntos> findById(Long id);

    AcervoIconograficoAssuntos update(AcervoIconograficoAssuntos assunto);

    void delete(Long id);
}
