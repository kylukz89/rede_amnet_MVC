package com.rede.App.View.DAO;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.HabProvisoria;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Habilita provisoriamente um plano suspenso por débito menos de 30 dias
 *
 * @author Igor Maximo
 * @criado 10/03/2019
 */
public class HabProvisoriaDAO {

    RecebeJson recebeJson = new RecebeJson();
    final Usuario usuario = new Usuario();
    public HabProvisoria hprov = new HabProvisoria();

    /**
     * Retorna todos os planos suspensos por débito do cliente
     *
     * @author Igor Maximo
     * @criado 10/03/2019
     */
    public final HabProvisoria getPlanosSuspensosPorDebito() throws Exception {
        ArrayList planosSuspensos = new ArrayList();
        ArrayList planosSuspensosCodSerCli = new ArrayList();

        try {
            JSONArray jsonArray = new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_PLANOS_SUSPENSOS_DEBITO_HABILITACAO_PROVISORIA,
                    new Uri.Builder().appendQueryParameter("codcli", usuario.getCpfCnpj())));
            JSONObject jsonObject = null;

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                planosSuspensos.add(i, jsonObject.getString("PRODUTO_NOME"));
                planosSuspensosCodSerCli.add(i, jsonObject.getString("COD_CNTR"));
            }

            hprov.setCodigoCliente(usuario.getCodigo());
            hprov.setPlanosSuspensosPorDebito(planosSuspensos);
            hprov.setPlanosSuspensosPorDebitoCodSerCli(planosSuspensosCodSerCli);

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            if (e.toString().matches("org.json.JSONException: Value null at 0 of type org.json.JSONObject") == true) {
                throw new Exception("Não há planos suspensos por débito! ");
            }
        }
        return hprov;
    }

    /**
     * Habilita plano suspenso por débito
     */
    public void executaHabilitacaoProvisoria(Context ctx) {
        JSONObject jsonObject = null;
        try {
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.EXECUTE_HABILITACAO_PROVISORIA, new Uri.Builder()
                    .appendQueryParameter("codsercli", hprov.getCodSerCli())
                    .appendQueryParameter("codcli", usuario.getCpfCnpj())));
            jsonObject = jsonArray.getJSONObject(0);

            Toast.makeText(ctx, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
