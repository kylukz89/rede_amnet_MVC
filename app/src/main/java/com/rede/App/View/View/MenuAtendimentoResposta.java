package com.rede.App.View.View;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.rede.App.View.Adapters.AtendimentoRespostaAdapter;
import com.rede.App.View.DAO.AppLogErroDAO;
import com.rede.App.View.DAO.AtendimentoDAO;
import com.rede.App.View.JavaBeans.ListaAtendimentoResposta;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.ToolBox.Animatoo;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.ToolBox.Internet;
import com.rede.App.View.ToolBox.LifeCycleObserver;
import com.rede.ncarede.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Exibe opções de resposta para um novo atendimento
 *
 * @author Igor Maximo
 * @criado 19/02/2021
 */
public class MenuAtendimentoResposta extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String ENDERECO_PLANO = null;
    public static String ENDERECO_NUMERO_PLANO = null;
    protected Context ctx = MenuAtendimentoResposta.this;

    Usuario usuario = new Usuario();
    public static String FK_CATEGORIA;
    public static String NOME_CATEGORIA_ESCOLHIDA;
    public static Bitmap CATEGORIA_ICONE_ESCOLHIDA;

    public static String COD_CONTRATO_ESCOLHIDO;
    public static String COD_CONTRATO_ITEM_ESCOLHIDO;

    public static Context CTX;

    @SuppressLint({"SetTextI18n", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_atendimento_resposta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.CTX = MenuAtendimentoResposta.this;
        // Botão voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Reproduz o efeito de vibrar o celular
                    ControladorInterface.setClickBotao(getApplicationContext());
                    if (new Internet(MenuPrincipal.CTX).verificaConexaoInternet()) {
                        startActivityForResult(new Intent(MenuAtendimentoResposta.this, MenuAtendimento.class), 0);
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
        toolbar.setElevation(0f);

        TextView textViewCategoriaNomeEscolhida = (TextView) findViewById(R.id.textViewCategoriaNomeEscolhida);
        textViewCategoriaNomeEscolhida.setText(NOME_CATEGORIA_ESCOLHIDA);

        ImageView imageViewCategoriaIcone = (ImageView) findViewById(R.id.imageViewCategoriaIcone);
        imageViewCategoriaIcone.setImageBitmap(CATEGORIA_ICONE_ESCOLHIDA);

        // Carrega lista de opçoes de atendimento
        setCarregarListaRespostaOpcoesAtendimento();
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
     * Carrega o recyclerview com todas as respostas por categoria disponíveis
     * de tipos de atendimento para o cliente
     *
     * @author Igor Maximo
     * @date 30/04/2021
     */
    @SuppressLint("SetTextI18n")
    private void setCarregarListaRespostaOpcoesAtendimento() {
        try {
            new MenuAtendimentoResposta.AsyncTaskCarregaListaOpcoesAtendimentosRespostas(MenuAtendimentoResposta.this).execute();
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
    }


    private RecyclerView.Adapter adapter;

    /**
     * Gerador Principal de ReyclerView
     *
     * @param recyclerView
     * @param recyclerviewAdapter
     * @param qtdColunasReyclerView
     * @param seUsaOrientacaoLayout
     * @param orientacao
     * @usage Roleta de Botões do Menu
     * @usage Lista de Grupos de Produtos
     * @usage Roleta dos Produtos de um determinado grupo
     * @usage Lista dos itens no carrinho (COMANDA)
     * @author Igor Maximo <igormaximo_1989@hotmail.com>
     * @date 26/04/2020
     */
    @SuppressLint("ClickableViewAccessibility")
    public RecyclerView setRecyclerView(final RecyclerView recyclerView, RecyclerView.Adapter recyclerviewAdapter, int qtdColunasReyclerView, boolean seUsaOrientacaoLayout, int orientacao) {
        adapter = recyclerviewAdapter;
        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new GridLayoutManager(this, qtdColunasReyclerView);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, qtdColunasReyclerView));
        if (seUsaOrientacaoLayout) {
            LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, orientacao, false);
            recyclerView.setLayoutManager(horizontalLayoutManagaer);
        }
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recyclerView.setItemAnimator(null);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        recyclerView.setNestedScrollingEnabled(false);

        final RecyclerView recyclerViewAddOnPreDrawListener = recyclerView;
        recyclerViewAddOnPreDrawListener.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                recyclerViewAddOnPreDrawListener.getViewTreeObserver().removeOnPreDrawListener(this);
                for (int i = 0; i < recyclerViewAddOnPreDrawListener.getChildCount(); i++) {
                    View v = recyclerViewAddOnPreDrawListener.getChildAt(i);
                    v.setAlpha(0.0f);
                    v.animate().alpha(1.5f)
                            .setDuration(1000)
                            .setStartDelay(i * 150)
                            .start();
                }
                return true;
            }
        });
        recyclerviewAdapter.notifyDataSetChanged();
        return recyclerView;
    }


    /**
     * Abre dialog para o cliente confirmar
     * se o mesmo abrir mesmo o atendimento
     *
     * @author Igor Maximo
     * @date 06/05/2021
     */
    public void setDialogConfirmarNovoAtendimento(final String fkCategoria, final String fkCategoriaResposta, final String codContrato, final String codContratoItem, final String descricao, final boolean seRespostaLivre, final boolean tipoDeAtendimento) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("WrongViewCast") View view = inflater.inflate(R.layout.fragment_novo_atendimento, null);

        builder.setView(view);
        final AlertDialog show = builder.show();
        final TextView textViewMsgAntiVirus = (TextView) view.findViewById(R.id.textViewMsgAntiVirus);
        textViewMsgAntiVirus.setText(Html.fromHtml("<span style='color: #555555; text-shadow: 5px 5px #333333;'>" +
                "Você está prestes a solicitar um atendimento com a seguinte resposta (<label style='color: #000000; text-shadow: 5px 5px #000000;'><b>" + descricao.replace(".", "") + "</b></label>). " +
                "Tem certeza que deseja continuar?</span>"));

        final TextView textViewMsgAntiVirusBotaoNegar = (TextView) view.findViewById(R.id.textViewMsgAntiVirusBotaoNegar);
        final TextView textViewMsgAntiVirusBotaoConfirmar = (TextView) view.findViewById(R.id.textViewMsgAntiVirusBotaoConfirmar);

        // Botão negar
        textViewMsgAntiVirusBotaoNegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show.dismiss();
            }
        });
        // Botão confirmar
        textViewMsgAntiVirusBotaoConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Valida se é uma resposta livre
                if (seRespostaLivre) {
                    setDialogRespostaLivre(fkCategoria, fkCategoriaResposta, codContrato, codContratoItem, descricao, seRespostaLivre, tipoDeAtendimento);
                } else {
                    setDialogConfirmarDados(fkCategoria, fkCategoriaResposta, codContrato, codContratoItem, descricao, seRespostaLivre, tipoDeAtendimento);
                }
                show.dismiss();
            }
        });
    }

    /**
     * Abre dialog para o cliente confirmar
     * se o mesmo abrir mesmo o atendimento
     * com uma resposta livre
     *
     * @author Igor Maximo
     * @date 06/05/2021
     */
    private void setDialogRespostaLivre(final String fkCategoria, final String fkCategoriaResposta, final String codContrato, final String codContratoItem, final String descricao, final boolean seRespostaLivre, final boolean tipoDeAtendimento) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("WrongViewCast") View view = inflater.inflate(R.layout.fragment_novo_atendimento_resposta_livre, null);

        builder.setView(view);
        final AlertDialog show = builder.show();
        final TextView textViewMsgAntiVirus = (TextView) view.findViewById(R.id.textViewMsgAntiVirus);
        textViewMsgAntiVirus.setText(Html.fromHtml("<span style='color: #555555; text-shadow: 5px 5px #333333;'>" +
                "Você está prestes a solicitar um atendimento, por favor, <label style='color: #000000; text-shadow: 5px 5px #000000;'><b>escreva com as suas palavras</b> qual problema está ocorrendo.</label> " +
                "Tem certeza que deseja continuar?</span>"));

        final TextView textViewMsgAntiVirusBotaoNegar = (TextView) view.findViewById(R.id.textViewMsgAntiVirusBotaoNegar);
        final TextView textViewMsgAntiVirusBotaoConfirmar = (TextView) view.findViewById(R.id.textViewMsgAntiVirusBotaoConfirmar);
        final EditText textAreaRespostaLIvre = (EditText) view.findViewById(R.id.textAreaRespostaLIvre);


        // Botão negar
        textViewMsgAntiVirusBotaoNegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show.dismiss();
            }
        });
        // Botão confirmar
        textViewMsgAntiVirusBotaoConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String respostaLivre = textAreaRespostaLIvre.getText().toString();
                if (respostaLivre.length() > 10 && respostaLivre.length() < 1000) {
                    // Abre novo atendimento
                    setDialogConfirmarDados(fkCategoria, fkCategoriaResposta, codContrato, codContratoItem, respostaLivre, seRespostaLivre, tipoDeAtendimento);

                    show.dismiss();
                } else {
                    // Menor que 10
                    if (respostaLivre.length() < 10) {
                        textAreaRespostaLIvre.setError("Relate melhor o problema.");
                        textAreaRespostaLIvre.setBackgroundResource(R.drawable.error_background);
                    }
                    // Maior que 1000
                    if (respostaLivre.length() > 500) {
                        textAreaRespostaLIvre.setError("Use no máximo 500 caracteres.");
                        textAreaRespostaLIvre.setBackgroundResource(R.drawable.error_background);
                    }
                }
            }
        });
    }

    /**
     * Abre dialog para o cliente confirmar
     * os dados cadastrais antes de abrir um atendimento
     *
     * @author Igor Maximo
     * @date 15/05/2021
     */
    private void setDialogConfirmarDados(final String fkCategoria, final String fkCategoriaResposta, final String codContrato, final String codContratoItem, final String descricao, final boolean seRespostaLivre, final boolean tipoDeAtendimento) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("WrongViewCast") View view = inflater.inflate(R.layout.fragment_novo_atendimento_resposta_confirmar_dados, null);

        builder.setView(view);
        final AlertDialog show = builder.show();


        final TextView textViewMsgAntiVirusBotaoNegar = (TextView) view.findViewById(R.id.textViewMsgAntiVirusBotaoNegar);
        final TextView textViewMsgAntiVirusBotaoConfirmar = (TextView) view.findViewById(R.id.textViewMsgAntiVirusBotaoConfirmar);


        final EditText campoEnderecoConfirmar = (EditText) view.findViewById(R.id.campoEnderecoConfirmar);
        final EditText campoNumeroConfirmar = (EditText) view.findViewById(R.id.campoNumeroConfirmar);
        final EditText campoFoneConfirmar = (EditText) view.findViewById(R.id.campoFoneConfirmar);
        final EditText campoCelularConfirmar = (EditText) view.findViewById(R.id.campoCelularConfirmar);
        final EditText campoEmailConfirmar = (EditText) view.findViewById(R.id.campoEmailConfirmar);
        final EditText campoCelularFacil = (EditText) view.findViewById(R.id.campoCelularFacil);


        campoEnderecoConfirmar.setText(MenuAtendimentoResposta.ENDERECO_PLANO);
        campoNumeroConfirmar.setText(MenuAtendimentoResposta.ENDERECO_NUMERO_PLANO);
        campoFoneConfirmar.setText(usuario.getFone());
        campoCelularConfirmar.setText(usuario.getCelular());
        campoEmailConfirmar.setText(usuario.getEmail());

        campoCelularFacil.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                try {
                    if (hasFocus) {
                        campoCelularFacil.setText("");
                    }
                    if (!hasFocus) {
                        campoCelularFacil.setText("(" + campoCelularConfirmar.getText().toString().replace("(", "").replace(")", "").replace(" ", "").replace("-", "").substring(0, 2) + ") " + campoCelularFacil.getText().toString().replace("(", "").replace(")", "").replace(" ", "").replace("-", "").substring(2, 7) + "-" + campoCelularFacil.getText().toString().replace("(", "").replace(")", "").replace(" ", "").replace("-", "").substring(7));
                    }
                } catch (Exception e) {

                }
            }
        });


        campoCelularFacil.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                try {
                    if (campoCelularFacil.getText().toString().length() == 11) {
                        campoCelularFacil.setText("(" + campoCelularConfirmar.getText().toString().replace("(", "").replace(")", "").replace(" ", "").replace("-", "").substring(0, 2) + ") " + campoCelularFacil.getText().toString().replace("(", "").replace(")", "").replace(" ", "").replace("-", "").substring(2, 7) + "-" + campoCelularFacil.getText().toString().replace("(", "").replace(")", "").replace(" ", "").replace("-", "").substring(7));
                    }
                } catch (Exception e) {

                }
                return false;
            }
        });


        // Botão negar
        textViewMsgAntiVirusBotaoNegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show.dismiss();
            }
        });
        // Botão confirmar
        textViewMsgAntiVirusBotaoConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (campoCelularFacil.getText().toString().length() == 11) {
                    campoCelularFacil.setText("(" + campoCelularConfirmar.getText().toString().replace("(", "").replace(")", "").replace(" ", "").replace("-", "").substring(0, 2) + ") " + campoCelularFacil.getText().toString().replace("(", "").replace(")", "").replace(" ", "").replace("-", "").substring(2, 7) + "-" + campoCelularFacil.getText().toString().replace("(", "").replace(")", "").replace(" ", "").replace("-", "").substring(7));
                }
                // ENDEREÇO
                if (campoEnderecoConfirmar.getText().toString().length() < 15) {
                    campoEnderecoConfirmar.setError("Endereço inválido!");
                    return;
                }
                // nº
                if (campoNumeroConfirmar.getText().toString().length() < 1 || Integer.parseInt(campoNumeroConfirmar.getText().toString()) < 0) {
                    campoNumeroConfirmar.setError("nº inválido.");
                    return;
                }
                // Celular
                if (campoCelularConfirmar.getText().toString().length() < 10) {
                    campoCelularConfirmar.setError("Celular inválido.");
                    return;
                }
                // Celular Fácil
                if (campoCelularFacil.getText().toString().length() < 15) {
                    campoCelularFacil.setError("Informe um celular mais fácil para contato.");
                    return;
                }
                // E-mail
                if (campoEmailConfirmar.getText().toString().length() < 8 || !campoEmailConfirmar.getText().toString().contains("@")) {
                    campoEmailConfirmar.setError("E-mail inválido!");
                    return;
                }

                show.dismiss();

                // Abre novo atendimento
                new AsyncTaskSetAbrirNovoAtendimento().execute(new Object[]{
                        usuario.getCpfCnpj(),
                        codContrato,
                        codContratoItem,
                        fkCategoria,
                        fkCategoriaResposta,
                        descricao,
                        tipoDeAtendimento,
                        new String[]{
                                (campoEnderecoConfirmar.getText().toString()),
                                (campoNumeroConfirmar.getText().toString()),
                                (campoFoneConfirmar.getText().toString()),
                                (campoCelularConfirmar.getText().toString()),
                                (campoEmailConfirmar.getText().toString()),
                                (campoCelularFacil.getText().toString())
                        }
                });

            }
        });
    }

    /**
     * AsyncTask para abrir novo atendimento
     * de suporte ou solicitação
     *
     * @author Igor Maximo
     * @date 03/05/2021
     */
    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskSetAbrirNovoAtendimento extends AsyncTask<Object[], Integer, Boolean> {
        private ProgressDialog mProgress = null;

        @Override
        protected Boolean doInBackground(final Object[]... dados) {
            ThreadRunningOperation();
            // Chama API da LivingSafe
            try {
                final Object[] retorno = new AtendimentoDAO().setAbrirAtendimento(
                        dados[0][0].toString(),
                        dados[0][1].toString(),
                        dados[0][2].toString(),
                        dados[0][3].toString(),
                        dados[0][4].toString(),
                        dados[0][5].toString(),
                        Boolean.parseBoolean(dados[0][6].toString()),
                        (String[]) dados[0][7]
                );

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuAtendimentoResposta.this);
                        final AlertDialog show = builder.show();
                        ////// AVISO //////
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuAtendimentoResposta.this);
                        // set title
                        alertDialogBuilder.setTitle("AVISO!");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage(String.valueOf(retorno[1]))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int idD) {
                                        // Redireciona para a activty principal
                                        startActivity(new Intent(MenuAtendimentoResposta.this, MenuPrincipal.class));
                                        show.dismiss();
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        //////////////
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            mProgress.setProgress(progress[0]);
        }

        @Override
        protected void onPreExecute() {
            this.mProgress = ProgressDialog.show(MenuAtendimentoResposta.this, null, "Processando...", true);
            this.mProgress.setIndeterminate(false);
            this.mProgress.setCancelable(false);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(Boolean result) {
            mProgress.dismiss();
        }

        private void ThreadRunningOperation() {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
    }


    List<ListaAtendimentoResposta> listaOpcoesAtendimentoResposta = new ArrayList<>();
    ListaAtendimentoResposta opcoesAtendimentoResposta;

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //                            ASYNCTASK PARA CARREGAMENTO DOS TÍTULOS                             //
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskCarregaListaOpcoesAtendimentosRespostas extends AsyncTask<Void, Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Context mContext = null;


        public AsyncTaskCarregaListaOpcoesAtendimentosRespostas(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... obj) {
            opcoesAtendimentoResposta = null;
            while (opcoesAtendimentoResposta == null) {
                ThreadRunningOperation();
                try {
                    opcoesAtendimentoResposta = new AtendimentoDAO().getListaRespostasOpcoesAtendimento(FK_CATEGORIA);
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
            for (int i = 0; i < opcoesAtendimentoResposta.getDadosFkCategoriaResposta().size(); i++) {
                listaOpcoesAtendimentoResposta.add(new ListaAtendimentoResposta(
                        opcoesAtendimentoResposta.getDadosFkCategoria().get(i),
                        opcoesAtendimentoResposta.getDadosFkCategoriaResposta().get(i),
                        opcoesAtendimentoResposta.getDadosResposta().get(i),
                        opcoesAtendimentoResposta.getDadosSeRespostaLivre().get(i),
                        MenuAtendimentoResposta.this
                ));
            }

            // Carrega RecyclerView com as opções da empresa
            setRecyclerView((RecyclerView) findViewById(R.id.recyclerViewListaTiposAtendimentosRespostas), new AtendimentoRespostaAdapter(listaOpcoesAtendimentoResposta), 1, false, 1);

            mProgress.dismiss();
        }

        private void ThreadRunningOperation() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
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
}