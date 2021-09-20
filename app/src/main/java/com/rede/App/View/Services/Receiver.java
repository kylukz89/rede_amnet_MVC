package com.rede.App.View.Services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.content.ContextCompat;
import com.rede.App.View.View.Splash;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent startIntent = new Intent(Splash.ctx, CustomService.class);
        startIntent.addFlags(Integer.parseInt(IntentService.CONNECTIVITY_SERVICE));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(Splash.ctx, startIntent);
        } else {
            Splash.ctx.startService(startIntent);
        }

    }

}