package com.example.apesc.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoConsulta {

    PRESENCIAL("Presencial"),
    REMOTO("Remoto");

    private String displayName;
}
