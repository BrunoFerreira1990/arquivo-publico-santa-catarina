package com.example.apesc.repository;

import com.example.apesc.model.RegistroConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RegistroConsultaRepository extends JpaRepository<RegistroConsulta, Long>, JpaSpecificationExecutor<RegistroConsulta> {
    boolean existsByPesquisadorId(Long pesquisadorId);

    // Usado pelo findById do service: sem isso, o "itens" (lazy) so seria carregado
    // dentro da transacao, e o mapeamento pro DTO (que roda no controller, fora dela
    // — open-in-view=false) quebraria com LazyInitializationException.
    @Query("SELECT DISTINCT rc FROM RegistroConsulta rc LEFT JOIN FETCH rc.itens WHERE rc.id = :id")
    Optional<RegistroConsulta> findByIdWithRelations(@Param("id") Long id);
}
