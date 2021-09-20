package com.rede.App.View.JavaBeans;

import android.content.Context;
import java.util.ArrayList;

/**
 * Entidade que auxulia armazneamento e carregamento de recyclerview
 *
 * @author Igor Maximo
 * @criado 30/03/2019
 * @editado 30/03/2019
 */
public class Titulo {

    public Titulo(int dias,
                  String valorFaturaCorrigidoManualmente,
                  String valorFatura,
                  String vencimentoFatura,
                  String iconeFatura,
                  String numBoleto,
                  String valorComJuros,
                  int indexFatura,
                  Context ctx,
                  String itensFatura,
                  String tipoFatura,
                  String empresa,
                  String codContratoTitulo) {
        this.dias = dias;
        this.valorFaturaCorrigidoManualmente = valorFaturaCorrigidoManualmente;
        this.valorFatura = valorFatura;
        this.vencimentoFatura = vencimentoFatura;
        this.numBoleto = numBoleto;
        this.valorJuros = valorJuros;
        this.valorComJuros = valorComJuros;
        this.indexFatura = indexFatura;
        this.ctx = ctx;
        this.iconeFatura = iconeFatura;
        this.itensFatura = itensFatura;
        this.tipoFatura = tipoFatura;
        this.empresa = empresa;
        this.codContratoTitulo = codContratoTitulo;
    }

    // Atributos soltos
    private int dias;
    private String valorFaturaCorrigidoManualmente;
    private String valorFatura;
    private String vencimentoFatura;
    private String iconeFatura;
    private String numBoleto;
    private String valorJuros;
    private String valorComJuros;
    private int indexFatura;
    private Context ctx;
    private String itensFatura;
    private String tipoFatura;
    private String empresa;
    private String codContratoTitulo;

    /////////////////////////////////////////////////////////////////

    // Para a RecyclerView da SegundaVia
    private ArrayList dadosHistoFat;
    private ArrayList dadosVencimento;
    private ArrayList dadosValorPagar;
    private ArrayList dadosCorIconePertinente;
    private ArrayList dadosNumBoleto;
    private ArrayList dadosSaldo;
    private ArrayList dadosJuros;
    private ArrayList dadosValorComJuros;
    private ArrayList dadosCodContrato;
    private ArrayList dadosContratoTitulo; // CODFAT
    private ArrayList dadosCodigoArquivoDocumento; //
    private ArrayList dadosDias;
    private ArrayList dadosValorCorrigidoManualmente;
    private ArrayList dadosPermitePagarCartao;
    private ArrayList dadosDetalhesFatura;
    private ArrayList dadosLinkBoletoGerado;
    private ArrayList dadosTipoFatura;
    private ArrayList dadosEmpresa; // CODCOB
    private ArrayList dadosFormaCobranca;

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public ArrayList getDadosFormaCobranca() {
        return dadosFormaCobranca;
    }

    public void setDadosFormaCobranca(ArrayList dadosFormaCobranca) {
        this.dadosFormaCobranca = dadosFormaCobranca;
    }

    public ArrayList getDadosCodContrato() {
        return dadosCodContrato;
    }

    public void setDadosCodContrato(ArrayList dadosCodContrato) {
        this.dadosCodContrato = dadosCodContrato;
    }

    public String getCodContratoTitulo() {
        return codContratoTitulo;
    }

