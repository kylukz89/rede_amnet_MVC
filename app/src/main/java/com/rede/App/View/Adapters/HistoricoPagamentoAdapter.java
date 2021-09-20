package com.rede.App.View.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rede.App.View.JavaBeans.HistoricoPagamento;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.ToolBox.ManipulaData;
import com.rede.App.View.View.MenuHistoricoPagamento;
import com.rede.ncarede.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Classe adaptardora da recyclerView que cria e popula
 * o histórico de pagamentos
 *
 * @author  Igor Maximo
 * @date    21/06/2021
 */
public class HistoricoPagamentoAdapter extends RecyclerView.Adapter<HistoricoPagamentoAdapter.HistoricoPagamentoViewHolder> {
    private List<HistoricoPagamento> historicoPagamentoLista;
    private Context mContext;
    private Animation animation;
    public static Context ctx;
    private Ferramentas ferramentas = new Ferramentas();
    MenuHistoricoPagamento menuHistoricoPagamento = new MenuHistoricoPagamento();

    public HistoricoPagamentoAdapter(List<HistoricoPagamento> historicoPagamentoLista) {
        this.historicoPagamentoLista = historicoPagamentoLista;
    }

    @Override
    public HistoricoPagamentoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = historicoPagamentoLista.get(0).getContext();
        return new HistoricoPagamentoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_historico_pagamento, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(final HistoricoPagamentoViewHolder holder, final int position) {
        try {
            if (position != 0) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.cardViewLinhaHistorico.getLayoutParams();
                params.topMargin = -20;
            }
            holder.cardViewLinhaHistorico.setCardBackgroundColor(Color.rgb(85, 85, 85));
            holder.imageViewDownloadComprovante.setVisibility(View.GONE);
            // Define estilizações
            if (historicoPagamentoLista.get(position).getCieloStatusCode() == 2) {
//                holder.imageViewDownloadComprovante.setVisibility(View.VISIBLE);
                // Define cores dos cards
                holder.cardViewBolinhaTituloPago.setCardBackgroundColor(Color.rgb(21, 132, 44));
                // Efeitos de sombreamento
                ferramentas.setSombraTextView(holder.textViewDataPagamento, 7.0f, Color.LTGRAY/*Color.HSVToColor(new float[]{127, 36, 100})*/);
                // Cor texto
//                holder.textViewDataPagamento.setTextColor(historicoPagamentoLista.get(0).getContext().getResources().getColor(R.color.colorFaturaVerde));
                // Define cor do extrato
                int icone = R.drawable.ic_fatura_verde;
                holder.imageViewHistoricoIcone.setImageResource(icone);
                // Exibe botão de download do comprovante
                holder.imageViewDownloadComprovante.setVisibility(View.VISIBLE);
                // Botão de download do comprovante
                holder.imageViewDownloadComprovante.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nomeArq = "comprovante_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".pdf";
                        ((MenuHistoricoPagamento) historicoPagamentoLista.get(0).getContext()).getDownloadComprovante(historicoPagamentoLista.get(position).getCompTit(), new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + nomeArq));
                        // Abre o boleto após download
                        ((MenuHistoricoPagamento) historicoPagamentoLista.get(0).getContext()).setAbrirPDFComprovante(nomeArq);
                    }
                });
            } else {
                // Define cores dos cards
                holder.cardViewBolinhaTituloPago.setCardBackgroundColor(Color.HSVToColor(new float[]{0, 29, 89}));
                // Efeitos de sombreamento
                ferramentas.setSombraTextView(holder.textViewDataPagamento, 7.0f, Color.LTGRAY/*Color.HSVToColor(new float[]{0, 29, 89})*/);
                // Cor texto
//                holder.textViewDataPagamento.setTextColor(historicoPagamentoLista.get(0).getContext().getResources().getColor(R.color.colorFaturaVermelha));
                // Define cor do extrato
                int icone = R.drawable.ic_fatura_vermelha;
                holder.imageViewHistoricoIcone.setImageResource(icone);
            }

            ferramentas.setSombraTextView(holder.listViewVencimentoTitulo, 7.0f, Color.LTGRAY);
            ferramentas.setSombraTextView(holder.textViewTipoPagamento, 7.0f, Color.LTGRAY);
            ferramentas.setSombraTextView(holder.textViewCodigoAutorizacao, 7.0f, Color.LTGRAY);
            ferramentas.setSombraTextView(holder.textViewValorPago, 7.0f, Color.LTGRAY);
            ferramentas.setSombraTextView(holder.textViewDataPagamento, 7.0f, Color.LTGRAY);

            holder.cardViewBolinhaTituloPago.setCardElevation(7);
            holder.cardViewBolinhaTituloPago.setElevation(2);
            holder.cardViewLinhaHistorico.setCardElevation(6);
            holder.cardViewLinhaHistorico.setElevation(1);
            // Preenche campos do extrato
            holder.listViewVencimentoTitulo.setText("Venc. " + new ManipulaData().converteDataFormatoBR(historicoPagamentoLista.get(position).getVencimentoFatura()));
            holder.textViewTipoPagamento.setText("Tipo pag.: " + historicoPagamentoLista.get(position).getFkFormaPagamento());
            holder.textViewCodigoAutorizacao.setText((historicoPagamentoLista.get(position).getCieloCodigoAutorizacao().length() > 1 ? "Autorização: " + historicoPagamentoLista.get(position).getCieloCodigoAutorizacao() : "Negada"));
            holder.textViewValorPago.setText("R$ " + Ferramentas.setArredondaValorMoedaReal(String.valueOf(historicoPagamentoLista.get(position).getValorCentavos() / 100)));
            holder.textViewDataPagamento.setText(new ManipulaData().converteDataFormatoBR(historicoPagamentoLista.get(position).getDataCadastro().substring(0, historicoPagamentoLista.get(position).getDataCadastro().indexOf(" "))) + " " + historicoPagamentoLista.get(position).getDataCadastro().substring(historicoPagamentoLista.get(position).getDataCadastro().indexOf(" ")).replace(" ", "\r\n  "));


        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @Override
    public int getItemCount() {
        return historicoPagamentoLista.size();
    }

    public class HistoricoPagamentoViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewHistoricoIcone;
        public ImageView imageViewDownloadComprovante;
        public CardView cardViewBolinhaTituloPago;
        public CardView cardViewLinhaHistorico;
        public TextView listViewVencimentoTitulo;
        public TextView textViewTipoPagamento;
        public TextView textViewCodigoAutorizacao;
        public TextView textViewValorPago;
        public TextView textViewDataPagamento;

        @SuppressLint("WrongViewCast")
        public HistoricoPagamentoViewHolder(View view) {
            super(view);
            imageViewHistoricoIcone = (ImageView) view.findViewById(R.id.imageViewHistoricoIcone);
            imageViewDownloadComprovante = (ImageView) view.findViewById(R.id.imageViewDownloadComprovante);
            cardViewBolinhaTituloPago = (CardView) view.findViewById(R.id.cardViewBolinhaTituloPago);
            cardViewLinhaHistorico = (CardView) view.findViewById(R.id.cardViewLinhaHistorico);
            listViewVencimentoTitulo = (TextView) view.findViewById(R.id.listViewVencimentoTitulo);
            textViewTipoPagamento = (TextView) view.findViewById(R.id.textViewTipoPagamento);
            textViewCodigoAutorizacao = (TextView) view.findViewById(R.id.textViewCodigoAutorizacao);
            textViewValorPago = (TextView) view.findViewById(R.id.textViewValorPago);
            textViewDataPagamento = (TextView) view.findViewById(R.id.textViewDataPagamento);
        }
    }

    private int retornaIconeCorFaturaP(int dia) {
        int icone = R.drawable.ic_fatura_azul;

        if (dia >= 0) {
            icone = R.drawable.ic_fatura_vermelha;
        }
        if (dia <= -1 && dia >= -30) {
            icone = R.drawable.ic_fatura_laranja;
        }
        if (dia < -30) {
            icone = R.drawable.ic_fatura_azul;
        }
        return icone;
    }
}