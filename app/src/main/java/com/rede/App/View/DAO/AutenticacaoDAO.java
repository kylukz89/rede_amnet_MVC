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
 * Autentica e salva as credenciais do usuário
 *
 * @author      Igor Maximo
 * @date        19/02/2019
 */
public final class AutenticacaoDAO {

    final Usuario autenticacao = new Usuario();
    final Usuario usuario = new Usuario();
    RecebeJson recebeJson = new RecebeJson();

    /**
     * Retorna a aut do cliente para comparar com a informada
     *
     * @author Igor Maximo
     */
    public final Object[] getCarregaDadosAutenticacaoUsuario(Usuario usuario) throws Exception {
        try {
            JSONObject jsonObject = new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_AUTENTICACAO,
                    new Uri.Builder()
                    .appendQueryParameter("cpf_cnpj", usuario.getCpfCnpj())
                    .appendQueryParameter("dispositivo", Ferramentas.getMarcaModeloDispositivo(Splash.ctx))))
                    .getJSONObject(0);
            autenticacao.setCpfCnpjAutenticacao(jsonObject.getString("CNPJ_CPF_CLIE"));
            autenticacao.setSenhaAutenticacao(jsonObject.getString("ASSINANTE_SENHA_PORTAL"));
            autenticacao.setCodigo(jsonObject.getString("COD_CLIE"));
            autenticacao.setNome(jsonObject.getString("RZAO_SOCL_CLIE"));

            if (jsonObject.getString("COD_CLIE").length() > 3 && jsonObject.getString("COD_CLIE") != null && jsonObject.getString("COD_CLIE") != "null") {
                return new Object[] {true, autenticacao};
            } else {
                return new Object[] {false, autenticacao};
            }
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            throw new Exception(e);
        }
    }

    /**
     *  Apenas envia ao servidor que um usuário deslogou do aplicativo
     *
     *  @author Igor Maximo
     */
    public final void registraLOGDeslogarUsuario(Usuario usuario) throws Exception {
        try {
            recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.UPDATE_DESLOGA_USUARIO, new Uri.Builder().appendQueryParameter("codcli", usuario.getCodigo()));
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            throw new Exception(e);
        }
    }


    /**
     * Insere/Atualiza o token do firebase por usuário.
     *
     * @author Igor Maximo
     * @date 25/10/2019
     */
    public void setAtualizaTokenDBFirebase(String codCli, String tokenFirebase) {
        try {
            new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.UPDATE_TOKEN_FIREBASE, new Uri.Builder()
                    .appendQueryParameter("user", codCli)
                    .appendQueryParameter("token", tokenFirebase)));
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), "setAtualizaTokenDBFirebase " + e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
    }
}
