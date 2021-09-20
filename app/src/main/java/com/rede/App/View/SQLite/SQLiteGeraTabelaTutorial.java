package com.rede.App.View.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rede.App.View.ToolBox.VariaveisGlobais;

/**
 * DDL SQLite para gerar tabela de alerta massivos e armazenar o alerta
 * RETORNO:
 * 0                      - Tabela existente
 * 1                      - Tabela criada com sucesso
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 * @editado 02/03/2019
 */
public final class SQLiteGeraTabelaTutorial extends SQLiteOpenHelper {

    private static final String ID = "_id";
    private static final String tabelaTutorial = "tutorial";

    public SQLiteGeraTabelaTutorial(Context ctx) {
        super(ctx, VariaveisGlobais.NOME_BANCO, null, VariaveisGlobais.VERSAO_DB);
    }

    public final String sqlCriaTabelaTutorial = "CREATE TABLE " + tabelaTutorial + " ("
            + ID + " integer, "
            + " lido" + " text"
            + ")";

    /**
     * On create
     *
     * @author      Igor Maximo
     * @date        31/12/2020
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(sqlCriaTabelaTutorial);
            System.err.println("Tabela Tutorial criada com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao criar tabela Tutorial !");
        }
    }

    /**
     * On upgrade
     *
     * @author      Igor Maximo
     * @date        31/12/2020
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //if (oldVersion < newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tabelaTutorial);
        try {
            onCreate(db);
            System.err.println("Tabela Dropada! " + tabelaTutorial);
        } catch (Exception e) {
            e.printStackTrace();

        }
        //}
    }

    /**
     * Cadastra a primeira credencial
     *
     * @author      Igor Maximo
     * @date        31/12/2020
     */
    public void cadastra1QuebraIncremento() throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("_id", 1);
            values.put("lido", "N");

            db.insert(tabelaTutorial, null, values);
        } catch (Exception e) {
            System.err.println("Erro cadastra1QuebraIncremento SQLite!");
            e.printStackTrace();
        }
    }

    /**
     * Retorna linhas do banco de dados SQLite
     *
     * @author      Igor Maximo
     * @date        31/12/2020
     */
    public int retornaLinhasBD(String selectQuery) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor myCursor = db.rawQuery(selectQuery, null);
        return myCursor.getCount();
    }

    /**
     * Seleciona a msg de tutorial
     *
     * @author      Igor Maximo
     * @date        31/12/2020
     */
    public String[] selectUltimoTutorial(String selectQuery, int colunas) {
        String[] vetor = new String[colunas];
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                do {
                    vetor[0] = c.getString(0);
                    vetor[1] = c.getString(1);
                } while (c.moveToNext());
            }

            c.close();
            db.close();
        } catch (Exception e) {
            System.err.println("Erro selectUltimoAlerta SQLite! ==>> " + e);
            e.printStackTrace();
        }
        return vetor;
    }

    /**
     * Atualiza opção que entendeu o tutorial
     *
     * @author      Igor Maximo
     * @date        31/12/2020
     */
    public void atualizaTutorial(String updateQuery) {
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
            System.err.println("Erro atualizaAlerta SQLite! ==>> " + e);
            e.printStackTrace();
        }
    }
}