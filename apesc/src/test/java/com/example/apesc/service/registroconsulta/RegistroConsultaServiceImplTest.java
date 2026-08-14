package com.example.apesc.service.registroconsulta;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.RegistroConsulta;
import com.example.apesc.repository.RegistroConsultaRepository;
import com.example.apesc.util.RegistroConsultaValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistroConsultaServiceImplTest {

    @Mock
    private RegistroConsultaRepository registroConsultaRepository;

    @Mock
    private RegistroConsultaValidation registroConsultaValidation;

    private RegistroConsultaServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new RegistroConsultaServiceImpl(registroConsultaRepository, registroConsultaValidation);
    }

    @Test
    void save_deveValidarEDelegarParaORepositorio() {
        RegistroConsulta registro = new RegistroConsulta();
        RegistroConsulta salvo = new RegistroConsulta();
        salvo.setId(1L);
        when(registroConsultaRepository.save(registro)).thenReturn(salvo);

        RegistroConsulta resultado = service.save(registro);

        verify(registroConsultaValidation).validateSave(registro);
        verify(registroConsultaRepository).save(registro);
        assertThat(resultado).isEqualTo(salvo);
    }

    @Test
    void save_naoDevePreencherDataAtualizacaoIgnorandoOEnviadoNoPayload() {
        RegistroConsulta registro = new RegistroConsulta();
        registro.setDataAtualizacao(LocalDateTime.of(2099, 12, 31, 23, 59));
        when(registroConsultaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RegistroConsulta resultado = service.save(registro);

        assertThat(resultado.getDataAtualizacao()).isNull();
    }

    @Test
    void save_naoDeveChamarRepositorioQuandoValidacaoFalhar() {
        RegistroConsulta registro = new RegistroConsulta();
        doThrow(new RuntimeException("inválido")).when(registroConsultaValidation).validateSave(registro);

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> service.save(registro));

        verify(registroConsultaRepository, never()).save(any());
    }

    @Test
    void update_deveValidarEDelegarParaORepositorio() {
        RegistroConsulta existente = new RegistroConsulta();
        existente.setId(1L);
        existente.setDataRegistro(LocalDateTime.of(2026, 1, 1, 10, 0));
        when(registroConsultaRepository.findById(1L)).thenReturn(Optional.of(existente));

        RegistroConsulta registro = new RegistroConsulta();
        registro.setId(1L);
        when(registroConsultaRepository.save(registro)).thenReturn(registro);

        RegistroConsulta resultado = service.update(registro);

        verify(registroConsultaValidation).validateUpdate(registro);
        verify(registroConsultaRepository).save(registro);
        assertThat(resultado).isEqualTo(registro);
    }

    @Test
    void update_devePreservarDataRegistroOriginalIgnorandoOEnviadoNoPayload() {
        LocalDateTime dataRegistroOriginal = LocalDateTime.of(2026, 1, 1, 10, 0);
        RegistroConsulta existente = new RegistroConsulta();
        existente.setId(1L);
        existente.setDataRegistro(dataRegistroOriginal);
        when(registroConsultaRepository.findById(1L)).thenReturn(Optional.of(existente));

        RegistroConsulta registro = new RegistroConsulta();
        registro.setId(1L);
        registro.setDataRegistro(LocalDateTime.of(2099, 12, 31, 23, 59));
        when(registroConsultaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RegistroConsulta resultado = service.update(registro);

        assertThat(resultado.getDataRegistro()).isEqualTo(dataRegistroOriginal);
    }

    @Test
    void update_devePreencherDataAtualizacaoComOInstanteDaEdicao() {
        RegistroConsulta existente = new RegistroConsulta();
        existente.setId(1L);
        when(registroConsultaRepository.findById(1L)).thenReturn(Optional.of(existente));

        RegistroConsulta registro = new RegistroConsulta();
        registro.setId(1L);
        when(registroConsultaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        LocalDateTime antes = LocalDateTime.now();
        RegistroConsulta resultado = service.update(registro);
        LocalDateTime depois = LocalDateTime.now();

        assertThat(resultado.getDataAtualizacao())
                .isNotNull()
                .isBetween(antes, depois);
    }

    @Test
    void update_deveLancarNotFoundQuandoRegistroNaoExiste() {
        when(registroConsultaRepository.findById(99L)).thenReturn(Optional.empty());

        RegistroConsulta registro = new RegistroConsulta();
        registro.setId(99L);

        assertThat(
                org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> service.update(registro))
                        .getDescription()
        ).isEqualTo(ErrorConstants.ID_NOT_FOUND);

        verify(registroConsultaRepository, never()).save(any());
    }

    @Test
    void update_naoDeveChamarRepositorioQuandoValidacaoFalhar() {
        RegistroConsulta registro = new RegistroConsulta();
        registro.setId(1L);
        doThrow(new RuntimeException("inválido")).when(registroConsultaValidation).validateUpdate(registro);

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> service.update(registro));

        verify(registroConsultaRepository, never()).save(any());
    }
}
