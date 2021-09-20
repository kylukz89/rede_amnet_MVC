package com.rede.App.View.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rede.App.View.Adapters.BotoesMenuPrincipalAdapter;
import com.rede.App.View.Adapters.ContratoAdapter;
import com.rede.App.View.DAO.AppLogErroDAO;
import com.rede.App.View.DAO.AtendimentoDAO;
import com.rede.App.View.DAO.AutenticacaoDAO;
import com.rede.App.View.DAO.CartaoRecorrenteVindiDAO;
import com.rede.App.View.DAO.ContratoRoletaDAO;
import com.rede.App.View.DAO.IPTVDAO;
import com.rede.App.View.DAO.NotificacaoDAO;
import com.rede.App.View.DAO.PotencialClienteDAO;
import com.rede.App.View.JavaBeans.BotoesMenuPrincipal;
import com.rede.App.View.JavaBeans.ContratoRoleta;
import com.rede.App.View.JavaBeans.Notificacao;
import com.rede.App.View.JavaBeans.PotencialCliente;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.SQLite.SQLiteGeraTabelaTutorial;
import com.rede.App.View.ToolBox.CirclePagerIndicatorDecoration;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.ToolBox.LifeCycleObserver;
import com.rede.App.View.ToolBox.MenusAbaLateralEsquerda;
import com.rede.App.View.ToolBox.MyBounceInterpolator;
import com.rede.App.View.ToolBox.SnapHelperOneByOne;
import com.rede.ncarede.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Tela principal do APP com todas as funcionalidades
 *
 * @author Igor Maximo
 * @date 19/02/2019
 */
