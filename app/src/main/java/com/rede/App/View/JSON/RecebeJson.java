package com.rede.App.View.JSON;


import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Recebe um JSON enviada do servidor para a aplicação
 * RETORNO:
 * 0                      - null
 * 1                      - JSON carregado
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 * @editado 02/03/2019
 */
public class RecebeJson {
    URL url = null;
    HttpURLConnection urlConnection = null;
    InputStream in = null;

    public String retornaJSON(String stringUrl, Uri.Builder builderAppends) { // teste via post
        try {
            url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setReadTimeout(VariaveisGlobais.HTTP_TIME_OUT);
            //urlConnection.setConnectTimeout(VariaveisGlobais.HTTP_URL_CONNECTION);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            String query = "";
            if (builderAppends != null) {
                query = builderAppends.build().getEncodedQuery();
            }
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            urlConnection.connect();
            in = new BufferedInputStream(urlConnection.getInputStream());

            return readStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return "";
    }

    public String retornaJSON(String stringUrl) {

        try {
            url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());

            return readStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return "";
    }


    public String readStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();

        return sb.toString();
    }
}
