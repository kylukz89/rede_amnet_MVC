package com.rede.App.View.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.rede.App.View.JavaBeans.CartaoRecorrente;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.View.MenuRecorrenteCartao;
import com.rede.ncarede.R;

import java.util.List;


/**
 * Classe adaptardora da recyclerView que cria e popula
 * os cartões de crédito cadastrados na VINDI
 *
 * @author  Igor Maximo
 * @date    21/06/2021
 */
public class CartaoRecorrenteAdapter extends RecyclerView.Adapter<CartaoRecorrenteAdapter.CartaoRecorrenteViewHolder> {
    protected Ferramentas ferramenta = new Ferramentas();
    private List<CartaoRecorrente> cartaoList;
    public static Context ctx;

    public CartaoRecorrenteAdapter(List<CartaoRecorrente> cartaoList) {
        this.cartaoList = cartaoList;
    }

    @Override
    public CartaoRecorrenteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cartao_recorrente, parent, false);
        return new CartaoRecorrenteViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(CartaoRecorrenteViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.imageViewBandeiraRecorrente.setImageResource(R.drawable.pngchip);

        // Bandeira padrão definida
        holder.imageViewBandeiraRecorrente.setImageResource(R.drawable.vindi);
        // Define logo da bandeira
        if (cartaoList.get(position).getCartaoBandeira().toUpperCase().contains("VISA")) {
            holder.imageViewBandeiraRecorrente.setImageResource(R.drawable.visa_logo_);
        }
        if (cartaoList.get(position).getCartaoBandeira().toUpperCase().contains("MASTER")) {
            holder.imageViewBandeiraRecorrente.setImageResource(R.drawable.vindi_master);
        }
        if (cartaoList.get(position).getCartaoBandeira().toUpperCase().contains("ELO")) {
            holder.imageViewBandeiraRecorrente.setImageResource(R.drawable.vindi_elo);
        }
        if (cartaoList.get(position).getCartaoBandeira().toUpperCase().contains("AMERICA")) {
            holder.imageViewBandeiraRecorrente.setImageResource(R.drawable.vindi_america);
        }

        // Preenche valores dos cartões existentes
        holder.textViewNumeroCartaoRecorrente.setText("**** **** **** " + (cartaoList.get(position).getCartaoNumero().replace("*", "")));
        holder.textViewNomeCartaoRecorrente.setText("Validade");
        holder.textViewVencimentoCartaoRecorrente.setText(cartaoList.get(position).getCartaoDataValidade().substring(0, 2) + "/" + cartaoList.get(position).getCartaoDataValidade().substring(2));

        // Para cliente selecionar um cartão existente e poder alterá-lo
        holder.cardCartaoRecorrente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reproduz o efeito de vibrar o celular
                ControladorInterface.setClickBotao(cartaoList.get(position).getContext());
                // Abre dialog de edição de dados do cartão
                ((MenuRecorrenteCartao) cartaoList.get(0).getContext()).setGeraDialogEditorFormCartao(false);
            }
        });

        // Efeitos de sombreamento
        ferramenta.setSombraTextView(holder.textViewNumeroCartaoRecorrente, 9.0f, Color.LTGRAY);
        ferramenta.setSombraTextView(holder.textViewNomeCartaoRecorrente, 9.5f, Color.LTGRAY);
        ferramenta.setSombraTextView(holder.textViewVencimentoCartaoRecorrente, 9.0f, Color.LTGRAY);
    }

    /*
       Retorna qtd de índices da list que preenche a RecyclerView
    */
    @Override
    public int getItemCount() {
        return cartaoList.size();
    }


    public class CartaoRecorrenteViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewBandeiraRecorrente;
        public TextView textViewNumeroCartaoRecorrente;
        public TextView textViewNomeCartaoRecorrente;
        public TextView textViewVencimentoCartaoRecorrente;
        public RelativeLayout cardCartaoRecorrente;

        @SuppressLint("WrongViewCast")
        public CartaoRecorrenteViewHolder(View view) {
            super(view);
            imageViewBandeiraRecorrente = (ImageView) view.findViewById(R.id.imageViewBandeiraRecorrente);
            textViewNumeroCartaoRecorrente = (TextView) view.findViewById(R.id.textViewNumeroCartaoRecorrente);
            textViewNomeCartaoRecorrente = (TextView) view.findViewById(R.id.textViewNomeCartaoRecorrente);
            textViewVencimentoCartaoRecorrente = (TextView) view.findViewById(R.id.textViewVencimentoCartaoRecorrente);
            cardCartaoRecorrente = (RelativeLayout) view.findViewById(R.id.cardCartaoRecorrente);
        }
    }
}