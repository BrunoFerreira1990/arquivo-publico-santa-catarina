package com.example.apesc.service.documenttype;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.apesc.model.TipoDocumento;
import com.example.apesc.repository.TipoDocumentoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TipoDocumentoServiceImpl {

    private final TipoDocumentoRepository tipoDocumentoRepository;
    
    public List<TipoDocumento> findAll() {
        return tipoDocumentoRepository.findAll();
    }
    
    public TipoDocumento findById(Long id) {
        return tipoDocumentoRepository.findById(id).orElse(null);
    }
    
    public TipoDocumento save(TipoDocumento tipoDocumento) {
        return tipoDocumentoRepository.save(tipoDocumento);
    }
    
    public TipoDocumento update(TipoDocumento tipoDocumento) {
        return tipoDocumentoRepository.save(tipoDocumento);
    }
    
    public void delete(Long id) {
        tipoDocumentoRepository.deleteById(id);
    }
    
    public List<TipoDocumento> findByName(String name) {
        return tipoDocumentoRepository.findByName(name);
    }
    
}
