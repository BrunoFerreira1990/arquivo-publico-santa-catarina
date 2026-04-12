package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoDocumental;
import com.example.apesc.repository.AcervoDocumentalRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import com.example.apesc.repository.EntidadeProdutoraRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AcervoDocumentalValidation {

    public void validateSave(AcervoDocumental acervo, 
                           AcervoDocumentalRepository acervoRepository,
                           TipoDocumentoRepository tipoDocumentoRepository,
                           EntidadeProdutoraRepository entidadeRepository) {
        
        if (acervo.getTipoDocumento() == null || acervo.getTipoDocumento().getId() == null) {
            throw new CustomException(
                ErrorConstants.TIPO_DOCUMENTO_REQUIRED, 
                HttpStatus.BAD_REQUEST
            );
        }

        if (tipoDocumentoRepository.findById(acervo.getTipoDocumento().getId()).isEmpty()) {
            throw new CustomException(
                ErrorConstants.TIPO_DOCUMENTO_NOT_FOUND, 
                HttpStatus.NOT_FOUND
            );
        }

        if (acervo.getEntidadeProdutora() == null || acervo.getEntidadeProdutora().getId() == null) {
            throw new CustomException(
                ErrorConstants.ENTIDADE_PRODUTORA_REQUIRED, 
                HttpStatus.BAD_REQUEST
            );
        }

        if (entidadeRepository.findById(acervo.getEntidadeProdutora().getId()).isEmpty()) {
            throw new CustomException(
                ErrorConstants.ENTIDADE_PRODUTORA_NOT_FOUND, 
                HttpStatus.NOT_FOUND
            );
        }

        if (acervo.getNaturezaTransacao() == null || acervo.getNaturezaTransacao().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.NATUREZA_TRANSACAO_REQUIRED, 
                HttpStatus.BAD_REQUEST
            );
        }

        if (acervo.getQuantidade() != null && acervo.getQuantidade() <= 0) {
            throw new CustomException(
                ErrorConstants.QUANTIDADE_INVALIDA, 
                HttpStatus.BAD_REQUEST
            );
        }
    }

    public void validateUpdate(AcervoDocumental acervo, 
                            AcervoDocumentalRepository acervoRepository,
                            TipoDocumentoRepository tipoDocumentoRepository,
                            EntidadeProdutoraRepository entidadeRepository) {
        
        if (acervo.getId() == null) {
            throw new CustomException(
                ErrorConstants.INVALID_ID, 
                HttpStatus.BAD_REQUEST
            );
        }

        if (acervoRepository.findById(acervo.getId()).isEmpty()) {
            throw new CustomException(
                ErrorConstants.ID_NOT_FOUND, 
                HttpStatus.NOT_FOUND
            );
        }

        validateSave(acervo, acervoRepository, tipoDocumentoRepository, entidadeRepository);
    }

    public void validateDelete(Long id, AcervoDocumentalRepository acervoRepository) {
        if (id == null) {
            throw new CustomException(
                ErrorConstants.INVALID_ID, 
                HttpStatus.BAD_REQUEST
            );
        }

        if (acervoRepository.findById(id).isEmpty()) {
            throw new CustomException(
                ErrorConstants.ID_NOT_FOUND, 
                HttpStatus.NOT_FOUND
            );
        }
    }

    public void validateFindById(Long id, AcervoDocumentalRepository acervoRepository) {
        if (id == null) {
            throw new CustomException(
                ErrorConstants.INVALID_ID, 
                HttpStatus.BAD_REQUEST
            );
        }
    }

    public void validateFindByTipoDocumento(Long tipoDocumentoId, TipoDocumentoRepository tipoDocumentoRepository) {
        if (tipoDocumentoId == null) {
            throw new CustomException(
                ErrorConstants.INVALID_ID, 
                HttpStatus.BAD_REQUEST
            );
        }

        if (tipoDocumentoRepository.findById(tipoDocumentoId).isEmpty()) {
            throw new CustomException(
                ErrorConstants.TIPO_DOCUMENTO_NOT_FOUND, 
                HttpStatus.NOT_FOUND
            );
        }
    }
}
