package com.example.apesc.service.bibliotecalivrosperiodicos.impl;

import com.example.apesc.model.BibliotecaLivrosPeriodicos;
import com.example.apesc.repository.BibliotecaLivrosPeriodicosRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import com.example.apesc.service.bibliotecalivrosperiodicos.BibliotecaLivrosPeriodicosService;
import com.example.apesc.util.BibliotecaLivrosPeriodicosValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BibliotecaLivrosPeriodicosServiceImpl implements BibliotecaLivrosPeriodicosService {

    private final BibliotecaLivrosPeriodicosRepository livroRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final BibliotecaLivrosPeriodicosValidation livroValidation;

    @Transactional
    public BibliotecaLivrosPeriodicos save(BibliotecaLivrosPeriodicos livro) {
        livroValidation.validateSave(livro, tipoDocumentoRepository);
        rehydrateRelationships(livro);
        return livroRepository.save(livro);
    }

    @Transactional(readOnly = true)
    public List<BibliotecaLivrosPeriodicos> findAllWithRelations() {
        return livroRepository.findAllWithRelations();
    }

    @Transactional(readOnly = true)
    public Optional<BibliotecaLivrosPeriodicos> findByIdWithRelations(Long id) {
        return livroRepository.findByIdWithRelations(id);
    }

    @Transactional(readOnly = true)
    public List<BibliotecaLivrosPeriodicos> findByTipoDocumento(Long tipoDocumentoId) {
        return livroRepository.findByTipoDocumentoId(tipoDocumentoId);
    }

    @Transactional
    public BibliotecaLivrosPeriodicos update(BibliotecaLivrosPeriodicos livro) {
        livroValidation.validateUpdate(livro, livroRepository, tipoDocumentoRepository);
        rehydrateRelationships(livro);
        return livroRepository.save(livro);
    }

    @Transactional
    public void delete(Long id) {
        livroValidation.validateDelete(id, livroRepository);
        livroRepository.deleteById(id);
    }

    private void rehydrateRelationships(BibliotecaLivrosPeriodicos livro) {
        if (livro.getTipoDocumento() != null && livro.getTipoDocumento().getId() != null) {
            livro.setTipoDocumento(tipoDocumentoRepository.findById(livro.getTipoDocumento().getId()).orElse(null));
        }
    }
}
