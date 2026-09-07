package com.example.apesc.service.procedimentorestauracao;

import com.example.apesc.model.ProcedimentoRestauracao;

import java.util.List;

public interface ProcedimentoRestauracaoService {

    ProcedimentoRestauracao save(ProcedimentoRestauracao procedimentoRestauracao);

    ProcedimentoRestauracao findById(Long id);

    ProcedimentoRestauracao findByNumeroDocumento(Integer numeroDocumento);

    List<ProcedimentoRestauracao> findAll();

    void delete(Long id);
    
    ProcedimentoRestauracao update(ProcedimentoRestauracao procedimentoRestauracao);
    
}
