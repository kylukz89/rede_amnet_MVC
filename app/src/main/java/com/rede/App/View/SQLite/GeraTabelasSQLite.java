package com.rede.App.View.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rede.App.View.ToolBox.VariaveisGlobais;

/**
 * DDL SQLite para gerar cadeia de tabelas no banco embarcado
 * RETORNO:
 * 0                      - Tabela existente
 * 1                      - Tabela criada com sucesso
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 * @editado 02/03/2019
 */

public class GeraTabelasSQLite extends SQLiteOpenHelper {

    private static final String ID = "_id";

    private static final String tabelaGerenciamento = "gerenciamento";
    private static final String tabelaAlertasMassivos = "alertas_massivos";
    private static final String tabelaAvisosFaturasAVencer = "avisos_faturas";
    private static final String tabelaAlertaAgradecimento = "alerta_agradecimento";
    private static final String tabelaTutorial = "tutorial";
    private static final String tabelaAlertaIndividualCliente = "alerta_cliente";

    public final String sqlCriaTabelaGerenciamento = "CREATE TABLE " + tabelaGerenciamento + " ("
            + ID + " integer, "
            + " cpf_cnpj" + " text,"
            + " senha" + " text,"
            + " tipo" + " text"
            + ")";
    public final String sqlCriaTabelaAlertasMassivos = "CREATE TABLE " + tabelaAlertasMassivos + " ("
            + ID + " integer, "
            + " alerta" + " text,"
            + " lido" + " text"
            + ")";
    public final String sqlCriaTabelaAvisosFaturasAVencer = "CREATE TABLE " + tabelaAvisosFaturasAVencer + " ("
            + ID + " integer, "
            + " alerta" + " text,"
            + " lido" + " text"
            + ")";
    public final String sqlCriaTabelaAlertaAgradecimento = "CREATE TABLE " + tabelaAlertaAgradecimento + " ("
            + ID + " integer, "
            + " alerta" + " text,"
            + " lido" + " text"
            + ")";
    public final String sqlCriaTabelaTutorial = "CREATE TABLE " + tabelaTutorial + " ("
            + ID + " integer, "
            + " lido" + " text"
            + ")";
    public final String sqlCriaTabelaAlertaIndividualCliente = "CREATE TABLE " + tabelaAlertaIndividualCliente + " ("
            + ID + " integer, "
            + " alerta" + " text,"
            + " codcli" + " text"
            + ")";

    private static GeraTabelasSQLite instance;

    public static synchronized GeraTabelasSQLite getHelper(Context context) {
        if (instance == null)
            instance = new GeraTabelasSQLite(context);
        return instance;
    }

    public GeraTabelasSQLite(Context context) {
        super(context, VariaveisGlobais.NOME_BANCO, null, VariaveisGlobais.VERSAO_DB);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(sqlCriaTabelaGerenciamento);
            System.err.println("Tabela Gerenciamento criada com sucesso! SQLiteGeraTabelaGerenciamento " + sqlCriaTabelaGerenciamento);

            db.execSQL(sqlCriaTabelaAlertasMassivos);
            System.err.println("Tabela Alertas Massivos criada com sucesso! SQLiteTabelaAlertasMassivos " + sqlCriaTabelaAlertasMassivos);

            db.execSQL(sqlCriaTabelaAvisosFaturasAVencer);
            System.err.println("Tabela Faturas a Vencer criada com sucesso! SQLiteGeraTabelaAvisosFaturas " + sqlCriaTabelaAvisosFaturasAVencer);

            db.execSQL(sqlCriaTabelaAlertaAgradecimento);
            System.err.println("Tabela Aivoss Agradecimento criada com sucesso! SQLiteGeraTabelaAlertaAgradecimento " + sqlCriaTabelaAlertaAgradecimento);

            db.execSQL(sqlCriaTabelaTutorial);
            System.err.println("Tabela Tutorial criada com sucesso! SQLiteGeraTabelaTutorial " + sqlCriaTabelaTutorial);

            db.execSQL(sqlCriaTabelaAlertaIndividualCliente);
            System.err.println("Tabela Tutorial criada com sucesso! SQLiteGeraTabelaAlertaInvididualCliente " + sqlCriaTabelaAlertaIndividualCliente);
        } catch (Exception e) {
            System.err.println("Erro ao criar tabelas  - SQLiteGeraTabela !");
        }
    }

    @Override
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
}