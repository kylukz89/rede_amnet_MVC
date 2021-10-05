package com.rede.App.View.View;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.rede.App.View.DAO.UsuarioDAO;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.ToolBox.Animatoo;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.ToolBox.Internet;
import com.rede.App.View.ToolBox.LifeCycleObserver;
import com.rede.App.View.ToolBox.MenusAbaLateralEsquerda;
import com.rede.ncarede.R;

/**
 * Exibe as informações cadastrais do usuário no Integrator
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 * @editado 01/03/2019
 */
public class MenuMinhaConta extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final Usuario usuario = new Usuario();
    final UsuarioDAO usuDao = new UsuarioDAO();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"SetTextI18n", "ResourceAsColor", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_minhaconta);
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
                    ControladorInterface.setClickBotao(MenuMinhaConta.this);
                    if (new Internet(MenuPrincipal.CTX).verificaConexaoInternet()) {
                        startActivityForResult(new Intent(MenuMinhaConta.this, MenuPrincipal.class), 0);
                        finish();
                    } else {
                        Toast.makeText(MenuPrincipal.CTX, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        });


        new AsyncTaskDadosUsuario(MenuMinhaConta.this).execute();

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
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_planos) {
            MenusAbaLateralEsquerda.botaoPlanos(MenuMinhaConta.this);
        } else if (id == R.id.nav_suporte) {
            MenusAbaLateralEsquerda.botaoSuporte(MenuMinhaConta.this);
        } else if (id == R.id.nav_servicos) {
            MenusAbaLateralEsquerda.botaoServicos(MenuMinhaConta.this);
        } else if (id == R.id.nav_fale_conosco) {
            MenusAbaLateralEsquerda.botaoFaleConosco(MenuMinhaConta.this);
        } else if (id == R.id.nav_sobre) {
            MenusAbaLateralEsquerda.botaoSobre(MenuMinhaConta.this);
        } else if (id == R.id.nav_options) {
            MenusAbaLateralEsquerda.botaoOpcoes(MenuMinhaConta.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////// ASYNCTASK PARA PROCESSAMENTO DE PAGAMENTOS /////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private class AsyncTaskDadosUsuario extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Context mContext = null;
        static private final int ITERATIONS = 1;

        public AsyncTaskDadosUsuario(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            for (int i = 0; i < ITERATIONS; i++) {
                ThreadRunningOperation();
            }

            try {
                usuDao.getCarregaDadosUsuario();
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

        @SuppressLint("ClickableViewAccessibility")
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onPostExecute(Boolean result) {
            EditText editTextStatusCliente = (EditText) findViewById(R.id.editTextStatusCliente);
            EditText editTextNomeCliente = (EditText) findViewById(R.id.editTextNomeCliente);
            EditText editTextCPFCNPJCliente = (EditText) findViewById(R.id.editTextCPFCNPJCliente);
            EditText editTextEnderecoCliente = (EditText) findViewById(R.id.editTextEnderecoCliente);
            EditText editTextBairroCliente = (EditText) findViewById(R.id.editTextBairroCliente);
            EditText editTextCEPCliente = (EditText) findViewById(R.id.editTextCEPCliente);
            EditText editTextCidadeCliente = (EditText) findViewById(R.id.editTextCidadeCliente);

            final EditText editTextFoneCliente01 = (EditText) findViewById(R.id.editTextFoneCliente01);
            final EditText editTextFoneCliente02 = (EditText) findViewById(R.id.editTextFoneCliente02);
            final EditText editTextFoneCliente03 = (EditText) findViewById(R.id.editTextFoneCliente03);
            final EditText editTextFoneCliente04 = (EditText) findViewById(R.id.editTextFoneCliente04);
            final EditText editTextEmailCliente = (EditText) findViewById(R.id.editTextEmailCliente);

            try {
                if (usuario.getStatus().equals("ATIVO")) {
                    editTextStatusCliente.setText(usuario.getStatus().toUpperCase());
                    editTextStatusCliente.setTextColor(Color.GREEN);
                } else {
                    editTextStatusCliente.setText(usuario.getStatus().toUpperCase());
                    editTextStatusCliente.setTextColor(Color.RED);
                }
                editTextNomeCliente.setText(Ferramentas.setCapitalizarTexto(usuario.getNome().toLowerCase()));
                editTextCPFCNPJCliente.setText(Ferramentas.setCapitalizarTexto(usuario.getCpfCnpj()));
                editTextEnderecoCliente.setText(Ferramentas.setCapitalizarTexto(usuario.getEndereco().toLowerCase()));
                editTextBairroCliente.setText(Ferramentas.setCapitalizarTexto(usuario.getBairro().toLowerCase()));
                editTextCEPCliente.setText(Ferramentas.setCapitalizarTexto(usuario.getCep().toLowerCase()));
                editTextCidadeCliente.setText(Ferramentas.setCapitalizarTexto(usuario.getCidade().toLowerCase()));
                // Dados de contato
                editTextFoneCliente01.setText(Ferramentas.setCapitalizarTexto(usuario.getCelular1().toLowerCase()));
                editTextFoneCliente02.setText(Ferramentas.setCapitalizarTexto(usuario.getCelular2().toLowerCase()));
                editTextFoneCliente03.setText(Ferramentas.setCapitalizarTexto(usuario.getCelular3().toLowerCase()));
                editTextFoneCliente04.setText(Ferramentas.setCapitalizarTexto(usuario.getCelular4().toLowerCase()));

                if (usuario.getCelular1().length() > 5) {
                    editTextFoneCliente01.setText(usuario.getFone());
                    editTextFoneCliente01.setEnabled(false);
                    editTextFoneCliente01.setFocusable(false);
                } else {
                    editTextFoneCliente01.setEnabled(true);
                    editTextFoneCliente01.setFocusable(false);
                    editTextFoneCliente01.setText("Adicionar Contato 01");
                    editTextFoneCliente01.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_add_contact, 0, 0, 0);
                }

                if (usuario.getCelular2().length() > 5) {
                    editTextFoneCliente02.setEnabled(false);
                    editTextFoneCliente02.setFocusable(false);
                    editTextFoneCliente02.setText(usuario.getCelular());
                } else {
                    editTextFoneCliente02.setEnabled(true);
                    editTextFoneCliente02.setFocusable(false);
                    editTextFoneCliente02.setText("Adicionar Contato 02");
                    editTextFoneCliente02.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_add_contact, 0, 0, 0);
                }

                if (usuario.getCelular3().length() > 5) {
                    editTextFoneCliente03.setEnabled(false);
                    editTextFoneCliente03.setFocusable(false);
                    editTextFoneCliente03.setText(usuario.getCelular());
                } else {
                    editTextFoneCliente03.setEnabled(true);
                    editTextFoneCliente03.setFocusable(false);
                    editTextFoneCliente03.setText("Adicionar Celular 03");
                    editTextFoneCliente03.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_add_contact, 0, 0, 0);
                }

                if (usuario.getCelular4().length() > 5) {
                    editTextFoneCliente04.setEnabled(false);
                    editTextFoneCliente04.setFocusable(false);
                    editTextFoneCliente04.setText(usuario.getCelular());
                } else {
                    editTextFoneCliente04.setEnabled(true);
                    editTextFoneCliente04.setFocusable(false);
                    editTextFoneCliente04.setText("Adicionar Celular 04");
                    editTextFoneCliente04.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_add_contact, 0, 0, 0);
                }


                if (usuario.getEmail().length() > 5) {
                    editTextEmailCliente.setEnabled(false);
                    editTextEmailCliente.setFocusable(false);
                    editTextEmailCliente.setText(usuario.getEmail());
                } else {
                    editTextEmailCliente.setEnabled(true);
                    editTextEmailCliente.setFocusable(false);
                    editTextEmailCliente.setText("Adicionar E-mail");
                    editTextEmailCliente.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_add_contact, 0, 0, 0);
                }

                editTextStatusCliente.setEnabled(false);
                editTextStatusCliente.setFocusable(false);

                editTextNomeCliente.setEnabled(false);
                editTextNomeCliente.setFocusable(false);

                editTextCPFCNPJCliente.setEnabled(false);
                editTextCPFCNPJCliente.setFocusable(false);

                editTextEnderecoCliente.setEnabled(false);
                editTextEnderecoCliente.setFocusable(false);

                editTextBairroCliente.setEnabled(false);
                editTextBairroCliente.setFocusable(false);

                editTextCEPCliente.setEnabled(false);
                editTextCEPCliente.setFocusable(false);

                editTextCidadeCliente.setEnabled(false);
                editTextCidadeCliente.setFocusable(false);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Erro ao carregar os dados! " + e.getStackTrace(), Toast.LENGTH_SHORT).show();
            }

            try {
                MenuEditaDadosContato.CPF_CNPJ = usuario.getCpfCnpj();
                MenuEditaDadosContato.CODCLIE = usuario.getCodigo();
                MenuEditaDadosContato.CELULAR_01 = usuario.getCelular1().toLowerCase();
                MenuEditaDadosContato.CELULAR_02 = usuario.getCelular2().toLowerCase();
                MenuEditaDadosContato.CELULAR_03 = usuario.getCelular3().toLowerCase();
                MenuEditaDadosContato.CELULAR_04 = usuario.getCelular4().toLowerCase();
                MenuEditaDadosContato.EMAIL = usuario.getEmail().toLowerCase();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Erro ao carregar os dados! " + e.getStackTrace(), Toast.LENGTH_SHORT).show();
            }

            editTextFoneCliente01.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEvent.ACTION_UP == event.getAction())
                        try {
                            startActivityForResult(new Intent(MenuMinhaConta.this, MenuEditaDadosContato.class), 0);
                            finish();
                        } catch (Exception e) {
                            System.err.println(e);
                        }
                    return false;
                }
            });
            editTextFoneCliente02.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEvent.ACTION_UP == event.getAction())
                        try {
                            startActivityForResult(new Intent(MenuMinhaConta.this, MenuEditaDadosContato.class), 0);
                            finish();
                        } catch (Exception e) {
                            System.err.println(e);
                        }
                    return false;
                }
            });
            editTextFoneCliente03.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEvent.ACTION_UP == event.getAction())
                        try {
                            startActivityForResult(new Intent(MenuMinhaConta.this, MenuEditaDadosContato.class), 0);
                            finish();
                        } catch (Exception e) {
                            System.err.println(e);
                        }
                    return false;
                }
            });
            editTextFoneCliente04.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEvent.ACTION_UP == event.getAction())
                        try {
                            startActivityForResult(new Intent(MenuMinhaConta.this, MenuEditaDadosContato.class), 0);
                            finish();
                        } catch (Exception e) {
                            System.err.println(e);
                        }
                    return false;
                }
            });


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


