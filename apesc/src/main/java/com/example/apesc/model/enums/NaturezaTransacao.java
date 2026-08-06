package com.example.apesc.model.enums;

public enum NaturezaTransacao {
    RECEBIDOS_DE("recebidos de"),
    EXPEDIDOS_PARA("expedidos para");

    private final String transacao;

    NaturezaTransacao(String transacao) {
        this.transacao = transacao;
    }

    public String getTransacao() {
        return transacao;
    }
}
