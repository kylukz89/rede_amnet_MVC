package com.rede.App.View.View;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.rede.App.View.Adapters.CartaoRecorrenteAdapter;
import com.rede.App.View.Adapters.ContratoRecorrenteAdapter;
import com.rede.App.View.DAO.AppLogErroDAO;
import com.rede.App.View.DAO.CartaoRecorrenteVindiDAO;
import com.rede.App.View.JavaBeans.CartaoRecorrente;
import com.rede.App.View.JavaBeans.CartaoRecorrenteManipulacao;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.ToolBox.Animatoo;
import com.rede.App.View.ToolBox.CelularMask;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.ToolBox.Internet;
import com.rede.App.View.ToolBox.LifeCycleObserver;
import com.rede.App.View.ToolBox.MascarasPagamento;
import com.rede.App.View.ToolBox.MyBounceInterpolator;
import com.rede.ncarede.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Exibe a lista de contratos para alteração na VINDI
 *
 * @author Igor Maximo
 * @date 31/05/2021
 */
public class MenuRecorrenteCartao extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String COD_CONTRATO_ITEM_ESCOLHIDO;
    public static String COD_CONTRATO_ESCOLHIDO;
    public static String COD_CODCLIE_CARTAO;
    public static String COD_CONTRATO_EMPRESA_CNPJ;
    public static String COD_CONTRATO_EMPRESA_NOME;
    public static String ID_CLIENTE_VINDI;
    public static String ID_CARTAO_VINDI;
    public static Context CTX;
    // Entidade usuário logado
    final Usuario usuario = new Usuario();
    private CartaoRecorrenteAdapter adapterRecyclerViewRoletaPlanos;
    // Recycler da roleta de planos
    public RecyclerView recyclerViewRoletaPlanos;
    private List<CartaoRecorrente> cartaoLista = new ArrayList<>();
    protected Button buttonBotaoCadastrarNovoCartaoVindi;
    protected boolean SE_PODE_CADASTRAR_NOVO_CARTAO = false;

    @SuppressLint({"SetTextI18n", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_cartao_recorrente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Carrega variável com o context dessa classe
        CTX = MenuRecorrenteCartao.this;
        // Botão voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Reproduz o efeito de vibrar o celular
                    ControladorInterface.setClickBotao(getApplicationContext());
                    if (new Internet(CTX).verificaConexaoInternet()) {
                        startActivityForResult(new Intent(MenuRecorrenteCartao.this, MenuRecorrenteContrato.class), 0);
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


        // Carrega recyclerview com todos os contratos
        setCarregarRoletaContratos();

        toolbar.setElevation(0f);

        // Para controlde de tempo da sessão
        LifeCycleObserver.context = this;
        // Animação de entrada
        Animatoo.animateSwipeLeft(this);
///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // disabilita aba lateral esquerda
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /**
     * Set resetar todas as variáveis globais
     *
     * @author      Igor Maximo
     * @date        23/06/2021
     */
    public static void setResetaVariaveisGlobais() {
        COD_CONTRATO_ITEM_ESCOLHIDO = null;
        COD_CONTRATO_ESCOLHIDO = null;
        COD_CODCLIE_CARTAO = null;
        COD_CONTRATO_EMPRESA_CNPJ = null;
        COD_CONTRATO_EMPRESA_NOME = null;
        ID_CLIENTE_VINDI = null;
    }

    /**
     * Carrega o recyclerview com cada entidade carregada pertinente ao índice
     *
     * @author  Igor Maximo
     * @date    18/04/2019
     */
    @SuppressLint("SetTextI18n")
    private void setCarregarRoletaContratos() {
        recyclerViewRoletaPlanos = (RecyclerView) findViewById(R.id.recyclerviewCartaoRecorrente);
        adapterRecyclerViewRoletaPlanos = new CartaoRecorrenteAdapter(cartaoLista);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewRoletaPlanos.setLayoutManager(mLayoutManager);
        recyclerViewRoletaPlanos.setAdapter(adapterRecyclerViewRoletaPlanos);
        recyclerViewRoletaPlanos.setItemAnimator(null);
        recyclerViewRoletaPlanos.setHasFixedSize(false);
        ////////////////////////////// EFEITO NUBANK INTERPOLAÇÃO ///////////////////////////////////////////////////////
        // Botão para cadastrar um novo cartão
        buttonBotaoCadastrarNovoCartaoVindi = (Button) findViewById(R.id.buttonBotaoCadastrarNovoCartaoVindi);
        // Esconde botão para se aplicar a regra se deve ou não ficar visível
        buttonBotaoCadastrarNovoCartaoVindi.setVisibility(View.INVISIBLE);
        try {
            new AsyncTaskCarregaCartao(MenuRecorrenteCartao.this).execute();
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), new Object() {
            }.getClass().getEnclosingMethod().getName() + " ERRO MSG: " + e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
        // Botão que redireciona para a tela de cadastro de novo cartão
        buttonBotaoCadastrarNovoCartaoVindi = (Button) findViewById(R.id.buttonBotaoCadastrarNovoCartaoVindi);
        buttonBotaoCadastrarNovoCartaoVindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reproduz o efeito de vibrar o celular
                ControladorInterface.setClickBotao(getApplicationContext());
                // Redireciona para tela de cadastro de cartões
                setGeraDialogEditorFormCartao(true);
            }
        }); 
    }


    private boolean seModalAberto = false;
    /**
     * Exibe o "modal" do crud de dados do cartão de crédito
     * <p>
     * seNovoCadastro for TRUE = CADASTRO NOVO CARTÃO (apenas clientes que não eram vindi)
     * seNovoCadastro for FALSE = EDIÇÃO/TROCA DE CARTÃO
     *
     * @author Igor Maximo
     * @data 18/04/2019
     */
    @SuppressLint("SetTextI18n")
    public void setGeraDialogEditorFormCartao(final boolean seNovoCadastro) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_cielo_png);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("WrongViewCast") final View view = inflater.inflate(R.layout.popup_cartao_editor_recorrente, null);
