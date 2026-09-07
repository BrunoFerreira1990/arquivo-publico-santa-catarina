package com.example.apesc.service.acervodocumentaltombo.impl;

import com.example.apesc.model.AcervoDocumentalTombo;
import com.example.apesc.repository.AcervoDocumentalRepository;
import com.example.apesc.repository.AcervoDocumentalTomboRepository;
import com.example.apesc.service.acervodocumentaltombo.AcervoDocumentalTomboService;
import com.example.apesc.specification.AcervoDocumentalTomboSearchFilter;
import com.example.apesc.specification.AcervoDocumentalTomboSpecification;
import com.example.apesc.util.AcervoDocumentalTomboValidation;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AcervoDocumentalTomboServiceImpl implements AcervoDocumentalTomboService {

    private final AcervoDocumentalTomboRepository tomboRepository;
    private final AcervoDocumentalRepository acervoDocumentalRepository;
    private final AcervoDocumentalTomboValidation tomboValidation;

    @Transactional
    public AcervoDocumentalTombo save(AcervoDocumentalTombo tombo) {
        tomboValidation.validateSave(tombo, tomboRepository, acervoDocumentalRepository);
        rehydrateRelationships(tombo);
        return tomboRepository.save(tombo);
    }

    @Transactional(readOnly = true)
    public List<AcervoDocumentalTombo> findByAcervoDocumento(Long acervoDocumentalId) {
        return tomboRepository.findByAcervoDocumentalId(acervoDocumentalId);
    }

    @Transactional(readOnly = true)
    public List<AcervoDocumentalTombo> search(AcervoDocumentalTomboSearchFilter filtro) {
        Specification<AcervoDocumentalTombo> spec = AcervoDocumentalTomboSpecification.searchByFields(filtro);
        return tomboRepository.findAll(spec);
    }

    @Transactional
    public AcervoDocumentalTombo update(AcervoDocumentalTombo tombo) {
        tomboValidation.validateUpdate(tombo, tomboRepository, acervoDocumentalRepository);
        rehydrateRelationships(tombo);
        return tomboRepository.save(tombo);
    }

    @Transactional
    public void delete(Long id) {
        tomboValidation.validateDelete(id, tomboRepository);
        tomboRepository.deleteById(id);
    }

    private void rehydrateRelationships(AcervoDocumentalTombo tombo) {
        if (tombo.getAcervoDocumental() != null && tombo.getAcervoDocumental().getId() != null) {
            tombo.setAcervoDocumental(acervoDocumentalRepository.findById(tombo.getAcervoDocumental().getId()).orElse(null));
        }
    }
}
