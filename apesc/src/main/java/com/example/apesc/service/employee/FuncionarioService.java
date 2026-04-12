package com.example.apesc.service.employee;

import com.example.apesc.model.Funcionario;

public interface FuncionarioService {
    
    public Funcionario findByNome(String nome);

    public Funcionario save(Funcionario funcionario);

    public Funcionario update(Funcionario funcionario);

    public void delete(Long id);

}