public class MenuPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Valida se é o primeiro login
    //////////////////////////////////////////////////////////////////////////////
    public static boolean CLIENTE_POTENCIAL; // Verifica se é nosso cliente ou não
    public static String CPFCNPJ_CLIENTE_POTENCIAL; // Verifica nome nosso cliente em potencial
    //////////////////////////////////////////////////////////////////////////////
    // Variáveis de contexto
    public static Context CTX;
    public static Activity CTX_ACTIVITY;
    // Nome e tipo do cliente
    public static String TIPO_CLIENTE;
    public static String NOME_CLIENTE;
    // Recycler da roleta de planos
    public RecyclerView recyclerViewRoletaPlanos;
    // Recycler dos botões abaixo dos planos
    public RecyclerView recyclerViewRoletaBotoesMenuPrincipal;
    private List<ContratoRoleta> planosLista = new ArrayList<>();
    private List<BotoesMenuPrincipal> botoesLista = new ArrayList<>();
    private ContratoAdapter adapterRecyclerViewRoletaPlanos;
    private BotoesMenuPrincipalAdapter adapterRecyclerViewRoletaBotoesMenuPrincipal;
    // SQLite banco para exibir tutorial
    private SQLiteGeraTabelaTutorial sqLiteGeraTabelaTutorial = new SQLiteGeraTabelaTutorial(MenuPrincipal.this);
    // Notificações
    private NotificacaoDAO notificacaoDAO = new NotificacaoDAO();
    private Notificacao notificacoesCliente = null;   // Verifica se o cliente possui notificações
    private ArrayList sePossuiNotificacoes = null;  // Verifica se o cliente possui notificações
    private ArrayList sePossuiNotificacaoAvaliacaoTecnica = null;  // Verifica se o cliente possui notificações pendentes
    private ArrayList seDependeConfirmacaAvaliacaoTecnica = null;  // Verifica se a notificação depende de alguma ação do usuário
    // Entidade usuário logado
    private Usuario usuario = new Usuario();
    // Armazena o token do firebase
    public volatile static String TOKEN_FIREBASE;
    // Carrega vários processos em segundo plano para alivio da main thread
    private Handler handlerGeralSegundoPlano;
    protected ImageView imageViewIconeFlag;
    // View da tela principal
    private View view;
    private ContratoRoleta contrato;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Remove título
        view = View.inflate(MenuPrincipal.this, R.layout.content_menu_principal, null);
        // Atribui valor padrão para as variáveis de contexto
        MenuPrincipal.CTX = MenuPrincipal.this;
        MenuPrincipal.CTX_ACTIVITY = (Activity) MenuPrincipal.this;


        try {
            // Verifica o ciclo de vida do App
            new LifeCycleObserver().onCreate();
            // Para controlde de tempo da sessão
            LifeCycleObserver.context = this;
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), "" + this.getClass().getSimpleName() + " | " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " | ERRO MSG: " + e + " | CLASS: " + e.getClass().getName(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
        // Carrega todos os conteudos da tela
        initComponents();

        RelativeLayout relativeIconeNotificacaoTopo = (RelativeLayout) findViewById(R.id.relativeIconeNotificacaoTopo);
        relativeIconeNotificacaoTopo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reproduz o efeito de vibrar o celular
                ControladorInterface.setClickBotao(getApplicationContext());

                setAbrirCentralNotificacoes(view);
            }
        });
    }

    /**
     * Abre a central de notificações
     *
     * @author Igor Maximo
     * @date 04/01/2021
     */
    public void setAbrirCentralNotificacoes(View view) {
        try {
            new AsyncTaskCentralNotificacoes().execute();
        } catch (Exception e) {

        }
    }

    /**
     * Inicia o carregamento da tela principal
     *
     * @author Igor Maximo
     * @date 09/01/2021
     */
    private void initComponents() {
        try {
            try {
                //////////////////////////////// FIREBASE TOKEN ////////////////////////////////
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        // Gera token do firebase
                        TOKEN_FIREBASE = task.getResult();
                        // Atualiza Token do firebase em segundo plano para aliviar a main thread
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    System.err.println("TokenFirebase======> " + TOKEN_FIREBASE);
                                    new AutenticacaoDAO().setAtualizaTokenDBFirebase(usuario.getCodigo(), TOKEN_FIREBASE);
                                } catch (Exception e) {
                                    AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                                    }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                                }
                            }
                        }, 0);
                    }
                });
            /*
            int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
            switch(screenSize) {
                case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                    Toast.makeText(this, "Extra Large Screen", Toast.LENGTH_LONG).show();
                    break;
                case Configuration.SCREENLAYOUT_SIZE_LARGE:
                    Toast.makeText(this, "Large Screen", Toast.LENGTH_LONG).show();
                    break;
                case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                    Toast.makeText(this, "Normal Screen " + screenSize, Toast.LENGTH_LONG).show();
                    break;
                case Configuration.SCREENLAYOUT_SIZE_SMALL:
                    Toast.makeText(this, "Small Screen", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(this, "Screen size is not xlarge, large, normal or small", Toast.LENGTH_LONG).show();
            }
            */
                //////////////////////////////////////////////////////////////////////////////
            } catch (Exception e) {
                AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            }


            try {
                //////////////////////////////// PUSHS ////////////////////////////////
                // Verifica quais notificações chegaram para o cliente e se as mesmas são de avaliação ou autorização
                setVerificarNotificacoesCliente();
            } catch (Exception e) {
                AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            }

            setContentView(R.layout.activity_menu_menuprincipal);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            try {
                //
                //////////////////////////////// CENTRAL ////////////////////////////////
                // Central de notificações
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("botao apertado");

                        Intent i = new Intent(MenuPrincipal.this, MenuCentralNotificacoes.class);
                        startActivity(i);
                    }
                });
            } catch (Exception e) {
                AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            }
            TextView textViewNomeCliente = (TextView) findViewById(R.id.textviewNomeCliente);
            PotencialCliente clientePotencialAux = new PotencialCliente();
            PotencialCliente clientePotencial = new PotencialCliente();
            clientePotencial.setCampoCPFCNPJ(CPFCNPJ_CLIENTE_POTENCIAL);
            clientePotencialAux = new PotencialClienteDAO().retornaDadosClientePotencial(clientePotencial);
            try {
                if (this.CLIENTE_POTENCIAL) {
                    MenuTesteVelocidade.clientePotencial = this.CLIENTE_POTENCIAL;
                    MenuTesteVelocidade.clientePotencialCPFCNPJ = this.CPFCNPJ_CLIENTE_POTENCIAL;
                    MenuTesteVelocidade.clientePotencialNome = clientePotencialAux.getCampoNomeCompleto();
                    textViewNomeCliente.setText(clientePotencialAux.getCampoNomeCompleto().substring(0, 1) + clientePotencialAux.getCampoNomeCompleto().substring(1, clientePotencialAux.getCampoNomeCompleto().indexOf(" ")).toLowerCase());
                } else {
                    textViewNomeCliente.setText(NOME_CLIENTE.substring(0, 1) + NOME_CLIENTE.substring(1, NOME_CLIENTE.indexOf(" ")).toLowerCase());
                }
                ////////////////////////////// EFEITO NUBANK INTERPOLAÇÃO ///////////////////////////////////////////////////////
                try {
                    final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.down_from_top);
                    // Use bounce interpolator with amplitude 0.1 and frequency 15
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 15);
                    myAnim.setInterpolator(interpolator);
                    textViewNomeCliente.startAnimation(myAnim);
                } catch (Exception e) {
                    System.err.println(e);
                }
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            } catch (Exception e) {
                AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            }
            // Critério para exibir o tutorial
            try {
                if (sqLiteGeraTabelaTutorial.selectUltimoTutorial("select _id, lido from tutorial", 2)[1] == null) {
                    sqLiteGeraTabelaTutorial.cadastra1QuebraIncremento();
                }
                if (sqLiteGeraTabelaTutorial.selectUltimoTutorial("select _id, lido from tutorial", 2)[1].equals("N")) {
                    try {
                        this.setGeraDialogTutorial();
                    } catch (Exception e) {
                        AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                        }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                    }
                }
            } catch (Exception e) {
                AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            }

