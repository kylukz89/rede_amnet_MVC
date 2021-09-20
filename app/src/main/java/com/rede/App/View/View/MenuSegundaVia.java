package com.rede.App.View.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
import com.rede.App.View.Adapters.TitulosAdapter;
import com.rede.App.View.DAO.AppLogErroDAO;
import com.rede.App.View.DAO.LinkBoletoDAO;
import com.rede.App.View.DAO.PagamentosDAO;
import com.rede.App.View.DAO.TituloDAO;
import com.rede.App.View.DAO.UsuarioDAO;
import com.rede.App.View.JavaBeans.PagamentoCartao;
import com.rede.App.View.JavaBeans.Titulo;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.ToolBox.Animatoo;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.ToolBox.Internet;
import com.rede.App.View.ToolBox.LifeCycleObserver;
import com.rede.App.View.ToolBox.ManipulaData;
import com.rede.App.View.ToolBox.MascarasPagamento;
import com.rede.App.View.ToolBox.MyBounceInterpolator;
import com.rede.App.View.ToolBox.VariaveisGlobais;
import com.rede.ncarede.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Exibe as faturas de um plano escolhido por ordem de pendência,
 * parar ser exibida a 2ª via; integração com CIELO.
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 */
public class MenuSegundaVia extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /*
    Cálculo de juros por dia de fatura vencida
    -------------------------------------------------
    (ValorPadrão + 10_ReaisMulta) * (0,13%_Juros) = y
    (R$ 89,90 + R$ 10,00) * 0,13% = 0,12987
    0,12987 + R$ 89,90 = 100,02 <- Resultado correto
    */
    public static String COD_CONTRATO_ITEM_ESCOLHIDO;
    protected Context ctx = MenuSegundaVia.this;
    private RecyclerView recyclerViewTitulo;
    private List<Titulo> listaTitulo = new ArrayList<>();
    private TitulosAdapter adapterTitulos;

    // Para realizar o filtro por codsercli ////////////////////////////
    public volatile static String classeInst = "";
    public volatile static String classeEnderecoPlano = "";
    public volatile static String classeInstCodSerCli = "";
    public volatile static String classeNomePlano = "";
    private int qtsDias;

    Calendar myCalendar = Calendar.getInstance();
    protected Titulo titulo = new Titulo(0, null, null, null, null, null, null, 0, null, null, null, null, null);
    TituloDAO tituloDAO = new TituloDAO();
    ManipulaData md = new ManipulaData();

    Usuario usuario = new Usuario();
    UsuarioDAO usudao = new UsuarioDAO();

    PagamentoCartao pagamento = new PagamentoCartao();
    PagamentosDAO pagamentoDAO = new PagamentosDAO();
    LinkBoletoDAO linkBoletoDAO = new LinkBoletoDAO();

    public static boolean seAutorizada = false;
    public static String msgTransacao = "";
    private boolean[] seCamposPreenchidos = new boolean[6];
    public static Context CTX;
    TextView textViewTituloSegundaVia;

    @SuppressLint({"SetTextI18n", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_segundavia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Inicia todos conteúdos da tela
        setIniciarTodosComponentes();
        // Botão voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Reproduz o efeito de vibrar o celular
                    ControladorInterface.setClickBotao(MenuSegundaVia.this);

                    if (new Internet(MenuPrincipal.CTX).verificaConexaoInternet()) {
                        startActivityForResult(new Intent(MenuSegundaVia.this, MenuSegundaViaContrato.class), 0);
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

        // Botão abrir histórico de pagamentos
        Button buttonBotaoHistoricoPagamentos = (Button) findViewById(R.id.buttonBotaoHistoricoPagamentos);
        buttonBotaoHistoricoPagamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reproduz o efeito de vibrar o celular
                ControladorInterface.setClickBotao(getApplicationContext());
                startActivity(new Intent(MenuSegundaVia.this, MenuHistoricoPagamento.class));

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


    public void setIniciarTodosComponentes() {
        this.CTX = MenuSegundaVia.this;
        // seta endereço do plano
        textViewTituloSegundaVia = (TextView) findViewById(R.id.textViewTituloSegundaVia);
        //////////////////////// Verifica instancia de classe /////////////////////////////////////
        if (classeInst.equals("MenuDadosPlano")) {
            iniciaClasse("MenuDadosPlano", classeInstCodSerCli);
            textViewTituloSegundaVia.setText((classeNomePlano + "\r\n" + classeEnderecoPlano));
        } else {
            iniciaClasse("MenuPrincipal", null);
            textViewTituloSegundaVia.setText("2ª Via - Todas as Faturas");
        }
        this.classeInst = ""; // Obrigatório para o correto funcionamento
        //////////////////////// Requisita permissões de armazenamento ao usuário  ////////////////
        setRequisitarPermissaoStorage();
    }

    /**
     * Solicia permissão de storage ao usuário
     *
     * @author Igor Maximo
     * date 12/07/2020
     */
    private void setRequisitarPermissaoStorage() {
        //////////////////////// Requisita permissões de armazenamento ao usuário  ////////////////
        ActivityCompat.requestPermissions(MenuSegundaVia.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
    }

    /**
     * Carrega todos os componentes da classe; semelhante ao método do java desktop initComponents()
     *
     * @author Igor Maximo
     * date 14/06/2019
     */
    private void iniciaClasse(final String classe, final String codSerCli) {
        ////////////////////////Setando data campos filtro //////////////////////////////////////
        EditText dataFrom = (EditText) findViewById(R.id.editTextDataDe); // De data
        EditText dataTo = (EditText) findViewById(R.id.editTextDataAte); // Até data
        dataFrom.setText(md.converteDataFormatoBR(md.getDataHojeDaquiMeses(-5)));
        dataTo.setText(md.converteDataFormatoBR(md.getDataHojeDaquiMeses(2)));
        ///////////////////  Preechimento Padrão //////////////////////////////////////////////////////////
        try {
            this.qtsDias = Integer.parseInt(new AsyncTaskParametrosBotoes().execute(0).get() + "");
        } catch (Exception e) {
            System.out.println(e);
        }
        /////////////////////////////////////////// Para funcionar calendário no EditText De ////////////////////
        EditText dataDe = (EditText) findViewById(R.id.editTextDataDe);
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEditTextDataDe();
            }

        };
        dataDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MenuSegundaVia.this, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dataDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasWindowFocus()) {
                    new DatePickerDialog(MenuSegundaVia.this, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////// Para funcionar calendário no EditText Ate ////////////////////
        EditText dataAte = (EditText) findViewById(R.id.editTextDataAte);
        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEditTextDataAte();
            }
        };

        dataAte.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(MenuSegundaVia.this, date2, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        dataAte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MenuSegundaVia.this, date2, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dataDe.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    setCarregarTitulos(classeNomePlano, COD_CONTRATO_ITEM_ESCOLHIDO);
                } catch (Exception e) {
                    AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                    }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                }
            }
        });
        dataAte.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                getCarregarFaturasFiltradas(classe, codSerCli);
                try {
                    setCarregarTitulos(classeNomePlano, COD_CONTRATO_ITEM_ESCOLHIDO);
                } catch (Exception e) {
                    AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                    }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                }
            }

        });

        try {
            setCarregarTitulos(classe, COD_CONTRATO_ITEM_ESCOLHIDO);
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
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

    /**
     * Exibe calendário ao usuário para escolher uma data
     *
     * @author Igor Maximo
     * data 18/04/2019
     */
    private void updateLabelEditTextDataDe() {
        EditText data = (EditText) findViewById(R.id.editTextDataDe);
        data.setText(new SimpleDateFormat(VariaveisGlobais.MASCARA_DATA_HORA, new Locale("pt", "BR")).format(myCalendar.getTime()));
    }

    /**
     * Exibe calendário ao usuário para escolher uma data
     *
     * @author Igor Maximo
     * data 18/04/2019
     */
    private void updateLabelEditTextDataAte() {
        EditText data = (EditText) findViewById(R.id.editTextDataAte);
        data.setText(new SimpleDateFormat(VariaveisGlobais.MASCARA_DATA_HORA, new Locale("pt", "BR")).format(myCalendar.getTime()));
    }


//////////////////////////////////// POPUPS DE PAGAMENTO /////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Recebe o índice escolhido do recyclerview para carregar os dados de uma fatura
     *
     * @author Igor Maximo
     * data 18/04/2019
     */
    public void recebeIndexFaturasAdapter(final int i) {
        setGeraDialogModosDePagamento(i);
    }

    /**
     * Exibe pop com os modos de pagamento
     *
     * @author Igor Maximo
     * @data 18/04/2019
     */
    private void setGeraDialogModosDePagamento(final int indexListaFatura) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pagar com?");
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("WrongViewCast") View view = inflater.inflate(R.layout.fragment_modos_pagamento, null);

        builder.setView(view);
        final AlertDialog show = builder.show();

        /////////////////////////////////////////////
        //             BOTÃO CRÉDITO               //
        /////////////////////////////////////////////
        LinearLayout linearBotaoCredito = (LinearLayout) view.findViewById(R.id.linearBotaoCredito);
        linearBotaoCredito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    // Reproduz o efeito de vibrar o celular
                    ControladorInterface.setClickBotao(getApplicationContext());

                    if (!Boolean.parseBoolean(new AsyncTaskParametrosBotoes().execute(1).get() + "")) {
                        ////// AVISO //////
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuSegundaVia.this);
                        // set title
                        alertDialogBuilder.setTitle("AVISO!");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Esta funcionalidade está temporariamente desativada devido a manutenções na plataforma, favor utilizar outra opção ou tente novamente mais tarde.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int idD) {
                                        show.dismiss();
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        //////////////
                    } else {
                        //                    if (!Ferramentas.codCobsNaoPermitidos(String.valueOf(segundaVia.getDadosCodCob().get(indexListaFatura)))) {
                        if (!(Integer.parseInt(String.valueOf(titulo.getDadosDias().get(indexListaFatura))) >= qtsDias) || Double.parseDouble(String.valueOf(titulo.getDadosValorCorrigidoManualmente().get(indexListaFatura))) < 1) { // Proteção após 30 dias o boleto some do banco
                               /* if (linkBoletoDAO.getSeBoletoFoiGeradoOuSolicitadoAnteriormente("" + segundaVia.getDadosContratoTitulo().get(indexListaFatura))) {
                                    ////// AVISO //////
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuSegundaVia.this);
                                    // set title
                                    alertDialogBuilder.setTitle("AVISO!");
                                    // set dialog message
                                    alertDialogBuilder
                                            .setMessage("Detectamos que foi gerado um boleto anteriormente para esta fatura. RECOMENDAMOS verificar se o boleto foi pago anteriormente por você ou por outra pessoa antes de prosseguir! Lembrando que pagamento por boleto pode demorar até 3 dias úteis para compensar.")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int idD) {
                                                    setGeraDialogPagarCartaoCreditoDebito(2, indexListaFatura, String.valueOf(segundaVia.getDadosEmpresa().get(indexListaFatura)));
                                                    show.dismiss();
                                                }
                                            });
                                    // create alert dialog
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                    //////////////
                                } else {*/
                            setGeraDialogPagarCartaoCreditoDebito(2, indexListaFatura, String.valueOf(titulo.getDadosEmpresa().get(indexListaFatura)));
                            show.dismiss();
                            //}
                        } else {
                            Toast.makeText(getApplicationContext(), "Não é possível pagar por crédito há mais de " + qtsDias + " dias! (" + String.valueOf(titulo.getDadosDias().get(indexListaFatura)) + " dias)", Toast.LENGTH_SHORT).show();
                        }
                        //                    } else {
                        //                        Toast.makeText(getApplicationContext(), "Este tipo de fatura não pode ser paga por crédito, apenas boleto!", Toast.LENGTH_SHORT).show();
                        //                    }
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        /////////////////////////////////////////////
        //             BOTÃO DÉBITO                //
        /////////////////////////////////////////////
        LinearLayout linearBotaoDebito = (LinearLayout) view.findViewById(R.id.linearBotaoDebito);
        linearBotaoDebito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Reproduz o efeito de vibrar o celular
                    ControladorInterface.setClickBotao(getApplicationContext());
//                    show.dismiss();
                    if (!Boolean.parseBoolean(new AsyncTaskParametrosBotoes().execute(2).get() + "")) {
                        ////// AVISO //////
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuSegundaVia.this);
                        // set title
                        alertDialogBuilder.setTitle("AVISO!");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Esta funcionalidade está temporariamente desativada devido a manutenções na plataforma, favor utilizar outra opção ou tente novamente mais tarde.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int idD) {
                                        show.dismiss();
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        //////////////
                    } else {
                        ////// AVISO //////
                        AlertDialog.Builder alertDialogBuilderAvisoDebito = new AlertDialog.Builder(MenuSegundaVia.this);
                        // set title
                        alertDialogBuilderAvisoDebito.setTitle("AVISO! (Internet Banking)");
                        // set dialog message
                        alertDialogBuilderAvisoDebito
                                .setMessage("O pagamento por cartão de débito pode não funcionar para alguns cartões, bancos ou bandeiras; " +
                                        "O cartão precisa estar HABILITADO para compras ONLINE (Internet Banking) e configurado com o nível de segurança exigido pelo banco. " +
                                        "Além disso o procedimento requer uma autenticação extra na plataforma do banco por segurança.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int idD) {
                                        show.dismiss();
                                        //                                    if (!Ferramentas.codCobsNaoPermitidos(String.valueOf(segundaVia.getDadosEmpresa().get(indexListaFatura)))) { // Verifica se a fatura pertence ao CODCOB


                                        if (!(Integer.parseInt(String.valueOf(titulo.getDadosDias().get(indexListaFatura))) >= qtsDias) || Double.parseDouble(String.valueOf(titulo.getDadosValorCorrigidoManualmente().get(indexListaFatura))) < 1) { // Proteção após 30 dias o boleto some do banco
                                            if (linkBoletoDAO.getSeBoletoFoiGeradoOuSolicitadoAnteriormente("" + titulo.getDadosContratoTitulo().get(indexListaFatura))) {
                                                ////// AVISO //////
                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuSegundaVia.this);
                                                // set title
                                                alertDialogBuilder.setTitle("AVISO!");
                                                // set dialog message
                                                alertDialogBuilder
                                                        .setMessage("Detectamos que foi gerado um boleto anteriormente para esta fatura. RECOMENDAMOS verificar se o boleto foi pago anteriormente por você ou por outra pessoa antes de prosseguir! Lembrando que pagamento por boleto pode demorar até 3 dias úteis para compensar.")
                                                        .setCancelable(false)
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int idD) {
                                                                setGeraDialogPagarCartaoCreditoDebito(3, indexListaFatura, String.valueOf(titulo.getDadosEmpresa().get(indexListaFatura)));

                                                                show.dismiss();
                                                            }
                                                        });
                                                // create alert dialog
                                                AlertDialog alertDialog = alertDialogBuilder.create();
                                                // show it
                                                alertDialog.show();
                                                //////////////
                                            } else {
                                                setGeraDialogPagarCartaoCreditoDebito(3, indexListaFatura, String.valueOf(titulo.getDadosEmpresa().get(indexListaFatura)));
                                                show.dismiss();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Não é possível pagar por crédito há mais de " + qtsDias + " dias! (" + String.valueOf(titulo.getDadosDias().get(indexListaFatura)) + " dias)", Toast.LENGTH_SHORT).show();
                                        }
                                        //} else {
                                        //    Toast.makeText(getApplicationContext(), "Você não possui permissão para pagar fatura com cartão! \nFavor entrar em contato com suporte!", Toast.LENGTH_SHORT).show();
                                        //}
                                        //                                    } else {
                                        //                                        Toast.makeText(getApplicationContext(), "Este tipo de fatura não pode ser paga por crédito, apenas boleto!", Toast.LENGTH_SHORT).show();
                                        //                                    }
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialogBuilderAvisoDebitoa = alertDialogBuilderAvisoDebito.create();
                        // show it
                        alertDialogBuilderAvisoDebitoa.show();
                        //////////////
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        /////////////////////////////////////////////
        //             BOTÃO BOLETO                //
        /////////////////////////////////////////////
        LinearLayout linearBotaoBoleto = (LinearLayout) view.findViewById(R.id.linearBotaoBoleto);
        linearBotaoBoleto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Reproduz o efeito de vibrar o celular
                    ControladorInterface.setClickBotao(getApplicationContext());
