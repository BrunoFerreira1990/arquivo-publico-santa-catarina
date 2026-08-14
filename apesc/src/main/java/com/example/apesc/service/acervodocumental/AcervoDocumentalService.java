package com.example.apesc.service.acervodocumental;

import com.example.apesc.model.AcervoDocumental;
import com.example.apesc.model.enums.NaturezaTransacao;

import java.util.List;
import java.util.Optional;

public interface AcervoDocumentalService    {

    AcervoDocumental save(AcervoDocumental acervoDocumental);

    List<AcervoDocumental> findAllWithRelations();

    Optional<AcervoDocumental> findByIdWithRelations(Long id);

    Optional<AcervoDocumental> findById(Long id);

    void delete(Long id);

    List<AcervoDocumental> findByTipoDocumento(Long tipoDocumentoId);

    AcervoDocumental update(AcervoDocumental acervoDocumental);

    List<AcervoDocumental> search(String tipoDocumentoNome, String entidadeProdutoraNome, String entidadeReceptoraNome, NaturezaTransacao naturezaTransacao);
}
