package com.example.apesc.dto;

import com.example.apesc.model.AcervoCartografico;
import com.example.apesc.model.EntidadeProdutora;
import com.example.apesc.model.TipoDocumento;

public record AcervoCartograficoDTO(
        Long id,
        Long tipoDocumentoId,
        String tipoDocumentoNome,
        String codigoIdentificacao,
        String titulo,
        String dimensao,
        String localizacao,
        String localidade,
        String ano,
        Integer quantidadeVolume,
        Boolean disponibilidade,
        Long entidadeProdutoraId,
        String entidadeProdutoraNome
) {

    public static AcervoCartograficoDTO fromEntity(AcervoCartografico acervo) {
        return new AcervoCartograficoDTO(
                acervo.getId(),
                acervo.getTipoDocumento() != null ? acervo.getTipoDocumento().getId() : null,
                acervo.getTipoDocumento() != null ? acervo.getTipoDocumento().getNomeDocumento() : null,
                acervo.getCodigoIdentificacao(),
                acervo.getTitulo(),
                acervo.getDimensao(),
                acervo.getLocalizacao(),
                acervo.getLocalidade(),
                acervo.getAno(),
                acervo.getQuantidadeVolume(),
                acervo.getDisponibilidade(),
                acervo.getEntidadeProdutora() != null ? acervo.getEntidadeProdutora().getId() : null,
                acervo.getEntidadeProdutora() != null ? acervo.getEntidadeProdutora().getNome() : null
        );
    }

    public AcervoCartografico toEntity() {
        AcervoCartografico acervo = new AcervoCartografico();
        acervo.setId(this.id);

        if (this.tipoDocumentoId != null) {
            TipoDocumento tipoDocumento = new TipoDocumento();
            tipoDocumento.setId(this.tipoDocumentoId);
            acervo.setTipoDocumento(tipoDocumento);
        }

        acervo.setCodigoIdentificacao(this.codigoIdentificacao);
        acervo.setTitulo(this.titulo);
        acervo.setDimensao(this.dimensao);
        acervo.setLocalizacao(this.localizacao);
        acervo.setLocalidade(this.localidade);
        acervo.setAno(this.ano);
        acervo.setQuantidadeVolume(this.quantidadeVolume);
        acervo.setDisponibilidade(this.disponibilidade);

        if (this.entidadeProdutoraId != null) {
            EntidadeProdutora entidadeProdutora = new EntidadeProdutora();
            entidadeProdutora.setId(this.entidadeProdutoraId);
            acervo.setEntidadeProdutora(entidadeProdutora);
        }

        return acervo;
    }
}
