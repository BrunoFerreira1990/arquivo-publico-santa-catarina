package com.example.apesc.service.entidadeprodutora;

import com.example.apesc.model.EntidadeProdutora;
import java.util.List;

public interface EntidadeProdutoraService {
    
    EntidadeProdutora save(EntidadeProdutora entidadeProdutora);
    
    EntidadeProdutora update(EntidadeProdutora entidadeProdutora);
    
    void delete(Long id);
    
    EntidadeProdutora findById(Long id);
    
    List<EntidadeProdutora> findAll();
    
    List<EntidadeProdutora> findByNome(String nome);
    
    List<EntidadeProdutora> findByAbreviacao(String abreviacao);
    
}
