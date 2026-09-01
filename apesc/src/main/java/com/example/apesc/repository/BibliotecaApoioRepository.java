package com.example.apesc.repository;

import com.example.apesc.model.BibliotecaApoio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BibliotecaApoioRepository extends JpaRepository<BibliotecaApoio, Long>, JpaSpecificationExecutor<BibliotecaApoio> {

    List<BibliotecaApoio> findByTipoDocumentoId(Long tipoDocumentoId);

    // Duplicidade: mesmo tipo de documento, entidade produtora, titulo e periodo.
    // localizacao, quantidadeVolume, identificador e disponibilidade sao ignorados.
    boolean existsByTipoDocumentoIdAndEntidadeProdutoraIdAndTituloAndPeriodo(
            Long tipoDocumentoId, Long entidadeProdutoraId, String titulo, String periodo);

    boolean existsByTipoDocumentoIdAndEntidadeProdutoraIdAndTituloAndPeriodoAndIdNot(
            Long tipoDocumentoId, Long entidadeProdutoraId, String titulo, String periodo, Long id);

    @Query("SELECT b FROM BibliotecaApoio b JOIN FETCH b.tipoDocumento JOIN FETCH b.entidadeProdutora")
    List<BibliotecaApoio> findAllWithRelations();

    @Query("SELECT b FROM BibliotecaApoio b JOIN FETCH b.tipoDocumento JOIN FETCH b.entidadeProdutora WHERE b.id = :id")
    Optional<BibliotecaApoio> findByIdWithRelations(@Param("id") Long id);
}
