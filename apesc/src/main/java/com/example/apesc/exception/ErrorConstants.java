package com.example.apesc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorConstants {

    DUPLICATE_NAME("Nome duplicado"),
    EMPTY_NAME("O nome não pode ser vazio"),
    INVALID_NAME("Nome do tipo de documento inválido"),
    NAME_NOT_FOUND("Nome não encontrado");

    private final String description;

}
