package com.example.apesc.service.diagnosticorestauracao;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.DiagnosticoRestauracao;
import com.example.apesc.repository.AcervoCartograficoRepository;
import com.example.apesc.repository.AcervoDocumentalProcessosRepository;
import com.example.apesc.repository.AcervoDocumentalTomboRepository;
import com.example.apesc.repository.AcervoIconograficoRepository;
import com.example.apesc.repository.BibliotecaApoioRepository;
import com.example.apesc.repository.BibliotecaLivrosPeriodicosRepository;
import com.example.apesc.repository.DiagnosticoRestauracaoRepository;
import com.example.apesc.repository.FuncionarioRepository;
import com.example.apesc.util.DiagnosticoRestauracaoValidation;

import lombok.AllArgsConstructor;

import java.util.List;

@Service
@AllArgsConstructor
public class DiagnosticoRestauracaoServiceImpl implements DiagnosticoRestauracaoService {

    private final DiagnosticoRestauracaoRepository diagnosticoRestauracaoRepository;
    private final DiagnosticoRestauracaoValidation diagnosticoRestauracaoValidation;
    private final FuncionarioRepository funcionarioRepository;
    private final AcervoDocumentalTomboRepository acervoDocumentalTomboRepository;
    private final AcervoDocumentalProcessosRepository acervoDocumentalProcessosRepository;
    private final AcervoCartograficoRepository acervoCartograficoRepository;
    private final AcervoIconograficoRepository acervoIconograficoRepository;
    private final BibliotecaLivrosPeriodicosRepository bibliotecaLivrosPeriodicosRepository;
    private final BibliotecaApoioRepository bibliotecaApoioRepository;

    @Transactional
    public DiagnosticoRestauracao save(DiagnosticoRestauracao diagnosticoRestauracao) {
        diagnosticoRestauracaoValidation.validateSave(diagnosticoRestauracao, diagnosticoRestauracaoRepository);
        rehydrateRelationships(diagnosticoRestauracao);
        return diagnosticoRestauracaoRepository.save(diagnosticoRestauracao);
    }

    @Transactional(readOnly = true)
    public DiagnosticoRestauracao findById(Long id) {
        return diagnosticoRestauracaoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public DiagnosticoRestauracao findByNumeroDocumento(Integer numeroDocumento) {
        return diagnosticoRestauracaoRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new CustomException(ErrorConstants.NUMERO_DOCUMENTO_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<DiagnosticoRestauracao> findAll() {
        return diagnosticoRestauracaoRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        diagnosticoRestauracaoValidation.validateDelete(id, diagnosticoRestauracaoRepository);
        diagnosticoRestauracaoRepository.deleteById(id);
    }

    @Transactional
    public DiagnosticoRestauracao update(DiagnosticoRestauracao diagnosticoRestauracao) {
        diagnosticoRestauracaoValidation.validateUpdate(diagnosticoRestauracao, diagnosticoRestauracaoRepository);
        rehydrateRelationships(diagnosticoRestauracao);
        return diagnosticoRestauracaoRepository.save(diagnosticoRestauracao);
    }

    // A validacao ja confirmou que cada FK preenchida existe; aqui so busca a
    // instancia gerenciada antes do save, pra nao mandar pro Hibernate uma
    // referencia transiente (new Entity(); setId()) numa associacao ManyToOne.
    private void rehydrateRelationships(DiagnosticoRestauracao diagnostico) {
        if (diagnostico.getResponsavelRestauracao() != null && diagnostico.getResponsavelRestauracao().getId() != null) {
            diagnostico.setResponsavelRestauracao(
                    funcionarioRepository.findById(diagnostico.getResponsavelRestauracao().getId()).orElse(null));
        }

        if (diagnostico.getAcervoDocumentalTombo() != null && diagnostico.getAcervoDocumentalTombo().getId() != null) {
            diagnostico.setAcervoDocumentalTombo(
                    acervoDocumentalTomboRepository.findById(diagnostico.getAcervoDocumentalTombo().getId()).orElse(null));
        }

        if (diagnostico.getAcervoDocumentalProcessos() != null && diagnostico.getAcervoDocumentalProcessos().getId() != null) {
            diagnostico.setAcervoDocumentalProcessos(
                    acervoDocumentalProcessosRepository.findById(diagnostico.getAcervoDocumentalProcessos().getId()).orElse(null));
        }

        if (diagnostico.getAcervoCartografico() != null && diagnostico.getAcervoCartografico().getId() != null) {
            diagnostico.setAcervoCartografico(
                    acervoCartograficoRepository.findById(diagnostico.getAcervoCartografico().getId()).orElse(null));
        }

        if (diagnostico.getAcervoIconografico() != null && diagnostico.getAcervoIconografico().getId() != null) {
            diagnostico.setAcervoIconografico(
                    acervoIconograficoRepository.findById(diagnostico.getAcervoIconografico().getId()).orElse(null));
        }

        if (diagnostico.getBibliotecaLivrosPeriodicos() != null && diagnostico.getBibliotecaLivrosPeriodicos().getId() != null) {
            diagnostico.setBibliotecaLivrosPeriodicos(
                    bibliotecaLivrosPeriodicosRepository.findById(diagnostico.getBibliotecaLivrosPeriodicos().getId()).orElse(null));
        }

        if (diagnostico.getBibliotecaApoio() != null && diagnostico.getBibliotecaApoio().getId() != null) {
            diagnostico.setBibliotecaApoio(
                    bibliotecaApoioRepository.findById(diagnostico.getBibliotecaApoio().getId()).orElse(null));
        }
    }

}
