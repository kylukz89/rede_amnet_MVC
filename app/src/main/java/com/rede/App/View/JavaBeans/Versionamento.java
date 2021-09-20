package com.rede.App.View.JavaBeans;

/*
    Entidade de Versionamento
 */
public final class Versionamento {

    private String id;
    private int versao_maior;  // 0 -> (0 para status alpha "fase de desenv.") -> Quando é alterada a API de maneira não compatível com versões anteriores. (MUDANÇAS MUITO DRÁSTICAS)
    private int versao_menor;  // 0 -> Sempre que uma nova funcionalidade for lançada
    private int correcao;      // 0 -> Incrementa sempre que qualquer ajuste que seja feito e seguido de um click no botão SALVAR da IDE.
    private String dataLancamento;
    private String dataInativacao;
    private String nomeVersao; // Ex: APLHA, BETA, CURRENT, RC, RELEASE...
    private String status; // Caso esteja INATIVO o sistema não funcionará

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersao_maior() {
        return versao_maior;
    }

    public void setVersao_maior(int versao_maior) {
        this.versao_maior = versao_maior;
    }

    public int getVersao_menor() {
        return versao_menor;
    }

    public void setVersao_menor(int versao_menor) {
        this.versao_menor = versao_menor;
    }

    public int getCorrecao() {
        return correcao;
    }

    public void setCorrecao(int correcao) {
        this.correcao = correcao;
    }

    public String getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getDataInativacao() {
        return dataInativacao;
    }

    public void setDataInativacao(String dataInativacao) {
        this.dataInativacao = dataInativacao;
    }

    public String getNomeVersao() {
        return nomeVersao;
    }

    public void setNomeVersao(String nomeVersao) {
        this.nomeVersao = nomeVersao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
