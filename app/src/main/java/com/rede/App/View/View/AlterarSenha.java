package com.rede.App.View.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.rede.App.View.DAO.AlteraSenhaDAO;
import com.rede.App.View.JavaBeans.AlteraSenha;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.ToolBox.LifeCycleObserver;
import com.rede.ncarede.R;

/**
 * Altera a senha da central do assinante no Integrator
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 * @editado 01/03/2019
 */
public class AlterarSenha extends AppCompatActivity {

    AlteraSenha altsen = new AlteraSenha();

    private EditText editTextCampoCPFCNPJRedefinir;
    private boolean usuario = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // remove titulo
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alterar_senha);

        editTextCampoCPFCNPJRedefinir = (EditText) findViewById(R.id.editTextCPFRedefinirSenha);


        // Para controlde de tempo da sessão
        LifeCycleObserver.context = this;


        final EditText editTextCPF = (EditText) findViewById(R.id.editTextCPF);
        editTextCPF.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (editTextCPF.getText().toString().length() == 11 && editTextCPF.getText().toString().length() <= 13) {
                        editTextCPF.setText(Ferramentas.mascaraCPF(editTextCPF.getText().toString()));
                    } else {
                        if (editTextCPF.getText().toString().length() == 14) {
                            editTextCPF.setText(Ferramentas.mascaraCNPJ(editTextCPF.getText().toString()));
                        }
                    }
                }
            }
        });
        editTextCPF.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode != KeyEvent.KEYCODE_DEL) {
                    if (editTextCPF.getText().toString().length() == 11) {
                    } else {
                        if (editTextCPF.getText().toString().length() == 14) {
                            editTextCPF.setText(Ferramentas.mascaraCNPJ(editTextCPF.getText().toString()));
                        }
                    }
                }
                return false;
            }
        });

        Button buttonBotaoAlterarSenha = (Button) findViewById(R.id.botaoAlterarSenha);
        buttonBotaoAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editTextCPFCNPJ = (EditText) findViewById(R.id.editTextCPF);
                EditText editTextSenhaAtual = (EditText) findViewById(R.id.editTextSenha);
                EditText editTextNovaSenha = (EditText) findViewById(R.id.editTextNovaSenha);
                EditText editTextRepitaNovaSenha = (EditText) findViewById(R.id.editTextRepitaNovaSenha);

                altsen.setCtx(getApplicationContext());
                altsen.setCpfCnpjCliente(editTextCPFCNPJ.getText().toString());
                altsen.setSenha(editTextSenhaAtual.getText().toString());
                altsen.setNovaSenha(editTextNovaSenha.getText().toString());
                altsen.setRepitaNovaSenha(editTextRepitaNovaSenha.getText().toString());

                try {
                    if (editTextNovaSenha.getText().toString().equals(editTextRepitaNovaSenha.getText().toString())) {
                        // Altera a senha no servidor
                        new AsyncTaskAlterarSenha().execute(altsen);
                    } else {
                        Toast.makeText(getApplicationContext(), "Senhas não combinam! ", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button buttonBotaoCancelar = (Button) findViewById(R.id.botaoCancelar);
        buttonBotaoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AlterarSenha.this, MainActivity.class));
                finish();
            }
        });

        Button botaoEsqueciSenha = (Button) findViewById(R.id.botaoEsqueciSenha);
        botaoEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AlterarSenha.this, EsqueciSenha.class));
            }
        });
    }


    /**
     * AsyncTask para processamento de alteração de senha do cliente
     *
     * @author      Igor Maximo
     * @date        25/01/2021
     */
    private class AsyncTaskAlterarSenha extends AsyncTask<AlteraSenha, Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Object[] dados;

        @Override
        protected Boolean doInBackground(AlteraSenha... altsen) {
            ThreadRunningOperation();
            this.dados = new AlteraSenhaDAO().setAlteraSenhaCentralAssinante(altsen[0]);
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            mProgress.setProgress(progress[0]);
        }

        @Override
        protected void onPreExecute() {
            this.mProgress = ProgressDialog.show(AlterarSenha.this, null, "Processando...", true);
            this.mProgress.setIndeterminate(false);
            this.mProgress.setCancelable(false);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(Boolean result) {
            mProgress.dismiss();
            try {
                // Altera a senha no servidor
                if (result) {
                    Toast.makeText(altsen.getCtx(), (CharSequence) this.dados[1], Toast.LENGTH_SHORT).show();
                    if (Boolean.parseBoolean(this.dados[0].toString())) {
                        startActivity(new Intent(AlterarSenha.this, MainActivity.class));
                        finish();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void ThreadRunningOperation() {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
    }

}
