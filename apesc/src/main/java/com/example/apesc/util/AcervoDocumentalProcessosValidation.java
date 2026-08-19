package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoDocumentalProcessos;
import com.example.apesc.repository.AcervoDocumentalProcessosRepository;
import com.example.apesc.repository.AcervoDocumentalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AcervoDocumentalProcessosValidation {

    public void validateSave(AcervoDocumentalProcessos processo,
                              AcervoDocumentalProcessosRepository processoRepository,
                              AcervoDocumentalRepository acervoDocumentalRepository) {

        validateBasicFields(processo, acervoDocumentalRepository);

        if (processoRepository.existsByAcervoDocumentalIdAndCaixaIdentificacao(
                processo.getAcervoDocumental().getId(), processo.getCaixaIdentificacao())) {
            throw new CustomException(ErrorConstants.PROCESSO_DUPLICADO, HttpStatus.CONFLICT);
        }
    }

    public void validateUpdate(AcervoDocumentalProcessos processo,
                                AcervoDocumentalProcessosRepository processoRepository,
                                AcervoDocumentalRepository acervoDocumentalRepository) {

        if (processo.getId() == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (processoRepository.findById(processo.getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        validateBasicFields(processo, acervoDocumentalRepository);

        if (processoRepository.existsByAcervoDocumentalIdAndCaixaIdentificacaoAndIdNot(
                processo.getAcervoDocumental().getId(), processo.getCaixaIdentificacao(), processo.getId())) {
            throw new CustomException(ErrorConstants.PROCESSO_DUPLICADO, HttpStatus.CONFLICT);
        }
    }

    public void validateDelete(Long id, AcervoDocumentalProcessosRepository processoRepository) {
        if (id == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (processoRepository.findById(id).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private void validateBasicFields(AcervoDocumentalProcessos processo, AcervoDocumentalRepository acervoDocumentalRepository) {
        if (processo.getAcervoDocumental() == null || processo.getAcervoDocumental().getId() == null) {
            throw new CustomException(ErrorConstants.ACERVO_DOCUMENTAL_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (acervoDocumentalRepository.findById(processo.getAcervoDocumental().getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.ACERVO_DOCUMENTAL_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if (processo.getCaixaIdentificacao() == null || processo.getCaixaIdentificacao().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.CAIXA_IDENTIFICACAO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (processo.getNomeProcesso() == null || processo.getNomeProcesso().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.NOME_PROCESSO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (processo.getLocalizacao() == null || processo.getLocalizacao().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.LOCALIZACAO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (processo.getIdentificacaoPasta() == null || processo.getIdentificacaoPasta().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.IDENTIFICACAO_PASTA_REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }
}
