package com.example.apesc.repository;

import com.example.apesc.model.BibliotecaLivrosPeriodicos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BibliotecaLivrosPeriodicosRepository extends JpaRepository<BibliotecaLivrosPeriodicos, Long> {

    List<BibliotecaLivrosPeriodicos> findByTipoDocumentoId(Long tipoDocumentoId);

    @Query("SELECT b FROM BibliotecaLivrosPeriodicos b JOIN FETCH b.tipoDocumento")
    List<BibliotecaLivrosPeriodicos> findAllWithRelations();

    @Query("SELECT b FROM BibliotecaLivrosPeriodicos b JOIN FETCH b.tipoDocumento WHERE b.id = :id")
    Optional<BibliotecaLivrosPeriodicos> findByIdWithRelations(@Param("id") Long id);
}
