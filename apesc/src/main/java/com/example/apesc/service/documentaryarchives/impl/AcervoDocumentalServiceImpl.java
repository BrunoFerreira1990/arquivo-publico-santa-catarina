package com.example.apesc.service.documentaryarchives.impl;

import com.example.apesc.model.AcervoDocumental;
import com.example.apesc.repository.AcervoDocumentalRepository;
import com.example.apesc.service.documentaryarchives.AcervoDocumentalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AcervoDocumentalServiceImpl implements AcervoDocumentalService {

    private final AcervoDocumentalRepository acervoDocumentalRepository;

    public AcervoDocumental save(AcervoDocumental acervoDocumental) {
        return acervoDocumentalRepository.save(acervoDocumental);
    }

    public List<AcervoDocumental> findAll() {
        return acervoDocumentalRepository.findAll();
    }

    public Optional<AcervoDocumental> findById(Long id) {
        return acervoDocumentalRepository.findById(id);
    }

    public void delete(Long id) {
        acervoDocumentalRepository.deleteById(id);
    }

    public List<AcervoDocumental> findByTipoDocumento(Long tipoDocumentoId) {
        return acervoDocumentalRepository.findByTipoDocumentoId(tipoDocumentoId);
    }

    public AcervoDocumental update(AcervoDocumental acervoDocumental) {
        return acervoDocumentalRepository.save(acervoDocumental);
    }

}
