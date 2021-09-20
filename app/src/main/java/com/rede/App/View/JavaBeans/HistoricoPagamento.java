package com.rede.App.View.JavaBeans;

import android.content.Context;

import java.util.ArrayList;

/**
 * Classe responsável por armazenar e manipular tudo
 * que for referente a hisótico de pagamentos por crédito/débito
 *
 * @author Igor Maximo
 * @date 09/02/2021
 */
public class HistoricoPagamento {

    // Atributos para construção da recyclerView
    private Context context;
    private int id;
    private String dataCadastro;
    private String nome;
    private String fkCliente;
    private String pedido;
    private boolean seBaixadaSimetra;
    private String codContrato;
    private String codContratoTitulo;
    private String codArquivoDoc;
    private String vencimentoFatura;
    private String fkEmpresa;
    private String fkFormaPagamento;
    private int valorCentavos;
    private String cieloMsg;
    private String cieloPaymentId;
    private String cieloCodigoRetorno;
    private String cieloCodigoAutorizacao;
    private int cieloStatusCode;
    private String compTit;

    public HistoricoPagamento(Context context,
                              String dataCadastro,
                              String nome,
                              String fkCliente,
                              String pedido,
                              boolean seBaixadaSimetra,
                              String codContrato,
                              String codContratoTitulo,
                              String codArquivoDoc,
                              String vencimentoFatura,
                              String fkEmpresa,
                              String fkFormaPagamento,
                              int valorCentavos,
                              String cieloMsg,
                              String cieloPaymentId,
                              String cieloCodigoRetorno,
                              String cieloCodigoAutorizacao,
                              int cieloStatusCode,
                              String compTit) {
        this.context = context;
        this.id = id;
        this.dataCadastro = dataCadastro;
        this.nome = nome;
        this.fkCliente = fkCliente;
        this.pedido = pedido;
        this.seBaixadaSimetra = seBaixadaSimetra;
        this.codContrato = codContrato;
        this.codContratoTitulo = codContratoTitulo;
        this.codArquivoDoc = codArquivoDoc;
        this.vencimentoFatura = vencimentoFatura;
        this.fkEmpresa = fkEmpresa;
        this.fkFormaPagamento = fkFormaPagamento;
        this.valorCentavos = valorCentavos;
        this.cieloMsg = cieloMsg;
        this.cieloPaymentId = cieloPaymentId;
        this.cieloCodigoRetorno = cieloCodigoRetorno;
        this.cieloCodigoAutorizacao = cieloCodigoAutorizacao;
        this.cieloStatusCode = cieloStatusCode;
        this.compTit = compTit;

    }


    // Atributos para coleta do back-end
    private ArrayList<Integer> dadosId;
    private ArrayList<String> dadosDataCadastro;
    private ArrayList<String> dadosNome;
    private ArrayList<String> dadosFkCliente;
    private ArrayList<String> dadosPedido;
    private ArrayList<Boolean> dadosSeBaixadaSimetra;
    private ArrayList<String> dadosCodContrato;
    private ArrayList<String> dadosCodContratoTitulo;
    private ArrayList<String> dadosCodArquivoDoc;
    private ArrayList<String> dadosVencimentoFatura;
    private ArrayList<String> dadosFkEmpresa;
    private ArrayList<String> dadosFkFormaPagamento;
    private ArrayList<Integer> dadosValorCentavos;
    private ArrayList<String> dadosCieloMsg;
    private ArrayList<String> dadosPaymentId;
    private ArrayList<String> dadosCodigoRetorno;
    private ArrayList<String> dadosCodigoAutorizacao;
    private ArrayList<Integer> dadosStatusCode;
    private ArrayList<String> dadosCompTit;

    public String getCompTit() {
        return compTit;
    }

    public void setCompTit(String compTit) {
        this.compTit = compTit;
    }

    public ArrayList<String> getDadosCompTit() {
        return dadosCompTit;
    }

