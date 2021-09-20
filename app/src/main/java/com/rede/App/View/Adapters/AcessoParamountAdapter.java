package com.rede.App.View.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.rede.App.View.JavaBeans.AcessoIPTV;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.ncarede.R;

import java.util.List;

/**
 * Adaptador da recyclerView dos logins de acesso às
 * plataformas de IPTV
 *
 * @author      Igor Maximo
 * @date        05/06/2021
 */
public class AcessoParamountAdapter extends RecyclerView.Adapter<AcessoParamountAdapter.PlanoViewHolder> {
    protected Ferramentas ferramenta = new Ferramentas();
    private List<AcessoIPTV> acessoTVList;
    public static Context ctx;

    public AcessoParamountAdapter(List<AcessoIPTV> planoList) {
        this.acessoTVList = planoList;
    }

    @Override
    public PlanoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_acesso_paramount_noggin, parent, false);
        return new PlanoViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(PlanoViewHolder holder, final int position) {

        holder.relativeLayoutAbrirLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(acessoTVList.get(position).getLinkApp()));
                    acessoTVList.get(position).getContext().startActivity(intent);
                } catch (Exception e) {

                }
            }
        });

        // Setando valores
        holder.textViewNomePlataforma.setText(acessoTVList.get(position).getNome());
        holder.textViewUsuarioAppTV.setText(acessoTVList.get(position).getUser());
        holder.textViewAppSenhaTV.setText(acessoTVList.get(position).getPass());
        holder.textViewDescricaoTV.setText(acessoTVList.get(position).getDescricao());

        // Efeitos de sombreamento
        ferramenta.setSombraTextView(holder.textViewNomePlataforma, 5.0f, Color.LTGRAY);
        ferramenta.setSombraTextView(holder.textViewUsuarioAppTV, 2.5f, Color.LTGRAY);
        ferramenta.setSombraTextView(holder.textViewAppSenhaTV, 5.0f, Color.LTGRAY);
        ferramenta.setSombraTextView(holder.textViewDescricaoTV, 5.0f, Color.LTGRAY);
    }

    /*
       Retorna qtd de índices da list que preenche a RecyclerView
    */
    @Override
    public int getItemCount() {
        return acessoTVList.size();
    }


    public class PlanoViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout relativeLayoutAbrirLink;
        public TextView textViewNomePlataforma;
        public TextView textViewUsuarioAppTV;
        public TextView textViewAppSenhaTV;
        public TextView textViewDescricaoTV;

        @SuppressLint("WrongViewCast")
        public PlanoViewHolder(View view) {
            super(view);
            relativeLayoutAbrirLink = (RelativeLayout) view.findViewById(R.id.relativeLayoutAbrirLink);
            textViewNomePlataforma = (TextView) view.findViewById(R.id.textViewNomePlataforma);
            textViewUsuarioAppTV = (TextView) view.findViewById(R.id.textViewUsuarioAppTV);
            textViewAppSenhaTV = (TextView) view.findViewById(R.id.textViewAppSenhaTV);
            textViewDescricaoTV = (TextView) view.findViewById(R.id.textViewDescricaoTV);
        }
    }


}
