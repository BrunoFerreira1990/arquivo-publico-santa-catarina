package com.example.apesc.service.documenttype;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.apesc.model.TipoDocumento;
import com.example.apesc.repository.TipoDocumentoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TipoDocumentoServiceImpl {

    private final TipoDocumentoRepository tipoDocumentoRepository;
    
    @Transactional(readOnly = true)
    public List<TipoDocumento> findAll() {
        return tipoDocumentoRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public TipoDocumento findById(Long id) {
        return tipoDocumentoRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public TipoDocumento save(TipoDocumento tipoDocumento) {
       
        
        return tipoDocumentoRepository.save(tipoDocumento);
    }
    
    @Transactional
    public TipoDocumento update(TipoDocumento tipoDocumento) {
        return tipoDocumentoRepository.save(tipoDocumento);
    }
    
    @Transactional
    public void delete(Long id) {
        tipoDocumentoRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<TipoDocumento> findByName(String name) {
        return tipoDocumentoRepository.findByName(name);
    }
    
}
