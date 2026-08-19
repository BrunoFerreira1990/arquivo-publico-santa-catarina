package com.example.apesc.service.permissoes;

import com.example.apesc.model.Permissoes;

import java.util.List;
import java.util.Optional;

public interface PermissoesService {

    Permissoes save(Permissoes permissoes);

    List<Permissoes> findAll();

    Optional<Permissoes> findById(Long id);

    Permissoes update(Permissoes permissoes);

    void delete(Long id);
}
