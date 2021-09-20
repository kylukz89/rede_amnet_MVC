package com.rede.App.View.ToolBox;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.rede.App.View.DAO.AppLogErroDAO;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.View.Splash;


/**
 * Verifica a disponibilidade da rede de dados, tanto WIFI quanto na 3G
 * LISTA DE RETORNO
 * <p>
 * OBS: Cuidado com bloqueio de pacotes ICMP (Alguns servidores não aceitam PING)
 *
 * @author Igor Maximo
 * @criado 22/02/2019
 * @editado 22/02/2019
 */
public class Internet {

    protected static String link = "localhost";
    Usuario usuario = new Usuario();

    private Context context;

    // Construtor
    public Internet(Context context) {
        this.context = context;
    }

    public boolean verificaConexaoInternet() {
        boolean conectado = false;
        try {
            final ConnectivityManager connMgr = (ConnectivityManager)
                    this.context.getSystemService(Context.CONNECTIVITY_SERVICE);

            final android.net.NetworkInfo wifi =
                    connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            final android.net.NetworkInfo mobile =
                    connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifi.isAvailable() && wifi.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
//                Toast.makeText(this.context, "Wifi" , Toast.LENGTH_LONG).show();
                conectado = true;
            } else if (mobile.isAvailable() && mobile.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
//                Toast.makeText(this.context, "Mobile 3G " , Toast.LENGTH_LONG).show();
                conectado = true;
            } else {
//                Toast.makeText(this.context, "No Network ", Toast.LENGTH_LONG).show();
                conectado = false;
            }
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
        return conectado;
    }

    private boolean ping(String IP) {
        String urlName = null;
        if (urlName == null) {
            urlName = IP;
        }
        java.net.HttpURLConnection urlConnection = null;
        try {
            java.net.URL url = new java.net.URL(urlName.toString());
            urlConnection = (java.net.HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == java.net.HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
