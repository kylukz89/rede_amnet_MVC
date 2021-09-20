package com.rede.App.View.DAO;

import android.net.Uri;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Classe responsável por retornar o link que gera boleto de faturas
 * de mensalidade para ser pago por bancos, débito ou lotéricas
 *
 * @author      Igor Maximo
 * @date        23/01/2021
 */
public class LinkBoletoDAO {

    final Usuario usuario = new Usuario();

    /**
     * Método responsável por gerar o boleto dentro do servidor,
     * receber o link do boleto para posterior download em PDF
     *
     * @author      Igor Maximo
     * @date        23/01/2021
     */
    public String getLinkBoletoEmitido(String codContratoTiulo, String codArquivoDocumento) {
        try {
            return new JSONArray(new RecebeJson().retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_LINK_BOLETO_FATURA_MENSALIDADE,
                    new Uri.Builder()
                            .appendQueryParameter("cpfCnpj", usuario.getCpfCnpj())
                            .appendQueryParameter("codContratoTiulo", codContratoTiulo)
                            .appendQueryParameter("codArquivoDocumento", codArquivoDocumento)

            )).getJSONObject(0).getString("link_boleto");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Método responsável por consultar se houve boleto gerado
     * anteriormente para uma fatura em específico
     *
     * @author      Igor Maximo
     * @date        23/01/2021
     */
    public boolean getSeBoletoFoiGeradoOuSolicitadoAnteriormente(String codfat) {
        try {
            if (new JSONArray(new RecebeJson().retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_SE_FOI_GERADO_BOLETO_ANTERIORMENTE, new Uri.Builder().appendQueryParameter("codfat", codfat))).getJSONObject(0).getString("se_ja").equals("S")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return false;
    }
}
