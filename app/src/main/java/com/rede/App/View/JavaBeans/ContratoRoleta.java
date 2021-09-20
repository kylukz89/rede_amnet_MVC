package com.rede.App.View.JavaBeans;

import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;

/**
 * Entidade que auxlilia recyclerview dos planos na roleta menu principal
 *
 * @author Igor Maximo
 * @date 30/03/2019
 */
public class ContratoRoleta {

    public ContratoRoleta(String codigoCliente, String codSercli, String nomeDoPlano, String statusPlano, String valorFinal, String enderecoInstalacao, String vencimentoPlano, Intent detalhesPlano, Context ctx, String codProd, String codContratoItem, String codClieCartao, String enderecoPuro, String numeroPuro, String empresaCNPJ, String empresaNome, String codAppExterno, String usuarioApp, String senhaApp) {
        this.codigoCliente = codigoCliente;
        this.codSercli = codSercli;
        this.nomeDoPlano = nomeDoPlano;
        this.statusPlano = statusPlano;
        this.valorFinal = valorFinal;
        this.enderecoInstalacao = enderecoInstalacao;
        this.vencimentoPlano = vencimentoPlano;
        this.intent = detalhesPlano;
        this.ctx = ctx;
        this.codProd = codProd;
        this.codContratoItem = codContratoItem;
        this.codClieCartao = codClieCartao;
        this.enderecoPuro = enderecoPuro;
        this.numeroPuro = numeroPuro;
        // Paramount / noggin
        this.codAppExterno = codAppExterno;
        this.usuarioApp = usuarioApp;
        this.senhaApp = senhaApp;
        // Dados da empresa fornecedora dos serviços
        this.empresaCNPJ = empresaCNPJ;
        this.empresaNome = empresaNome;
    }

    private String plano;
    private String valorFatura; //Valor_Plano (MENSALIDADE DO PLANO)
    //private String statusPlano;
    private int icFatura;
    // Atributos soltos
    private String codigoCliente;
    private String codSercli;
    private String nomeDoPlano;
    private String statusPlano;
    private String valorFinal;
    private String enderecoInstalacao;
    private String vencimentoPlano;
    private Intent intent;
    private Context ctx;
    private String codProd;
    private String codContratoItem;
    private String codClieCartao;
    private String enderecoPuro;
    private String numeroPuro;
    // Paramount / noggin
    private String codAppExterno;
    private String usuarioApp;
    private String senhaApp;
    // Dados da empresa fornecedores
    private String empresaCNPJ;
    private String empresaNome;

    // Para Roleta
    private ArrayList dadosCodigoCliente;
    private ArrayList dadosCodContrato;
    private ArrayList dadosNomeDoPlano;
    private ArrayList dadosStatusPlano;
    private ArrayList dadosValorFinal;
    private ArrayList dadosEnderecoInstalacao;
    private ArrayList dadosVencimentoPlano;
    private ArrayList dadosSaldoDoPlano;
    private ArrayList dadosCodPolicy;
    private ArrayList dadosCodProd;
    private ArrayList dadosCodContratoItem;
    private ArrayList dadosCodClieCartaoVindi;
    private ArrayList dadosEnderecoInstalacaoPuro;
    private ArrayList dadosNumeroInstalacaoPuro;
    // Paramount / Noggin
    private ArrayList dadosCodAppExterno;
    private ArrayList dadosUsuarioApp;
    private ArrayList dadosSenhaApp;
    // Dados do fornecedor de serviços, Empresa
    private ArrayList dadosContratoEmpresaCNPJ;
    private ArrayList dadosContratoEmpresaNome;

    public ArrayList getDadosContratoEmpresaCNPJ() {
        return dadosContratoEmpresaCNPJ;
    }

    public void setDadosContratoEmpresaCNPJ(ArrayList dadosContratoEmpresaCNPJ) {
        this.dadosContratoEmpresaCNPJ = dadosContratoEmpresaCNPJ;
    }

    public ArrayList getDadosContratoEmpresaNome() {
        return dadosContratoEmpresaNome;
    }

    public void setDadosContratoEmpresaNome(ArrayList dadosContratoEmpresaNome) {
        this.dadosContratoEmpresaNome = dadosContratoEmpresaNome;
    }

