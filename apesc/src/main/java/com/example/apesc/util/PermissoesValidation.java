package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.Permissoes;
import com.example.apesc.repository.PermissoesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class PermissoesValidation {

    public void validateSave(Permissoes permissoes, PermissoesRepository permissoesRepository) {
        validateNomeRegra(permissoes);

        if (!permissoesRepository.findByNomeRegraIgnoreCase(permissoes.getNomeRegra()).isEmpty()) {
            throw new CustomException(ErrorConstants.NOME_REGRA_DUPLICADA, HttpStatus.CONFLICT);
        }
    }

    public void validateUpdate(Permissoes permissoes, PermissoesRepository permissoesRepository) {
        if (permissoes.getId() == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (permissoesRepository.findById(permissoes.getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        validateNomeRegra(permissoes);

        java.util.List<Permissoes> existentes = permissoesRepository.findByNomeRegraIgnoreCase(permissoes.getNomeRegra());
        if (!existentes.isEmpty() && !existentes.get(0).getId().equals(permissoes.getId())) {
            throw new CustomException(ErrorConstants.NOME_REGRA_DUPLICADA, HttpStatus.CONFLICT);
        }
    }

    public void validateDelete(Long id, PermissoesRepository permissoesRepository) {
        if (id == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (permissoesRepository.findById(id).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private void validateNomeRegra(Permissoes permissoes) {
        if (permissoes.getNomeRegra() == null || permissoes.getNomeRegra().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.NOME_REGRA_REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }
}
