package com.rede.App.View.DAO;

import android.net.Uri;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.CartaoRecorrente;
import com.rede.App.View.JavaBeans.EditarDadosContato;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Carrega os dados do usuário do Integrator
 * Salva as credenciais do usuário
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 * @editado 04/03/2019
 */
public final class UsuarioDAO {

    RecebeJson recebeJson = new RecebeJson();
    final Usuario usuario = new Usuario();




    public final void getCarregaDadosUsuario() throws Exception {
        try {
            JSONObject jsonObject = new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_DADOS_CLIENTE, new Uri.Builder()
                    .appendQueryParameter("cpf_cnpj", usuario.getCpfCnpj()))).getJSONObject(0);

            usuario.setStatus(jsonObject.getString("STATUS"));
            usuario.setCodigo(jsonObject.getString("COD_CLIE"));
            usuario.setNome(jsonObject.getString("RZAO_SOCL_CLIE"));
            usuario.setCpfCnpj(jsonObject.getString("CNPJ_CPF_CLIE"));
            usuario.setEndereco(jsonObject.getString("ENDER_FAT_LOGR_NOM"));
            usuario.setBairro(jsonObject.getString("ENDER_FAT_BAIR_NOM"));
            usuario.setNumero(jsonObject.getString("ENDER_FAT_NUM"));
            usuario.setCep(jsonObject.getString("ENDER_FAT_CEP"));
            usuario.setCidade(jsonObject.getString("ENDER_FAT_CDE_NOM") + "/" + jsonObject.getString("ENDER_FAT_UF"));
            usuario.setFone(jsonObject.getString("TELEFONE1"));
            // Campos de contato
            usuario.setCelular1(jsonObject.getString("TELEFONE1"));
            usuario.setCelular2(jsonObject.getString("TELEFONE2"));
            usuario.setCelular3(jsonObject.getString("TELEFONE3"));
            usuario.setCelular4(jsonObject.getString("TELEFONE4"));
            if (jsonObject.getString("TELEFONE1").length() >= 9) {
                usuario.setCelular(jsonObject.getString("TELEFONE1").trim().replace("{", "").replace("}", "").replace("\"", "").replace(":", "").replace("(", "").replace(")", ""));
            } else {
                usuario.setCelular(jsonObject.getString("TELEFONE1"));
            }
            if (jsonObject.getString("ASSINANTE_EMAIL").contains("@")) {
                usuario.setEmail(jsonObject.getString("ASSINANTE_EMAIL"));
            } else {
                usuario.setEmail("");
            }
            // Coleta dados de pagamento recorrente, se houver no simetra
            ArrayList<CartaoRecorrente> cartoesRecorrentesLista = new ArrayList<>();
            JSONArray schedule_Array = jsonObject.getJSONArray("FAT_CLIENTE_CARTAO");
            for (int i = 0; i < schedule_Array.length(); i++) {
                JSONObject jOBJ = schedule_Array.getJSONObject(i);
                // Carrega entidade com os cartões retornados do cadastro da VINDI via api do SIMETRA
                CartaoRecorrente cartaoRecorrente = new CartaoRecorrente(null, null, null, null, null, null, null, null, null, false, null, null, null, null);
                // Seta valores da entidade
                cartaoRecorrente.setCartaoNumero(jOBJ.getString("CARTAO_NRO"));
                cartaoRecorrente.setCartaoBandeira(jOBJ.getString("CARTAO_BANDEIRA"));
                cartaoRecorrente.setCartaoDataValidade(jOBJ.getString("CARTAO_DATA_VALIDADE"));
                cartaoRecorrente.setCartaoPlataforma(jOBJ.getString("CARTAO_PLATAFORMA"));
                cartaoRecorrente.setSeContratoVinculado(jOBJ.getString("CONTRATO_VINCULADO").equals("1"));
                cartaoRecorrente.setCartaoImagemBase64Bandeira(Ferramentas.convertBase64ToBitmap(jOBJ.getString("IMG_BANDEIRA")));
                cartaoRecorrente.setCodigoCliente(jOBJ.getString("COD_CLIE_CARTAO"));
                cartaoRecorrente.setIdClienteVindi(jOBJ.getString("ID_CLIENTE_VINDI"));
                cartaoRecorrente.setIdCartaoVindi(jOBJ.getString("ID_CARTAO_VINDI"));
                cartaoRecorrente.setFkEmpresaCNPJ(jOBJ.getString("VINDI_QUAL_NOSSA_EMPRESA"));
                // Adiciona no arraylist
                cartoesRecorrentesLista.add(cartaoRecorrente);
            }
            usuario.setCartoesRecorrentes(cartoesRecorrentesLista);
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            throw new Exception(e);
        }
    }

    /**
     * Realiza alteração dos dados cadastrais de contato
     * do cliente no simetra
     *
     * @author Igor Maximo
     * @date 13/05/2021
     */
    public Object[] setEditarDadosContato(EditarDadosContato dadosCadastraisContato) {
        Object[] dados = new String[2];
        Uri.Builder paramsBuilder = new Uri.Builder()
                .appendQueryParameter("versao", "1")
                .appendQueryParameter("cpfcnpj", String.valueOf(dadosCadastraisContato.getCpfCnpj()))
                .appendQueryParameter("codClie", String.valueOf(dadosCadastraisContato.getCodClie()))
                .appendQueryParameter("celular1", String.valueOf(dadosCadastraisContato.getCelular1()))
                .appendQueryParameter("celular2", String.valueOf(dadosCadastraisContato.getCelular2()))
                .appendQueryParameter("celular3", String.valueOf(dadosCadastraisContato.getCelular3()))
                .appendQueryParameter("celular4", String.valueOf(dadosCadastraisContato.getCelular4()))
                .appendQueryParameter("email", String.valueOf(dadosCadastraisContato.getEmail())); // +1 appendQuery caso queira mais parâmetros
        try {
            JSONObject jsonObject = new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.UPDATE_DADOS_CONTATO, paramsBuilder)).getJSONObject(0);
            // Coleta retorno
            dados[0] = jsonObject.getString("status");
            dados[1] = jsonObject.getString("msg");
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), "(linha " + e.getStackTrace()[0].getLineNumber() + ") setEditarDadosContato " + e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
        return dados;
    }
}