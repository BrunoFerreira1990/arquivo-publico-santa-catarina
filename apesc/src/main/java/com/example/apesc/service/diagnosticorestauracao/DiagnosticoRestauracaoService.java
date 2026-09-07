package com.example.apesc.service.diagnosticorestauracao;

import com.example.apesc.model.DiagnosticoRestauracao;

import java.util.List;

public interface DiagnosticoRestauracaoService {

    DiagnosticoRestauracao save(DiagnosticoRestauracao diagnosticoRestauracao);

    DiagnosticoRestauracao findById(Long id);

    DiagnosticoRestauracao findByNumeroDocumento(Integer numeroDocumento);

    List<DiagnosticoRestauracao> findAll();

    void delete(Long id);
    
    DiagnosticoRestauracao update(DiagnosticoRestauracao diagnosticoRestauracao);
    
}