//                    show.dismiss();
                    if (!Boolean.parseBoolean(new AsyncTaskParametrosBotoes().execute(3).get() + "")) {
                        ////// AVISO //////
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuSegundaVia.this);
                        // set title
                        alertDialogBuilder.setTitle("AVISO!");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Esta funcionalidade está temporariamente desativada devido a manutenções na plataforma, favor utilizar outra opção ou tente novamente mais tarde.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int idD) {
                                        show.dismiss();
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        //////////////
                    } else {
                        // Permissão
                        setRequisitarPermissaoStorage();
                        if (!(Integer.parseInt(String.valueOf(titulo.getDadosDias().get(indexListaFatura))) >= qtsDias)) { // Proteção após 90 dias o boleto some do banco
                            if (show != null && show.isShowing()) {
                                show.dismiss();
                                try {
                                    // Utiliza a classe pagamento cartao apenas para gravar no banco a solicitação de gerar boleto
                                    pagamento.setTipoPagamentoCreditoDebito(1);
                                    pagamento.setNumeroCartao("-");
                                    pagamento.setValidade("-");
                                    pagamento.setNomeTitular("-");
                                    pagamento.setDocTitular("-");
                                    pagamento.setCelular("-");
                                    pagamento.setCVV("-");
                                    pagamento.setBandeira("-");
                                    pagamento.setValor("0");
                                    pagamento.setDataTransacaoPagamento(md.getDataSQL());
                                    pagamento.setCodigoCliente(usuario.getCodigo());
                                    pagamento.setCodigoArquivoDocumento((String) titulo.getDadosCodigoArquivoDocumento().get(indexListaFatura));
                                    pagamento.setCodigoContratoTitulo((String) titulo.getDadosNumBoleto().get(indexListaFatura));
                                    pagamento.setCodigoFatura((String) titulo.getDadosContratoTitulo().get(indexListaFatura)); // codfat
                                    pagamento.setVencFatura(md.converteDataFormatoSQL(String.valueOf(titulo.getDadosVencimento().get(indexListaFatura)).substring(0, 10))); // venc fat
                                    pagamento.setContrato(MenuSegundaVia.classeInstCodSerCli);

                                    try {
                                        pagamento.setObs(Ferramentas.getMarcaModeloDispositivo(MenuSegundaVia.this));
                                    } catch (Exception e) {
                                        System.err.println(e);
                                    }

                                    try {
                                        pagamentoDAO.setPagarFaturaMensalidadeCartaoCreditoOuBoleto(pagamento);
                                    } catch (Exception e) {
                                        System.err.println(e);
                                    }

                                    if (Integer.parseInt(titulo.getDadosCodigoArquivoDocumento().get(indexListaFatura).toString()) == 0) {
                                        Toast.makeText(getApplicationContext(), "Boleto não disponível", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    // Envia os dados do boleto para a string base 64 do boleto ser gerada dentro do servidor
                                    String linkBoletoGerado = new LinkBoletoDAO().getLinkBoletoEmitido(titulo.getDadosContratoTitulo().get(indexListaFatura).toString(), titulo.getDadosCodigoArquivoDocumento().get(indexListaFatura).toString());
                                    // DOWNLOAD FATURA PDF
                                    try {
                                        String nomeFile = "/fat" + titulo.getDadosCodigoArquivoDocumento().get(indexListaFatura).toString() + ".pdf";
                                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + nomeFile);
                                        try {
                                            // Se deu certo gerar o arquivo
                                            if (!file.createNewFile()) {
                                                AppLogErroDAO.gravaErroLOGServidor("FALHA AO CRIAR ARQUIVO EM BRANCO PARA PDF " + usuario.getTipoCliente(), "", usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                                            }
                                            // Faz download do boleto
                                            getDownloadFatura(linkBoletoGerado, file);
                                            // Abre o boleto após download
                                            setAbrirPDFBoleto(nomeFile);
                                        } catch (IOException e) {
                                            AppLogErroDAO.gravaErroLOGServidor("GRAVA BOLETO " + usuario.getTipoCliente(), "" + this.getClass().getSimpleName() + " | " + new Object() {
                                            }.getClass().getEnclosingMethod().getName() + " | ERRO MSG: " + e + " | CLASS: " + e.getClass().getName(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                                            e.printStackTrace();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.err.println("Erro ao gerar boleto = " + e);
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Não é possível gerar boleto vencido há mais de " + qtsDias + " dias, favor entrar em contato com nossa Central de Relacionamento. (" + String.valueOf(titulo.getDadosDias().get(indexListaFatura)) + " dias)", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        /////////////////////////////////////////////
        //             BOTÃO CÓD. BARRAS           //
        /////////////////////////////////////////////
        LinearLayout linearBotaoCodBarras = (LinearLayout) view.findViewById(R.id.linearCodBarras);
        linearBotaoCodBarras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Reproduz o efeito de vibrar o celular
                    ControladorInterface.setClickBotao(getApplicationContext());
//                    show.dismiss();
                    if (!Boolean.parseBoolean(new AsyncTaskParametrosBotoes().execute(4).get() + "")) {
                        ////// AVISO //////
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuSegundaVia.this);
                        // set title
                        alertDialogBuilder.setTitle("AVISO!");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Esta funcionalidade está temporariamente desativada devido a manutenções na plataforma, favor utilizar outra opção ou tente novamente mais tarde.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int idD) {
                                        show.dismiss();
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        //////////////
                    } else {
                        try {
                            String codBarrasCodFat = new Ferramentas().copiarTextClipBoard(MenuSegundaVia.this, tituloDAO.retornaCodBarrasCodFat(titulo.getDadosContratoTitulo().get(indexListaFatura).toString(), titulo.getDadosCodigoArquivoDocumento().get(indexListaFatura).toString()));
                            Toast.makeText(getApplicationContext(), "Código de barras copiado com sucesso! \n" + codBarrasCodFat, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            System.err.println(e);
                        }
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Realiza download da fatura em PDF do servidor
     *
     * @author Igor Maximo
     * @date 18/04/2019
     */
    private void getDownloadFatura(String fileURL, File directory) {
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
            AppLogErroDAO.gravaErroLOGServidor("getDownloadFatura GRAVA BOLETO " + usuario.getTipoCliente(), "" + this.getClass().getSimpleName() + " | " + new Object() {
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
    private void setAbrirPDFBoleto(String fileName) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(FileProvider.getUriForFile(MenuSegundaVia.this, "com.rede.App.View.View.MenuSegundaVia", new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + fileName)), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor("setAbrirPDFBoleto GRAVA BOLETO " + usuario.getTipoCliente(), "" + this.getClass().getSimpleName() + " | " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " | ERRO MSG: " + e + " | CLASS: " + e.getClass().getName(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
    }
////////////// Fim pertinente aos boletos //////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Exibe o "modal" do crud de dados do cartão de crédito
     *
     * @author Igor Maximo
     * @data 18/04/2019
     */
    @SuppressLint("SetTextI18n")
    private void setGeraDialogPagarCartaoCreditoDebito(final int modoPagamento, final int indexListaFatura, final String formaCobranca) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("MODO CRÉDITO");
        builder.setIcon(R.drawable.ic_cielo_png);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("WrongViewCast") final View view = inflater.inflate(R.layout.popup_pagar_cartao, null);

        if (modoPagamento == (3)) {
            ImageView prodImg = (ImageView) view.findViewById(R.id.imageViewBandeiras);
            int resID = view.getResources().getIdentifier("@drawable/bandeirasdebito", "drawable", MenuSegundaVia.this.getPackageName());
            prodImg.setImageResource(resID);
        } else {
            if (modoPagamento == (2)) {
                ImageView prodImg = (ImageView) view.findViewById(R.id.imageViewBandeiras);
                int resID = view.getResources().getIdentifier("@drawable/bandeiras", "drawable", MenuSegundaVia.this.getPackageName());
                prodImg.setImageResource(resID);
            }
        }

        try {
            usudao.getCarregaDadosUsuario();
        } catch (Exception e) {
            e.printStackTrace();
        }

//////////////////////////////////// CAMPOS ///////////////////////////////////////
        final EditText editTextValor = (EditText) view.findViewById(R.id.editTextValor);

        final EditText editTextNumeroCartao = (EditText) view.findViewById(R.id.editTextNumeroCartao);
        final EditText editTextValidade = (EditText) view.findViewById(R.id.editTextValidade);
        final EditText editTextCVV = (EditText) view.findViewById(R.id.editTextCVV);

        final EditText editTextTitular = (EditText) view.findViewById(R.id.editTextTitular);
        final EditText editTextCPF = (EditText) view.findViewById(R.id.editTextCPF);
        final EditText editTextFone = (EditText) view.findViewById(R.id.editTextFone);

//////////////////////////////////// Máscaras ///////////////////////////////////////
        editTextNumeroCartao.addTextChangedListener(MascarasPagamento.insert(MascarasPagamento.CARTAO_MASK, editTextNumeroCartao));
        editTextValidade.addTextChangedListener(MascarasPagamento.insert(MascarasPagamento.VALIDADE_MASK, editTextValidade));
        editTextCVV.addTextChangedListener(MascarasPagamento.insert(MascarasPagamento.CVV_MASK, editTextCVV));

        editTextCPF.addTextChangedListener(MascarasPagamento.insert(MascarasPagamento.CPF_MASK, editTextCPF));
        //editTextFone.addTextChangedListener(MascarasPagamento.insert(MascarasPagamento.CEL_MASK, editTextFone));
/////////////////////////////////////////////////////////////////////////////////////
///////////////////////////// PREENCHIMENTO AUTOMÁTICO //////////////////////////////

        editTextValor.setFocusable(false);
        editTextValor.setEnabled(false);
        editTextValor.setText("R$ " + Ferramentas.setArredondaValorMoedaReal(String.valueOf(titulo.getDadosValorCorrigidoManualmente().get(indexListaFatura))) + " (" + (modoPagamento == 3 ? "Débito" : "Crédito") + ")");

        editTextNumeroCartao.requestFocus();

        if (!usuario.getTipoCliente().equals("J")) {
            editTextTitular.setText(usuario.getNome());
            editTextCPF.setText(usuario.getCpfCnpj());
        }

        editTextFone.setText(usuario.getCelular());

/////////////////////////////////////////////////////////////////////////////////////
        builder.setView(view);
        final AlertDialog show = builder.show();


        final LinearLayout linearBotaoConfirmarPagamento = (LinearLayout) view.findViewById(R.id.linearBotaoConfirmarPagamento);
        linearBotaoConfirmarPagamento.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"WrongConstant", "ResourceAsColor"})
            @Override
            public void onClick(View v) {
                // Reproduz o efeito de vibrar o celular
                ControladorInterface.setClickBotao(getApplicationContext());
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////// VERIFICA CAMPOS PREENCHIDOS /////////////////////////////////////////////////////
                if (editTextNumeroCartao.getText().toString().replace(".", "").length() <= 15) {
                    editTextNumeroCartao.setError("Número do cartão é obrigatório!");
                    seCamposPreenchidos[0] = false;
                } else {
                    seCamposPreenchidos[0] = true;
                }

                if (editTextCVV.getText().toString().replace(".", "").length() < 3) {
                    editTextCVV.setError("Código de segurança existente atrás do cartão é obrigatório!");
                    seCamposPreenchidos[1] = false;
                } else {
                    seCamposPreenchidos[1] = true;
                }

                if (editTextValidade.getText().toString().replace("/", "").length() < 4) {
                    editTextValidade.setError("Campo validade do cartão é obrigatório! (Ex: 12/22)");
                    seCamposPreenchidos[2] = false;
                } else {
                    seCamposPreenchidos[2] = true;
                }

                if (editTextTitular.getText().toString().replace("/", "").length() <= 7) { // Para casos de clientes com nome curto demais
                    editTextTitular.setError("Nome do titular do cartão é obrigatório! (Min. 7 caracteres)");
                    seCamposPreenchidos[3] = false;
                } else {
                    seCamposPreenchidos[3] = true;
                }

                seCamposPreenchidos[4] = true;
                seCamposPreenchidos[5] = true;
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////// DAO PARA PAGAMENTO /////////////////////////////////////////////////////
                if (Ferramentas.getVerificaTodosCamposPreenchidos(seCamposPreenchidos, MenuSegundaVia.this)) {
                    try {
                        pagamento.setTipoPagamentoCreditoDebito(modoPagamento);
                        pagamento.setNumeroCartao(editTextNumeroCartao.getText().toString().replace(".", ""));
                        pagamento.setValidade(editTextValidade.getText().toString());
                        switch (editTextValidade.getText().toString().length()) {
                            case 5:
                                pagamento.setValidade(editTextValidade.getText().toString().substring(0, editTextValidade.getText().toString().indexOf("/")) + "/20" + editTextValidade.getText().toString().substring(editTextValidade.getText().toString().indexOf("/") + 1, editTextValidade.getText().length()));
                                break;
                        }
                        pagamento.setNomeTitular(editTextTitular.getText().toString());
                        pagamento.setDocTitular(editTextCPF.getText().toString().replace(".", "").replace("-", ""));
                        pagamento.setCelular(editTextFone.getText().toString().replace("(", "").replace(")", "").replace("-", "").trim());
                        pagamento.setCVV(editTextCVV.getText().toString());
                        pagamento.setBandeira(Ferramentas.getBandeiraCartao(editTextNumeroCartao.getText().toString().replace(".", "")));
                        pagamento.setValor((String) titulo.getDadosValorCorrigidoManualmente().get(indexListaFatura)); // CORRIGIDO ERRO DE CÁLCULO NO INTEGRATOR MANUALMENTE
                        pagamento.setDataTransacaoPagamento(md.getDataSQL());
                        pagamento.setCodigoCliente(usuario.getCodigo());
                        pagamento.setCodigoContrato((String) titulo.getDadosCodContrato().get(indexListaFatura));
                        pagamento.setCodigoContratoTitulo((String) titulo.getDadosNumBoleto().get(indexListaFatura));
                        pagamento.setCodigoArquivoDocumento((String) titulo.getDadosCodigoArquivoDocumento().get(indexListaFatura));  // Necessário para download do PDF
                        pagamento.setCodigoFatura((String) titulo.getDadosContratoTitulo().get(indexListaFatura));               // Necessário para download do PDF
                        pagamento.setFormaCobranca((String) titulo.getDadosFormaCobranca().get(indexListaFatura));
                        pagamento.setContrato(MenuSegundaVia.classeInstCodSerCli); // Plano que foi escolhido

                        if (md.converteDataFormatoSQL(String.valueOf(titulo.getDadosVencimento().get(indexListaFatura))).length() <= 8) {
                            pagamento.setVencFatura(md.converteDataFormatoSQL(String.valueOf(titulo.getDadosVencimento().get(indexListaFatura))).substring(0, 8)); // venc fat
                        } else {
                            pagamento.setVencFatura(md.converteDataFormatoSQL(String.valueOf(titulo.getDadosVencimento().get(indexListaFatura))).substring(0, 10)); // venc fat
                        }


                        pagamento.setEmpresa(formaCobranca);

                        // Grava Marca e Modelo do Celular
                        try {
                            pagamento.setObs(Ferramentas.getMarcaModeloDispositivo(MenuSegundaVia.this));
                        } catch (Exception e) {
                            System.err.println(e);
                        }

                        linearBotaoConfirmarPagamento.setFocusable(false);
                        linearBotaoConfirmarPagamento.setEnabled(false);

                        // Método de pagamento
                        if (modoPagamento == (3)) {
                            new AsyncTaskPagamento().execute(); // Método Assíncrono para processar pagamento por cartão
                        } else {
                            new AsyncTaskPagamento().execute(); // Método Assíncrono para processar pagamento por cartão
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (show != null && show.isShowing() && Ferramentas.getVerificaTodosCamposPreenchidos(seCamposPreenchidos, MenuSegundaVia.this) == true) {
                        show.dismiss();
                    }

                    seCamposPreenchidos = null;
                    seCamposPreenchidos = new boolean[6];
                } else {
                    Toast.makeText(getApplicationContext(), "Existem campos obrigatórios!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public class Client extends WebViewClient {
        private String element;
        private int margin;

        public void onScrollPositionChange(String topElementCssSelector, int topElementTopMargin) {
            Log.d("WebScrollListener", "Scroll position changed: " + topElementCssSelector + " " + topElementTopMargin);
            element = topElementCssSelector;
            margin = topElementTopMargin;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
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
     * Exibe o "modal" do crud de dados do cartão de débito
     * para ambiente bancário
     *
     * @author Igor Maximo
     * @data 02/04/2020
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setGeraDialogRedirecionamentoAmbienteBancario(String urlInternetBanking, final String codFat) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        View dialogView = this.getLayoutInflater().inflate(R.layout.fragment_internetbanking, null);
        webView = (WebView) dialogView.findViewById(R.id.webViewInternetBanking);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);// Zoom
        webView.getSettings().setDisplayZoomControls(false); // Zoom
        WebScrollListener scrollListener = new WebScrollListener(); // save this in an instance variable
        webView.addJavascriptInterface(scrollListener, "WebScrollListener");
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // Nunca armazenar o cache
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        processandoInternetBanking = true;
        new AsyncTaskProcessamentoWebView(MenuSegundaVia.this).execute(); // Para rodar o spinner enquanto carrega o ambiente bancário
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.setInitialScale((int) (92 * view.getScale()));
                // Se a operação foi finalizada
                if (url.contains("about:blank") || url.contains("fim")) {
                    Toast.makeText(getApplicationContext(), "Fim da operação!", Toast.LENGTH_SHORT).show();
                    setFechaDialogWebViewTransacao(codFat);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

        dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // Verifica o status da transação
                new PagamentosDAO().getStatusTransacaoPorDebito(codFat);
                setGeraPopUpTransacao(seAutorizada, msgTransacao); // cor do popup transação
            }
        });

        WebView web = (WebView) dialogView.findViewById(R.id.webViewInternetBanking);
        EditText edit = (EditText) dialogView.findViewById(R.id.edit);
        edit.setFocusable(true);
        edit.requestFocus();
        webView.loadUrl(urlInternetBanking);
        webView = web;
        dialogBuilder.setView(dialogView);
        markerPopUpDialog = dialogBuilder.create();
        markerPopUpDialog.show();
    }


    /**
     * Fecha o "modal" da webview após o término da transação
     * de débito
     *
     * @author Igor Maximo
     * @data 07/04/2019
     */
    public void setFechaDialogWebViewTransacao(String codFat) {
        // Verifica o status da transação
        new PagamentosDAO().getStatusTransacaoPorDebito(codFat);
        setGeraPopUpTransacao(seAutorizada, msgTransacao); // cor do popup transação
        markerPopUpDialog.dismiss();
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

            try {
                // Redireciona para o layout de histórico de comprovantes para download do comprovante
                startActivity(new Intent(MenuSegundaVia.this, MenuHistoricoPagamento.class));
            } catch (Exception e) {

            }
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
                        setCarregarTitulos(classeNomePlano, COD_CONTRATO_ITEM_ESCOLHIDO);
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
//
//    /**
//     * Exibe o "modal" para mostrar uma mensagem de aviso qualquer
//     *
//     * @author Igor Maximo
//     * @data 18/04/2019
//     */
//    public void geraPopUpAlertaMsg(String msg) {
//        LayoutInflater inflater = getLayoutInflater();
//        View layout;
//
//        layout = inflater.inflate(R.layout.toast_msg_alerta, (ViewGroup) findViewById(R.id.toast_layout_root_autorizada));
//        TextView text = (TextView) layout.findViewById(R.id.text);
//        text.setText(msg);
//
//        Toast toast = new Toast(getApplicationContext());
//        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.setView(layout);
//        toast.show();
//        // Recria a tela
//        recreate();
//    }


    /**
     * Exibe o "modal" do crud de dados do cartão de crédito
     *
     * @author Igor Maximo
     * @date 18/04/2019
     */
    private void setCarregarTitulos(String classe, String codSerCli) {
        listaTitulo.clear();
        recyclerViewTitulo = (RecyclerView) findViewById(R.id.recyclerviewFaturas);

        adapterTitulos = new TitulosAdapter(listaTitulo);
        adapterTitulos.notifyDataSetChanged(); // limpa recyclerview

        recyclerViewTitulo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewTitulo.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTitulo.setAdapter(adapterTitulos);
        recyclerViewTitulo.setLayoutManager(new LinearLayoutManager(MenuSegundaVia.this, LinearLayoutManager.VERTICAL, false));
        TitulosAdapter.ctx = MenuSegundaVia.this;

        try {
            final EditText campoDe = (EditText) findViewById(R.id.editTextDataDe);
            final EditText campoAte = (EditText) findViewById(R.id.editTextDataAte);
            String[] convDe = campoDe.getText().toString().split("/");
            String[] convAte = campoAte.getText().toString().split("/");
            // Carrega em background
            new AsyncTaskCarregaTitulos(MenuSegundaVia.this).execute(new Object[]{
                    convDe[2] + "-" + convDe[1] + "-" + convDe[0],
                    convAte[2] + "-" + convAte[1] + "-" + convAte[0],
                    classe,
                    codSerCli
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
//                            ASYNCTASK PARA CARREGAMENTO DOS TÍTULOS                             //
////////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskCarregaTitulos extends AsyncTask<Object[], Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Context mContext = null;
        static private final int ITERATIONS = 5;

        public AsyncTaskCarregaTitulos(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Object[]... obj) {
            titulo = null;
            while (titulo == null) {
                ThreadRunningOperation();
                try {
                    titulo = tituloDAO.getCarregarTitulosCliente(
                            String.valueOf(obj[0][0]),
                            String.valueOf(obj[0][1]),
                            String.valueOf(obj[0][2]),
                            String.valueOf(obj[0][3])
                    );
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

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) {
            try {
                FrameLayout frameLayoutMsgLista = (FrameLayout) findViewById(R.id.frameLayoutMsgLista);
                TextView textViewMsgLista = (TextView) findViewById(R.id.textViewMsgLista);
                ////////////////////////////// EFEITO NUBANK INTERPOLAÇÃO ///////////////////////////////////////////////////////
                final Animation myAnim = AnimationUtils.loadAnimation(MenuSegundaVia.this, R.anim.up_from_bottom);
                // Use bounce interpolator with amplitude 0.1 and frequency 15
                myAnim.setInterpolator(new MyBounceInterpolator(0.1, 15));
                recyclerViewTitulo.startAnimation(myAnim);
                textViewMsgLista.startAnimation(myAnim);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                Titulo titulos = null;

                if (titulo.getDadosVencimento().size() != 0) {
                    // Esconde as msgs
                    frameLayoutMsgLista.setVisibility(View.GONE);
                    textViewMsgLista.setText("");
                    // Exibe lista de títulos para serem pagos...
                    for (int i = 0; i < titulo.getDadosValorPagar().size(); i++) {
                        titulos = new Titulo(
                                Integer.parseInt(String.valueOf(titulo.getDadosDias().get(i))),
                                String.valueOf(titulo.getDadosValorCorrigidoManualmente().get(i)),
                                String.valueOf(titulo.getDadosValorPagar().get(i)),
                                String.valueOf(titulo.getDadosVencimento().get(i)),
                                String.valueOf(titulo.getDadosCorIconePertinente().get(i)),
                                String.valueOf(titulo.getDadosNumBoleto().get(i)),
                                String.valueOf(titulo.getDadosValorCorrigidoManualmente().get(i)),
                                i,
                                MenuSegundaVia.this,
                                null,
                                String.valueOf(titulo.getDadosTipoFatura().get(i)),
                                String.valueOf(titulo.getDadosEmpresa().get(i)),
                                String.valueOf(titulo.getDadosContratoTitulo().get(i)));
                        listaTitulo.add(titulos);
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

                } else {
                    // Exibe msg caso não existe planos disponíveis/suportados
                    frameLayoutMsgLista.setVisibility(View.VISIBLE);
                    textViewMsgLista.setText("Não há títulos disponíveis.");
                }
            } catch (Exception e) {
                System.err.println("=========> " + e);
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

////////////////////////////////////////////////////////////////////////////////////////////////////
//                            ASYNCTASK PARA PROCESSAMENTO DE PAGAMENTOS                          //
////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * AsyncTask para processamento paralelo do pagamento e spinner para congelar a tela
     *
     * @author Igor Maximo
     * @date 18/04/2019
     */
    private class AsyncTaskPagamento extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog mProgress = null;
        String[] dados;

        @Override
        protected Boolean doInBackground(String... strings) {
            ThreadRunningOperation();
            // Chama método de pagamento com integração CIELO no back-end
            dados = pagamentoDAO.setPagarFaturaMensalidadeCartaoCreditoOuBoleto(pagamento);
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            mProgress.setProgress(progress[0]);
        }

        @Override
        protected void onPreExecute() {
            this.mProgress = ProgressDialog.show(MenuSegundaVia.this, null, "Processando...", true);
            this.mProgress.setIndeterminate(false);
            this.mProgress.setCancelable(false);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(Boolean result) {
            mProgress.dismiss();
            // Abre modal de webview para ambiente bancário caso seja débito
            if (pagamento.getTipoPagamentoCreditoDebito() == (3)) {
                setGeraDialogRedirecionamentoAmbienteBancario(String.valueOf(dados[2]), pagamento.getCodigoFatura());
            } else {
                setGeraPopUpTransacao(seAutorizada, msgTransacao); // cor do popup transação
            }
        }

        private void ThreadRunningOperation() {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////
//                         ASYNCTASK PARA PROCESSAMENTO DA WEBVIEW DE DÉBITO                      //
////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * AsyncTask para processamento do link bancário retornado para débito
     *
     * @author Igor Maximo
     * @date 30/08/2021
     */
    private class AsyncTaskProcessamentoWebView extends AsyncTask<String, Integer, Boolean> {

        private ProgressDialog mProgress = null;
        private Context mContext = null;
        static private final int ITERATIONS = 1;

        public AsyncTaskProcessamentoWebView(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            ThreadRunningOperation();
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            mProgress.setProgress(progress[0]);
        }

        @Override
        protected void onPreExecute() {
            mProgress = new ProgressDialog(mContext);
            mProgress.setMessage("Redirecionando...");
            mProgress.setIndeterminate(false);
            mProgress.setCancelable(false);
            mProgress.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            try {
                mProgress.dismiss();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        private void ThreadRunningOperation() {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {

            }
        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////
//                           ASYNCTASK PARA CONSULTA DE DE PARÂMETROS                             //
////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Consulta se algumas das formas de pagamentos está desabiltiar/habilitada
     * para ilustrar uma mensagem ao usuário
     *
     * @author Igor Maximo
     * @date 06/02/2021
     */
    private Object retornoParametro;

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskParametrosBotoes extends AsyncTask<Integer, Integer, Object> {
        private ProgressDialog mProgress = null;
        private Context mContext = MenuSegundaVia.this;

        @Override
        protected Object doInBackground(Integer... opcao) {
            ThreadRunningOperation();
            // Consulta quais opções estão habilitadas/desabilitadas
            switch (Integer.parseInt(opcao[0] + "")) {
                case 0: // Parâmetro total de dias permitidos gerar segunda via
                    retornoParametro = new PagamentosDAO().getQtsDiasAposVencimentoPodeGerarBoleto();
                    return retornoParametro;
                case 1: // Permissão para Crédito
                    retornoParametro = new PagamentosDAO().getSeAppHabilitadoPagamentoPorCredito();
                    return retornoParametro;
                case 2: // Permissão para Débito
                    retornoParametro = new PagamentosDAO().getSeAppHabilitadoPagamentoPorDebito();
                    return retornoParametro;
                case 3: // Permissão para Boleto
                    retornoParametro = new PagamentosDAO().getSeAppHabilitadoEmissaoBoletos();
                    return retornoParametro;
                case 4: // Permissão para cód. de barras
                    retornoParametro = new PagamentosDAO().getSeAppHabilitadoCodigoBarras();
                    return retornoParametro;
            }
            return retornoParametro;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            mProgress.setProgress(progress[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress = new ProgressDialog(MenuSegundaVia.this);
            mProgress.setMessage("Processando...");
            mProgress.setIndeterminate(false);
            mProgress.setCancelable(false);
            mProgress.show();
        }

        @Override
        protected void onPostExecute(Object result) {
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