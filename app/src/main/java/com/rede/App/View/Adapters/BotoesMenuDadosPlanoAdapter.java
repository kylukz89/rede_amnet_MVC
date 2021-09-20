package com.rede.App.View.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rede.App.View.JavaBeans.BotoesMenuDadosPlano;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.MenuSegundaVia;
import com.rede.ncarede.R;

import java.util.List;

/**
 * Classe responsável por adaptar os botões
 * da roleta referente ao plano
 *
 * @author      Igor Maximo
 * @date        22/01/2021
 */
public class BotoesMenuDadosPlanoAdapter extends RecyclerView.Adapter<BotoesMenuDadosPlanoAdapter.BookViewHolder> {

    private Ferramentas ferramenta = new Ferramentas();
    private List<BotoesMenuDadosPlano> botoesLista;
    private int pos;

    public BotoesMenuDadosPlanoAdapter(List<BotoesMenuDadosPlano> botoesLista) {
        this.botoesLista = botoesLista;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_menu_interno_minhaconta, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, final int position) {
        try {
            holder.imageViewIconeBotao.setImageResource(botoesLista.get(position).getIconeBotao());
            holder.textViewNomeBotao.setText(botoesLista.get(position).getNomeBotao());

            // Sombreamento do texto do botão
            ferramenta.setSombraTextView(holder.textViewNomeBotao, 2.0f, R.color.colorPrimaryDark);
            ferramenta.setSombraImageView(holder.imageViewIconeBotao);

            holder.textViewNomeBotao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Reproduz o efeito de vibrar o celular
                    ControladorInterface.setClickBotao(botoesLista.get(position).getCtx());

                    MenuSegundaVia.classeInst = "MenuDadosPlano";
                    MenuSegundaVia.classeInstCodSerCli = botoesLista.get(position).getCodSerCli();
                    MenuSegundaVia.classeNomePlano = botoesLista.get(position).getNomePlano();
                    MenuSegundaVia.classeEnderecoPlano = botoesLista.get(position).getNomeEnderecoPlano();



                    pos = position;
                    new AsyncTaskBotoesMenuDadosPlano(botoesLista.get(position).getCtx()).execute("");
                }
            });
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return botoesLista.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout relativeLayoutSegundaVia;
        public ImageView imageViewIconeBotao;
        public TextView textViewNomeBotao;

        public BookViewHolder(View view) {
            super(view);
            relativeLayoutSegundaVia = (RelativeLayout) view.findViewById(R.id.relativeLayoutSegundaVia);
            imageViewIconeBotao = (ImageView) view.findViewById(R.id.imageViewIconeBotao);
            textViewNomeBotao = (TextView) view.findViewById(R.id.textViewNomeBotao);
        }
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////// ASYNCTASK PARA PROCESSAMENTO DE PAGAMENTOS /////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private class AsyncTaskBotoesMenuDadosPlano extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Context mContext = null;
        static private final int ITERATIONS = 1;

        public AsyncTaskBotoesMenuDadosPlano(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            for (int i = 0; i < ITERATIONS; i++) {
                ThreadRunningOperation();
            }

            try {
                botoesLista.get(pos).getCtx().startActivity(botoesLista.get(pos).getIntent());
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

            }
        }
    }
}