package com.rede.App.View.JavaBeans;


import android.content.Context;
import java.util.ArrayList;

/**
 * Entidade responsável pelos alertas
 *
 * @author Igor Maximo
 * @criado 30/03/2019
 * @editado 30/03/2019
 */
public class Notificacao {

    private int id;
    private boolean massivo;
    private boolean seLido;
    private String codigoCliente;
    private String alertaMassivo;
    private String alertaIndividual;
    private String dataNotificacao;
    private int codCli;
    // Central notificações
    private ArrayList dadosNotificacoes;
    private ArrayList dadosData;
    private ArrayList dadosIdsNotificacoes;
    private ArrayList dadosSeConfirmadoNotificacao;
    private ArrayList<Integer> dadosSeDependeNotificacao;
    private ArrayList<Integer> dadosProtocolo;
    private ArrayList dadosSeLido;
    private ArrayList dadosTipoNotificacao;
    // Montar recyclerview
    private String notificacao;
    private int idNotificacao;
    private boolean seLidoNotificacao;
    private int seConfirmado;
    private int seDependeConfirmacao;
    private String tipoNotificacao;
    int protocolo;
    private Context context;

    public Notificacao(String notificacao, String dataNotificacao, int idNotificacao, boolean seLidoNotificacao, int seConfirmado, int seDependeConfirmacao, int protocolo, String tipoNotificacao, Context context) {
        this.notificacao = notificacao;
        this.dataNotificacao = dataNotificacao;
        this.idNotificacao = idNotificacao;
        this.seLidoNotificacao = seLidoNotificacao;
        this.seConfirmado = seConfirmado;
        this.seDependeConfirmacao = seDependeConfirmacao;
        this.protocolo = protocolo;
        this.tipoNotificacao = tipoNotificacao;
        this.context = context;
    }

    public ArrayList<String> getDadosTipoNotificacao() {
        return dadosTipoNotificacao;
    }

    public void setDadosTipoNotificacao(ArrayList<String> dadosTipoNotificacao) {
        this.dadosTipoNotificacao = dadosTipoNotificacao;
    }

    public String getTipoNotificacao() {
        return tipoNotificacao;
    }

    public void setTipoNotificacao(String tipoNotificacao) {
        this.tipoNotificacao = tipoNotificacao;
    }

    public int getCodCli() {
        return codCli;
    }

    public void setCodCli(int codCli) {
        this.codCli = codCli;
    }

    public int getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(int protocolo) {
        this.protocolo = protocolo;
    }

    public ArrayList<Integer> getDadosProtocolo() {
        return dadosProtocolo;
    }

    public void setDadosProtocolo(ArrayList<Integer> dadosProtocolo) {
        this.dadosProtocolo = dadosProtocolo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isMassivo() {
        return massivo;
    }

    public void setMassivo(boolean massivo) {
        this.massivo = massivo;
    }

    public boolean isSeLido() {
        return seLido;
    }

    public void setSeLido(boolean seLido) {
        this.seLido = seLido;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getAlertaMassivo() {
        return alertaMassivo;
    }

    public void setAlertaMassivo(String alertaMassivo) {
        this.alertaMassivo = alertaMassivo;
    }

    public String getAlertaIndividual() {
        return alertaIndividual;
    }

    public void setAlertaIndividual(String alertaIndividual) {
        this.alertaIndividual = alertaIndividual;
    }

    public String getDataNotificacao() {
        return dataNotificacao;
    }

    public void setDataNotificacao(String dataNotificacao) {
        this.dataNotificacao = dataNotificacao;
    }

    public ArrayList getDadosNotificacoes() {
        return dadosNotificacoes;
    }

    public void setDadosNotificacoes(ArrayList dadosNotificacoes) {
        this.dadosNotificacoes = dadosNotificacoes;
    }

    public ArrayList getDadosData() {
        return dadosData;
    }

    public void setDadosData(ArrayList dadosData) {
        this.dadosData = dadosData;
    }

    public ArrayList getDadosIdsNotificacoes() {
        return dadosIdsNotificacoes;
    }

    public void setDadosIdsNotificacoes(ArrayList dadosIdsNotificacoes) {
        this.dadosIdsNotificacoes = dadosIdsNotificacoes;
    }

    public ArrayList getDadosSeConfirmadoNotificacao() {
        return dadosSeConfirmadoNotificacao;
    }

    public void setDadosSeConfirmadoNotificacao(ArrayList dadosSeConfirmadoNotificacao) {
        this.dadosSeConfirmadoNotificacao = dadosSeConfirmadoNotificacao;
    }

    public ArrayList<Integer> getDadosSeDependeNotificacao() {
        return dadosSeDependeNotificacao;
    }

    public void setDadosSeDependeNotificacao(ArrayList<Integer> dadosSeDependeNotificacao) {
        this.dadosSeDependeNotificacao = dadosSeDependeNotificacao;
    }

    public ArrayList getDadosSeLido() {
        return dadosSeLido;
    }

    public void setDadosSeLido(ArrayList dadosSeLido) {
        this.dadosSeLido = dadosSeLido;
    }

    public String getNotificacao() {
        return notificacao;
    }

    public void setNotificacao(String notificacao) {
        this.notificacao = notificacao;
    }

    public int getIdNotificacao() {
        return idNotificacao;
    }

    public void setIdNotificacao(int idNotificacao) {
        this.idNotificacao = idNotificacao;
    }

    public boolean isSeLidoNotificacao() {
        return seLidoNotificacao;
    }

    public void setSeLidoNotificacao(boolean seLidoNotificacao) {
        this.seLidoNotificacao = seLidoNotificacao;
    }

    public int getSeConfirmado() {
        return seConfirmado;
    }

    public void setSeConfirmado(int seConfirmado) {
        this.seConfirmado = seConfirmado;
    }

    public int getSeDependeConfirmacao() {
        return seDependeConfirmacao;
    }

    public void setSeDependeConfirmacao(int seDependeConfirmacao) {
        this.seDependeConfirmacao = seDependeConfirmacao;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
