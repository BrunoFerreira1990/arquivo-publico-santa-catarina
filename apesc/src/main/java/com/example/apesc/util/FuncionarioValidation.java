package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.Funcionario;
import com.example.apesc.repository.FuncionarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class FuncionarioValidation {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioValidation(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public void validateSave(Funcionario funcionario) {
        validateNome(funcionario);
        validateDataNascimento(funcionario);
        validateGenero(funcionario);
        validateEmail(funcionario);
        validateNumeroMatricula(funcionario);
        validateCargo(funcionario);
        validateSetor(funcionario);
    }

    public void validateUpdate(Funcionario funcionario) {
        if (funcionario.getId() == null) {
            throw new CustomException(
                ErrorConstants.INVALID_ID,
                HttpStatus.BAD_REQUEST
            );
        }
        validateNome(funcionario);
        validateDataNascimento(funcionario);
        validateGenero(funcionario);
        validateEmail(funcionario);
        validateNumeroMatriculaForUpdate(funcionario);
        validateCargo(funcionario);
        validateSetor(funcionario);
    }

    private void validateNome(Funcionario funcionario) {
        if (funcionario.getNome() == null || funcionario.getNome().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.NOME_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }

        String[] nomeParts = funcionario.getNome().trim().split("\\s+");
        if (nomeParts.length < 2) {
            throw new CustomException(
                ErrorConstants.NOME_INCOMPLETE,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateDataNascimento(Funcionario funcionario) {
        if (funcionario.getDataNascimento() == null) {
            throw new CustomException(
                ErrorConstants.DATA_NASCIMENTO_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateGenero(Funcionario funcionario) {
        if (funcionario.getGenero() == null) {
            throw new CustomException(
                ErrorConstants.GENERO_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateEmail(Funcionario funcionario) {
        if (funcionario.getEmail() == null || funcionario.getEmail().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.EMAIL_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }

        if (!EMAIL_PATTERN.matcher(funcionario.getEmail()).matches()) {
            throw new CustomException(
                ErrorConstants.EMAIL_INVALID,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateNumeroMatricula(Funcionario funcionario) {
        if (funcionario.getNumeroMatricula() == null || funcionario.getNumeroMatricula().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.NUMERO_MATRICULA_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }

        if (funcionarioRepository.existsByNumeroMatricula(funcionario.getNumeroMatricula())) {
            throw new CustomException(
                ErrorConstants.MATRICULA_DUPLICADA,
                HttpStatus.CONFLICT
            );
        }
    }

    private void validateNumeroMatriculaForUpdate(Funcionario funcionario) {
        if (funcionario.getNumeroMatricula() == null || funcionario.getNumeroMatricula().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.NUMERO_MATRICULA_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }

        if (funcionarioRepository.existsByNumeroMatriculaAndIdNot(
                funcionario.getNumeroMatricula(),
                funcionario.getId())) {
            throw new CustomException(
                ErrorConstants.MATRICULA_DUPLICADA,
                HttpStatus.CONFLICT
            );
        }
    }

    private void validateCargo(Funcionario funcionario) {
        if (funcionario.getCargo() == null || funcionario.getCargo().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.CARGO_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }

    private void validateSetor(Funcionario funcionario) {
        if (funcionario.getSetor() == null || funcionario.getSetor().trim().isEmpty()) {
            throw new CustomException(
                ErrorConstants.SETOR_REQUIRED,
                HttpStatus.BAD_REQUEST
            );
        }
    }
}
