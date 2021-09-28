package com.rede.App.View.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.rede.App.View.DAO.AppLogErroDAO;
import com.rede.App.View.DAO.AutenticacaoDAO;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.SQLite.GeraTabelasSQLite;
import com.rede.App.View.SQLite.SQLiteGeraTabelaGerenciamento;
import com.rede.App.View.Services.AppService;
import com.rede.App.View.ToolBox.ConsultaVersionamento;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.ControleSessao;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.ToolBox.LifeCycleObserver;
import com.rede.App.View.ToolBox.ValidaCNPJ;
import com.rede.App.View.ToolBox.ValidaCPF;
import com.rede.App.View.ToolBox.VariaveisGlobais;
import com.rede.ncarede.R;

/**
 * Valida e autentica por CPF/CNPJ e SENHA no cadastro do usuário no Simetra
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 * @editado 08/04/2020
 */
public class MainActivity extends AppCompatActivity {

    Usuario usuario = new Usuario();
    SQLiteGeraTabelaGerenciamento sqliger = new SQLiteGeraTabelaGerenciamento(MainActivity.this);
    private Button buttonBotaoAcessar;
    private TextView text;
    private TextView textVersao;
    //    PotencialCliente potencialCliente = new PotencialCliente();
//    PotencialClienteDAO potencialClienteDAO = new PotencialClienteDAO();
    public static Context ctx;

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // remove titulo
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        this.ctx = MainActivity.this;

        // Para controlde de tempo da sessão
        LifeCycleObserver.context = this;

        try {
            this.textVersao = (TextView) findViewById(R.id.versao);
            this.textVersao.setText("Copyright © Rede Telecom | Americanet - " + VariaveisGlobais.VERSAO + " (" + VariaveisGlobais.VERSAO_NOME + ")");
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }


//        textVersao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Object retorno = new ExecutadorMetodoAssincrono(MainActivity.this).setExecutarMetodo(
//                        new Avisos(),
//                        "getAlertaMassivoTeste",
//                        new Object[] {"123", 555, true},
//                        new Class[] {String.class, int.class, boolean.class},
//                        MainActivity.this
//                );
//            }
//        });



        final EditText editTextcampoSenha = (EditText) findViewById(R.id.editTextcampoSenha);
        editTextcampoSenha.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editTextcampoSenha.setError("Por padrão, a senha são os 6 primeiros dígitos do CPF/CNPJ do titular.");
            }
        });

////////////// SQLITE GERA DB //////////////////////////////////////////////////////////////////////
        GeraTabelasSQLite dbh = new GeraTabelasSQLite(MainActivity.this);
        dbh.getWritableDatabase();
////////////////////////////////////////////////////////////////////////////////////////////////////

        final EditText editTextCampoCPFCNPJ = (EditText) findViewById(R.id.editTextcampoLogin);
        final EditText editTextSenha = (EditText) findViewById(R.id.editTextcampoSenha);
        editTextCampoCPFCNPJ.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (editTextCampoCPFCNPJ.getText().toString().length() == 11) {
                        editTextCampoCPFCNPJ.setText(Ferramentas.mascaraCPF(editTextCampoCPFCNPJ.getText().toString()));
                    } else {
                        if (editTextCampoCPFCNPJ.getText().toString().length() == 14) {
                            usuario.setTipoCliente("J");
                            editTextCampoCPFCNPJ.setText(Ferramentas.mascaraCNPJ(editTextCampoCPFCNPJ.getText().toString()));
                        }
                    }
                }
            }
        });



//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        if (tst != null) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        editTextCampoCPFCNPJ.setText(tst);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
//                        }
//                        sleep(1000);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        thread.start();



        editTextCampoCPFCNPJ.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode != KeyEvent.KEYCODE_DEL) {
                    if (editTextCampoCPFCNPJ.getText().toString().length() == 11) {
                        usuario.setTipoCliente("F");
                    } else {
                        if (editTextCampoCPFCNPJ.getText().toString().length() == 14) {
                            usuario.setTipoCliente("J");
                            editTextCampoCPFCNPJ.setText(Ferramentas.mascaraCNPJ(editTextCampoCPFCNPJ.getText().toString()));
                        }
                    }
                }
                return false;
            }
        });
///////////////////////////// INDISPENSÁVEL PARA CONEXÃO /////////////////////////////////////
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
//////////////////////////////////////////////////////////////////////////////////////////////
        usuario.setTipoCliente("F"); // CPF por padrão
        editTextCampoCPFCNPJ.setHint("CPF ou CNPJ");
        editTextCampoCPFCNPJ.setText("");
