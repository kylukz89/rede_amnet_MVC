package com.rede.App.View.Adapters;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.rede.App.View.DAO.UsuarioDAO;
import com.rede.App.View.JavaBeans.BotoesMenuPrincipal;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.MenusAbaLateralEsquerda;
import com.rede.ncarede.R;

import java.util.List;


/**
 * Classe adaptardora da recyclerView que cria e popula
 * os botões do menu da tela principal
 *
 * @author  Igor Maximo
 * @date    21/06/2021
 */
public class BotoesMenuPrincipalAdapter extends RecyclerView.Adapter<BotoesMenuPrincipalAdapter.BookViewHolder> {
    final Usuario usuario = new Usuario();
    final UsuarioDAO usuDao = new UsuarioDAO();

    private List<BotoesMenuPrincipal> botoesLista;
    private int pos;

    public BotoesMenuPrincipalAdapter(List<BotoesMenuPrincipal> botoesLista) {
        this.botoesLista = botoesLista;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_botao_padrao_menuprincipal, parent, false);
        return new BookViewHolder(itemView);
    }

    public Object getItem(int position) {
        return botoesLista.get(position);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, final int position) {
        holder.imageViewIconeFlag.setImageResource(botoesLista.get(position).getIconeBotaoFlag());
        holder.imageViewIconeBotao.setImageResource(botoesLista.get(position).getIconeBotao());
        holder.textViewNomeBotao.setText(botoesLista.get(position).getNomeBotao());
        // Pega o evento de algum botão da tela
        holder.textViewNomeBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reproduz o efeito de vibrar o celular
                ControladorInterface.setClickBotao(botoesLista.get(position).getCtx());

                if (botoesLista.get(position).getNomeBotao().toUpperCase().contains("INDICAR")) {
                    try {
                        ShareCompat.IntentBuilder.from((Activity) (botoesLista.get(position).getCtx()))
                                .setType("text/plain")
                                .setChooserTitle("Indicar para um amigo")
                                .setText("http://play.google.com/store/apps/details?id=" + botoesLista.get(position).getCtx().getPackageName())
                                .startChooser();
                    } catch (Exception e) {

                    }
                }
                if (position == botoesLista.size() - 1) {
                    MenusAbaLateralEsquerda.botao3Pontinhos(botoesLista.get(position).getCtx());
                }
                pos  = position;
                new AsyncTaskBotoesMenuPrincipal(botoesLista.get(position).getCtx()).execute("");
            }
        });
    }

    @Override
    public int getItemCount() {
        return botoesLista.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout relativeLayoutSegundaVia;
        public ImageView imageViewIconeBotao;
        public ImageView imageViewIconeFlag;
        public TextView textViewNomeBotao;

        public BookViewHolder(View view) {
            super(view);

            relativeLayoutSegundaVia = (RelativeLayout) view.findViewById(R.id.relativeLayoutSegundaVia);
            imageViewIconeFlag = (ImageView) view.findViewById(R.id.imageViewIconeFlag);
            imageViewIconeBotao = (ImageView) view.findViewById(R.id.imageViewIconeBotao);
            textViewNomeBotao = (TextView) view.findViewById(R.id.textViewNomeBotao);
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////// ASYNCTASK PARA PROCESSAMENTO DE PAGAMENTOS /////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Classe responsável pelo carregamentos assíncronos
     * do botões para transição de tela...
     *
     * @author      Igor Maximo
     * @date        31/12/2020
     */
    private class AsyncTaskBotoesMenuPrincipal extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private Context mContext = null;
        static private final int ITERATIONS = 1;

        public AsyncTaskBotoesMenuPrincipal(Context context) {
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
            //mProgress.setTitle("Pagamento");
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

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}