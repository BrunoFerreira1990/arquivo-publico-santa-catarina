package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.RegistroConsulta;
import com.example.apesc.repository.RegistroConsultaRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@AllArgsConstructor
public class RegistroConsultaValidation {

    private final RegistroConsultaRepository registroConsultaRepository;

    public void validateSave(RegistroConsulta registroConsulta) {
        validateCamposObrigatorios(registroConsulta);
        validateDuplicadoNoMesmoDia(registroConsulta);
    }

    public void validateUpdate(RegistroConsulta registroConsulta) {
        if (registroConsulta.getId() == null) {
            throw new CustomException(
                ErrorConstants.INVALID_ID,
                HttpStatus.BAD_REQUEST
            );
        }
        validateCamposObrigatorios(registroConsulta);
    }

    private void validateDuplicadoNoMesmoDia(RegistroConsulta registroConsulta) {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicioDoDia = hoje.atStartOfDay();
        LocalDateTime fimDoDia = hoje.atTime(LocalTime.MAX);

        boolean duplicado = registroConsultaRepository.existsDuplicadoNoMesmoDia(
                registroConsulta.getPesquisador().getId(),
                registroConsulta.getDataPesquisa(),
                registroConsulta.getTipoConsulta(),
                registroConsulta.getAcervoDocumental().getId(),
                registroConsulta.getPeriodo(),
                registroConsulta.getQuantidade(),
                registroConsulta.getFuncionario().getId(),
                inicioDoDia,
                fimDoDia
        );

        if (duplicado) {
            throw new CustomException(
                ErrorConstants.REGISTRO_CONSULTA_DUPLICADO,
                HttpStatus.CONFLICT
            );
        }
    }

    private void validateCamposObrigatorios(RegistroConsulta registroConsulta) {
        validatePesquisador(registroConsulta);
        validateDataPesquisa(registroConsulta);
        validateTipoConsulta(registroConsulta);
        validateAcervoDocumental(registroConsulta);
        validatePeriodo(registroConsulta);
        validateQuantidade(registroConsulta);
        validateFuncionario(registroConsulta);
    }

    private void validatePesquisador(RegistroConsulta registroConsulta) {
        if (registroConsulta.getPesquisador() == null || registroConsulta.getPesquisador().getId() == null) {
            throw new CustomException(
                ErrorConstants.PESQUISADOR_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateDataPesquisa(RegistroConsulta registroConsulta) {
        if (registroConsulta.getDataPesquisa() == null) {
            throw new CustomException(
                ErrorConstants.DATA_PESQUISA_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateTipoConsulta(RegistroConsulta registroConsulta) {
        if (registroConsulta.getTipoConsulta() == null) {
            throw new CustomException(
                ErrorConstants.TIPO_CONSULTA_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateAcervoDocumental(RegistroConsulta registroConsulta) {
        if (registroConsulta.getAcervoDocumental() == null || registroConsulta.getAcervoDocumental().getId() == null) {
            throw new CustomException(
                ErrorConstants.ACERVO_DOCUMENTAL_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validatePeriodo(RegistroConsulta registroConsulta) {
        if (registroConsulta.getPeriodo() == null || registroConsulta.getPeriodo().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.PERIODO_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateQuantidade(RegistroConsulta registroConsulta) {
        if (registroConsulta.getQuantidade() == null) {
            throw new CustomException(
                ErrorConstants.QUANTIDADE_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateFuncionario(RegistroConsulta registroConsulta) {
        if (registroConsulta.getFuncionario() == null || registroConsulta.getFuncionario().getId() == null) {
            throw new CustomException(
                ErrorConstants.FUNCIONARIO_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }
}
