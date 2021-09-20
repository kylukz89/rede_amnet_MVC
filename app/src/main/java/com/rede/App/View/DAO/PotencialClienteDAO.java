package com.rede.App.View.DAO;


import android.net.Uri;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.PotencialCliente;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Classe responsável por cadastrar e manipular um potencial cliente cadastrado no app
 */
public class PotencialClienteDAO {

    RecebeJson recebeJson = new RecebeJson();
    final Usuario usuario = new Usuario();

    /**
     * Realiza o cadastro de um potencial cliente no banco de dados
     */
    public String cadastraPotencialCliente(PotencialCliente potencialCliente) {
        try {
            Uri.Builder paramsBuilder = new Uri.Builder()
                    .appendQueryParameter("tipo", potencialCliente.getTipoCliente())
                    .appendQueryParameter("cpfCnpj", potencialCliente.getCampoCPFCNPJ())
                    .appendQueryParameter("rg", potencialCliente.getCampoRG())
                    .appendQueryParameter("nome_completo", potencialCliente.getCampoNomeCompleto())
                    .appendQueryParameter("email", potencialCliente.getCampoEmail())
                    .appendQueryParameter("nascimento", potencialCliente.getCampoNascimento())
                    .appendQueryParameter("celular", potencialCliente.getCampoCelular()); // +1 appendQuery caso queira mais parâmetros


            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.INSERT_DADOS_CADASTRO_POTENCIAL_CLIENTE, paramsBuilder);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jsonInput);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            return jsonObject.getString("resultado");

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.err.println(e);
        }
        return null;
    }

    /**
     * Retorna se cliente existe no banco
     */
    public boolean retornaSeCPFExisteClientePotencial(PotencialCliente potencialCliente) {
        boolean existe = false;
        try {
            Uri.Builder paramsBuilder = new Uri.Builder()
                    .appendQueryParameter("cpfCnpj", potencialCliente.getCampoCPFCNPJ()); // +1 appendQuery caso queira mais parâmetros

            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_SE_CLIENTE_EXISTE_BANCO_CLIENTEPOTENCIAL, paramsBuilder);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jsonInput);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            existe = jsonObject.getBoolean("existe");

            System.err.println("===========================> "+jsonObject.getBoolean("existe"));

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.err.println(e);
        }
        return existe;
    }

    /**
     * Retorna dados do cliente potencial, se ele existir no banco
     */
    public PotencialCliente retornaDadosClientePotencial(PotencialCliente potencialCliente) {
        try {
            Uri.Builder paramsBuilder = new Uri.Builder()
                    .appendQueryParameter("cpfCnpj", potencialCliente.getCampoCPFCNPJ()); // +1 appendQuery caso queira mais parâmetros
            System.out.println("retornaDadosClientePotencial getCampoCPFCNPJ ====================> "+potencialCliente.getCampoCPFCNPJ());
            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_DADOS_CLIENTEPOTENCIAL, paramsBuilder);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jsonInput);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            potencialCliente.setTipoCliente(jsonObject.getString("tipo"));
            potencialCliente.setCampoCPFCNPJ(jsonObject.getString("cpf_cnpj"));
            potencialCliente.setCampoRG(jsonObject.getString("rg"));
            potencialCliente.setCampoNomeCompleto(jsonObject.getString("nome"));
            potencialCliente.setCampoEmail(jsonObject.getString("email"));
            potencialCliente.setCampoNascimento(jsonObject.getString("nascimento"));
            potencialCliente.setCampoCelular(jsonObject.getString("celular"));

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.err.println(e);
        }

        return potencialCliente;
    }
}
