package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoIconografico;
import com.example.apesc.model.AcervoIconograficoAssuntos;
import com.example.apesc.model.AcervoIconograficoPersonalidades;
import com.example.apesc.repository.AcervoIconograficoAssuntosRepository;
import com.example.apesc.repository.AcervoIconograficoPersonalidadesRepository;
import com.example.apesc.repository.AcervoIconograficoRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AcervoIconograficoValidation {

    public void validateSave(AcervoIconografico acervo,
                              AcervoIconograficoRepository acervoRepository,
                              TipoDocumentoRepository tipoDocumentoRepository,
                              AcervoIconograficoAssuntosRepository assuntosRepository,
                              AcervoIconograficoPersonalidadesRepository personalidadesRepository) {

        validateBasicFields(acervo, tipoDocumentoRepository, assuntosRepository, personalidadesRepository);
        validateCodigoIdentificacaoDuplicado(acervo, acervoRepository, null);
    }

    public void validateUpdate(AcervoIconografico acervo,
                                AcervoIconograficoRepository acervoRepository,
                                TipoDocumentoRepository tipoDocumentoRepository,
                                AcervoIconograficoAssuntosRepository assuntosRepository,
                                AcervoIconograficoPersonalidadesRepository personalidadesRepository) {

        if (acervo.getId() == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (acervoRepository.findById(acervo.getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        validateBasicFields(acervo, tipoDocumentoRepository, assuntosRepository, personalidadesRepository);
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
                                      AcervoIconograficoAssuntosRepository assuntosRepository,
                                      AcervoIconograficoPersonalidadesRepository personalidadesRepository) {
        if (acervo.getTipoDocumento() == null || acervo.getTipoDocumento().getId() == null) {
            throw new CustomException(ErrorConstants.TIPO_DOCUMENTO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (tipoDocumentoRepository.findById(acervo.getTipoDocumento().getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.TIPO_DOCUMENTO_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        // Remove espacos em branco no inicio/fim antes de validar e persistir. Em
        // titulo e localidade, alem do trim, capitaliza so a primeira letra do texto
        // (o resto fica como foi digitado — nao e title case por palavra).
        acervo.setCodigoIdentificacao(CommonUtils.trim(acervo.getCodigoIdentificacao()));
        acervo.setTitulo(CommonUtils.capitalizeFirst(CommonUtils.trim(acervo.getTitulo())));
        acervo.setLocalizacao(CommonUtils.trim(acervo.getLocalizacao()));
        acervo.setLocalidade(CommonUtils.capitalizeFirst(CommonUtils.trim(acervo.getLocalidade())));
        acervo.setAno(CommonUtils.trim(acervo.getAno()));

        if (acervo.getCodigoIdentificacao() == null || acervo.getCodigoIdentificacao().isEmpty()) {
            throw new CustomException(ErrorConstants.CODIGO_IDENTIFICACAO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (acervo.getTitulo() == null || acervo.getTitulo().isEmpty()) {
            throw new CustomException(ErrorConstants.TITULO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (acervo.getLocalizacao() == null || acervo.getLocalizacao().isEmpty()) {
            throw new CustomException(ErrorConstants.LOCALIZACAO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (acervo.getAno() == null || acervo.getAno().isEmpty()) {
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

        // personalidades e opcional (nem toda foto tem personalidade identificada),
        // mas cada id informado precisa existir de fato no catalogo
        if (acervo.getPersonalidades() != null) {
            for (AcervoIconograficoPersonalidades personalidade : acervo.getPersonalidades()) {
                if (personalidade.getId() == null || personalidadesRepository.findById(personalidade.getId()).isEmpty()) {
                    throw new CustomException(ErrorConstants.PERSONALIDADE_ICONOGRAFICA_NOT_FOUND, HttpStatus.NOT_FOUND);
                }
            }
        }
    }
}
