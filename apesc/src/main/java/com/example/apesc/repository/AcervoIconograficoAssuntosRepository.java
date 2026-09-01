package com.example.apesc.repository;

import com.example.apesc.model.AcervoIconograficoAssuntos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AcervoIconograficoAssuntosRepository extends JpaRepository<AcervoIconograficoAssuntos, Long>, JpaSpecificationExecutor<AcervoIconograficoAssuntos> {

    List<AcervoIconograficoAssuntos> findByDescricaoIgnoreCase(String descricao);

}
