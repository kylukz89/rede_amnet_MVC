package com.rede.App.View.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.rede.App.View.JavaBeans.ListaAtendimento;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.View.MenuAtendimento;
import com.rede.App.View.View.MenuAtendimentoResposta;
import com.rede.ncarede.R;

import java.util.List;


/**
 * Classe adaptardora da recyclerView que cria e popula
 * as opções de atendimentos
 *
 * @author  Igor Maximo
 * @date    21/06/2021
 */
public class AtendimentoAdapter extends RecyclerView.Adapter<AtendimentoAdapter.AtendimentoListaViewHolder> {
    private List<ListaAtendimento> atendimentoList;
    public static Context ctx;

    public AtendimentoAdapter(List<ListaAtendimento> atendimentoList) {
        this.atendimentoList = atendimentoList;
    }

    @Override
    public AtendimentoListaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_atendimento, parent, false);
        return new AtendimentoListaViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(AtendimentoListaViewHolder holder, final int position) {

        holder.imageViewIconeAtendimento.setImageBitmap(atendimentoList.get(position).getIcone());
        holder.textTituloAtendimento.setText(atendimentoList.get(position).getNome());
        holder.textSubtitulo.setText(atendimentoList.get(position).getSubtitulo());
        holder.textDescricao.setText(atendimentoList.get(position).getDescricao());

        // Abre a tela com a lista de respostas para o tipo de atendimento da categoria escolhida
        holder.linearLayoutListaAtendimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reproduz o efeito de vibrar o celular
                ControladorInterface.setClickBotao(atendimentoList.get(position).getContext());
                // Carrega a tela com FK da categoria escolhida para filtrar as respostas por categoria
                MenuAtendimentoResposta.FK_CATEGORIA = atendimentoList.get(position).getfkCategoria() + "";
                MenuAtendimentoResposta.NOME_CATEGORIA_ESCOLHIDA = atendimentoList.get(position).getNome() + "";
                MenuAtendimentoResposta.CATEGORIA_ICONE_ESCOLHIDA = atendimentoList.get(position).getIcone();

                if (atendimentoList.get(position).isSeHouveIncidente()) {
                    ((MenuAtendimento) atendimentoList.get(0).getContext()).setDialogPerguntaSeDesejaProsseguirCasoIncidente(atendimentoList.get(position).getMsgIncidente(), atendimentoList.get(position).getIntent());
                } else {
                    // Abre com o context do MenuAtendimento
                    atendimentoList.get(position).getContext().startActivity(atendimentoList.get(position).getIntent());
                }
            }
        });
    }


    /*
       Retorna qtd de índices da list que preenche a RecyclerView
    */
    @Override
    public int getItemCount() {
        return atendimentoList.size();
    }


    public class AtendimentoListaViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewIconeAtendimento;
        public TextView textTituloAtendimento;
        public TextView textSubtitulo;
        public TextView textDescricao;
        public LinearLayout linearLayoutListaAtendimento;


        @SuppressLint("WrongViewCast")
        public AtendimentoListaViewHolder(View view) {
            super(view);
            imageViewIconeAtendimento = (ImageView) view.findViewById(R.id.imageViewIconeAtendimento);
            textTituloAtendimento = (TextView) view.findViewById(R.id.textTituloAtendimento);
            textSubtitulo = (TextView) view.findViewById(R.id.textSubtitulo);
            textDescricao = (TextView) view.findViewById(R.id.textDescricao);
            linearLayoutListaAtendimento = (LinearLayout) view.findViewById(R.id.linearLayoutListaAtendimento);
        }
    }
}
