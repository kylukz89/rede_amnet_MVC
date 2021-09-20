package com.rede.App.View.JavaBeans;


import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;

/**
 * Entidade que auxilia na criação do recyclerview da roleta de planos do menu principal
 *
 * @author Igor Maximo
 * @criado 30/03/2019
 * @editado 30/03/2019
 */
public class BotoesMenuDadosPlano {

    private Context ctx;
    private LinearLayout linearLayout;
    private int iconeBotao;
    private String nomeBotao;
    private Intent intent;
    private String codSerCli;
    private String nomePlano;
    private String nomeEnderecoPlano;
    private String codCob;

    public BotoesMenuDadosPlano(int iconeBotao, String nomeBotao, Context ctx, Intent intent, String codSerCli, String codCob, String nomePlano, String nomeEnderecoPlano) {
        this.iconeBotao = iconeBotao;
        this.nomeBotao = nomeBotao;
        this.ctx = ctx;
        this.intent = intent;
        this.codSerCli = codSerCli;
        this.codCob = codCob;
        this.nomePlano = nomePlano;
        this.nomeEnderecoPlano = nomeEnderecoPlano;
    }

    public String getCodCob() {
        return codCob;
    }

    public void setCodCob(String codCob) {
        this.codCob = codCob;
    }

    public String getNomePlano() {
        return nomePlano;
    }

    public void setNomePlano(String nomePlano) {
        this.nomePlano = nomePlano;
    }

    public String getNomeEnderecoPlano() {
        return nomeEnderecoPlano;
    }

    public void setNomeEnderecoPlano(String nomeEnderecoPlano) {
        this.nomeEnderecoPlano = nomeEnderecoPlano;
    }

    public int getIconeBotao() {
        return iconeBotao;
    }

    public void setIconeBotao(int iconeBotao) {
        this.iconeBotao = iconeBotao;
    }

    public String getNomeBotao() {
        return nomeBotao;
    }

    public void setNomeBotao(String nomeBotao) {
        this.nomeBotao = nomeBotao;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public String getCodSerCli() {
        return codSerCli;
    }

    public void setCodSerCli(String codSerCli) {
        this.codSerCli = codSerCli;
    }
}