package com.example.apesc.service.restorationdiagnosis;

import com.example.apesc.model.DiagnosticoRestauracao;

public interface DiagnosticoRestauracaoService {
    
    DiagnosticoRestauracao save(DiagnosticoRestauracao diagnosticoRestauracao);

    DiagnosticoRestauracao findById(Long id);
    
    void delete(Long id);
    
    DiagnosticoRestauracao update(DiagnosticoRestauracao diagnosticoRestauracao);
    
}
