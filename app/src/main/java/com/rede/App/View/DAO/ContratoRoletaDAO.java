package com.rede.App.View.DAO;

import android.net.Uri;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.ContratoRoleta;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Carrega os dados planos do usuário do Integrator para exibição tela principal
 *
 * @author Igor Maximo
 * @criado 04/03/2019
 * @editado 06/03/2019
 */
public final class ContratoRoletaDAO {

    ContratoRoleta planoRoleta = new ContratoRoleta("", "", "", "", "", "", "", null, null, null, null, null,null,null, null, null, null, null, null);
    final Usuario usuario = new Usuario();

    public ContratoRoleta getTodosContratosCliente() throws Exception {
        ArrayList<String> dadosCodigoCliente = new ArrayList<>();
        ArrayList<String> dadosCodsercli = new ArrayList<>();
        ArrayList<String> dadosNomeDoPlano = new ArrayList<>();
        ArrayList<String> dadosStatusPlano = new ArrayList<>();
        ArrayList<String> dadosValorFinal = new ArrayList<>();
        ArrayList<String> dadosEnderecosInstalacao = new ArrayList<>();
        ArrayList<String> dadosVencimentoPlano = new ArrayList<>();
        ArrayList<String> dadosSaldoPlano = new ArrayList<>();
        ArrayList<String> dadosCodPolicy = new ArrayList<>();
        ArrayList<String> dadosCodProd = new ArrayList<>();
        ArrayList<String> dadosCodContratoItem = new ArrayList<>();
        ArrayList<Boolean> dadosSeRegraAntivirus = new ArrayList<>();
        ArrayList<String> dadosEnderecoInstalacaoPuro = new ArrayList<>();
        ArrayList<String> dadosNumeroInstalacaoPuro = new ArrayList<>();
        ArrayList<String> dadosCodClieCartaoVindi = new ArrayList<>();
        // Dados do noggin e paramout
        ArrayList<String> dadosCodAppExterno = new ArrayList<>();
        ArrayList<String> dadosUsuarioApp = new ArrayList<>();
        ArrayList<String> dadosSenhaApp = new ArrayList<>();
        // Dados da empresa fornecedora do serviço
        ArrayList<String> dadosContratoCNPJ = new ArrayList<>();
        ArrayList<String> dadosContratoNome = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(new RecebeJson().retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_PLANOS_CLIENTE_ROLETA,
                    new Uri.Builder().appendQueryParameter("cpf_cnpj", usuario.getCpfCnpj())));
            JSONObject jsonObject = null;

            System.out.println(Rotas.ROTA_PADRAO + Rotas.SELECT_PLANOS_CLIENTE_ROLETA);

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                dadosCodigoCliente.add(i, usuario.getCpfCnpj());
                dadosCodsercli.add(i, jsonObject.getString("COD_CNTR"));
                dadosNomeDoPlano.add(i, Ferramentas.setCapitalizarTexto(jsonObject.getString("PRODUTO_NOME").substring(jsonObject.getString("PRODUTO_NOME").indexOf(" "))));
                dadosStatusPlano.add(i, Ferramentas.setCapitalizarTexto(jsonObject.getString("DESCR_STAT_CNTR")));
                dadosValorFinal.add(i, jsonObject.getString("VLR_CNTR"));
                dadosEnderecosInstalacao.add(i, Ferramentas.setCapitalizarTexto((jsonObject.getString("ENDER_INSTALACAO_LOGR_TIPO") + " " + jsonObject.getString("ENDER_INSTALACAO_LOGR_NOM"))) + ", " + jsonObject.getString("ENDER_INSTALACAO_NUM"));
                dadosVencimentoPlano.add(i, jsonObject.getString("DIA_VENC"));
                dadosCodPolicy.add(i, jsonObject.getString("COD_POLICY"));
                dadosCodProd.add(i, jsonObject.getString("COD_PROD"));
                dadosSeRegraAntivirus.add(i, jsonObject.getBoolean("SE_ENQUADRA_REGRA_ANTIVIRUS"));
                dadosCodContratoItem.add(i, jsonObject.getString("COD_CNTR_ITEM"));
                dadosEnderecoInstalacaoPuro.add(i, jsonObject.getString("ENDER_INSTALACAO_LOGR_TIPO") + " " + jsonObject.getString("ENDER_INSTALACAO_LOGR_NOM"));
                dadosNumeroInstalacaoPuro.add(i, jsonObject.getString("ENDER_INSTALACAO_NUM"));
                dadosCodClieCartaoVindi.add(i, jsonObject.getString("COD_CLIE_CARTAO"));
                // Dados do noggin e paramout
                dadosCodAppExterno.add(i, jsonObject.getString("COD_APP_EXTERNO"));
                dadosUsuarioApp.add(i, jsonObject.getString("USUR_APP"));
                dadosSenhaApp.add(i, jsonObject.getString("SEN_APP"));
                // Dados da coleta da empresa fornecedora do serviço
                dadosContratoCNPJ.add(i, jsonObject.getString("EMPRESA_CNPJ"));
                dadosContratoNome.add(i, jsonObject.getString("EMPRESA_RAZAO_SOCIAL"));
            }

            planoRoleta.setDadosCodigoCliente(dadosCodigoCliente);
            planoRoleta.setDadosCodContrato(dadosCodsercli);
            planoRoleta.setDadosNomeDoPlano(dadosNomeDoPlano);
            planoRoleta.setDadosStatusPlano(dadosStatusPlano);
            planoRoleta.setDadosValorFinal(dadosValorFinal);
            planoRoleta.setDadosEnderecoInstalacao(dadosEnderecosInstalacao);
            planoRoleta.setDadosVencimentoPlano(dadosVencimentoPlano);
            planoRoleta.setDadosSaldoDoPlano(dadosSaldoPlano);
            planoRoleta.setDadosCodPolicy(dadosCodPolicy);
            planoRoleta.setDadosCodProd(dadosCodProd);
            planoRoleta.setDadosSeRegraAntivirus(dadosSeRegraAntivirus);
            planoRoleta.setDadosCodContratoItem(dadosCodContratoItem);
            planoRoleta.setDadosEnderecoInstalacaoPuro(dadosEnderecoInstalacaoPuro);
            planoRoleta.setDadosNumeroInstalacaoPuro(dadosNumeroInstalacaoPuro);
            planoRoleta.setDadosCodClieCartaoVindi(dadosCodClieCartaoVindi);
            // Dados do noggin e paramout
            planoRoleta.setDadosCodAppExterno(dadosCodAppExterno);
            planoRoleta.setDadosUsuarioApp(dadosUsuarioApp);
            planoRoleta.setDadosSenhaApp(dadosSenhaApp);
            // Coleta dados da empresa fornecedora
            planoRoleta.setDadosContratoEmpresaCNPJ(dadosContratoCNPJ);
            planoRoleta.setDadosContratoEmpresaNome(dadosContratoNome);

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            throw new Exception(e);
        }
        return planoRoleta;
    }
}

