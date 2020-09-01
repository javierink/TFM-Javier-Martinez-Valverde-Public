package com.example.healthcarehighbloodpressure.services;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.example.healthcarehighbloodpressure.omronDevice.OmronManager;
import com.example.healthcarehighbloodpressure.omronDevice.TransferDeviceManager;
import com.example.healthcarehighbloodpressure.supportClass.LoginManager;

import java.util.concurrent.ExecutionException;

/**
 * This Class is a service class to extract data of the wristband in background
 */
public class HealthGuideService extends JobIntentService {

    private static final String TAG = "HealthGuideService";

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, HealthGuideService.class, 123, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork");

        Log.d(TAG, "Context: " + OmronManager.getSingletonInstance().getAppContext());

        if (OmronManager.getSingletonInstance().getAppContext() == null){
            Log.d(TAG, "Service stopped because bad status of devices or login");
            return;
        }

        if (OmronManager.getSingletonInstance().getConnectedOmronPeripheral() != null &&
                LoginManager.getInstance().isLogin()){
            TransferDeviceManager manager = new TransferDeviceManager();
            manager.doInBackground();
        }else{
            Log.d(TAG, "Not connected device or not login");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

    }

    @Override
    public boolean onStopCurrentWork() {
        Log.d(TAG, "onStopCurrentWork");
        return super.onStopCurrentWork();
    }

}
