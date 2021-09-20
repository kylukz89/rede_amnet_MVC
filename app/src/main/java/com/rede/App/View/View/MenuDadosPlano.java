package com.rede.App.View.View;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.rede.App.View.Adapters.BotoesMenuDadosPlanoAdapter;
import com.rede.App.View.DAO.ContratoDAO;
import com.rede.App.View.JavaBeans.BotoesMenuDadosPlano;
import com.rede.App.View.JavaBeans.Contrato;
import com.rede.App.View.ToolBox.Animatoo;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.ToolBox.Internet;
import com.rede.App.View.ToolBox.LifeCycleObserver;
import com.rede.App.View.ToolBox.MenusAbaLateralEsquerda;
import com.rede.App.View.ToolBox.MyBounceInterpolator;
import com.rede.ncarede.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Exibe as informações cadastrais do plano do usuário no Integrator
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 * @editado 12/03/2019
 */
public class MenuDadosPlano extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final Contrato plano = new Contrato();
    ContratoDAO pldao = new ContratoDAO();
    public static int idRoleta = 0;

    RecyclerView recyclerviewRoletaBotoesDadosPlano;
    private List<BotoesMenuDadosPlano> botoesLista = new ArrayList<>();
    private BotoesMenuDadosPlanoAdapter mAdapter;

    public static Context ctx;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dados_plano);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ctx = MenuDadosPlano.this;

        // Animação de entrada
        Animatoo.animateSwipeLeft(this);

        // Botão voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reproduz o efeito de vibrar o celular
                ControladorInterface.setClickBotao(MenuDadosPlano.this);

                if (new Internet(MenuPrincipal.CTX).verificaConexaoInternet()) {
                    startActivity(new Intent(MenuDadosPlano.this, MenuPrincipal.class));
                    finish();
                } else {
                    Toast.makeText(MenuPrincipal.CTX, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Animação de transição
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        // Para controlde de tempo da sessão
        LifeCycleObserver.context = this;

        new AsyncTaskCarregaDadosContrato(MenuDadosPlano.this).execute();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        */
        // disabilita aba lateral esquerda
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_x, menu);
        return true;
    }

    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_planos) {
            MenusAbaLateralEsquerda.botaoPlanos(MenuDadosPlano.this);
        } else if (id == R.id.nav_suporte) {
            MenusAbaLateralEsquerda.botaoSuporte(MenuDadosPlano.this);
        } else if (id == R.id.nav_servicos) {
            MenusAbaLateralEsquerda.botaoServicos(MenuDadosPlano.this);
        } else if (id == R.id.nav_fale_conosco) {
            MenusAbaLateralEsquerda.botaoFaleConosco(MenuDadosPlano.this);
        } else if (id == R.id.nav_sobre) {
            MenusAbaLateralEsquerda.botaoSobre(MenuDadosPlano.this);
        } else if (id == R.id.nav_options) {
            MenusAbaLateralEsquerda.botaoOpcoes(MenuDadosPlano.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Carrega roleta de botões do menu
     *
     * @author Igor Maximo
     * date 18/04/2019
     */
    private void carregaRoletaBotoesMenu(final String codSerCli, String codCob, final String nomePlano, final String nomeEnderecoPlano) {
        recyclerviewRoletaBotoesDadosPlano = (RecyclerView) findViewById(R.id.recyclerviewRoletaBotoesDadosPlano);
        mAdapter = new BotoesMenuDadosPlanoAdapter(botoesLista);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        recyclerviewRoletaBotoesDadosPlano.setLayoutManager(mLayoutManager2);
        recyclerviewRoletaBotoesDadosPlano.setItemAnimator(new DefaultItemAnimator());
        recyclerviewRoletaBotoesDadosPlano.setAdapter(mAdapter);
        LinearLayoutManager horizontalLayoutManagaer2 = new LinearLayoutManager(MenuDadosPlano.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerviewRoletaBotoesDadosPlano.setLayoutManager(horizontalLayoutManagaer2);

        ////////////////////////////// EFEITO NUBANK INTERPOLAÇÃO ///////////////////////////////////////////////////////
        try {
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.enter_from_right);
            // Use bounce interpolator with amplitude 0.1 and frequency 15
            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 15);
            myAnim.setInterpolator(interpolator);
            recyclerviewRoletaBotoesDadosPlano.startAnimation(myAnim);
        } catch (Exception e) {
            System.err.println(e);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        BotoesMenuDadosPlano botao = null;

        botao = new BotoesMenuDadosPlano(R.drawable.ic_hab_prov, "Habilitação Prov.", MenuDadosPlano.this, new Intent(MenuDadosPlano.this, MenuHabProv.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), null, null, null, null);
        // Se o plano está suspenso por débito
        if (plano.getStatusPlano().equals("Bloqueado")) {
            botoesLista.add(botao);
        }

//        botao = new BotoesMenuDadosPlano(R.drawable.ic_extrato_financeiro_azul, "Pagar", MenuDadosPlano.this, new Intent(MenuDadosPlano.this, MenuSegundaVia.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), codSerCli, codCob, nomePlano, nomeEnderecoPlano);
//        botoesLista.add(botao);
//
//        botao = new BotoesMenuDadosPlano(R.drawable.ic_segundavia, "2ª Via\nPagamentos", MenuDadosPlano.this, new Intent(MenuDadosPlano.this, MenuSegundaVia.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), codSerCli, codCob, nomePlano, nomeEnderecoPlano);
//        botoesLista.add(botao);

        //  Se o plano suporta upgrades
//        if (pldao.retornaSeMudancaPlanoHabilitada() && pldao.retornaSePlanoPossuiDescontosOuAcrescimos(plano.getCodserCli(), usuario.getCodigo()) && vencimentoDAO.retornaSePlanoSuportaUpgradePorCodCob(plano.getCodCob()) && pldao.retornaSeCodSerdoPlanoCodSerCliAtualEFibra(plano.getCodserCli(), usuario.getCodigo())) {
//            botao = new BotoesMenuDadosPlano(R.drawable.ic_alterar_plano, "Mudança\nPlano", MenuDadosPlano.this, new Intent(MenuDadosPlano.this, EscolheModalidadePlanoActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), codSerCli, codCob, nomePlano, nomeEnderecoPlano);
//            botoesLista.add(botao);
//        }

//        botao = new BotoesMenuDadosPlano(R.drawable.ic_nf, "Notas Fiscais", MenuDadosPlano.this, new Intent(MenuDadosPlano.this, MenuNotasFiscais.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), codSerCli, codCob, nomePlano, nomeEnderecoPlano);
//        botoesLista.add(botao);
//
//        botao = new BotoesMenuDadosPlano(R.drawable.ic_extrato_financeiro, "Extrato Financ.", MenuDadosPlano.this, new Intent(MenuDadosPlano.this, MenuExtratoFinanceiro.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), codSerCli, codCob, nomePlano, nomeEnderecoPlano);
//        botoesLista.add(botao);
//
//        botao = new BotoesMenuDadosPlano(R.drawable.ic_extconexao, "Extrato Conexão", MenuDadosPlano.this, new Intent(MenuDadosPlano.this, MenuExtratoConexao.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), codSerCli, codCob, nomePlano, nomeEnderecoPlano);
//        botoesLista.add(botao);


//        botao = new BotoesMenuDadosPlano(R.drawable.ic_alterar_vencimento, "Alterar\nVencimento", MenuDadosPlano.this, new Intent(MenuDadosPlano.this, MenuAlterarVencimentoPlano.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), codSerCli, nomePlano, nomeEnderecoPlano);
//        botoesLista.add(botao);

        mAdapter.notifyDataSetChanged();
    }


    /**
     * Carrega dados do contrato
     *
     * @author Igor Maximo
     * @date 03/02/2021
     */
    private class AsyncTaskCarregaDadosContrato extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Context mContext = null;
        static private final int ITERATIONS = 1;

        public AsyncTaskCarregaDadosContrato(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            for (int i = 0; i < ITERATIONS; i++) {
                ThreadRunningOperation();
            }

            try {
                pldao.retornaPlanoUsuarioPorCodSerCli();
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
            mProgress = new ProgressDialog(mContext);
            mProgress.setMessage("Processando...");
            mProgress.setIndeterminate(false);
            mProgress.setCancelable(false);
            mProgress.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            /////////// Roleta de botoes /////////
            try {
                carregaRoletaBotoesMenu(plano.getCodserCli(), plano.getCodCob(), plano.getNomePlano(), plano.getEnderecoInstalacao());
            } catch (Exception e) {
                e.getStackTrace();
            }
            /////////////////////////////////////

            EditText editTextStatusPlano = (EditText) findViewById(R.id.editTextStatusPlano);
            EditText editTextNomePlano = (EditText) findViewById(R.id.editTextNomePlano);
            EditText editTextEnderecoInstalacao = (EditText) findViewById(R.id.editTextEnderecoInstalacao);
            EditText editTextBairroInstalacao = (EditText) findViewById(R.id.editTextBairroInstalacao);
            EditText editTextCEPInstalacao = (EditText) findViewById(R.id.editTextCEPInstalacao);
            EditText editTextCidade = (EditText) findViewById(R.id.editTextCidade);
            EditText editTextVencimento = (EditText) findViewById(R.id.editTextVencimento);
            EditText editTextValorFinal = (EditText) findViewById(R.id.editTextValorFinal);



            try {
                if (plano.getStatusPlano().equals("Ativo")) {
                    editTextStatusPlano.setText("Status: " + plano.getStatusPlano().toUpperCase());
                    editTextStatusPlano.setTextColor(Color.GREEN);
                } else {
                    editTextStatusPlano.setText("Status: " + plano.getStatusPlano().toUpperCase());
                    editTextStatusPlano.setTextColor(Color.RED);
                }
                editTextNomePlano.setText("Plano: " + plano.getNomePlano());
                editTextEnderecoInstalacao.setText("Inst.: " + plano.getEnderecoInstalacao());
                editTextBairroInstalacao.setText("Bairro Inst.: " + plano.getBairroInstalacao());
                editTextCEPInstalacao.setText("CEP Inst.: " + plano.getCepInstalacao());
                editTextCidade.setText("Cidade: " + plano.getCidade());
                editTextVencimento.setText("Vence todo dia " + plano.getVencimento() + " de cada mês.");
                editTextValorFinal.setText("Valor do Plano - R$ " + Ferramentas.setArredondaValorMoedaReal(plano.getValorFinal()));

                editTextStatusPlano.setEnabled(false);
                editTextStatusPlano.setFocusable(false);

                editTextNomePlano.setEnabled(false);
                editTextNomePlano.setFocusable(false);

                editTextEnderecoInstalacao.setEnabled(false);
                editTextEnderecoInstalacao.setFocusable(false);

                editTextBairroInstalacao.setEnabled(false);
                editTextBairroInstalacao.setFocusable(false);

                editTextCEPInstalacao.setEnabled(false);
                editTextCEPInstalacao.setFocusable(false);

                editTextCidade.setEnabled(false);
                editTextCidade.setFocusable(false);

                editTextVencimento.setEnabled(false);
                editTextVencimento.setFocusable(false);

                editTextValorFinal.setEnabled(false);
                editTextValorFinal.setFocusable(false);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Erro ao carregar os dados! " + e, Toast.LENGTH_SHORT).show();
            }

            mProgress.dismiss();
        }

        private void ThreadRunningOperation() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }
}


