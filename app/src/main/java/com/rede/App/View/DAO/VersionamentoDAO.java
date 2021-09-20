package com.rede.App.View.DAO;


import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.JavaBeans.Versionamento;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Carrega a versao que usuário está usando
 *
 *  Ação: select status from versionamento order by id desc limit 1
 *
 * @author Igor Maximo
 * @criado 06/03/2019
 * @editado 31/03/2019
 */

public final class VersionamentoDAO {

    final Usuario usuario = new Usuario();

    public final Versionamento consultaVersionamentoEStatus(Versionamento versionamento) throws Exception {
        try {
            JSONArray jsonArray = new JSONArray(new RecebeJson().retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_VERSIONAMENTO, null));
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            versionamento.setVersao_maior(Integer.parseInt(jsonObject.getString("min_versao_maior"))); // 1.
            versionamento.setVersao_menor(Integer.parseInt(jsonObject.getString("min_versao_menor"))); // 0.
            versionamento.setCorrecao(Integer.parseInt(jsonObject.getString("min_versao_correcao")));  // 0
            versionamento.setStatus(jsonObject.getString("min_versao_status"));                        // ATIVO


        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.err.println(e);
        }
        return versionamento;
    }
    /*
    SELECT
        min(id),
        min(versao_maior),
        min(versao_menor),
        min(correcao),
        min(status)
         FROM versionamento where status = 'ATIVO' limit 1;
     */
}
