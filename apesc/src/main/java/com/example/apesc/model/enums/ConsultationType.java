package com.example.apesc.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConsultationType {

    IN_PERSON("In Person"),
    REMOTE("Remote");

    private String displayName;
}
