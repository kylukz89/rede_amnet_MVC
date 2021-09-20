package com.rede.App.View.JavaBeans;


import android.content.Context;
import android.graphics.Bitmap;

/**
 * Entidade com os dados de todos os cartões recorrentes
 *
 * @author      Igor Maximo
 * @date        07/06/2021
 */
public class CartaoRecorrente {

    private String idPerfil;
    private String cpfCnpj;
    private String codigoCliente;
    private String codContrato;
    // Dados do cartão
    private String cartaoNumero;
    private String cartaoBandeira;
    private String cartaoDataValidade;
    private String cartaoPlataforma;
    private Bitmap cartaoImagemBase64Bandeira;
    private boolean seContratoVinculado;
    private String idClienteVindi;
    private String idCartaoVindi;
    // Fk empresa que vindi pertence
    private String fkEmpresaCNPJ;
    private Context context;

    public CartaoRecorrente(String idPerfil, String cpfCnpj, String codigoCliente, String codContrato, String cartaoNumero, String cartaoBandeira, String cartaoDataValidade, String cartaoPlataforma, Bitmap cartaoImagemBase64Bandeira, boolean seContratoVinculado, String idClienteVindi, String idCartaoVindi, String fkEmpresaCNPJ, Context context) {
        this.idPerfil = idPerfil;
        this.cpfCnpj = cpfCnpj;
        this.codigoCliente = codigoCliente;
        this.codContrato = codContrato;
        this.cartaoNumero = cartaoNumero;
        this.cartaoBandeira = cartaoBandeira;
        this.cartaoDataValidade = cartaoDataValidade;
        this.cartaoPlataforma = cartaoPlataforma;
        this.cartaoImagemBase64Bandeira = cartaoImagemBase64Bandeira;
        this.seContratoVinculado = seContratoVinculado;
        this.idClienteVindi = idClienteVindi;
        this.idCartaoVindi = idCartaoVindi;
        this.fkEmpresaCNPJ = fkEmpresaCNPJ;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getFkEmpresaCNPJ() {
        return fkEmpresaCNPJ;
    }

    public void setFkEmpresaCNPJ(String fkEmpresaCNPJ) {
        this.fkEmpresaCNPJ = fkEmpresaCNPJ;
    }

    public String getIdClienteVindi() {
        return idClienteVindi;
    }

    public void setIdClienteVindi(String idClienteVindi) {
        this.idClienteVindi = idClienteVindi;
    }

    public String getIdCartaoVindi() {
        return idCartaoVindi;
    }

    public void setIdCartaoVindi(String idCartaoVindi) {
        this.idCartaoVindi = idCartaoVindi;
    }

    public Bitmap getCartaoImagemBase64Bandeira() {
        return cartaoImagemBase64Bandeira;
    }

    public void setCartaoImagemBase64Bandeira(Bitmap cartaoImagemBase64Bandeira) {
        this.cartaoImagemBase64Bandeira = cartaoImagemBase64Bandeira;
    }

    public String getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(String idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getCodContrato() {
        return codContrato;
    }

    public void setCodContrato(String codContrato) {
        this.codContrato = codContrato;
    }

    public String getCartaoNumero() {
        return cartaoNumero;
    }

    public void setCartaoNumero(String cartaoNumero) {
        this.cartaoNumero = cartaoNumero;
    }

    public String getCartaoBandeira() {
        return cartaoBandeira;
    }

    public void setCartaoBandeira(String cartaoBandeira) {
        this.cartaoBandeira = cartaoBandeira;
    }

    public String getCartaoDataValidade() {
        return cartaoDataValidade;
    }

    public void setCartaoDataValidade(String cartaoDataValidade) {
        this.cartaoDataValidade = cartaoDataValidade;
    }

    public String getCartaoPlataforma() {
        return cartaoPlataforma;
    }

    public void setCartaoPlataforma(String cartaoPlataforma) {
        this.cartaoPlataforma = cartaoPlataforma;
    }

    public boolean isSeContratoVinculado() {
        return seContratoVinculado;
    }

    public void setSeContratoVinculado(boolean seContratoVinculado) {
        this.seContratoVinculado = seContratoVinculado;
    }
}
