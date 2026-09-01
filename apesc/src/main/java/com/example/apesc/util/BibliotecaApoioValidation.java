package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.BibliotecaApoio;
import com.example.apesc.repository.BibliotecaApoioRepository;
import com.example.apesc.repository.EntidadeProdutoraRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BibliotecaApoioValidation {

    public void validateSave(BibliotecaApoio apoio,
                              BibliotecaApoioRepository apoioRepository,
                              TipoDocumentoRepository tipoDocumentoRepository,
                              EntidadeProdutoraRepository entidadeProdutoraRepository) {
        validateBasicFields(apoio, tipoDocumentoRepository, entidadeProdutoraRepository);

        if (apoioRepository.existsByTipoDocumentoIdAndEntidadeProdutoraIdAndTituloAndPeriodo(
                apoio.getTipoDocumento().getId(), apoio.getEntidadeProdutora().getId(),
                apoio.getTitulo(), apoio.getPeriodo())) {
            throw new CustomException(ErrorConstants.BIBLIOTECA_APOIO_DUPLICADO, HttpStatus.CONFLICT);
        }
    }

    public void validateUpdate(BibliotecaApoio apoio,
                                BibliotecaApoioRepository apoioRepository,
                                TipoDocumentoRepository tipoDocumentoRepository,
                                EntidadeProdutoraRepository entidadeProdutoraRepository) {

        if (apoio.getId() == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (apoioRepository.findById(apoio.getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        validateBasicFields(apoio, tipoDocumentoRepository, entidadeProdutoraRepository);

        if (apoioRepository.existsByTipoDocumentoIdAndEntidadeProdutoraIdAndTituloAndPeriodoAndIdNot(
                apoio.getTipoDocumento().getId(), apoio.getEntidadeProdutora().getId(),
                apoio.getTitulo(), apoio.getPeriodo(), apoio.getId())) {
            throw new CustomException(ErrorConstants.BIBLIOTECA_APOIO_DUPLICADO, HttpStatus.CONFLICT);
        }
    }

    public void validateDelete(Long id, BibliotecaApoioRepository apoioRepository) {
        if (id == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (apoioRepository.findById(id).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private void validateBasicFields(BibliotecaApoio apoio,
                                      TipoDocumentoRepository tipoDocumentoRepository,
                                      EntidadeProdutoraRepository entidadeProdutoraRepository) {
        // Remove espacos em branco no inicio/fim antes de validar e persistir.
        // Ids externos (tipoDocumento, entidadeProdutora) e campos nao-texto
        // (quantidadeVolume, disponibilidade) ficam de fora.
        apoio.setTitulo(CommonUtils.trim(apoio.getTitulo()));
        apoio.setPeriodo(CommonUtils.trim(apoio.getPeriodo()));
        apoio.setIdentificador(CommonUtils.trim(apoio.getIdentificador()));
        apoio.setLocalizacao(CommonUtils.trim(apoio.getLocalizacao()));

        if (apoio.getTipoDocumento() == null || apoio.getTipoDocumento().getId() == null) {
            throw new CustomException(ErrorConstants.TIPO_DOCUMENTO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (tipoDocumentoRepository.findById(apoio.getTipoDocumento().getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.TIPO_DOCUMENTO_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if (apoio.getEntidadeProdutora() == null || apoio.getEntidadeProdutora().getId() == null) {
            throw new CustomException(ErrorConstants.ENTIDADE_PRODUTORA_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (entidadeProdutoraRepository.findById(apoio.getEntidadeProdutora().getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.ENTIDADE_PRODUTORA_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if (apoio.getTitulo() == null || apoio.getTitulo().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.TITULO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (apoio.getPeriodo() == null || apoio.getPeriodo().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.PERIODO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (apoio.getLocalizacao() == null || apoio.getLocalizacao().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.LOCALIZACAO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (apoio.getQuantidadeVolume() == null) {
            throw new CustomException(ErrorConstants.QUANTIDADE_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (apoio.getQuantidadeVolume() <= 0) {
            throw new CustomException(ErrorConstants.QUANTIDADE_INVALIDA, HttpStatus.BAD_REQUEST);
        }
    }
}
