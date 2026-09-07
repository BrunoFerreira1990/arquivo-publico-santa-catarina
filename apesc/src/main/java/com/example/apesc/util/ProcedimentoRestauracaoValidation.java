package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.ProcedimentoRestauracao;
import com.example.apesc.repository.DiagnosticoRestauracaoRepository;
import com.example.apesc.repository.FuncionarioRepository;
import com.example.apesc.repository.ProcedimentoRestauracaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProcedimentoRestauracaoValidation {

    private final DiagnosticoRestauracaoRepository diagnosticoRestauracaoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public void validateSave(ProcedimentoRestauracao procedimento, ProcedimentoRestauracaoRepository procedimentoRepository) {
        validateBasicFields(procedimento);

        if (procedimentoRepository.existsByDiagnosticoRestauracaoId(procedimento.getDiagnosticoRestauracao().getId())) {
            throw new CustomException(ErrorConstants.DIAGNOSTICO_RESTAURACAO_JA_POSSUI_PROCEDIMENTO, HttpStatus.CONFLICT);
        }
    }

    public void validateUpdate(ProcedimentoRestauracao procedimento, ProcedimentoRestauracaoRepository procedimentoRepository) {
        if (procedimento.getId() == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (procedimentoRepository.findById(procedimento.getId().longValue()).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        validateBasicFields(procedimento);

        if (procedimentoRepository.existsByDiagnosticoRestauracaoIdAndIdNot(procedimento.getDiagnosticoRestauracao().getId(), procedimento.getId())) {
            throw new CustomException(ErrorConstants.DIAGNOSTICO_RESTAURACAO_JA_POSSUI_PROCEDIMENTO, HttpStatus.CONFLICT);
        }
    }

    public void validateDelete(Long id, ProcedimentoRestauracaoRepository procedimentoRepository) {
        if (id == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (procedimentoRepository.findById(id).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private void validateBasicFields(ProcedimentoRestauracao procedimento) {
        if (procedimento.getDiagnosticoRestauracao() == null || procedimento.getDiagnosticoRestauracao().getId() == null) {
            throw new CustomException(ErrorConstants.DIAGNOSTICO_RESTAURACAO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (diagnosticoRestauracaoRepository.findById(procedimento.getDiagnosticoRestauracao().getId().longValue()).isEmpty()) {
            throw new CustomException(ErrorConstants.DIAGNOSTICO_RESTAURACAO_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if (procedimento.getResponsavelRestauracao() == null || procedimento.getResponsavelRestauracao().getId() == null) {
            throw new CustomException(ErrorConstants.FUNCIONARIO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (funcionarioRepository.findById(procedimento.getResponsavelRestauracao().getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.FUNCIONARIO_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if (procedimento.getDataSaida() == null) {
            throw new CustomException(ErrorConstants.DATA_SAIDA_REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }
}
