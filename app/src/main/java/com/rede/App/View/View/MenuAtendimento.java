package com.rede.App.View.View;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

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
import com.rede.App.View.Adapters.AtendimentoAdapter;
import com.rede.App.View.DAO.AppLogErroDAO;
import com.rede.App.View.DAO.AtendimentoDAO;
import com.rede.App.View.JavaBeans.ListaAtendimento;
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
 * Exibe opções de atendimento
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 */
public class MenuAtendimento extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected Context ctx = MenuAtendimento.this;

    Usuario usuario = new Usuario();

    public static Context CTX;

    @SuppressLint({"SetTextI18n", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_atendimento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.CTX = MenuAtendimento.this;
        // Botão voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Reproduz o efeito de vibrar o celular
                    ControladorInterface.setClickBotao(getApplicationContext());
                    if (new Internet(MenuPrincipal.CTX).verificaConexaoInternet()) {
                        startActivityForResult(new Intent(MenuAtendimento.this, MenuPrincipal.class), 0);
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


        // Carrega lista de opçoes de atendimento
        setCarregarListaOpcoesAtendimento();


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
     * Carrega o recyclerview com todas as opções disponíveis
     * de tipos de atendimento para o cliente
     *
     * @author Igor Maximo
     * @date 30/04/2021
     */
    @SuppressLint("SetTextI18n")
    private void setCarregarListaOpcoesAtendimento() {
        try {
            new MenuAtendimento.AsyncTaskCarregaListaOpcoesAtendimentos(MenuAtendimento.this).execute();
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
    }



    /**
     * Abre dialog para informar que houve
     * um incidente referente a essa categoria escolhida
     *
     * @author      Igor Maximo
     * @date        06/05/2021
     */
    public void setDialogPerguntaSeDesejaProsseguirCasoIncidente(String msgIncidente, final Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("WrongViewCast") View view = inflater.inflate(R.layout.fragment_novo_atendimento, null);

        builder.setView(view);
        final AlertDialog show = builder.show();
        final TextView textViewMsgAntiVirus = (TextView) view.findViewById(R.id.textViewMsgAntiVirus);
        textViewMsgAntiVirus.setText(Html.fromHtml("<span style='color: #555555; text-shadow: 5px 5px #333333;'>" +
                "<label style='color: #000000; text-shadow: 5px 5px #000000;'><b>" + msgIncidente + "</b></label> " +
                "Tem certeza que deseja continuar?</span>"));

        final TextView textViewMsgAntiVirusBotaoNegar = (TextView) view.findViewById(R.id.textViewMsgAntiVirusBotaoNegar);
        final TextView textViewMsgAntiVirusBotaoConfirmar = (TextView) view.findViewById(R.id.textViewMsgAntiVirusBotaoConfirmar);

        textViewMsgAntiVirusBotaoNegar.setText("Agora não");
        textViewMsgAntiVirusBotaoConfirmar.setText("Prosseguir");

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
                // Abre com o context do MenuAtendimento
                startActivity(intent);
                show.dismiss();
            }
        });
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
//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(EmpresaActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
//             @Override
//                public void onLongItemClick(View view, int position) {
//
//                }
//           })
//        );
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


    List<ListaAtendimento> listaOpcoesAtendimento = new ArrayList<>();
    ListaAtendimento opcoesAtendimento;

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //                            ASYNCTASK PARA CARREGAMENTO DOS TÍTULOS                             //
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskCarregaListaOpcoesAtendimentos extends AsyncTask<Void, Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Context mContext = null;


        public AsyncTaskCarregaListaOpcoesAtendimentos(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... obj) {
            opcoesAtendimento = null;
            while (opcoesAtendimento == null) {
                ThreadRunningOperation();
                try {
                    opcoesAtendimento = new AtendimentoDAO().getListaOpcoesAtendimento();
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
            for (int i = 0; i < opcoesAtendimento.getDadosFkCategoria().size(); i++) {
                listaOpcoesAtendimento.add(new ListaAtendimento(
                        opcoesAtendimento.getDadosIcone().get(i),
                        opcoesAtendimento.getDadosNome().get(i),
                        opcoesAtendimento.getDadosTitulo().get(i),
                        opcoesAtendimento.getDadosSubtitulo().get(i),
                        Integer.parseInt(String.valueOf(opcoesAtendimento.getDadosFkCategoria().get(i))),
                        opcoesAtendimento.getDadosDescricao().get(i),
                        new Intent(MenuAtendimento.this, MenuAtendimentoContrato.class),
                        MenuAtendimento.this,
                        opcoesAtendimento.getDadosMsgIncidentes().get(i),
                        opcoesAtendimento.getDadosSeHouveIndicentes().get(i)
                ));
            }

            // Carrega RecyclerView com as opções da empresa
            setRecyclerView((RecyclerView) findViewById(R.id.recyclerViewListaTiposAtendimentos), new AtendimentoAdapter(listaOpcoesAtendimento), 2, false, 1);

            mProgress.dismiss();
        }

        private void ThreadRunningOperation() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
        }
    }

    public void setResposta(int fkCategoria) {
        MenuAtendimentoResposta.FK_CATEGORIA = fkCategoria + "";
        System.out.println("================> " + fkCategoria);
        startActivity(new Intent(MenuAtendimento.this, MenuAtendimentoResposta.class));
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