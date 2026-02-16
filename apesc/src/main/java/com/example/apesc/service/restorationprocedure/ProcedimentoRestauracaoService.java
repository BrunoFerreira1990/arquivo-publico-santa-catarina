package com.example.apesc.service.restorationprocedure;

import com.example.apesc.model.ProcedimentoRestauracao;

public interface ProcedimentoRestauracaoService {

    ProcedimentoRestauracao save(ProcedimentoRestauracao procedimentoRestauracao);

    ProcedimentoRestauracao findById(Long id);
    void delete(Long id);
    
    ProcedimentoRestauracao update(ProcedimentoRestauracao procedimentoRestauracao);
    
}
