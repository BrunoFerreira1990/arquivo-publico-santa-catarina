package com.example.apesc.repository;

import com.example.apesc.model.AcervoDocumental;
import com.example.apesc.model.enums.NaturezaTransacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AcervoDocumentalRepository extends JpaRepository<AcervoDocumental, Long>, JpaSpecificationExecutor<AcervoDocumental> {

    List<AcervoDocumental> findByTipoDocumentoId(Long tipoDocumentoId);

    boolean existsByTipoDocumentoId(Long tipoDocumentoId);

    boolean existsByTipoDocumentoIdAndEntidadeProdutoraIdAndNaturezaTransacao(
            Long tipoDocumentoId,
            Long entidadeProdutoraId,
            NaturezaTransacao naturezaTransacao
    );

    boolean existsByTipoDocumentoIdAndEntidadeProdutoraIdAndNaturezaTransacaoAndIdNot(
            Long tipoDocumentoId,
            Long entidadeProdutoraId,
            NaturezaTransacao naturezaTransacao,
            Long id
    );

    boolean existsByTipoDocumentoIdAndEntidadeProdutoraIdAndEntidadeReceptoraIdAndNaturezaTransacao(
            Long tipoDocumentoId,
            Long entidadeProdutoraId,
            Long entidadeReceptoraId,
            NaturezaTransacao naturezaTransacao
    );

    boolean existsByTipoDocumentoIdAndEntidadeProdutoraIdAndEntidadeReceptoraIdAndNaturezaTransacaoAndIdNot(
            Long tipoDocumentoId,
            Long entidadeProdutoraId,
            Long entidadeReceptoraId,
            NaturezaTransacao naturezaTransacao,
            Long id
    );

    @Query("SELECT a FROM AcervoDocumental a JOIN FETCH a.tipoDocumento JOIN FETCH a.entidadeProdutora LEFT JOIN FETCH a.entidadeReceptora")
    List<AcervoDocumental> findAllWithRelations();

    @Query("SELECT a FROM AcervoDocumental a JOIN FETCH a.tipoDocumento JOIN FETCH a.entidadeProdutora LEFT JOIN FETCH a.entidadeReceptora WHERE a.tipoDocumento.id = :tipoDocumentoId")
    List<AcervoDocumental> findByTipoDocumentoIdWithRelations(@org.springframework.data.repository.query.Param("tipoDocumentoId") Long tipoDocumentoId);

    @Query("SELECT a FROM AcervoDocumental a JOIN FETCH a.tipoDocumento JOIN FETCH a.entidadeProdutora LEFT JOIN FETCH a.entidadeReceptora WHERE a.id = :id")
    Optional<AcervoDocumental> findByIdWithRelations(@org.springframework.data.repository.query.Param("id") Long id);
}
