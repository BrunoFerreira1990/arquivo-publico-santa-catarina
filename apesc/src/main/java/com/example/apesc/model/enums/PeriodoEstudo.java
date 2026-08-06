package com.example.apesc.model.enums;

import lombok.Getter;

@Getter
public enum PeriodoEstudo {

    COLONAL("Colonial"),
    IMPERIAL("Imperial"),
    REPUBLICANO("Republicano");

    private String displayName;

    PeriodoEstudo(String displayName) {
        this.displayName = displayName;
    }

}
