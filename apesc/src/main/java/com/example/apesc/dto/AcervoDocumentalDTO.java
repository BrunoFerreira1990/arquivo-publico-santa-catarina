package com.example.apesc.dto;

import com.example.apesc.model.AcervoDocumental;
import com.example.apesc.model.EntidadeProdutora;
import com.example.apesc.model.TipoDocumento;



public record AcervoDocumentalDTO(
        Long tipoDocumentoId,
        String tipoDocumentoNome,
        Long entidadeProdutoraId,
        String entidadeProdutoraNome,
        Long entidadeReceptoraId,
        String entidadeReceptoraNome,
        String naturezaTransacao,
        String periodo,
        String estante,
        Integer quantidade,
        Boolean disponibilidade
) {

    public static AcervoDocumentalDTO fromEntity(AcervoDocumental acervo) {
        return new AcervoDocumentalDTO(
                acervo.getTipoDocumento() != null ? acervo.getTipoDocumento().getId() : null,
                acervo.getTipoDocumento() != null ? acervo.getTipoDocumento().getNomeDocumento() : null,
                acervo.getEntidadeProdutora() != null ? acervo.getEntidadeProdutora().getId() : null,
                acervo.getEntidadeProdutora() != null ? acervo.getEntidadeProdutora().getNome() : null,
                acervo.getEntidadeReceptora() != null ? acervo.getEntidadeReceptora().getId() : null,
                acervo.getEntidadeReceptora() != null ? acervo.getEntidadeReceptora().getNome() : null,
                acervo.getNaturezaTransacao(),
                acervo.getPeriodo(),
                acervo.getEstante(),
                acervo.getQuantidade(),
                acervo.getDisponibilidade()
        );
    }

    public AcervoDocumental toEntity() {
        AcervoDocumental acervo = new AcervoDocumental();

        if (this.tipoDocumentoId != null) {
            TipoDocumento tipoDocumento = new TipoDocumento();
            tipoDocumento.setId(this.tipoDocumentoId);
            acervo.setTipoDocumento(tipoDocumento);
        }

        if (this.entidadeProdutoraId != null) {
            EntidadeProdutora produtora = new EntidadeProdutora();
            produtora.setId(this.entidadeProdutoraId);
            acervo.setEntidadeProdutora(produtora);
        }

        if (this.entidadeReceptoraId != null) {
            EntidadeProdutora receptora = new EntidadeProdutora();
            receptora.setId(this.entidadeReceptoraId);
            acervo.setEntidadeReceptora(receptora);
        }

        acervo.setNaturezaTransacao(this.naturezaTransacao);
        acervo.setPeriodo(this.periodo);
        acervo.setEstante(this.estante);
        acervo.setQuantidade(this.quantidade);
        acervo.setDisponibilidade(this.disponibilidade);

        // Note: diagnosticosRestauracao não é definido aqui pois é uma relação gerenciada pelo JPA

        return acervo;
    }
}
