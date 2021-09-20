package com.rede.App.View.JavaBeans;

import java.util.ArrayList;

/**
 * Entidade com os dados de um cliente
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 * @editado 02/03/2019
 */

public final class Usuario {

    // Para demais serviços
    private static String status;
    private static String codigo;
    private static String nome;
    private static String cpfCnpj;
    private static String endereco;
    private static String bairro;
    private static String numero;
    private static String cep;
    private static String cidade;
    private static String uf;
    private static String fone;
    private static String celular;
    private static String email;
    private static String senha;
    // Para Autenticação
    private static String tipoCliente;
    private static String cpfCnpjAutenticacao;
    private static String senhaAutenticacao;
    // Contato
    private static String celular1;
    private static String celular2;
    private static String celular3;
    private static String celular4;
    // Paramount e Noggin
    private static String userApp;
    private static String passApp;
    // Para cartão recorrente
    private static ArrayList<CartaoRecorrente> cartoesRecorrentes;

    public ArrayList<CartaoRecorrente> getCartoesRecorrentes() {
        return cartoesRecorrentes;
    }

    public void setCartoesRecorrentes(ArrayList<CartaoRecorrente> cartoesRecorrentes) {
        this.cartoesRecorrentes = cartoesRecorrentes;
    }

    public String getUserApp() {
        return userApp;
    }

    public void setUserApp(String userApp) {
        this.userApp = userApp;
    }

    public String getPassApp() {
        return passApp;
    }

    public void setPassApp(String passApp) {
        this.passApp = passApp;
    }

    public String getCelular1() {
        return celular1;
    }

    public void setCelular1(String celular1) {
        this.celular1 = celular1;
    }

    public String getCelular2() {
        return celular2;
    }

    public void setCelular2(String celular2) {
        this.celular2 = celular2;
    }

    public   String getCelular3() {
        return celular3;
    }

    public   void setCelular3(String celular3) {
        this.celular3 = celular3;
    }

    public   String getCelular4() {
        return celular4;
    }

    public   void setCelular4(String celular4) {
        this.celular4 = celular4;
    }

    public  String getNumero() {
        return numero;
    }

    public  void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCpfCnpjAutenticacao() {
        return cpfCnpjAutenticacao;
    }

    public void setCpfCnpjAutenticacao(String cpfCnpjAutenticacao) {
        this.cpfCnpjAutenticacao = cpfCnpjAutenticacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getSenhaAutenticacao() {
        return senhaAutenticacao;
    }

    public void setSenhaAutenticacao(String senhaAutenticacao) {
        this.senhaAutenticacao = senhaAutenticacao;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
