package com.example.apesc.repository;

import com.example.apesc.model.BibliotecaLivrosPeriodicos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BibliotecaLivrosPeriodicosRepository extends JpaRepository<BibliotecaLivrosPeriodicos, Long>, JpaSpecificationExecutor<BibliotecaLivrosPeriodicos> {

    boolean existsByTituloAndSubtituloAndAutoresAndEditoraAndEdicao(
            String titulo, String subtitulo, String autores, String editora, String edicao);

    boolean existsByTituloAndSubtituloAndAutoresAndEditoraAndEdicaoAndIdNot(
            String titulo, String subtitulo, String autores, String editora, String edicao, Long id);
}
