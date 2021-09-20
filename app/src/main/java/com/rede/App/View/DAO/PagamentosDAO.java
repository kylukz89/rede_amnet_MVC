package com.rede.App.View.DAO;

import android.net.Uri;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.HistoricoPagamento;
import com.rede.App.View.JavaBeans.PagamentoCartao;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.MenuSegundaVia;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Para pagamento por cartões
 *
 * @author Igor Maximo
 * @date 10/03/2019
 * @edited 15/03/2019
 */
public class PagamentosDAO {

    private RecebeJson recebeJson = new RecebeJson();
    private Usuario usuario = new Usuario();

    ////////////////////// Mensalidades/Faturas //////////////////////////

    /**
     * Realiza o pagamento de uma fatura de mensalidade por crédito
     *
     * @author Igor Maximo
     * @date 26/01/2021
     */
    public String[] setPagarFaturaMensalidadeCartaoCreditoOuBoleto(PagamentoCartao pgcartao) {

        System.out.println("====================> " + pgcartao.getCodigoCliente());
        System.out.println("====================> " + pgcartao.getCodigoContrato());
        System.out.println("====================> " + pgcartao.getCodigoFatura());
        System.out.println("====================> " + pgcartao.getValor());

        String[] dados = new String[3];
        Uri.Builder paramsBuilder = new Uri.Builder()
                .appendQueryParameter("codclie", pgcartao.getCodigoCliente())
                .appendQueryParameter("COD_CNTR", pgcartao.getCodigoContrato())
                .appendQueryParameter("COD_CNTR_TITL", pgcartao.getCodigoFatura())
                .appendQueryParameter("COD_ARQ_DOC", pgcartao.getCodigoArquivoDocumento())
                .appendQueryParameter("venc_fatura", pgcartao.getVencFatura())
                .appendQueryParameter("valor", pgcartao.getValor())
                .appendQueryParameter("tipo_pagamento", String.valueOf(pgcartao.getTipoPagamentoCreditoDebito()))
                .appendQueryParameter("dispositivo", pgcartao.getObs())
                .appendQueryParameter("numerocard", pgcartao.getNumeroCartao())
                .appendQueryParameter("validade", pgcartao.getValidade())
                .appendQueryParameter("cvv", pgcartao.getCVV())
                .appendQueryParameter("titular", pgcartao.getNomeTitular())
                .appendQueryParameter("bandeira", pgcartao.getBandeira())
                .appendQueryParameter("CNPJ_EMPR", pgcartao.getEmpresa())
                .appendQueryParameter("formaCobranca", pgcartao.getFormaCobranca()); // +1 appendQuery caso queira mais parâmetros

        switch (pgcartao.getTipoPagamentoCreditoDebito()) {
            case 1:
                try {
                    recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.PAY_MENSALIDADE_SEGVIA, paramsBuilder);
                } catch (Exception e) {
                    AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), "(linha " + e.getStackTrace()[0].getLineNumber() + ") pagaFaturaMensalidadeCartaoCreditoOuBoleto " + e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                    System.err.println(e);
                }
                break;
            default:
                try {
                    JSONObject jsonObject = new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.PAY_MENSALIDADE_SEGVIA, paramsBuilder)).getJSONObject(0);

                    MenuSegundaVia.seAutorizada = jsonObject.getBoolean("transacao_status");
                    MenuSegundaVia.msgTransacao = jsonObject.getString("msg");

                    dados[0] = jsonObject.getString("transacao_status");
                    dados[1] = jsonObject.getString("msg");
                    // Se retornou url de redirecionamento de internet banking
                    dados[2] = jsonObject.getString("url_banking");
                } catch (Exception e) {
                    AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), "(linha " + e.getStackTrace()[0].getLineNumber() + ") pagaFaturaMensalidadeCartaoCreditoOuBoleto " + e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                }
                break;
        }

        return dados;
    }

    /**
     * Realiza consulta dos pagamentos realizados por crédito/débito
     *
     * @author Igor Maximo
     * @date 26/01/2021
     */
    public HistoricoPagamento getHistoricoPagamento(String codClie) {
        HistoricoPagamento historicoPagamento = new HistoricoPagamento(
                null,
                "",
                "",
                "",
                "",
                false,
                "",
                "",
                "",
                "",
                "",
                "",
                0,
                "",
                "",
                "",
                "",
                0,
                "");
        ArrayList<String> dadosDataCadastro = new ArrayList<>();
        ArrayList<String> dadosNome = new ArrayList<>();
        ArrayList<String> dadosFkCliente = new ArrayList<>();
        ArrayList<String> dadosPedido = new ArrayList<>();
        ArrayList<Boolean> dadosSeBaixadaSimetra = new ArrayList<>();
        ArrayList<String> dadosCodContrato = new ArrayList<>();
        ArrayList<String> dadosCodContratoTitulo = new ArrayList<>();
        ArrayList<String> dadosCodArquivoDoc = new ArrayList<>();
        ArrayList<String> dadosVencimentoFatura = new ArrayList<>();
        ArrayList<String> dadosFkEmpresa = new ArrayList<>();
        ArrayList<String> dadosFkFormaPagamento = new ArrayList<>();
        ArrayList<Integer> dadosValorCentavos = new ArrayList<>();
        ArrayList<String> dadosCieloMsg = new ArrayList<>();
        ArrayList<String> dadosPaymentId = new ArrayList<>();
        ArrayList<String> dadosCodigoRetorno = new ArrayList<>();
        ArrayList<String> dadosCodigoAutorizacao = new ArrayList<>();
        ArrayList<Integer> dadosStatusCode = new ArrayList<>();
        ArrayList<String> dadosCompTitulo = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(new RecebeJson().retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_HISTORICO_PAGAMENTOS,
                    new Uri.Builder().appendQueryParameter("codClie", codClie)));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                dadosDataCadastro.add(i, jsonObject.getString("data_cadastro"));
                dadosNome.add(i, jsonObject.getString("nome"));
                dadosFkCliente.add(i, jsonObject.getString("fk_cliente"));
                dadosPedido.add(i, jsonObject.getString("pedido"));

                if (jsonObject.getString("se_baixada_simetra").equals("1")) {
                    dadosSeBaixadaSimetra.add(i, true);
                } else {
                    dadosSeBaixadaSimetra.add(i, false);
                }
                dadosCodContrato.add(i, jsonObject.getString("cod_contrato"));
                dadosCodContratoTitulo.add(i, jsonObject.getString("cod_contrato_titulo"));
                dadosCodArquivoDoc.add(i, jsonObject.getString("cod_arquivo_doc"));
                dadosVencimentoFatura.add(i, jsonObject.getString("vencimento_fatura"));
                dadosFkEmpresa.add(i, jsonObject.getString("fk_empresa"));
                dadosFkFormaPagamento.add(i, jsonObject.getString("pag_form"));
                dadosValorCentavos.add(i, jsonObject.getInt("valor_centavo"));
                dadosCieloMsg.add(i, jsonObject.getString("int_cielo_msg"));
                dadosPaymentId.add(i, jsonObject.getString("int_cielo_payment_id"));
                dadosCodigoRetorno.add(i, jsonObject.getString("int_cielo_codigo_retorno"));
                dadosCodigoAutorizacao.add(i, jsonObject.getString("int_cielo_codigo_autorizacao"));
                dadosStatusCode.add(i, (jsonObject.getString("int_cielo_status_code") == null || jsonObject.getString("int_cielo_status_code").equals("") ? 0 : jsonObject.getInt("int_cielo_status_code")));
                dadosCompTitulo.add(i, jsonObject.getString("comp_tit"));
            }

            historicoPagamento.setDadosDataCadastro(dadosDataCadastro);
            historicoPagamento.setDadosNome(dadosNome);
            historicoPagamento.setDadosFkCliente(dadosFkCliente);
            historicoPagamento.setDadosPedido(dadosPedido);;
            historicoPagamento.setDadosSeBaixadaSimetra(dadosSeBaixadaSimetra);
            historicoPagamento.setDadosCodContrato(dadosCodContrato);
            historicoPagamento.setDadosCodContratoTitulo(dadosCodContratoTitulo);
            historicoPagamento.setDadosCodArquivoDoc(dadosCodArquivoDoc);
            historicoPagamento.setDadosVencimentoFatura(dadosVencimentoFatura);
            historicoPagamento.setDadosFkEmpresa(dadosFkEmpresa);
            historicoPagamento.setDadosFkFormaPagamento(dadosFkFormaPagamento);
            historicoPagamento.setDadosValorCentavos(dadosValorCentavos);
            historicoPagamento.setDadosCieloMsg(dadosCieloMsg);
            historicoPagamento.setDadosPaymentId(dadosPaymentId);
            historicoPagamento.setDadosCodigoRetorno(dadosCodigoRetorno);
            historicoPagamento.setDadosCodigoAutorizacao(dadosCodigoAutorizacao);
            historicoPagamento.setDadosStatusCode(dadosStatusCode);
            historicoPagamento.setDadosCompTit(dadosCompTitulo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return historicoPagamento;
    }


    /**
     * Função que retorna o status da transação realizada por débito
     *
     * @author Igor Maximo
     * @date 16/04/2020
     */
    public final void getStatusTransacaoPorDebito(String codContratoTitulo) {
        try {
            JSONObject jsonObject = new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_DADOS_TRANSACAO_DEBITO,
                    new Uri.Builder()
                            .appendQueryParameter("codContratoTitulo", codContratoTitulo)))
                    .getJSONObject(0);
            // Coleta
            MenuSegundaVia.seAutorizada = jsonObject.getBoolean("transacao_status");
            MenuSegundaVia.msgTransacao = jsonObject.getString("msg");
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
    }

    /**
     * Função que retorna se o app está habilitado para realizar pagamentos por cartão de crédito.
     *
     * @author Igor Maximo
     * @date 13/05/2019
     */
    public boolean getSeAppHabilitadoPagamentoPorCredito() {
        try {
            if (new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_SE_APP_HABILITADO_PAGAMENTO_POR_CREDITO, null)).getJSONObject(0).getString("pg_cred").equals("true")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
        return false;
    }

    /**
     * Função que retorna se o app está habilitado para realizar pagamentos por cartão de débito.
     *
     * @author Igor Maximo
     * @date 07/04/2020
     */
    public boolean getSeAppHabilitadoPagamentoPorDebito() {
        try {
            if (new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_SE_APP_HABILITADO_PAGAMENTO_POR_DEBITO, null)).getJSONObject(0).getString("pg_debit").equals("true")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
        return false;
    }

    /**
     * Função que retorna se o app está habilitado para realizar emissão de boletos
     *
     * @author Igor Maximo
     * @date 06/02/2021
     */
    public boolean getSeAppHabilitadoEmissaoBoletos() {
        try {
            if (new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_SE_APP_HABILITADO_EMISSAO_BOLETOS, null))
                    .getJSONObject(0).getString("pg_bolet").equals("true")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
        return false;
    }

    /**
     * Função que retorna se o app está habilitado para realizar emissão de códigos de barra
     *
     * @author Igor Maximo
     * @date 06/02/2021
     */
    public boolean getSeAppHabilitadoCodigoBarras() {
        try {
            if (new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_SE_APP_HABILITADO_CODIGO_BARRAS, null))
                    .getJSONObject(0).getString("pg_cbarr").equals("true")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
        return false;
    }


    /**
     * Função que retorna qts dias pode-se gerar boleto vencido, padrão 90 dias
     *
     * @author Igor Maximo
     * @date 16/05/2019
     */
    public int getQtsDiasAposVencimentoPodeGerarBoleto() {
        try {
            return new JSONArray(recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_QTS_DIAS_PODE_GERAR_BOLETO_VENCIDO, null)).getJSONObject(0).getInt("qtds_dias");
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
        return 90; // Padrão novo
    }

///////////////////////////////////////////////////////////////////////


}
