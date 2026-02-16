package com.example.apesc.service.productionentity;

import com.example.apesc.model.EntidadeProdutora;
import java.util.List;

public interface EntidadeProdutoraService {
    
    EntidadeProdutora save(EntidadeProdutora entidadeProdutora);
    
    EntidadeProdutora update(EntidadeProdutora entidadeProdutora);
    
    void delete(Long id);
    
    EntidadeProdutora findById(Long id);
    
    List<EntidadeProdutora> findAll();
    
    List<EntidadeProdutora> findByName(String name);
    
}
