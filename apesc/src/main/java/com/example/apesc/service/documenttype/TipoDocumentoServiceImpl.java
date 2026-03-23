package com.example.apesc.service.documenttype;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.apesc.model.TipoDocumento;
import com.example.apesc.repository.AcervoDocumentalRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import com.example.apesc.util.TipoDocumentoValidation;
import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TipoDocumentoServiceImpl {

    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final TipoDocumentoValidation tipoDocumentoValidation;
    private final AcervoDocumentalRepository acervoDocumentalRepository;
    
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
        tipoDocumentoValidation.validateUpdate(tipoDocumento, tipoDocumentoRepository);
        return tipoDocumentoRepository.save(tipoDocumento);
    }
    
    @Transactional
    public String delete(Long id) {
        tipoDocumentoValidation.validateDelete(id, tipoDocumentoRepository);
        
        if (acervoDocumentalRepository.existsByTipoDocumentoId(id)) {
            throw new CustomException(
                ErrorConstants.TIPO_DOCUMENTO_HAS_DOCUMENTS, 
                HttpStatus.CONFLICT
            );
        }
        
        tipoDocumentoRepository.deleteById(id);
        return "Tipo de documento deletado com sucesso";
    }
    
    @Transactional(readOnly = true)
    public List<TipoDocumento> findByName(String name) {
        tipoDocumentoValidation.validateFindByName(name, tipoDocumentoRepository);
        return tipoDocumentoRepository.findByName(name);
    }
    
}
