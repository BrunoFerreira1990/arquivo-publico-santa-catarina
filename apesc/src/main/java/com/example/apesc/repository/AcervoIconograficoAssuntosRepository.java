package com.example.apesc.repository;

import com.example.apesc.model.AcervoIconograficoAssuntos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AcervoIconograficoAssuntosRepository extends JpaRepository<AcervoIconograficoAssuntos, Long> {

    List<AcervoIconograficoAssuntos> findByAssuntosIgnoreCase(String assuntos);

}
