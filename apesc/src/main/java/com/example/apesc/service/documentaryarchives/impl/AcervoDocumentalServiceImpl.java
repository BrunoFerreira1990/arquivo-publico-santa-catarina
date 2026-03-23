package com.example.apesc.service.documentaryarchives.impl;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoDocumental;
import com.example.apesc.repository.AcervoDocumentalRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import com.example.apesc.repository.EntidadeProdutoraRepository;
import com.example.apesc.service.documentaryarchives.AcervoDocumentalService;
import com.example.apesc.util.AcervoDocumentalValidation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AcervoDocumentalServiceImpl implements AcervoDocumentalService {

    private final AcervoDocumentalRepository acervoDocumentalRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final EntidadeProdutoraRepository entidadeProdutoraRepository;
    private final AcervoDocumentalValidation acervoValidation;

    @Transactional
    public AcervoDocumental save(AcervoDocumental acervoDocumental) {
        acervoValidation.validateSave(acervoDocumental, acervoDocumentalRepository, 
                                   tipoDocumentoRepository, entidadeProdutoraRepository);
        return acervoDocumentalRepository.save(acervoDocumental);
    }

    @Transactional(readOnly = true)
    public List<AcervoDocumental> findAll() {
        return acervoDocumentalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<AcervoDocumental> findById(Long id) {
        acervoValidation.validateFindById(id, acervoDocumentalRepository);
        return acervoDocumentalRepository.findById(id);
    }

    @Transactional
    public void delete(Long id) {
        acervoValidation.validateDelete(id, acervoDocumentalRepository);
        acervoDocumentalRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AcervoDocumental> findByTipoDocumento(Long tipoDocumentoId) {
        acervoValidation.validateFindByTipoDocumento(tipoDocumentoId, tipoDocumentoRepository);
        return acervoDocumentalRepository.findByTipoDocumentoId(tipoDocumentoId);
    }

    @Transactional
    public AcervoDocumental update(AcervoDocumental acervoDocumental) {
        acervoValidation.validateUpdate(acervoDocumental, acervoDocumentalRepository, 
                                     tipoDocumentoRepository, entidadeProdutoraRepository);
        return acervoDocumentalRepository.save(acervoDocumental);
    }
}
