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
        validateDescricao(assunto);

        if (!repository.findByDescricaoIgnoreCase(assunto.getDescricao()).isEmpty()) {
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

        validateDescricao(assunto);

        java.util.List<AcervoIconograficoAssuntos> existentes = repository.findByDescricaoIgnoreCase(assunto.getDescricao());
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

    private void validateDescricao(AcervoIconograficoAssuntos assunto) {
        // Remove espacos em branco no inicio/fim antes de validar e persistir.
        assunto.setDescricao(CommonUtils.trim(assunto.getDescricao()));

        if (assunto.getDescricao() == null || assunto.getDescricao().isEmpty()) {
            throw new CustomException(ErrorConstants.ASSUNTO_REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }
}
