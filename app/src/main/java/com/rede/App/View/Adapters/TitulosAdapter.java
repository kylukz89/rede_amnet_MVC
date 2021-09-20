package com.rede.App.View.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.rede.App.View.JavaBeans.Titulo;
import com.rede.App.View.ToolBox.ControladorInterface;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.App.View.ToolBox.ManipulaData;
import com.rede.App.View.View.MenuSegundaVia;
import com.rede.ncarede.R;

import java.util.List;

/**
 * Classe adaptardora da recyclerView que cria e popula
 * os títulos do usuário
 *
 * @author  Igor Maximo
 * @date    21/06/2021
 */
public class TitulosAdapter extends RecyclerView.Adapter<TitulosAdapter.FaturaViewHolder> {
    private List<Titulo> faturaLista;
    private Context mContext;
    private Animation animation;
    public static Context ctx;
    Ferramentas ferramenta = new Ferramentas();

    public TitulosAdapter(List<Titulo> faturaLista) {
        this.faturaLista = faturaLista;
    }

    @Override
    public FaturaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = faturaLista.get(0).getCtx();
        return new FaturaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_segvia_recyclerview, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(final FaturaViewHolder holder, final int position) {
        try {
            holder.listviewIconeFatura.setImageResource(retornaIconeCorFatura(faturaLista.get(position).getDias()));
            holder.listviewValorFatura.setText("R$ " + Ferramentas.setArredondaValorMoedaReal(faturaLista.get(position).getValorFaturaCorrigidoManualmente()));

            if (new ManipulaData().getDiferencaDiasEntreUmaDataAteHoje(faturaLista.get(position).getVencimentoFatura()) >= 0) {
                holder.listviewValorFatura.setTextColor(ctx.getResources().getColor(R.color.colorVermelhoEscuro));
            } else {
                holder.listviewValorFatura.setTextColor(ctx.getResources().getColor(R.color.colorLetraPreta));
            }

            if (position == 0) {
                ///// Coloca ícone
                holder.listviewVencimentoFatura.setTypeface(null, Typeface.BOLD);
                //// Animação
                animation = new AlphaAnimation(1, 0); // Altera alpha de visível a invisível
                animation.setDuration(1000); // duração - meio segundo
                animation.setInterpolator(new LinearInterpolator());
                animation.setRepeatCount(Animation.INFINITE); // Repetir infinitamente
                animation.setRepeatMode(Animation.REVERSE); //Inverte a animação no final para que o botão vá desaparecendo
                holder.listviewIconeFatura.startAnimation(animation);
            }
            holder.listviewVencimentoFatura.setText("Venc. " + (faturaLista.get(position).getVencimentoFatura()));
            holder.listviewNumFat.setText("nº " + faturaLista.get(position).getCodContratoTitulo());
            holder.listviewItensRelacionados.setText(faturaLista.get(position).getItensFatura());
            holder.textViewTipoFatura.setText(faturaLista.get(position).getTipoFatura());

            if (!Ferramentas.codCobsNaoPermitidos(faturaLista.get(position).getEmpresa())) {
                holder.textViewFlag.setImageResource(R.drawable.ic_podecartao);
            } else {
                holder.textViewFlag.setImageResource(0);
            }

            try {
                // Efeitos de sombreamento
                ferramenta.setSombraTextView(holder.listviewValorFatura, 7.0f, Color.LTGRAY);
                ferramenta.setSombraTextView(holder.listviewVencimentoFatura, 9.0f, Color.LTGRAY);
                ferramenta.setSombraTextView(holder.listviewNumFat, 9.0f, Color.LTGRAY);
                ferramenta.setSombraTextView(holder.textViewTipoFatura, 9.0f, Color.LTGRAY);
            } catch (Exception e) {
                System.out.println("Erro " + e);
            }
        } catch (Exception e) {
            System.err.println(e);
        }

        holder.relativeLayoutPagarFatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Reproduz o efeito de vibrar o celular
                    ControladorInterface.setClickBotao(faturaLista.get(position).getCtx());

                    ((MenuSegundaVia) mContext).recebeIndexFaturasAdapter(faturaLista.get(position).getIndexFatura());
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return faturaLista.size();
    }

    public class FaturaViewHolder extends RecyclerView.ViewHolder {
        public ImageView listviewIconeFatura;
        public TextView listviewValorFatura;
        public TextView listviewVencimentoFatura;
        public TextView listviewNumFat;
        public RelativeLayout relativeLayoutPagarFatura;
        public TextView listviewItensRelacionados;
        public TextView textViewTipoFatura;
        public ImageView textViewFlag;


        @SuppressLint("WrongViewCast")
        public FaturaViewHolder(View view) {
            super(view);
            listviewIconeFatura = (ImageView) view.findViewById(R.id.listviewIcone);
            listviewValorFatura = (TextView) view.findViewById(R.id.listviewValorFatura);
            listviewVencimentoFatura = (TextView) view.findViewById(R.id.listviewVencimentoFatura);
            listviewNumFat = (TextView) view.findViewById(R.id.listviewNumFat);
            relativeLayoutPagarFatura = (RelativeLayout) view.findViewById(R.id.relativeLayoutPagarFatura);
            textViewTipoFatura = (TextView) view.findViewById(R.id.textViewTipoFatura);
            listviewItensRelacionados = (TextView) view.findViewById(R.id.listviewItensRelacionados);
            textViewFlag = (ImageView) view.findViewById(R.id.textViewFlag);
        }
    }

    private int retornaIconeCorFatura(int dia) {
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