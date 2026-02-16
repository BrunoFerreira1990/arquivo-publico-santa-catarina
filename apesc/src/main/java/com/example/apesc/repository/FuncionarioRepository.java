package com.example.apesc.repository;

import com.example.apesc.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Funcionario findByName(String name);

}
