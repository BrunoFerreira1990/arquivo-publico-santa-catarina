package com.example.apesc.util;

import org.springframework.http.HttpStatus;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.TipoDocumento;
import com.example.apesc.repository.TipoDocumentoRepository;

public class TipoDocumentoValidation {

    public void validateSave(TipoDocumento tipoDocumento, TipoDocumentoRepository tipoDocumentoRepository) {
        
     if (tipoDocumento.getNomeDocumento() == null || tipoDocumento.getNomeDocumento().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.EMPTY_NAME, 
                HttpStatus.BAD_REQUEST
            );
        }

     if (!tipoDocumentoRepository.findByName(tipoDocumento.getNomeDocumento()).isEmpty()) {
            throw new CustomException(
                ErrorConstants.DUPLICATE_NAME, 
                HttpStatus.BAD_REQUEST
            );
        }
    }

    public void validateFindByName(String name, TipoDocumentoRepository tipoDocumentoRepository) {
        if (name == null || name.trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.EMPTY_NAME, 
                HttpStatus.BAD_REQUEST
            );
        }

        if (tipoDocumentoRepository.findByName(name).isEmpty()) {
            throw new CustomException(
                ErrorConstants.NAME_NOT_FOUND, 
                HttpStatus.NOT_FOUND
            );
        }
    }

    public void validateDelete(Long id, TipoDocumentoRepository tipoDocumentoRepository) {
        if (id == null) {
            throw new CustomException(
                ErrorConstants.INVALID_ID, 
                HttpStatus.BAD_REQUEST
            );
        }

        if (tipoDocumentoRepository.findById(id).isEmpty()) {
            throw new CustomException(
                ErrorConstants.ID_NOT_FOUND, 
                HttpStatus.NOT_FOUND
            );
        }
    }
}
