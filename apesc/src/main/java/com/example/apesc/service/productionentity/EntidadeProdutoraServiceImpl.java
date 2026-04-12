package com.example.apesc.service.productionentity;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.apesc.model.EntidadeProdutora;
import com.example.apesc.repository.EntidadeProdutoraRepository;
import com.example.apesc.util.CommonUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EntidadeProdutoraServiceImpl implements EntidadeProdutoraService {
    
    private final EntidadeProdutoraRepository entidadeProdutoraRepository;
    

    public EntidadeProdutora save(EntidadeProdutora entidadeProdutora) {
        if (entidadeProdutora.getNome() != null) {
            entidadeProdutora.setNome(CommonUtils.toTitleCase(entidadeProdutora.getNome()));
        }
        if (entidadeProdutora.getAbreviacao() != null) {
            entidadeProdutora.setAbreviacao(CommonUtils.toUpperCaseSafe(entidadeProdutora.getAbreviacao()));
        }
        return entidadeProdutoraRepository.save(entidadeProdutora);
    }
    
    public EntidadeProdutora update(EntidadeProdutora entidadeProdutora) {
        if (entidadeProdutora.getNome() != null) {
            entidadeProdutora.setNome(CommonUtils.toTitleCase(entidadeProdutora.getNome()));
        }
        if (entidadeProdutora.getAbreviacao() != null) {
            entidadeProdutora.setAbreviacao(CommonUtils.toUpperCaseSafe(entidadeProdutora.getAbreviacao()));
        }
        return entidadeProdutoraRepository.save(entidadeProdutora);
    }
    
    public void delete(Long id) {
        entidadeProdutoraRepository.deleteById(id);
    }
    
    public EntidadeProdutora findById(Long id) {
        return entidadeProdutoraRepository.findById(id).orElse(null);
    }
    
    public List<EntidadeProdutora> findAll() {
        return entidadeProdutoraRepository.findAll();
    }
    
    public List<EntidadeProdutora> findByNome(String nome) {
        return entidadeProdutoraRepository.findByNome(nome);
        
    }
    
}
