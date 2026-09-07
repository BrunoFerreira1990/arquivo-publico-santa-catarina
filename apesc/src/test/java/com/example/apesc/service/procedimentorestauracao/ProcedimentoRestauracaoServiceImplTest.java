package com.example.apesc.service.procedimentorestauracao;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.DiagnosticoRestauracao;
import com.example.apesc.model.Funcionario;
import com.example.apesc.model.ProcedimentoRestauracao;
import com.example.apesc.repository.DiagnosticoRestauracaoRepository;
import com.example.apesc.repository.FuncionarioRepository;
import com.example.apesc.repository.ProcedimentoRestauracaoRepository;
import com.example.apesc.util.ProcedimentoRestauracaoValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcedimentoRestauracaoServiceImplTest {

    @Mock
    private ProcedimentoRestauracaoRepository procedimentoRestauracaoRepository;

    @Mock
    private ProcedimentoRestauracaoValidation procedimentoRestauracaoValidation;

    @Mock
    private DiagnosticoRestauracaoRepository diagnosticoRestauracaoRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    private ProcedimentoRestauracaoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProcedimentoRestauracaoServiceImpl(
                procedimentoRestauracaoRepository, procedimentoRestauracaoValidation,
                diagnosticoRestauracaoRepository, funcionarioRepository);
    }

    private ProcedimentoRestauracao umProcedimento() {
        ProcedimentoRestauracao procedimento = new ProcedimentoRestauracao();

        DiagnosticoRestauracao diagnostico = new DiagnosticoRestauracao();
        diagnostico.setId(10);
        procedimento.setDiagnosticoRestauracao(diagnostico);

        Funcionario restaurador = new Funcionario();
        restaurador.setId(1L);
        procedimento.setResponsavelRestauracao(restaurador);

        procedimento.setDataSaida(LocalDate.of(2026, 1, 25));

        return procedimento;
    }

    @Test
    void save_deveValidarERehidratarRelacionamentosAntesDeSalvar() {
        ProcedimentoRestauracao procedimento = umProcedimento();
        DiagnosticoRestauracao diagnosticoCompleto = new DiagnosticoRestauracao();
        diagnosticoCompleto.setId(10);
        Funcionario restauradorCompleto = new Funcionario();
        restauradorCompleto.setId(1L);
        when(diagnosticoRestauracaoRepository.findById(10L)).thenReturn(Optional.of(diagnosticoCompleto));
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(restauradorCompleto));
        when(procedimentoRestauracaoRepository.save(procedimento)).thenAnswer(inv -> inv.getArgument(0));

        ProcedimentoRestauracao resultado = service.save(procedimento);

        verify(procedimentoRestauracaoValidation).validateSave(procedimento, procedimentoRestauracaoRepository);
        assertThat(resultado.getDiagnosticoRestauracao()).isEqualTo(diagnosticoCompleto);
        assertThat(resultado.getResponsavelRestauracao()).isEqualTo(restauradorCompleto);
    }

    @Test
    void save_naoDeveChamarRepositorioQuandoValidacaoFalhar() {
        ProcedimentoRestauracao procedimento = umProcedimento();
        doThrow(new CustomException(ErrorConstants.DIAGNOSTICO_RESTAURACAO_JA_POSSUI_PROCEDIMENTO, HttpStatus.CONFLICT))
                .when(procedimentoRestauracaoValidation).validateSave(procedimento, procedimentoRestauracaoRepository);

        assertThatThrownBy(() -> service.save(procedimento)).isInstanceOf(CustomException.class);

        verify(procedimentoRestauracaoRepository, never()).save(any());
    }

    @Test
    void findById_deveDelegarParaRepositorio() {
        ProcedimentoRestauracao procedimento = umProcedimento();
        when(procedimentoRestauracaoRepository.findById(1L)).thenReturn(Optional.of(procedimento));

        assertThat(service.findById(1L)).isEqualTo(procedimento);
    }

    @Test
    void findById_deveRetornarNuloQuandoNaoExiste() {
        when(procedimentoRestauracaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.findById(99L)).isNull();
    }

    @Test
    void findByNumeroDocumento_deveDelegarParaRepositorio() {
        ProcedimentoRestauracao procedimento = umProcedimento();
        when(procedimentoRestauracaoRepository.findByDiagnosticoRestauracao_NumeroDocumento(90001)).thenReturn(Optional.of(procedimento));

        assertThat(service.findByNumeroDocumento(90001)).isEqualTo(procedimento);
    }

    @Test
    void findByNumeroDocumento_deveLancarNotFoundQuandoNaoExiste() {
        when(procedimentoRestauracaoRepository.findByDiagnosticoRestauracao_NumeroDocumento(99999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByNumeroDocumento(99999))
                .isInstanceOf(CustomException.class)
                .satisfies(ex -> assertThat(((CustomException) ex).getDescription()).isEqualTo(ErrorConstants.NUMERO_DOCUMENTO_NOT_FOUND));
    }

    @Test
    void findAll_deveDelegarParaRepositorio() {
        List<ProcedimentoRestauracao> esperado = List.of(umProcedimento());
        when(procedimentoRestauracaoRepository.findAll()).thenReturn(esperado);

        assertThat(service.findAll()).isEqualTo(esperado);
    }

    @Test
    void delete_deveValidarEDelegarParaRepositorio() {
        service.delete(1L);

        verify(procedimentoRestauracaoValidation).validateDelete(1L, procedimentoRestauracaoRepository);
        verify(procedimentoRestauracaoRepository).deleteById(1L);
    }

    @Test
    void delete_naoDeveChamarRepositorioQuandoValidacaoFalhar() {
        doThrow(new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND))
                .when(procedimentoRestauracaoValidation).validateDelete(99L, procedimentoRestauracaoRepository);

        assertThatThrownBy(() -> service.delete(99L)).isInstanceOf(CustomException.class);

        verify(procedimentoRestauracaoRepository, never()).deleteById(any());
    }

    @Test
    void update_deveValidarERehidratarAntesDeSalvar() {
        ProcedimentoRestauracao procedimento = umProcedimento();
        procedimento.setId(1);
        DiagnosticoRestauracao diagnosticoCompleto = new DiagnosticoRestauracao();
        diagnosticoCompleto.setId(10);
        when(diagnosticoRestauracaoRepository.findById(10L)).thenReturn(Optional.of(diagnosticoCompleto));
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(new Funcionario()));
        when(procedimentoRestauracaoRepository.save(procedimento)).thenAnswer(inv -> inv.getArgument(0));

        ProcedimentoRestauracao resultado = service.update(procedimento);

        verify(procedimentoRestauracaoValidation).validateUpdate(procedimento, procedimentoRestauracaoRepository);
        assertThat(resultado.getDiagnosticoRestauracao()).isEqualTo(diagnosticoCompleto);
    }
}