//////////////////////////////////// CAMPOS ///////////////////////////////////////
        final EditText editTextNumeroCartao = (EditText) view.findViewById(R.id.editTextNumeroCartao);
        final EditText editTextValidade = (EditText) view.findViewById(R.id.editTextValidade);
        final EditText editTextCVV = (EditText) view.findViewById(R.id.editTextCVV);
        final EditText editTextTitular = (EditText) view.findViewById(R.id.editTextTitular);
        final EditText editTextCPF = (EditText) view.findViewById(R.id.editTextCPF);
        final EditText editTextFone = (EditText) view.findViewById(R.id.editTextFone);
//////////////////////////////////// Máscaras ///////////////////////////////////////
        editTextNumeroCartao.addTextChangedListener(MascarasPagamento.insert(MascarasPagamento.CARTAO_MASK, editTextNumeroCartao));
        editTextValidade.addTextChangedListener(MascarasPagamento.insert(MascarasPagamento.VALIDADE_MASK, editTextValidade));
        editTextCVV.addTextChangedListener(MascarasPagamento.insert(MascarasPagamento.CVV_MASK, editTextCVV));
        editTextCPF.addTextChangedListener(MascarasPagamento.insert(MascarasPagamento.CPF_MASK, editTextCPF));
///////////////////////////// PREENCHIMENTO AUTOMÁTICO //////////////////////////////
        editTextNumeroCartao.requestFocus();
        if (!usuario.getTipoCliente().equals("J")) {
            editTextTitular.setText(usuario.getNome());
            editTextCPF.setText(usuario.getCpfCnpj());
        }
