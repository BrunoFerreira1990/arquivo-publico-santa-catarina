package com.example.apesc.service.acervocartografico.impl;

import com.example.apesc.model.AcervoCartografico;
import com.example.apesc.repository.AcervoCartograficoRepository;
import com.example.apesc.repository.EntidadeProdutoraRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import com.example.apesc.service.acervocartografico.AcervoCartograficoService;
import com.example.apesc.util.AcervoCartograficoValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AcervoCartograficoServiceImpl implements AcervoCartograficoService {

    private final AcervoCartograficoRepository acervoCartograficoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final EntidadeProdutoraRepository entidadeProdutoraRepository;
    private final AcervoCartograficoValidation acervoCartograficoValidation;

    @Transactional
    public AcervoCartografico save(AcervoCartografico acervoCartografico) {
        acervoCartograficoValidation.validateSave(acervoCartografico, acervoCartograficoRepository, tipoDocumentoRepository, entidadeProdutoraRepository);

        rehydrateRelationships(acervoCartografico);

        return acervoCartograficoRepository.save(acervoCartografico);
    }

    @Transactional(readOnly = true)
    public List<AcervoCartografico> findAllWithRelations() {
        return acervoCartograficoRepository.findAllWithRelations();
    }

    @Transactional(readOnly = true)
    public Optional<AcervoCartografico> findByIdWithRelations(Long id) {
        return acervoCartograficoRepository.findByIdWithRelations(id);
    }

    @Transactional(readOnly = true)
    public Optional<AcervoCartografico> findById(Long id) {
        acervoCartograficoValidation.validateFindById(id);
        return acervoCartograficoRepository.findById(id);
    }

    @Transactional
    public void delete(Long id) {
        acervoCartograficoValidation.validateDelete(id, acervoCartograficoRepository);
        acervoCartograficoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AcervoCartografico> findByTipoDocumento(Long tipoDocumentoId) {
        return acervoCartograficoRepository.findByTipoDocumentoId(tipoDocumentoId);
    }

    @Transactional
    public AcervoCartografico update(AcervoCartografico acervoCartografico) {
        acervoCartograficoValidation.validateUpdate(acervoCartografico, acervoCartograficoRepository, tipoDocumentoRepository, entidadeProdutoraRepository);

        rehydrateRelationships(acervoCartografico);

        return acervoCartograficoRepository.save(acervoCartografico);
    }

    private void rehydrateRelationships(AcervoCartografico acervoCartografico) {
        if (acervoCartografico.getTipoDocumento() != null && acervoCartografico.getTipoDocumento().getId() != null) {
            acervoCartografico.setTipoDocumento(
                    tipoDocumentoRepository.findById(acervoCartografico.getTipoDocumento().getId()).orElse(null)
            );
        }
        if (acervoCartografico.getEntidadeProdutora() != null && acervoCartografico.getEntidadeProdutora().getId() != null) {
            acervoCartografico.setEntidadeProdutora(
                    entidadeProdutoraRepository.findById(acervoCartografico.getEntidadeProdutora().getId()).orElse(null)
            );
        }
    }
}
