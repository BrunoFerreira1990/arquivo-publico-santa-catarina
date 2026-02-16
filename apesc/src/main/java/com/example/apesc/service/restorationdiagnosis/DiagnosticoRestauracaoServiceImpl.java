package com.example.apesc.service.restorationdiagnosis;

import org.springframework.stereotype.Service;

import com.example.apesc.model.DiagnosticoRestauracao;
import com.example.apesc.repository.DiagnosticoRestauracaoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DiagnosticoRestauracaoServiceImpl implements DiagnosticoRestauracaoService {
    
    private final DiagnosticoRestauracaoRepository diagnosticoRestauracaoRepository;

    public DiagnosticoRestauracao save(DiagnosticoRestauracao diagnosticoRestauracao) {
        return diagnosticoRestauracaoRepository.save(diagnosticoRestauracao);
    }
    
    public DiagnosticoRestauracao findById(Long id) {
        return diagnosticoRestauracaoRepository.findById(id).orElse(null);
    }
    
    public void delete(Long id) {
        diagnosticoRestauracaoRepository.deleteById(id);
    }
    
    public DiagnosticoRestauracao update(DiagnosticoRestauracao diagnosticoRestauracao) {
        return diagnosticoRestauracaoRepository.save(diagnosticoRestauracao);
    }

}