/////////////////////////////////////////////////////////////////////////////////////
        builder.setView(view);
        // Verifica se o alerta já está exibido
        seModalAberto = view.isShown();
        final AlertDialog show =  (!seModalAberto) ? builder.show() : null;
        // Seta máscara de celular
        editTextFone.addTextChangedListener(new CelularMask(new WeakReference<EditText>(editTextFone)));
        final LinearLayout linearBotaoConfirmarPagamento = (LinearLayout) view.findViewById(R.id.linearBotaoConfirmarPagamento);
        TextView imageButtonPagar = (TextView) view.findViewById(R.id.imageButtonPagar);
        TextView textViewTituloPOPUP = (TextView) view.findViewById(R.id.textViewTituloPOPUP);

        // Valida se operação é de novo cadastrou ou edição
        if (seNovoCadastro) {
            imageButtonPagar.setText("CADASTRAR");
            textViewTituloPOPUP.setText("VINDI - NOVO CARTÃO");
        } else {
            imageButtonPagar.setText("SALVAR");
            textViewTituloPOPUP.setText("VINDI - EDITAR CARTÃO");
        }

        linearBotaoConfirmarPagamento.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"WrongConstant", "ResourceAsColor"})
            @Override
            public void onClick(View v) {
                // Reproduz o efeito de vibrar o celular
                ControladorInterface.setClickBotao(getApplicationContext());
//////////////////////////////////////////// VERIFICA CAMPOS PREENCHIDOS /////////////////////////////////////////////////////
                try {
                    editTextFone.setText("(" + editTextFone.getText().toString().replace("(", "").replace(")", "").replace(" ", "").replace("-", "").substring(0, 2) + ") " + editTextFone.getText().toString().replace("(", "").replace(")", "").replace(" ", "").replace("-", "").substring(2, 7) + "-" + editTextFone.getText().toString().replace("(", "").replace(")", "").replace(" ", "").replace("-", "").substring(7));
                } catch (Exception e) {

                }
                // Número do cartão
                if (editTextNumeroCartao.getText().toString().replace(".", "").length() <= 15) {
                    editTextNumeroCartao.setError("Número do cartão é obrigatório!");
                    seCamposPreenchidos[0] = false;
                } else {
                    seCamposPreenchidos[0] = true;
                }
                // CVV do cartão
                if (editTextCVV.getText().toString().replace(".", "").length() < 3) {
                    editTextCVV.setError("Código de segurança existente atrás do cartão é obrigatório!");
                    seCamposPreenchidos[1] = false;
                } else {
                    seCamposPreenchidos[1] = true;
                }
                // Validade do cartão
                if (editTextValidade.getText().toString().replace("/", "").length() < 4) {
                    editTextValidade.setError("Campo validade do cartão é obrigatório! (Ex: 12/22)");
                    seCamposPreenchidos[2] = false;
                } else {
                    seCamposPreenchidos[2] = true;
                }
                // Titular do cartão
                if (editTextTitular.getText().toString().replace("/", "").length() <= 7) { // Para casos de clientes com nome curto demais
                    editTextTitular.setError("Nome do titular do cartão é obrigatório! (Min. 7 caracteres)");
                    seCamposPreenchidos[3] = false;
                } else {
                    seCamposPreenchidos[3] = true;
                }
                // Celular do cliente
                if (editTextFone.getText().toString().length() < 15) {
                    editTextFone.setError("Campo do celular!");
                    seCamposPreenchidos[4] = false;
                } else {
                    seCamposPreenchidos[4] = true;
                }
                //////////////////////////////////////////// DAO PARA PAGAMENTO /////////////////////////////////////////
                if (Ferramentas.getVerificaTodosCamposPreenchidos(seCamposPreenchidos, MenuRecorrenteCartao.this)) {
                    try {
                        CartaoRecorrenteManipulacao cartaoRecorrenteManipulacao = new CartaoRecorrenteManipulacao();
                        // Dados do cliente
                        cartaoRecorrenteManipulacao.setCpfCnpj(usuario.getCpfCnpj());
                        cartaoRecorrenteManipulacao.setSeNovoCadastro(seNovoCadastro); // Se cadastro ou edição
                        // Carrega entidade de dados pra cadastramento na vindi ou para pagamento recorrente
                        cartaoRecorrenteManipulacao.setNumeroCartao(editTextNumeroCartao.getText().toString());
                        cartaoRecorrenteManipulacao.setValidade(editTextValidade.getText().toString());
                        cartaoRecorrenteManipulacao.setCVV(editTextCVV.getText().toString());
                        cartaoRecorrenteManipulacao.setNomeTitular(editTextTitular.getText().toString());
                        cartaoRecorrenteManipulacao.setCampoCelularNovoVindi(editTextFone.getText().toString());
                        cartaoRecorrenteManipulacao.setCodContratoEscolhido(COD_CONTRATO_ESCOLHIDO);
                        cartaoRecorrenteManipulacao.setCodClieCartao(COD_CODCLIE_CARTAO);
                        cartaoRecorrenteManipulacao.setBandeira(Ferramentas.getBandeiraCartao(editTextNumeroCartao.getText().toString().replace(".", "")));
                        // Dados da empresa fornecedora
                        cartaoRecorrenteManipulacao.setContratoEmpresaCNPJ(COD_CONTRATO_EMPRESA_CNPJ);
                        cartaoRecorrenteManipulacao.setContratoEmpresaNome(COD_CONTRATO_EMPRESA_NOME);
                        cartaoRecorrenteManipulacao.setIdClienteVindi(ID_CLIENTE_VINDI);
                        cartaoRecorrenteManipulacao.setCodContratoItemEscolhido(COD_CONTRATO_ITEM_ESCOLHIDO);
                        // Exibe mensagem do valor que será descontado do cartão
                        setDialogAlert(cartaoRecorrenteManipulacao);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (show != null && show.isShowing() && Ferramentas.getVerificaTodosCamposPreenchidos(seCamposPreenchidos, MenuRecorrenteCartao.this)) {
                        show.dismiss();
                    }
                    seCamposPreenchidos = null;
                    seCamposPreenchidos = new boolean[5];
                } else {
                    Toast.makeText(getApplicationContext(), "Existem campos obrigatórios!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Gera alert dialog para explicar que é debitado X qtd do cartão
     * para que seja possível validar se o cartão e válido na CIELO
     *
     * @author      Igor Mxaimo
     * @date        29/06/20210
     */
    public void setDialogAlert(CartaoRecorrenteManipulacao cartaoRecorrenteManipulacao) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("WrongViewCast") View view = inflater.inflate(R.layout.fragment_novo_atendimento, null);

        builder.setView(view);
        final AlertDialog show = builder.show();
        final TextView textViewMsgAntiVirus = (TextView) view.findViewById(R.id.textViewMsgAntiVirus);
        textViewMsgAntiVirus.setText(Html.fromHtml("<span style='color: #555555; text-shadow: 5px 5px #333333;'>" +
                "<label style='color: #000000; text-shadow: 5px 5px #000000;'><b>"
                + "Ao utilizar este recurso será DEBITADO e ESTORNADO o valor de 20 centavos do seu cartão para verificarmos se o cartão é válido. " +
                "Deseja prosseguir?</b></label></span>"));

        view.findViewById(R.id.textViewMsgAntiVirusBotaoNegar).setVisibility(View.GONE);
        final TextView textViewMsgAntiVirusBotaoOK = (TextView) view.findViewById(R.id.textViewMsgAntiVirusBotaoConfirmar);

        textViewMsgAntiVirusBotaoOK.setText("Ok, eu concordo!");

        // Botão confirmar
        textViewMsgAntiVirusBotaoOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre com o context do MenuAtendimento
                show.dismiss();
                // Chama operação de cadastrar cartão na vindi
                new AsyncTaskEditarCartaoClienteVindiCartaoRecorrente().execute(cartaoRecorrenteManipulacao);

            }
        });
    }

    private boolean[] seCamposPreenchidos = new boolean[5];
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

    /**
     * AsyncTask para processamento paralelo do pagamento e spinner para congelar a tela
     *
     * @author Igor Maximo
     * @date 18/04/2019
     */
    private class AsyncTaskEditarCartaoClienteVindiCartaoRecorrente extends AsyncTask<CartaoRecorrenteManipulacao, Integer, Object[]> {
        private ProgressDialog mProgress = null;
        Object[] dados;

        @Override
        protected Object[] doInBackground(CartaoRecorrenteManipulacao... entidade) {
            ThreadRunningOperation();
            // Chama método de pagamento com integração VINDI no back-end
            dados = new CartaoRecorrenteVindiDAO().setCartaoClienteVindi(entidade[0]);
            return dados;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            mProgress.setProgress(progress[0]);
        }

        @Override
        protected void onPreExecute() {
            this.mProgress = ProgressDialog.show(MenuRecorrenteCartao.this, null, "Processando...", true);
            this.mProgress.setIndeterminate(false);
            this.mProgress.setCancelable(false);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(final Object[] result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MenuRecorrenteCartao.this);
            final AlertDialog show = builder.show();
            ////// AVISO //////
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuRecorrenteCartao.this);
            alertDialogBuilder.setTitle("AVISO!");
            alertDialogBuilder
                    .setMessage(String.valueOf(result[1]))
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int idD) {
                            // Redireciona para a activty principal
                            if (Boolean.parseBoolean(String.valueOf(result[0]))) {
                                startActivity(new Intent(MenuRecorrenteCartao.this, MenuPrincipal.class));
                            }
                            show.dismiss();
                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            //////////////
            mProgress.dismiss();
        }

        private void ThreadRunningOperation() {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //                            ASYNCTASK PARA CARREGAMENTO DOS CARRTÕES                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskCarregaCartao extends AsyncTask<Object[], Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Context mContext = null;

        public AsyncTaskCarregaCartao(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Object[]... obj) {
            ThreadRunningOperation();
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
                final Animation myAnim = AnimationUtils.loadAnimation(MenuRecorrenteCartao.this, R.anim.up_from_bottom);
                // Use bounce interpolator with amplitude 0.1 and frequency 15
                myAnim.setInterpolator(new MyBounceInterpolator(0.1, 15));
                recyclerViewRoletaPlanos.startAnimation(myAnim);
                textViewMsgLista.startAnimation(myAnim);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                CartaoRecorrente cartaoRecorrente = null;
                ContratoRecorrenteAdapter.ctx = MenuRecorrenteCartao.this;
                // Botão para cadastrar um novo cartão
                buttonBotaoCadastrarNovoCartaoVindi = (Button) findViewById(R.id.buttonBotaoCadastrarNovoCartaoVindi);

                if (usuario.getCartoesRecorrentes().size() > 0) {
                    // Esconde as msgs
                    frameLayoutMsgLista.setVisibility(View.GONE);
                    textViewMsgLista.setText("");
                    // Esconde botão e prova em booleano que não se pode cadastrar novos cartões
                    SE_PODE_CADASTRAR_NOVO_CARTAO = false;
                    for (int i = 0; i < usuario.getCartoesRecorrentes().size(); i++) {
                        cartaoRecorrente = new CartaoRecorrente(
                                usuario.getCartoesRecorrentes().get(i).getIdPerfil(),
                                usuario.getCpfCnpj(),
                                usuario.getCartoesRecorrentes().get(i).getCodigoCliente(),
                                usuario.getCartoesRecorrentes().get(i).getCodContrato(),
                                usuario.getCartoesRecorrentes().get(i).getCartaoNumero(),
                                usuario.getCartoesRecorrentes().get(i).getCartaoBandeira(),
                                usuario.getCartoesRecorrentes().get(i).getCartaoDataValidade(),
                                usuario.getCartoesRecorrentes().get(i).getCartaoPlataforma(),
                                usuario.getCartoesRecorrentes().get(i).getCartaoImagemBase64Bandeira(),
                                usuario.getCartoesRecorrentes().get(i).isSeContratoVinculado(),
                                usuario.getCartoesRecorrentes().get(i).getIdClienteVindi(),
                                usuario.getCartoesRecorrentes().get(i).getIdCartaoVindi(),
                                usuario.getCartoesRecorrentes().get(i).getFkEmpresaCNPJ(),
                                MenuRecorrenteCartao.this
                        );


                        // Exibe apenas o cartão que está cadastrado/vinculado aquele plano
                        if (MenuRecorrenteCartao.COD_CODCLIE_CARTAO.equals(usuario.getCartoesRecorrentes().get(i).getCodigoCliente())) {
                            cartaoLista.add(cartaoRecorrente);
                        }
                    }
                } else {
                    // Exibe botão de cadastrar novo cartão
                    buttonBotaoCadastrarNovoCartaoVindi.setVisibility(View.VISIBLE);
                    SE_PODE_CADASTRAR_NOVO_CARTAO = true;
                    // Exibe msg caso não existe planos disponíveis/suportados
                    frameLayoutMsgLista.setVisibility(View.VISIBLE);
                    textViewMsgLista.setText("Não existem cartões cadastrados.");
                }

                if (cartaoLista.size() <= 0) {
                    // Exibe botão de cadastrar novo cartão
                    buttonBotaoCadastrarNovoCartaoVindi.setVisibility(View.VISIBLE);
                    SE_PODE_CADASTRAR_NOVO_CARTAO = true;
                    // Exibe msg caso não existe planos disponíveis/suportados
                    frameLayoutMsgLista.setVisibility(View.VISIBLE);
                    textViewMsgLista.setText("Não existem cartões cadastrados.");
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