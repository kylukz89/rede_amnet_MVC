package com.rede.App.View.DAO;


import android.graphics.Bitmap;
import android.net.Uri;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.ListaAtendimento;
import com.rede.App.View.JavaBeans.ListaAtendimentoResposta;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Classe responsável por gerenciar as operações
 * referentes aos atendimentos
 *
 * @author Igor Maximo
 * @date 30/04/2021
 */
public class AtendimentoDAO {

    final Usuario usuario = new Usuario();
    //
    public ListaAtendimento listaAtendimento = new ListaAtendimento(null, null, null, null, 0, "", null, null, null, false);
    public ListaAtendimentoResposta listaAtendimentoResposta = new ListaAtendimentoResposta(0, 0, "", false, null);

    /**
     * Retorna lista completa de opções de atendimento
     * para listar na tela do usuário
     *
     * @author Igor Maximo
     * @date 30/04/2021
     */
    public final ListaAtendimento getListaOpcoesAtendimento() throws Exception {
        ArrayList<Integer> dadosFkCategoria = new ArrayList<Integer>();
        ArrayList<Bitmap> dadosIcone = new ArrayList<Bitmap>();
        ArrayList<String> dadosNome = new ArrayList<String>();
        ArrayList<String> dadosTitulo = new ArrayList<String>();
        ArrayList<String> dadosSubtitulo = new ArrayList<String>();
        ArrayList<String> dadosDescricao = new ArrayList<String>();

        ArrayList<Boolean> dadosSeHouveIndicentes = new ArrayList<Boolean>();
        ArrayList<String> dadosMsgIncidentes = new ArrayList<String>();

        try {
            JSONArray jsonArray = new JSONArray(new RecebeJson().retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_LISTA_OPCOES_ATENDIMENTO, null));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                dadosFkCategoria.add(i, jsonObject.getInt("id"));
                dadosIcone.add(i, Ferramentas.convertBase64ToBitmap(jsonObject.getString("icone_blob")));
                dadosNome.add(i, jsonObject.getString("nome"));
                dadosTitulo.add(i, jsonObject.getString("titulo"));
                dadosSubtitulo.add(i, jsonObject.getString("subtitulo"));
                dadosDescricao.add(i, jsonObject.getString("descricao"));
                // Caso se houver incidentes
                dadosMsgIncidentes.add(jsonObject.getString("incidentes_cadastrados"));
                dadosSeHouveIndicentes.add(jsonObject.getBoolean("se_houve_incidentes"));
            }

