package com.rede.App.View.ToolBox;

import com.rede.App.View.DAO.AppLogErroDAO;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.View.MenuPrincipal;
import com.rede.App.View.View.Splash;

/**
 * Classe responsável por verificar constantemente
 * se a sessão ainda está ativa via thread
 * para caso não esteja, encerrar o aplicativo pra evitar bugs
 *
 * @author Igor Maximo
 * @date 06/03/2021
 */
public class ControleSessao {

    final Usuario usuario = new Usuario();

    /**
     * Método que inicia uma thread que verifica constantemente
     * se o objeto "usuario" estático ainda contém valores
     *
     * @author Igor Maximo
     * @date 06/03/2021
     */
    public void getControlarSeSessaoAtivaUsuario() {
        try {
            setStartThreadDeConsulta();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Thread que roda constantemente em busco de verificar
     * se o objeto "usuario" contém valores válidos para
     * o app operar, retorna false caso o atributo seja limpo
     * a fim de previnir bugs e garbage collector indevidos
     *
     * @author Igor Maximo
     * @date 06/03/2021
     */
    private boolean setStartThreadDeConsulta() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int i = 0;
                    while (true) {
//                        System.out.println(i +  " >> "+Thread.currentThread().getId()+" ==========> " + usuario.getNome());
                        if (usuario.getNome() == null) {
                            try {
                                Ferramentas.setRestartAppNovaSessao(MenuPrincipal.CTX);
                            } catch (Exception e) {
                                AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                                System.out.println("setStartThreadDeConsulta() 1" + e.getMessage());
                            }
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        i++;
                    }
                }
            }).start();
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            System.out.println("setStartThreadDeConsulta() 2" + e.getMessage());
        }
        return false;
    }
}