//////////////////////////////////////// Credenciais salvas ///////////////////////////////////////////////
        String[] infos = sqliger.selectUltimoLogin("select _id, cpf_cnpj, senha, tipo from gerenciamento", 4);
        editTextCampoCPFCNPJ.setText(infos[1]);
        editTextSenha.setText(infos[2]);

        if (editTextCampoCPFCNPJ.getText().toString().length() == 11 && infos[3].equals("F")) {
            editTextCampoCPFCNPJ.setText(Ferramentas.mascaraCPF(editTextCampoCPFCNPJ.getText().toString()));
            usuario.setTipoCliente("F");
        }
        if (editTextCampoCPFCNPJ.getText().toString().length() >= 14 && infos[3].equals("J")) {
            usuario.setTipoCliente("J");
            editTextCampoCPFCNPJ.setText(infos[1]);
        }

////////////////////////////////////////////// ACESSOS /////////////////////////////////////////////////////////////////////
///////////////// Se os dados já estiverem salvos, loga automaticamente ////////////////////////////////////////////////////
        buttonBotaoAcessar = (Button) findViewById(R.id.buttonBotaoAcessar);
        text = (TextView) findViewById(R.id.tex);
        if (editTextCampoCPFCNPJ.getText().length() >= 11 && editTextSenha.getText().length() > 0) {
            setButtonAcessarClick();
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        buttonBotaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reproduz o efeito de vibrar o celular
                ControladorInterface.setClickBotao(MainActivity.this);

                setButtonAcessarClick();
            }
        });
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        TextView textViewEsqueciSenha = (TextView) findViewById(R.id.textViewEsqueciSenha);
        textViewEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AlterarSenha.class));
            }
        });
    }

    private void setButtonAcessarClick() {
        //////////////////////////// CONSULTA VERSIOANEMNTO ////////////////////////////
//                if (new ConsultaVersionamento().retornaMenorVersaoAceitavel()) {
        final EditText editTextCampoCPFCNPJ = (EditText) findViewById(R.id.editTextcampoLogin);
        final EditText editTextSenha = (EditText) findViewById(R.id.editTextcampoSenha);
        usuario.setCpfCnpj(editTextCampoCPFCNPJ.getText().toString());
        usuario.setSenha(editTextSenha.getText().toString());

        if (editTextCampoCPFCNPJ.length() < 11) {
            Toast.makeText(MainActivity.this, "Preencha os campo!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            View view = getCurrentFocus();
            if (view == null) {
                view = new View(MainActivity.this);
            }
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {

        }


        // Chamada Async para autenticação
        new AutenticarAsyncTask(MainActivity.this).execute(usuario);
        ////////////////////////////////////////////////////////////////////////////////
    }

    /**
     * Método principal de login
     *
     * @author Igor Maximo
     * @date 13/01/2021
     */
    private void setLogar(Usuario usuario) {
        try {
            // Verifica se é P. Física
            if (usuario.getTipoCliente().equals("F")) {
                if (!ValidaCPF.valida(usuario.getCpfCnpj().replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]", "").replaceAll("[:]", "").replaceAll("[)]", ""))) {
                    Toast.makeText(getApplicationContext(), "CPF inválido!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        if (usuario.getCpfCnpj().replace(".", "").replace("-", "").equals(usuario.getCpfCnpjAutenticacao().replace(".", "").replace("-", ""))
                                && usuario.getSenha().equals(usuario.getSenhaAutenticacao())) {
                            Toast.makeText(getApplicationContext(), "Autenticado com sucesso! ", Toast.LENGTH_SHORT).show();
                            MenuPrincipal.NOME_CLIENTE = usuario.getNome();
                            MenuPrincipal.TIPO_CLIENTE = usuario.getTipoCliente();
                            ///////// SERVICE APP ABERTO ////////
                            startService(new Intent(getApplicationContext(), AppService.class));
                            ///////// SQLITE /////////
                            sqliger.atualizaLogin("update gerenciamento set cpf_cnpj = '" + usuario.getCpfCnpjAutenticacao() + "', senha = '" + usuario.getSenhaAutenticacao() + "', tipo = '" + usuario.getTipoCliente() + "'");
                            //////////////////////////
                            // Starta controle de sessão
                            new ControleSessao().getControlarSeSessaoAtivaUsuario();
                            // Start Menu
                            startActivity(new Intent(MainActivity.this, MenuPrincipal.class));
                            this.finish(); // Mata activity
                        } else {
                            Toast.makeText(getApplicationContext(), "Senha incorreta! ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                   /*     if (e.toString().matches("java.net.ConnectException")) {
                            Toast.makeText(MainActivity.this, "Problemas de conexão!.", Toast.LENGTH_SHORT).show();
                        }
                        if (e.toString().contains("No route to host")) {
                            Toast.makeText(MainActivity.this, "Problemas de conexão!.", Toast.LENGTH_SHORT).show();
                        }
                        if (e.toString().contains("End of input at character 0 of")) {
                            Toast.makeText(MainActivity.this, "O app está em manutenção e deve retornar em algumas horas!\nPara maiores informações entre em contato com a nossa central de relacionamentos.", Toast.LENGTH_SHORT).show();
                        }
                        if (e.toString().contains("No value for CNPJ_CPF_CLIE")) {
                            Toast.makeText(MainActivity.this, "Cadastro inexistente!", Toast.LENGTH_SHORT).show();
//                            potencialCliente.setCampoCPFCNPJ(editTextCampoCPFCNPJ.getText().toString());
//                            if (potencialClienteDAO.retornaSeCPFExisteClientePotencial(potencialCliente)) { // Pesquisa banco de dados do servidor
//                                MenuPrincipal.CLIENTE_POTENCIAL = true;
//                                MenuPrincipal.CPFCNPJ_CLIENTE_POTENCIAL = editTextCampoCPFCNPJ.getText().toString();
//                                startActivity(i);
//                            } else {
//                                // Redireciona para a tela de cadastro gratuito
//                                geraDialogDesejoMeCadastrarGratuitamente(editTextCampoCPFCNPJ.getText().toString());
//                            }
                        }*/
                    }
                }
            } else {
                // Verifica se é P. Jurídica
                if (!ValidaCNPJ.valida(usuario.getCpfCnpj().replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]", "").replaceAll("[:]", "").replaceAll("[)]", ""))) {
                    Toast.makeText(getApplicationContext(), "CNPJ inválido!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        // 220 836 148 28
//                            autdao.carregaDadosAutenticacaoUsuario(); // Autentica
                        if (usuario.getCpfCnpj().replace(".", "").replace("-", "").replace("/", "").equals(usuario.getCpfCnpjAutenticacao().replace(".", "").replace("-", "").replace("/", ""))
                                && usuario.getSenha().equals(usuario.getSenhaAutenticacao())) {
                            Toast.makeText(getApplicationContext(), "Autenticado com sucesso! ", Toast.LENGTH_SHORT).show();
                            MenuPrincipal.NOME_CLIENTE = usuario.getNome();
                            MenuPrincipal.TIPO_CLIENTE = usuario.getTipoCliente();
                            ///////// SERVICE APP ABERTO ////////
                            startService(new Intent(getApplicationContext(), AppService.class));
                            ///////// SQLITE /////////
                            sqliger.atualizaLogin("update gerenciamento set cpf_cnpj = '" + usuario.getCpfCnpjAutenticacao() + "', senha = '" + usuario.getSenhaAutenticacao() + "', tipo = '" + usuario.getTipoCliente() + "'");
                            //////////////////////////
                            // Starta controle de sessão
                            new ControleSessao().getControlarSeSessaoAtivaUsuario();
                            // Start Menu
                            startActivity(new Intent(MainActivity.this, MenuPrincipal.class));
                            this.finish(); // Mata activity
                        } else {
                            Toast.makeText(getApplicationContext(), "Senha incorreta! ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
//                        Toast.makeText(getApplicationContext(), "Erro interno!", Toast.LENGTH_SHORT).show();
                    /*    if (e.toString().matches("No route to host")) {
                            Toast.makeText(MainActivity.this, "Problemas de conexão!.", Toast.LENGTH_SHORT).show();
                        }
                        if (e.toString().contains("End of input at character 0 of")) {
                            Toast.makeText(MainActivity.this, "O app está em manutenção e deve retornar em algumas horas!\nPara maiores informações entre em contato com a nossa central de relacionamentos.", Toast.LENGTH_SHORT).show();
                        }
                        if (e.toString().matches("java.net.NoRouteToHostException: No route to host")) {
                            Toast.makeText(MainActivity.ctx, "Problemas de conexão!", Toast.LENGTH_SHORT).show();
                        }
                        if (e.toString().contains("No value for CNPJ_CPF_CLIE")) {
                            Toast.makeText(MainActivity.this, "Cadastro inexistente!", Toast.LENGTH_SHORT).show();
//                            potencialCliente.setCampoCPFCNPJ(editTextCampoCPFCNPJ.getText().toString());
//                            if (potencialClienteDAO.retornaSeCPFExisteClientePotencial(potencialCliente)) { // Pesquisa banco de dados do servidor
//                                MenuPrincipal.CLIENTE_POTENCIAL = true;
//                                MenuPrincipal.CPFCNPJ_CLIENTE_POTENCIAL = editTextCampoCPFCNPJ.getText().toString();
//                                startActivity(i);
//                            } else {
//                                // Redireciona para a tela de cadastro gratuito
//                                geraDialogDesejoMeCadastrarGratuitamente(editTextCampoCPFCNPJ.getText().toString());
//                            }
                        }*/
                    }
                }
            }
        } catch (Exception e) {
//                if (e.toString().matches("java.net.ConnectException")) {
//                    Toast.makeText(MainActivity.ctx, "Problemas de conexão!.", Toast.LENGTH_SHORT).show();
//                }
//                if (e.toString().matches("End of input at character 0 of")) {
//                    Toast.makeText(MainActivity.ctx, "Falha na solicitação, tente novamente!", Toast.LENGTH_SHORT).show();
//                }
//                if (e.toString().matches("java.net.NoRouteToHostException: No route to host") || e.toString().matches("java.net.SocketException: Network is unreachable")) {
//                    Toast.makeText(MainActivity.ctx, "Problemas de conexão!", Toast.LENGTH_SHORT).show();
//                }
//                if (e.toString().matches("java.net.SocketTimeoutException: connect timed out")) {
//                    Toast.makeText(MainActivity.ctx, "Tempo conexão excedido!", Toast.LENGTH_SHORT).show();
//                }
//                if (e.toString().contains("No value for CNPJ_CPF_CLIE")) {
//                    Toast.makeText(MainActivity.ctx, "Cadastro inexistente!", Toast.LENGTH_SHORT).show();
//                }
        }

    }

    /**
     * Exibe tutorial na primeira vez que usuário loga no APP
     *
     * @author Igor Maximo
     * @data 23/04/2019
     */
    private void geraDialogDesejoMeCadastrarGratuitamente(final String cpfCnpj) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("WrongViewCast") View view = inflater.inflate(R.layout.desejo_cadastrar, null);
        builder.setView(view);
        final AlertDialog show = builder.show();

//        TextView linearBotaoDesejoCadastrar = (TextView) view.findViewById(R.id.botaoDesejoMeCadastrar);
//        linearBotaoDesejoCadastrar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                show.dismiss();
//                try {
//                    @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchTipoDocumentoCpfCnpj = (Switch) findViewById(R.id.switch1);
//                    if (switchTipoDocumentoCpfCnpj.isChecked()) {
//                        CadastraPotencialCliente.tipoClienteMainAct = "J";
//                    } else {
//                        CadastraPotencialCliente.tipoClienteMainAct = "F";
//                    }
//                    CadastraPotencialCliente.cpfCnpjMainAct = cpfCnpj;
//                    Intent cadastraPotencialCliente = new Intent(MainActivity.this, CadastraPotencialCliente.class);
//                    startActivity(cadastraPotencialCliente);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        view.findViewById(R.id.botaoNaoCadstrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
            }
        });
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// ASYNCTASK ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    private class AutenticarAsyncTask extends AsyncTask<Usuario, Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Context mContext = null;
        private Usuario usuarioRetornado = null;

        public AutenticarAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(final Usuario... usuario) {
            try {
                try {
                    usuarioRetornado = (Usuario) new AutenticacaoDAO().getCarregaDadosAutenticacaoUsuario(usuario[0])[1];
                } catch (final Exception e) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                if (e.toString().contains("java.net.ConnectException")) {
                                    Toast.makeText(MainActivity.ctx, "Problemas de conexão!.", Toast.LENGTH_SHORT).show();
                                }
                                if (e.toString().contains("End of input at character 0 of")) {
                                    Toast.makeText(MainActivity.ctx, "Falha na solicitação, tente novamente!", Toast.LENGTH_SHORT).show();
                                }
                                if (e.toString().contains("java.net.NoRouteToHostException: No route to host") || e.toString().matches("java.net.SocketException: Network is unreachable")) {
                                    Toast.makeText(MainActivity.ctx, "Problemas de conexão!", Toast.LENGTH_SHORT).show();
                                }
                                if (e.toString().contains("java.net.NoRouteToHostException: No route to host")) {
                                    Toast.makeText(MainActivity.ctx, "Problemas de conexão!", Toast.LENGTH_SHORT).show();
                                }
                                if (e.toString().contains("java.net.SocketTimeoutException: connect timed out")) {
                                    Toast.makeText(MainActivity.ctx, "Tempo conexão excedido!", Toast.LENGTH_SHORT).show();
                                }
                                if (e.toString().contains("No value for CNPJ_CPF_CLIE")) {
                                    Toast.makeText(MainActivity.ctx, "Cadastro inexistente!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {

                            }
                        }
                    });
                }
                Thread.sleep(500);
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
            this.mProgress = ProgressDialog.show(MainActivity.this, null, "Processando...", true);
            this.mProgress.setIndeterminate(false);
            this.mProgress.setCancelable(false);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                if (new ConsultaVersionamento().retornaMenorVersaoAceitavel()) {
                    setLogar(usuarioRetornado);
                }
            }
            mProgress.dismiss();
        }
    }
}
