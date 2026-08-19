package com.example.apesc.service.bibliotecalivrosperiodicos;

import com.example.apesc.model.BibliotecaLivrosPeriodicos;

import java.util.List;
import java.util.Optional;

public interface BibliotecaLivrosPeriodicosService {

    BibliotecaLivrosPeriodicos save(BibliotecaLivrosPeriodicos livro);

    List<BibliotecaLivrosPeriodicos> findAllWithRelations();

    Optional<BibliotecaLivrosPeriodicos> findByIdWithRelations(Long id);

    List<BibliotecaLivrosPeriodicos> findByTipoDocumento(Long tipoDocumentoId);

    BibliotecaLivrosPeriodicos update(BibliotecaLivrosPeriodicos livro);

    void delete(Long id);
}
