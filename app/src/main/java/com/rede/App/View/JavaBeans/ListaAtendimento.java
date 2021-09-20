package com.rede.App.View.JavaBeans;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Entidade responsável por manipular
 * os recursos da lista de atendimento
 * bem como solicitações do usuário
 *
 * @author      Igor Maximo
 * @date        29/04/2021
 */
public class ListaAtendimento {

    // Para uso da recyclerView
    public ListaAtendimento(Bitmap icone, String nome, String titulo, String subtitulo, int fkCategoria, String descricao, Intent intent, Context context, String msgIncidente, boolean seHouveIncidente) {
        this.id = id;
        this.icone = icone;
        this.nome = nome;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.fkCategoria = fkCategoria;
        this.intent = intent;
        this.descricao = descricao;
        this.context = context;

        this.msgIncidente = msgIncidente;
        this.seHouveIncidente = seHouveIncidente;
    }


    // Atributos
    private int id;
    private Bitmap icone;
    private String nome;
    private String titulo;
    private String subtitulo;
    private int fkCategoria;
    private String descricao;
    private boolean seRespostaLivre;
    private Intent intent;
    private Context context;

    private String msgIncidente;
    private boolean seHouveIncidente;
    // Lista
    private ArrayList<String> dadosId;
    private ArrayList<Bitmap> dadosIcone;
    private ArrayList<String> dadosNome;
    private ArrayList<String> dadosTitulo;
    private ArrayList<String> dadosSubtitulo;
    private ArrayList<String> dadosDescricao;
    // Resposta
    private ArrayList<Integer> dadosRespostafkCategoria;
    private ArrayList<Integer> dadosFkCategoria;
    private ArrayList<String> dadosResposta;
    private ArrayList<Boolean> dadosSeRespostaLivre;

    private ArrayList<String> dadosMsgIncidentes;
    private ArrayList<Boolean> dadosSeHouveIndicentes;

    public String getMsgIncidente() {
        return msgIncidente;
    }

    public void setMsgIncidente(String msgIncidente) {
        this.msgIncidente = msgIncidente;
    }

    public boolean isSeHouveIncidente() {
        return seHouveIncidente;
    }

    public void setSeHouveIncidente(boolean seHouveIncidente) {
        this.seHouveIncidente = seHouveIncidente;
    }

    public int getFkCategoria() {
        return fkCategoria;
    }

    public void setFkCategoria(int fkCategoria) {
        this.fkCategoria = fkCategoria;
    }

    public ArrayList<String> getDadosMsgIncidentes() {
        return dadosMsgIncidentes;
    }

    public void setDadosMsgIncidentes(ArrayList<String> dadosMsgIncidentes) {
        this.dadosMsgIncidentes = dadosMsgIncidentes;
    }

    public ArrayList<Boolean> getDadosSeHouveIndicentes() {
        return dadosSeHouveIndicentes;
    }

    public void setDadosSeHouveIndicentes(ArrayList<Boolean> dadosSeHouveIndicentes) {
        this.dadosSeHouveIndicentes = dadosSeHouveIndicentes;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public boolean isSeRespostaLivre() {
        return seRespostaLivre;
    }

    public void setSeRespostaLivre(boolean seRespostaLivre) {
        this.seRespostaLivre = seRespostaLivre;
    }

    public ArrayList<Boolean> getDadosSeRespostaLivre() {
        return dadosSeRespostaLivre;
    }

    public void setDadosSeRespostaLivre(ArrayList<Boolean> dadosSeRespostaLivre) {
        this.dadosSeRespostaLivre = dadosSeRespostaLivre;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Integer> getDadosRespostafkCategoria() {
        return dadosRespostafkCategoria;
    }

    public void setDadosRespostafkCategoria(ArrayList<Integer> dadosRespostafkCategoria) {
        this.dadosRespostafkCategoria = dadosRespostafkCategoria;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getIcone() {
        return icone;
    }

    public void setIcone(Bitmap icone) {
        this.icone = icone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public int getfkCategoria() {
        return fkCategoria;
    }

    public void setfkCategoria(int fkCategoria) {
        this.fkCategoria = fkCategoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ArrayList<String> getDadosId() {
        return dadosId;
    }

    public void setDadosId(ArrayList<String> dadosId) {
        this.dadosId = dadosId;
    }

    public ArrayList<Bitmap> getDadosIcone() {
        return dadosIcone;
    }

    public void setDadosIcone(ArrayList<Bitmap> dadosIcone) {
        this.dadosIcone = dadosIcone;
    }

    public ArrayList<String> getDadosNome() {
        return dadosNome;
    }

    public void setDadosNome(ArrayList<String> dadosNome) {
        this.dadosNome = dadosNome;
    }

    public ArrayList<String> getDadosTitulo() {
        return dadosTitulo;
    }

    public void setDadosTitulo(ArrayList<String> dadosTitulo) {
        this.dadosTitulo = dadosTitulo;
    }

    public ArrayList<String> getDadosSubtitulo() {
        return dadosSubtitulo;
    }

    public void setDadosSubtitulo(ArrayList<String> dadosSubtitulo) {
        this.dadosSubtitulo = dadosSubtitulo;
    }
 

    public ArrayList<String> getDadosDescricao() {
        return dadosDescricao;
    }

    public void setDadosDescricao(ArrayList<String> dadosDescricao) {
        this.dadosDescricao = dadosDescricao;
    }
}