    public void setCodContratoTitulo(String codContratoTitulo) {
        this.codContratoTitulo = codContratoTitulo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public ArrayList getDadosEmpresa() {
        return dadosEmpresa;
    }

    public void setDadosEmpresa(ArrayList dadosEmpresa) {
        this.dadosEmpresa = dadosEmpresa;
    }

    public String getValorFaturaCorrigidoManualmente() {
        return valorFaturaCorrigidoManualmente;
    }

    public void setValorFaturaCorrigidoManualmente(String valorFaturaCorrigidoManualmente) {
        this.valorFaturaCorrigidoManualmente = valorFaturaCorrigidoManualmente;
    }

    public String getTipoFatura() {
        return tipoFatura;
    }

    public void setTipoFatura(String tipoFatura) {
        this.tipoFatura = tipoFatura;
    }

    public ArrayList getDadosTipoFatura() {
        return dadosTipoFatura;
    }

    public void setDadosTipoFatura(ArrayList dadosTipoFatura) {
        this.dadosTipoFatura = dadosTipoFatura;
    }

    public String getValorFatura() {
        return valorFatura;
    }

    public void setValorFatura(String valorFatura) {
        this.valorFatura = valorFatura;
    }

    public String getVencimentoFatura() {
        return vencimentoFatura;
    }

    public void setVencimentoFatura(String vencimentoFatura) {
        this.vencimentoFatura = vencimentoFatura;
    }

    public String getIconeFatura() {
        return iconeFatura;
    }

    public void setIconeFatura(String iconeFatura) {
        this.iconeFatura = iconeFatura;
    }

    public String getNumBoleto() {
        return numBoleto;
    }

    public void setNumBoleto(String numBoleto) {
        this.numBoleto = numBoleto;
    }

    public String getValorJuros() {
        return valorJuros;
    }

    public void setValorJuros(String valorJuros) {
        this.valorJuros = valorJuros;
    }

    public String getValorComJuros() {
        return valorComJuros;
    }

    public void setValorComJuros(String valorComJuros) {
        this.valorComJuros = valorComJuros;
    }

    public int getIndexFatura() {
        return indexFatura;
    }

    public void setIndexFatura(int indexFatura) {
        this.indexFatura = indexFatura;
    }

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public ArrayList getDadosHistoFat() {
        return dadosHistoFat;
    }

    public void setDadosHistoFat(ArrayList dadosHistoFat) {
        this.dadosHistoFat = dadosHistoFat;
    }

    public ArrayList getDadosVencimento() {
        return dadosVencimento;
    }

    public void setDadosVencimento(ArrayList dadosVencimento) {
        this.dadosVencimento = dadosVencimento;
    }

    public ArrayList getDadosValorPagar() {
        return dadosValorPagar;
    }

    public void setDadosValorPagar(ArrayList dadosValorPagar) {
        this.dadosValorPagar = dadosValorPagar;
    }

    public ArrayList getDadosCorIconePertinente() {
        return dadosCorIconePertinente;
    }

    public void setDadosCorIconePertinente(ArrayList dadosCorIconePertinente) {
        this.dadosCorIconePertinente = dadosCorIconePertinente;
    }

    public ArrayList getDadosNumBoleto() {
        return dadosNumBoleto;
    }

    public void setDadosNumBoleto(ArrayList dadosNumBoleto) {
        this.dadosNumBoleto = dadosNumBoleto;
    }

    public ArrayList getDadosSaldo() {
        return dadosSaldo;
    }

    public void setDadosSaldo(ArrayList dadosSaldo) {
        this.dadosSaldo = dadosSaldo;
    }

    public ArrayList getDadosJuros() {
        return dadosJuros;
    }

    public void setDadosJuros(ArrayList dadosJuros) {
        this.dadosJuros = dadosJuros;
    }

    public ArrayList getDadosValorComJuros() {
        return dadosValorComJuros;
    }

    public void setDadosValorComJuros(ArrayList dadosValorComJuros) {
        this.dadosValorComJuros = dadosValorComJuros;
    }

    public ArrayList getDadosContratoTitulo() {
        return dadosContratoTitulo;
    }

    public void setDadosContratoTitulo(ArrayList dadosContratoTitulo) {
        this.dadosContratoTitulo = dadosContratoTitulo;
    }

    public ArrayList getDadosDias() {
        return dadosDias;
    }

    public void setDadosDias(ArrayList dadosDias) {
        this.dadosDias = dadosDias;
    }

    public ArrayList getDadosValorCorrigidoManualmente() {
        return dadosValorCorrigidoManualmente;
    }

    public void setDadosValorCorrigidoManualmente(ArrayList dadosValorCorrigidoManualmente) {
        this.dadosValorCorrigidoManualmente = dadosValorCorrigidoManualmente;
    }

    public ArrayList getDadosPermitePagarCartao() {
        return dadosPermitePagarCartao;
    }

    public void setDadosPermitePagarCartao(ArrayList dadosPermitePagarCartao) {
        this.dadosPermitePagarCartao = dadosPermitePagarCartao;
    }

    public ArrayList getDadosDetalhesFatura() {
        return dadosDetalhesFatura;
    }

    public void setDadosDetalhesFatura(ArrayList dadosDetalhesFatura) {
        this.dadosDetalhesFatura = dadosDetalhesFatura;
    }

    public String getItensFatura() {
        return itensFatura;
    }

    public void setItensFatura(String itensFatura) {
        this.itensFatura = itensFatura;
    }

    public ArrayList getDadosLinkBoletoGerado() {
        return dadosLinkBoletoGerado;
    }

    public void setDadosLinkBoletoGerado(ArrayList dadosLinkBoletoGerado) {
        this.dadosLinkBoletoGerado = dadosLinkBoletoGerado;
    }

    public ArrayList getDadosCodigoArquivoDocumento() {
        return dadosCodigoArquivoDocumento;
    }

    public void setDadosCodigoArquivoDocumento(ArrayList dadosCodigoArquivoDocumento) {
        this.dadosCodigoArquivoDocumento = dadosCodigoArquivoDocumento;
    }
}

