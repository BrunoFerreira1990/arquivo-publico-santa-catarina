package com.example.apesc.util;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.EntidadeProdutora;
import com.example.apesc.repository.EntidadeProdutoraRepository;

import java.util.List;

@Component
public class EntidadeProdutoraValidation {

    public void validateSave(EntidadeProdutora entidadeProdutora, EntidadeProdutoraRepository repository) {
        if (entidadeProdutora.getNome() == null || entidadeProdutora.getNome().isEmpty()) {
            throw new CustomException(ErrorConstants.EMPTY_NAME, HttpStatus.BAD_REQUEST);
        }

        if (entidadeProdutora.getAbreviacao() == null || entidadeProdutora.getAbreviacao().isEmpty()) {
            throw new CustomException(ErrorConstants.EMPTY_ABREVIACAO, HttpStatus.BAD_REQUEST);
        }

        if (!repository.findByNomeIgnoreCase(entidadeProdutora.getNome()).isEmpty()) {
            throw new CustomException(ErrorConstants.DUPLICATE_NAME, HttpStatus.BAD_REQUEST);
        }

        if (!repository.findByAbreviacaoIgnoreCase(entidadeProdutora.getAbreviacao()).isEmpty()) {
            throw new CustomException(ErrorConstants.DUPLICATE_ABREVIACAO, HttpStatus.BAD_REQUEST);
        }
    }

    public void validateUpdate(EntidadeProdutora entidadeProdutora, EntidadeProdutoraRepository repository) {
        if (entidadeProdutora.getId() == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }
        
        if (repository.findById(entidadeProdutora.getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if (entidadeProdutora.getNome() == null || entidadeProdutora.getNome().isEmpty()) {
            throw new CustomException(ErrorConstants.EMPTY_NAME, HttpStatus.BAD_REQUEST);
        }

        if (entidadeProdutora.getAbreviacao() == null || entidadeProdutora.getAbreviacao().isEmpty()) {
            throw new CustomException(ErrorConstants.EMPTY_ABREVIACAO, HttpStatus.BAD_REQUEST);
        }

        List<EntidadeProdutora> existingByName = repository.findByNomeIgnoreCase(entidadeProdutora.getNome());
        if (!existingByName.isEmpty() && !existingByName.get(0).getId().equals(entidadeProdutora.getId())) {
            throw new CustomException(ErrorConstants.DUPLICATE_NAME, HttpStatus.BAD_REQUEST);
        }

        List<EntidadeProdutora> existingByAbrev = repository.findByAbreviacaoIgnoreCase(entidadeProdutora.getAbreviacao());
        if (!existingByAbrev.isEmpty() && !existingByAbrev.get(0).getId().equals(entidadeProdutora.getId())) {
            throw new CustomException(ErrorConstants.DUPLICATE_ABREVIACAO, HttpStatus.BAD_REQUEST);
        }
    }

    public void validateDelete(Long id, EntidadeProdutoraRepository repository) {
        if (id == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (repository.findById(id).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    public void validateFindByName(String name, EntidadeProdutoraRepository repository) {
        if (name == null || name.trim().isEmpty()) {
            throw new CustomException(ErrorConstants.EMPTY_NAME, HttpStatus.BAD_REQUEST);
        }

        if (repository.findByNomeIgnoreCase(name).isEmpty()) {
            throw new CustomException(ErrorConstants.NAME_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    public void validateFindByAbreviacao(String abreviacao, EntidadeProdutoraRepository repository) {
        if (abreviacao == null || abreviacao.trim().isEmpty()) {
            throw new CustomException(ErrorConstants.EMPTY_ABREVIACAO, HttpStatus.BAD_REQUEST);
        }

        if (repository.findByAbreviacaoIgnoreCase(abreviacao).isEmpty()) {
            throw new CustomException(ErrorConstants.ABREVIACAO_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }
}
