package com.rede.App.View.JavaBeans;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class Adesao {

    private String planoAdesaoId;
    private String planoAdesaoNome;
    private String planoAdesaoValor;
    private String planoAdesaoDownload;
    private String planoAdesaoUpload;
    private String planoAdesaoTecnologia;
    private String planoAdesaoCategoria;
    private String planoAdesaoInstalacao;
    private String planoAdesaoCodIspTelecom;
    private String planoAdesaoValorMensalidadePlano;
    private String categoria;
    private Intent intent;
    private Context ctx;
    // Para o módulo de alteração de planos do vendas online
    private ArrayList dadosPlanoAlteracaoId;
    private ArrayList dadosPlanoAlteracaoNome;
    private ArrayList dadosPlanoAlteracaoValor;
    private ArrayList dadosPlanoAlteracaoDownload;
    private ArrayList dadosPlanoAlteracaoUpload;
    private ArrayList dadosPlanoAlteracaoTecnologia;
    private ArrayList dadosPlanoAlteracaoCategoria;
    private ArrayList dadosPlanoAlteracaoInstalacao;
    private ArrayList dadosPlanoAlteracaoCodIspTelecom;

    public Adesao(
            String categoria,
            String planoAdesaoId,
            String planoAdesaoNome,
            String planoAdesaoValor,
            String planoAdesaoDownload,
            String planoAdesaoUpload,
            String planoAdesaoTecnologia,
            String planoAdesaoCategoria,
            String planoAdesaoInstalacao,
            String planoAdesaoCodIspTelecom,
            Intent intent,
            Context ctx) {
        this.categoria = categoria;
        this.planoAdesaoId = planoAdesaoId;
        this.planoAdesaoNome = planoAdesaoNome;
        this.planoAdesaoValor = planoAdesaoValor;
        this.planoAdesaoDownload = planoAdesaoDownload;
        this.planoAdesaoUpload = planoAdesaoUpload;
        this.planoAdesaoTecnologia = planoAdesaoTecnologia;
        this.planoAdesaoCategoria = planoAdesaoCategoria;
        this.planoAdesaoInstalacao = planoAdesaoInstalacao;
        this.planoAdesaoCodIspTelecom = planoAdesaoCodIspTelecom;
        this.intent = intent;
        this.ctx = ctx;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPlanoAdesaoId() {
        return planoAdesaoId;
    }

    public void setPlanoAdesaoId(String planoAdesaoId) {
        this.planoAdesaoId = planoAdesaoId;
    }

    public String getPlanoAdesaoNome() {
        return planoAdesaoNome;
    }

    public void setPlanoAdesaoNome(String planoAdesaoNome) {
        this.planoAdesaoNome = planoAdesaoNome;
    }

    public String getPlanoAdesaoValor() {
        return planoAdesaoValor;
    }

    public void setPlanoAdesaoValor(String planoAdesaoValor) {
        this.planoAdesaoValor = planoAdesaoValor;
    }

    public String getPlanoAdesaoDownload() {
        return planoAdesaoDownload;
    }

    public void setPlanoAdesaoDownload(String planoAdesaoDownload) {
        this.planoAdesaoDownload = planoAdesaoDownload;
    }

    public String getPlanoAdesaoUpload() {
        return planoAdesaoUpload;
    }

    public void setPlanoAdesaoUpload(String planoAdesaoUpload) {
        this.planoAdesaoUpload = planoAdesaoUpload;
    }

    public String getPlanoAdesaoTecnologia() {
        return planoAdesaoTecnologia;
    }

    public void setPlanoAdesaoTecnologia(String planoAdesaoTecnologia) {
        this.planoAdesaoTecnologia = planoAdesaoTecnologia;
    }

    public String getPlanoAdesaoCategoria() {
        return planoAdesaoCategoria;
    }

    public void setPlanoAdesaoCategoria(String planoAdesaoCategoria) {
        this.planoAdesaoCategoria = planoAdesaoCategoria;
    }

    public String getPlanoAdesaoInstalacao() {
        return planoAdesaoInstalacao;
    }

    public void setPlanoAdesaoInstalacao(String planoAdesaoInstalacao) {
        this.planoAdesaoInstalacao = planoAdesaoInstalacao;
    }

    public String getPlanoAdesaoCodIspTelecom() {
        return planoAdesaoCodIspTelecom;
    }

    public void setPlanoAdesaoCodIspTelecom(String planoAdesaoCodIspTelecom) {
        this.planoAdesaoCodIspTelecom = planoAdesaoCodIspTelecom;
    }

    public String getPlanoAdesaoValorMensalidadePlano() {
        return planoAdesaoValorMensalidadePlano;
    }

    public void setPlanoAdesaoValorMensalidadePlano(String planoAdesaoValorMensalidadePlano) {
        this.planoAdesaoValorMensalidadePlano = planoAdesaoValorMensalidadePlano;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public ArrayList getDadosPlanoAlteracaoId() {
        return dadosPlanoAlteracaoId;
    }

    public void setDadosPlanoAlteracaoId(ArrayList dadosPlanoAlteracaoId) {
        this.dadosPlanoAlteracaoId = dadosPlanoAlteracaoId;
    }

    public ArrayList getDadosPlanoAlteracaoNome() {
        return dadosPlanoAlteracaoNome;
    }

    public void setDadosPlanoAlteracaoNome(ArrayList dadosPlanoAlteracaoNome) {
        this.dadosPlanoAlteracaoNome = dadosPlanoAlteracaoNome;
    }

    public ArrayList getDadosPlanoAlteracaoValor() {
        return dadosPlanoAlteracaoValor;
    }

    public void setDadosPlanoAlteracaoValor(ArrayList dadosPlanoAlteracaoValor) {
        this.dadosPlanoAlteracaoValor = dadosPlanoAlteracaoValor;
    }

    public ArrayList getDadosPlanoAlteracaoDownload() {
        return dadosPlanoAlteracaoDownload;
    }

    public void setDadosPlanoAlteracaoDownload(ArrayList dadosPlanoAlteracaoDownload) {
        this.dadosPlanoAlteracaoDownload = dadosPlanoAlteracaoDownload;
    }

    public ArrayList getDadosPlanoAlteracaoUpload() {
        return dadosPlanoAlteracaoUpload;
    }

    public void setDadosPlanoAlteracaoUpload(ArrayList dadosPlanoAlteracaoUpload) {
        this.dadosPlanoAlteracaoUpload = dadosPlanoAlteracaoUpload;
    }

    public ArrayList getDadosPlanoAlteracaoTecnologia() {
        return dadosPlanoAlteracaoTecnologia;
    }

    public void setDadosPlanoAlteracaoTecnologia(ArrayList dadosPlanoAlteracaoTecnologia) {
        this.dadosPlanoAlteracaoTecnologia = dadosPlanoAlteracaoTecnologia;
    }

    public ArrayList getDadosPlanoAlteracaoCategoria() {
        return dadosPlanoAlteracaoCategoria;
    }

    public void setDadosPlanoAlteracaoCategoria(ArrayList dadosPlanoAlteracaoCategoria) {
        this.dadosPlanoAlteracaoCategoria = dadosPlanoAlteracaoCategoria;
    }

    public ArrayList getDadosPlanoAlteracaoInstalacao() {
        return dadosPlanoAlteracaoInstalacao;
    }

    public void setDadosPlanoAlteracaoInstalacao(ArrayList dadosPlanoAlteracaoInstalacao) {
        this.dadosPlanoAlteracaoInstalacao = dadosPlanoAlteracaoInstalacao;
    }

    public ArrayList getDadosPlanoAlteracaoCodIspTelecom() {
        return dadosPlanoAlteracaoCodIspTelecom;
    }

    public void setDadosPlanoAlteracaoCodIspTelecom(ArrayList dadosPlanoAlteracaoCodIspTelecom) {
        this.dadosPlanoAlteracaoCodIspTelecom = dadosPlanoAlteracaoCodIspTelecom;
    }
}