package com.rede.App.View.JavaBeans;


import java.util.ArrayList;

/**
 * Entidade com os dados de todos os vencimentos de planos
 *
 * @author Igor Maximo
 * @date   30/03/2020
 */
public class Vencimento {

    private ArrayList<Integer> dadosId;
    private ArrayList<Integer> dadosVencDia;
    private ArrayList<String> dadosFormaCobranca;
    private ArrayList<String> dadosCodVenc;
    private ArrayList<String> dadosRegra;

    public ArrayList<Integer> getDadosId() {
        return dadosId;
    }

    public void setDadosId(ArrayList<Integer> dadosId) {
        this.dadosId = dadosId;
    }

    public ArrayList<Integer> getDadosVencDia() {
        return dadosVencDia;
    }

    public void setDadosVencDia(ArrayList<Integer> dadosVencDia) {
        this.dadosVencDia = dadosVencDia;
    }

    public ArrayList<String> getDadosFormaCobranca() {
        return dadosFormaCobranca;
    }

    public void setDadosFormaCobranca(ArrayList<String> dadosFormaCobranca) {
        this.dadosFormaCobranca = dadosFormaCobranca;
    }

    public ArrayList<String> getDadosCodVenc() {
        return dadosCodVenc;
    }

    public void setDadosCodVenc(ArrayList<String> dadosCodVenc) {
        this.dadosCodVenc = dadosCodVenc;
    }

    public ArrayList<String> getDadosRegra() {
        return dadosRegra;
    }

    public void setDadosRegra(ArrayList<String> dadosRegra) {
        this.dadosRegra = dadosRegra;
    }
}
