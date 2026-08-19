package com.example.apesc.service.bibliotecaapoio;

import com.example.apesc.model.BibliotecaApoio;

import java.util.List;
import java.util.Optional;

public interface BibliotecaApoioService {

    BibliotecaApoio save(BibliotecaApoio apoio);

    List<BibliotecaApoio> findAllWithRelations();

    Optional<BibliotecaApoio> findByIdWithRelations(Long id);

    List<BibliotecaApoio> findByTipoDocumento(Long tipoDocumentoId);

    BibliotecaApoio update(BibliotecaApoio apoio);

    void delete(Long id);
}
