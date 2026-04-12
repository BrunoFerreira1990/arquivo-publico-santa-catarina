package com.example.apesc.dto;

import com.example.apesc.model.ProcedimentoRestauracao;
import com.example.apesc.model.DiagnosticoRestauracao;
import com.example.apesc.model.Funcionario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcedimentoRestauracaoDTO {

    private Integer id;
    private Integer diagnosticoRestauracaoId;
    private Long responsavelRestauracaoId;
    private LocalDate dataSaida;
    private String estadoConservacao;
    private Boolean manchasDeAgua;
    private Boolean fungos;
    private Boolean lama;
    private Boolean ferrugem;
    private Boolean buracos;
    private Boolean fitaAdesiva;
    private Boolean oxidacaoTinta;
    private Boolean insetos;
    private Boolean dobras;
    private Boolean gordura;
    private Boolean ondulacoes;
    private Boolean durex;
    private Boolean rasigosCortes;
    private Boolean sujidades;
    private Boolean cola;
    private Boolean perdaCapa;
    private Boolean perdaPigmento;
    private Boolean escurecimento;
    private Boolean queimaduras;
    private Boolean perdaFolhas;
    private String outros;
    private String desmonte;
    private String limpezaMecanica;
    private String banho;
    private String testeQuimico;
    private String reconstituicao;
    private Integer velatura;
    private Integer encolagem;
    private Integer reforco;
    private Integer carcela;
    private Integer restauracao;
    private Integer obturacao;
    private String observacoes;

    public ProcedimentoRestauracao toEntity() {
        ProcedimentoRestauracao entity = new ProcedimentoRestauracao();
        entity.setId(this.id);

        if (this.diagnosticoRestauracaoId != null) {
            DiagnosticoRestauracao diag = new DiagnosticoRestauracao();
            diag.setId(this.diagnosticoRestauracaoId);
            entity.setDiagnosticoRestauracao(diag);
        }

        if (this.responsavelRestauracaoId != null) {
            Funcionario func = new Funcionario();
            func.setId(this.responsavelRestauracaoId);
            entity.setResponsavelRestauracao(func);
        }

        entity.setDataSaida(this.dataSaida);
        entity.setEstadoConservacao(this.estadoConservacao);
        entity.setManchasDeAgua(this.manchasDeAgua);
        entity.setFungos(this.fungos);
        entity.setLama(this.lama);
        entity.setFerrugem(this.ferrugem);
        entity.setBuracos(this.buracos);
        entity.setFitaAdesiva(this.fitaAdesiva);
        entity.setOxidacaoTinta(this.oxidacaoTinta);
        entity.setInsetos(this.insetos);
        entity.setDobras(this.dobras);
        entity.setGordura(this.gordura);
        entity.setOndulacoes(this.ondulacoes);
        entity.setDurex(this.durex);
        entity.setRasigosCortes(this.rasigosCortes);
        entity.setSujidades(this.sujidades);
        entity.setCola(this.cola);
        entity.setPerdaCapa(this.perdaCapa);
        entity.setPerdaPigmento(this.perdaPigmento);
        entity.setEscurecimento(this.escurecimento);
        entity.setQueimaduras(this.queimaduras);
        entity.setPerdaFolhas(this.perdaFolhas);
        entity.setOutros(this.outros);
        entity.setDesmonte(this.desmonte);
        entity.setLimpezaMecanica(this.limpezaMecanica);
        entity.setBanho(this.banho);
        entity.setTesteQuimico(this.testeQuimico);
        entity.setReconstituicao(this.reconstituicao);
        entity.setVelatura(this.velatura);
        entity.setEncolagem(this.encolagem);
        entity.setReforco(this.reforco);
        entity.setCarcela(this.carcela);
        entity.setRestauracao(this.restauracao);
        entity.setObturacao(this.obturacao);
        entity.setObservacoes(this.observacoes);

        return entity;
    }

    public static ProcedimentoRestauracaoDTO fromEntity(ProcedimentoRestauracao entity) {
        if (entity == null) return null;
        return new ProcedimentoRestauracaoDTO(
            entity.getId(),
            entity.getDiagnosticoRestauracao() != null ? entity.getDiagnosticoRestauracao().getId() : null,
            entity.getResponsavelRestauracao() != null ? entity.getResponsavelRestauracao().getId() : null,
            entity.getDataSaida(),
            entity.getEstadoConservacao(),
            entity.getManchasDeAgua(),
            entity.getFungos(),
            entity.getLama(),
            entity.getFerrugem(),
            entity.getBuracos(),
            entity.getFitaAdesiva(),
            entity.getOxidacaoTinta(),
            entity.getInsetos(),
            entity.getDobras(),
            entity.getGordura(),
            entity.getOndulacoes(),
            entity.getDurex(),
            entity.getRasigosCortes(),
            entity.getSujidades(),
            entity.getCola(),
            entity.getPerdaCapa(),
            entity.getPerdaPigmento(),
            entity.getEscurecimento(),
            entity.getQueimaduras(),
            entity.getPerdaFolhas(),
            entity.getOutros(),
            entity.getDesmonte(),
            entity.getLimpezaMecanica(),
            entity.getBanho(),
            entity.getTesteQuimico(),
            entity.getReconstituicao(),
            entity.getVelatura(),
            entity.getEncolagem(),
            entity.getReforco(),
            entity.getCarcela(),
            entity.getRestauracao(),
            entity.getObturacao(),
            entity.getObservacoes()
        );
    }
}
