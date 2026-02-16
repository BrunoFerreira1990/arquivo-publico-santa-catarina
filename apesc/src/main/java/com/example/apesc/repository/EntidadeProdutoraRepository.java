package com.example.apesc.repository;

import com.example.apesc.model.EntidadeProdutora;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntidadeProdutoraRepository extends JpaRepository<EntidadeProdutora, Long> {

    List<EntidadeProdutora> findByName(String name);

}
