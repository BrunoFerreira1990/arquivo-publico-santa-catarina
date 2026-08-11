package com.example.apesc.service.employee;

import com.example.apesc.model.Funcionario;

import java.util.List;

public interface FuncionarioService {

    Funcionario save(Funcionario funcionario);

    Funcionario update(Funcionario funcionario);

    void delete(Long id);

    List<Funcionario> findBySetor(String setor);

}
