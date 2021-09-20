package com.rede.App.View.DAO;

import android.net.Uri;
import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.Routes.Rotas;

/*
 * Classe responsável por gravar todos os erros
 */
public abstract class AppLogErroDAO {

    public static void gravaErroLOGServidor(String tipo, String descricao, String codCli, String aparelho) {
        RecebeJson recebeJson = new RecebeJson();
        try {
            Uri.Builder paramsBuilder = new Uri.Builder()
                    .appendQueryParameter("tipo", tipo)
                    .appendQueryParameter("descricao", descricao)
                    .appendQueryParameter("codcli", codCli)
                    .appendQueryParameter("aparelho", aparelho); // +1 appendQuery caso queira mais parâmetros

            recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.INSERT_ERRO_LOG, paramsBuilder);

        } catch (Exception e) {
        }
    }
}
