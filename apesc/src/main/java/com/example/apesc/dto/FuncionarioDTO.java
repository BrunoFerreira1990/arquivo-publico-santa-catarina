package com.example.apesc.dto;

import com.example.apesc.model.Funcionario;
import com.example.apesc.model.Permissoes;
import com.example.apesc.model.enums.Generos;
import com.fasterxml.jackson.annotation.JsonSetter;
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
    private Long permissoesId;
    private String permissoesNomeRegra;

    @JsonSetter("genero")
    public void setGenero(String genero) {
        this.genero = genero == null || genero.trim().isEmpty() ? null : Generos.valueOf(genero.toUpperCase());
    }

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

        if (this.permissoesId != null) {
            Permissoes permissoes = new Permissoes();
            permissoes.setId(this.permissoesId);
            entity.setPermissoes(permissoes);
        }

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
            entity.getSetor(),
            entity.getPermissoes() != null ? entity.getPermissoes().getId() : null,
            entity.getPermissoes() != null ? entity.getPermissoes().getNomeRegra() : null
        );
    }
}
