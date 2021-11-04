package com.rede.App.View.View;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.judemanutd.autostarter.AutoStartPermissionHelper;
import com.rede.App.View.DAO.AppLogErroDAO;
import com.rede.App.View.ToolBox.Animatoo;
import com.rede.App.View.ToolBox.Ferramentas;
import com.rede.ncarede.R;




/**
 * Exibe animação com logo da rede no app
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 * @editado 02/03/2019
 */
public class Splash extends AppCompatActivity implements LifecycleObserver {

    //////////////// Inicia o construtor da ThreadIM ////////////////
    public static Context ctx;
    /////////////////////////////////////////////////////////////////
    int segundos = 1500;

    public static Context getContext() {
        return ctx;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); // remove barra de título
        getWindow().getDecorView();
        setContentView(R.layout.activity_splash);

        try {
            Animatoo.animateZoom(this);
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor("-", "Splash "+e.toString(), "0", Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }

//        Button crashButton = new Button(this);
//        crashButton.setText("Test Crash");
//        crashButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                throw new RuntimeException("Test Crash"); // Force a crash
//            }
//        });
//
//        addContentView(crashButton, new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));

        ////////////////////////////////////////////////////////
        //////////////////////// 2º PLANO //////////////////////
        ////////////////////////////////////////////////////////

        try {
            String manufacturer = "xiaomi";
            if (manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
                //this will open auto start screen where user can enable permission for your app
                Intent intent1 = new Intent();
                intent1.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                //startActivity(intent1);
            } else {
                AutoStartPermissionHelper.getInstance().getAutoStartPermission(Splash.this);
            }
            //AutoStartPermissionHelper.getInstance().getAutoStartPermission(Splash.this);
            ////////////////////////////////////////////////////////
            ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
            Thread geradorThread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(segundos); // Tempo de sp
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        Animatoo.animateZoom(Splash.this);  //fire the zoom animation
                    }
                }
            };
            geradorThread.start();
        } catch (Exception e) {
            AppLogErroDAO.gravaErroLOGServidor("-", "Splash "+e.toString(), "0", Ferramentas.getMarcaModeloDispositivo(Splash.ctx));
        }
    }
}




