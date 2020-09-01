package com.example.healthcarehighbloodpressure.services;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * BroadcastReceiver for check if the notification service is stop, in that case create a new service.
 */
public class RestartServiceReceiver  extends BroadcastReceiver {

    private static final String TAG = "RestartServiceReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "RestartServiceReceiver triggered");

        boolean isOn = false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LoopNotificationService.class.equals(service.service.getClassName())) {
                isOn = true;
                Log.d(TAG, "Service on");
            }
        }

        // If the service is off, restart the service
        if(!isOn){
            Log.d(TAG, "Service off");
            Intent service = new Intent(context, LoopNotificationService.class);
            context.startService(service);
        }

    }
}
