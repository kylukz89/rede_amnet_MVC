package com.rede.App.View.DAO;


import android.net.Uri;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.Adesao;
import com.rede.App.View.JavaBeans.Contrato;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Carrega os planos do usuário
 *
 * @author Igor Maximo
 * @date 04/03/2019
 */
public final class ContratoDAO {

    RecebeJson recebeJson = new RecebeJson();
    final Usuario usuario = new Usuario();
    final Contrato plano = new Contrato();

    /**
     * Carrega detalhes do plano escolhido
     *
     * @author Igor Maximo
     * @date 21/01/2021
     */
    public final void retornaPlanoUsuarioPorCodSerCli() throws Exception {
        try {
            JSONObject jsonObject = new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_DADOS_PLANO_CLIENTE_CODSERCLI, new Uri.Builder()
                    .appendQueryParameter("cpf_cnpj", usuario.getCpfCnpj())
                    .appendQueryParameter("codsercli", plano.getCodserCli())))
                    .getJSONObject(0);

            plano.setCodigoCliente(usuario.getCodigo());
            plano.setStatus_plano(Ferramentas.setCapitalizarTexto(jsonObject.getString("DESCR_STAT_CNTR")));
            plano.setNomePlano(Ferramentas.setCapitalizarTexto(jsonObject.getString("PRODUTO_NOME")));
            plano.setEnderecoInstalacao(Ferramentas.setCapitalizarTexto(jsonObject.getString("ENDER_INSTALACAO_LOGR_TIPO") + " " + jsonObject.getString("ENDER_INSTALACAO_LOGR_NOM") + ", " + jsonObject.getString("ENDER_INSTALACAO_NUM")));
            plano.setBairroInstalacao(Ferramentas.setCapitalizarTexto(jsonObject.getString("ENDER_INSTALACAO_BAIR_NOM")));
            plano.setCepInstalacao(jsonObject.getString("ENDER_COB_CEP"));
            plano.setCidade(Ferramentas.setCapitalizarTexto(jsonObject.getString("ENDER_INSTALACAO_CDE_NOM")));
            plano.setCodserCli(jsonObject.getString("COD_CNTR"));
            plano.setVencimento(jsonObject.getString("DIA_VENC"));
            plano.setValorPlano(jsonObject.getString("VLR_CNTR"));
            plano.setValorFinal(jsonObject.getString("VLR_CNTR"));
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            throw new Exception(e);
        }
    }

    /**
     * Retorna todos os planos disponíveis para venda/alteração
     *
     * @author Igor Maximo
     * @date 23/04/2020
     */
    public final Adesao retornaTodosPlanosVenda(String categoria) throws Exception {
        Adesao adesao = null;
        ArrayList dadosPlanoAlteracaoId = new ArrayList();
        ArrayList dadosPlanoAlteracaoNome = new ArrayList();
        ArrayList dadosPlanoAlteracaoValor = new ArrayList();
        ArrayList dadosPlanoAlteracaoDownload = new ArrayList();
        ArrayList dadosPlanoAlteracaoUpload = new ArrayList();
        ArrayList dadosPlanoAlteracaoTecnologia = new ArrayList();
        ArrayList dadosPlanoAlteracaoCategoria = new ArrayList();
        ArrayList dadosPlanoAlteracaoInstalacao = new ArrayList();
        ArrayList dadosPlanoAlteracaoCodIspTelecom = new ArrayList();

        try {
            JSONArray jsonArray = new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_DADOS_PLANOS_VENDAS_ONLINE, new Uri.Builder().appendQueryParameter("categoria", categoria)));
            JSONObject jsonObject = null;

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                dadosPlanoAlteracaoId.add(i, jsonObject.getInt("id"));
                dadosPlanoAlteracaoNome.add(i, jsonObject.getString("nome"));
                dadosPlanoAlteracaoValor.add(i, jsonObject.getString("valor"));
                dadosPlanoAlteracaoDownload.add(i, jsonObject.getString("download"));
                dadosPlanoAlteracaoUpload.add(i, jsonObject.getString("upload"));
                dadosPlanoAlteracaoTecnologia.add(i, jsonObject.getString("tecnologia"));
                dadosPlanoAlteracaoCategoria.add(i, jsonObject.getString("categoria"));
                dadosPlanoAlteracaoInstalacao.add(i, jsonObject.getString("instalacao"));
                dadosPlanoAlteracaoCodIspTelecom.add(i, jsonObject.getString("cod_isp_telecom"));
            }

            adesao.setDadosPlanoAlteracaoId(dadosPlanoAlteracaoId);
            adesao.setDadosPlanoAlteracaoNome(dadosPlanoAlteracaoNome);
            adesao.setDadosPlanoAlteracaoValor(dadosPlanoAlteracaoValor);
            adesao.setDadosPlanoAlteracaoDownload(dadosPlanoAlteracaoDownload);
            adesao.setDadosPlanoAlteracaoUpload(dadosPlanoAlteracaoUpload);
            adesao.setDadosPlanoAlteracaoTecnologia(dadosPlanoAlteracaoTecnologia);
            adesao.setDadosPlanoAlteracaoCategoria(dadosPlanoAlteracaoCategoria);
            adesao.setDadosPlanoAlteracaoInstalacao(dadosPlanoAlteracaoInstalacao);
            adesao.setDadosPlanoAlteracaoCodIspTelecom(dadosPlanoAlteracaoCodIspTelecom);

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            throw new Exception(e);
        }

        return adesao;
    }

    /**
     * Método para realizar UPGRADE do plano do cliente
     * (Módulo mudança de plano)
     *
     * @author Igor Maximo
     * @date 27/05/2020
     */
    public String setUpgradePlanoCliente(String codSerCliPlanoAtual, String codSerPlanoNovoEscolhido, String nomePlanoAtual, String valorPlanoEscolhido, int vencimentoDia, String codVencimento, String codCli, String velocidadeNovaMb, String categoria) {
        String retorno = "";
        try {
            JSONObject jsonObject = new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.EXECUTE_UPGRADE_PLANO_CLIENTE, new Uri.Builder()
                    .appendQueryParameter("codcli", codCli)
                    .appendQueryParameter("codser", codSerPlanoNovoEscolhido)
                    .appendQueryParameter("codVencimento", codVencimento)
                    .appendQueryParameter("valorPlanoEscolhido", valorPlanoEscolhido)
                    .appendQueryParameter("velocidade_mb_nova", velocidadeNovaMb)
                    .appendQueryParameter("categoria", categoria)
                    .appendQueryParameter("codsercli", codSerCliPlanoAtual)
                    .appendQueryParameter("vencimentoDia", String.valueOf(vencimentoDia))
                    .appendQueryParameter("nomePlanoAtual", nomePlanoAtual)
                    .appendQueryParameter("dispositivo", "Android"))).getJSONObject(0);
            retorno = jsonObject.getString("msg");
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
        return retorno;
    }


    /**
     * Função que retorna se o app está habilitado para realizar pagamentos por cartão de débito.
     *
     * @author Igor Maximo
     * @date 07/04/2020
     */
    public boolean retornaSeMudancaPlanoHabilitada() {
        try {
            if (new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_SE_APP_HABILITADO_MUDANCA_PLANO, null)).getJSONObject(0).getString("se_mudanca").equals("true")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return false;
    }


    /**
     * Função que retorna se o CODSER do plano (CODSERCLI) atual pertence
     * à lista de cod_isp_telecom, se pertence, significa que o plano é
     * realmente de fibra e está habilitado a sofrer mudanças de plano
     * pelo módulo
     *
     * @author Igor Maximo
     * @date 05/05/2020
     */
    public boolean retornaSeCodSerdoPlanoCodSerCliAtualEFibra(String codSerCli, String codCli) {
        try {
            if (new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_SE_CODSER_DO_PLANO_CODSERCLI_EM_QUESTAO_E_FIBRA, new Uri.Builder()
                    .appendQueryParameter("codcli", codCli)
                    .appendQueryParameter("codsercli", codSerCli))).getJSONObject(0).getString("se_plano_fibra").equals("true")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return false;
    }

    /**
     * Função que retorna se o (CODSERCLI) atual é empresarial
     *
     * @author Igor Maximo
     * @date 05/05/2020
     */
    public boolean retornaSeTipoCategoriaPlanoAtual(String codSerCli) {
        try {
            JSONObject jsonObject = new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_SE_CATEGORIA_PLANO_ATUAL_EMPRESARIAL, new Uri.Builder().appendQueryParameter("codsercli", codSerCli))).getJSONObject(0);
            if (jsonObject.getString("se_empresarial").equals("true")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return true;
    }


    /**
     * Função que retorna se o (CODSERCLI) atual possui descontos
     *
     * @author Igor Maximo
     * @date 28/05/2020
     */
    public boolean retornaSePlanoPossuiDescontosOuAcrescimos(String codSerCli, String codCli) {
        try {
            if (new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_SE_PLANO_POSSUI_DESCONTOS, new Uri.Builder()
                    .appendQueryParameter("codCli", codCli)
                    .appendQueryParameter("codsercli", codSerCli))).getJSONObject(0).getString("possui_desconto").equals("true")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return true;
    }
}
