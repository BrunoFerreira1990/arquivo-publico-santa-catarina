package com.example.apesc.repository;

import com.example.apesc.model.AcervoCartografico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AcervoCartograficoRepository extends JpaRepository<AcervoCartografico, Long>, JpaSpecificationExecutor<AcervoCartografico> {

    boolean existsByCodigoIdentificacao(String codigoIdentificacao);

    boolean existsByCodigoIdentificacaoAndIdNot(String codigoIdentificacao, Long id);
}
