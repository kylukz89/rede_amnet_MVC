package com.rede.App.View.DAO;

import android.net.Uri;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.SpeedTest;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *  Classe responsável por armazenar todas as vezes que o usuário utiliza o speed test
 *
 * @author Igor Maximo
 * @date 11/06/2019
 */
public class SpeedTestDAO {

    RecebeJson recebeJson = new RecebeJson();
    Usuario usuario = new Usuario();

    /**
     * Armazena o resultado do speed test no banco do servidor
     *
     */
    public void armazenaResultadoSpeedTestBD(SpeedTest speedTest) {

        Uri.Builder paramsBuilder = new Uri.Builder()
                .appendQueryParameter("cpf", speedTest.getCodCli())
                .appendQueryParameter("nome_cli", speedTest.getNomeCliente())
                .appendQueryParameter("ping", speedTest.getPing())
                .appendQueryParameter("resultado_download", speedTest.getDownload())
                .appendQueryParameter("resultado_upload", speedTest.getUpload())
                .appendQueryParameter("ip", speedTest.getIP()); // +1 appendQuery caso queira mais parâmetros
        try {
            recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.INSERT_DADOS_SPEED_TEST, paramsBuilder);
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.out.println(e);
        }
    }

    /**
     * Armazena o resultado do speed test apenas se for do MAC gravado no banco
     *
     */
    public String retornaMACAccessPointBanco() {
        try {
            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_MAC_ROTEADOR_BANCO, null);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jsonInput);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            return jsonObject.getString("mac_ap");

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.out.println(e);
        }
        return null;
    }

    /**
     * Armazena o resultado do speed test apenas se for do IP gravado no banco
     *
     */
    public String retornaIPAccessPointBanco() {
        try {
            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_IP_ROTEADOR_BANCO, null);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jsonInput);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            return jsonObject.getString("ip_ap");

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.out.println(e);
        }
        return null;
    }

    /**
     * Armazena o SSID gravado no banco
     *
     */
    public String retornaSSIDAccessPointBanco() {
        try {
            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_SSID_ROTEADOR_BANCO, null);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jsonInput);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            return jsonObject.getString("ssid_ap");

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.out.println(e);
        }
        return null;
    }



    /**
     * Retorna se o botão de Speed Test deve estar aparecendo na tela principal
     *
     */
    public boolean retornaSeBotaoSpeedTestHabilitado() {
        try {
            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_BOTAO_SPEEDTEST, null);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jsonInput);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            System.err.println("botao_ativo =================> " + jsonObject.getBoolean("botao_ativo"));

            return jsonObject.getBoolean("botao_ativo");

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.out.println(e);
        }
        return false;
    }

}
