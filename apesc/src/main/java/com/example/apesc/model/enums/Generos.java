package com.example.apesc.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Generos {

    MASCULINO("Masculino"),
    FEMININO("Feminino");

    private String displayName;
}
