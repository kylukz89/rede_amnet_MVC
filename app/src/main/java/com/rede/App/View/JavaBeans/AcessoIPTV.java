package com.rede.App.View.JavaBeans;

import android.content.Context;

public class AcessoIPTV {

    public AcessoIPTV(String user, String pass, String nome, String descricao, String linkApp, String linkWeb, Context context) {
        this.user = user;
        this.pass = pass;
        this.nome = nome;
        this.descricao = descricao;
        this.linkApp = linkApp;
        this.linkWeb = linkWeb;
        this.context = context;
    }

    private String user;
    private String pass;
    // Plataforma
    private String nome;
    private String descricao;
    String linkApp;
    String linkWeb;
    Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getLinkApp() {
        return linkApp;
    }

    public void setLinkApp(String linkApp) {
        this.linkApp = linkApp;
    }

    public String getLinkWeb() {
        return linkWeb;
    }

    public void setLinkWeb(String linkWeb) {
        this.linkWeb = linkWeb;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
