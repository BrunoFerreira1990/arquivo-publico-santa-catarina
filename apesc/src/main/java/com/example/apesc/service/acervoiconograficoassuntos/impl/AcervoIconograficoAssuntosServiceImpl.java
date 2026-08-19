package com.example.apesc.service.acervoiconograficoassuntos.impl;

import com.example.apesc.model.AcervoIconograficoAssuntos;
import com.example.apesc.repository.AcervoIconograficoAssuntosRepository;
import com.example.apesc.service.acervoiconograficoassuntos.AcervoIconograficoAssuntosService;
import com.example.apesc.util.AcervoIconograficoAssuntosValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Optional<AcervoIconograficoAssuntos> findById(Long id) {
        return assuntosRepository.findById(id);
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
