package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.Pesquisador;
import com.example.apesc.repository.PesquisadorRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PesquisadorValidation {

    private final PesquisadorRepository pesquisadorRepository;

    public void validateSave(Pesquisador pesquisador) {
        validateNome(pesquisador);
        validateDataNascimento(pesquisador);
        validateGenero(pesquisador);
        validateEmail(pesquisador);
        validateCpf(pesquisador);
        validateNacionalidade(pesquisador);
        validateNumeroTelefone(pesquisador);
        validateLogradouro(pesquisador);
        validateNumeroCasa(pesquisador);
        validateBairro(pesquisador);
        validateCidade(pesquisador);
        validateEstado(pesquisador);
        validateCep(pesquisador);
        validateNivelEducacional(pesquisador);
        validateProfissao(pesquisador);
        validateAssuntoPesquisa(pesquisador);
        validateFinalidadePesquisa(pesquisador);
        validatePeriodoEstudo(pesquisador);
        validateAreaEstudo(pesquisador);
    }

    public void validateUpdate(Pesquisador pesquisador) {
        if (pesquisador.getId() == null) {
            throw new CustomException(
                ErrorConstants.INVALID_ID,
                HttpStatus.BAD_REQUEST
            );
        }
        validateNome(pesquisador);
        validateDataNascimento(pesquisador);
        validateGenero(pesquisador);
        validateEmail(pesquisador);
        validateCpfForUpdate(pesquisador);
        validateNacionalidade(pesquisador);
        validateNumeroTelefone(pesquisador);
        validateLogradouro(pesquisador);
        validateNumeroCasa(pesquisador);
        validateBairro(pesquisador);
        validateCidade(pesquisador);
        validateEstado(pesquisador);
        validateCep(pesquisador);
        validateNivelEducacional(pesquisador);
        validateProfissao(pesquisador);
        validateAssuntoPesquisa(pesquisador);
        validateFinalidadePesquisa(pesquisador);
        validatePeriodoEstudo(pesquisador);
        validateAreaEstudo(pesquisador);
    }

    private void validateNome(Pesquisador pesquisador) {
        if (pesquisador.getNome() == null || pesquisador.getNome().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.NOME_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateDataNascimento(Pesquisador pesquisador) {
        if (pesquisador.getDataNascimento() == null) {
            throw new CustomException(
                ErrorConstants.DATA_NASCIMENTO_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateGenero(Pesquisador pesquisador) {
        if (pesquisador.getGenero() == null) {
            throw new CustomException(
                ErrorConstants.GENERO_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateEmail(Pesquisador pesquisador) {
        if (pesquisador.getEmail() != null && !pesquisador.getEmail().trim().isEmpty()) {
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            if (!pesquisador.getEmail().matches(emailRegex)) {
                throw new CustomException(
                    ErrorConstants.EMAIL_INVALID,
                    HttpStatus.BAD_REQUEST
                );
            }
        }
    }

    private void validateCpf(Pesquisador pesquisador) {
        if (pesquisador.getCpf() == null || pesquisador.getCpf().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.CPF_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }

        if (pesquisadorRepository.existsByCpf(pesquisador.getCpf())) {
            throw new CustomException(
                ErrorConstants.CPF_DUPLICADO,
                HttpStatus.CONFLICT
            );
        }
    }

    private void validateCpfForUpdate(Pesquisador pesquisador) {
        if (pesquisador.getCpf() == null || pesquisador.getCpf().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.CPF_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }

        if (pesquisadorRepository.existsByCpfAndIdNot(
                pesquisador.getCpf(),
                pesquisador.getId())) {
            throw new CustomException(
                ErrorConstants.CPF_DUPLICADO,
                HttpStatus.CONFLICT
            );
        }
    }

    private void validateNacionalidade(Pesquisador pesquisador) {
        if (pesquisador.getNacionalidade() == null) {
            throw new CustomException(
                ErrorConstants.NACIONALIDADE_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateNumeroTelefone(Pesquisador pesquisador) {
        if (pesquisador.getNumeroTelefone() == null || pesquisador.getNumeroTelefone().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.NUMERO_TELEFONE_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateLogradouro(Pesquisador pesquisador) {
        if (pesquisador.getLogradouro() == null || pesquisador.getLogradouro().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.LOGRADOURO_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateNumeroCasa(Pesquisador pesquisador) {
        if (pesquisador.getNumeroCasa() == null || pesquisador.getNumeroCasa().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.NUMERO_CASA_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateBairro(Pesquisador pesquisador) {
        if (pesquisador.getBairro() == null || pesquisador.getBairro().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.BAIRRO_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateCidade(Pesquisador pesquisador) {
        if (pesquisador.getCidade() == null || pesquisador.getCidade().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.CIDADE_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateEstado(Pesquisador pesquisador) {
        if (pesquisador.getEstado() == null) {
            throw new CustomException(
                ErrorConstants.ESTADO_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateCep(Pesquisador pesquisador) {
        if (pesquisador.getCep() == null || pesquisador.getCep().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.CEP_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateNivelEducacional(Pesquisador pesquisador) {
        if (pesquisador.getNivelEducacional() == null) {
            throw new CustomException(
                ErrorConstants.NIVEL_EDUCACIONAL_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateProfissao(Pesquisador pesquisador) {
        if (pesquisador.getProfissao() == null || pesquisador.getProfissao().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.PROFISSAO_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateAssuntoPesquisa(Pesquisador pesquisador) {
        if (pesquisador.getAssuntoPesquisa() == null || pesquisador.getAssuntoPesquisa().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.ASSUNTO_PESQUISA_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateFinalidadePesquisa(Pesquisador pesquisador) {
        if (pesquisador.getFinalidadePesquisa() == null || pesquisador.getFinalidadePesquisa().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.FINALIDADE_PESQUISA_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validatePeriodoEstudo(Pesquisador pesquisador) {
        if (pesquisador.getPeriodoEstudo() == null || pesquisador.getPeriodoEstudo().isEmpty()) {
            throw new CustomException(
                ErrorConstants.PERIODO_ESTUDO_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateAreaEstudo(Pesquisador pesquisador) {
        if (pesquisador.getAreaEstudo() == null || pesquisador.getAreaEstudo().isEmpty()) {
            throw new CustomException(
                ErrorConstants.AREA_ESTUDO_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }
}
