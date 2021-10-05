package com.rede.App.View.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.rede.App.View.Adapters.HistoricoPagamentoAdapter;
import com.rede.App.View.DAO.AppLogErroDAO;
import com.rede.App.View.DAO.PagamentosDAO;
import com.rede.App.View.JavaBeans.HistoricoPagamento;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.ToolBox.Animatoo;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.ToolBox.Internet;
import com.rede.App.View.ToolBox.LifeCycleObserver;
import com.rede.App.View.ToolBox.MyBounceInterpolator;
import com.rede.ncarede.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Exibe as todas as faturas pagas
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 */
public class MenuHistoricoPagamento extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected Context ctx = MenuHistoricoPagamento.this;

    private RecyclerView recyclerViewTitulo;
    private List<HistoricoPagamento> listaTitulo = new ArrayList<>();
    private HistoricoPagamentoAdapter adapterTitulos;

    // Para realizar o filtro por codsercli ////////////////////////////
    public volatile static String classeEnderecoPlano = "";
    public volatile static String classeNomePlano = "";

    protected HistoricoPagamento historicoPagamento = new HistoricoPagamento(
            null,
            "",
            "",
            "",
            "",
            false,
            "",
            "",
            "",
            "",
            "",
            "",
            0,
            "",
            "",
            "",
            "",
            0,
            "");
    PagamentosDAO pagamentosDAO = new PagamentosDAO();
    Usuario usuario = new Usuario();

    public static Context CTX;

    @SuppressLint({"SetTextI18n", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_historico_pagamentos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.CTX = MenuHistoricoPagamento.this;
        setRequisitarPermissaoStorage();

        listaTitulo.clear();
        recyclerViewTitulo = (RecyclerView) findViewById(R.id.recyclerViewHistoricoPagamento);

        adapterTitulos = new HistoricoPagamentoAdapter(listaTitulo);
        adapterTitulos.notifyDataSetChanged(); // limpa recyclerview

        recyclerViewTitulo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewTitulo.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTitulo.setAdapter(adapterTitulos);
        recyclerViewTitulo.setLayoutManager(new LinearLayoutManager(MenuHistoricoPagamento.this, LinearLayoutManager.VERTICAL, false));
        HistoricoPagamentoAdapter.ctx = MenuHistoricoPagamento.this;

        try {
            // Carrega em background
            new AsyncTaskCarregaTitulosPagos(MenuHistoricoPagamento.this).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Avaliar")
                    .setMessage("Por favor, avalie nosso método de pagamento!")
                    .setPositiveButton("Sim, avaliar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (MenuHistoricoPagamento.this != null) {
                                String link = "market://details?id=";
                                try {
                                    // play market available
                                    MenuHistoricoPagamento.this.getPackageManager()
                                            .getPackageInfo("com.rede.ncarede", 0);
                                    // not available
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                    // should use browser
                                    link = "https://play.google.com/store/apps/details?id=";
                                }
                                // starts external action
                                MenuHistoricoPagamento.this.startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(link + MenuHistoricoPagamento.this.getPackageName())));
                            }
                        }
                    })
                    .setNegativeButton("Cancelar", null);
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Botão voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Reproduz o efeito de vibrar o celular
                    ControladorInterface.setClickBotao(getApplicationContext());
                    if (new Internet(MenuPrincipal.CTX).verificaConexaoInternet()) {
                        startActivityForResult(new Intent(MenuHistoricoPagamento.this, MenuSegundaVia.class), 0);
                        finish();
                    } else {
                        Toast.makeText(MenuPrincipal.CTX, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                    }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                }
            }
        });


        TextView textView7 = (TextView) findViewById(R.id.textView7);
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeArq = "comprovante_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".pdf";
                getDownloadComprovante("http://187.95.0.22/producao/central/App/APIs/Downloader/ComprovanteDownloaded/compTit.php?pct=66356801", new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + nomeArq));
                // Abre o boleto após download
                setAbrirPDFComprovante(nomeArq);
            }
        });


        toolbar.setElevation(0f);

        // Para controlde de tempo da sessão
        LifeCycleObserver.context = this;
        // Animação de entrada
        Animatoo.animateSwipeLeft(this);
