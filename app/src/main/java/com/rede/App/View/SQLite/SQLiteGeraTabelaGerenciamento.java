package com.rede.App.View.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rede.App.View.ToolBox.VariaveisGlobais;

/**
 * DDL SQLite para gerar tabela gerenciamento no banco embarcado a fim de salvar as credenciais
 * RETORNO:
 * 0                      - Tabela existente
 * 1                      - Tabela criada com sucesso
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 * @editado 02/03/2019
 */

public final class SQLiteGeraTabelaGerenciamento extends SQLiteOpenHelper {

    private static final String ID = "_id";
    private static final String tabelaGerenciamento = "gerenciamento";

    Cursor cur;

    public SQLiteGeraTabelaGerenciamento(Context ctx) {
        super(ctx, VariaveisGlobais.NOME_BANCO, null, VariaveisGlobais.VERSAO_DB);
    }

    public final String sqlCriaTabelaGerenciamento = "CREATE TABLE " + tabelaGerenciamento + " ("
            + ID + " integer, "
            + " cpf_cnpj" + " text,"
            + " senha" + " text,"
            + " tipo" + " text"
            + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(sqlCriaTabelaGerenciamento);
            System.err.println("Tabela Gerenciamento criada com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao criar tabela Gerenciamento !");
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + tabelaGerenciamento);
            try {
                onCreate(db);
                System.err.println("Tabela Dropada! " + tabelaGerenciamento);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public void cadastra1QuebraIncremento() throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("_id", 1);
            values.put("cpf_cnpj", "-");
            values.put("senha", "-");
            values.put("tipo", "-");

            db.insert(tabelaGerenciamento, null, values);
        } catch (Exception e) {
            System.err.println("Erro cadastra1QuebraIncremento SQLite!");
            e.printStackTrace();
        }

    }

    public int retornaLinhasBD(String selectQuery) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor myCursor = db.rawQuery(selectQuery, null);
        return myCursor.getCount();
    }


    public String[] selectUltimoLogin(String selectQuery, int colunas) {

        String[] vetor = new String[colunas];
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    vetor[0] = c.getString(0);
                    vetor[1] = c.getString(1).replace(".", "").replace("-", "").replace("/", "");
                    vetor[2] = c.getString(2);
                    vetor[3] = c.getString(3);


                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            System.err.println("Erro selectUltimoLogin SQLite! ==>> " + e);
            e.printStackTrace();
        }
        return vetor;
    }

    public void atualizaLogin(String updateQuery) {
        try {
            cadastra1QuebraIncremento();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(updateQuery);
            db.close();
        } catch (Exception e) {
            System.err.println("Erro atualizaLogin SQLite! ==>> " + e);
            e.printStackTrace();
        }
    }


}
