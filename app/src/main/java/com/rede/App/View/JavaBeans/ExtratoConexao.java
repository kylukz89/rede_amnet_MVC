package com.rede.App.View.JavaBeans;

import java.util.ArrayList;

/**
 *  Entidade responsável pelo extrato de conexão de um plano
 */
public class ExtratoConexao {

    private String periodo;
    private String pppoe;
    private String tempo;
    private String trafegoDown;
    private String trafegoUp;
    private String totalConexoes;

    private ArrayList dadosPeriodo;
    private ArrayList dadosPPPoE;
    private ArrayList dadosTempo;
    private ArrayList dadosTrafegoDown;
    private ArrayList dadosTrafegoUp;

    public ArrayList getDadosPeriodo() {
        return dadosPeriodo;
    }

    public void setDadosPeriodo(ArrayList dadosPeriodo) {
        this.dadosPeriodo = dadosPeriodo;
    }

    public ArrayList getDadosPPPoE() {
        return dadosPPPoE;
    }

    public void setDadosPPPoE(ArrayList dadosPPPoE) {
        this.dadosPPPoE = dadosPPPoE;
    }

    public ArrayList getDadosTempo() {
        return dadosTempo;
    }

    public void setDadosTempo(ArrayList dadosTempo) {
        this.dadosTempo = dadosTempo;
    }

    public ArrayList getDadosTrafegoDown() {
        return dadosTrafegoDown;
    }

    public void setDadosTrafegoDown(ArrayList dadosTrafegoDown) {
        this.dadosTrafegoDown = dadosTrafegoDown;
    }

    public ArrayList getDadosTrafegoUp() {
        return dadosTrafegoUp;
    }

    public void setDadosTrafegoUp(ArrayList dadosTrafegoUp) {
        this.dadosTrafegoUp = dadosTrafegoUp;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getPppoe() {
        return pppoe;
    }

    public void setPppoe(String pppoe) {
        this.pppoe = pppoe;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getTrafegoDown() {
        return trafegoDown;
    }

    public void setTrafegoDown(String trafegoDown) {
        this.trafegoDown = trafegoDown;
    }

    public String getTrafegoUp() {
        return trafegoUp;
    }

    public void setTrafegoUp(String trafegoUp) {
        this.trafegoUp = trafegoUp;
    }

    public String getTotalConexoes() {
        return totalConexoes;
    }

    public void setTotalConexoes(String totalConexoes) {
        this.totalConexoes = totalConexoes;
    }
}