/////////////////////////////////////////// Pertinente Carrar RecycleViews ////////////////////////////////////////
            setCarregarRoletaPlanos();


        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    /*    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
*/
        // disabilita aba lateral esquerda
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void GoToLogo() {
        Intent i = new Intent(MenuPrincipal.this, MenuCentralNotificacoes.class);
        startActivity(i);
    }

    /**
     * Verifica se o cliente possui notificações
     * para ilustrar o sino na parte superior direita
     *
     * @author Igor Maximo
     * @date 13/01/2021
     */
    public void setVerificarNotificacoesCliente() {
        try {
            // Atualiza Token do firebase em segundo plano para aliviar a main thread
            handlerGeralSegundoPlano = new Handler();
            handlerGeralSegundoPlano.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Remove bolinha de notificações para posterior consulta se existe
                        imageViewIconeFlag = (ImageView) findViewById(R.id.imageViewIconeFlagTopo);
                        imageViewIconeFlag.setVisibility(View.GONE);
                        // Verifica se há notificações do tipo avaliação técnica para o cliente
                        notificacoesCliente = notificacaoDAO.getListaTodasNotificacoesUsuario();
                        sePossuiNotificacoes = notificacoesCliente.getDadosSeLido();
                        seDependeConfirmacaAvaliacaoTecnica = notificacoesCliente.getDadosSeDependeNotificacao();
                        sePossuiNotificacaoAvaliacaoTecnica = notificacoesCliente.getDadosTipoNotificacao();
                        // Abre popup de avaliação de visita técnica para o usuário escolher entre as 5 estrelas
                        if (sePossuiNotificacaoAvaliacaoTecnica.contains("Avaliação Visita") && Integer.parseInt(String.valueOf(seDependeConfirmacaAvaliacaoTecnica.get(sePossuiNotificacaoAvaliacaoTecnica.indexOf("Avaliação Visita")))) == 1) {
                            // id da notificação pertinente às estrelas que o usuário deverá avaliar a notificação
                            setAbrirPopUpAvaliacao((int) notificacoesCliente.getDadosIdsNotificacoes().get(sePossuiNotificacaoAvaliacaoTecnica.indexOf("Avaliação Visita")), "Poderia avaliar o serviço do técnico?");
                        }

                        try {
                            // Verifica se há notificações não lidas para se inserir a bolinha
                            if (sePossuiNotificacoes.contains(true)) {
                                imageViewIconeFlag.setVisibility(View.VISIBLE);
                            } else {
                                imageViewIconeFlag.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                        }
                    } catch (Exception e) {
                        AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                        }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                    }
                }
            }, 0);
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
    }

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_x, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_sair) {
//            MenusAbaLateralEsquerda.botao3Pontinhos(this);
//        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_planos) {
            MenusAbaLateralEsquerda.botaoPlanos(MenuPrincipal.this);
        } else if (id == R.id.nav_suporte) {
            MenusAbaLateralEsquerda.botaoSuporte(MenuPrincipal.this);
        } else if (id == R.id.nav_servicos) {
            MenusAbaLateralEsquerda.botaoServicos(MenuPrincipal.this);
        } else if (id == R.id.nav_fale_conosco) {
            MenusAbaLateralEsquerda.botaoFaleConosco(MenuPrincipal.this);
        } else if (id == R.id.nav_sobre) {
            MenusAbaLateralEsquerda.botaoSobre(MenuPrincipal.this);
        } else if (id == R.id.nav_options) {
            MenusAbaLateralEsquerda.botaoOpcoes(MenuPrincipal.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Carrega o recyclerview com cada entidade carregada pertinente ao índice
     *
     * @author Igor Maximo
     * @date 18/04/2019
     */
    private void setCarregarRoletaPlanos() {
        recyclerViewRoletaPlanos = (RecyclerView) findViewById(R.id.recyclerviewRoleta);
        adapterRecyclerViewRoletaPlanos = new ContratoAdapter(planosLista);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewRoletaPlanos.setLayoutManager(mLayoutManager);
        recyclerViewRoletaPlanos.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRoletaPlanos.setAdapter(adapterRecyclerViewRoletaPlanos);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(MenuPrincipal.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRoletaPlanos.setLayoutManager(horizontalLayoutManagaer);
        recyclerViewRoletaPlanos.setItemAnimator(null);
        recyclerViewRoletaPlanos.setHasFixedSize(false);
        ////////////////////////////// EFEITO NUBANK INTERPOLAÇÃO ///////////////////////////////////////////////////////
        final Animation myAnimRight = AnimationUtils.loadAnimation(MenuPrincipal.this, R.anim.enter_from_right);

        try {
            new AsyncTaskCarregaContratos(MenuPrincipal.this).execute();

            try {
                // Use bounce interpolator with amplitude 0.1 and frequency 15
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 15);
                myAnimRight.setInterpolator(interpolator);
                recyclerViewRoletaPlanos.startAnimation(myAnimRight);
            } catch (Exception e) {
                AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            }

        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
    }

    /**
     * Carrega o recyclerview com os botões do menu principal
     *
     * @author Igor Maximo
     * @data 18/04/2019
     */
    public void setCarregaRoletaBotoesMenu() {
        handlerGeralSegundoPlano = new Handler();
        handlerGeralSegundoPlano.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    recyclerViewRoletaBotoesMenuPrincipal = (RecyclerView) findViewById(R.id.recyclerviewRoletaBotoesMenu);
                    adapterRecyclerViewRoletaBotoesMenuPrincipal = new BotoesMenuPrincipalAdapter(botoesLista);
                    recyclerViewRoletaBotoesMenuPrincipal.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerViewRoletaBotoesMenuPrincipal.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewRoletaBotoesMenuPrincipal.setAdapter(adapterRecyclerViewRoletaBotoesMenuPrincipal);
                    recyclerViewRoletaBotoesMenuPrincipal.setLayoutManager(new LinearLayoutManager(MenuPrincipal.this, LinearLayoutManager.HORIZONTAL, false));
                    ////////////////////////////// EFEITO NUBANK INTERPOLAÇÃO ///////////////////////////////////////////////////////
                    try {
                        final Animation myAnim = AnimationUtils.loadAnimation(MenuPrincipal.this, R.anim.enter_from_right);
                        // Use bounce interpolator with amplitude 0.1 and frequency 15
                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 15);
                        myAnim.setInterpolator(interpolator);
                        recyclerViewRoletaBotoesMenuPrincipal.startAnimation(myAnim);
                    } catch (Exception e) {
                        AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                        }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                    }
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    BotoesMenuPrincipal botao = null;
                    // Pagar
                    botao = new BotoesMenuPrincipal(R.drawable.ic_extrato_financeiro_branco, 0, "Pagar", MenuPrincipal.this, new Intent(MenuPrincipal.this, MenuSegundaViaContrato.class));
                    botoesLista.add(botao);
                    // 2ª via
                    botao = new BotoesMenuPrincipal(R.drawable.ic_segundavia_branca, 0, "2ª Via\nPagamentos", MenuPrincipal.this, new Intent(MenuPrincipal.this, MenuSegundaViaContrato.class));
                    botoesLista.add(botao);
                    // Atendimento
                    botao = new BotoesMenuPrincipal(R.drawable.ic_precisa_ajuda, 0, "Precisa de\nAjuda?", MenuPrincipal.this, new Intent(MenuPrincipal.this, MenuAtendimento.class));
                    if (new AtendimentoDAO().getSeBotaoAtendimentoHabilitado()) {
                        botoesLista.add(botao);
                    }
                    // Cartão VINDI
                    botao = new BotoesMenuPrincipal(R.drawable.ic_vindi, 0, "Cartão\nCrédito", MenuPrincipal.this, new Intent(MenuPrincipal.this, MenuRecorrenteContrato.class));
                    if (new CartaoRecorrenteVindiDAO().getSeBotaoDebitoAutomaticoHabilitado()) {
                        botoesLista.add(botao);
                    }
                    // IPTV
                    botao = new BotoesMenuPrincipal(R.drawable.ic_iptv, 0, "IPTV", MenuPrincipal.this, new Intent(MenuPrincipal.this, IPTVWebViewActivity.class));
                    if (new IPTVDAO().getSeBotaoIPTVHabilitado()) {
                        botoesLista.add(botao);
                    }
                    // Paramount/Noggin
                    botao = new BotoesMenuPrincipal(R.drawable.ic_iptv, 0, "Acesso\nParamount/Noggin", MenuPrincipal.this, new Intent(MenuPrincipal.this, MenuParamountNogginContrato.class));
                    botoesLista.add(botao);
                    // Teste de velocidade
//                    botao = new BotoesMenuPrincipal(R.drawable.ic_teste_velocidade, 0, "Teste\nVelocidade", MenuPrincipal.this, new Intent(MenuPrincipal.this, MenuTesteVelocidade.class));
//                    if (new SpeedTestDAO().retornaSeBotaoSpeedTestHabilitado()) {
//                        botoesLista.add(botao);
//                    }
                    // Botão para ativar antivírus
                    botao = new BotoesMenuPrincipal(R.drawable.ic_antivirus, 0, "Ativar\nAntivírus", MenuPrincipal.this, new Intent(MenuPrincipal.this, MenuAntivirus.class));
                    botoesLista.add(botao);
                    // Botão para indicar app para um amigo
                    botao = new BotoesMenuPrincipal(R.drawable.ic_amigo, 0, "Indicar\nAmigo", MenuPrincipal.this, null);
                    botoesLista.add(botao);
                    // Fale Conosco
                    botao = new BotoesMenuPrincipal(R.drawable.ic_fone, 0, "Fale\nConosco", MenuPrincipal.this, new Intent(MenuPrincipal.this, MenuFaleConosco.class));
                    botoesLista.add(botao);
                    // Meus Dados
                    botao = new BotoesMenuPrincipal(R.drawable.ic_conta, 0, "Meus Dados", MenuPrincipal.this, new Intent(MenuPrincipal.this, MenuMinhaConta.class));
                    if (!CLIENTE_POTENCIAL) {
                        botoesLista.add(botao);
                    }
                    // Sobre
                    botao = new BotoesMenuPrincipal(R.drawable.ic_vetor_ong, 0, "Sobre", MenuPrincipal.this, new Intent(MenuPrincipal.this, ScrollingSobre.class));
                    botoesLista.add(botao);
                    // Encerrar - truncate sqlite
                    botao = new BotoesMenuPrincipal(R.drawable.ic_encerrar_sessao, 0, "Encerrar\nSessão", MenuPrincipal.this, new Intent(MenuPrincipal.this, ScrollingSobre.class));
                    botoesLista.add(botao);
                    // Carrega
                    adapterRecyclerViewRoletaBotoesMenuPrincipal.notifyDataSetChanged();
                } catch (Exception e) {
                    AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                    }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                }
            }
        }, 0);
    }

    /**
     * Exibe o popup de avaliação de serviços do técnico
     *
     * @author Igor Maximo
     * @data 13/08/2020
     */
    public void setAbrirPopUpAvaliacao(final int idNotificacao, String texto) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MenuPrincipal.this);
        View view = getLayoutInflater().inflate(R.layout.card_avaliacao, null);

        // Texto de avaliação
        TextView textView = (TextView) view.findViewById(R.id.textViewPOPUPTextoAvaliacao);
        textView.setText(texto);
        builder.setView(view);
        builder.setCancelable(false);
        final RatingBar ratingBarAvaliartecnico = (RatingBar) view.findViewById(R.id.ratingBarAvaliartecnico);
        ratingBarAvaliartecnico.setStepSize(1.0f);

        // BOTÃO - OK, AVALIAR
        view.findViewById(R.id.relativeLayoutbuttonOKAvaliarTecnico).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show().dismiss();
                // Grava avaliação
                if (notificacaoDAO.setGravaVotoAvaliacaoEstrelasServicoTecnico(idNotificacao, Math.round(ratingBarAvaliartecnico.getRating()))) {
                    Toast.makeText(getApplicationContext(), "Avaliação enviada com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao enviar avaliação!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // BOTÃO - NÃO AVALIAR
        view.findViewById(R.id.relativeLayoutbuttonNaoQueroAvaliarTecnico).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show().dismiss();
                // Grava avaliação
                if (notificacaoDAO.setGravaVotoAvaliacaoEstrelasServicoTecnico(idNotificacao, Math.round(ratingBarAvaliartecnico.getRating()))) {
                    Toast.makeText(getApplicationContext(), "Obrigado!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao enviar avaliação!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Exibe tutorial na primeira vez que usuário loga no APP
     *
     * @author Igor Maximo
     * @data 23/04/2019
     */
    private void setGeraDialogTutorial() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MenuPrincipal.this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("WrongViewCast") View view = inflater.inflate(R.layout.tutorial_tela1, null);
        builder.setView(view);
        TextView linearBotaoProximo = (TextView) view.findViewById(R.id.botaoProximo1);
        linearBotaoProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show().dismiss();
                // SQLITE
                try {
                    sqLiteGeraTabelaTutorial.atualizaTutorial("update tutorial set lido = 'S'");
                } catch (Exception e) {
                    AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                    }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                }
            }
        });
    }

    /**
     * Exibe dialog que tempo expirou
     *
     * @author Igor Maximo
     * @data 23/04/2019
     */
    public void geraDialogTempoExpirado() {
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("WrongViewCast") View view = inflater.inflate(R.layout.tempo_expirado, null);
        new AlertDialog.Builder(MenuPrincipal.this).setView(view);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            usuario = new Usuario();
            new AutenticacaoDAO().registraLOGDeslogarUsuario(usuario);
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
    }


    @Override
    protected void onUserLeaveHint() {
        Log.v("", "Home Button Pressed");
        super.onUserLeaveHint();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                moveTaskToBack(true);
                return true;
            }
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
        return super.onKeyDown(keyCode, event);

    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////// ASYNCTASK PARA PROCESSAMENTO DE PAGAMENTOS /////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////////
