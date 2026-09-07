package com.example.apesc.service.procedimentorestauracao;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.ProcedimentoRestauracao;
import com.example.apesc.repository.DiagnosticoRestauracaoRepository;
import com.example.apesc.repository.FuncionarioRepository;
import com.example.apesc.repository.ProcedimentoRestauracaoRepository;
import com.example.apesc.util.ProcedimentoRestauracaoValidation;

import java.util.List;

@Service
@AllArgsConstructor
public class ProcedimentoRestauracaoServiceImpl implements ProcedimentoRestauracaoService {

    private final ProcedimentoRestauracaoRepository procedimentoRestauracaoRepository;
    private final ProcedimentoRestauracaoValidation procedimentoRestauracaoValidation;
    private final DiagnosticoRestauracaoRepository diagnosticoRestauracaoRepository;
    private final FuncionarioRepository funcionarioRepository;

    @Transactional
    public ProcedimentoRestauracao save(ProcedimentoRestauracao procedimentoRestauracao) {
        procedimentoRestauracaoValidation.validateSave(procedimentoRestauracao, procedimentoRestauracaoRepository);
        rehydrateRelationships(procedimentoRestauracao);
        return procedimentoRestauracaoRepository.save(procedimentoRestauracao);
    }

    @Transactional(readOnly = true)
    public ProcedimentoRestauracao findById(Long id) {
        return procedimentoRestauracaoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public ProcedimentoRestauracao findByNumeroDocumento(Integer numeroDocumento) {
        return procedimentoRestauracaoRepository.findByDiagnosticoRestauracao_NumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new CustomException(ErrorConstants.NUMERO_DOCUMENTO_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<ProcedimentoRestauracao> findAll() {
        return procedimentoRestauracaoRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        procedimentoRestauracaoValidation.validateDelete(id, procedimentoRestauracaoRepository);
        procedimentoRestauracaoRepository.deleteById(id);
    }

    @Transactional
    public ProcedimentoRestauracao update(ProcedimentoRestauracao procedimentoRestauracao) {
        procedimentoRestauracaoValidation.validateUpdate(procedimentoRestauracao, procedimentoRestauracaoRepository);
        rehydrateRelationships(procedimentoRestauracao);
        return procedimentoRestauracaoRepository.save(procedimentoRestauracao);
    }

    // A validacao ja confirmou que diagnosticoRestauracao e responsavelRestauracao
    // existem; aqui so busca as instancias gerenciadas antes do save, pra nao mandar
    // pro Hibernate uma referencia transiente (new Entity(); setId()) numa associacao.
    private void rehydrateRelationships(ProcedimentoRestauracao procedimento) {
        if (procedimento.getDiagnosticoRestauracao() != null && procedimento.getDiagnosticoRestauracao().getId() != null) {
            procedimento.setDiagnosticoRestauracao(
                    diagnosticoRestauracaoRepository.findById(procedimento.getDiagnosticoRestauracao().getId().longValue()).orElse(null));
        }

        if (procedimento.getResponsavelRestauracao() != null && procedimento.getResponsavelRestauracao().getId() != null) {
            procedimento.setResponsavelRestauracao(
                    funcionarioRepository.findById(procedimento.getResponsavelRestauracao().getId()).orElse(null));
        }
    }

}
