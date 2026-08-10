package com.example.apesc.service.employee;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.apesc.model.Funcionario;
import com.example.apesc.repository.FuncionarioRepository;
import com.example.apesc.util.FuncionarioValidation;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FuncionarioServiceImpl implements FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final FuncionarioValidation funcionarioValidation;

    @Transactional
    public Funcionario save(Funcionario funcionario) {
        funcionarioValidation.validateSave(funcionario);
        return funcionarioRepository.save(funcionario);
    }

    @Transactional
    public Funcionario update(Funcionario funcionario) {
        funcionarioValidation.validateUpdate(funcionario);
        return funcionarioRepository.save(funcionario);
    }

    public void delete(Long id) {
        funcionarioRepository.deleteById(id);
    }

    public Funcionario findByNome(String nome) {
        return funcionarioRepository.findByNome(nome);
    }
    
}
