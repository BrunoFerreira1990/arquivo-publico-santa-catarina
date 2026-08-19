package com.example.apesc.service.acervoiconografico.impl;

import com.example.apesc.model.AcervoIconografico;
import com.example.apesc.model.AcervoIconograficoAssuntos;
import com.example.apesc.repository.AcervoIconograficoAssuntosRepository;
import com.example.apesc.repository.AcervoIconograficoRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import com.example.apesc.service.acervoiconografico.AcervoIconograficoService;
import com.example.apesc.util.AcervoIconograficoValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AcervoIconograficoServiceImpl implements AcervoIconograficoService {

    private final AcervoIconograficoRepository acervoIconograficoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final AcervoIconograficoAssuntosRepository assuntosRepository;
    private final AcervoIconograficoValidation acervoIconograficoValidation;

    @Transactional
    public AcervoIconografico save(AcervoIconografico acervo) {
        acervoIconograficoValidation.validateSave(acervo, acervoIconograficoRepository, tipoDocumentoRepository, assuntosRepository);
        rehydrateRelationships(acervo);
        return acervoIconograficoRepository.save(acervo);
    }

    @Transactional(readOnly = true)
    public List<AcervoIconografico> findAllWithRelations() {
        return acervoIconograficoRepository.findAllWithRelations();
    }

    @Transactional(readOnly = true)
    public Optional<AcervoIconografico> findByIdWithRelations(Long id) {
        return acervoIconograficoRepository.findByIdWithRelations(id);
    }

    @Transactional(readOnly = true)
    public List<AcervoIconografico> findByTipoDocumento(Long tipoDocumentoId) {
        return acervoIconograficoRepository.findByTipoDocumentoId(tipoDocumentoId);
    }

    @Transactional
    public AcervoIconografico update(AcervoIconografico acervo) {
        acervoIconograficoValidation.validateUpdate(acervo, acervoIconograficoRepository, tipoDocumentoRepository, assuntosRepository);
        rehydrateRelationships(acervo);
        return acervoIconograficoRepository.save(acervo);
    }

    @Transactional
    public void delete(Long id) {
        acervoIconograficoValidation.validateDelete(id, acervoIconograficoRepository);
        acervoIconograficoRepository.deleteById(id);
    }

    private void rehydrateRelationships(AcervoIconografico acervo) {
        if (acervo.getTipoDocumento() != null && acervo.getTipoDocumento().getId() != null) {
            acervo.setTipoDocumento(tipoDocumentoRepository.findById(acervo.getTipoDocumento().getId()).orElse(null));
        }
        if (acervo.getAssuntos() != null && !acervo.getAssuntos().isEmpty()) {
            List<Long> assuntoIds = acervo.getAssuntos().stream()
                    .map(AcervoIconograficoAssuntos::getId)
                    .collect(Collectors.toList());
            Set<AcervoIconograficoAssuntos> assuntosCompletos = Set.copyOf(assuntosRepository.findAllById(assuntoIds));
            acervo.setAssuntos(assuntosCompletos);
        }
    }
}
