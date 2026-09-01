package com.example.apesc.service.acervoiconografico.impl;

import com.example.apesc.model.AcervoIconografico;
import com.example.apesc.model.AcervoIconograficoAssuntos;
import com.example.apesc.model.AcervoIconograficoPersonalidades;
import com.example.apesc.repository.AcervoIconograficoAssuntosRepository;
import com.example.apesc.repository.AcervoIconograficoPersonalidadesRepository;
import com.example.apesc.repository.AcervoIconograficoRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import com.example.apesc.service.acervoiconografico.AcervoIconograficoService;
import com.example.apesc.specification.AcervoIconograficoSearchFilter;
import com.example.apesc.specification.AcervoIconograficoSpecification;
import com.example.apesc.util.AcervoIconograficoValidation;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AcervoIconograficoServiceImpl implements AcervoIconograficoService {

    private final AcervoIconograficoRepository acervoIconograficoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final AcervoIconograficoAssuntosRepository assuntosRepository;
    private final AcervoIconograficoPersonalidadesRepository personalidadesRepository;
    private final AcervoIconograficoValidation acervoIconograficoValidation;

    @Transactional
    public AcervoIconografico save(AcervoIconografico acervo) {
        acervoIconograficoValidation.validateSave(acervo, acervoIconograficoRepository, tipoDocumentoRepository, assuntosRepository, personalidadesRepository);
        rehydrateRelationships(acervo);
        return acervoIconograficoRepository.save(acervo);
    }

    @Transactional(readOnly = true)
    public List<AcervoIconografico> findAllWithRelations() {
        return acervoIconograficoRepository.findAllWithRelations();
    }

    @Transactional(readOnly = true)
    public List<AcervoIconografico> search(AcervoIconograficoSearchFilter filtro) {
        Specification<AcervoIconografico> spec = AcervoIconograficoSpecification.searchByFields(filtro);
        return acervoIconograficoRepository.findAll(spec);
    }

    @Transactional(readOnly = true)
    public List<AcervoIconografico> findByTipoDocumento(Long tipoDocumentoId) {
        return acervoIconograficoRepository.findByTipoDocumentoId(tipoDocumentoId);
    }

    @Transactional
    public AcervoIconografico update(AcervoIconografico acervo) {
        acervoIconograficoValidation.validateUpdate(acervo, acervoIconograficoRepository, tipoDocumentoRepository, assuntosRepository, personalidadesRepository);
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
        if (acervo.getPersonalidades() != null && !acervo.getPersonalidades().isEmpty()) {
            List<Long> personalidadeIds = acervo.getPersonalidades().stream()
                    .map(AcervoIconograficoPersonalidades::getId)
                    .collect(Collectors.toList());
            Set<AcervoIconograficoPersonalidades> personalidadesCompletas = Set.copyOf(personalidadesRepository.findAllById(personalidadeIds));
            acervo.setPersonalidades(personalidadesCompletas);
        }
    }
}