    public String getEmpresaCNPJ() {
        return empresaCNPJ;
    }

    public void setEmpresaCNPJ(String empresaCNPJ) {
        this.empresaCNPJ = empresaCNPJ;
    }

    public String getEmpresaNome() {
        return empresaNome;
    }

    public void setEmpresaNome(String empresaNome) {
        this.empresaNome = empresaNome;
    }


    public String getCodClieCartao() {
        return codClieCartao;
    }

    public void setCodClieCartao(String codClieCartao) {
        this.codClieCartao = codClieCartao;
    }

    private int incrementaRoleta;


    public String getEnderecoPuro() {
        return enderecoPuro;
    }

    public void setEnderecoPuro(String enderecoPuro) {
        this.enderecoPuro = enderecoPuro;
    }

    public String getNumeroPuro() {
        return numeroPuro;
    }

    public void setNumeroPuro(String numeroPuro) {
        this.numeroPuro = numeroPuro;
    }



    public String getCodAppExterno() {
        return codAppExterno;
    }

    public void setCodAppExterno(String codAppExterno) {
        this.codAppExterno = codAppExterno;
    }

    public String getUsuarioApp() {
        return usuarioApp;
    }

    public void setUsuarioApp(String usuarioApp) {
        this.usuarioApp = usuarioApp;
    }

    public String getSenhaApp() {
        return senhaApp;
    }

    public void setSenhaApp(String senhaApp) {
        this.senhaApp = senhaApp;
    }

    public ArrayList getDadosCodAppExterno() {
        return dadosCodAppExterno;
    }

    public void setDadosCodAppExterno(ArrayList dadosCodAppExterno) {
        this.dadosCodAppExterno = dadosCodAppExterno;
    }

    public ArrayList getDadosUsuarioApp() {
        return dadosUsuarioApp;
    }

    public void setDadosUsuarioApp(ArrayList dadosUsuarioApp) {
        this.dadosUsuarioApp = dadosUsuarioApp;
    }

    public ArrayList getDadosSenhaApp() {
        return dadosSenhaApp;
    }

    public void setDadosSenhaApp(ArrayList dadosSenhaApp) {
        this.dadosSenhaApp = dadosSenhaApp;
    }

    public ArrayList getDadosCodClieCartaoVindi() {
        return dadosCodClieCartaoVindi;
    }

    public void setDadosCodClieCartaoVindi(ArrayList dadosCodClieCartaoVindi) {
        this.dadosCodClieCartaoVindi = dadosCodClieCartaoVindi;
    }

    public ArrayList getDadosEnderecoInstalacaoPuro() {
        return dadosEnderecoInstalacaoPuro;
    }

    public void setDadosEnderecoInstalacaoPuro(ArrayList dadosEnderecoInstalacaoPuro) {
        this.dadosEnderecoInstalacaoPuro = dadosEnderecoInstalacaoPuro;
    }

    public ArrayList getDadosNumeroInstalacaoPuro() {
        return dadosNumeroInstalacaoPuro;
    }

    public void setDadosNumeroInstalacaoPuro(ArrayList dadosNumeroInstalacaoPuro) {
        this.dadosNumeroInstalacaoPuro = dadosNumeroInstalacaoPuro;
    }

    private ArrayList<Boolean> dadosSeRegraAntivirus;

    public String getCodContratoItem() {
        return codContratoItem;
    }

    public void setCodContratoItem(String codContratoItem) {
        this.codContratoItem = codContratoItem;
    }

    public ArrayList getDadosCodContratoItem() {
        return dadosCodContratoItem;
    }

    public void setDadosCodContratoItem(ArrayList dadosCodContratoItem) {
        this.dadosCodContratoItem = dadosCodContratoItem;
    }

    public String getCodProd() {
        return codProd;
    }

    public void setCodProd(String codProd) {
        this.codProd = codProd;
    }

    public ArrayList<Boolean> getDadosSeRegraAntivirus() {
        return dadosSeRegraAntivirus;
    }

    public void setDadosSeRegraAntivirus(ArrayList<Boolean> dadosSeRegraAntivirus) {
        this.dadosSeRegraAntivirus = dadosSeRegraAntivirus;
    }

