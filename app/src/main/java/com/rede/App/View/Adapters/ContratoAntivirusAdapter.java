package com.rede.App.View.Adapters;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.rede.App.View.DAO.AppLogErroDAO;
import com.rede.App.View.JavaBeans.ContratoRoleta;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.MenuAntivirus;
import com.rede.App.View.View.Splash;
import com.rede.ncarede.R;

import java.util.List;


/**
 * Classe adaptardora da recyclerView que cria e popula
 * os contratos disponíveis para adesão do antivírus
 *
 * @author  Igor Maximo
 * @date    21/06/2021
 */
public class ContratoAntivirusAdapter extends RecyclerView.Adapter<ContratoAntivirusAdapter.PlanoViewHolder> {
    protected Ferramentas ferramenta = new Ferramentas();
    private List<ContratoRoleta> planoList;
    public static Context ctx;
    private int pos;
    Usuario usuario = new Usuario();

    public ContratoAntivirusAdapter(List<ContratoRoleta> planoList) {
        this.planoList = planoList;
    }

    @Override
    public PlanoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_planos, parent, false);
        return new PlanoViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(PlanoViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        holder.imageViewFatura.setImageResource(Ferramentas.getIconeRoletaContratoPorStatus(planoList.get(position).getStatusPlano()));
        holder.textViewStatusPlano.setText(Html.fromHtml("<span><b>" + planoList.get(position).getStatusPlano() + "</b></span>"));



        if (planoList.get(position).getStatusPlano().equals("Bloqueado")) {
            holder.textViewStatusPlano.setTextColor(ctx.getResources().getColor(R.color.colorFaturaVermelha));
            holder.textViewNomePlano.setTextColor(ctx.getResources().getColor(R.color.colorVermelhoEscuro));
        } else {
            if (planoList.get(position).getStatusPlano().equals("Em Ativação")) {
                holder.textViewStatusPlano.setTextColor(ctx.getResources().getColor(R.color.colorPrimaryDark));
                holder.textViewNomePlano.setTextColor(ctx.getResources().getColor(R.color.colorPadraoCinzaChumbo));
            } else {
                if (planoList.get(position).getStatusPlano().equals("Suspensão Temporária")) {
                    holder.textViewStatusPlano.setTextColor(ctx.getResources().getColor(R.color.colorFaturaLaranja));
                    holder.textViewNomePlano.setTextColor(ctx.getResources().getColor(R.color.colorPadraoCinzaChumbo));
                } else {
                    holder.textViewStatusPlano.setTextColor(ctx.getResources().getColor(R.color.colorFaturaVerde));
                    holder.textViewNomePlano.setTextColor(ctx.getResources().getColor(R.color.colorIconeAzulNubank));
                    if (planoList.get(position).getStatusPlano().equals("Cancelado")) {
                        holder.textViewStatusPlano.setTextColor(ctx.getResources().getColor(R.color.colorPadraoCinzaChumbo));
                        holder.textViewNomePlano.setTextColor(ctx.getResources().getColor(R.color.colorPadraoCinzaChumbo));
                    }
                }
            }
        }

        holder.textViewNomePlano.setText(Html.fromHtml("<span style='font-weight: 900; text-shadow: 5px 5px #333;'><b>" + planoList.get(position).getNomeDoPlano() + "</b></span>"));
        holder.textViewEnderecoInstalacao.setText(planoList.get(position).getEnderecoInstalacao());
        String valorFinal = planoList.get(position).getValorFinal().replace(".", ",");
        holder.textViewValor.setText(Html.fromHtml("R$ <span style='color: #000; text-shadow: 5px 5px #333;'><b>" + valorFinal.substring(0, valorFinal.indexOf(",")) + "</b>" + valorFinal.substring(valorFinal.indexOf(",")) + "</span>"));
        holder.textViewVencimento.setText(Html.fromHtml("Vence todo dia <label style='color: #000; font-weight: bold; font-family: Arial;'><b>" + planoList.get(position).getVencimentoPlano() + "</b></label> de cada mês."));

        // Botão escolha de plano
        holder.textViewEscolherPlanoAntivirus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reproduz o efeito de vibrar o celular
                ControladorInterface.setClickBotao(planoList.get(position).getCtx());
                ((MenuAntivirus) planoList.get(0).getCtx()).setDialogConfirmarAtivacaoAntivirus(planoList.get(position).getCodProd(), planoList.get(position).getCodSercli(), planoList.get(position).getCodContratoItem());
            }
        });

        // Efeitos de sombreamento
        ferramenta.setSombraTextView(holder.textViewStatusPlano, 5.0f, Color.LTGRAY);
        ferramenta.setSombraTextView(holder.textViewNomePlano, 2.5f, Color.LTGRAY);
        ferramenta.setSombraTextView(holder.textViewEnderecoInstalacao, 5.0f, Color.LTGRAY);
        ferramenta.setSombraTextView(holder.textViewValor, 5.0f, Color.LTGRAY);
        ferramenta.setSombraTextView(holder.textViewVencimento, 5.0f, Color.LTGRAY);


    }

    /*
       Retorna qtd de índices da list que preenche a RecyclerView
    */
    @Override
    public int getItemCount() {
        return planoList.size();
    }


    public class PlanoViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewFatura;
        public TextView textViewStatusPlano;
        public TextView textViewEnderecoInstalacao;
        public TextView textViewNomePlano;
        public TextView textViewValor;
        public TextView textViewVencimento;
        public TextView textViewFaixaLateralDifRoleta;
        public TextView textViewEscolherPlanoAntivirus;
        public RelativeLayout cardRoletaPlano;
        public WebView webViewPropagandaRoleta;

        @SuppressLint("WrongViewCast")
        public PlanoViewHolder(View view) {
            super(view);
            imageViewFatura = (ImageView) view.findViewById(R.id.imageViewFatura);
            textViewStatusPlano = (TextView) view.findViewById(R.id.textViewStatusPlano);
            textViewEnderecoInstalacao = (TextView) view.findViewById(R.id.textViewEnderecoInstalacao);
            textViewNomePlano = (TextView) view.findViewById(R.id.textViewNomePlano);
            textViewValor = (TextView) view.findViewById(R.id.textViewValor);
            textViewVencimento = (TextView) view.findViewById(R.id.textViewVencimento);
            textViewFaixaLateralDifRoleta = (TextView) view.findViewById(R.id.textViewFaixaLateralDifRoleta);
            textViewEscolherPlanoAntivirus = (TextView) view.findViewById(R.id.textViewEscolherPlanoAntivirus);
            cardRoletaPlano = (RelativeLayout) view.findViewById(R.id.cardRoletaPlano);
            webViewPropagandaRoleta = (WebView) view.findViewById(R.id.webViewPropagandaRoleta);
        }
    }




/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////// ASYNCTASK PARA PROCESSAMENTO DE PAGAMENTOS /////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private class AsyncTaskSegundaVia extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Context mContext = null;
        static private final int ITERATIONS = 1;

        public AsyncTaskSegundaVia(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            for (int i = 0; i < ITERATIONS; i++) {
                ThreadRunningOperation();
            }

            try {
                planoList.get(pos).getCtx().startActivity(planoList.get(pos).getIntent());
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


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}