///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        /*
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        */
        // disabilita aba lateral esquerda
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }


    /**
     * Solicia permissão de storage ao usuário
     *
     * @author Igor Maximo
     * date 12/07/2020
     */
    private void setRequisitarPermissaoStorage() {
        //////////////////////// Requisita permissões de armazenamento ao usuário  ////////////////
        ActivityCompat.requestPermissions(MenuHistoricoPagamento.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_x, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


//////////////////////////////////// POPUPS DE PAGAMENTO /////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Realiza download da comprovantes em PDF do servidor
     *
     * @author Igor Maximo
     * @date 18/04/2019
     */
    public void getDownloadComprovante(String fileURL, File directory) {
        System.out.println("fileURL====================> " + fileURL);
        try {
            FileOutputStream f = new FileOutputStream(directory);
            URL u = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor("getDownloadComprovante GRAVA BOLETO " + usuario.getTipoCliente(), "" + this.getClass().getSimpleName() + " | " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " | ERRO MSG: " + e + " | CLASS: " + e.getClass().getName(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            e.printStackTrace();
        }
    }

    /**
     * Abre a fatura baixada do servidor na tela do app
     *
     * @author Igor Maximo
     * @data 18/04/2019
     */
    public void setAbrirPDFComprovante(String fileName) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(FileProvider.getUriForFile(MenuHistoricoPagamento.this, "com.rede.App.View.View.MenuSegundaVia", new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + fileName)), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor("setAbrirPDFComprovante GRAVA BOLETO " + usuario.getTipoCliente(), "" + this.getClass().getSimpleName() + " | " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " | ERRO MSG: " + e + " | CLASS: " + e.getClass().getName(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
    }

    private WebView webView;
    private boolean processandoInternetBanking = false;
    Dialog markerPopUpDialog = null;
    private String urlFinished = " ";

    public class WebScrollListener {
        private String element;
        private int margin;

        @JavascriptInterface
        public void onScrollPositionChange(String topElementCssSelector, int topElementTopMargin) {
            Log.d("WebScrollListener", "Scroll position changed: " + topElementCssSelector + " " + topElementTopMargin);
            element = topElementCssSelector;
            margin = topElementTopMargin;
        }
    }


    /**
     * Exibe o "modal" verde ou vermelho de acordo com resultado da transação CIELO
     *
     * @author Igor Maximo
     * @data 18/04/2019
     */
    public void setGeraPopUpTransacao(boolean autorizada, String msg) {
        View layout;
        if (autorizada) {
            layout = getLayoutInflater().inflate(R.layout.toast_transacao_autorizada, (ViewGroup) findViewById(R.id.toast_layout_root));
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(R.string.action_transacao_autorizada);
        } else {
            layout = getLayoutInflater().inflate(R.layout.toast_transacao_negada, (ViewGroup) findViewById(R.id.toast_layout_root));
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(msg);
        }
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
        // Recria a tela
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        setCarregarTitulos(classeNomePlano, classeEnderecoPlano);
                    } catch (Exception e) {
                        AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                        }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                    }
                }
            }, 1200);
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
    }

    /**
     * Exibe o "modal" do crud de dados do cartão de crédito
     *
     * @author Igor Maximo
     * @date 18/04/2019
     */
    private void setCarregarTitulos(String classe, String codSerCli) {
        listaTitulo.clear();
        recyclerViewTitulo = (RecyclerView) findViewById(R.id.recyclerViewHistoricoPagamento);

        adapterTitulos = new HistoricoPagamentoAdapter(listaTitulo);
        adapterTitulos.notifyDataSetChanged(); // limpa recyclerview

        recyclerViewTitulo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewTitulo.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTitulo.setAdapter(adapterTitulos);
        recyclerViewTitulo.setLayoutManager(new LinearLayoutManager(MenuHistoricoPagamento.this, LinearLayoutManager.VERTICAL, false));
        HistoricoPagamentoAdapter.ctx = MenuHistoricoPagamento.this;

        try {
            // Carrega em background
            new AsyncTaskCarregaTitulosPagos(MenuHistoricoPagamento.this).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //                            ASYNCTASK PARA CARREGAMENTO DOS TÍTULOS                             //
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskCarregaTitulosPagos extends AsyncTask<Void, Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Context mContext = null;
        static private final int ITERATIONS = 5;

        public AsyncTaskCarregaTitulosPagos(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... obj) {
            historicoPagamento = null;
            while (historicoPagamento == null) {
                ThreadRunningOperation();
                try {
                    historicoPagamento = pagamentosDAO.getHistoricoPagamento(usuario.getCodigo());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            mProgress.setProgress(progress[0]);
        }

        @Override
        protected void onPreExecute() {
            mProgress = new ProgressDialog(mContext);
            mProgress.setMessage("Processando...");
            mProgress.setIndeterminate(false);
            mProgress.setCancelable(false);
            mProgress.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            try {
                ////////////////////////////// EFEITO NUBANK INTERPOLAÇÃO ///////////////////////////////////////////////////////
                final Animation myAnim = AnimationUtils.loadAnimation(MenuHistoricoPagamento.this, R.anim.up_from_bottom);
                // Use bounce interpolator with amplitude 0.1 and frequency 15
                myAnim.setInterpolator(new MyBounceInterpolator(0.1, 15));
                recyclerViewTitulo.startAnimation(myAnim);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                HistoricoPagamento historicoPagamentoAux = null;
                for (int i = 0; i < historicoPagamento.getDadosValorCentavos().size(); i++) {
                    historicoPagamentoAux = new HistoricoPagamento(
                            MenuHistoricoPagamento.this,
                            historicoPagamento.getDadosDataCadastro().get(i),
                            historicoPagamento.getDadosNome().get(i),
                            historicoPagamento.getDadosFkCliente().get(i),
                            historicoPagamento.getDadosPedido().get(i),
                            historicoPagamento.getDadosSeBaixadaSimetra().get(i),
                            historicoPagamento.getDadosCodContrato().get(i),
                            historicoPagamento.getDadosCodContratoTitulo().get(i),
                            historicoPagamento.getDadosCodArquivoDoc().get(i),
                            historicoPagamento.getDadosVencimentoFatura().get(i),
                            historicoPagamento.getDadosFkEmpresa().get(i),
                            historicoPagamento.getDadosFkFormaPagamento().get(i),
                            Integer.parseInt(historicoPagamento.getDadosValorCentavos().get(i) + ""),
                            historicoPagamento.getDadosCieloMsg().get(i),
                            historicoPagamento.getDadosPaymentId().get(i),
                            historicoPagamento.getDadosCodigoRetorno().get(i),
                            historicoPagamento.getDadosCodigoAutorizacao().get(i),
                            historicoPagamento.getDadosStatusCode().get(i),
                            historicoPagamento.getDadosCompTit().get(i)
                    );
                    listaTitulo.add(historicoPagamentoAux);
                }

                recyclerViewTitulo.getViewTreeObserver().addOnPreDrawListener(
                        new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                recyclerViewTitulo.getViewTreeObserver().removeOnPreDrawListener(this);
                                for (int i = 0; i < recyclerViewTitulo.getChildCount(); i++) {
                                    View v = recyclerViewTitulo.getChildAt(i);
                                    v.setAlpha(0.0f);
                                    v.animate().alpha(1.0f)
                                            .setDuration(1000)
                                            .setStartDelay(i * 350)
                                            .start();
                                }
                                return true;
                            }
                        });
            } catch (Exception e) {
                System.err.println(e);
            }
            adapterTitulos.notifyDataSetChanged();
            mProgress.dismiss();
        }

        private void ThreadRunningOperation() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
        }
    }
}