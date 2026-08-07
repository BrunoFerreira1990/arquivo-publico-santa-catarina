package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoDocumental;
import com.example.apesc.model.enums.NaturezaTransacao;
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
        
        validateBasicFields(acervo, tipoDocumentoRepository, entidadeRepository);
        validateDuplicateAcervoForSave(acervo, acervoRepository);
        validateSameEntidadeId(acervo);
    }

    private void validateDuplicateAcervoForSave(AcervoDocumental acervo, 
                                                AcervoDocumentalRepository acervoRepository) {
        Long tipoDocumentoId = acervo.getTipoDocumento().getId();
        Long entidadeProdutoraId = acervo.getEntidadeProdutora().getId();
        NaturezaTransacao naturezaTransacao = acervo.getNaturezaTransacao();
        Long entidadeReceptoraId = acervo.getEntidadeReceptora() != null 
                                    ? acervo.getEntidadeReceptora().getId() 
                                    : null;

        boolean exists;
        if (entidadeReceptoraId != null) {
            exists = acervoRepository.existsByTipoDocumentoIdAndEntidadeProdutoraIdAndEntidadeReceptoraIdAndNaturezaTransacao(
                tipoDocumentoId,
                entidadeProdutoraId,
                entidadeReceptoraId,
                naturezaTransacao
            );
        } else {
            exists = acervoRepository.existsByTipoDocumentoIdAndEntidadeProdutoraIdAndNaturezaTransacao(
                tipoDocumentoId,
                entidadeProdutoraId,
                naturezaTransacao
            );
        }

        if (exists) {
            throw new CustomException(
                ErrorConstants.DUPLICATE_ACERVO,
                HttpStatus.CONFLICT
            );
        }
    }

    private void validateDuplicateAcervo(AcervoDocumental acervo, 
                                        AcervoDocumentalRepository acervoRepository,
                                        Long excludeId) {
        Long tipoDocumentoId = acervo.getTipoDocumento().getId();
        Long entidadeProdutoraId = acervo.getEntidadeProdutora().getId();
        NaturezaTransacao naturezaTransacao = acervo.getNaturezaTransacao();
        Long entidadeReceptoraId = acervo.getEntidadeReceptora() != null 
                                    ? acervo.getEntidadeReceptora().getId() 
                                    : null;

        boolean exists;
        if (entidadeReceptoraId != null) {
            exists = acervoRepository.existsByTipoDocumentoIdAndEntidadeProdutoraIdAndEntidadeReceptoraIdAndNaturezaTransacaoAndIdNot(
                tipoDocumentoId,
                entidadeProdutoraId,
                entidadeReceptoraId,
                naturezaTransacao,
                excludeId
            );
        } else {
            exists = acervoRepository.existsByTipoDocumentoIdAndEntidadeProdutoraIdAndNaturezaTransacaoAndIdNot(
                tipoDocumentoId,
                entidadeProdutoraId,
                naturezaTransacao,
                excludeId
            );
        }

        if (exists) {
            throw new CustomException(
                ErrorConstants.DUPLICATE_ACERVO,
                HttpStatus.CONFLICT
            );
        }
    }

    private void validateSameEntidadeId(AcervoDocumental acervo) {
        Long entidadeProdutoraId = acervo.getEntidadeProdutora().getId();
        Long entidadeReceptoraId = acervo.getEntidadeReceptora() != null 
                                    ? acervo.getEntidadeReceptora().getId() 
                                    : null;

        if (entidadeReceptoraId != null && entidadeProdutoraId.equals(entidadeReceptoraId)) {
            throw new CustomException(
                ErrorConstants.SAME_ENTIDADE_ID,
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

        validateBasicFields(acervo, tipoDocumentoRepository, entidadeRepository);
        validateDuplicateAcervo(acervo, acervoRepository, acervo.getId());
        validateSameEntidadeId(acervo);
    }

    private void validateBasicFields(AcervoDocumental acervo, 
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

        if (acervo.getNaturezaTransacao() == null) {
            throw new CustomException(
                ErrorConstants.NATUREZA_TRANSACAO_REQUIRED, 
                HttpStatus.BAD_REQUEST
            );
        }

        if (acervo.getPeriodo() == null || acervo.getPeriodo().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.PERIODO_REQUIRED, 
                HttpStatus.BAD_REQUEST
            );
        }

        if (acervo.getEstante() == null || acervo.getEstante().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.ESTANTE_REQUIRED, 
                HttpStatus.BAD_REQUEST
            );
        }

        if (acervo.getQuantidade() == null) {
            throw new CustomException(
                ErrorConstants.QUANTIDADE_REQUIRED, 
                HttpStatus.BAD_REQUEST
            );
        }

        if (acervo.getQuantidade() <= 0) {
            throw new CustomException(
                ErrorConstants.QUANTIDADE_INVALIDA, 
                HttpStatus.BAD_REQUEST
            );
        }

        validateEntidadeReceptoraRequired(acervo);
        validateNaturezaTransacaoWithReceptora(acervo);
    }

    private void validateNaturezaTransacaoWithReceptora(AcervoDocumental acervo) {
        if (acervo.getNaturezaTransacao() == null || acervo.getEntidadeReceptora() == null) {
            return;
        }

        boolean isInvalidNatureza = acervo.getNaturezaTransacao() == NaturezaTransacao.RECEBIDOS 
                                    || acervo.getNaturezaTransacao() == NaturezaTransacao.EXPEDIDOS;

        if (isInvalidNatureza) {
            throw new CustomException(
                ErrorConstants.NATUREZA_TRANSACAO_INVALIDA,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateEntidadeReceptoraRequired(AcervoDocumental acervo) {
        if (acervo.getNaturezaTransacao() == null) {
            return;
        }

        boolean requiresReceptora = acervo.getNaturezaTransacao() == NaturezaTransacao.RECEBIDOS_DE 
                                    || acervo.getNaturezaTransacao() == NaturezaTransacao.EXPEDIDOS_PARA;

        if (requiresReceptora && acervo.getEntidadeReceptora() == null) {
            throw new CustomException(
                ErrorConstants.ENTIDADE_RECEPTORA_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
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
