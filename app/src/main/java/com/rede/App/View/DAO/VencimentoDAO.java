package com.rede.App.View.DAO;


import android.net.Uri;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.Contrato;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.JavaBeans.Vencimento;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Carrega os planos do usuário do Integrator
 *
 * @author Igor Maximo
 * @criado 04/03/2019
 * @editado 04/03/2019
 */
public final class VencimentoDAO {

    RecebeJson recebeJson = new RecebeJson();
    final Usuario usuario = new Usuario();
    final Contrato plano = new Contrato();


    /**
     * Consulta os vencimentos existentes
     *
     * @author Igor maximo
     * @date 30/03/2020
     */
    public final Vencimento consultaVencimentosPorFormaCobranca(String formaCobranca) {
        ArrayList<Integer> dadosId = new ArrayList<Integer>();
        ArrayList<Integer> dadosVencDia = new ArrayList<Integer>();
        ArrayList<String> dadosFormaCobranca = new ArrayList<String>();
        ArrayList<String> dadosCodVenc = new ArrayList<String>();
        ArrayList<String> dadosRegra = new ArrayList<String>();

        Vencimento vencimentos = new Vencimento();

        try {
            Uri.Builder paramsBuilder = new Uri.Builder().appendQueryParameter("codcob", formaCobranca); // +1 appendQuery caso queira mais parâmetros
            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_VENCIMENTOS_POR_CODCOB, paramsBuilder);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jsonInput);
            int length = jsonArray.length();
            JSONObject jsonObject;

            for (int i = 0; i < length; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                dadosId.add(i, jsonObject.getInt("id"));
                dadosVencDia.add(i, jsonObject.getInt("venc_dia"));
                dadosFormaCobranca.add(i, jsonObject.getString("forma_cobranca"));
                dadosCodVenc.add(i, jsonObject.getString("cod_venc"));
//                dadosRegra.add(i, jsonObject.getString("regra"));
            }

            vencimentos.setDadosId(dadosId);
            vencimentos.setDadosVencDia(dadosVencDia);
            vencimentos.setDadosFormaCobranca(dadosFormaCobranca);
            vencimentos.setDadosCodVenc(dadosCodVenc);
            vencimentos.setDadosRegra(dadosRegra);

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.out.println("erro ==> " + e);
        }
        return vencimentos;
    }


    /**
     * Consulta se a forma de cobraça do plano do cliente
     * suporta mudanças (UPGRADE DE PLANO - BANDA)
     *
     * @author      Igor Maximo
     * @date        28/04/2020
     */
    public final boolean retornaSePlanoSuportaUpgradePorCodCob(String formaCobranca) {
        try {
            Uri.Builder paramsBuilder = new Uri.Builder().appendQueryParameter("codcob", formaCobranca); // +1 appendQuery caso queira mais parâmetros
            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_SE_FORMA_COBRANCA_PLANO_SUPORTA_UPGRADE, paramsBuilder);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jsonInput);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            if (jsonObject.getBoolean("se_plano_suporta_upgrade")) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }

        return false;
    }

    /**
     * Consulta os vencimentos existentes
     *
     * @author Igor maximo
     * @date 30/03/2020
     */
    public final Vencimento consultaVencimentos() {
        ArrayList dadosCodVenc = new ArrayList();
        ArrayList dadosDiasVenc = new ArrayList();
        Vencimento vencimentos = new Vencimento();

        try {
            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_VENCIMENTOS, null);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jsonInput);
            int length = jsonArray.length();
            JSONObject jsonObject;

            for (int i = 0; i < length; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                dadosCodVenc.add(i, jsonObject.getString("codvenc"));
                dadosDiasVenc.add(i, jsonObject.getInt("dia"));
            }

            vencimentos.setDadosCodVenc(dadosCodVenc);
            vencimentos.setDadosVencDia(dadosDiasVenc);

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.out.println("erro ==> " + e);
        }
        return vencimentos;
    }
}
