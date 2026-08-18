package com.example.apesc.util;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.RegistroConsulta;
import com.example.apesc.model.RegistroConsultaItem;
import com.example.apesc.repository.RegistroConsultaItemRepository;
import com.example.apesc.repository.RegistroConsultaRepository;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RegistroConsultaValidation {

    private final RegistroConsultaRepository registroConsultaRepository;
    private final RegistroConsultaItemRepository registroConsultaItemRepository;

    public void validateSave(RegistroConsulta registroConsulta) {
        validateCamposObrigatorios(registroConsulta);
        // create: nao ha ID proprio ainda, entao qualquer item identico ja existente bloqueia.
        validateDuplicado(registroConsulta, null);
    }

    public void validateUpdate(RegistroConsulta registroConsulta) {
        if (registroConsulta.getId() == null) {
            throw new CustomException(
                ErrorConstants.INVALID_ID,
                HttpStatus.BAD_REQUEST
            );
        }
        validateCamposObrigatorios(registroConsulta);
        // update: exclui os itens do proprio registro da busca — salvar as mesmas
        // informacoes de volta nele mesmo e permitido, so bloqueia se houver item
        // identico em OUTRO registro.
        validateDuplicado(registroConsulta, registroConsulta.getId());
    }

    public void validateDelete(Long id) {
        if (id == null) {
            throw new CustomException(
                ErrorConstants.INVALID_ID,
                HttpStatus.BAD_REQUEST
            );
        }

        if (registroConsultaRepository.findById(id).isEmpty()) {
            throw new CustomException(
                ErrorConstants.ID_NOT_FOUND,
                HttpStatus.NOT_FOUND
            );
        }
    }

    private void validateDuplicado(RegistroConsulta registroConsulta, Long idAtual) {
        for (RegistroConsultaItem item : registroConsulta.getItens()) {
            boolean duplicado = ehItemDocumental(item)
                    ? registroConsultaItemRepository.existsDuplicadoDocumental(
                            registroConsulta.getPesquisador().getId(),
                            registroConsulta.getDataPesquisa(),
                            registroConsulta.getTipoConsulta(),
                            item.getAcervoDocumental().getId(),
                            item.getPeriodo(),
                            item.getQuantidade(),
                            idAtual)
                    : registroConsultaItemRepository.existsDuplicadoCartografico(
                            registroConsulta.getPesquisador().getId(),
                            registroConsulta.getDataPesquisa(),
                            registroConsulta.getTipoConsulta(),
                            item.getAcervoCartografico().getId(),
                            item.getPeriodo(),
                            item.getQuantidade(),
                            idAtual);

            if (duplicado) {
                throw new CustomException(
                    ErrorConstants.REGISTRO_CONSULTA_DUPLICADO,
                    HttpStatus.CONFLICT
                );
            }
        }
    }

    private void validateCamposObrigatorios(RegistroConsulta registroConsulta) {
        validatePesquisador(registroConsulta);
        validateDataPesquisa(registroConsulta);
        validateTipoConsulta(registroConsulta);
        validateItens(registroConsulta);
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

    private void validateItens(RegistroConsulta registroConsulta) {
        if (registroConsulta.getItens() == null || registroConsulta.getItens().isEmpty()) {
            throw new CustomException(
                ErrorConstants.ITENS_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }

        // IDs ja vistos nesta mesma submissao, um conjunto por tipo — o mesmo
        // acervo_documental_id nao pode repetir entre itens, e o mesmo
        // acervo_cartografico_id tambem nao, mas os dois "espacos" sao
        // independentes entre si (a constraint unica do banco segue essa mesma logica).
        Set<Long> documentaisJaVistos = new HashSet<>();
        Set<Long> cartograficosJaVistos = new HashSet<>();

        for (RegistroConsultaItem item : registroConsulta.getItens()) {
            validateTipoDoItem(item);
            validatePeriodoDoItem(item.getPeriodo());
            validateQuantidadeDoItem(item.getQuantidade());

            if (ehItemDocumental(item)) {
                if (!documentaisJaVistos.add(item.getAcervoDocumental().getId())) {
                    throw new CustomException(
                        ErrorConstants.ACERVO_DOCUMENTAL_DUPLICADO_NO_REGISTRO,
                        HttpStatus.BAD_REQUEST
                    );
                }
            } else {
                if (!cartograficosJaVistos.add(item.getAcervoCartografico().getId())) {
                    throw new CustomException(
                        ErrorConstants.ACERVO_CARTOGRAFICO_DUPLICADO_NO_REGISTRO,
                        HttpStatus.BAD_REQUEST
                    );
                }
            }
        }
    }

    // Cada item precisa referenciar exatamente 1 tipo de acervo — nem 0, nem os 2 ao
    // mesmo tempo. Isso espelha, no nivel de validacao de negocio, o @Check que a
    // entidade RegistroConsultaItem tem no banco.
    private void validateTipoDoItem(RegistroConsultaItem item) {
        boolean temDocumental = item.getAcervoDocumental() != null && item.getAcervoDocumental().getId() != null;
        boolean temCartografico = item.getAcervoCartografico() != null && item.getAcervoCartografico().getId() != null;

        if (!temDocumental && !temCartografico) {
            throw new CustomException(
                ErrorConstants.ACERVO_ITEM_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }

        if (temDocumental && temCartografico) {
            throw new CustomException(
                ErrorConstants.ACERVO_ITEM_TIPO_AMBIGUO,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private boolean ehItemDocumental(RegistroConsultaItem item) {
        return item.getAcervoDocumental() != null && item.getAcervoDocumental().getId() != null;
    }

    private void validatePeriodoDoItem(String periodo) {
        if (periodo == null || periodo.trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.PERIODO_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateQuantidadeDoItem(Integer quantidade) {
        if (quantidade == null) {
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
