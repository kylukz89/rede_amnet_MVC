package com.rede.App.View.View;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.rede.App.View.Adapters.CartaoRecorrenteAdapter;
import com.rede.App.View.Adapters.ContratoRecorrenteAdapter;
import com.rede.App.View.DAO.AppLogErroDAO;
import com.rede.App.View.JavaBeans.CartaoRecorrente;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.ToolBox.Animatoo;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.ToolBox.Internet;
import com.rede.App.View.ToolBox.LifeCycleObserver;
import com.rede.App.View.ToolBox.MyBounceInterpolator;
import com.rede.ncarede.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Exibe a lista de contratos para alteração na vindi
 *
 * @author Igor Maximo
 * @date 31/05/2021
 */
public class MenuRecorrenteCartaoFormEditor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String COD_CONTRATO_ESCOLHIDO;
    public static String COD_CODCLIE_CARTAO;

    protected Context CTX = MenuRecorrenteCartaoFormEditor.this;
    // Entidade usuário logado
    final Usuario usuario = new Usuario();

    private CartaoRecorrenteAdapter adapterRecyclerViewRoletaPlanos;
    // Recycler da roleta de planos
    public RecyclerView recyclerViewRoletaPlanos;
    private List<CartaoRecorrente> cartaoLista = new ArrayList<>();


    protected Button buttonBotaoCadastrarNovoCartaoVindi;
    protected boolean SE_PODE_CADASTRAR_NOVO_CARTAO = false;


    @SuppressLint({"SetTextI18n", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_cartao_recorrente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Botão voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Reproduz o efeito de vibrar o celular
                    ControladorInterface.setClickBotao(getApplicationContext());
                    if (new Internet(CTX).verificaConexaoInternet()) {
                        startActivityForResult(new Intent(MenuRecorrenteCartaoFormEditor.this, MenuPrincipal.class), 0);
                        finish();
                    } else {
                        Toast.makeText(CTX, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
                    }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
                }
            }
        });


        setCarregarRoletaPlanos();

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
     * Carrega o recyclerview com cada entidade carregada pertinente ao índice
     *
     * @author Igor Maximo
     * @data 18/04/2019
     */
    @SuppressLint("SetTextI18n")
    private void setCarregarRoletaPlanos() {
        recyclerViewRoletaPlanos = (RecyclerView) findViewById(R.id.recyclerviewCartaoRecorrente);
        adapterRecyclerViewRoletaPlanos = new CartaoRecorrenteAdapter(cartaoLista);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewRoletaPlanos.setLayoutManager(mLayoutManager);
        recyclerViewRoletaPlanos.setAdapter(adapterRecyclerViewRoletaPlanos);
        recyclerViewRoletaPlanos.setItemAnimator(null);
        recyclerViewRoletaPlanos.setHasFixedSize(false);
        ////////////////////////////// EFEITO NUBANK INTERPOLAÇÃO ///////////////////////////////////////////////////////
        try {
            new MenuRecorrenteCartaoFormEditor.AsyncTaskCarregaContratos(MenuRecorrenteCartaoFormEditor.this).execute();
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
                final Animation myAnim = AnimationUtils.loadAnimation(MenuRecorrenteCartaoFormEditor.this, R.anim.up_from_bottom);
                // Use bounce interpolator with amplitude 0.1 and frequency 15
                myAnim.setInterpolator(new MyBounceInterpolator(0.1, 15));
                recyclerViewRoletaPlanos.startAnimation(myAnim);
                textViewMsgLista.startAnimation(myAnim);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                // Botão para cadastrar um novo cartão
                buttonBotaoCadastrarNovoCartaoVindi = (Button) findViewById(R.id.buttonBotaoCadastrarNovoCartaoVindi);
                // Esconde botão para se aplicar a regra se deve ou não ficar visível
//                buttonBotaoCadastrarNovoCartaoVindi.setVisibility(View.INVISIBLE);

                CartaoRecorrente cartaoRecorrente = null;
                ContratoRecorrenteAdapter.ctx = MenuRecorrenteCartaoFormEditor.this;

                if (usuario.getCartoesRecorrentes().size() > 0) {
                    // Esconde as msgs
                    frameLayoutMsgLista.setVisibility(View.GONE);
                    textViewMsgLista.setText("");
                    // Esconde botão e prova em booleano que não se pode cadastrar novos cartões
                    SE_PODE_CADASTRAR_NOVO_CARTAO = false;
                    for (int i = 0; i < usuario.getCartoesRecorrentes().size(); i++) {
                        cartaoRecorrente = new CartaoRecorrente(
                                usuario.getCartoesRecorrentes().get(i).getIdPerfil(),
                                usuario.getCpfCnpj(),
                                usuario.getCartoesRecorrentes().get(i).getCodigoCliente(),
                                usuario.getCartoesRecorrentes().get(i).getCodContrato(),
                                usuario.getCartoesRecorrentes().get(i).getCartaoNumero(),
                                usuario.getCartoesRecorrentes().get(i).getCartaoBandeira(),
                                usuario.getCartoesRecorrentes().get(i).getCartaoDataValidade(),
                                usuario.getCartoesRecorrentes().get(i).getCartaoPlataforma(),
                                usuario.getCartoesRecorrentes().get(i).getCartaoImagemBase64Bandeira(),
                                usuario.getCartoesRecorrentes().get(i).isSeContratoVinculado(),
                                usuario.getCartoesRecorrentes().get(i).getIdClienteVindi(),
                                usuario.getCartoesRecorrentes().get(i).getIdCartaoVindi(),
                                usuario.getCartoesRecorrentes().get(i).getFkEmpresaCNPJ(),
                                usuario.getCartoesRecorrentes().get(i).getContext()
                        );
                        // Exibe apenas o cartão que está cadastrado/vinculado aquele plano
                        if (MenuRecorrenteCartaoFormEditor.COD_CODCLIE_CARTAO.equals(usuario.getCartoesRecorrentes().get(i).getCodigoCliente())) {
                            cartaoLista.add(cartaoRecorrente);
                        }
                    }
                } else {
                    // Exibe botão de cadastrar novo cartão
                    buttonBotaoCadastrarNovoCartaoVindi.setVisibility(View.VISIBLE);
                    SE_PODE_CADASTRAR_NOVO_CARTAO = true;
                    // Exibe msg caso não existe planos disponíveis/suportados
                    frameLayoutMsgLista.setVisibility(View.VISIBLE);
                    textViewMsgLista.setText("Não existem cartões cadastrados.");
                }

            } catch (Exception e) {
                System.err.println(e);
            }
            adapterRecyclerViewRoletaPlanos.notifyDataSetChanged();
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
}