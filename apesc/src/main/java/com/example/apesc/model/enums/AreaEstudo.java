package com.example.apesc.model.enums;

import lombok.Getter;

@Getter
public enum AreaEstudo {

    HISTORIA_POLITICA("História Política"),
    HISTORIA_ECONOMICA("História Econômica"),
    HISTORIA_SOCIAL("História Social"),
    HISTORIA_ADMINISTRATIVA("História Administrativa"),
    HISTORIA_REGIONAL("História Regional"),
    GENEALOGIA("Genealogia"),
    HISTORIA_BIOGRAFICA("História Biográfica"),
    AQUISICAO_TERRAS("Aquisição de Terras"),
    OUTROS_ASSUNTOS("Outros Assuntos");

    private String displayName;

    AreaEstudo(String displayName) {
        this.displayName = displayName;
    }

}