    public ArrayList getDadosCodProd() {
        return dadosCodProd;
    }

    public void setDadosCodProd(ArrayList dadosCodProd) {
        this.dadosCodProd = dadosCodProd;
    }

    public ArrayList getDadosCodPolicy() {
        return dadosCodPolicy;
    }

    public void setDadosCodPolicy(ArrayList dadosCodPolicy) {
        this.dadosCodPolicy = dadosCodPolicy;
    }

    public ArrayList getDadosSaldoDoPlano() {
        return dadosSaldoDoPlano;
    }

    public void setDadosSaldoDoPlano(ArrayList dadosSaldoDoPlano) {
        this.dadosSaldoDoPlano = dadosSaldoDoPlano;
    }

    public String getPlano() {
        return plano;
    }

    public void setPlano(String plano) {
        this.plano = plano;
    }

    public String getValorFatura() {
        return valorFatura;
    }

    public void setValorFatura(String valorFatura) {
        this.valorFatura = valorFatura;
    }

    public int getIcFatura() {
        return icFatura;
    }

    public void setIcFatura(int icFatura) {
        this.icFatura = icFatura;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getCodSercli() {
        return codSercli;
    }

    public void setCodSercli(String codSercli) {
        this.codSercli = codSercli;
    }

    public String getNomeDoPlano() {
        return nomeDoPlano;
    }

    public void setNomeDoPlano(String nomeDoPlano) {
        this.nomeDoPlano = nomeDoPlano;
    }

    public String getStatusPlano() {
        return statusPlano;
    }

    public void setStatusPlano(String statusPlano) {
        this.statusPlano = statusPlano;
    }

    public String getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(String valorFinal) {
        this.valorFinal = valorFinal;
    }

    public String getEnderecoInstalacao() {
        return enderecoInstalacao;
    }

    public void setEnderecoInstalacao(String enderecoInstalacao) {
        this.enderecoInstalacao = enderecoInstalacao;
    }

    public ArrayList getDadosCodigoCliente() {
        return dadosCodigoCliente;
    }

    public void setDadosCodigoCliente(ArrayList dadosCodigoCliente) {
        this.dadosCodigoCliente = dadosCodigoCliente;
    }

    public ArrayList getDadosCodContrato() {
        return dadosCodContrato;
    }

    public void setDadosCodContrato(ArrayList dadosCodContrato) {
        this.dadosCodContrato = dadosCodContrato;
    }

    public ArrayList getDadosNomeDoPlano() {
        return dadosNomeDoPlano;
    }

    public void setDadosNomeDoPlano(ArrayList dadosNomeDoPlano) {
        this.dadosNomeDoPlano = dadosNomeDoPlano;
    }

    public ArrayList getDadosStatusPlano() {
        return dadosStatusPlano;
    }

    public void setDadosStatusPlano(ArrayList dadosStatusPlano) {
        this.dadosStatusPlano = dadosStatusPlano;
    }

    public ArrayList getDadosValorFinal() {
        return dadosValorFinal;
    }

    public void setDadosValorFinal(ArrayList dadosValorFinal) {
        this.dadosValorFinal = dadosValorFinal;
    }

    public ArrayList getDadosEnderecoInstalacao() {
        return dadosEnderecoInstalacao;
    }

    public void setDadosEnderecoInstalacao(ArrayList dadosEnderecoInstalacao) {
        this.dadosEnderecoInstalacao = dadosEnderecoInstalacao;
    }

    public int getIncrementaRoleta() {
        return incrementaRoleta;
    }

    public void setIncrementaRoleta(int incrementaRoleta) {
        this.incrementaRoleta = incrementaRoleta;
    }

    public ArrayList getDadosVencimentoPlano() {
        return dadosVencimentoPlano;
    }

    public void setDadosVencimentoPlano(ArrayList dadosVencimentoPlano) {
        this.dadosVencimentoPlano = dadosVencimentoPlano;
    }

    public String getVencimentoPlano() {
        return vencimentoPlano;
    }

    public void setVencimentoPlano(String vencimentoPlano) {
        this.vencimentoPlano = vencimentoPlano;
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
}