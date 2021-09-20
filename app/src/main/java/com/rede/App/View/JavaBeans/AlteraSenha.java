package com.rede.App.View.JavaBeans;

import android.content.Context;

/**
 * Entidade responsável pela customização de senhas
 *
 * @author Igor Maximo
 * @criado 30/03/2019
 * @editado 30/03/2019
 */
public class AlteraSenha {

    private Context ctx;
    private String tipoCliente;
    private String cpfCnpjCliente;
    private String senha;
    private String novaSenha;
    private String repitaNovaSenha;

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getCpfCnpjCliente() {
        return cpfCnpjCliente;
    }

    public void setCpfCnpjCliente(String cpfCnpjCliente) {
        this.cpfCnpjCliente = cpfCnpjCliente;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getRepitaNovaSenha() {
        return repitaNovaSenha;
    }

    public void setRepitaNovaSenha(String repitaNovaSenha) {
        this.repitaNovaSenha = repitaNovaSenha;
    }

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }
}
