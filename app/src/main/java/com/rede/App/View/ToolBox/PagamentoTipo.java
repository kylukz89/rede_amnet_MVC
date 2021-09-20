package com.rede.App.View.ToolBox;

public enum PagamentoTipo {

    Credito(1),
    Debito(2),
    Boleto(3),
    CodBarras(4);

    public int tipo;
    PagamentoTipo(int id) {
        tipo = id;
    }
}
