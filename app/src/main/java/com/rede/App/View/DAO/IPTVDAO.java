package com.rede.App.View.DAO;


import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;

/**
 * Classe responsável por gerenciar as operações
 * referentes ao IPTV
 *
 * @author Igor Maximo
 * @date 30/04/2021
 */
public class IPTVDAO {

    final Usuario usuario = new Usuario();

    /**
     * Retorna se o botão de IPTV deve aparecer
     *
     * @author      Igor Maximo
     * @date        03/05/2021
     */
    public boolean getSeBotaoIPTVHabilitado() {
        try {
            return new JSONArray(new RecebeJson().retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_BOTAO_IPTV, null)).getJSONObject(0).getBoolean("botao_ativo");
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.out.println(e);
        }
        return false;
    }
}