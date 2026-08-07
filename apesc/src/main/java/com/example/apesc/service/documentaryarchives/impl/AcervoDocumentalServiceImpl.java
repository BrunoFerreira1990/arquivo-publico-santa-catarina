package com.example.apesc.service.documentaryarchives.impl;

import com.example.apesc.model.AcervoDocumental;
import com.example.apesc.repository.AcervoDocumentalRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import com.example.apesc.repository.EntidadeProdutoraRepository;
import com.example.apesc.service.documentaryarchives.AcervoDocumentalService;
import com.example.apesc.util.AcervoDocumentalValidation;
import lombok.AllArgsConstructor;
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
        
        rehydrateRelationships(acervoDocumental);

        return acervoDocumentalRepository.save(acervoDocumental);
    }

    @Transactional(readOnly = true)
    public List<AcervoDocumental> findAllWithRelations() {
        return acervoDocumentalRepository.findAllWithRelations();
    }

    @Transactional(readOnly = true)
    public Optional<AcervoDocumental> findByIdWithRelations(Long id) {
        return acervoDocumentalRepository.findByIdWithRelations(id);
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
        return acervoDocumentalRepository.findByTipoDocumentoIdWithRelations(tipoDocumentoId);
    }

    @Transactional
    public AcervoDocumental update(AcervoDocumental acervoDocumental) {
        acervoValidation.validateUpdate(acervoDocumental, acervoDocumentalRepository, 
                                     tipoDocumentoRepository, entidadeProdutoraRepository);
                                     
        rehydrateRelationships(acervoDocumental);
        
        return acervoDocumentalRepository.save(acervoDocumental);
    }

    private void rehydrateRelationships(AcervoDocumental acervoDocumental) {
        if (acervoDocumental.getTipoDocumento() != null && acervoDocumental.getTipoDocumento().getId() != null) {
            acervoDocumental.setTipoDocumento(tipoDocumentoRepository.findById(acervoDocumental.getTipoDocumento().getId()).orElse(null));
        }
        if (acervoDocumental.getEntidadeProdutora() != null && acervoDocumental.getEntidadeProdutora().getId() != null) {
            acervoDocumental.setEntidadeProdutora(entidadeProdutoraRepository.findById(acervoDocumental.getEntidadeProdutora().getId()).orElse(null));
        }
        if (acervoDocumental.getEntidadeReceptora() != null && acervoDocumental.getEntidadeReceptora().getId() != null) {
            acervoDocumental.setEntidadeReceptora(entidadeProdutoraRepository.findById(acervoDocumental.getEntidadeReceptora().getId()).orElse(null));
        }
    }
}
