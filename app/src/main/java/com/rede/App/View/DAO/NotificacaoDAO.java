package com.rede.App.View.DAO;

import android.net.Uri;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.Notificacao;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Classe que pesquisa o que será exibido de alertas do app
 *
 * @author Igor Maximo
 * @date 30/03/2019
 * @edited 29/04/2019
 */
public class NotificacaoDAO {

    Notificacao notificacao = new Notificacao("", "", 0, false, 0, 0, 0, "", null);
    RecebeJson recebeJson = new RecebeJson();
    final Usuario usuario = new Usuario();

//
//    public final Notificacao retornaAlertasMassivos() throws Exception {
//        try {
//            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_ALERTAS_MASSIVOS, null);
//            JSONArray jsonArray = null;
//            jsonArray = new JSONArray(jsonInput);
//            JSONObject jsonObject = jsonArray.getJSONObject(0);
//
//            notificacao.setAlertaMassivo(jsonObject.getString("alerta"));
//
//        } catch (Exception e) {
//            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
//            throw new Exception(e);
//        }
//
//        return notificacao;
//    }
//
//    public final Avisos retornaAvisoFaltamDiasVencerFatura() throws Exception {
//        Uri.Builder paramsBuilder = new Uri.Builder().appendQueryParameter("codcli", usuario.getCodigo()); // +1 appendQuery caso queira mais parâmetros
//        try {
//            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_ALERTAS_PROXIMA_FATURA, paramsBuilder);
//            JSONArray jsonArray = null;
//            jsonArray = new JSONArray(jsonInput);
//            JSONObject jsonObject = jsonArray.getJSONObject(0);
//
//            avisos.setAlertaMassivo(jsonObject.getString("dias"));
//
//        } catch (Exception e) {
//            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
//            throw new Exception(e);
//        }
//
//        return avisos;
//    }
//
//    public final Notificacao retornaAlertaIndividualCliente(String codCli) throws Exception {
//        Uri.Builder paramsBuilder = new Uri.Builder().appendQueryParameter("codcli", codCli); // +1 appendQuery caso queira mais parâmetros
//        try {
//            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_ALERTA_INDIVIDUAL_CLIENTE, paramsBuilder);
//            JSONArray jsonArray = null;
//            jsonArray = new JSONArray(jsonInput);
//            JSONObject jsonObject = jsonArray.getJSONObject(0);
//
//            notificacao.setAlertaIndividual(jsonObject.getString("alerta"));
//            notificacao.setCodigoCliente(jsonObject.getString("codigo_cliente"));
//
//        } catch (Exception e) {
//            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
//            throw new Exception(e);
//        }
//
//        return notificacao;
//    }

    /**
     * Retorna todas as notificações pertinentes ao usuário
     */
    public final Notificacao getListaTodasNotificacoesUsuario() throws Exception {
        ArrayList<String> dadosNotificacao = new ArrayList<>();
        ArrayList<String> dadosData = new ArrayList<>();
        ArrayList<Boolean> dadosSeLido = new ArrayList<>();
        ArrayList<Integer> dadosIdsNotificacoes = new ArrayList<>();
        ArrayList<Integer> dadosSeConfirmadoNotificacao = new ArrayList<>();
        ArrayList<Integer> dadosSeDependeNotificacao = new ArrayList<>();
        ArrayList<Integer> dadosProtocolo = new ArrayList<>();
        ArrayList<String> dadosTipoNotificacao = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_TODAS_NOTIFICACOES_USUARIO,
                    new Uri.Builder().appendQueryParameter("codcli", usuario.getCodigo())));
            JSONObject jsonObject;

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                dadosNotificacao.add(i, jsonObject.getString("notificacao"));
                dadosData.add(i, jsonObject.getString("data"));
                dadosIdsNotificacoes.add(i, jsonObject.getInt("id"));
                dadosSeConfirmadoNotificacao.add(i, jsonObject.getInt("se_confirmado"));
                dadosSeDependeNotificacao.add(i, jsonObject.getInt("se_depende_confirmacao"));
                dadosTipoNotificacao.add(i, jsonObject.getString("tipo"));
                dadosProtocolo.add(i, jsonObject.getInt("protocolo"));
                // Se há notificações não lidas
                if (jsonObject.getInt("lido") == 1) {
                    dadosSeLido.add(i, true);
                } else {
                    dadosSeLido.add(i, false);
                }
            }

            notificacao.setDadosNotificacoes(dadosNotificacao);
            notificacao.setDadosData(dadosData);
            notificacao.setDadosIdsNotificacoes(dadosIdsNotificacoes);
            notificacao.setDadosSeConfirmadoNotificacao(dadosSeConfirmadoNotificacao);
            notificacao.setDadosSeDependeNotificacao(dadosSeDependeNotificacao);
            notificacao.setDadosProtocolo(dadosProtocolo); // FK_AGT_AGENDA -> ID auto incremento
            notificacao.setDadosTipoNotificacao(dadosTipoNotificacao); // Se Tarefa ou Avaliação
            notificacao.setDadosSeLido(dadosSeLido);
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            throw new Exception(e);
        }

        return notificacao;
    }

    /**
     * Marca as notificações como lida no banco
     *
     * @author Igor Maximo
     * @date 26/03/2020
     */
    public void setMarcaLido(ArrayList idsNotificacao) {
        Uri.Builder paramsBuilder = new Uri.Builder().appendQueryParameter("idsNotificacoes", String.valueOf(idsNotificacao)); // +1 appendQuery caso queira mais parâmetros
        try {
            recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.UPDATE_MARCA_NOTIFICACOES_LIDAS, paramsBuilder);
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString() + "marcaLido() ", usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.out.println(e);
        }
    }

    /**
     * Marca voto estrelas notificação do tipo avaliação
     *
     * @author      Igor Maximo
     * @date        12/08/2020
     */
    public boolean setGravaVotoAvaliacaoEstrelasServicoTecnico(int idNotificacao, int estrelas) {
        Uri.Builder paramsBuilder = new Uri.Builder()
                .appendQueryParameter("idNotificacao", String.valueOf(idNotificacao))
                .appendQueryParameter("estrelas", String.valueOf(estrelas)
                ); // +1 appendQuery caso queira mais parâmetros
        try {
            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.UPDATE_GRAVA_AVALIACAO_VISITA_TECNICA, paramsBuilder);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jsonInput);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            // Retorno do servidor
            return jsonObject.getBoolean("se_marcado");
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString() + "marcaLido() ", usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.out.println(e);
        }
        return false;
    }

    /**
     * Marca a notificação como SIM ou NÃO
     *
     * @author Igor Maximo
     * @date 25/07/2020
     */
    public boolean setMarcaNotificacaoSimouNao(int idNotificacao, String tipoNotificacao, int protocolo, int simOuNao) {
        Uri.Builder paramsBuilder = new Uri.Builder()
                .appendQueryParameter("idNotificacoes", idNotificacao + "")
                .appendQueryParameter("tipoNotificacao", tipoNotificacao)
                .appendQueryParameter("protocolo", protocolo + "")
                .appendQueryParameter("seSimOuNao", simOuNao + "")
                .appendQueryParameter("codCli", usuario.getCodigo() + ""); // +1 appendQuery caso queira mais parâmetros
        try {
            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.UPDATE_MARCA_MARCA_SIM_NAO, paramsBuilder);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jsonInput);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            if (jsonObject.getBoolean("se_marcado")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString() + "marcaRespostaNotificacao() ", usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.out.println(e);
        }
        return false;
    }
}