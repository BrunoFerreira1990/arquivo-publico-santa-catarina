package com.example.apesc.service.documenttype;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.apesc.model.TipoDocumento;
import com.example.apesc.repository.TipoDocumentoRepository;
import com.example.apesc.util.TipoDocumentoValidation;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TipoDocumentoServiceImpl {

    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final TipoDocumentoValidation tipoDocumentoValidation;
    
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
        tipoDocumentoValidation.validateSave(tipoDocumento, tipoDocumentoRepository);
        return tipoDocumentoRepository.save(tipoDocumento);
    }
    
    @Transactional
    public TipoDocumento update(TipoDocumento tipoDocumento) {
        tipoDocumentoValidation.validateSave(tipoDocumento, tipoDocumentoRepository);
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
