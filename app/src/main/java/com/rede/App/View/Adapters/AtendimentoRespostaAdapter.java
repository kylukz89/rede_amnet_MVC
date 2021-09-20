package com.rede.App.View.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rede.App.View.JavaBeans.ListaAtendimentoResposta;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.View.MenuAtendimentoResposta;
import com.rede.ncarede.R;

import java.util.List;

/**
 * Classe adaptardora da recyclerView que cria e popula
 * as opções de respostas de atendimentos
 *
 * @author  Igor Maximo
 * @date    21/06/2021
 */
public class AtendimentoRespostaAdapter extends RecyclerView.Adapter<AtendimentoRespostaAdapter.AtendimentoRespostaListaViewHolder> {
    private List<ListaAtendimentoResposta> atendimentoRespostaList;
    public static Context ctx;

    public AtendimentoRespostaAdapter(List<ListaAtendimentoResposta> atendimentoRespostaList) {
        this.atendimentoRespostaList = atendimentoRespostaList;
    }

    @Override
    public AtendimentoRespostaListaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_atendimento_resposta, parent, false);
        return new AtendimentoRespostaListaViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(AtendimentoRespostaListaViewHolder holder, final int position) {
        holder.textDescricaoResposta.setText(atendimentoRespostaList.get(position).getResposta());
        // Realiza abertura de novo atendimento
        holder.cardAtendimentoResposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reproduz o efeito de vibrar o celular
                ControladorInterface.setClickBotao(atendimentoRespostaList.get(position).getContext());
                ((MenuAtendimentoResposta) atendimentoRespostaList.get(0).getContext()).setDialogConfirmarNovoAtendimento(
                        MenuAtendimentoResposta.FK_CATEGORIA,
                        String.valueOf(atendimentoRespostaList.get(position).getFkCategoriaResposta()),
                        MenuAtendimentoResposta.COD_CONTRATO_ESCOLHIDO,
                        MenuAtendimentoResposta.COD_CONTRATO_ITEM_ESCOLHIDO,
                        atendimentoRespostaList.get(position).getResposta(),
                        atendimentoRespostaList.get(position).isSeRespostaLivre(),
                        true
                );
            }
        });
    }


    /*
       Retorna qtd de índices da list que preenche a RecyclerView
    */
    @Override
    public int getItemCount() {
        return atendimentoRespostaList.size();
    }

    public class AtendimentoRespostaListaViewHolder extends RecyclerView.ViewHolder {
        public TextView textDescricaoResposta;
        public CardView cardAtendimentoResposta;

        @SuppressLint("WrongViewCast")
        public AtendimentoRespostaListaViewHolder(View view) {
            super(view);
            textDescricaoResposta = (TextView) view.findViewById(R.id.textDescricaoResposta);
            cardAtendimentoResposta = (CardView) view.findViewById(R.id.cardAtendimentoResposta);
        }
    }
}