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
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.rede.App.View.DAO.HabProvisoriaDAO;
import com.rede.App.View.JavaBeans.HabProvisoria;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.ToolBox.Animatoo;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.Internet;
import com.rede.App.View.ToolBox.LifeCycleObserver;
import com.rede.App.View.ToolBox.MenusAbaLateralEsquerda;
import com.rede.ncarede.R;

/**
 * Exibe o extrato das faturas de um plano escolhido
 * parar ser gerada 2ª via
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 * @editado 02/03/2019
 */
public class MenuHabProv extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final Usuario usuario = new Usuario();
    private final HabProvisoriaDAO habprov = new HabProvisoriaDAO();
    //FragmentRoletaPlanos fragmentRoletaPlanos = new FragmentRoletaPlanos();
    private HabProvisoria hprov;
    WebView mWebView;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_habprov);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    ControladorInterface.setClickBotao(getApplicationContext());
                    if (new Internet(MenuPrincipal.CTX).verificaConexaoInternet()) {
                        startActivityForResult(new Intent(MenuHabProv.this, MenuPrincipal.class), 0);
                        finish();
                    } else {
                        Toast.makeText(MenuPrincipal.CTX, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        });


        mWebView = (WebView) findViewById(R.id.textView4);

        String text = "<html><body>"
                + "<p align=\"center\" style=\"color: #FF0000; font-weight: bold; \">"
                + ("NOTA</p><p align=\"justify\" style=\"color: #FF0000; font-weight: bold; \">Ao habilitar provisoriamente um plano suspenso por débito esta ação irá ativar o plano durante os próximos 03 (três) dias corridos e esta funcionalidade ficará indisponível até o mês seguinte.")
                + "</p> "
                + "</body></html>";
        mWebView.loadData(text, "text/html", "utf-8");


        // Carrega lista dos planos suspensos por débito (bloqueados)
        new AsyncTaskConsultasPlanosBloqueados(MenuHabProv.this).execute();

        // Botão para habilitar provisoriamente um plano escolhido
        LinearLayout buttonBotaoHabilitar = (LinearLayout) findViewById(R.id.linearBotaoHabilitar);
        final Spinner spinnerComboPlanos = (Spinner) findViewById(R.id.spinnerComboPlanosSuspensos);
        buttonBotaoHabilitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if ((hprov.getPlanosSuspensosPorDebitoCodSerCli().size() != 0)) {
                        hprov.setCodigoCliente(usuario.getCodigo());
                        hprov.setCodSerCli(String.valueOf(hprov.getPlanosSuspensosPorDebitoCodSerCli().get(spinnerComboPlanos.getSelectedItemPosition())));

                        habprov.executaHabilitacaoProvisoria(MenuHabProv.this);

                        new AsyncTaskHabProv(MenuHabProv.this).execute(); // Método Assíncrono para processar habilitação provisória
                        startActivityForResult(new Intent(MenuHabProv.this, MenuPrincipal.class), 0);

                        new MenuPrincipal().recreate();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Não há planos suspensos por débito!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    System.err.println("=======================================================> " + e);
                }
            }
        });


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


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////// ASYNCTASK PARA PROCESSAMENTO DE PAGAMENTOS /////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class AsyncTaskConsultasPlanosBloqueados extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Context mContext = null;
        static private final int ITERATIONS = 1;

        public AsyncTaskConsultasPlanosBloqueados(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            for (int i = 0; i < ITERATIONS; i++) {
                ThreadRunningOperation();
            }

            try {
                hprov = habprov.getPlanosSuspensosPorDebito();
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
            //mProgress.setTitle("Pagamento");
            mProgress.setMessage("Processando...");
            mProgress.setIndeterminate(false);
            mProgress.setCancelable(false);
            mProgress.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {

            final Spinner spinnerComboPlanos = (Spinner) findViewById(R.id.spinnerComboPlanosSuspensos);

            // Combo carregado com nome dos planos suspensos por débito
            try {
                if ((hprov.getPlanosSuspensosPorDebitoCodSerCli().size() != 0)) {
                    spinnerComboPlanos.setAdapter(new ArrayAdapter<String>(MenuHabProv.this, android.R.layout.simple_list_item_1, hprov.getPlanosSuspensosPorDebito())); // Preenche com os planos suspensos por débito do cliente
                } else {
                    spinnerComboPlanos.setAdapter(new ArrayAdapter<String>(MenuHabProv.this, android.R.layout.simple_list_item_1, new String[]{"Sem planos suspensos"})); // Preenche com os planos suspensos por débito do cliente
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            mProgress.dismiss();
            //geraPopUpTransacao(autorizada, msgTransacao); // cor do popup transação
            // Toast.makeText(MenuHabProv.this, "Plano habilitado por 3 dias! ", Toast.LENGTH_SHORT).show();
        }

        private void ThreadRunningOperation() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }

    private class AsyncTaskHabProv extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Context mContext = null;
        static private final int ITERATIONS = 1;

        public AsyncTaskHabProv(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            for (int i = 0; i < ITERATIONS; i++) {
                ThreadRunningOperation();
            }

            try {
                //habprov.executaHabilitacaoProvisoria(mContext);
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
            //mProgress.setTitle("Pagamento");
            mProgress.setMessage("Processando...");
            mProgress.setIndeterminate(false);
            mProgress.setCancelable(false);
            mProgress.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgress.dismiss();
            //geraPopUpTransacao(autorizada, msgTransacao); // cor do popup transação
            // Toast.makeText(MenuHabProv.this, "Plano habilitado por 3 dias! ", Toast.LENGTH_SHORT).show();
        }

        private void ThreadRunningOperation() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
            MenusAbaLateralEsquerda.botaoPlanos(MenuHabProv.this);
        } else if (id == R.id.nav_suporte) {
            MenusAbaLateralEsquerda.botaoSuporte(MenuHabProv.this);
        } else if (id == R.id.nav_servicos) {
            MenusAbaLateralEsquerda.botaoServicos(MenuHabProv.this);
        } else if (id == R.id.nav_fale_conosco) {
            MenusAbaLateralEsquerda.botaoFaleConosco(MenuHabProv.this);
        } else if (id == R.id.nav_sobre) {
            MenusAbaLateralEsquerda.botaoSobre(MenuHabProv.this);
        } else if (id == R.id.nav_options) {
            MenusAbaLateralEsquerda.botaoOpcoes(MenuHabProv.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}


