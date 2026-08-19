package com.example.apesc.service.bibliotecaapoio.impl;

import com.example.apesc.model.BibliotecaApoio;
import com.example.apesc.repository.BibliotecaApoioRepository;
import com.example.apesc.repository.EntidadeProdutoraRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import com.example.apesc.service.bibliotecaapoio.BibliotecaApoioService;
import com.example.apesc.util.BibliotecaApoioValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BibliotecaApoioServiceImpl implements BibliotecaApoioService {

    private final BibliotecaApoioRepository apoioRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final EntidadeProdutoraRepository entidadeProdutoraRepository;
    private final BibliotecaApoioValidation apoioValidation;

    @Transactional
    public BibliotecaApoio save(BibliotecaApoio apoio) {
        apoioValidation.validateSave(apoio, tipoDocumentoRepository, entidadeProdutoraRepository);
        rehydrateRelationships(apoio);
        return apoioRepository.save(apoio);
    }

    @Transactional(readOnly = true)
    public List<BibliotecaApoio> findAllWithRelations() {
        return apoioRepository.findAllWithRelations();
    }

    @Transactional(readOnly = true)
    public Optional<BibliotecaApoio> findByIdWithRelations(Long id) {
        return apoioRepository.findByIdWithRelations(id);
    }

    @Transactional(readOnly = true)
    public List<BibliotecaApoio> findByTipoDocumento(Long tipoDocumentoId) {
        return apoioRepository.findByTipoDocumentoId(tipoDocumentoId);
    }

    @Transactional
    public BibliotecaApoio update(BibliotecaApoio apoio) {
        apoioValidation.validateUpdate(apoio, apoioRepository, tipoDocumentoRepository, entidadeProdutoraRepository);
        rehydrateRelationships(apoio);
        return apoioRepository.save(apoio);
    }

    @Transactional
    public void delete(Long id) {
        apoioValidation.validateDelete(id, apoioRepository);
        apoioRepository.deleteById(id);
    }

    private void rehydrateRelationships(BibliotecaApoio apoio) {
        if (apoio.getTipoDocumento() != null && apoio.getTipoDocumento().getId() != null) {
            apoio.setTipoDocumento(tipoDocumentoRepository.findById(apoio.getTipoDocumento().getId()).orElse(null));
        }
        if (apoio.getEntidadeProdutora() != null && apoio.getEntidadeProdutora().getId() != null) {
            apoio.setEntidadeProdutora(entidadeProdutoraRepository.findById(apoio.getEntidadeProdutora().getId()).orElse(null));
        }
    }
}