            listaAtendimento.setDadosFkCategoria(dadosFkCategoria);
            listaAtendimento.setDadosIcone(dadosIcone);
            listaAtendimento.setDadosNome(dadosNome);
            listaAtendimento.setDadosTitulo(dadosTitulo);
            listaAtendimento.setDadosSubtitulo(dadosSubtitulo);
            listaAtendimento.setDadosDescricao(dadosDescricao);
            listaAtendimento.setDadosSeHouveIndicentes(dadosSeHouveIndicentes);
            listaAtendimento.setDadosMsgIncidentes(dadosMsgIncidentes);

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            throw new Exception("getListaOpcoesAtendimento() " + e);
        }
        return listaAtendimento;
    }

    /**
     * Retorna lista das opções de resposta
     * filtrados por categoria.
     *
     * @author Igor Maximo
     * @date 30/04/2021
     */
    public final ListaAtendimentoResposta getListaRespostasOpcoesAtendimento(String fkCategoria) throws Exception {
        ArrayList<Integer> dadosFkCategoriaResposta = new ArrayList<Integer>();
        ArrayList<Integer> dadosFkCategoria = new ArrayList<Integer>();
        ArrayList<String> dadosResposta = new ArrayList<String>();
        ArrayList<Boolean> dadosSeRespostaLivre = new ArrayList<Boolean>();

        try {
            JSONArray jsonArray = new JSONArray(new RecebeJson().retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_LISTA_OPCOES_ATENDIMENTO_CATEGORIA_ITEM_RESPOSTA,
                    new Uri.Builder()
                            .appendQueryParameter("fk_categoria", fkCategoria)));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                dadosFkCategoriaResposta.add(i, jsonObject.getInt("id"));
                dadosFkCategoria.add(i, jsonObject.getInt("fk_categoria"));
                dadosResposta.add(i, jsonObject.getString("resposta"));
                dadosSeRespostaLivre.add(i, (jsonObject.getInt("se_resposta_livre") == 1 ? true : false));
            }
            listaAtendimentoResposta.setDadosFkCategoriaResposta(dadosFkCategoriaResposta);
            listaAtendimentoResposta.setDadosFkCategoria(dadosFkCategoria);
            listaAtendimentoResposta.setDadosResposta(dadosResposta);
            listaAtendimentoResposta.setDadosSeRespostaLivre(dadosSeRespostaLivre);
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            throw new Exception("getListaRespostasOpcoesAtendimento() " + e);
        }
        return listaAtendimentoResposta;
    }

    /**
     * Realiza abertura de um chamado/atendimento
     *
     * @param cpfCnpj
     * @param codContrato
     * @param codContratoItem
     * @param fkCategoria
     * @param descricao
     * @param tipoDeAtendimento --> Se é do tipo "problema" ou "serviço"
     * @author Igor Maximo
     * @date 03/05/2021
     */
    public String[] setAbrirAtendimento(String cpfCnpj, String codContrato, String codContratoItem, String fkCategoria, String fkCategoriaResposta, String descricao, boolean tipoDeAtendimento, String[] dadosCadastrais) {
        String[] dados = new String[3];
        Uri.Builder paramsBuilder = new Uri.Builder()
                .appendQueryParameter("cpf_cnpj", cpfCnpj)
                .appendQueryParameter("contrato", codContrato)
                .appendQueryParameter("contratoItem", codContratoItem)
                .appendQueryParameter("fkCategoria", fkCategoria)
                .appendQueryParameter("fkCategoriaResposta", fkCategoriaResposta)
                .appendQueryParameter("descricao", descricao)
                .appendQueryParameter("dispositivo", Ferramentas.getMarcaModeloDispositivo(Splash.ctx))
                .appendQueryParameter("tipoDeAtendimento", (tipoDeAtendimento) ? "P" : "S")
                .appendQueryParameter("seComDadosConfirmacao", "1")
                .appendQueryParameter("campoEnderecoConfirmar", dadosCadastrais[0])
                .appendQueryParameter("campoNumeroConfirmar", dadosCadastrais[1])
                .appendQueryParameter("campoFoneConfirmar", dadosCadastrais[2])
                .appendQueryParameter("campoCelularConfirmar", dadosCadastrais[3])
                .appendQueryParameter("campoEmailConfirmar", dadosCadastrais[4])
                .appendQueryParameter("campoCelularFacil", dadosCadastrais[5]); // +1 appendQuery caso queira mais parâmetros
        try {
            JSONObject jsonObject = new JSONArray(new RecebeJson().retornaJSON(Rotas.ROTA_PADRAO + Rotas.INSERT_ABRIR_ATENDIMENTO, paramsBuilder)).getJSONObject(0);
            // Coleta retorno
            dados[0] = jsonObject.getString("status");
            dados[1] = jsonObject.getString("msg");
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), "(linha " + e.getStackTrace()[0].getLineNumber() + ") setAbrirAtendimento " + e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
        return dados;
    }

    /**
     * Retorna se o botão de atendimento deve aparecer
     *
     * @author Igor Maximo
     * @date 03/05/2021
     */
    public boolean getSeBotaoAtendimentoHabilitado() {
        try {
            return new JSONArray(new RecebeJson().retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_BOTAO_ATENDIMENTO, null)).getJSONObject(0).getBoolean("botao_ativo");
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.out.println(e);
        }
        return false;
    }
}