//                            ASYNCTASK PARA CARREGAMENTO DOS TÍTULOS                             //
////////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskCarregaContratos extends AsyncTask<Object[], Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Context mContext = null;

        public AsyncTaskCarregaContratos(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Object[]... obj) {
            contrato = null;
            while (contrato == null) {
                ThreadRunningOperation();
                try {
                    contrato = new ContratoRoletaDAO().getTodosContratosCliente();
                    //erro que as vezes acontece Network is unreachable (SEM INTERNET)
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
                ////////////////////////////// EFEITO NUBANK ROLAGEM/////////////////////////////////////////////////////////////
                LinearSnapHelper snapHelper = new SnapHelperOneByOne();
                snapHelper.attachToRecyclerView(recyclerViewRoletaPlanos);
                new LinearSmoothScroller(recyclerViewRoletaPlanos.getContext()) {
                    @Override
                    protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_ANY;
                    }

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return 120f / displayMetrics.densityDpi;
                    }
                };
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                ContratoRoleta plano = null;
                ContratoAdapter.ctx = MenuPrincipal.this;

                if (contrato.getDadosCodContrato().size() != 0) {
                    // Add primeiro index
                    plano = new ContratoRoleta(
                            String.valueOf(contrato.getDadosCodigoCliente().get(0)),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            new Intent(MenuPrincipal.this, AdesoesWebViewActivity.class),
                            MenuPrincipal.this, null, null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    );
                    planosLista.add(plano);
                    // Planos por index
                    for (int i = 0; i < contrato.getDadosCodContrato().size(); i++) {
                        plano = new ContratoRoleta(
                                String.valueOf(contrato.getDadosCodigoCliente().get(i)),
                                String.valueOf(contrato.getDadosCodContrato().get(i)),
                                String.valueOf(contrato.getDadosNomeDoPlano().get(i)),
                                String.valueOf(contrato.getDadosStatusPlano().get(i)),
                                Ferramentas.setArredondaValorMoedaReal((String) contrato.getDadosValorFinal().get(i)),
                                String.valueOf(contrato.getDadosEnderecoInstalacao().get(i)),
                                String.valueOf(contrato.getDadosVencimentoPlano().get(i)),
                                new Intent(MenuPrincipal.this, MenuDadosPlano.class),
                                MenuPrincipal.this,
                                String.valueOf(contrato.getDadosCodProd().get(i)),
                                String.valueOf(contrato.getDadosCodContratoItem().get(i)),
                                String.valueOf(contrato.getDadosCodClieCartaoVindi().get(i)),
                                String.valueOf(contrato.getDadosEnderecoInstalacaoPuro().get(i)),
                                String.valueOf(contrato.getDadosNumeroInstalacaoPuro().get(i)),
                                String.valueOf(contrato.getDadosContratoEmpresaCNPJ().get(i)),
                                String.valueOf(contrato.getDadosContratoEmpresaNome().get(i)),
                                String.valueOf(contrato.getDadosCodAppExterno().get(i)),
                                String.valueOf(contrato.getDadosUsuarioApp().get(i)),
                                String.valueOf(contrato.getDadosSenhaApp().get(i))
                        );
                        planosLista.add(plano);
                    }
                }
                // Cas não tenha retornado nenhum contrato (só se for bug)
                if (contrato.getDadosCodContrato().size() == 0) {
                    // Add último index
                    plano = new ContratoRoleta(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            new Intent(MenuPrincipal.this, AdesoesWebViewActivity.class),
                            MenuPrincipal.this,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    );
                    planosLista.add(plano);
                }
                adapterRecyclerViewRoletaPlanos.notifyDataSetChanged();
                // Para fazer o efeitos da bolinhas que indicam o index da recyclerview
                recyclerViewRoletaPlanos.addItemDecoration(new CirclePagerIndicatorDecoration());
            } catch (Exception e) {
                System.err.println("REC===" + e.getMessage());
            }
            adapterRecyclerViewRoletaPlanos.notifyDataSetChanged();
            // Carrega roleta de botões dos menus
            setCarregaRoletaBotoesMenu();
            // Remove barra de progresso
            mProgress.dismiss();
        }

        private void ThreadRunningOperation() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
        }
    }

    /**
     * Abre a central de notificações
     *
     * @author Igor Maximo
     * @date 04/01/2021
     */
    private class AsyncTaskCentralNotificacoes extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog mProgress = null;

        @Override
        protected Boolean doInBackground(String... strings) {
            ThreadRunningOperation();

            try {
                startActivity(new Intent(MenuPrincipal.this, MenuCentralNotificacoes.class));
            } catch (Exception e) {
                AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            mProgress.setProgress(progress[0]);
        }

        @Override
        protected void onPreExecute() {
            this.mProgress = ProgressDialog.show(MenuPrincipal.this, null, "Processando...", true);
            this.mProgress.setIndeterminate(false);
            this.mProgress.setCancelable(false);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgress.dismiss();
        }

        private void ThreadRunningOperation() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            }
        }
    }

    /**
     * Finaliza o app em caso de garbage collector
     * ter feito sua coleta maldita
     *
     * @author Igor Maximo
     * @date 05/03/2021
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}