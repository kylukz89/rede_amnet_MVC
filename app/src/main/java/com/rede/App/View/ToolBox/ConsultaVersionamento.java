package com.rede.App.View.ToolBox;

import com.rede.App.View.DAO.VersionamentoDAO;
import com.rede.App.View.JavaBeans.Versionamento;
/*
    VERSAO_APP_LOCAL = {1, 0, 0};
 */
public final class ConsultaVersionamento {

    ManipulaData md = new ManipulaData();

    Versionamento versionamento = new Versionamento();
    VersionamentoDAO verdao = new VersionamentoDAO();

    boolean aceitavel = false;

    public boolean retornaMenorVersaoAceitavel() {

        try {
            versionamento = verdao.consultaVersionamentoEStatus(versionamento);
            // Se getVersao_maior mais antiga e ativa do banco for igual ou maior que do App.
            // Ex: Se 2 >= 3 -> false.

            if (Integer.parseInt(VariaveisGlobais.VERSAO_APP_LOCAL[0]+""+VariaveisGlobais.VERSAO_APP_LOCAL[1]+""+VariaveisGlobais.VERSAO_APP_LOCAL[2])
                >=
                Integer.parseInt(versionamento.getVersao_maior()+""+versionamento.getVersao_menor()+""+versionamento.getCorrecao())) {
                aceitavel = true;
            }

        } catch (Exception e) {
            System.err.println("retornaMenorVersaoAceitavel() "+e);
        }
        return aceitavel;
    }

}
