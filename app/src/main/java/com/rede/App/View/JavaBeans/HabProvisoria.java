package com.rede.App.View.JavaBeans;

import java.util.ArrayList;

/**
 * Entidade que carrega os dados de habilitação provisória
 *
 * @author Igor Maximo
 * @criado 30/03/2019
 * @editado 30/03/2019
 */

public class HabProvisoria {

    private String codigoCliente;
    private String codSerCli;
    private String dias; // 3
    private String modulo;
    private String ipComprador;

    private ArrayList planosSuspensosPorDebito;
    private ArrayList planosSuspensosPorDebitoCodSerCli;

    public String getCodSerCli() {
        return codSerCli;
    }

    public void setCodSerCli(String codSerCli) {
        this.codSerCli = codSerCli;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getIpComprador() {
        return ipComprador;
    }

    public void setIpComprador(String ipComprador) {
        this.ipComprador = ipComprador;
    }

    public ArrayList getPlanosSuspensosPorDebito() {
        return planosSuspensosPorDebito;
    }

    public void setPlanosSuspensosPorDebito(ArrayList planosSuspensosPorDebito) {
        this.planosSuspensosPorDebito = planosSuspensosPorDebito;
    }

    public ArrayList getPlanosSuspensosPorDebitoCodSerCli() {
        return planosSuspensosPorDebitoCodSerCli;
    }

    public void setPlanosSuspensosPorDebitoCodSerCli(ArrayList planosSuspensosPorDebitoCodSerCli) {
        this.planosSuspensosPorDebitoCodSerCli = planosSuspensosPorDebitoCodSerCli;
    }
}
