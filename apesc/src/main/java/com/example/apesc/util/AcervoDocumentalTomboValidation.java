package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoDocumentalTombo;
import com.example.apesc.repository.AcervoDocumentalTomboRepository;
import com.example.apesc.repository.AcervoDocumentalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AcervoDocumentalTomboValidation {

    public void validateSave(AcervoDocumentalTombo tombo,
                              AcervoDocumentalTomboRepository tomboRepository,
                              AcervoDocumentalRepository acervoDocumentalRepository) {

        validateBasicFields(tombo, acervoDocumentalRepository);

        if (tomboRepository.existsByAcervoDocumentalIdAndNumeroTombo(tombo.getAcervoDocumental().getId(), tombo.getNumeroTombo())) {
            throw new CustomException(ErrorConstants.NUMERO_TOMBO_DUPLICADO, HttpStatus.CONFLICT);
        }
    }

    public void validateUpdate(AcervoDocumentalTombo tombo,
                                AcervoDocumentalTomboRepository tomboRepository,
                                AcervoDocumentalRepository acervoDocumentalRepository) {

        if (tombo.getId() == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (tomboRepository.findById(tombo.getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        validateBasicFields(tombo, acervoDocumentalRepository);

        if (tomboRepository.existsByAcervoDocumentalIdAndNumeroTomboAndIdNot(
                tombo.getAcervoDocumental().getId(), tombo.getNumeroTombo(), tombo.getId())) {
            throw new CustomException(ErrorConstants.NUMERO_TOMBO_DUPLICADO, HttpStatus.CONFLICT);
        }
    }

    public void validateDelete(Long id, AcervoDocumentalTomboRepository tomboRepository) {
        if (id == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (tomboRepository.findById(id).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private void validateBasicFields(AcervoDocumentalTombo tombo, AcervoDocumentalRepository acervoDocumentalRepository) {
        if (tombo.getAcervoDocumental() == null || tombo.getAcervoDocumental().getId() == null) {
            throw new CustomException(ErrorConstants.ACERVO_DOCUMENTAL_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (acervoDocumentalRepository.findById(tombo.getAcervoDocumental().getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.ACERVO_DOCUMENTAL_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if (tombo.getNumeroTombo() == null) {
            throw new CustomException(ErrorConstants.NUMERO_TOMBO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (tombo.getPeriodo() == null || tombo.getPeriodo().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.PERIODO_REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }
}
