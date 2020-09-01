package com.example.healthcarehighbloodpressure.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.healthcarehighbloodpressure.omronDevice.OmronManager;
import com.example.healthcarehighbloodpressure.supportClass.ContextManager;
import com.example.healthcarehighbloodpressure.supportClass.DataManager;
import com.example.healthcarehighbloodpressure.supportClass.LoginManager;

/**
 * Service for old data, it uses the DataManager class to send old data if the files exist.
 * These files are created when measurement or survey data could not be sent, for example
 * because there was no Internet.
 */
public class DataService extends JobIntentService {

    private static final String TAG = "DataService";

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, DataService.class, 125, work);
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

        if (ContextManager.getSingletonInstance().getAppContext() == null) {
            Log.d(TAG, "Service stopped because bad status of devices or login");
            return;
        }

        if (LoginManager.getInstance().isLogin()) {

            DataManager.getInstance().sendDataDeviceFromFile();
            DataManager.getInstance().sendDataSurveyFromFile();

        } else {
            Log.d(TAG, "Not login");
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
