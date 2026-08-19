package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoIconografico;
import com.example.apesc.model.AcervoIconograficoAssuntos;
import com.example.apesc.repository.AcervoIconograficoAssuntosRepository;
import com.example.apesc.repository.AcervoIconograficoRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AcervoIconograficoValidation {

    public void validateSave(AcervoIconografico acervo,
                              AcervoIconograficoRepository acervoRepository,
                              TipoDocumentoRepository tipoDocumentoRepository,
                              AcervoIconograficoAssuntosRepository assuntosRepository) {

        validateBasicFields(acervo, tipoDocumentoRepository, assuntosRepository);
        validateCodigoIdentificacaoDuplicado(acervo, acervoRepository, null);
    }

    public void validateUpdate(AcervoIconografico acervo,
                                AcervoIconograficoRepository acervoRepository,
                                TipoDocumentoRepository tipoDocumentoRepository,
                                AcervoIconograficoAssuntosRepository assuntosRepository) {

        if (acervo.getId() == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (acervoRepository.findById(acervo.getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        validateBasicFields(acervo, tipoDocumentoRepository, assuntosRepository);
        validateCodigoIdentificacaoDuplicado(acervo, acervoRepository, acervo.getId());
    }

    public void validateDelete(Long id, AcervoIconograficoRepository acervoRepository) {
        if (id == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (acervoRepository.findById(id).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private void validateCodigoIdentificacaoDuplicado(AcervoIconografico acervo,
                                                        AcervoIconograficoRepository acervoRepository,
                                                        Long excludeId) {
        boolean duplicado = excludeId == null
                ? acervoRepository.existsByCodigoIdentificacao(acervo.getCodigoIdentificacao())
                : acervoRepository.existsByCodigoIdentificacaoAndIdNot(acervo.getCodigoIdentificacao(), excludeId);

        if (duplicado) {
            throw new CustomException(ErrorConstants.CODIGO_IDENTIFICACAO_DUPLICADO, HttpStatus.CONFLICT);
        }
    }

    private void validateBasicFields(AcervoIconografico acervo,
                                      TipoDocumentoRepository tipoDocumentoRepository,
                                      AcervoIconograficoAssuntosRepository assuntosRepository) {
        if (acervo.getTipoDocumento() == null || acervo.getTipoDocumento().getId() == null) {
            throw new CustomException(ErrorConstants.TIPO_DOCUMENTO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (tipoDocumentoRepository.findById(acervo.getTipoDocumento().getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.TIPO_DOCUMENTO_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if (acervo.getCodigoIdentificacao() == null || acervo.getCodigoIdentificacao().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.CODIGO_IDENTIFICACAO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (acervo.getTitulo() == null || acervo.getTitulo().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.TITULO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (acervo.getLocalizacao() == null || acervo.getLocalizacao().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.LOCALIZACAO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (acervo.getAno() == null || acervo.getAno().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.ANO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        // pelo menos 1 assunto, e cada um precisa existir de verdade no catalogo
        if (acervo.getAssuntos() == null || acervo.getAssuntos().isEmpty()) {
            throw new CustomException(ErrorConstants.ASSUNTO_ICONOGRAFICO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        for (AcervoIconograficoAssuntos assunto : acervo.getAssuntos()) {
            if (assunto.getId() == null || assuntosRepository.findById(assunto.getId()).isEmpty()) {
                throw new CustomException(ErrorConstants.ASSUNTO_ICONOGRAFICO_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        }
    }
}
