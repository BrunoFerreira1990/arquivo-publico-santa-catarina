package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoIconograficoPersonalidades;
import com.example.apesc.repository.AcervoIconograficoPersonalidadesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AcervoIconograficoPersonalidadesValidation {

    public void validateSave(AcervoIconograficoPersonalidades personalidade, AcervoIconograficoPersonalidadesRepository repository) {
        validateNome(personalidade);

        if (!repository.findByNomeIgnoreCase(personalidade.getNome()).isEmpty()) {
            throw new CustomException(ErrorConstants.PERSONALIDADE_DUPLICADA, HttpStatus.CONFLICT);
        }
    }

    public void validateUpdate(AcervoIconograficoPersonalidades personalidade, AcervoIconograficoPersonalidadesRepository repository) {
        if (personalidade.getId() == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (repository.findById(personalidade.getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        validateNome(personalidade);

        List<AcervoIconograficoPersonalidades> existentes = repository.findByNomeIgnoreCase(personalidade.getNome());
        if (!existentes.isEmpty() && !existentes.get(0).getId().equals(personalidade.getId())) {
            throw new CustomException(ErrorConstants.PERSONALIDADE_DUPLICADA, HttpStatus.CONFLICT);
        }
    }

    public void validateDelete(Long id, AcervoIconograficoPersonalidadesRepository repository) {
        if (id == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (repository.findById(id).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private void validateNome(AcervoIconograficoPersonalidades personalidade) {
        // Remove espacos em branco no inicio/fim e capitaliza cada palavra (nome e
        // sobrenome) antes de validar e persistir — ex.: "joao  da silva" vira "Joao Da Silva".
        personalidade.setNome(CommonUtils.toTitleCase(CommonUtils.trim(personalidade.getNome())));

        if (personalidade.getNome() == null || personalidade.getNome().isEmpty()) {
            throw new CustomException(ErrorConstants.PERSONALIDADE_REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }
}
