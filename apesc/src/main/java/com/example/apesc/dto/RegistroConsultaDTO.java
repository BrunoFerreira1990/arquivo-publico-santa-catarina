package com.example.apesc.dto;

import com.example.apesc.model.RegistroConsulta;
import com.example.apesc.model.Pesquisador;
import com.example.apesc.model.AcervoDocumental;
import com.example.apesc.model.Funcionario;
import com.example.apesc.model.enums.TipoConsulta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroConsultaDTO {

    private Long id;
    private Long pesquisadorId;
    private LocalDate dataPesquisa;
    private TipoConsulta tipoConsulta;
    private Long acervoDocumentalId;
    private String periodo;
    private Integer quantidade;
    private Long funcionarioId;
    private LocalDateTime dataRegistro;
    private LocalDateTime dataAtualizacao;
    private Long funcionarioAtualizacaoId;

    public RegistroConsulta toEntity() {
        RegistroConsulta entity = new RegistroConsulta();
        entity.setId(this.id);
        
        if (this.pesquisadorId != null) {
            Pesquisador p = new Pesquisador();
            p.setId(this.pesquisadorId);
            entity.setPesquisador(p);
        }

        entity.setDataPesquisa(this.dataPesquisa);
        entity.setTipoConsulta(this.tipoConsulta);
        
        if (this.acervoDocumentalId != null) {
            AcervoDocumental acervo = new AcervoDocumental();
            acervo.setId(this.acervoDocumentalId);
            entity.setAcervoDocumental(acervo);
        }

        entity.setPeriodo(this.periodo);
        entity.setQuantidade(this.quantidade);
        
        if (this.funcionarioId != null) {
            Funcionario f = new Funcionario();
            f.setId(this.funcionarioId);
            entity.setFuncionario(f);
        }

        entity.setDataRegistro(this.dataRegistro);
        entity.setDataAtualizacao(this.dataAtualizacao);

        if (this.funcionarioAtualizacaoId != null) {
            Funcionario f = new Funcionario();
            f.setId(this.funcionarioAtualizacaoId);
            entity.setFuncionarioAtualizacao(f);
        }

        return entity;
    }

    public static RegistroConsultaDTO fromEntity(RegistroConsulta entity) {
        if (entity == null) return null;
        return new RegistroConsultaDTO(
            entity.getId(),
            entity.getPesquisador() != null ? entity.getPesquisador().getId() : null,
            entity.getDataPesquisa(),
            entity.getTipoConsulta(),
            entity.getAcervoDocumental() != null ? entity.getAcervoDocumental().getId() : null,
            entity.getPeriodo(),
            entity.getQuantidade(),
            entity.getFuncionario() != null ? entity.getFuncionario().getId() : null,
            entity.getDataRegistro(),
            entity.getDataAtualizacao(),
            entity.getFuncionarioAtualizacao() != null ? entity.getFuncionarioAtualizacao().getId() : null
        );
    }
}
