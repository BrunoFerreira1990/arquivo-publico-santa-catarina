package com.example.apesc.model.enums;

import lombok.Getter;

@Getter
public enum NivelEducacional {

    ENSINO_FUNDAMENTAL("Ensino Fundamental"),
    ENSINO_MEDIO("Ensino Médio"),
    ENSINO_SUPERIOR("Ensino Superior"),
    ESPECIALIZACAO("Especialização"),
    MESTRADO("Mestrado"),
    DOUTORADO("Doutorado");

    private String displayName;

    NivelEducacional(String displayName) {
        this.displayName = displayName;
    }

}
