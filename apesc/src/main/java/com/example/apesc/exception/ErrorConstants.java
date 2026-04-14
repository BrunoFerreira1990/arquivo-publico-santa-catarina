package com.example.apesc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorConstants {

    DUPLICATE_NAME("Nome duplicado"),
    DUPLICATE_ABREVIACAO("Abreviação duplicada"),
    EMPTY_NAME("O nome não pode ser vazio"),
    EMPTY_ABREVIACAO("A abreviação não pode ser vazia"),
    INVALID_NAME("Nome do tipo de documento inválido"),
    NAME_NOT_FOUND("Nome não encontrado"),
    ABREVIACAO_NOT_FOUND("Abreviação não encontrada"),
    INVALID_ID("ID inválido"),
    ID_NOT_FOUND("ID não encontrado"),
    TIPO_DOCUMENTO_HAS_DOCUMENTS("Tipo de documento possui documentos associados e não pode ser deletado"),
    TIPO_DOCUMENTO_REQUIRED("Tipo de documento é obrigatório"),
    TIPO_DOCUMENTO_NOT_FOUND("Tipo de documento não encontrado"),
    ENTIDADE_PRODUTORA_REQUIRED("Entidade produtora é obrigatória"),
    ENTIDADE_PRODUTORA_NOT_FOUND("Entidade produtora não encontrada"),
    NATUREZA_TRANSACAO_REQUIRED("Natureza da transação é obrigatória"),
    QUANTIDADE_INVALIDA("Quantidade deve ser maior que zero");

    private final String description;

}
