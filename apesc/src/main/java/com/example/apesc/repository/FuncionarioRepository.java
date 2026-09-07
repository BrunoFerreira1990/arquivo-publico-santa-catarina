package com.example.apesc.repository;

import com.example.apesc.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    @Query("SELECT f FROM Funcionario f WHERE LOWER(f.setor) LIKE LOWER(CONCAT('%', :setor, '%'))")
    List<Funcionario> findBySetorContainingIgnoreCase(@Param("setor") String setor);

    boolean existsByNumeroMatricula(String numeroMatricula);

    boolean existsByNumeroMatriculaAndIdNot(String numeroMatricula, Long id);

    Optional<Funcionario> findByEmailIgnoreCase(String email);

    boolean existsBySenhaIsNotNull();

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

}
