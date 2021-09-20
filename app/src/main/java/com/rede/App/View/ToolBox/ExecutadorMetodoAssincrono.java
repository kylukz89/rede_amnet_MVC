package com.rede.App.View.ToolBox;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Classe assíncrona responsável por processar qualquer
 * tipo de requisição ao servidor de forma global e neutra
 *
 * @author Igor Maximo
 * @return Object
 * @date 27/02/2021
 */
public class ExecutadorMetodoAssincrono extends AsyncTask<ArrayList<Object>, Integer, Object> {

    private Object retorno = null;

    /**
     * Executa qualquer método de forma assíncrona com loader
     *
     * @param instanciaClasse       classe que contém os métodos
     * @param nomeMetodo            Nome do método que deve ser executado
     * @param parametros            Lista de parâmetros que são executados no método informado
     * @param tiposParametros       Lista dos tipos de cada parâmetro informado
     * @param context               Activity contexto que foi usado e deverá ser exibido o spinner
     * @author      Igor Maximo
     * @date        01/03/2021
     */
    public Object setExecutarMetodo(Object instanciaClasse, String nomeMetodo, Object[] parametros, Object[] tiposParametros, Context context) {
//        Object retorno = null;
        // Execução do método
        ArrayList<Object> listaParamFunc = new ArrayList<>();
        // Objeto instância da classe
        listaParamFunc.add(0, instanciaClasse);
        // Nome do método
        listaParamFunc.add(1, nomeMetodo);
        // Tipos de cada parâmetro de entrada
        listaParamFunc.add(2, tiposParametros);
        // Parâmetros de entrada
        listaParamFunc.add(3, parametros);
        new ExecutadorMetodoAssincrono(context).execute(listaParamFunc);
        System.out.println("retorno===123" + this.retorno);
//        try {
//            retorno = new ExecutadorMetodoAssincrono(context).execute(listaParamFunc).get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return this.retorno;
    }

    public ProgressDialog mProgress = null;
    @SuppressLint("StaticFieldLeak")
    Context mContext;

    public ExecutadorMetodoAssincrono(Context context) {
        mContext = context;
    }

    @SafeVarargs
    @Override
    protected final Object doInBackground(ArrayList<Object>... dados) {
        try {

            int i = 0;
            while (i < 9999999) {
                 i++;
            }

            this.retorno = setMetodo(
                    dados[0].get(0),
                    String.valueOf(dados[0].get(1)),
                    (Class[]) dados[0].get(2),
                    (Object[]) dados[0].get(3)
            );
//            System.out.println("retorno===" + this.retorno);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.retorno;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        mProgress.setProgress(progress[0]);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage("Processando...");
        mProgress.setIndeterminate(false);
        mProgress.setCancelable(false);
        mProgress.show();
    }

    @Override
    protected void onPostExecute(Object result) {
        this.retorno = result;
        mProgress.dismiss();
    }

    private Object setMetodo(Object classe, String nomeMetodo, Class[] tiposParametros,  Object[] valoresParametros) {
        Object retorno = null;
        try {
            retorno = setMetodoExecutar(
                    classe,
                    classe.getClass().getMethod(nomeMetodo, tiposParametros),
                    valoresParametros
            );
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return retorno;
    }
    private Object setMetodoExecutar(Object object, Method metodo, Object[] valoresParametros) {
        Object retorno = null;
        try {
            retorno = metodo.invoke(object, valoresParametros);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return retorno;
    }
}
