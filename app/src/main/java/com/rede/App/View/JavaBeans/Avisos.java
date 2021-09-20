package com.rede.App.View.JavaBeans;


/**
 * Entidade responsável pelos alertas
 *
 * @author Igor Maximo
 * @criado 30/03/2019
 * @editado 30/03/2019
 */
public class Avisos {

    private String alertaMassivo;

    public String getAlertaMassivo() {
        return alertaMassivo;
    }

    public void setAlertaMassivo(String alertaMassivo) {
        this.alertaMassivo = alertaMassivo;
    }


    public String getAlertaMassivoTeste(String param, int param2, boolean boleano) {
        System.out.println(">> " + param2 + " -param===========>> " + param + " " + boleano);
        return "56789";
    }
}
