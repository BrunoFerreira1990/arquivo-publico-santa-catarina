package com.example.apesc.service.funcionario;

import com.example.apesc.model.Funcionario;
import com.example.apesc.repository.FuncionarioRepository;
import com.example.apesc.util.FuncionarioValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FuncionarioServiceImplTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private FuncionarioValidation funcionarioValidation;

    @Mock
    private PasswordEncoder passwordEncoder;

    private FuncionarioServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new FuncionarioServiceImpl(funcionarioRepository, funcionarioValidation, passwordEncoder);
    }

    @Test
    void save_deveGravarHashDaSenhaEmVezDoTextoPuro() {
        doNothing().when(funcionarioValidation).validateSave(any());
        when(passwordEncoder.encode("senha12345")).thenReturn("HASH");
        when(funcionarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Funcionario entrada = new Funcionario();
        entrada.setSenha(" senha12345 ");

        Funcionario salvo = service.save(entrada);

        assertThat(salvo.getSenha()).isEqualTo("HASH");
    }

    @Test
    void update_semSenhaNova_devePreservarHashAtual() {
        doNothing().when(funcionarioValidation).validateUpdate(any());
        Funcionario existente = new Funcionario();
        existente.setId(5L);
        existente.setSenha("HASH_ANTIGO");
        when(funcionarioRepository.findById(5L)).thenReturn(Optional.of(existente));
        when(funcionarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Funcionario entrada = new Funcionario();
        entrada.setId(5L);
        entrada.setSenha(null);

        Funcionario atualizado = service.update(entrada);

        assertThat(atualizado.getSenha()).isEqualTo("HASH_ANTIGO");
    }

    @Test
    void update_comSenhaNova_deveRegravarHash() {
        doNothing().when(funcionarioValidation).validateUpdate(any());
        Funcionario existente = new Funcionario();
        existente.setId(5L);
        existente.setSenha("HASH_ANTIGO");
        when(funcionarioRepository.findById(5L)).thenReturn(Optional.of(existente));
        when(passwordEncoder.encode("novaSenha123")).thenReturn("HASH_NOVO");
        when(funcionarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Funcionario entrada = new Funcionario();
        entrada.setId(5L);
        entrada.setSenha("novaSenha123");

        ArgumentCaptor<Funcionario> captor = ArgumentCaptor.forClass(Funcionario.class);
        service.update(entrada);
        org.mockito.Mockito.verify(funcionarioRepository).save(captor.capture());

        assertThat(captor.getValue().getSenha()).isEqualTo("HASH_NOVO");
    }
}
