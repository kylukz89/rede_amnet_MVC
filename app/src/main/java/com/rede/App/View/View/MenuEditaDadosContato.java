package com.rede.App.View.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.rede.App.View.DAO.UsuarioDAO;
import com.rede.App.View.JavaBeans.EditarDadosContato;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.Internet;
import com.rede.App.View.ToolBox.LifeCycleObserver;
import com.rede.App.View.ToolBox.MenusAbaLateralEsquerda;
import com.rede.App.View.ToolBox.PhoneTextFormatter;
import com.rede.ncarede.R;


/**
 * Exibe as informações cadastrais do usuário no Integrator
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 * @editado 01/03/2019
 */
public class MenuEditaDadosContato extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final UsuarioDAO usuDao = new UsuarioDAO();

    protected static String CPF_CNPJ;
    protected static String CODCLIE;
    protected static String CELULAR_01;
    protected static String CELULAR_02;
    protected static String CELULAR_03;
    protected static String CELULAR_04;
    protected static String EMAIL;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_edita_fixo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Para controlde de tempo da sessão
        LifeCycleObserver.context = this;

        final EditText editTextFone01 = (EditText) findViewById(R.id.editTextFone01);
        final EditText editTextFone02 = (EditText) findViewById(R.id.editTextFone02);
        final EditText editTextFone03 = (EditText) findViewById(R.id.editTextFone03);
        final EditText editTextFone04 = (EditText) findViewById(R.id.editTextFone04);
        final EditText email = (EditText) findViewById(R.id.editTextEmail);
        final LinearLayout linearBotaoEditar = (LinearLayout) findViewById(R.id.linearBotaoEditar);


        if (CELULAR_01.length() > 5) {
            editTextFone01.setText(CELULAR_01);
            editTextFone01.setEnabled(false);
            editTextFone01.setFocusable(false);
        } else {
            editTextFone01.setEnabled(true);
            editTextFone01.setFocusable(true);
        }
        if (CELULAR_02.length() > 5) {
            editTextFone02.setText(CELULAR_02);
            editTextFone02.setEnabled(false);
            editTextFone02.setFocusable(false);
        } else {
            editTextFone02.setEnabled(true);
            editTextFone02.setFocusable(true);
        }
        if (CELULAR_03.length() > 5) {
            editTextFone03.setText(CELULAR_03);
            editTextFone03.setEnabled(false);
            editTextFone03.setFocusable(false);
        } else {
            editTextFone03.setEnabled(true);
            editTextFone03.setFocusable(true);
        }
        if (CELULAR_04.length() > 5) {
            editTextFone04.setText(CELULAR_04);
            editTextFone04.setEnabled(false);
            editTextFone04.setFocusable(false);
        } else {
            editTextFone04.setEnabled(true);
            editTextFone04.setFocusable(true);
        }
        if (EMAIL.length() > 5) {
            email.setText(EMAIL);
            email.setEnabled(false);
            email.setFocusable(false);
        } else {
            email.setEnabled(true);
            email.setFocusable(true);
        }



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (new Internet(MenuPrincipal.CTX).verificaConexaoInternet()) {
                        startActivityForResult(new Intent(MenuEditaDadosContato.this, MenuMinhaConta.class), 0);
                        finish();
                    } else {
                        Toast.makeText(MenuPrincipal.CTX, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        });



        editTextFone01.addTextChangedListener(new PhoneTextFormatter(editTextFone01, "(##) #####-####"));


        linearBotaoEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Reproduz o efeito de vibrar o celular
                    ControladorInterface.setClickBotao(getApplicationContext());

                    EditarDadosContato editarDadosContato = new EditarDadosContato();
                    // Carrega entidade
                    editarDadosContato.setCpfCnpj(CPF_CNPJ);
                    editarDadosContato.setCodClie(CODCLIE);
                    editarDadosContato.setCelular1(editTextFone01.getText().toString());
                    editarDadosContato.setCelular2(editTextFone02.getText().toString());
                    editarDadosContato.setCelular3(editTextFone03.getText().toString());
                    editarDadosContato.setCelular4(editTextFone04.getText().toString());
                    editarDadosContato.setEmail(email.getText().toString());

                    // Celular 01
                    if (editTextFone01.getText().toString().length() < 10) {
                        editTextFone01.setError("Celular inválido.");
                        return;
                    }
                    // Celular 02
                    if (editTextFone02.getText().toString().length() < 10) {
                        editTextFone02.setError("Celular inválido.");
                        return;
                    }
                    // Celular 03
                    if (editTextFone03.getText().toString().length() < 10) {
                        editTextFone03.setError("Celular inválido.");
                        return;
                    }
                    // Celular 04
                    if (editTextFone04.getText().toString().length() < 10) {
                        editTextFone04.setError("Celular inválido.");
                        return;
                    }
                    // E-mail
                    if (email.getText().toString().length() < 8 || !email.getText().toString().contains("@")) {
                        email.setError("E-mail inválido!");
                        return;
                    }

                    // Realiza edição cadastral
                    new AsyncTaskEditarDadosContato(MenuEditaDadosContato.this).execute(editarDadosContato);
                } catch (Exception e) {

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
            MenusAbaLateralEsquerda.botaoPlanos(MenuEditaDadosContato.this);
        } else if (id == R.id.nav_suporte) {
            MenusAbaLateralEsquerda.botaoSuporte(MenuEditaDadosContato.this);
        } else if (id == R.id.nav_servicos) {
            MenusAbaLateralEsquerda.botaoServicos(MenuEditaDadosContato.this);
        } else if (id == R.id.nav_fale_conosco) {
            MenusAbaLateralEsquerda.botaoFaleConosco(MenuEditaDadosContato.this);
        } else if (id == R.id.nav_sobre) {
            MenusAbaLateralEsquerda.botaoSobre(MenuEditaDadosContato.this);
        } else if (id == R.id.nav_options) {
            MenusAbaLateralEsquerda.botaoOpcoes(MenuEditaDadosContato.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                ASYNCTASK PARA DADOS DO TÍTULO                                  //
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskEditarDadosContato extends AsyncTask<EditarDadosContato, Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Context mContext = null;


        public AsyncTaskEditarDadosContato(Context context) {
            mContext = context;
        }


        @Override
        protected void onProgressUpdate(Integer... progress) {
            mProgress.setProgress(progress[0]);
        }

        @Override
        protected Boolean doInBackground(EditarDadosContato... params) {
            ThreadRunningOperation();
            try {
                // Chama API da LivingSafe
                final Object[] retorno = new UsuarioDAO().setEditarDadosContato(params[0]);
                // Exibe msg de retorno
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuEditaDadosContato.this);
                        final AlertDialog show = builder.show();
                        ////// AVISO //////
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuEditaDadosContato.this);
                        // set title
                        alertDialogBuilder.setTitle("AVISO!");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage(String.valueOf(retorno[1]))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int idD) {
                                        if (Boolean.parseBoolean(String.valueOf(retorno[0]))) {
                                            // Redireciona para a activty principal
                                            startActivity(new Intent(MenuEditaDadosContato.this, MenuPrincipal.class));
                                            show.dismiss();
                                        }
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
            return null;
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


