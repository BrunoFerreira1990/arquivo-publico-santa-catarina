package com.example.apesc.dto;

import com.example.apesc.model.Pesquisador;
import com.example.apesc.model.enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PesquisadorDTO {

    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private Generos genero;
    private String email;
    
    private String cpf;
    private Nacionalidade nacionalidade;
    private String numeroTelefone;
    private String logradouro;
    private String numeroCasa;
    private String complemento;
    private String bairro;
    private String cidade;
    private Estados estado;
    private String cep;
    private NivelEducacional nivelEducacional;
    private String instituicaoEnsino;
    private String curso;
    private String profissao;
    private String assuntoPesquisa;
    private String finalidadePesquisa;
    private Set<PeriodoEstudo> periodoEstudo;
    private Set<AreaEstudo> areaEstudo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Pesquisador toEntity() {
        Pesquisador entity = new Pesquisador();
        entity.setId(this.id);
        entity.setNome(this.nome);
        entity.setDataNascimento(this.dataNascimento);
        entity.setGenero(this.genero);
        entity.setEmail(this.email);
        
        entity.setCpf(this.cpf);
        entity.setNacionalidade(this.nacionalidade);
        entity.setNumeroTelefone(this.numeroTelefone);
        entity.setLogradouro(this.logradouro);
        entity.setNumeroCasa(this.numeroCasa);
        entity.setComplemento(this.complemento);
        entity.setBairro(this.bairro);
        entity.setCidade(this.cidade);
        entity.setEstado(this.estado);
        entity.setCep(this.cep);
        entity.setNivelEducacional(this.nivelEducacional);
        entity.setInstituicaoEnsino(this.instituicaoEnsino);
        entity.setCurso(this.curso);
        entity.setProfissao(this.profissao);
        entity.setAssuntoPesquisa(this.assuntoPesquisa);
        entity.setFinalidadePesquisa(this.finalidadePesquisa);
        entity.setPeriodoEstudo(this.periodoEstudo == null ? new HashSet<>() : this.periodoEstudo);
        entity.setAreaEstudo(this.areaEstudo == null ? new HashSet<>() : this.areaEstudo);
        entity.setCriadoEm(this.criadoEm);
        entity.setAtualizadoEm(this.atualizadoEm);
        return entity;
    }

    public static PesquisadorDTO fromEntity(Pesquisador entity) {
        if (entity == null) return null;
        return new PesquisadorDTO(
            entity.getId(),
            entity.getNome(),
            entity.getDataNascimento(),
            entity.getGenero(),
            entity.getEmail(),
            entity.getCpf(),
            entity.getNacionalidade(),
            entity.getNumeroTelefone(),
            entity.getLogradouro(),
            entity.getNumeroCasa(),
            entity.getComplemento(),
            entity.getBairro(),
            entity.getCidade(),
            entity.getEstado(),
            entity.getCep(),
            entity.getNivelEducacional(),
            entity.getInstituicaoEnsino(),
            entity.getCurso(),
            entity.getProfissao(),
            entity.getAssuntoPesquisa(),
            entity.getFinalidadePesquisa(),
            entity.getPeriodoEstudo(),
            entity.getAreaEstudo(),
            entity.getCriadoEm(),
            entity.getAtualizadoEm()
        );
    }
}
