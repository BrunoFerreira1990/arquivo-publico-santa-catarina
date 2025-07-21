package com.example.apesc.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Genres {

    MALE("Male"),
    FEMALE("Female");

    private String displayName;
}
