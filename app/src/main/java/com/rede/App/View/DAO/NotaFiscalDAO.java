package com.rede.App.View.DAO;


import android.net.Uri;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.NotaFiscal;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *  Classe que irá realizar as consultas e downloads das notas fiscais do cliente
 */
public class NotaFiscalDAO {

    NotaFiscal notaFiscal = new NotaFiscal();
    RecebeJson recebeJson = new RecebeJson();
    Usuario usuario = new Usuario();

    public NotaFiscal carregaNotasFiscaisUsuario(String from, String to, String tipoConsulta, String codSerCli) throws Exception {
        ArrayList dadosCodCli = new ArrayList();
        ArrayList dadosCodNF = new ArrayList();
        ArrayList dadosCodTNF = new ArrayList();
        ArrayList dadosNumeroNF = new ArrayList();
        ArrayList dadosModelo = new ArrayList();
        ArrayList dadosEmpresa = new ArrayList();
        ArrayList dadosDataLancamento = new ArrayList();
        ArrayList dadosValorNF = new ArrayList();
        ArrayList dadosIdentificacao = new ArrayList();
        ArrayList dadosLinkNF = new ArrayList();

        //System.err.println("============> codcli "+usuario.getCodigo());

        try {
            Uri.Builder paramsBuilder = new Uri.Builder()
                    .appendQueryParameter("codcli", usuario.getCodigo())
                    .appendQueryParameter("de", from)
                    .appendQueryParameter("ate", to)
                    .appendQueryParameter("tipo_consulta", tipoConsulta)
                    .appendQueryParameter("codsercli", codSerCli); // +1 appendQuery caso queira mais parâmetros

            String jsonInput = recebeJson.retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_NOTAS_FISCAIS, paramsBuilder);
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(jsonInput);
            int length = jsonArray.length();
            JSONObject jsonObject;

            for (int i = 0; i < length; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                dadosCodCli.add(i, jsonObject.getString("codigo_cliente"));
                dadosCodNF.add(i, jsonObject.getString("codnf"));
                dadosCodTNF.add(i, jsonObject.getString("codtnf"));
                dadosNumeroNF.add(i, jsonObject.getString("numero_nf"));
                dadosModelo.add(i, jsonObject.getString("modelo"));
                dadosEmpresa.add(i, jsonObject.getString("empresa"));
                dadosDataLancamento.add(i, jsonObject.getString("data_lan"));
                dadosValorNF.add(i, jsonObject.getString("valor_nf"));
                dadosIdentificacao.add(i, jsonObject.getString("identificacao"));
                dadosLinkNF.add(i, jsonObject.getString("link_pdf"));

                //System.err.println("========================================================================================> "+jsonObject.getString("link_pdf"));
            }

            notaFiscal.setDadosCodCli(dadosCodCli);
            notaFiscal.setDadosCodNF(dadosCodNF);
            notaFiscal.setDadosCodTNF(dadosCodTNF);
            notaFiscal.setDadosNumeroNF(dadosNumeroNF);
            notaFiscal.setDadosModelo(dadosModelo);
            notaFiscal.setDadosEmpresa(dadosEmpresa);
            notaFiscal.setDadosDataLancamento(dadosDataLancamento);
            notaFiscal.setDadosValorNF(dadosValorNF);
            notaFiscal.setDadosIdentificacao(dadosIdentificacao);
            notaFiscal.setDadosLinkPDFNF(dadosLinkNF);

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            throw new Exception("carregaNotasFiscaisUsuario() " + e);
        }
        return notaFiscal;
    }

}
