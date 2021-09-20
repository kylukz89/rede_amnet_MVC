package com.rede.App.View.DAO;

import android.net.Uri;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.Titulo;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.Splash;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Carrega as faturas do usuário do Integrator para exibição tela segunda via
 *
 * @author Igor Maximo
 * @date        15/01/2021
 */
public class TituloDAO {
    Titulo segvia = new Titulo(0, null,null,null,null,null,null,0, null,null,null,null,null);
    final Usuario usuario = new Usuario();

    /**
     *  Carrega as faturas entre dois períodos
     *
     * @author      Igor Maximo
     * @date        15/01/2021
     */
    public final Titulo getCarregarTitulosCliente(String from, String to, String tipoConsulta, String codSerCli) throws Exception {

//        System.out.println("FROM=============> " + from);
//        System.out.println("TO===============> " + to);

        ArrayList<String> dadosVencimento = new ArrayList<>();
        ArrayList<String> dadosValorPagar = new ArrayList<>();
        ArrayList<String> dadosNumBoleto = new ArrayList<>();
        ArrayList<String> dadosNossoNumeroDoc = new ArrayList<>();
        ArrayList<String> dadosCodFat = new ArrayList<>();
        ArrayList<String> dadosCodContrato = new ArrayList<>();
        ArrayList<String> dadosDias = new ArrayList<>();
        ArrayList<String> dadosValorCorrigidoManualmente = new ArrayList<>();
        ArrayList<String> dadosTipoFatura = new ArrayList<>();
        ArrayList<String> dadosEmpresa = new ArrayList<>();
        ArrayList<String> dadosFormaCobranca = new ArrayList<>();
        ArrayList<Integer> dadosCorIconePertinente = new ArrayList<>();

        try {

            System.out.println("SELECT_TITULOS_FATURAS_CLIENTE_SEGUNDA_VIA===============> " + Rotas.ROTA_PADRAO + Rotas.SELECT_TITULOS_FATURAS_CLIENTE_SEGUNDA_VIA);
            System.out.println("SELECT_TITULOS_FATURAS_CLIENTE_SEGUNDA_VIA===============> " + usuario.getCodigo());
            System.out.println("SELECT_TITULOS_FATURAS_CLIENTE_SEGUNDA_VIA===============> " + from);
            System.out.println("SELECT_TITULOS_FATURAS_CLIENTE_SEGUNDA_VIA===============> " + to);
            System.out.println("SELECT_TITULOS_FATURAS_CLIENTE_SEGUNDA_VIA===============> " + codSerCli);
            System.out.println("SELECT_TITULOS_FATURAS_CLIENTE_SEGUNDA_VIA===============> " + tipoConsulta);


            JSONArray jsonArray = new JSONArray(new RecebeJson().retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_TITULOS_FATURAS_CLIENTE_SEGUNDA_VIA,
                    new Uri.Builder()
                    .appendQueryParameter("codcli",  (codSerCli.equals("")  || codSerCli.isEmpty() ? usuario.getCodigo() : ""))
                    .appendQueryParameter("from", from)
                    .appendQueryParameter("to", to)
                    .appendQueryParameter("codsercli", codSerCli)
                    .appendQueryParameter("tipo_consulta", tipoConsulta)));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                dadosTipoFatura.add(i, "Fatura");
                dadosDias.add(i, jsonObject.getString("DIAS"));
                dadosVencimento.add(i, jsonObject.getString("DAT_VENC") + " (" + String.valueOf(dadosDias.get(i)) + ")");
                dadosValorPagar.add(i, jsonObject.getString("VLR_TOTAL").replace(".", ","));
                dadosNumBoleto.add(i, jsonObject.getString("NUM_PARCL"));
                dadosNossoNumeroDoc.add(i, jsonObject.getString("COD_ARQ_DOC")); // Necessário para download do PDF
                dadosCodFat.add(i, jsonObject.getString("COD_CNTR_TITL")); // Necessário para download do PDF
                dadosCodContrato.add(i, jsonObject.getString("COD_CNTR")); // Necessário para baixa
                dadosValorCorrigidoManualmente.add(i, jsonObject.getString("VLR_TOTAL_FINAL"));
                dadosEmpresa.add(i, jsonObject.getString("CNPJ_EMPR"));
                dadosFormaCobranca.add(i, jsonObject.getString("FORM_PAGAMENTO"));
                dadosCorIconePertinente.add(i, Ferramentas.getIconeCorFatura((String) dadosVencimento.get(i))); // Verifica se fatura está vencida, se estiver ícone vermelho, senão ícone laranja
            }
            segvia.setDadosDias(dadosDias);
            segvia.setDadosVencimento(dadosVencimento);
            segvia.setDadosValorPagar(dadosValorPagar);
            segvia.setDadosNumBoleto(dadosNumBoleto);
            segvia.setDadosCodContrato(dadosCodContrato);
            segvia.setDadosContratoTitulo(dadosCodFat);
            segvia.setDadosCodigoArquivoDocumento(dadosNossoNumeroDoc);
            segvia.setDadosCorIconePertinente(dadosCorIconePertinente);
            segvia.setDadosValorCorrigidoManualmente(dadosValorCorrigidoManualmente);
            segvia.setDadosTipoFatura(dadosTipoFatura);
            segvia.setDadosEmpresa(dadosEmpresa);
            segvia.setDadosFormaCobranca(dadosFormaCobranca);

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            throw new Exception("carregaDetalhesBasicosFaturasUsuario() " + e);
        }
        return segvia;
    }

    /**
     *  Carrega as faturas entre dois períodos
     *
     * @author      Igor Maximo
     * @date        15/01/2021
     */
    public final Titulo setCarregarTitulosClienteHistoricoPago(String from, String to, String tipoConsulta, String codSerCli) throws Exception {

//        System.out.println("FROM=============> " + from);
//        System.out.println("TO===============> " + to);

        ArrayList<String> dadosVencimento = new ArrayList<>();
        ArrayList<String> dadosValorPagar = new ArrayList<>();
        ArrayList<String> dadosNumBoleto = new ArrayList<>();
        ArrayList<String> dadosNossoNumeroDoc = new ArrayList<>();
        ArrayList<String> dadosCodFat = new ArrayList<>();
        ArrayList<String> dadosCodContrato = new ArrayList<>();
        ArrayList<String> dadosDias = new ArrayList<>();
        ArrayList<String> dadosValorCorrigidoManualmente = new ArrayList<>();
        ArrayList<String> dadosTipoFatura = new ArrayList<>();
        ArrayList<String> dadosEmpresa = new ArrayList<>();
        ArrayList<String> dadosFormaCobranca = new ArrayList<>();
        ArrayList<Integer> dadosCorIconePertinente = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(new RecebeJson().retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_TITULOS_FATURAS_CLIENTE_SEGUNDA_VIA, new Uri.Builder()
                    .appendQueryParameter("codcli", usuario.getCodigo())
                    .appendQueryParameter("from", from)
                    .appendQueryParameter("to", to)
                    .appendQueryParameter("codsercli", codSerCli)
                    .appendQueryParameter("tipo_consulta", tipoConsulta)));

            //for (int i = 0; i < jsonArray.length(); i++) {
            for (int i = 0; i < 10; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                dadosTipoFatura.add(i, "Fatura");
                dadosDias.add(i, jsonObject.getString("DIAS"));
                dadosVencimento.add(i, jsonObject.getString("DAT_VENC") + " (" + String.valueOf(dadosDias.get(i)) + ")");
                dadosValorPagar.add(i, jsonObject.getString("VLR_TOTAL").replace(".", ","));
                dadosNumBoleto.add(i, jsonObject.getString("NUM_PARCL"));
                dadosNossoNumeroDoc.add(i, jsonObject.getString("COD_ARQ_DOC")); // Necessário para download do PDF
                dadosCodFat.add(i, jsonObject.getString("COD_CNTR_TITL")); // Necessário para download do PDF
                dadosCodContrato.add(i, jsonObject.getString("COD_CNTR")); // Necessário para baixa
                dadosValorCorrigidoManualmente.add(i, jsonObject.getString("VLR_TOTAL_FINAL"));
                dadosEmpresa.add(i, jsonObject.getString("CNPJ_EMPR"));
                dadosFormaCobranca.add(i, jsonObject.getString("FORM_PAGAMENTO"));
                dadosCorIconePertinente.add(i, Ferramentas.getIconeCorFatura((String) dadosVencimento.get(i))); // Verifica se fatura está vencida, se estiver ícone vermelho, senão ícone laranja
            }
            segvia.setDadosDias(dadosDias);
            segvia.setDadosVencimento(dadosVencimento);
            segvia.setDadosValorPagar(dadosValorPagar);
            segvia.setDadosNumBoleto(dadosNumBoleto);
            segvia.setDadosCodContrato(dadosCodContrato);
            segvia.setDadosContratoTitulo(dadosCodFat);
            segvia.setDadosCodigoArquivoDocumento(dadosNossoNumeroDoc);
            segvia.setDadosCorIconePertinente(dadosCorIconePertinente);
            segvia.setDadosValorCorrigidoManualmente(dadosValorCorrigidoManualmente);
            segvia.setDadosTipoFatura(dadosTipoFatura);
            segvia.setDadosEmpresa(dadosEmpresa);
            segvia.setDadosFormaCobranca(dadosFormaCobranca);

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            throw new Exception("carregaDetalhesBasicosFaturasUsuario() " + e);
        }
        return segvia;
    }


    /**
     * Carrega o cód de barras a partir do codfat
     */
    public String retornaCodBarrasCodFat(String codContratoTiulo, String codArquivoDocumento) throws Exception {
        try {
            return new JSONArray(new RecebeJson().retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_BOTAO_CODIGO_BARRAS,
                    new Uri.Builder()
                            .appendQueryParameter("cpfCnpj", usuario.getCpfCnpj())
                            .appendQueryParameter("codContratoTiulo", codContratoTiulo)
                            .appendQueryParameter("codArquivoDocumento", codArquivoDocumento)
            )).getJSONObject(0).getString("cod_barra");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
