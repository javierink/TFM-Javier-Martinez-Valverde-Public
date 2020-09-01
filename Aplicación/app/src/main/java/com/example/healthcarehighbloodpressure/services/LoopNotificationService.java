package com.example.healthcarehighbloodpressure.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.healthcarehighbloodpressure.MainActivity;
import com.example.healthcarehighbloodpressure.R;
import com.example.healthcarehighbloodpressure.omronDevice.OmronManager;

/**
 * Main service of the operation. It creates a notification and an eternal loop so that the
 * operating system does not kill the process. It also starts other services for the app features.
 */
public class LoopNotificationService extends IntentService {

    private static final String TAG = "NotificationService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public LoopNotificationService() {
        super("MasterService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");

        // Create a notification
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        Notification Notification = new NotificationCompat.Builder(this, "notificationServices")
                .setSmallIcon(R.drawable.onda)
                .setOngoing(true)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                //.setPriority(-1)
                .setContentIntent(pendingIntent).build();
        startForeground(1337, Notification);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.i(TAG, "onHandleIntent");

        // Controls the other services through the class TimingServicesManager
        if (intent != null) {
            // Eternal loop
            while (true) {
                try {
                    Thread.sleep(30000);
                    Log.d(TAG,"Notification loop");

                    // Starts a device service for the data of the wristband
                    if(TimingServicesManager.getSingletonInstance().isTimeForHealthDevice()){
                        Log.d(TAG,"Is time for take device data");

                        Intent heartIntent = new Intent(this, HealthGuideService.class);
                        HealthGuideService.enqueueWork(this, heartIntent);

                        TimingServicesManager.getSingletonInstance().setHealthDeviceTime();
                    }

                    // Starts a geo service for the location
                    if(TimingServicesManager.getSingletonInstance().isTimeForGeoTime()){
                        Log.d(TAG,"Is time for take geo data");

                        // Depending on the version of the system, one type of service is used.
                        // Due to incompatibilities with location classes.
                        // if(android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                        //     Log.d(TAG,"Modern version");
                        Intent geoIntent = new Intent(this, GeoService.class);
                        GeoService.enqueueWork(this, geoIntent);
                        // }else{
                        //    Log.d(TAG,"Oreo version");
                        //     //stopService(new Intent(getApplicationContext(), GeoServiceOldVersion.class));
                        //     Intent i= new Intent(getApplicationContext(), GeoServiceOldVersion.class);
                        //     getApplicationContext().startService(i);
                        // }

                        TimingServicesManager.getSingletonInstance().setGeoTime();
                    }

                    // Starts a data service for old data not sent to the server
                    if(TimingServicesManager.getSingletonInstance().isTimeForDataTime()){
                        Log.d(TAG,"Is time for take file data");

                        Intent dataIntent = new Intent(this, DataService.class);
                        DataService.enqueueWork(this, dataIntent);

                        TimingServicesManager.getSingletonInstance().setDataTime();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    // Notification channel for the android API +26
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification";
            String description = "Autoservice notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notificationServices", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Log.i(TAG, "Notification channel created");
        }
    }
}
