package com.example.apesc.service.documenttype;

import com.example.apesc.model.TipoDocumento;

import java.util.List;

public interface TipoDocumentoService {
    
    List<TipoDocumento> findAll();
    
    TipoDocumento findById(Long id);
    
    TipoDocumento save(TipoDocumento tipoDocumento);
    
    TipoDocumento update(TipoDocumento tipoDocumento);
    
    void delete(Long id);
    
    List<TipoDocumento> findByName(String name);
    
}
