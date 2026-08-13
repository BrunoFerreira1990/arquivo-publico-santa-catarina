package com.example.apesc.service.employee;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.Funcionario;
import com.example.apesc.repository.FuncionarioRepository;
import com.example.apesc.util.FuncionarioValidation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        if (!funcionarioRepository.existsById(id)) {
            throw new CustomException(
                ErrorConstants.ID_NOT_FOUND,
                HttpStatus.NOT_FOUND
            );
        }
        funcionarioRepository.deleteById(id);
    }

    public List<Funcionario> findBySetor(String setor) {
        return funcionarioRepository.findBySetorContainingIgnoreCase(setor);
    }

    public List<Funcionario> findAll() {
        return funcionarioRepository.findAll();
    }
    
}
