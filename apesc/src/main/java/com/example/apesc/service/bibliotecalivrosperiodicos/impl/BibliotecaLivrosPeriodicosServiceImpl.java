package com.example.apesc.service.bibliotecalivrosperiodicos.impl;

import com.example.apesc.model.BibliotecaLivrosPeriodicos;
import com.example.apesc.repository.BibliotecaLivrosPeriodicosRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import com.example.apesc.service.bibliotecalivrosperiodicos.BibliotecaLivrosPeriodicosService;
import com.example.apesc.specification.BibliotecaLivrosPeriodicosSearchFilter;
import com.example.apesc.specification.BibliotecaLivrosPeriodicosSpecification;
import com.example.apesc.util.BibliotecaLivrosPeriodicosValidation;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class BibliotecaLivrosPeriodicosServiceImpl implements BibliotecaLivrosPeriodicosService {

    private final BibliotecaLivrosPeriodicosRepository livroRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final BibliotecaLivrosPeriodicosValidation livroValidation;

    @Transactional
    public BibliotecaLivrosPeriodicos save(BibliotecaLivrosPeriodicos livro) {
        livroValidation.validateSave(livro, livroRepository, tipoDocumentoRepository);
        rehydrateRelationships(livro);
        return livroRepository.save(livro);
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

    @Transactional(readOnly = true)
    public List<BibliotecaLivrosPeriodicos> search(BibliotecaLivrosPeriodicosSearchFilter filtro) {
        Specification<BibliotecaLivrosPeriodicos> spec = BibliotecaLivrosPeriodicosSpecification.searchByFields(filtro);
        return livroRepository.findAll(spec);
    }

    private void rehydrateRelationships(BibliotecaLivrosPeriodicos livro) {
        if (livro.getTipoDocumento() != null && livro.getTipoDocumento().getId() != null) {
            livro.setTipoDocumento(tipoDocumentoRepository.findById(livro.getTipoDocumento().getId()).orElse(null));
        }
    }
}
