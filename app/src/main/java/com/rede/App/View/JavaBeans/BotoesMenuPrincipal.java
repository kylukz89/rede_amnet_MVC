package com.rede.App.View.JavaBeans;


import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;
/**
 * Entidade que auxilia na criação do recyclerview dos botões menu principal
 *
 * @author Igor Maximo
 * @criado 30/03/2019
 * @editado 30/03/2019
 */
public class BotoesMenuPrincipal {

    private Context ctx;
    private LinearLayout linearLayout;
    private int iconeBotao;
    private int iconeBotaoFlag;
    private String nomeBotao;
    private Intent intent;

    public BotoesMenuPrincipal(int iconeBotao, int iconeBotaoFlag, String nomeBotao, Context ctx, Intent intent) {
        this.iconeBotao = iconeBotao;
        this.nomeBotao = nomeBotao;
        this.iconeBotaoFlag = iconeBotaoFlag;
        this.ctx = ctx;
        this.intent = intent;
    }

    public int getIconeBotaoFlag() {
        return iconeBotaoFlag;
    }

    public void setIconeBotaoFlag(int iconeBotaoFlag) {
        this.iconeBotaoFlag = iconeBotaoFlag;
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
}