package com.rede.App.View.DAO;

import android.net.Uri;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.AlteraSenha;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Altera as credenciais do usuário
 *
 * @author Igor Maximo
 * @date 19/02/2019
 */
public class AlteraSenhaDAO {

    RecebeJson recebeJson = new RecebeJson();
    final Usuario usuario = new Usuario();

    /**
     * Altera as credenciais do usuário
     *
     * @author Igor Maximo
     * @date 25/01/2021
     */
    public Object[] setAlteraSenhaCentralAssinante(AlteraSenha altsen) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.UPDATE_ALTERA_SENHA, new Uri.Builder()
                    .appendQueryParameter("cpf_cnpj", altsen.getCpfCnpjCliente())
                    .appendQueryParameter("atual", altsen.getSenha())
                    .appendQueryParameter("nova", altsen.getRepitaNovaSenha())))
                    .getJSONObject(0);
            return new Object[]{jsonObject.getBoolean("status"), Ferramentas.setCapitalizarTexto(jsonObject.getString("msg").toLowerCase())};
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.out.println("alteraSenhaCentralAssinante() " + e.getMessage());
        }
        return new Object[]{false, "Erro interno!"};
    }


    /**
     * Força servidor a enviar a senha via e-mail
     *
     * @author      Igor Maximo
     * @date        25/01/2021
     */
    public Object[] setEnviarSenhaParaEmail(AlteraSenha altsen) {
        try {
            JSONObject jsonObject = new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.EXECUTE_ENVIA_SENHA_EMAIL, new Uri.Builder()
                    .appendQueryParameter("cpf_cnpj", altsen.getCpfCnpjCliente()))).getJSONObject(0);
            return new Object[]{jsonObject.getBoolean("status"), jsonObject.getString("email")};
//            if (jsonObject.getString("enviada_email").equals("true")) {
//                Toast.makeText(altsen.getCtx(), "Senha enviada para o e-mail: " + jsonObject.getString("email"), Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(altsen.getCtx(), "Você não possui um e-mail cadastrado conosco!", Toast.LENGTH_SHORT).show();
//            }
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.out.println("alteraSenhaCentralAssinante() " + e.getMessage());
        }
        return new Object[]{false, "Erro interno!"};
    }
}
