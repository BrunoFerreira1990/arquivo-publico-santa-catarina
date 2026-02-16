package com.example.apesc.service.restorationprocedure;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import com.example.apesc.model.ProcedimentoRestauracao;
import com.example.apesc.repository.ProcedimentoRestauracaoRepository;

@Service
@AllArgsConstructor
public class ProcedimentoRestauracaoServiceImpl implements ProcedimentoRestauracaoService {
    
    private final ProcedimentoRestauracaoRepository procedimentoRestauracaoRepository;
    
    public ProcedimentoRestauracao save(ProcedimentoRestauracao procedimentoRestauracao) {
        return procedimentoRestauracaoRepository.save(procedimentoRestauracao);
    }
    
    public ProcedimentoRestauracao findById(Long id) {
        return procedimentoRestauracaoRepository.findById(id).orElse(null);
    }
    
    public void delete(Long id) {
        procedimentoRestauracaoRepository.deleteById(id);
    }
    
    public ProcedimentoRestauracao update(ProcedimentoRestauracao procedimentoRestauracao) {
        return procedimentoRestauracaoRepository.save(procedimentoRestauracao);
    }
    
}
