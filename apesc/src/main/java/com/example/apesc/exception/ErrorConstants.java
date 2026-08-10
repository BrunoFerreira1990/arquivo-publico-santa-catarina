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
    QUANTIDADE_INVALIDA("Quantidade deve ser maior que zero"),
    PERIODO_REQUIRED("Período é obrigatório"),
    ESTANTE_REQUIRED("Estante é obrigatória"),
    QUANTIDADE_REQUIRED("Quantidade é obrigatória"),
    DUPLICATE_ACERVO("Já existe um registro no acervo documental com o mesmo tipo de documento, entidade produtora, entidade receptora e natureza de transação"),
    SAME_ENTIDADE_ID("Entidade produtora e receptora não podem ter o mesmo ID"),
    ENTIDADE_RECEPTORA_REQUIRED("Entidade receptora é obrigatória para esta natureza de transação"),
    NATUREZA_TRANSACAO_INVALIDA("Natureza de transação inválida para entidade receptora informada"),
    NOME_REQUIRED("Nome é obrigatório"),
    NOME_INCOMPLETE("Nome deve conter nome e sobrenome"),
    DATA_NASCIMENTO_REQUIRED("Data de nascimento é obrigatória"),
    GENERO_REQUIRED("Gênero é obrigatório"),
    NUMERO_MATRICULA_REQUIRED("Número de matrícula é obrigatório"),
    MATRICULA_DUPLICADA("Número de matrícula já existe"),
    CARGO_REQUIRED("Cargo é obrigatório"),
    SETOR_REQUIRED("Setor é obrigatório"),
    EMAIL_REQUIRED("Email é obrigatório"),
    EMAIL_INVALID("Email inválido");

    private final String description;

}
