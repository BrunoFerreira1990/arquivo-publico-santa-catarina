package com.example.apesc.model.enums;

import lombok.Getter;

@Getter
public enum Generos {

    MASCULINO("Masculino"),
    FEMININO("Feminino");

    private String displayName;

    Generos(String displayName) {
        this.displayName = displayName;
    }

}
