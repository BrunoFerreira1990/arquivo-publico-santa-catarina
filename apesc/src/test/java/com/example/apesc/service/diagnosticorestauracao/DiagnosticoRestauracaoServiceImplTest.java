package com.example.apesc.service.diagnosticorestauracao;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.AcervoDocumentalTombo;
import com.example.apesc.model.DiagnosticoRestauracao;
import com.example.apesc.model.Funcionario;
import com.example.apesc.repository.AcervoCartograficoRepository;
import com.example.apesc.repository.AcervoDocumentalProcessosRepository;
import com.example.apesc.repository.AcervoDocumentalTomboRepository;
import com.example.apesc.repository.AcervoIconograficoRepository;
import com.example.apesc.repository.BibliotecaApoioRepository;
import com.example.apesc.repository.BibliotecaLivrosPeriodicosRepository;
import com.example.apesc.repository.DiagnosticoRestauracaoRepository;
import com.example.apesc.repository.FuncionarioRepository;
import com.example.apesc.util.DiagnosticoRestauracaoValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class DiagnosticoRestauracaoServiceImplTest {

    @Mock
    private DiagnosticoRestauracaoRepository diagnosticoRestauracaoRepository;

    @Mock
    private DiagnosticoRestauracaoValidation diagnosticoRestauracaoValidation;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private AcervoDocumentalTomboRepository acervoDocumentalTomboRepository;

    @Mock
    private AcervoDocumentalProcessosRepository acervoDocumentalProcessosRepository;

    @Mock
    private AcervoCartograficoRepository acervoCartograficoRepository;

    @Mock
    private AcervoIconograficoRepository acervoIconograficoRepository;

    @Mock
    private BibliotecaLivrosPeriodicosRepository bibliotecaLivrosPeriodicosRepository;

    @Mock
    private BibliotecaApoioRepository bibliotecaApoioRepository;

    private DiagnosticoRestauracaoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DiagnosticoRestauracaoServiceImpl(
                diagnosticoRestauracaoRepository, diagnosticoRestauracaoValidation, funcionarioRepository,
                acervoDocumentalTomboRepository, acervoDocumentalProcessosRepository, acervoCartograficoRepository,
                acervoIconograficoRepository, bibliotecaLivrosPeriodicosRepository, bibliotecaApoioRepository);
    }

    private DiagnosticoRestauracao umDiagnostico() {
        DiagnosticoRestauracao diagnostico = new DiagnosticoRestauracao();
        diagnostico.setNumeroDocumento(90001);
        diagnostico.setDataDiagnostico(LocalDate.of(2026, 1, 10));

        Funcionario responsavel = new Funcionario();
        responsavel.setId(1L);
        diagnostico.setResponsavelRestauracao(responsavel);

        AcervoDocumentalTombo tombo = new AcervoDocumentalTombo();
        tombo.setId(2L);
        diagnostico.setAcervoDocumentalTombo(tombo);

        return diagnostico;
    }

    @Test
    void save_deveValidarERehidratarRelacionamentosAntesDeSalvar() {
        DiagnosticoRestauracao diagnostico = umDiagnostico();
        Funcionario responsavelCompleto = new Funcionario();
        responsavelCompleto.setId(1L);
        AcervoDocumentalTombo tomboCompleto = new AcervoDocumentalTombo();
        tomboCompleto.setId(2L);
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(responsavelCompleto));
        when(acervoDocumentalTomboRepository.findById(2L)).thenReturn(Optional.of(tomboCompleto));
        when(diagnosticoRestauracaoRepository.save(diagnostico)).thenAnswer(inv -> inv.getArgument(0));

        DiagnosticoRestauracao resultado = service.save(diagnostico);

        verify(diagnosticoRestauracaoValidation).validateSave(diagnostico, diagnosticoRestauracaoRepository);
        assertThat(resultado.getResponsavelRestauracao()).isEqualTo(responsavelCompleto);
        assertThat(resultado.getAcervoDocumentalTombo()).isEqualTo(tomboCompleto);
    }

    @Test
    void save_naoDeveRehidratarFksDeAcervoNaoPreenchidas() {
        DiagnosticoRestauracao diagnostico = umDiagnostico();
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(new Funcionario()));
        when(acervoDocumentalTomboRepository.findById(2L)).thenReturn(Optional.of(new AcervoDocumentalTombo()));
        when(diagnosticoRestauracaoRepository.save(diagnostico)).thenAnswer(inv -> inv.getArgument(0));

        service.save(diagnostico);

        verify(acervoDocumentalProcessosRepository, never()).findById(any());
        verify(acervoCartograficoRepository, never()).findById(any());
        verify(acervoIconograficoRepository, never()).findById(any());
        verify(bibliotecaLivrosPeriodicosRepository, never()).findById(any());
        verify(bibliotecaApoioRepository, never()).findById(any());
    }

    @Test
    void save_naoDeveChamarRepositorioQuandoValidacaoFalhar() {
        DiagnosticoRestauracao diagnostico = umDiagnostico();
        doThrow(new CustomException(ErrorConstants.NUMERO_DOCUMENTO_DUPLICADO, org.springframework.http.HttpStatus.CONFLICT))
                .when(diagnosticoRestauracaoValidation).validateSave(diagnostico, diagnosticoRestauracaoRepository);

        assertThatThrownBy(() -> service.save(diagnostico)).isInstanceOf(CustomException.class);

        verify(diagnosticoRestauracaoRepository, never()).save(any());
    }

    @Test
    void findById_deveDelegarParaRepositorio() {
        DiagnosticoRestauracao diagnostico = umDiagnostico();
        when(diagnosticoRestauracaoRepository.findById(1L)).thenReturn(Optional.of(diagnostico));

        assertThat(service.findById(1L)).isEqualTo(diagnostico);
    }

    @Test
    void findById_deveRetornarNuloQuandoNaoExiste() {
        when(diagnosticoRestauracaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(service.findById(99L)).isNull();
    }

    @Test
    void findByNumeroDocumento_deveDelegarParaRepositorio() {
        DiagnosticoRestauracao diagnostico = umDiagnostico();
        when(diagnosticoRestauracaoRepository.findByNumeroDocumento(90001)).thenReturn(Optional.of(diagnostico));

        assertThat(service.findByNumeroDocumento(90001)).isEqualTo(diagnostico);
    }

    @Test
    void findByNumeroDocumento_deveLancarNotFoundQuandoNaoExiste() {
        when(diagnosticoRestauracaoRepository.findByNumeroDocumento(99999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByNumeroDocumento(99999))
                .isInstanceOf(CustomException.class)
                .satisfies(ex -> assertThat(((CustomException) ex).getDescription()).isEqualTo(ErrorConstants.NUMERO_DOCUMENTO_NOT_FOUND));
    }

    @Test
    void findAll_deveDelegarParaRepositorio() {
        List<DiagnosticoRestauracao> esperado = List.of(umDiagnostico());
        when(diagnosticoRestauracaoRepository.findAll()).thenReturn(esperado);

        assertThat(service.findAll()).isEqualTo(esperado);
    }

    @Test
    void delete_deveValidarEDelegarParaRepositorio() {
        service.delete(1L);

        verify(diagnosticoRestauracaoValidation).validateDelete(1L, diagnosticoRestauracaoRepository);
        verify(diagnosticoRestauracaoRepository).deleteById(1L);
    }

    @Test
    void delete_naoDeveChamarRepositorioQuandoValidacaoFalhar() {
        doThrow(new CustomException(ErrorConstants.ID_NOT_FOUND, org.springframework.http.HttpStatus.NOT_FOUND))
                .when(diagnosticoRestauracaoValidation).validateDelete(99L, diagnosticoRestauracaoRepository);

        assertThatThrownBy(() -> service.delete(99L)).isInstanceOf(CustomException.class);

        verify(diagnosticoRestauracaoRepository, never()).deleteById(any());
    }

    @Test
    void update_deveValidarERehidratarAntesDeSalvar() {
        DiagnosticoRestauracao diagnostico = umDiagnostico();
        diagnostico.setId(1);
        Funcionario responsavelCompleto = new Funcionario();
        responsavelCompleto.setId(1L);
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(responsavelCompleto));
        when(acervoDocumentalTomboRepository.findById(2L)).thenReturn(Optional.of(new AcervoDocumentalTombo()));
        when(diagnosticoRestauracaoRepository.save(diagnostico)).thenAnswer(inv -> inv.getArgument(0));

        DiagnosticoRestauracao resultado = service.update(diagnostico);

        verify(diagnosticoRestauracaoValidation).validateUpdate(diagnostico, diagnosticoRestauracaoRepository);
        assertThat(resultado.getResponsavelRestauracao()).isEqualTo(responsavelCompleto);
    }
}
