package com.rede.App.View.DAO;

import android.net.Uri;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Manipulação das chaves de antivírus da Living Safe
 *
 * @author      Igor Maximo
 * @date        26/02/2021
 */
public class AntivirusDAO {

    RecebeJson recebeJson = new RecebeJson();
    final Usuario usuario = new Usuario();

    /**
     * Gera chave de antivírus e envia por e-mail
     *
     * @author      Igor Maximo
     * @date        26/02/2021
     */
    public Object[] setGerarChaveAntivirusEmail(String cpfCnpj, String codProd, String codContrato, String codContratoItem) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.INSERT_SOLICITAR_ANTIVIRUS_CHAVE,
                    new Uri.Builder()
                            .appendQueryParameter("cpfCnpj", cpfCnpj)
                            .appendQueryParameter("codProd", codProd)
                            .appendQueryParameter("codContrato", codContrato)
                            .appendQueryParameter("codContratoItem", codContratoItem)
            )).getJSONObject(0);
            return new Object[]{jsonObject.getBoolean("status"), jsonObject.getString("msg")};
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.out.println("setGerarChaveAntivirusEmail() " + e.getMessage());
        }
        return new Object[]{false, "Erro interno!"};
    }
}
