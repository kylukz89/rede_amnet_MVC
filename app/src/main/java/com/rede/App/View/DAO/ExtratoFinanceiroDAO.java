package com.rede.App.View.DAO;

import android.net.Uri;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.ExtratoFinanceiro;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.ToolBox.ManipulaData;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Carrega o extrato finnaceiro do Integrator para exibição tela extrato financeiro
 *
 * @author Igor Maximo
 * @criado 06/03/2019
 * @editado 06/03/2019
 */
public class ExtratoFinanceiroDAO {

    RecebeJson recebeJson = new RecebeJson();
    ExtratoFinanceiro extfinanc = new ExtratoFinanceiro();
    final Usuario usuario = new Usuario();

    public ExtratoFinanceiro carregaExtratoFinanceiroUsuario(String from, String to, String tipoConsulta, String codSerCli) throws Exception {
        ArrayList dadosDataProc = new ArrayList();
        ArrayList dadosHistoConta = new ArrayList();
        ArrayList dadosPeriodo = new ArrayList();
        ArrayList dadosCodCRec = new ArrayList();
        ArrayList dadosFormaCob = new ArrayList();
        ArrayList dadosStatusConta = new ArrayList();
        ArrayList dadosCodCob = new ArrayList();
        ArrayList dadosDataBaixa = new ArrayList();
        ArrayList dadosDataVenc = new ArrayList();
        ArrayList dadosDescServ = new ArrayList();
        ArrayList dadosValorLanc = new ArrayList();
        ArrayList dadosValorPago = new ArrayList();
        ArrayList dadosValor = new ArrayList();
        ArrayList dadosStatus = new ArrayList();
        ArrayList dadosGeraNossoNro = new ArrayList();
        ArrayList dadosFCodCob = new ArrayList();
        ArrayList dadosCodSerCli = new ArrayList();
        ArrayList dadosCorIconePertinente = new ArrayList();

        ManipulaData md = new ManipulaData();

        try {
            Uri.Builder paramsBuilder = new Uri.Builder()
                    .appendQueryParameter("codcli", usuario.getCodigo())
                    .appendQueryParameter("from", from)
                    .appendQueryParameter("to", to)
                    .appendQueryParameter("tipo_consulta", tipoConsulta)
                    .appendQueryParameter("codsercli", codSerCli); // +1 appendQuery caso queira mais parâmetros

            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_EXTRATO_FINANCEIRO, paramsBuilder);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jsonInput);
            int length = jsonArray.length();
            JSONObject jsonObject;

            for (int i = 0; i < length; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                dadosDataProc.add(i, jsonObject.getString("Data_Processamento"));
                dadosHistoConta.add(i, jsonObject.getString("Historico_da_Conta"));
                dadosPeriodo.add(i, jsonObject.getString("periodo"));
                dadosCodCRec.add(i, jsonObject.getString("codcrec"));
                dadosFormaCob.add(i, jsonObject.getString("forma_cob"));
                dadosStatusConta.add(i, jsonObject.getString("Status_da_conta"));
                dadosCodCob.add(i, jsonObject.getString("codcob"));
                dadosDataBaixa.add(i, jsonObject.getString("data_bai"));
                dadosDataVenc.add(i, md.converteDataFormatoBR(jsonObject.getString("data_ven")) + " (" + md.getDiferencaDiasEntreUmaDataAteHoje(md.converteDataFormatoBR(jsonObject.getString("data_ven"))) + ")");
                dadosDescServ.add(i, jsonObject.getString("descri_ser"));
                dadosValorLanc.add(i, jsonObject.getString("valor_lan"));
                dadosValorPago.add(i, jsonObject.getString("valor_pag"));
                dadosValor.add(i, jsonObject.getString("valor"));
                dadosStatus.add(i, jsonObject.getString("status"));
                dadosGeraNossoNro.add(i, jsonObject.getString("gera_nosso_nro"));
                dadosFCodCob.add(i, jsonObject.getString("f_codcob"));
                dadosCodSerCli.add(i, jsonObject.getString("codsercli"));
                dadosCorIconePertinente.add(i, Ferramentas.getIconeCorExtrato((String) dadosStatusConta.get(i))); // Verifica se extrato está vencida, se estiver ícone vermelho, verde caso nem esteja no mês
            }

            extfinanc.setDadosIconePertinente(dadosCorIconePertinente);
            extfinanc.setDadosDataProcessamento(dadosDataProc);
            extfinanc.setDadosHistoricoConta(dadosHistoConta);
            extfinanc.setDadosPeriodo(dadosPeriodo);
            extfinanc.setDadosCodCRec(dadosCodCRec);
            extfinanc.setDadosFormaCob(dadosFormaCob);
            extfinanc.setDadosStatusConta(dadosStatusConta);
            extfinanc.setDadosCodCob(dadosCodCob);
            extfinanc.setDadosDataBaixa(dadosDataBaixa);
            extfinanc.setDadosDataVenc(dadosDataVenc);
            extfinanc.setDadosDescServ(dadosDescServ);
            extfinanc.setDadosValorLancamento(dadosValorLanc);
            extfinanc.setDadosValorPago(dadosValorPago);
            extfinanc.setDadosValor(dadosValor);
            extfinanc.setDadosStatus(dadosStatus);
            extfinanc.setDadosGeraNossoNro(dadosGeraNossoNro);
            extfinanc.setDadosFCodCob(dadosFCodCob);
            extfinanc.setDadosCodSerCli(dadosCodSerCli);


        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            throw new Exception("carregaExtratoFinanceiroUsuario() " + e);
        }
        return extfinanc;
    }
}

