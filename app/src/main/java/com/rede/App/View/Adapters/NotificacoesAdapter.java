package com.rede.App.View.Adapters;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.rede.App.View.DAO.NotificacaoDAO;
import com.rede.App.View.JavaBeans.Notificacao;
import com.rede.App.View.View.MenuCentralNotificacoes;
import com.rede.ncarede.R;

import java.util.List;

/**
 * Classe adaptardora da recyclerView que cria e popula
 * as notificações recebidas pelo usuário
 *
 * @author  Igor Maximo
 * @date    21/06/2021
 */
public class NotificacoesAdapter extends RecyclerView.Adapter<NotificacoesAdapter.NotificacaoViewHolder> {

    private List<Notificacao> notificacoesLista;
    private Context mContext;
    public static Context ctx;
    NotificacaoDAO notificacaoDAO = new NotificacaoDAO();

    public NotificacoesAdapter(List<Notificacao> notificacoesLista) {
        this.notificacoesLista = notificacoesLista;
    }

    @Override
    public NotificacaoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_central_notificacoes, parent, false);
        this.mContext = notificacoesLista.get(0).getContext();
        return new NotificacaoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NotificacaoViewHolder holder, final int position) {
        // Set título da notificação
        holder.textViewTituloNotificacao.setText(notificacoesLista.get(position).getDataNotificacao());
        // Set data e hora da notificação
        holder.textViewMsgNotificacao.setText(notificacoesLista.get(position).getNotificacao());
        // Define se deve exibir a caixa dos botões de SIM e NÃO
        if (notificacoesLista.get(position).getSeDependeConfirmacao() == 1) {
            holder.linearLayoutBotoesNotificacao.setVisibility(View.VISIBLE);
            // Definição dos botões do layout do card da notificação, dependendo do tipo de notificação que é
            switch (notificacoesLista.get(position).getTipoNotificacao()) {
                case "Autorização": // Autorização de realização de serviço
                    // Exibe o escopo de layout referente aos botões de SIM e NÃO
                    holder.linearLayoutBotoesNotificacao.setVisibility(View.VISIBLE);
                    // Exibe apenas o botão de SIM que será renomeado para CONFIRMAR
                    holder.relativeLayoutbuttonRecusarNotificacao.setVisibility(View.GONE);
                    // Muda texto do botão de sim para Autorizar
                    holder.textViewLayoutbuttonConfirmarNotificacaoTexto.setText("Autorizar");
                    // Para centralizar o texto do botão
                    holder.textViewLayoutbuttonConfirmarNotificacaoTexto.setPadding(3, 0, 0, 0);
                    break;
                case "Avaliação": // Avaliação de visita técnica
                    // Exibe o escopo de layout referente aos botões de SIM e NÃO
                    holder.linearLayoutBotoesNotificacao.setVisibility(View.VISIBLE);
                    // Exibe apenas o botão de SIM que será renomeado para CONFIRMAR
                    holder.relativeLayoutbuttonRecusarNotificacao.setVisibility(View.GONE);
                    // Muda texto do botão de sim para Autorizar
                    holder.textViewLayoutbuttonConfirmarNotificacaoTexto.setText("Avaliar");
                    // Para centralizar o texto do botão
                    holder.textViewLayoutbuttonConfirmarNotificacaoTexto.setPadding(3, 0, 0, 0);
                    break;
                case "Tarefa":
                    break;
            }
        } else {
            holder.linearLayoutBotoesNotificacao.setVisibility(View.GONE);
        }

        // Botão SIM
        holder.relativeLayoutbuttonConfirmarNotificacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    switch (notificacoesLista.get(position).getTipoNotificacao()) {
                        case "Avaliação Visita":
                            // Se for avaliação de visita técnica, botão sim abre o modal
                            // Abre modal de avaliação de visita técnica contido no MenuPrincipal
                            setAbrirPopUpAvaliacao(notificacoesLista.get(position).getIdNotificacao(), MenuCentralNotificacoes.CONTEXT, (Activity) MenuCentralNotificacoes.CONTEXT, "Poderia avaliar o serviço do técnico?");
                            break;
                        default:
                            new AsyncTaskMarcarConfirmacaoNotificacao(mContext).execute(new Object[]{notificacoesLista.get(position).getIdNotificacao(), notificacoesLista.get(position).getTipoNotificacao(), notificacoesLista.get(position).getProtocolo(), 1});
                            break;
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        });

        // Botão NÃO
        holder.relativeLayoutbuttonRecusarNotificacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTaskMarcarConfirmacaoNotificacao(mContext).execute(new Object[]{notificacoesLista.get(position).getIdNotificacao(), notificacoesLista.get(position).getTipoNotificacao(), notificacoesLista.get(position).getProtocolo(), 2});
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificacoesLista.size();
    }

    public class NotificacaoViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTituloNotificacao;
        public TextView textViewMsgNotificacao;
        public LinearLayout linearLayoutBotoesNotificacao;
        // Layout dos Botões
        public RelativeLayout relativeLayoutbuttonConfirmarNotificacao;
        public RelativeLayout relativeLayoutbuttonRecusarNotificacao;
        // Botões
        public TextView textViewLayoutbuttonConfirmarNotificacaoTexto;
        public TextView textViewLayoutbuttonRecusarNotificacaoTexto;

        @SuppressLint("WrongViewCast")
        public NotificacaoViewHolder(View view) {
            super(view);
            textViewTituloNotificacao = (TextView) view.findViewById(R.id.textViewTituloNotificacao);
            textViewMsgNotificacao = (TextView) view.findViewById(R.id.textViewMsgNotificacao);
            linearLayoutBotoesNotificacao = (LinearLayout) view.findViewById(R.id.linearLayoutBotoesNotificacao);
            relativeLayoutbuttonConfirmarNotificacao = (RelativeLayout) view.findViewById(R.id.relativeLayoutbuttonConfirmarNotificacao);
            relativeLayoutbuttonRecusarNotificacao = (RelativeLayout) view.findViewById(R.id.relativeLayoutbuttonRecusarNotificacao);

            textViewLayoutbuttonConfirmarNotificacaoTexto = (TextView) view.findViewById(R.id.textViewLayoutbuttonConfirmarNotificacaoTexto);
            textViewLayoutbuttonRecusarNotificacaoTexto = (TextView) view.findViewById(R.id.textViewLayoutbuttonRecusarNotificacaoTexto);

        }
    }

    /**
     * Exibe o popup de avaliação de serviços do técnico
     *
     * @author Igor Maximo
     * @data 12/08/2020
     */
    private void setAbrirPopUpAvaliacao(final int idNotificacao, Context context, final Activity activity, String texto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.card_avaliacao, null);

        // Texto de avaliação
        TextView textView = (TextView) view.findViewById(R.id.textViewPOPUPTextoAvaliacao);
        textView.setText(texto);

        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog show = builder.show();
        RelativeLayout relativeLayoutbuttonOKAvaliarTecnico = (RelativeLayout) view.findViewById(R.id.relativeLayoutbuttonOKAvaliarTecnico);
        RelativeLayout relativeLayoutbuttonNaoQueroAvaliarTecnico = (RelativeLayout) view.findViewById(R.id.relativeLayoutbuttonNaoQueroAvaliarTecnico);
        final RatingBar ratingBarAvaliartecnico = (RatingBar) view.findViewById(R.id.ratingBarAvaliartecnico);
        ratingBarAvaliartecnico.setStepSize(1.0f);

        // BOTÃO - OK, AVALIAR
        relativeLayoutbuttonOKAvaliarTecnico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
                // Grava avaliação
                if (notificacaoDAO.setGravaVotoAvaliacaoEstrelasServicoTecnico(idNotificacao, Math.round(ratingBarAvaliartecnico.getRating()))) {
                    Toast.makeText(activity, "Avaliação enviada com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Erro ao enviar avaliação!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // BOTÃO - NÃO AVALIAR
        relativeLayoutbuttonNaoQueroAvaliarTecnico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
                // Grava avaliação
                if (notificacaoDAO.setGravaVotoAvaliacaoEstrelasServicoTecnico(idNotificacao, Math.round(ratingBarAvaliartecnico.getRating()))) {
                    Toast.makeText(activity, "Obrigado!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Erro ao enviar avaliação!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Recarregar a tela após a avaliação
        ((MenuCentralNotificacoes) mContext).setPreencheListaTodasNotificacoes();
    }

    /**
     * AsyncTask para processamento de marcar SIM ou NÃO
     * na notificação do cliente
     *
     * @author Igor Maximo
     * @data 25/07/2020
     */
    private class AsyncTaskMarcarConfirmacaoNotificacao extends AsyncTask<Object[], Integer, Boolean> {
        private ProgressDialog mProgress = null;
        private int idNotificacao = 0;
        private int resposta = 0;
        private int protocolo = 0;
        private String tipoNotificacao = "";
        private Context mContext;

        public AsyncTaskMarcarConfirmacaoNotificacao(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Object[]... escolha) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                // Marca no servidor a escolha da notificação do cliente
                idNotificacao = Integer.parseInt(String.valueOf(escolha[0][0]));    // Id da notificação
                tipoNotificacao = String.valueOf(escolha[0][1]);                    // Tipo de notificação
                protocolo = Integer.parseInt(String.valueOf(escolha[0][2]));        // Protocolo
                resposta = Integer.parseInt(String.valueOf(escolha[0][3]));         // Resposta escolhida pelo cliente (1 ou 0)
                final boolean retorno = notificacaoDAO.setMarcaNotificacaoSimouNao(idNotificacao, tipoNotificacao, protocolo, resposta);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ((MenuCentralNotificacoes) mContext).setPreencheListaTodasNotificacoes();
                        if (retorno) {
                            if (resposta == 1) {
                                Toast.makeText(MenuCentralNotificacoes.CONTEXT, "Resposta SIM enviada com sucesso!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MenuCentralNotificacoes.CONTEXT, "Resposta NÃO enviada com sucesso!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MenuCentralNotificacoes.CONTEXT, "Erro ao enviar resposta!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
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
    }
}