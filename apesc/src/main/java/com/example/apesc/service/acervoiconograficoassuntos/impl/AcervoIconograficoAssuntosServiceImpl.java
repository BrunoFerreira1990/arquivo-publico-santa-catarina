package com.example.apesc.service.acervoiconograficoassuntos.impl;

import com.example.apesc.model.AcervoIconograficoAssuntos;
import com.example.apesc.repository.AcervoIconograficoAssuntosRepository;
import com.example.apesc.service.acervoiconograficoassuntos.AcervoIconograficoAssuntosService;
import com.example.apesc.specification.AcervoIconograficoAssuntosSearchFilter;
import com.example.apesc.specification.AcervoIconograficoAssuntosSpecification;
import com.example.apesc.util.AcervoIconograficoAssuntosValidation;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AcervoIconograficoAssuntosServiceImpl implements AcervoIconograficoAssuntosService {

    private final AcervoIconograficoAssuntosRepository assuntosRepository;
    private final AcervoIconograficoAssuntosValidation assuntosValidation;

    @Transactional
    public AcervoIconograficoAssuntos save(AcervoIconograficoAssuntos assunto) {
        assuntosValidation.validateSave(assunto, assuntosRepository);
        return assuntosRepository.save(assunto);
    }

    @Transactional(readOnly = true)
    public List<AcervoIconograficoAssuntos> findAll() {
        return assuntosRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<AcervoIconograficoAssuntos> search(AcervoIconograficoAssuntosSearchFilter filtro) {
        Specification<AcervoIconograficoAssuntos> spec = AcervoIconograficoAssuntosSpecification.searchByFields(filtro);
        return assuntosRepository.findAll(spec);
    }

    @Transactional
    public AcervoIconograficoAssuntos update(AcervoIconograficoAssuntos assunto) {
        assuntosValidation.validateUpdate(assunto, assuntosRepository);
        return assuntosRepository.save(assunto);
    }

    @Transactional
    public void delete(Long id) {
        assuntosValidation.validateDelete(id, assuntosRepository);
        assuntosRepository.deleteById(id);
    }
}
