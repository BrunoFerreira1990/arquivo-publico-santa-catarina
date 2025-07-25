package com.example.apesc.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PeriodoEstudo {

    COLONAL("Colonial"),
    IMPERIAL("Imperial"),
    REPUBLICANO("Republicano");

    private String displayName;

}
