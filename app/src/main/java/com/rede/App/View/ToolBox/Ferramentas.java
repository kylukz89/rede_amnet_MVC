package com.rede.App.View.ToolBox;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.TaskStackBuilder;

import com.rede.App.View.SQLite.SQLiteGeraTabelaGerenciamento;
import com.rede.App.View.View.MainActivity;
import com.rede.App.View.View.Splash;
import com.rede.ncarede.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import static android.content.Context.VIBRATOR_SERVICE;


/**
 * Métodos auxiliares genéricos para qualquer parte da aplicação
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 * @editado 02/03/2019
 */
public final class Ferramentas {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void geraNotificacaoPushPadrao(Context ctx, String titulo, String msg, int idNotification) {
        try {
            Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(700);
        } catch (Exception e) {
            System.err.println(e);
        }

        ////////////////////// VIBRAR ///////////////////////////
        Notification.Builder mBuilder = null;
        mBuilder = new Notification.Builder(ctx)
                .setSmallIcon(R.drawable.ic_notificacao_padrao)
                .setContentTitle(titulo).setStyle(new Notification.BigTextStyle())
                .setContentText(msg);
        Intent resultIntent = new Intent(ctx, MainActivity.class);
        android.app.TaskStackBuilder stackBuilder = android.app.TaskStackBuilder.create(ctx);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNotificationManager.notify(idNotification, mBuilder.build());
        }
        //////////////////////////////////////////////////////////
    }

    public static String[] concatenaVetores(String[] vetor1, String[] vetor2) {
        final int length = vetor1.length + vetor2.length;
        String[] vetorConcatenado = new String[length];

        int i = 0;
        int k = 0;
        int t = 0;
        int j = vetor1.length;

        while (i < length) {
            if (i >= vetor1.length) {
                vetorConcatenado[j] = vetor2[k];
                j++;
                k++;
            } else {
                if (i < vetor1.length) {
                    vetorConcatenado[t] = vetor1[t];
                    t++;
                }
            }
            i++;
        }
        return vetorConcatenado;
    }

    public static Bitmap convertBase64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public static String extraiPalavraEntreParenteses(String x) {
        boolean parenteseEsquerdo = false;
        boolean parenteseDireito = false;
        int pos1 = 0;
        int pos2 = 0;
        for (int i = 0; i < x.length(); i++) {
            if (x.substring(i, i + 1).equals("(")) {
                parenteseEsquerdo = true;
                pos1 = i;
            }
            if (x.substring(i, i + 1).equals(")")) {
                parenteseDireito = true;
                pos2 = i;
            }

        }
        return x.substring(pos1 + 1, pos2);
    }

    public String copiarTextClipBoard(Context context, String text) {
        try {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
                clipboard.setPrimaryClip(clip);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return text;
    }


    public static void disparaAlertaNotificacaoPadrao(Context ctx, String msg, long milliseconds) {
        // Dispara alerta preto e branco no relógio do dispositivo; igual qualquer outro app.
        Vibrator rr = (Vibrator) ctx.getSystemService(VIBRATOR_SERVICE);
        rr.vibrate(milliseconds);
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
        rr.vibrate(milliseconds);
    }

    public static void restartAPPeApagaCredenciais(Context context) {
        try {
            setApagaCredenciaisAcessoSQLite(context);
            PackageManager packageManager = context.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
            ComponentName componentName = intent.getComponent();
            Intent mainIntent = Intent.makeRestartActivityTask(componentName);
            context.startActivity(mainIntent);
            System.exit(0);
        } catch (Exception e) {

        }
    }

    public static void setRestartAppNovaSessao(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
            ComponentName componentName = intent.getComponent();
            Intent mainIntent = Intent.makeRestartActivityTask(componentName);
            context.startActivity(mainIntent);
            System.exit(0);
        } catch (Exception e) {

        }
    }

    public static void setEncerraAPP(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        System.exit(0);
    }

    public static void setRestartAPP(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        System.exit(0);
    }

    public static String setConverteImageViewBase64(ImageView imagem) {
        //String imageEncoded = null;
        Bitmap bitmap = null;
        bitmap = ((BitmapDrawable) imagem.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, VariaveisGlobais.QUALIDADE_IMAGEM, baos);
        byte[] byteArrayImage = baos.toByteArray();
        return Base64.encodeToString(byteArrayImage, Base64.NO_WRAP);
    }

    public static int getNumeroXTemporal(Spinner comboX, String[] vetor, String tipo, String mdX) {
        int i;
        int foundIndex = 0;
        i = 0;
        while (i < vetor.length) {
            if (comboX.getItemAtPosition(i).toString().equals(mdX)) {
                foundIndex = i;
                break;
            }
            i++;
        }
        return foundIndex;
    }


    public static String[] getContadorTemporal(int pontoInicial, int length) {
        int i = 0;
        String[] contagem = new String[length];
        while (i < length) {
            if (pontoInicial < 10) {
                contagem[i] = String.valueOf("0" + pontoInicial);
            } else {
                contagem[i] = String.valueOf(pontoInicial);
            }
            pontoInicial++;
            i++;
        }

        return contagem;
    }

    public static int getInteiroRandom(int limite) {
        int last = 0;
        Random r = new Random();
        int random = 0;
        random = r.nextInt(limite);
        last = random;
        if (random != last) {
            return random;
        } else {
            return r.nextInt(limite);
        }
    }

    public static String getCorHexadecimalAleatoria() {
        //(r,g,b)
        return String.format("%02x%02x%02x", getInteiroRandom(255), getInteiroRandom(255), getInteiroRandom(255));
    }


    public static String mascaraCPF(String CPF) {
        return CPF.substring(0, 3) + "." + CPF.substring(3, 6) + "." + CPF.substring(6, 9) + "-" + CPF.substring(9, CPF.length());
    }

    public static String mascaraCNPJ(String CNPJ) {
        return CNPJ.substring(0, 2) + "." + CNPJ.substring(2, 5) + "." + CNPJ.substring(5, 8) + "/" + CNPJ.substring(8, 12) + "-" + CNPJ.substring(12, CNPJ.length());
    }

    public static int retornaIconeCorPlano(String dataVencimento) {
        ManipulaData md = new ManipulaData();
        int icone = 0;
        if (md.getDiferencaDiasEntreUmaDataAteHoje((dataVencimento)) > 31) {
            icone = R.drawable.ic_plano_vermelha;
        }
        if (md.getDiferencaDiasEntreUmaDataAteHoje((dataVencimento)) < 31 && md.getDiferencaDiasEntreUmaDataAteHoje((dataVencimento)) > 0) {
            icone = R.drawable.ic_plano_laranja;
        }
        if (md.getDiferencaDiasEntreUmaDataAteHoje((dataVencimento)) < 0) {
            icone = R.drawable.ic_plano_verde;
        }
        return icone;
    }

    public static int getIconeCorFatura(String dataVencimento) {
        ManipulaData md = new ManipulaData();
        int icone = 0;
        if (md.getDiferencaDiasEntreUmaDataAteHoje((dataVencimento)) > 0) {
            icone = R.drawable.ic_fatura_vermelha;
        }
        if (md.getDiferencaDiasEntreUmaDataAteHoje((dataVencimento)) < 0) {
            icone = R.drawable.ic_fatura_laranja;
        }
        return icone;
    }

    public static int getIconeCorExtrato(String status) {
        int icone = 0;
        if (status.equals("Conta Liquidada") || status.equals("Conta Quitada")) {
            icone = R.drawable.ic_extrato_pago;
        } else {
            icone = R.drawable.ic_extrato_devedor;
        }
        return icone;
    }

    private static void setApagaCredenciaisAcessoSQLite(Context ctx) {
        SQLiteGeraTabelaGerenciamento sqliger = new SQLiteGeraTabelaGerenciamento(ctx);
        try {
            sqliger.atualizaLogin("update gerenciamento set cpf_cnpj = '', senha = '', tipo = ''");
        } catch (Exception e) {
            System.err.println("apagaCredenciaisAcessoSQLite() " + e);
        }

    }


    /**
     * Retorna a bandeira do cartão com base na numeração
     *
     * @author  Igor Maximo
     * @date    21/06/2021
     */
    public static String getBandeiraCartao(String cartaoNumero) {
        /*
                  Visa       | 4                                           | 13,16            | 3                    |
                | Mastercard | 5                                           | 16               | 3                    |
                | Diners     | 301,305,36,38                               | 14,16            | 3                    |
                | Elo        | 636368,438935,504175,451416,509048,509067,  |                  | 3(?)
                |            | 509049,509069,509050,509074,509068,509040,
                |            | 509045,509051,509046,509066,509047,509042,
                |            | 509052,509043,509064,509040                 |                  |
                |            | 36297, 5067,4576,4011                       | 16               | 3
                | Amex       | 34,37                                       | 15               | 4                    |
                | Discover   | 6011,622,64,65                              | 16               | 4                    |
                | Aura       | 50                                          | 16               | 3                    |
                | jcb        | 35                                          | 16               | 3                    |
                | Hipercard  | 38,60                                       | 13,16,19         | 3
        */
        String cartao = cartaoNumero.replace(".", "");

        if (cartao.substring(0, 1).equals("4")) {
            return "Visa";
        } else {
            if (cartao.substring(0, 1).equals("5")) {
                return "Master";
            } else {
                if (cartao.substring(0, 3).equals("301") || cartao.substring(0, 3).equals("305") || cartao.substring(0, 2).equals("36") || cartao.substring(0, 2).equals("38")) {
                    return "Diners";
                } else {
                    if (cartao.substring(0, 6).equals("636368") ||
                            cartao.substring(0, 6).equals("438935") ||
                            cartao.substring(0, 6).equals("504175") ||
                            cartao.substring(0, 6).equals("451416") ||
                            cartao.substring(0, 6).equals("509048") ||
                            cartao.substring(0, 6).equals("509067") ||
                            cartao.substring(0, 6).equals("509049") ||
                            cartao.substring(0, 6).equals("509069") ||
                            cartao.substring(0, 6).equals("509050") ||
                            cartao.substring(0, 6).equals("509074") ||
                            cartao.substring(0, 6).equals("509068") ||
                            cartao.substring(0, 6).equals("509045") ||
                            cartao.substring(0, 6).equals("509051") ||
                            cartao.substring(0, 6).equals("509046") ||
                            cartao.substring(0, 6).equals("509066") ||
                            cartao.substring(0, 6).equals("509047") ||
                            cartao.substring(0, 6).equals("509042") ||
                            cartao.substring(0, 6).equals("509052") ||
                            cartao.substring(0, 6).equals("509043") ||
                            cartao.substring(0, 6).equals("509064") ||
                            cartao.substring(0, 6).equals("509040") ||
                            cartao.substring(0, 5).equals("36297") ||
                            cartao.substring(0, 4).equals("5067") ||
                            cartao.substring(0, 4).equals("4576") ||
                            cartao.substring(0, 4).equals("6362") ||
                            cartao.substring(0, 4).equals("4011")) {
                        return "Elo";
                    } else {
                        if (cartao.substring(0, 2).equals("34") || cartao.substring(0, 2).equals("37")) {
                            return "Amex";
                        } else {
                            if (cartao.substring(0, 4).equals("6011")) {
                                return "Discover";
                            } else {
                                if (cartao.substring(0, 2).equals("35")) {
                                    return "Jcb";
                                } else {
                                    if (cartao.substring(0, 2).equals("38") || cartao.substring(0, 2).equals("60")) {
                                        return "Hipercard";
                                    } else {
                                        if (cartao.substring(0, 15).equals("000000000000000")) {
                                            return "Visa"; // Ambiente de testes
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Verifica se todos os campos de um form foram preenchidos
     *
     * @author  Igor Maximo
     * @date    21/06/2021
     */
    public static boolean getVerificaTodosCamposPreenchidos(boolean[] campos, Context ctx) {
        boolean status = false;
        for (int i = 0; i < campos.length; i++) {
            if (campos[i] == false) {
                return false;
            } else {
                if (i == campos.length - 1) {
                    status = true;
                    return status;
                }
            }
        }
        return status;
    }

    public static String setArredondaValorMoedaReal(String valor) {
        BigDecimal v = new BigDecimal(valor);
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return (nf.format(v)).replace("R$", "").replace("$", "");
    }

    // Retorna Marca e Modelo do smartphone
    public static String getMarcaModeloDispositivo(Context ctx) {
        String manufacturer = "";
        String model = "";
        try {
            manufacturer = Build.MANUFACTURER;
            model = Build.MODEL;
            if (model.startsWith(manufacturer)) {
                return setCapitalizarTexto(model);
            }
        } catch (Exception e) {

        }
        return "Marca: " + setCapitalizarTexto(manufacturer) + " / Modelo: " + model;
    }

    /**
     * Capitaliza um texto passado como parâmetro
     *
     * @author Igor Maximo
     * @date 21/01/2021
     */
    public static String setCapitalizarTexto(String str) {
        str = str.toLowerCase();
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public static String getNumeroCelular(Context ctx) {
        TelephonyManager tMgr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ctx, "android.permission.READ_PHONE_NUMBERS")
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return null;
        }
        return tMgr.getLine1Number();
    }


    /**
     * Formas de cobrança NÃO permitidas
     */
    public static boolean codCobsNaoPermitidos(String codCob) {

        boolean permitido = false;

        switch (codCob) {
            case "01VINDI": // 01VINDI
                permitido = true;
            case "01900NMFPT": // Vindi Telecom
                permitido = true;
            case "02F70W6ZCI": // Boleto Próprio 1
                permitido = true;
        }
        return permitido;
    }

    /**
     * Seta sombreamento em texto de textViews
     *
     * @author Igor Maximo
     * @date 27/03/2020
     */
    public void setSombraTextView(TextView textView, int cor) {
        textView.setShadowLayer(7.0f, 0, 0, cor);
    }

    /**
     * Seta sombreamento em texto de textViews
     *
     * @author Igor Maximo
     * @date 27/03/2020
     */
    public void setSombraTextView(TextView textView, float espessura, int cor) {
        textView.setShadowLayer(espessura, 0, 0, cor);
    }

    /**
     * Seta sombreamento em texto de textViews
     *
     * @author Igor Maximo
     * @date 27/03/2020
     */
    public void setSombraTextView(TextView textView, float espessura, int cor, float dx, float dy) {
        textView.setShadowLayer(espessura, dx, dy, cor);
    }

    /**
     * Seta sombreamento em texto de imageViews
     *
     * @author Igor Maximo
     * @date 30/03/2020
     */
    public void setSombraImageView(ImageView imageView) {
        imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////// GET MAC DO CELULAR ///////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < macBytes.length; i++) {
                    sb.append(String.format("%02X%s", macBytes[i], (i < macBytes.length - 1) ? "-" : ""));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return getMacAddress();
    }

    public static String loadFileAsString(String filePath) throws java.io.IOException {
        StringBuffer data = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            data.append(readData);
        }
        reader.close();
        return data.toString();
    }

    public static String getMacAddress() {
        try {
            return loadFileAsString("/sys/class/net/eth0/address")
                    .toUpperCase().substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
            return "02:00:00:00:00:00";
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Retorna o endereço MAC do roteador onde o celular está conectado
     *
     * @author Igor Maximo
     * @date 18/06/2019
     */
    public static String getMACAccessPoint(Context ctx) {
        WifiManager wifiManager = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getBSSID();
    }

    /**
     * Retorna o SSID do roteador onde o celular está conectado
     *
     * @author Igor Maximo
     * @date 18/06/2019
     */
    public static String getSSIDAccessPoint(Context ctx) {
        WifiManager wifiManager = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID().replace("\"", "");
    }

    /**
     * Retorna o endereço IP privado do roteador onde o celular está conectado
     *
     * @author Igor Maximo
     * @date 18/06/2019
     */
    public static String getIPPrivadoAccessPoint(Context ctx) {
        WifiManager wifiMgr = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        return String.valueOf(ipAddress);
    }

    /**
     * Retorna o endereço IP público do roteador onde o celular está conectado
     *
     * @author Igor Maximo
     * @date 18/06/2019
     */
    public static String getIPPublicoAccessPoint(Context ctx) throws Exception {
        //final NetworkInfo info = NetworkUtils.getNetworkInfo(context);

        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();

        RunnableFuture<String> futureRun = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                if ((info != null && info.isAvailable()) && (info.isConnected())) {
                    StringBuilder response = new StringBuilder();

                    try {
                        HttpURLConnection urlConnection = (HttpURLConnection) (
                                new URL("http://checkip.amazonaws.com/").openConnection());
                        urlConnection.setRequestProperty("User-Agent", "Android-device");
                        //urlConnection.setRequestProperty("Connection", "close");
                        urlConnection.setReadTimeout(15000);
                        urlConnection.setConnectTimeout(15000);
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setRequestProperty("Content-type", "application/json");
                        urlConnection.connect();

                        int responseCode = urlConnection.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_OK) {

                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }

                        }
                        urlConnection.disconnect();
                        return response.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //Log.w(TAG, "No network available INTERNET OFF!");
                    return null;
                }
                return null;
            }
        });

        new Thread(futureRun).start();

        try {
            return futureRun.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
        public void mudaCorBorda(RelativeLayout relativeLayout, View view) {
            RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.borderEffect); // id fetch from xml
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            paint.setColor(Color.GRAY);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5); // you can change the value of 5
            layout.setBackgroundDrawable(rectShapeDrawable);
        }
    */
    public static void setGeraAlerta(Context ctx, String alertaTitulo, String alertaMassivo) {
        try {
/////// VIBRAR
            Vibrator rr = (Vibrator) ctx.getSystemService(VIBRATOR_SERVICE);
            long milliseconds = 1500;
            rr.vibrate(milliseconds);

            ////////////////////// VIBRAR ///////////////////////////
            Notification.Builder mBuilder = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                mBuilder = new Notification.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_notificacao_padrao)
                        .setContentTitle(alertaTitulo).setStyle(new Notification.BigTextStyle())
                        .setContentText(alertaMassivo);
            }
            Intent resultIntent = new Intent(ctx, Splash.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
            stackBuilder.addParentStack(Splash.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mNotificationManager.notify((int) Math.random(), mBuilder.build());
            }
            //////////////////////////////////////////////////////////
        } catch (Exception e) {

        }

    }


    /**
     * Retorna o ícone com base no status do contrato
     *
     * @author Igor Maximo
     * @date 21/06/2021
     */
    public static int getIconeRoletaContratoPorStatus(String status) {
        if (status.equals("Cancelado")) {
            return (R.drawable.ic_plano_cinza);
        }
        if (status.equals("Bloqueado")) {
            return (R.drawable.ic_plano_vermelha);
        }
        if (status.equals("Em Ativação")) {
            return (R.drawable.ic_plano_azul);
        }
        if (status.equals("Aguardando Instalação")) {
            return (R.drawable.ic_plano_azul);
        }
        if (status.equals("Ativo")) {
            return (R.drawable.ic_plano_verde);
        }
        if (status.equals("Suspensão Temporária")) {
            return (R.drawable.ic_plano_laranja);
        }
        return (R.drawable.ic_plano_azul);
    }

    /**
     * Detecta se contém um caracter específico
     */
    public static boolean detectaCaracterEspecifico(String palavra, String caracter) {
        for (int i = 0; i < palavra.length(); i++) {
            if (palavra.substring(i).equals(caracter)) {
                return true;
            }
        }
        return false;
    }
}
