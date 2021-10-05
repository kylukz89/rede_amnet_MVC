package com.rede.App.View.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.rede.App.View.Adapters.AcessoParamountAdapter;
import com.rede.App.View.DAO.AppLogErroDAO;
import com.rede.App.View.JavaBeans.AcessoIPTV;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.ToolBox.Animatoo;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.ToolBox.Internet;
import com.rede.App.View.ToolBox.LifeCycleObserver;
import com.rede.App.View.ToolBox.MyBounceInterpolator;
import com.rede.App.View.ToolBox.VariaveisGlobais;
import com.rede.ncarede.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Exibe os usuários do IPTV
 *
 * @author Igor Maximo
 * @date 19/02/2019
 */
public class MenuParamountNoggin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected Context CTX = MenuParamountNoggin.this;
    // Entidade usuário logado
    final Usuario usuario = new Usuario();

    private AcessoParamountAdapter adapterRecyclerViewRoletaPlanos;
    // Recycler da roleta de planos
    public RecyclerView recyclerViewRoletaPlanos;
    private List<AcessoIPTV> planosLista = new ArrayList<>();
    // Credenciais de acesso às plataformas do noggin/paramount
    public static String COD_APP_EXTERNO;
    public static String USUR_APP;
    public static String SEN_APP;


    @SuppressLint({"SetTextI18n", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_paramount_noggin);
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
                        startActivityForResult(new Intent(MenuParamountNoggin.this, MenuParamountNogginContrato.class), 0);
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


        setListaCredenciaisNogginParamount();

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
    private void setListaCredenciaisNogginParamount() {
        recyclerViewRoletaPlanos = (RecyclerView) findViewById(R.id.recyclerviewAcessosIPTV);
        adapterRecyclerViewRoletaPlanos = new AcessoParamountAdapter(planosLista);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewRoletaPlanos.setLayoutManager(mLayoutManager);
        recyclerViewRoletaPlanos.setAdapter(adapterRecyclerViewRoletaPlanos);
        recyclerViewRoletaPlanos.setItemAnimator(null);
        recyclerViewRoletaPlanos.setHasFixedSize(false);
        ////////////////////////////// EFEITO NUBANK INTERPOLAÇÃO ///////////////////////////////////////////////////
        try {
            FrameLayout frameLayoutMsgLista = (FrameLayout) findViewById(R.id.frameLayoutMsgLista);
            TextView textViewMsgLista = (TextView) findViewById(R.id.textViewMsgLista);
            ////////////////////////////// EFEITO NUBANK INTERPOLAÇÃO ///////////////////////////////////////////////////////
            final Animation myAnim = AnimationUtils.loadAnimation(MenuParamountNoggin.this, R.anim.up_from_bottom);
            // Use bounce interpolator with amplitude 0.1 and frequency 15
            myAnim.setInterpolator(new MyBounceInterpolator(0.1, 15));
            recyclerViewRoletaPlanos.startAnimation(myAnim);
            textViewMsgLista.startAnimation(myAnim);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            AcessoIPTV plano = null;
            boolean seSemDireito = true;
            // Valida se possui noggin e parmaount
            if (COD_APP_EXTERNO.contains("noggin") && COD_APP_EXTERNO.contains("paramoun")) {
                // Paramount
                plano = new AcessoIPTV(
                        USUR_APP,
                        SEN_APP,
                        "Paramount",
                        "Ideal para você e sua família.",
                        VariaveisGlobais.IP_IPTV_REDE_TELECOM_PARAMOUNT_APP,
                        "",
                        MenuParamountNoggin.this
                );
                planosLista.add(plano);
                // Noggin
                plano = new AcessoIPTV(
                        USUR_APP,
                        SEN_APP,
                        "Noggin",
                        "Ideal para os seus filhos(as).",
                        VariaveisGlobais.IP_IPTV_REDE_TELECOM_NOGGIN_APP,
                        "",
                        MenuParamountNoggin.this
                );
                planosLista.add(plano);
                seSemDireito = false;
            }

            // Valida se possui noggin
            if (COD_APP_EXTERNO.contains("noggin") && !COD_APP_EXTERNO.contains("paramoun")) {
                // Paramount
                plano = new AcessoIPTV(
                        USUR_APP,
                        SEN_APP,
                        "Noggin",
                        "Ideal para os seus filhos(as).",
                        VariaveisGlobais.IP_IPTV_REDE_TELECOM_NOGGIN_APP,
                        "",
                        MenuParamountNoggin.this
                );
                planosLista.add(plano);
                seSemDireito = false;
            }

            // Valida se possui PARAMOUNT
            if (!COD_APP_EXTERNO.contains("noggin") && COD_APP_EXTERNO.contains("paramoun")) {
                // Paramount
                plano = new AcessoIPTV(
                        USUR_APP,
                        SEN_APP,
                        "Paramount",
                        "Ideal para você e sua família.",
                        VariaveisGlobais.IP_IPTV_REDE_TELECOM_PARAMOUNT_APP,
                        "",
                        MenuParamountNoggin.this
                );
                planosLista.add(plano);
                seSemDireito = false;
            }

            if (seSemDireito) {
                // Exibe msg caso não existe planos disponíveis/suportados
                frameLayoutMsgLista.setVisibility(View.VISIBLE);
                textViewMsgLista.setText("Não há IPTV disponível para seu plano.");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        adapterRecyclerViewRoletaPlanos.notifyDataSetChanged();
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