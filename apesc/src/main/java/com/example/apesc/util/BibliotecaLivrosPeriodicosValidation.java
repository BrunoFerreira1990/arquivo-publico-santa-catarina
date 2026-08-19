package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.BibliotecaLivrosPeriodicos;
import com.example.apesc.repository.BibliotecaLivrosPeriodicosRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BibliotecaLivrosPeriodicosValidation {

    public void validateSave(BibliotecaLivrosPeriodicos livro,
                              TipoDocumentoRepository tipoDocumentoRepository) {
        validateBasicFields(livro, tipoDocumentoRepository);
    }

    public void validateUpdate(BibliotecaLivrosPeriodicos livro,
                                BibliotecaLivrosPeriodicosRepository livroRepository,
                                TipoDocumentoRepository tipoDocumentoRepository) {

        if (livro.getId() == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (livroRepository.findById(livro.getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        validateBasicFields(livro, tipoDocumentoRepository);
    }

    public void validateDelete(Long id, BibliotecaLivrosPeriodicosRepository livroRepository) {
        if (id == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (livroRepository.findById(id).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private void validateBasicFields(BibliotecaLivrosPeriodicos livro, TipoDocumentoRepository tipoDocumentoRepository) {
        if (livro.getTipoDocumento() == null || livro.getTipoDocumento().getId() == null) {
            throw new CustomException(ErrorConstants.TIPO_DOCUMENTO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (tipoDocumentoRepository.findById(livro.getTipoDocumento().getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.TIPO_DOCUMENTO_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if (livro.getTitulo() == null || livro.getTitulo().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.TITULO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (livro.getAutores() == null || livro.getAutores().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.AUTORES_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (livro.getEditora() == null || livro.getEditora().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.EDITORA_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (livro.getLocalizacao() == null || livro.getLocalizacao().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.LOCALIZACAO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (livro.getQuantidadeExemplar() == null) {
            throw new CustomException(ErrorConstants.QUANTIDADE_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (livro.getQuantidadeExemplar() <= 0) {
            throw new CustomException(ErrorConstants.QUANTIDADE_INVALIDA, HttpStatus.BAD_REQUEST);
        }
    }
}
