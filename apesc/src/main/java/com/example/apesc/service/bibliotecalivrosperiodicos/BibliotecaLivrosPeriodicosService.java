package com.example.apesc.service.bibliotecalivrosperiodicos;

import com.example.apesc.model.BibliotecaLivrosPeriodicos;
import com.example.apesc.specification.BibliotecaLivrosPeriodicosSearchFilter;

import java.util.List;

public interface BibliotecaLivrosPeriodicosService {

    BibliotecaLivrosPeriodicos save(BibliotecaLivrosPeriodicos livro);

    BibliotecaLivrosPeriodicos update(BibliotecaLivrosPeriodicos livro);

    void delete(Long id);

    List<BibliotecaLivrosPeriodicos> search(BibliotecaLivrosPeriodicosSearchFilter filtro);
}
