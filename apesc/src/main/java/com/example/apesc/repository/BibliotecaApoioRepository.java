package com.example.apesc.repository;

import com.example.apesc.model.BibliotecaApoio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BibliotecaApoioRepository extends JpaRepository<BibliotecaApoio, Long> {

    List<BibliotecaApoio> findByTipoDocumentoId(Long tipoDocumentoId);

    @Query("SELECT b FROM BibliotecaApoio b JOIN FETCH b.tipoDocumento JOIN FETCH b.entidadeProdutora")
    List<BibliotecaApoio> findAllWithRelations();

    @Query("SELECT b FROM BibliotecaApoio b JOIN FETCH b.tipoDocumento JOIN FETCH b.entidadeProdutora WHERE b.id = :id")
    Optional<BibliotecaApoio> findByIdWithRelations(@Param("id") Long id);
}
