package com.rede.App.View.JavaBeans;


/**
 * Entidade responsável por cadastrar um suposto cliente para utilizar bandwidth test
 */
public class PotencialCliente {

    private String tipoCliente;
    private String campoCPFCNPJ;
    private String campoRG;
    private String campoNomeCompleto;
    private String campoEmail;
    private String campoNascimento;
    private String campoCelular;
    private String dataCadastro;


    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getCampoCPFCNPJ() {
        return campoCPFCNPJ;
    }

    public void setCampoCPFCNPJ(String campoCPFCNPJ) {
        this.campoCPFCNPJ = campoCPFCNPJ;
    }

    public String getCampoRG() {
        return campoRG;
    }

    public void setCampoRG(String campoRG) {
        this.campoRG = campoRG;
    }

    public String getCampoNomeCompleto() {
        return campoNomeCompleto;
    }

    public void setCampoNomeCompleto(String campoNomeCompleto) {
        this.campoNomeCompleto = campoNomeCompleto;
    }

    public String getCampoEmail() {
        return campoEmail;
    }

    public void setCampoEmail(String campoEmail) {
        this.campoEmail = campoEmail;
    }

    public String getCampoNascimento() {
        return campoNascimento;
    }

    public void setCampoNascimento(String campoNascimento) {
        this.campoNascimento = campoNascimento;
    }

    public String getCampoCelular() {
        return campoCelular;
    }

    public void setCampoCelular(String campoCelular) {
        this.campoCelular = campoCelular;
    }
}
