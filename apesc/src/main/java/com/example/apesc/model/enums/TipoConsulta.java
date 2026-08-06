package com.example.apesc.model.enums;

import lombok.Getter;

@Getter
public enum TipoConsulta {

    PRESENCIAL("Presencial"),
    REMOTO("Remoto");

    private String displayName;

    TipoConsulta(String displayName) {
        this.displayName = displayName;
    }

}
