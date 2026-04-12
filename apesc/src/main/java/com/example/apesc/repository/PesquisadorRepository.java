package com.example.apesc.repository;

import com.example.apesc.model.Pesquisador;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PesquisadorRepository extends JpaRepository<Pesquisador, Long> {
    List<Pesquisador> findByNome(String nome);
}
