package com.example.apesc.util;

import org.springframework.http.HttpStatus;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.TipoDocumento;
import com.example.apesc.repository.TipoDocumentoRepository;

public class TipoDocumentoValidation {

    public static void validateSave(TipoDocumento tipoDocumento, TipoDocumentoRepository tipoDocumentoRepository) {
        
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
}
