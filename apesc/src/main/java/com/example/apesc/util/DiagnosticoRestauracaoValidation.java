package com.example.apesc.util;

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
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DiagnosticoRestauracaoValidation {

    private final FuncionarioRepository funcionarioRepository;
    private final AcervoDocumentalTomboRepository acervoDocumentalTomboRepository;
    private final AcervoDocumentalProcessosRepository acervoDocumentalProcessosRepository;
    private final AcervoCartograficoRepository acervoCartograficoRepository;
    private final AcervoIconograficoRepository acervoIconograficoRepository;
    private final BibliotecaLivrosPeriodicosRepository bibliotecaLivrosPeriodicosRepository;
    private final BibliotecaApoioRepository bibliotecaApoioRepository;

    public void validateSave(DiagnosticoRestauracao diagnostico, DiagnosticoRestauracaoRepository diagnosticoRepository) {
        validateBasicFields(diagnostico);

        if (diagnosticoRepository.existsByNumeroDocumento(diagnostico.getNumeroDocumento())) {
            throw new CustomException(ErrorConstants.NUMERO_DOCUMENTO_DUPLICADO, HttpStatus.CONFLICT);
        }
    }

    public void validateUpdate(DiagnosticoRestauracao diagnostico, DiagnosticoRestauracaoRepository diagnosticoRepository) {
        if (diagnostico.getId() == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (diagnosticoRepository.findById(diagnostico.getId().longValue()).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        validateBasicFields(diagnostico);

        if (diagnosticoRepository.existsByNumeroDocumentoAndIdNot(diagnostico.getNumeroDocumento(), diagnostico.getId())) {
            throw new CustomException(ErrorConstants.NUMERO_DOCUMENTO_DUPLICADO, HttpStatus.CONFLICT);
        }
    }

    public void validateDelete(Long id, DiagnosticoRestauracaoRepository diagnosticoRepository) {
        if (id == null) {
            throw new CustomException(ErrorConstants.INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        if (diagnosticoRepository.findById(id).isEmpty()) {
            throw new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private void validateBasicFields(DiagnosticoRestauracao diagnostico) {
        if (diagnostico.getNumeroDocumento() == null) {
            throw new CustomException(ErrorConstants.NUMERO_DOCUMENTO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        validateResponsavel(diagnostico);

        if (diagnostico.getDataDiagnostico() == null) {
            throw new CustomException(ErrorConstants.DATA_DIAGNOSTICO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        validatePeloMenosUmAcervo(diagnostico);
    }

    private void validateResponsavel(DiagnosticoRestauracao diagnostico) {
        if (diagnostico.getResponsavelRestauracao() == null || diagnostico.getResponsavelRestauracao().getId() == null) {
            throw new CustomException(ErrorConstants.FUNCIONARIO_REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (funcionarioRepository.findById(diagnostico.getResponsavelRestauracao().getId()).isEmpty()) {
            throw new CustomException(ErrorConstants.FUNCIONARIO_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    // "Pelo menos uma" das 6 FKs polimorficas de acervo precisa vir preenchida com um
    // ID existente. Diferente do RegistroConsultaItem (que exige exatamente 1 dos 6),
    // aqui o negocio permite mais de uma marcada ao mesmo tempo no mesmo diagnostico.
    private void validatePeloMenosUmAcervo(DiagnosticoRestauracao diagnostico) {
        boolean algumPreenchido = false;

        if (diagnostico.getAcervoDocumentalTombo() != null && diagnostico.getAcervoDocumentalTombo().getId() != null) {
            if (acervoDocumentalTomboRepository.findById(diagnostico.getAcervoDocumentalTombo().getId()).isEmpty()) {
                throw new CustomException(ErrorConstants.ACERVO_DOCUMENTAL_TOMBO_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            algumPreenchido = true;
        }

        if (diagnostico.getAcervoDocumentalProcessos() != null && diagnostico.getAcervoDocumentalProcessos().getId() != null) {
            if (acervoDocumentalProcessosRepository.findById(diagnostico.getAcervoDocumentalProcessos().getId()).isEmpty()) {
                throw new CustomException(ErrorConstants.ACERVO_DOCUMENTAL_PROCESSOS_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            algumPreenchido = true;
        }

        if (diagnostico.getAcervoCartografico() != null && diagnostico.getAcervoCartografico().getId() != null) {
            if (acervoCartograficoRepository.findById(diagnostico.getAcervoCartografico().getId()).isEmpty()) {
                throw new CustomException(ErrorConstants.ACERVO_CARTOGRAFICO_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            algumPreenchido = true;
        }

        if (diagnostico.getAcervoIconografico() != null && diagnostico.getAcervoIconografico().getId() != null) {
            if (acervoIconograficoRepository.findById(diagnostico.getAcervoIconografico().getId()).isEmpty()) {
                throw new CustomException(ErrorConstants.ACERVO_ICONOGRAFICO_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            algumPreenchido = true;
        }

        if (diagnostico.getBibliotecaLivrosPeriodicos() != null && diagnostico.getBibliotecaLivrosPeriodicos().getId() != null) {
            if (bibliotecaLivrosPeriodicosRepository.findById(diagnostico.getBibliotecaLivrosPeriodicos().getId()).isEmpty()) {
                throw new CustomException(ErrorConstants.BIBLIOTECA_LIVROS_PERIODICOS_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            algumPreenchido = true;
        }

        if (diagnostico.getBibliotecaApoio() != null && diagnostico.getBibliotecaApoio().getId() != null) {
            if (bibliotecaApoioRepository.findById(diagnostico.getBibliotecaApoio().getId()).isEmpty()) {
                throw new CustomException(ErrorConstants.BIBLIOTECA_APOIO_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            algumPreenchido = true;
        }

        if (!algumPreenchido) {
            throw new CustomException(ErrorConstants.DIAGNOSTICO_RESTAURACAO_ACERVO_REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }
}
