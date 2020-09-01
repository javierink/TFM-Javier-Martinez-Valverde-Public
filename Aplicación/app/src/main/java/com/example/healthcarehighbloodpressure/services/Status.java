package com.example.healthcarehighbloodpressure.services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.GeneralTests;
import com.example.healthcarehighbloodpressure.MainActivity;
import com.example.healthcarehighbloodpressure.R;
import com.example.healthcarehighbloodpressure.omronDevice.OmronManager;

/**
 * Activity class for look the active service. Not in use in the app now.
 */
public class Status extends AppCompatActivity {

    private TextView editHealth;
    private TextView editGeo;
    private TextView editData;
    private TextView editDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        editHealth = (TextView) findViewById(R.id.editHealth);
        editGeo = (TextView) findViewById(R.id.editGeo);
        editData = (TextView) findViewById(R.id.editData);
        editDevice = (TextView) findViewById(R.id.editDevice);

        editHealth.setText(Boolean.toString(isMyServiceRunning(HealthGuideService.class)));
        editGeo.setText(Boolean.toString(isMyServiceRunning(GeoService.class)));
        editData.setText(Boolean.toString(isMyServiceRunning(DataService.class)));
        editDevice.setText(Boolean.toString(OmronManager.getSingletonInstance().isConnected()));

    }

    public void check(View v){

        editHealth.setText(Boolean.toString(isMyServiceRunning(HealthGuideService.class)));
        editGeo.setText(Boolean.toString(isMyServiceRunning(GeoService.class)));
        editData.setText(Boolean.toString(isMyServiceRunning(DataService.class)));
        editDevice.setText(Boolean.toString(OmronManager.getSingletonInstance().isConnected()));
    }

    /**
     * Check if the service is active.
     * @param serviceClass
     * @return
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void exitToMain(View v) {
        Intent intent = new Intent(Status.this, MainActivity.class);
        startActivity(intent);
    }
}
