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
import com.rede.App.View.Adapters.ContratoParamountNogginAdapter;
import com.rede.App.View.DAO.AppLogErroDAO;
import com.rede.App.View.DAO.ContratoRoletaDAO;
import com.rede.App.View.DAO.UsuarioDAO;
import com.rede.App.View.JavaBeans.ContratoRoleta;
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
 * Exibe os planos que tem direito a usar
 * o IPTV do noggin
 *
 * @author      Igor Maximo
 * @date        25/08/2021
 */
public class MenuParamountNogginContrato extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected Context CTX = MenuParamountNogginContrato.this;
    // Entidade usuário logado
    final Usuario usuario = new Usuario();
    private ContratoParamountNogginAdapter adapterRecyclerViewRoletaPlanos;
    ContratoRoleta contrato;
    private ContratoRoletaDAO roletaPlanosDAO = new ContratoRoletaDAO();
    // Recycler da roleta de planos
    public RecyclerView recyclerViewRoletaPlanos;
    private List<ContratoRoleta> planosLista = new ArrayList<>();

    @SuppressLint({"SetTextI18n", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_antivirus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Botão voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reproduz o efeito de vibrar o celular
                ControladorInterface.setClickBotao(getApplicationContext());
                try {
                    if (new Internet(CTX).verificaConexaoInternet()) {
                        startActivityForResult(new Intent(MenuParamountNogginContrato.this, MenuPrincipal.class), 0);
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
        recyclerViewRoletaPlanos = (RecyclerView) findViewById(R.id.recyclerviewContratosAntivirus);
        adapterRecyclerViewRoletaPlanos = new ContratoParamountNogginAdapter(planosLista);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewRoletaPlanos.setLayoutManager(mLayoutManager);
        recyclerViewRoletaPlanos.setAdapter(adapterRecyclerViewRoletaPlanos);
        recyclerViewRoletaPlanos.setItemAnimator(null);
        recyclerViewRoletaPlanos.setHasFixedSize(false);
        ////////////////////////////// EFEITO NUBANK INTERPOLAÇÃO ///////////////////////////////////////////////////////
        try {
            new MenuParamountNogginContrato.AsyncTaskCarregaContratos(MenuParamountNogginContrato.this).execute();
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
            contrato = null;
            while (contrato == null) {
                ThreadRunningOperation();
                try {
                    new UsuarioDAO().getCarregaDadosUsuario();
                    contrato = roletaPlanosDAO.getTodosContratosCliente();
                    //erro que as vezes acontece Network is unreachable
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
                final Animation myAnim = AnimationUtils.loadAnimation(MenuParamountNogginContrato.this, R.anim.up_from_bottom);
                // Use bounce interpolator with amplitude 0.1 and frequency 15
                myAnim.setInterpolator(new MyBounceInterpolator(0.1, 15));
                recyclerViewRoletaPlanos.startAnimation(myAnim);
                textViewMsgLista.startAnimation(myAnim);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                ContratoRoleta plano = null;
                ContratoParamountNogginAdapter.ctx = MenuParamountNogginContrato.this;

                if (contrato.getDadosCodContrato().size() > 0) {
                    // Esconde as msgs
                    frameLayoutMsgLista.setVisibility(View.GONE);
                    textViewMsgLista.setText("");
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
                                new Intent(MenuParamountNogginContrato.this, MenuParamountNoggin.class),
                                MenuParamountNogginContrato.this,
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
                        // Apenas planos que estão enquadrados na regra de solicitação de antivírus
                        if (contrato.getDadosStatusPlano().get(i).equals("Ativo")) {
                            planosLista.add(plano);
                        }
                    }
                } else {
                    // Exibe msg caso não existe planos disponíveis/suportados
                    frameLayoutMsgLista.setVisibility(View.VISIBLE);
                    textViewMsgLista.setText("Não há planos disponíveis para IPTV.");
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