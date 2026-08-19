package com.example.apesc.repository;

import com.example.apesc.model.Permissoes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissoesRepository extends JpaRepository<Permissoes, Long> {

    List<Permissoes> findByNomeRegraIgnoreCase(String nomeRegra);

}
