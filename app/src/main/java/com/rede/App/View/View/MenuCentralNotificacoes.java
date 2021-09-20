package com.rede.App.View.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.rede.App.View.Adapters.NotificacoesAdapter;
import com.rede.App.View.DAO.NotificacaoDAO;
import com.rede.App.View.JavaBeans.Notificacao;
import com.rede.App.View.ToolBox.Animatoo;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.Internet;
import com.rede.App.View.ToolBox.LifeCycleObserver;
import com.rede.App.View.ToolBox.MenusAbaLateralEsquerda;
import com.rede.App.View.ToolBox.MyBounceInterpolator;
import com.rede.ncarede.R;

import java.util.ArrayList;
import java.util.List;

/*
 * Exibe todos os alertas pertinentes ao usuário
 *
 * @author Igor Maximo
 * @criado 04/06/2019
 * @updated  23/07/2020
 */
public class MenuCentralNotificacoes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NotificacaoDAO aledao = new NotificacaoDAO();
    private RecyclerView recyclerviewNotificacoes;
    private List<Notificacao> notificacoesList = new ArrayList<>();
    private NotificacoesAdapter mAdapter;
    public static Context CONTEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_central_notificacoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CONTEXT = MenuCentralNotificacoes.this;

        // Para controlde de tempo da sessão
        LifeCycleObserver.context = this;
        // Animação de entrada
        Animatoo.animateSwipeLeft(this);
        // Botão voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Reproduz o efeito de vibrar o celular
                    ControladorInterface.setClickBotao(MenuCentralNotificacoes.this);

                    if (new Internet(MenuPrincipal.CTX).verificaConexaoInternet()) {
                        startActivityForResult(new Intent(MenuCentralNotificacoes.this, MenuPrincipal.class), 0);
                        finish();
                    } else {
                        Toast.makeText(MenuPrincipal.CTX, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        });

//////////////////////// Verifica instancia de classe ////////////////////////////////////////////////////////////////////

        try {
            // Preenche a Listview com todas as notificações do clientes
            setPreencheListaTodasNotificacoes();
        } catch (Exception e) {
            e.printStackTrace();
        }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

    /**
     * Preenche Recyclerview de todas as notificações
     *
     * @author Igor Maximo
     * @date 23/07/2020
     */
    public void setPreencheListaTodasNotificacoes() {
        notificacoesList.clear();
        recyclerviewNotificacoes = (RecyclerView) findViewById(R.id.recyclerviewNotificacoes);
        mAdapter = new NotificacoesAdapter(notificacoesList);

        mAdapter.notifyDataSetChanged(); // limpa recyclerview

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerviewNotificacoes.setLayoutManager(mLayoutManager);
        recyclerviewNotificacoes.setItemAnimator(new DefaultItemAnimator());
        recyclerviewNotificacoes.setAdapter(mAdapter);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(MenuCentralNotificacoes.this, LinearLayoutManager.VERTICAL, false);
        recyclerviewNotificacoes.setLayoutManager(horizontalLayoutManagaer);

        Notificacao alerta = null;
        Notificacao alertaAux = null;

        NotificacoesAdapter.ctx = MenuCentralNotificacoes.this;

        try {
            // Retorna todas as notificações do cliente
            alertaAux = aledao.getListaTodasNotificacoesUsuario();
            // Simples fato de abrir a janela já marca como lida as notificações
            aledao.setMarcaLido(alertaAux.getDadosIdsNotificacoes());

            try {
                ////////////////////////////// EFEITO NUBANK INTERPOLAÇÃO ///////////////////////////////////////////////////////
                final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.up_from_bottom);
                // Use bounce interpolator with amplitude 0.1 and frequency 15
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 15);
                myAnim.setInterpolator(interpolator);
                recyclerviewNotificacoes.startAnimation(myAnim);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            } catch (Exception e) {
                System.err.println(e);
            }

            for (int i = 0; i < alertaAux.getDadosNotificacoes().size(); i++) {
                alerta = new Notificacao(
                        String.valueOf(alertaAux.getDadosNotificacoes().get(i)),
                        String.valueOf(alertaAux.getDadosData().get(i)),
                        Integer.parseInt(alertaAux.getDadosIdsNotificacoes().get(i).toString()),
                        Boolean.parseBoolean(alertaAux.getDadosSeLido().get(i).toString()),
                        Integer.parseInt(String.valueOf(alertaAux.getDadosSeConfirmadoNotificacao().get(i))),
                        alertaAux.getDadosSeDependeNotificacao().get(i),
                        alertaAux.getDadosProtocolo().get(i),
                        alertaAux.getDadosTipoNotificacao().get(i),
                        MenuCentralNotificacoes.this
                );
                notificacoesList.add(alerta);
            }

            recyclerviewNotificacoes.getViewTreeObserver().addOnPreDrawListener(
                    new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            recyclerviewNotificacoes.getViewTreeObserver().removeOnPreDrawListener(this);
                            for (int i = 0; i < recyclerviewNotificacoes.getChildCount(); i++) {
                                View v = recyclerviewNotificacoes.getChildAt(i);
                                v.setAlpha(0.0f);
                                v.animate().alpha(1.0f)
                                        .setDuration(1000)
                                        .setStartDelay(i * 350)
                                        .start();
                            }
                            return true;
                        }
                    });

            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_x, menu);
        return true;
    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_sair) {
//            MenusAbaLateralEsquerda.botao3Pontinhos(this);
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_planos) {
            MenusAbaLateralEsquerda.botaoPlanos(MenuCentralNotificacoes.this);
        } else if (id == R.id.nav_suporte) {
            MenusAbaLateralEsquerda.botaoSuporte(MenuCentralNotificacoes.this);
        } else if (id == R.id.nav_servicos) {
            MenusAbaLateralEsquerda.botaoServicos(MenuCentralNotificacoes.this);
        } else if (id == R.id.nav_fale_conosco) {
            MenusAbaLateralEsquerda.botaoFaleConosco(MenuCentralNotificacoes.this);
        } else if (id == R.id.nav_sobre) {
            MenusAbaLateralEsquerda.botaoSobre(MenuCentralNotificacoes.this);
        } else if (id == R.id.nav_options) {
            MenusAbaLateralEsquerda.botaoOpcoes(MenuCentralNotificacoes.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}


