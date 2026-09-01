package com.example.apesc.repository;

import com.example.apesc.model.AcervoIconograficoPersonalidades;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AcervoIconograficoPersonalidadesRepository extends JpaRepository<AcervoIconograficoPersonalidades, Long>, JpaSpecificationExecutor<AcervoIconograficoPersonalidades> {

    List<AcervoIconograficoPersonalidades> findByNomeIgnoreCase(String nome);

}
