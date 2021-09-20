package com.rede.App.View.ToolBox;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.rede.App.View.DAO.AutenticacaoDAO;
import com.rede.App.View.JavaBeans.Usuario;
import com.rede.App.View.Services.AppService;
import com.rede.App.View.View.AdesoesWebViewActivity;
import com.rede.App.View.View.MenuFaleConosco;
import com.rede.App.View.View.ScrollingSobre;

/*
    Classe que centraliza/organiza os métodos da aba lateral esquerda do APP
 */
public abstract class MenusAbaLateralEsquerda {

    // Abre tela Planos
    public static void botaoPlanos(Context ctx) {
        Toast.makeText(ctx, "Módulo em construção! ", Toast.LENGTH_SHORT).show();
    }
    // Abre tela Suporte
    public static void botaoSuporte(Context ctx) {
        Toast.makeText(ctx, "Módulo em construção! ", Toast.LENGTH_SHORT).show();
    }
    // Abre tela Serviços
    public static void botaoServicos(Context ctx) {
        Toast.makeText(ctx, "Módulo em construção! ", Toast.LENGTH_SHORT).show();
    }
    // Abre tela Fale Conosco
    public static void botaoFaleConosco(Context ctx) {
        Intent i = new Intent(ctx, MenuFaleConosco.class);
        ctx.startActivity(i);
    }
    // Abre tela Sobre
    public static void botaoSobre(Context ctx) {
        Intent i = new Intent(ctx, ScrollingSobre.class);
        ctx.startActivity(i);
    }
    // Abre tela Opções
    public static void botaoOpcoes(Context ctx) {
        Toast.makeText(ctx, "Módulo em construção! ", Toast.LENGTH_SHORT).show();
    }

    ////////////////////////////////
    // Botão (3 pontinhos) lado direito superior.
    public static void botao3Pontinhos(Context ctx) {
        Usuario usuario = new Usuario();

        AutenticacaoDAO autdao = new AutenticacaoDAO();
        if (ctx instanceof AppService) {
            try {
                autdao.registraLOGDeslogarUsuario(usuario);
            } catch (Exception e) {
                e.getStackTrace();
            }
        } else {
            try {
                autdao.registraLOGDeslogarUsuario(usuario);
            } catch (Exception e) {
                e.getStackTrace();
            }
            Ferramentas.restartAPPeApagaCredenciais(ctx);
        }
    }

    ////////////////////////////////
    // Botão (3 pontinhos) lado direito superior.
    public static Intent botaoDeslogar(Context ctx) {
        Usuario usuario = new Usuario();

        AutenticacaoDAO autdao = new AutenticacaoDAO();
        if (ctx instanceof AppService) {
            try {
                autdao.registraLOGDeslogarUsuario(usuario);
            } catch (Exception e) {
                e.getStackTrace();
            }
        } else {
            try {
                autdao.registraLOGDeslogarUsuario(usuario);
            } catch (Exception e) {
                e.getStackTrace();
            }
            Ferramentas.restartAPPeApagaCredenciais(ctx);
        }
        return null;
    }

    //////////////////////////////////
    // Apoio ao site de vendas
    public static void botaoAbreSiteVendas(Context ctx) {
        Intent i = new Intent(ctx, AdesoesWebViewActivity.class);
        ctx.startActivity(i);
    }
}
