package com.rede.App.View.Adapters;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.rede.App.View.DAO.AppLogErroDAO;
import com.rede.App.View.JavaBeans.Contrato;
import com.rede.App.View.JavaBeans.ContratoRoleta;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.ToolBox.Internet;
import com.rede.App.View.ToolBox.MenusAbaLateralEsquerda;
import com.rede.App.View.View.AdesoesWebViewActivity;
import com.rede.App.View.View.MenuDadosPlano;
import com.rede.App.View.View.MenuPrincipal;
import com.rede.App.View.View.Splash;
import com.rede.ncarede.R;

import java.util.List;

/**
 * Classe adaptardora da recyclerView que cria e popula
 * os contratos na tela do app
 *
 * @author  Igor Maximo
 * @date    21/06/2021
 */
public class ContratoAdapter extends RecyclerView.Adapter<ContratoAdapter.ContratoViewHolder> {
    protected Ferramentas ferramenta = new Ferramentas();
    private Animation animation;
    private List<ContratoRoleta> contratoList;
    public static Context ctx;
    private int pos;
    Usuario usuario = new Usuario();
    public ContratoAdapter(List<ContratoRoleta> contratoList) {
        this.contratoList = contratoList;
    }

    @Override
    public ContratoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_roleta_planos, parent, false);
        return new ContratoViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ContratoViewHolder holder, final int position) {

        //if (0 != position && contratoList.size() - 1 > position) { // Para inserir o último index da recyclerview apontando o site de vendas da rede telecom
        if (0 != position && contratoList.size() > position) {
            holder.setIsRecyclable(false);

            holder.imageViewFatura.setImageResource(Ferramentas.getIconeRoletaContratoPorStatus(contratoList.get(position).getStatusPlano()));
            holder.textViewStatusPlano.setText(Html.fromHtml("<span><b>" + contratoList.get(position).getStatusPlano() + "</b></span>"));

            if (contratoList.get(position).getStatusPlano().equals("Bloqueado")) {
                holder.textViewStatusPlano.setTextColor(ctx.getResources().getColor(R.color.colorFaturaVermelha));
                holder.textViewNomePlano.setTextColor(ctx.getResources().getColor(R.color.colorVermelhoEscuro));
            } else {
                if (contratoList.get(position).getStatusPlano().equals("Em Ativação")) {
                    holder.textViewStatusPlano.setTextColor(ctx.getResources().getColor(R.color.colorPrimaryDark));
                    holder.textViewNomePlano.setTextColor(ctx.getResources().getColor(R.color.colorPadraoCinzaChumbo));
                } else {
                    if (contratoList.get(position).getStatusPlano().equals("Suspensão Temporária")) {
                        holder.textViewStatusPlano.setTextColor(ctx.getResources().getColor(R.color.colorFaturaLaranja));
                        holder.textViewNomePlano.setTextColor(ctx.getResources().getColor(R.color.colorPadraoCinzaChumbo));
                    } else {
                        holder.textViewStatusPlano.setTextColor(ctx.getResources().getColor(R.color.colorFaturaVerde));
                        holder.textViewNomePlano.setTextColor(ctx.getResources().getColor(R.color.colorIconeAzulNubank));
                        if (contratoList.get(position).getStatusPlano().equals("Cancelado")) {
                            holder.textViewStatusPlano.setTextColor(ctx.getResources().getColor(R.color.colorPadraoCinzaChumbo));
                            holder.textViewNomePlano.setTextColor(ctx.getResources().getColor(R.color.colorPadraoCinzaChumbo));
                        }
                    }
                }
            }

            holder.textViewNomePlano.setText(Html.fromHtml("<span style='font-weight: 900; text-shadow: 5px 5px #333;'><b>" + contratoList.get(position).getNomeDoPlano() + "</b></span>"));
            holder.textViewEnderecoInstalacao.setText(contratoList.get(position).getEnderecoInstalacao());
            String valorFinal = contratoList.get(position).getValorFinal().replace(".", ",");
            holder.textViewValor.setText(Html.fromHtml("R$ <span style='color: #000; text-shadow: 5px 5px #333;'><b>" + valorFinal.substring(0, valorFinal.indexOf(",")) + "</b>" + valorFinal.substring(valorFinal.indexOf(",")) + "</span>"));
            holder.textViewVencimento.setText(Html.fromHtml("Vence todo dia <label style='color: #000; font-weight: bold; font-family: Arial;'><b>" + contratoList.get(position).getVencimentoPlano() + "</b></label> de cada mês."));
            holder.textViewVisualizarPlano.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = position;
                    // Reproduz o efeito de vibrar o celular
                    ControladorInterface.setClickBotao(contratoList.get(position).getCtx());
                    // Verifica a conectiviade antes
                    if (new Internet(MenuPrincipal.CTX).verificaConexaoInternet()) {
                        new AsyncTaskCarregarTela(contratoList.get(position).getCtx()).execute(new String[]{""}); // Método Assíncrono para processar habilitação provisória
                        MenuDadosPlano.idRoleta = position;
                        new Contrato().setCodserCli(contratoList.get(position).getCodSercli());
                    } else {
                        Toast.makeText(MenuPrincipal.CTX, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Efeitos de sombreamento
            ferramenta.setSombraTextView(holder.textViewStatusPlano, 5.0f, Color.LTGRAY);
            ferramenta.setSombraTextView(holder.textViewNomePlano, 2.5f, Color.LTGRAY);
            ferramenta.setSombraTextView(holder.textViewEnderecoInstalacao, 5.0f, Color.LTGRAY);
            ferramenta.setSombraTextView(holder.textViewValor, 5.0f, Color.LTGRAY);
            ferramenta.setSombraTextView(holder.textViewVencimento, 5.0f, Color.LTGRAY);
            ferramenta.setSombraTextView(holder.textViewVisualizarPlano, 5.0f, Color.LTGRAY);
        } else {

            /////////////////////////////////////////////////////////////////////
            //                     CARTÃO SLIDER PROPAGANDAS                   //
            /////////////////////////////////////////////////////////////////////

            if (0 == position) {
                holder.imageViewFatura.setVisibility(View.GONE);
                holder.textViewStatusPlano.setVisibility(View.GONE);
                holder.textViewNomePlano.setVisibility(View.GONE);
                holder.textViewNomePlano.setVisibility(View.GONE);
                holder.textViewNomePlano.setVisibility(View.GONE);
                holder.textViewEnderecoInstalacao.setVisibility(View.GONE);
                holder.textViewVencimento.setVisibility(View.GONE);
                holder.textViewValor.setVisibility(View.GONE);
                holder.textViewVisualizarPlano.setVisibility(View.GONE);
                holder.relativeLayoutDetalhesPlano.setVisibility(View.GONE);

                // Carrega propaganda - Telecom
                holder.textViewFaixaLateralDifRoleta.setVisibility(View.INVISIBLE);
                holder.webViewPropagandaRoleta.setVisibility(View.VISIBLE);
                holder.webViewPropagandaRoleta.setInitialScale(1);
                holder.webViewPropagandaRoleta.setWebChromeClient(new WebChromeClient());
                holder.webViewPropagandaRoleta.getSettings().setAllowFileAccess(true);
                holder.webViewPropagandaRoleta.getSettings().setPluginState(WebSettings.PluginState.ON);
                holder.webViewPropagandaRoleta.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
                holder.webViewPropagandaRoleta.setWebViewClient(new WebViewClient());
                holder.webViewPropagandaRoleta.getSettings().setJavaScriptEnabled(true);
                holder.webViewPropagandaRoleta.getSettings().setLoadWithOverviewMode(true);
                holder.webViewPropagandaRoleta.getSettings().setUseWideViewPort(true);
                holder.webViewPropagandaRoleta.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                // Carrega link da propaganda
                holder.webViewPropagandaRoleta.postUrl("http://187.95.0.22/producao/central/propaganda/scp.php", null);
                // Redireciona para o site de vendas
            }
        }

        try {
            // Animação
            animation = new AlphaAnimation(0.3f, 1); // Altera alpha de visível a invisível
            animation.setDuration(1000); // duração - meio segundo
            animation.setInterpolator(new DecelerateInterpolator());
            animation.setRepeatCount(Animation.INFINITE); // Repetir infinitamente
            animation.setRepeatMode(Animation.REVERSE); //Inverte a animação no final para que o botão vá desaparecendo
            holder.textViewVisualizarPlano.startAnimation(animation);
        } catch (Exception e) {
            System.out.println(e);
        }

        if (position == 0) {
            holder.webViewPropagandaRoleta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (new Internet(MenuPrincipal.CTX).verificaConexaoInternet()) {
                        // Site de vendas
                        MenusAbaLateralEsquerda.botaoAbreSiteVendas(contratoList.get(position).getCtx());
                        contratoList.get(position).getCtx().startActivity(new Intent(contratoList.get(position).getCtx(), AdesoesWebViewActivity.class));
                    } else {
                        Toast.makeText(MenuPrincipal.CTX, "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /*
       Retorna qtd de índices da list que preenche a RecyclerView
    */
    @Override
    public int getItemCount() {
        return contratoList.size();
    }


    public class ContratoViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewFatura;
        public TextView textViewStatusPlano;
        public TextView textViewEnderecoInstalacao;
        public TextView textViewNomePlano;
        public TextView textViewValor;
        public TextView textViewVencimento;
        public TextView textViewVisualizarPlano;
        public TextView textViewFaixaLateralDifRoleta;
        public RelativeLayout relativeLayoutDetalhesPlano;
        public RelativeLayout cardRoletaPlano;
        public WebView webViewPropagandaRoleta;

        @SuppressLint("WrongViewCast")
        public ContratoViewHolder(View view) {
            super(view);
            imageViewFatura = (ImageView) view.findViewById(R.id.imageViewFatura);
            textViewStatusPlano = (TextView) view.findViewById(R.id.textViewStatusPlano);
            textViewEnderecoInstalacao = (TextView) view.findViewById(R.id.textViewEnderecoInstalacao);
            textViewNomePlano = (TextView) view.findViewById(R.id.textViewNomePlano);
            textViewValor = (TextView) view.findViewById(R.id.textViewValor);
            textViewVencimento = (TextView) view.findViewById(R.id.textViewVencimento);
            textViewVisualizarPlano = (TextView) view.findViewById(R.id.textViewVisualizarPlano);
            textViewFaixaLateralDifRoleta = (TextView) view.findViewById(R.id.textViewFaixaLateralDifRoleta);
            relativeLayoutDetalhesPlano = (RelativeLayout) view.findViewById(R.id.relativeLayoutDetalhesPlano);
            cardRoletaPlano = (RelativeLayout) view.findViewById(R.id.cardRoletaPlano);
            webViewPropagandaRoleta = (WebView) view.findViewById(R.id.webViewPropagandaRoleta);
        }
    }

    /**
     * Classe private para abrir outra tela do app
     *
     * @author  Igor Maximo
     * @date    21/06/2021
     */
    private class AsyncTaskCarregarTela extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Context mContext = null;
        static private final int ITERATIONS = 1;

        public AsyncTaskCarregarTela(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            for (int i = 0; i < ITERATIONS; i++) {
                ThreadRunningOperation();
            }

            try {
                // Carrega outra tela com spinner
                contratoList.get(pos).getCtx().startActivity(contratoList.get(pos).getIntent());
            } catch (Exception e) {
                AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
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
            mProgress.dismiss();
        }

        private void ThreadRunningOperation() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                AppLogErroDAO.gravaErroLOGServidor(usuario.getTipoCliente(), e.toString(), usuario.getCodigo(), Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
            }
        }
    }
}