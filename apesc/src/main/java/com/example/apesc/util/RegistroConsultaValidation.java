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

    // ---------- duplicidade entre registros ----------

    private void validateDuplicado(RegistroConsulta registroConsulta, Long idAtual) {
        for (RegistroConsultaItem item : registroConsulta.getItens()) {
            boolean duplicado = existsDuplicadoDoItem(registroConsulta, item, idAtual);

            if (duplicado) {
                throw new CustomException(
                    ErrorConstants.REGISTRO_CONSULTA_DUPLICADO,
                    HttpStatus.CONFLICT
                );
            }
        }
    }

    private boolean existsDuplicadoDoItem(RegistroConsulta registroConsulta, RegistroConsultaItem item, Long idAtual) {
        Long pesquisadorId = registroConsulta.getPesquisador().getId();
        var dataPesquisa = registroConsulta.getDataPesquisa();
        var tipoConsulta = registroConsulta.getTipoConsulta();
        Integer quantidade = item.getQuantidade();
        String periodo = item.getPeriodo();

        if (item.getAcervoDocumental() != null) {
            return registroConsultaItemRepository.existsDuplicadoDocumental(
                    pesquisadorId, dataPesquisa, tipoConsulta, item.getAcervoDocumental().getId(), periodo, quantidade, idAtual);
        }
        if (item.getAcervoDocumentalProcessos() != null) {
            return registroConsultaItemRepository.existsDuplicadoDocumentalProcessos(
                    pesquisadorId, dataPesquisa, tipoConsulta, item.getAcervoDocumentalProcessos().getId(), periodo, quantidade, idAtual);
        }
        if (item.getAcervoIconografico() != null) {
            return registroConsultaItemRepository.existsDuplicadoIconografico(
                    pesquisadorId, dataPesquisa, tipoConsulta, item.getAcervoIconografico().getId(), periodo, quantidade, idAtual);
        }
        if (item.getAcervoCartografico() != null) {
            return registroConsultaItemRepository.existsDuplicadoCartografico(
                    pesquisadorId, dataPesquisa, tipoConsulta, item.getAcervoCartografico().getId(), periodo, quantidade, idAtual);
        }
        if (item.getBibliotecaLivrosPeriodicos() != null) {
            return registroConsultaItemRepository.existsDuplicadoBibliotecaLivrosPeriodicos(
                    pesquisadorId, dataPesquisa, tipoConsulta, item.getBibliotecaLivrosPeriodicos().getId(), periodo, quantidade, idAtual);
        }
        if (item.getBibliotecaApoio() != null) {
            return registroConsultaItemRepository.existsDuplicadoBibliotecaApoio(
                    pesquisadorId, dataPesquisa, tipoConsulta, item.getBibliotecaApoio().getId(), periodo, quantidade, idAtual);
        }
        // Nunca deveria chegar aqui — validateItens ja garantiu que todo item tem
        // exatamente 1 tipo de acervo antes de validateDuplicado ser chamado.
        return false;
    }

    // ---------- campos obrigatorios ----------

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

    // ---------- itens: pelo menos 1, exatamente 1 tipo por item, sem repetir no registro ----------
    // Excecao: se semConsulta = true, o registro existe mas nao envolveu consulta a
    // nenhum acervo, entao a exigencia de "pelo menos 1 item" nao se aplica. Se ainda
    // assim vierem itens junto (por engano ou nao), eles continuam sendo validados
    // normalmente — o flag so dispensa a obrigatoriedade, nao permite item invalido.

    private void validateItens(RegistroConsulta registroConsulta) {
        boolean semItens = registroConsulta.getItens() == null || registroConsulta.getItens().isEmpty();

        if (semItens) {
            if (Boolean.TRUE.equals(registroConsulta.getSemConsulta())) {
                return;
            }
            throw new CustomException(
                ErrorConstants.ITENS_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }

        // Um Set por tipo, pra barrar o mesmo acervo (do mesmo tipo) repetido no
        // mesmo registro — os 6 "espacos" de ID sao independentes entre si.
        Set<Long> documentaisVistos = new HashSet<>();
        Set<Long> documentalProcessosVistos = new HashSet<>();
        Set<Long> iconograficosVistos = new HashSet<>();
        Set<Long> cartograficosVistos = new HashSet<>();
        Set<Long> bibliotecaLivrosVistos = new HashSet<>();
        Set<Long> bibliotecaApoioVistos = new HashSet<>();

        for (RegistroConsultaItem item : registroConsulta.getItens()) {
            validateTipoDoItem(item);
            validatePeriodoDoItem(item.getPeriodo());
            validateQuantidadeDoItem(item.getQuantidade());

            if (item.getAcervoDocumental() != null) {
                validateSemRepeticao(documentaisVistos, item.getAcervoDocumental().getId(), ErrorConstants.ACERVO_DOCUMENTAL_DUPLICADO_NO_REGISTRO);
            } else if (item.getAcervoDocumentalProcessos() != null) {
                validateSemRepeticao(documentalProcessosVistos, item.getAcervoDocumentalProcessos().getId(), ErrorConstants.ACERVO_DOCUMENTAL_PROCESSOS_DUPLICADO_NO_REGISTRO);
            } else if (item.getAcervoIconografico() != null) {
                validateSemRepeticao(iconograficosVistos, item.getAcervoIconografico().getId(), ErrorConstants.ACERVO_ICONOGRAFICO_DUPLICADO_NO_REGISTRO);
            } else if (item.getAcervoCartografico() != null) {
                validateSemRepeticao(cartograficosVistos, item.getAcervoCartografico().getId(), ErrorConstants.ACERVO_CARTOGRAFICO_DUPLICADO_NO_REGISTRO);
            } else if (item.getBibliotecaLivrosPeriodicos() != null) {
                validateSemRepeticao(bibliotecaLivrosVistos, item.getBibliotecaLivrosPeriodicos().getId(), ErrorConstants.BIBLIOTECA_LIVROS_PERIODICOS_DUPLICADO_NO_REGISTRO);
            } else if (item.getBibliotecaApoio() != null) {
                validateSemRepeticao(bibliotecaApoioVistos, item.getBibliotecaApoio().getId(), ErrorConstants.BIBLIOTECA_APOIO_DUPLICADO_NO_REGISTRO);
            }
        }
    }

    private void validateSemRepeticao(Set<Long> idsVistos, Long id, ErrorConstants erroSeDuplicado) {
        if (!idsVistos.add(id)) {
            throw new CustomException(erroSeDuplicado, HttpStatus.BAD_REQUEST);
        }
    }

    // Cada item precisa referenciar exatamente 1 dos 6 tipos de acervo — nem 0, nem
    // mais de 1. Isso espelha, no nivel de validacao de negocio, o @Check que a
    // entidade RegistroConsultaItem tem no banco.
    private void validateTipoDoItem(RegistroConsultaItem item) {
        int quantosTipos = 0;
        quantosTipos += temId(item.getAcervoDocumental() != null ? item.getAcervoDocumental().getId() : null) ? 1 : 0;
        quantosTipos += temId(item.getAcervoDocumentalProcessos() != null ? item.getAcervoDocumentalProcessos().getId() : null) ? 1 : 0;
        quantosTipos += temId(item.getAcervoIconografico() != null ? item.getAcervoIconografico().getId() : null) ? 1 : 0;
        quantosTipos += temId(item.getAcervoCartografico() != null ? item.getAcervoCartografico().getId() : null) ? 1 : 0;
        quantosTipos += temId(item.getBibliotecaLivrosPeriodicos() != null ? item.getBibliotecaLivrosPeriodicos().getId() : null) ? 1 : 0;
        quantosTipos += temId(item.getBibliotecaApoio() != null ? item.getBibliotecaApoio().getId() : null) ? 1 : 0;

        if (quantosTipos == 0) {
            throw new CustomException(ErrorConstants.ACERVO_ITEM_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (quantosTipos > 1) {
            throw new CustomException(ErrorConstants.ACERVO_ITEM_TIPO_AMBIGUO, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean temId(Long id) {
        return id != null;
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
