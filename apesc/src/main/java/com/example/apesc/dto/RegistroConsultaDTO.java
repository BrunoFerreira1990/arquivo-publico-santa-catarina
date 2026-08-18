package com.example.apesc.dto;

import com.example.apesc.model.RegistroConsulta;
import com.example.apesc.model.Pesquisador;
import com.example.apesc.model.Funcionario;
import com.example.apesc.model.enums.TipoConsulta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroConsultaDTO {

    private Long id;
    private Long pesquisadorId;
    private LocalDate dataPesquisa;
    private TipoConsulta tipoConsulta;
    private List<RegistroConsultaItemDTO> itens;
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

        if (this.itens != null) {
            this.itens.forEach(itemDTO -> entity.addItem(itemDTO.toEntity()));
        }

        if (this.funcionarioId != null) {
            Funcionario f = new Funcionario();
            f.setId(this.funcionarioId);
            entity.setFuncionario(f);
        }

        // dataRegistro e dataAtualizacao nao vem do cliente: sao preenchidas
        // automaticamente pela auditoria do JPA (@CreatedDate/@LastModifiedDate).

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
            entity.getItens() != null
                ? entity.getItens().stream().map(RegistroConsultaItemDTO::fromEntity).collect(Collectors.toList())
                : null,
            entity.getFuncionario() != null ? entity.getFuncionario().getId() : null,
            entity.getDataRegistro(),
            entity.getDataAtualizacao(),
            entity.getFuncionarioAtualizacao() != null ? entity.getFuncionarioAtualizacao().getId() : null
        );
    }
}
