package com.rede.App.View.DAO;

import android.net.Uri;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.ExtratoConexao;
import com.rede.App.View.JavaBeans.Contrato;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Classe que pesquisa os extratos de conexão de diferentes planos
 *
 * @author Igor Maximo
 * @criado 30/03/2019
 * @editado 30/03/2019
 */
public class ExtratoConexaoDAO {

    RecebeJson recebeJson = new RecebeJson();
    private Contrato plano = new Contrato();
    final Usuario usuario = new Usuario();
    ExtratoConexao extratoConexao = new ExtratoConexao();

    public ExtratoConexao carregaExtratoConexaoPlano(String from, String to) throws Exception {
        ArrayList dadosPeriodo = new ArrayList();
        ArrayList dadosPPPoE = new ArrayList();
        ArrayList dadosTempo = new ArrayList();
        ArrayList dadosTrafegoDown = new ArrayList();
        ArrayList dadosTrafegoUp = new ArrayList();


        try {
            Uri.Builder paramsBuilder = new Uri.Builder()
                    .appendQueryParameter("pppoe", plano.getLoginrad())
                    .appendQueryParameter("from", from)
                    .appendQueryParameter("to", to); // +1 appendQuery caso queira mais parâmetros

            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_EXTRATO_CONEXAO_PPPOE, paramsBuilder);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jsonInput);
            int length = jsonArray.length();
            JSONObject jsonObject = null;


            for (int i = 0; i < length; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                dadosPeriodo.add(i, jsonObject.getString("periodo"));
                dadosPPPoE.add(i, jsonObject.getString("pppoe"));
                dadosTempo.add(i, jsonObject.getString("tempo"));
                dadosTrafegoDown.add(i, jsonObject.getString("trafego_down"));
                dadosTrafegoUp.add(i, jsonObject.getString("trafego_up"));
            }

            extratoConexao.setDadosPeriodo(dadosPeriodo);
            extratoConexao.setDadosPPPoE(dadosPPPoE);
            extratoConexao.setDadosTempo(dadosTempo);
            extratoConexao.setDadosTrafegoDown(dadosTrafegoDown);
            extratoConexao.setDadosTrafegoUp(dadosTrafegoUp);
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            throw new Exception("carregaExtratoFinanceiroUsuario() " + e);
        }

        return extratoConexao;
    }


}
