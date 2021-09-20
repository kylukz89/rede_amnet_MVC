package com.rede.App.View.JavaBeans;

/**
 * Classe responsável por manipular os dados de cadastro
 * da VINDI bem como todas as operações de cartão recorrente
 *
 * OBS: Herança dos atributos de cartão de crédito da tela de pagamento via crédito
 *
 * @author  Igor Maximo
 * @date    10/06/2021
 */
public class CartaoRecorrenteManipulacao extends PagamentoCartao {

    private int id;
    private String codContratoEscolhido;
    private String codContratoItemEscolhido;
    private String idClienteVindi;
    private String idCartaoVindi;
    private String codClieCartao;
    private boolean seCartaoVinculado;
    // Dados do cadastro pessoal
    private String cpfCnpj;
    private String campoCelularNovoVindi;
    // Dados da empresa fornecedora
    private String contratoEmpresaCNPJ;
    private String contratoEmpresaNome;
    private boolean seNovoCadastro;

    public String getCodContratoItemEscolhido() {
        return codContratoItemEscolhido;
    }

    public void setCodContratoItemEscolhido(String codContratoItemEscolhido) {
        this.codContratoItemEscolhido = codContratoItemEscolhido;
    }

    public boolean isSeNovoCadastro() {
        return seNovoCadastro;
    }

    public void setSeNovoCadastro(boolean seNovoCadastro) {
        this.seNovoCadastro = seNovoCadastro;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getCampoCelularNovoVindi() {
        return campoCelularNovoVindi;
    }

    public void setCampoCelularNovoVindi(String campoCelularNovoVindi) {
        this.campoCelularNovoVindi = campoCelularNovoVindi;
    }

    public String getContratoEmpresaCNPJ() {
        return contratoEmpresaCNPJ;
    }

    public void setContratoEmpresaCNPJ(String contratoEmpresaCNPJ) {
        this.contratoEmpresaCNPJ = contratoEmpresaCNPJ;
    }

    public String getContratoEmpresaNome() {
        return contratoEmpresaNome;
    }

    public void setContratoEmpresaNome(String contratoEmpresaNome) {
        this.contratoEmpresaNome = contratoEmpresaNome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodContratoEscolhido() {
        return codContratoEscolhido;
    }

    public void setCodContratoEscolhido(String codContratoEscolhido) {
        this.codContratoEscolhido = codContratoEscolhido;
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

    public String getCodClieCartao() {
        return codClieCartao;
    }

    public void setCodClieCartao(String codClieCartao) {
        this.codClieCartao = codClieCartao;
    }

    public boolean isSeCartaoVinculado() {
        return seCartaoVinculado;
    }

    public void setSeCartaoVinculado(boolean seCartaoVinculado) {
        this.seCartaoVinculado = seCartaoVinculado;
    }
}
