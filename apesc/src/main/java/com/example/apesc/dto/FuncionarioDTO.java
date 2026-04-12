package com.example.apesc.dto;

import com.example.apesc.model.Funcionario;
import com.example.apesc.model.enums.Generos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioDTO {

    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private Generos genero;
    private String email;
    private String numeroMatricula;
    private String cargo;
    private String setor;

    public Funcionario toEntity() {
        Funcionario entity = new Funcionario();
        entity.setId(this.id);
        entity.setNome(this.nome);
        entity.setDataNascimento(this.dataNascimento);
        entity.setGenero(this.genero);
        entity.setEmail(this.email);
        entity.setNumeroMatricula(this.numeroMatricula);
        entity.setCargo(this.cargo);
        entity.setSetor(this.setor);
        return entity;
    }

    public static FuncionarioDTO fromEntity(Funcionario entity) {
        if (entity == null) return null;
        return new FuncionarioDTO(
            entity.getId(),
            entity.getNome(),
            entity.getDataNascimento(),
            entity.getGenero(),
            entity.getEmail(),
            entity.getNumeroMatricula(),
            entity.getCargo(),
            entity.getSetor()
        );
    }
}
