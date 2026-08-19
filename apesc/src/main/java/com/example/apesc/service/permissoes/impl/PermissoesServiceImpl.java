package com.example.apesc.service.permissoes.impl;

import com.example.apesc.model.Permissoes;
import com.example.apesc.repository.PermissoesRepository;
import com.example.apesc.service.permissoes.PermissoesService;
import com.example.apesc.util.PermissoesValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PermissoesServiceImpl implements PermissoesService {

    private final PermissoesRepository permissoesRepository;
    private final PermissoesValidation permissoesValidation;

    @Transactional
    public Permissoes save(Permissoes permissoes) {
        permissoesValidation.validateSave(permissoes, permissoesRepository);
        return permissoesRepository.save(permissoes);
    }

    @Transactional(readOnly = true)
    public List<Permissoes> findAll() {
        return permissoesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Permissoes> findById(Long id) {
        return permissoesRepository.findById(id);
    }

    @Transactional
    public Permissoes update(Permissoes permissoes) {
        permissoesValidation.validateUpdate(permissoes, permissoesRepository);
        return permissoesRepository.save(permissoes);
    }

    @Transactional
    public void delete(Long id) {
        permissoesValidation.validateDelete(id, permissoesRepository);
        permissoesRepository.deleteById(id);
    }
}