    public void setDadosCompTit(ArrayList<String> dadosCompTit) {
        this.dadosCompTit = dadosCompTit;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFkCliente() {
        return fkCliente;
    }

    public void setFkCliente(String fkCliente) {
        this.fkCliente = fkCliente;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public boolean isSeBaixadaSimetra() {
        return seBaixadaSimetra;
    }

    public void setSeBaixadaSimetra(boolean seBaixadaSimetra) {
        this.seBaixadaSimetra = seBaixadaSimetra;
    }

    public String getCodContrato() {
        return codContrato;
    }

    public void setCodContrato(String codContrato) {
        this.codContrato = codContrato;
    }

    public String getCodContratoTitulo() {
        return codContratoTitulo;
    }

    public void setCodContratoTitulo(String codContratoTitulo) {
        this.codContratoTitulo = codContratoTitulo;
    }

    public String getCodArquivoDoc() {
        return codArquivoDoc;
    }

    public void setCodArquivoDoc(String codArquivoDoc) {
        this.codArquivoDoc = codArquivoDoc;
    }

    public String getVencimentoFatura() {
        return vencimentoFatura;
    }

    public void setVencimentoFatura(String vencimentoFatura) {
        this.vencimentoFatura = vencimentoFatura;
    }

    public String getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(String fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }

    public String getFkFormaPagamento() {
        return fkFormaPagamento;
    }

    public void setFkFormaPagamento(String fkFormaPagamento) {
        this.fkFormaPagamento = fkFormaPagamento;
    }

    public double getValorCentavos() {
        return valorCentavos;
    }

    public void setValorCentavos(int valorCentavos) {
        this.valorCentavos = valorCentavos;
    }

    public String getCieloMsg() {
        return cieloMsg;
    }

    public void setCieloMsg(String cieloMsg) {
        this.cieloMsg = cieloMsg;
    }

    public String getCieloPaymentId() {
        return cieloPaymentId;
    }

    public void setCieloPaymentId(String cieloPaymentId) {
        this.cieloPaymentId = cieloPaymentId;
    }

    public String getCieloCodigoRetorno() {
        return cieloCodigoRetorno;
    }

    public void setCieloCodigoRetorno(String cieloCodigoRetorno) {
        this.cieloCodigoRetorno = cieloCodigoRetorno;
    }

    public String getCieloCodigoAutorizacao() {
        return cieloCodigoAutorizacao;
    }

    public void setCieloCodigoAutorizacao(String cieloCodigoAutorizacao) {
        this.cieloCodigoAutorizacao = cieloCodigoAutorizacao;
    }

    public int getCieloStatusCode() {
        return cieloStatusCode;
    }

    public void setCieloStatusCode(int cieloStatusCode) {
        this.cieloStatusCode = cieloStatusCode;
    }

    public ArrayList<Integer> getDadosId() {
        return dadosId;
    }

    public void setDadosId(ArrayList<Integer> dadosId) {
        this.dadosId = dadosId;
    }

    public ArrayList<String> getDadosDataCadastro() {
        return dadosDataCadastro;
    }

    public void setDadosDataCadastro(ArrayList<String> dadosDataCadastro) {
        this.dadosDataCadastro = dadosDataCadastro;
    }

    public ArrayList<String> getDadosNome() {
        return dadosNome;
    }

    public void setDadosNome(ArrayList<String> dadosNome) {
        this.dadosNome = dadosNome;
    }

    public ArrayList<String> getDadosFkCliente() {
        return dadosFkCliente;
    }

    public void setDadosFkCliente(ArrayList<String> dadosFkCliente) {
        this.dadosFkCliente = dadosFkCliente;
    }

    public ArrayList<String> getDadosPedido() {
        return dadosPedido;
    }

    public void setDadosPedido(ArrayList<String> dadosPedido) {
        this.dadosPedido = dadosPedido;
    }

    public ArrayList<Boolean> getDadosSeBaixadaSimetra() {
        return dadosSeBaixadaSimetra;
    }

    public void setDadosSeBaixadaSimetra(ArrayList<Boolean> dadosSeBaixadaSimetra) {
        this.dadosSeBaixadaSimetra = dadosSeBaixadaSimetra;
    }

    public ArrayList<String> getDadosCodContrato() {
        return dadosCodContrato;
    }

    public void setDadosCodContrato(ArrayList<String> dadosCodContrato) {
        this.dadosCodContrato = dadosCodContrato;
    }

    public ArrayList<String> getDadosCodContratoTitulo() {
        return dadosCodContratoTitulo;
    }

    public void setDadosCodContratoTitulo(ArrayList<String> dadosCodContratoTitulo) {
        this.dadosCodContratoTitulo = dadosCodContratoTitulo;
    }

    public ArrayList<String> getDadosCodArquivoDoc() {
        return dadosCodArquivoDoc;
    }

    public void setDadosCodArquivoDoc(ArrayList<String> dadosCodArquivoDoc) {
        this.dadosCodArquivoDoc = dadosCodArquivoDoc;
    }

    public ArrayList<String> getDadosVencimentoFatura() {
        return dadosVencimentoFatura;
    }

    public void setDadosVencimentoFatura(ArrayList<String> dadosVencimentoFatura) {
        this.dadosVencimentoFatura = dadosVencimentoFatura;
    }

    public ArrayList<String> getDadosFkEmpresa() {
        return dadosFkEmpresa;
    }

    public void setDadosFkEmpresa(ArrayList<String> dadosFkEmpresa) {
        this.dadosFkEmpresa = dadosFkEmpresa;
    }

    public ArrayList<String> getDadosFkFormaPagamento() {
        return dadosFkFormaPagamento;
    }

    public void setDadosFkFormaPagamento(ArrayList<String> dadosFkFormaPagamento) {
        this.dadosFkFormaPagamento = dadosFkFormaPagamento;
    }

    public ArrayList<Integer> getDadosValorCentavos() {
        return dadosValorCentavos;
    }

    public void setDadosValorCentavos(ArrayList<Integer> dadosValorCentavos) {
        this.dadosValorCentavos = dadosValorCentavos;
    }

    public ArrayList<String> getDadosCieloMsg() {
        return dadosCieloMsg;
    }

    public void setDadosCieloMsg(ArrayList<String> dadosCieloMsg) {
        this.dadosCieloMsg = dadosCieloMsg;
    }

    public ArrayList<String> getDadosPaymentId() {
        return dadosPaymentId;
    }

    public void setDadosPaymentId(ArrayList<String> dadosPaymentId) {
        this.dadosPaymentId = dadosPaymentId;
    }

    public ArrayList<String> getDadosCodigoRetorno() {
        return dadosCodigoRetorno;
    }

    public void setDadosCodigoRetorno(ArrayList<String> dadosCodigoRetorno) {
        this.dadosCodigoRetorno = dadosCodigoRetorno;
    }

    public ArrayList<String> getDadosCodigoAutorizacao() {
        return dadosCodigoAutorizacao;
    }

    public void setDadosCodigoAutorizacao(ArrayList<String> dadosCodigoAutorizacao) {
        this.dadosCodigoAutorizacao = dadosCodigoAutorizacao;
    }

    public ArrayList<Integer> getDadosStatusCode() {
        return dadosStatusCode;
    }

    public void setDadosStatusCode(ArrayList<Integer> dadosStatusCode) {
        this.dadosStatusCode = dadosStatusCode;
    }
}
