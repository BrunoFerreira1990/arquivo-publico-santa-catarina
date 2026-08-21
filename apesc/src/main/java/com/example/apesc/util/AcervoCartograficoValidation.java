package com.example.apesc.util;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoCartografico;
import com.example.apesc.repository.AcervoCartograficoRepository;
import com.example.apesc.repository.EntidadeProdutoraRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AcervoCartograficoValidation {

    public void validateSave(AcervoCartografico acervo,
                              AcervoCartograficoRepository acervoRepository,
                              TipoDocumentoRepository tipoDocumentoRepository,
                              EntidadeProdutoraRepository entidadeProdutoraRepository) {

        validateBasicFields(acervo, tipoDocumentoRepository, entidadeProdutoraRepository);
        validateCodigoIdentificacaoDuplicado(acervo, acervoRepository, null);
    }

    public void validateUpdate(AcervoCartografico acervo,
                                AcervoCartograficoRepository acervoRepository,
                                TipoDocumentoRepository tipoDocumentoRepository,
                                EntidadeProdutoraRepository entidadeProdutoraRepository) {

        if (acervo.getId() == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (acervoRepository.findById(acervo.getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        validateBasicFields(acervo, tipoDocumentoRepository, entidadeProdutoraRepository);
        validateCodigoIdentificacaoDuplicado(acervo, acervoRepository, acervo.getId());
    }

    // Busca o registro existente pra servir de base do merge de um PATCH (ver
    // AcervoCartograficoServiceImpl.update) — falha do mesmo jeito que validateUpdate
    // falharia se o id fosse invalido/inexistente, só que retornando a entidade.
    public AcervoCartografico fetchExistenteParaUpdate(Long id, AcervoCartograficoRepository acervoRepository) {
        if (id == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        return acervoRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public void validateDelete(Long id, AcervoCartograficoRepository acervoRepository) {
        if (id == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (acervoRepository.findById(id).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    public void validateFindById(Long id) {
        if (id == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateCodigoIdentificacaoDuplicado(AcervoCartografico acervo,
                                                        AcervoCartograficoRepository acervoRepository,
                                                        Long excludeId) {
        boolean duplicado = excludeId == null
                ? acervoRepository.existsByCodigoIdentificacao(acervo.getCodigoIdentificacao())
                : acervoRepository.existsByCodigoIdentificacaoAndIdNot(acervo.getCodigoIdentificacao(), excludeId);

        if (duplicado) {
            throw new CustomException(ErrorConstants.CODIGO_IDENTIFICACAO_DUPLICADO, HttpStatus.CONFLICT);
        }
    }

    private void validateBasicFields(AcervoCartografico acervo,
                                      TipoDocumentoRepository tipoDocumentoRepository,
                                      EntidadeProdutoraRepository entidadeProdutoraRepository) {
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

        if (acervo.getDimensao() == null || acervo.getDimensao().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.DIMENSAO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (acervo.getLocalizacao() == null || acervo.getLocalizacao().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.LOCALIZACAO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (acervo.getLocalidade() == null || acervo.getLocalidade().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.LOCALIDADE_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (acervo.getAno() == null || acervo.getAno().trim().isEmpty()) {
            throw new CustomException(ErrorConstants.ANO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (acervo.getQuantidadeVolume() == null) {
            throw new CustomException(ErrorConstants.QUANTIDADE_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (acervo.getQuantidadeVolume() <= 0) {
            throw new CustomException(ErrorConstants.QUANTIDADE_INVALIDA, HttpStatus.BAD_REQUEST);
        }

        if (acervo.getDisponibilidade() == null) {
            throw new CustomException(ErrorConstants.DISPONIBILIDADE_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (acervo.getEntidadeProdutora() == null || acervo.getEntidadeProdutora().getId() == null) {
            throw new CustomException(ErrorConstants.ENTIDADE_PRODUTORA_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (entidadeProdutoraRepository.findById(acervo.getEntidadeProdutora().getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.ENTIDADE_PRODUTORA_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }
}
