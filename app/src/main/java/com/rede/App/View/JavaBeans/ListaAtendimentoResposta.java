package com.rede.App.View.JavaBeans;

import android.content.Context;

import java.util.ArrayList;

/**
 * Entidade responsável por manipular
 * os recursos da lista de atendimento
 * bem como solicitações do usuário
 *
 * @author      Igor Maximo
 * @date        03/05/2021
 */
public class ListaAtendimentoResposta {

    // Para uso da recyclerView
    public ListaAtendimentoResposta(int fkCategoria, int fkCategoriaResposta, String resposta,  boolean seRespostaLivre,  Context context) {
        this.fkCategoriaResposta = fkCategoriaResposta;
        this.fkCategoria = fkCategoria;
        this.resposta = resposta;
        this.seRespostaLivre = seRespostaLivre;
        this.context = context;
    }
    // Atributos
    private int fkCategoria;
    private int fkCategoriaResposta;
    private String resposta;
    private String categoriaNome;
    private boolean seRespostaLivre;
    private  Context context;
    // Lista
    private ArrayList<Integer> dadosFkCategoriaResposta;
    private ArrayList<Integer> dadosFkCategoria;
    private ArrayList<String> dadosResposta;
    private ArrayList<Boolean> dadosSeRespostaLivre;

    public ArrayList<Integer> getDadosFkCategoriaResposta() {
        return dadosFkCategoriaResposta;
    }

    public void setDadosFkCategoriaResposta(ArrayList<Integer> dadosFkCategoriaResposta) {
        this.dadosFkCategoriaResposta = dadosFkCategoriaResposta;
    }

    public int getFkCategoriaResposta() {
        return fkCategoriaResposta;
    }

    public void setFkCategoriaResposta(int fkCategoriaResposta) {
        this.fkCategoriaResposta = fkCategoriaResposta;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }



    public int getFkCategoria() {
        return fkCategoria;
    }

    public void setFkCategoria(int fkCategoria) {
        this.fkCategoria = fkCategoria;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public boolean isSeRespostaLivre() {
        return seRespostaLivre;
    }

    public void setSeRespostaLivre(boolean seRespostaLivre) {
        this.seRespostaLivre = seRespostaLivre;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public ArrayList<Integer> getDadosFkCategoria() {
        return dadosFkCategoria;
    }

    public void setDadosFkCategoria(ArrayList<Integer> dadosFkCategoria) {
        this.dadosFkCategoria = dadosFkCategoria;
    }

    public ArrayList<String> getDadosResposta() {
        return dadosResposta;
    }

    public void setDadosResposta(ArrayList<String> dadosResposta) {
        this.dadosResposta = dadosResposta;
    }

    public ArrayList<Boolean> getDadosSeRespostaLivre() {
        return dadosSeRespostaLivre;
    }

    public void setDadosSeRespostaLivre(ArrayList<Boolean> dadosSeRespostaLivre) {
        this.dadosSeRespostaLivre = dadosSeRespostaLivre;
    }
}
