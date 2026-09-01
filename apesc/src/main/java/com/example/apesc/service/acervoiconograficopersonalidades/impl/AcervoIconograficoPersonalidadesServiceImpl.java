package com.example.apesc.service.acervoiconograficopersonalidades.impl;

import com.example.apesc.model.AcervoIconograficoPersonalidades;
import com.example.apesc.repository.AcervoIconograficoPersonalidadesRepository;
import com.example.apesc.service.acervoiconograficopersonalidades.AcervoIconograficoPersonalidadesService;
import com.example.apesc.specification.AcervoIconograficoPersonalidadesSearchFilter;
import com.example.apesc.specification.AcervoIconograficoPersonalidadesSpecification;
import com.example.apesc.util.AcervoIconograficoPersonalidadesValidation;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AcervoIconograficoPersonalidadesServiceImpl implements AcervoIconograficoPersonalidadesService {

    private final AcervoIconograficoPersonalidadesRepository personalidadesRepository;
    private final AcervoIconograficoPersonalidadesValidation personalidadesValidation;

    @Transactional
    public AcervoIconograficoPersonalidades save(AcervoIconograficoPersonalidades personalidade) {
        personalidadesValidation.validateSave(personalidade, personalidadesRepository);
        return personalidadesRepository.save(personalidade);
    }

    @Transactional(readOnly = true)
    public List<AcervoIconograficoPersonalidades> findAll() {
        return personalidadesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<AcervoIconograficoPersonalidades> search(AcervoIconograficoPersonalidadesSearchFilter filtro) {
        Specification<AcervoIconograficoPersonalidades> spec = AcervoIconograficoPersonalidadesSpecification.searchByFields(filtro);
        return personalidadesRepository.findAll(spec);
    }

    @Transactional
    public AcervoIconograficoPersonalidades update(AcervoIconograficoPersonalidades personalidade) {
        personalidadesValidation.validateUpdate(personalidade, personalidadesRepository);
        return personalidadesRepository.save(personalidade);
    }

    @Transactional
    public void delete(Long id) {
        personalidadesValidation.validateDelete(id, personalidadesRepository);
        personalidadesRepository.deleteById(id);
    }
}
