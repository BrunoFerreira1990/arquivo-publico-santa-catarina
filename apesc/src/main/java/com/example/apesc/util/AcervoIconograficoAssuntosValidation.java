package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoIconograficoAssuntos;
import com.example.apesc.repository.AcervoIconograficoAssuntosRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AcervoIconograficoAssuntosValidation {

    public void validateSave(AcervoIconograficoAssuntos assunto, AcervoIconograficoAssuntosRepository repository) {
        validateAssuntos(assunto);

        if (!repository.findByAssuntosIgnoreCase(assunto.getAssuntos()).isEmpty()) {
            throw new CustomException(ErrorConstants.ASSUNTO_DUPLICADO, HttpStatus.CONFLICT);
        }
    }

    public void validateUpdate(AcervoIconograficoAssuntos assunto, AcervoIconograficoAssuntosRepository repository) {
        if (assunto.getId() == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (repository.findById(assunto.getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        validateAssuntos(assunto);

        java.util.List<AcervoIconograficoAssuntos> existentes = repository.findByAssuntosIgnoreCase(assunto.getAssuntos());
        if (!existentes.isEmpty() && !existentes.get(0).getId().equals(assunto.getId())) {
            throw new CustomException(ErrorConstants.ASSUNTO_DUPLICADO, HttpStatus.CONFLICT);
        }
    }

    public void validateDelete(Long id, AcervoIconograficoAssuntosRepository repository) {
        if (id == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (repository.findById(id).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private void validateAssuntos(AcervoIconograficoAssuntos assunto) {
        if (assunto.getAssuntos() == null || assunto.getAssuntos().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.ASSUNTO_REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }
}
