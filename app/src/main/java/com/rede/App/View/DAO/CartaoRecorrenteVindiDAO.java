package com.rede.App.View.DAO;

import android.net.Uri;

import com.rede.App.View.JSON.RecebeJson;
import com.rede.App.View.JavaBeans.CartaoRecorrenteManipulacao;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Routes.Rotas;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.MenuRecorrenteCartao;
import com.rede.App.View.View.Splash;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Realiza operações referente a vindi
 *
 * @author Igor Maximo
 * @date 14/06/2021
 */
public class CartaoRecorrenteVindiDAO {

    private final Usuario usuario = new Usuario();

    /**
     * Realiza o cadastramento de um cartão na vindi
     *
     * @author Igor Maximo
     * @date 14/06/2021
     */
    public Object[] setCartaoClienteVindi(CartaoRecorrenteManipulacao entidadeCartaoRecorrenteManipulacao) {
        Object[] dados = new String[3];
        Uri.Builder paramsBuilder = new Uri.Builder()
                .appendQueryParameter("dispositivo", Ferramentas.getMarcaModeloDispositivo(MenuRecorrenteCartao.CTX))
                .appendQueryParameter("cpfCnpj", entidadeCartaoRecorrenteManipulacao.getCpfCnpj())
                .appendQueryParameter("celularNovo", entidadeCartaoRecorrenteManipulacao.getCampoCelularNovoVindi())
                .appendQueryParameter("codContrato", entidadeCartaoRecorrenteManipulacao.getCodContratoEscolhido())
                .appendQueryParameter("codContratoItem", entidadeCartaoRecorrenteManipulacao.getCodContratoItemEscolhido())
                .appendQueryParameter("idCustomerClienteVindi", entidadeCartaoRecorrenteManipulacao.getIdClienteVindi())
                .appendQueryParameter("codClieCartaoVINDI", entidadeCartaoRecorrenteManipulacao.getCodClieCartao())
                .appendQueryParameter("cnpjEmpresa", entidadeCartaoRecorrenteManipulacao.getContratoEmpresaCNPJ())
                .appendQueryParameter("nomeEmpresa", entidadeCartaoRecorrenteManipulacao.getContratoEmpresaNome())
                .appendQueryParameter("cartaoNumero", entidadeCartaoRecorrenteManipulacao.getNumeroCartao())
                .appendQueryParameter("cartaoValidade", entidadeCartaoRecorrenteManipulacao.getValidade())
                .appendQueryParameter("cartaoCVV", entidadeCartaoRecorrenteManipulacao.getCVV())
                .appendQueryParameter("cartaoBandeira", entidadeCartaoRecorrenteManipulacao.getBandeira())
                .appendQueryParameter("seNovoCadastro", (entidadeCartaoRecorrenteManipulacao.isSeNovoCadastro() ? "1" : "0"))
                .appendQueryParameter("cartaoNomeTitular", entidadeCartaoRecorrenteManipulacao.getNomeTitular()); // +1 appendQuery caso queira mais parâmetros

        try {
            JSONObject jsonObject = new JSONArray(new RecebeJson().retornaJSON(Rotas.ROTA_PADRAO + Rotas.PAY_CARTAO_VINDI_CLIENTE, paramsBuilder)).getJSONObject(0);

            dados[0] = String.valueOf(jsonObject.getBoolean("status"));
            dados[1] = jsonObject.getString("msg");

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), "(linha " + e.getStackTrace()[0].getLineNumber() + ") setCartaoClienteVindi " + e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
        return dados;
    }


    /**
     * Retorna se o botão de débito automático deve aparecer
     *
     * @author Igor Maximo
     * @date 03/05/2021
     */
    public boolean getSeBotaoDebitoAutomaticoHabilitado() {
        try {
            return new JSONArray(new RecebeJson().retornaJSON(Rotas.ROTA_PADRAO + Rotas.SELECT_BOTAO_DEBITO_AUTOMATICO, null)).getJSONObject(0).getBoolean("botao_ativo");
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.out.println(e);
        }
        return false;
    }

}
