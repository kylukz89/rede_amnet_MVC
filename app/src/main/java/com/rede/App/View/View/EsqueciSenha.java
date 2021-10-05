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
public class EsqueciSenha extends AppCompatActivity {

    AlteraSenha altsen = new AlteraSenha();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // remove titulo
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_esqueci_senha);

        // Para controlde de tempo da sessão
        LifeCycleObserver.context = this;

        final EditText editTextCPFRedefinirSenha = (EditText) findViewById(R.id.editTextCPFRedefinirSenha);
        editTextCPFRedefinirSenha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (editTextCPFRedefinirSenha.getText().toString().length() == 11 && editTextCPFRedefinirSenha.getText().toString().length() <= 13) {
                        editTextCPFRedefinirSenha.setText(Ferramentas.mascaraCPF(editTextCPFRedefinirSenha.getText().toString()));
                    } else {
                        if (editTextCPFRedefinirSenha.getText().toString().length() == 14) {
                            editTextCPFRedefinirSenha.setText(Ferramentas.mascaraCNPJ(editTextCPFRedefinirSenha.getText().toString()));
                        }
                    }
                }
            }
        });
        editTextCPFRedefinirSenha.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode != KeyEvent.KEYCODE_DEL) {
                    if (editTextCPFRedefinirSenha.getText().toString().length() == 11) {
                    } else {
                        if (editTextCPFRedefinirSenha.getText().toString().length() == 14) {
                            editTextCPFRedefinirSenha.setText(Ferramentas.mascaraCNPJ(editTextCPFRedefinirSenha.getText().toString()));
                        }
                    }
                }
                return false;
            }
        });

        findViewById(R.id.botaoEnviarSenhaEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (editTextCPFRedefinirSenha.getText().toString().length() > 0) {
                        altsen.setCpfCnpjCliente(editTextCPFRedefinirSenha.getText().toString());
                        altsen.setCtx(EsqueciSenha.this);

                        new AsyncTaskEsqueciSenha().execute(altsen);

                    } else {
                        Toast.makeText(getApplicationContext(), "Preenche corretamente o campo! ", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        });

        findViewById(R.id.botaoCancelarEsqueciSenha).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EsqueciSenha.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    /**
     * AsyncTask para processamento de alteração de senha do cliente
     *
     * @author      Igor Maximo
     * @date        25/01/2021
     */
    private class AsyncTaskEsqueciSenha extends AsyncTask<AlteraSenha, Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Object[] dados;

        @Override
        protected Boolean doInBackground(AlteraSenha... altsen) {
            ThreadRunningOperation();
            this.dados = new AlteraSenhaDAO().setEnviarSenhaParaEmail(altsen[0]);
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            mProgress.setProgress(progress[0]);
        }

        @Override
        protected void onPreExecute() {
            this.mProgress = ProgressDialog.show(EsqueciSenha.this, null, "Processando...", true);
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
                        startActivity(new Intent(EsqueciSenha.this, MainActivity.class));
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
