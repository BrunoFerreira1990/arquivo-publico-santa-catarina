package com.example.apesc.service.employee;

import org.springframework.stereotype.Service;

import com.example.apesc.model.Funcionario;
import com.example.apesc.repository.FuncionarioRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FuncionarioServiceImpl implements FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public Funcionario save(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    public Funcionario update(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    public void delete(Long id) {
        funcionarioRepository.deleteById(id);
    }

    public Funcionario findByName(String name) {
        return funcionarioRepository.findByName(name);
    }
    
}
