package com.rede.App.View.ToolBox;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class LifeCycleObserver extends Application implements LifecycleObserver {

    public static Context context;

    private static int contador = 0;
    private boolean seBackground = false;

    Timer timer = null;
    TimerTask tarefa = null;


    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        Log.d("MyApp", "App in background ==>");
        seBackground = true;
        long TEMPO = (1000); // chama o método a cada 10 segundos (10000 = 10 seg)
        if (timer == null) {
            timer = new Timer();
            tarefa = new TimerTask() {
                public void run() {
                    try {
                        if (seBackground) {
                            System.err.println("COUNT ====================> " + contador);
                            contador++;
                        }
                    } catch (Exception e) {
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                }
            };
            timer.scheduleAtFixedRate(tarefa, TEMPO, TEMPO);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
        Log.d("App", "App in foreground");
        try {
            seBackground = false;
            if (contador >= VariaveisGlobais.TEMPO_MAX_EXPIRAR) { // 3 minutos de inatividade para encerrar o App
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Tempo Expirado!");
                builder.setMessage("Desculpe! Seu tempo de sessão expirou porque o app ficou minimizado por mais de um minuto. Favor reiniciar o aplicativo.");
                AlertDialog alert = builder.create();
                alert.setCancelable(false);
                alert.setCanceledOnTouchOutside(false);
                alert.show();
            }
            contador = 0;
        } catch (Exception e) {
            System.err.println(e);
        }
    }


   /* public boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }*/

}
