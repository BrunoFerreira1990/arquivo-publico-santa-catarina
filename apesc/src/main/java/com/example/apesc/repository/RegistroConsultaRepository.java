package com.example.apesc.repository;

import com.example.apesc.model.RegistroConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RegistroConsultaRepository extends JpaRepository<RegistroConsulta, Long>, JpaSpecificationExecutor<RegistroConsulta> {
    boolean existsByPesquisadorId(Long pesquisadorId);
}
