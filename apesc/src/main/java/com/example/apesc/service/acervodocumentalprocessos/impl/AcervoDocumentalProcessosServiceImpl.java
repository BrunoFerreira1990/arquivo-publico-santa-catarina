package com.example.apesc.service.acervodocumentalprocessos.impl;

import com.example.apesc.model.AcervoDocumentalProcessos;
import com.example.apesc.repository.AcervoDocumentalProcessosRepository;
import com.example.apesc.repository.AcervoDocumentalRepository;
import com.example.apesc.service.acervodocumentalprocessos.AcervoDocumentalProcessosService;
import com.example.apesc.util.AcervoDocumentalProcessosValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AcervoDocumentalProcessosServiceImpl implements AcervoDocumentalProcessosService {

    private final AcervoDocumentalProcessosRepository processoRepository;
    private final AcervoDocumentalRepository acervoDocumentalRepository;
    private final AcervoDocumentalProcessosValidation processoValidation;

    @Transactional
    public AcervoDocumentalProcessos save(AcervoDocumentalProcessos processo) {
        processoValidation.validateSave(processo, processoRepository, acervoDocumentalRepository);
        rehydrateRelationships(processo);
        return processoRepository.save(processo);
    }

    @Transactional(readOnly = true)
    public List<AcervoDocumentalProcessos> findAllWithRelations() {
        return processoRepository.findAllWithRelations();
    }

    @Transactional(readOnly = true)
    public Optional<AcervoDocumentalProcessos> findByIdWithRelations(Long id) {
        return processoRepository.findByIdWithRelations(id);
    }

    @Transactional(readOnly = true)
    public List<AcervoDocumentalProcessos> findByAcervoDocumento(Long acervoDocumentalId) {
        return processoRepository.findByAcervoDocumentalId(acervoDocumentalId);
    }

    @Transactional
    public AcervoDocumentalProcessos update(AcervoDocumentalProcessos processo) {
        processoValidation.validateUpdate(processo, processoRepository, acervoDocumentalRepository);
        rehydrateRelationships(processo);
        return processoRepository.save(processo);
    }

    @Transactional
    public void delete(Long id) {
        processoValidation.validateDelete(id, processoRepository);
        processoRepository.deleteById(id);
    }

    private void rehydrateRelationships(AcervoDocumentalProcessos processo) {
        if (processo.getAcervoDocumental() != null && processo.getAcervoDocumental().getId() != null) {
            processo.setAcervoDocumental(acervoDocumentalRepository.findById(processo.getAcervoDocumental().getId()).orElse(null));
        }
    }
}
