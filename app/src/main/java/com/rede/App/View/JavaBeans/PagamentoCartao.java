package com.rede.App.View.JavaBeans;


/**
 * Entidade de pagamento por cartão
 *
 * @author Igor Maximo
 * @date 26/01/2021
 */
public class PagamentoCartao {

    private int tipoPagamentoCreditoDebito; // true para crédito e false para débito
    private String numeroCartao;
    private String validade;
    private String nomeTitular;
    private String docTitular;
    private String celular;
    private String bandeira; // Cielo para mensalidades
    private String valor;
    private String dataTransacaoPagamento;
    private String CVV;
    private String codigoContrato;
    private String codigoContratoTitulo; // CODFAT
    private String codigoArquivoDocumento; //
    private String vencFatura;
    private String transacao;
    private String transacaoMsg;
    private String codigoCliente;
    private String contrato; // Se houver
    private String codigoFatura;
    private String parcelas;
    private String obs;
    private String empresa;
    private String formaCobranca;

    public String getFormaCobranca() {
        return formaCobranca;
    }

    public void setFormaCobranca(String formaCobranca) {
        this.formaCobranca = formaCobranca;
    }

    public String getCodigoContrato() {
        return codigoContrato;
    }

    public void setCodigoContrato(String codigoContrato) {
        this.codigoContrato = codigoContrato;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getCodigoArquivoDocumento() {
        return codigoArquivoDocumento;
    }

    public void setCodigoArquivoDocumento(String codigoArquivoDocumento) {
        this.codigoArquivoDocumento = codigoArquivoDocumento;
    }

    public String getVencFatura() {
        return vencFatura;
    }

    public void setVencFatura(String vencFatura) {
        this.vencFatura = vencFatura;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    public String getDocTitular() {
        return docTitular;
    }

    public void setDocTitular(String docTitular) {
        this.docTitular = docTitular;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDataTransacaoPagamento() {
        return dataTransacaoPagamento;
    }

    public void setDataTransacaoPagamento(String dataTransacaoPagamento) {
        this.dataTransacaoPagamento = dataTransacaoPagamento;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getCVV() {
        return CVV;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    public String getCodigoContratoTitulo() {
        return codigoContratoTitulo;
    }

    public void setCodigoContratoTitulo(String codigoContratoTitulo) {
        this.codigoContratoTitulo = codigoContratoTitulo;
    }

    public String getTransacaoMsg() {
        return transacaoMsg;
    }

    public void setTransacaoMsg(String transacaoMsg) {
        this.transacaoMsg = transacaoMsg;
    }

    public String getTransacao() {
        return transacao;
    }

    public void setTransacao(String transacao) {
        this.transacao = transacao;
    }

    public int getTipoPagamentoCreditoDebito() {
        return tipoPagamentoCreditoDebito;
    }

    public void setTipoPagamentoCreditoDebito(int tipoPagamentoCreditoDebito) {
        this.tipoPagamentoCreditoDebito = tipoPagamentoCreditoDebito;
    }

    public String getCodigoFatura() {
        return codigoFatura;
    }

    public void setCodigoFatura(String codigoFatura) {
        this.codigoFatura = codigoFatura;
    }

    public String getParcelas() {
        return parcelas;
    }

    public void setParcelas(String parcelas) {
        this.parcelas = parcelas;
    }
}
