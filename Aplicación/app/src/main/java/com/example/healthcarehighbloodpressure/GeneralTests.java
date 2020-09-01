package com.example.healthcarehighbloodpressure;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.services.DataService;
import com.example.healthcarehighbloodpressure.services.GeoService;
import com.example.healthcarehighbloodpressure.services.HealthGuideService;
import com.example.healthcarehighbloodpressure.supportClass.DataManager;
import com.example.healthcarehighbloodpressure.supportClass.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Activity class for little tests, not important. Not visible in a normal apk version.
 */
public class GeneralTests extends AppCompatActivity {
    private static final String TAG = "GeneralTests";

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_tests);
        text = (TextView) findViewById(R.id.textView);
    }

    public void save(View v){
        // Creates a json file with the information for the logging
        JSONObject deviceJson = new JSONObject();
        double ml = 1588669666213.0;

        try {
            deviceJson.put("username", LoginManager.getInstance().getUsername());
            deviceJson.put("datetime", ml);
            deviceJson.put("pulse", 71);
            deviceJson.put("pressuresys", 117);
            deviceJson.put("pressuredia", 71);
            deviceJson.put("steps", 7500);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i(TAG, "json: " + deviceJson.toString());
        DataManager.getInstance().saveDataDeviceToFile(deviceJson);

        ml = 1588669667567.0;
        try {
            deviceJson.put("username", LoginManager.getInstance().getUsername());
            deviceJson.put("datetime", ml);
            deviceJson.put("pulse", 67);
            deviceJson.put("pressuresys", 115);
            deviceJson.put("pressuredia", 69);
            deviceJson.put("steps", 8000);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i(TAG, "json: " + deviceJson.toString());
        DataManager.getInstance().saveDataDeviceToFile(deviceJson);

        // Creates a json file with the information for the logging
        deviceJson = new JSONObject();
        ml = 1588669666213.0;

        try {
            deviceJson.put("username", LoginManager.getInstance().getUsername());
            deviceJson.put("datetime", ml);
            deviceJson.put("type", 1);
            deviceJson.put("q1", 4);
            deviceJson.put("q2", 3);
            deviceJson.put("q3", 5);
            deviceJson.put("q4", 3);
            deviceJson.put("q5", 4);
            deviceJson.put("q6", 5);
            deviceJson.put("q7", 4);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i(TAG, "json: " + deviceJson.toString());
        DataManager.getInstance().saveDataSurveyToFile(deviceJson);

        ml = 1588669667567.0;
        try {
            deviceJson.put("username", LoginManager.getInstance().getUsername());
            deviceJson.put("datetime", ml);
            deviceJson.put("type", 1);
            deviceJson.put("q1", 4);
            deviceJson.put("q2", 33);
            deviceJson.put("q3", 55);
            deviceJson.put("q4", 443);
            deviceJson.put("q5", 4);
            deviceJson.put("q6", 5);
            deviceJson.put("q7", 4);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i(TAG, "json: " + deviceJson.toString());
        DataManager.getInstance().saveDataSurveyToFile(deviceJson);
    }

    public void send(View v){

        DataManager.getInstance().sendDataDeviceFromFile();
    }

    public void service(View v){

        isMyServiceRunning(HealthGuideService.class);

        text.setText("Service:" + Boolean.toString(isMyServiceRunning(HealthGuideService.class)));
    }

    public void sha(View v){

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest("16283892black".getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            Log.d(TAG, "SHA: " + sb);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void sendDataSurvey(View v) {
        DataManager.getInstance(). sendDataSurveyFromFile();
        DataManager.getInstance(). sendDataDeviceFromFile();
    }

    public void serviceHeart(View v) {
        Intent serIntent = new Intent(this, HealthGuideService.class);
        HealthGuideService.enqueueWork(this, serIntent);
    }

    public void serviceData(View v) {
        Intent serIntent = new Intent(this, DataService.class);
        DataService.enqueueWork(this, serIntent);
    }

    public void serviceGeo(View v) {
        Intent serIntent = new Intent(this, GeoService.class);
        GeoService.enqueueWork(this, serIntent);
    }

    public void exitToMain(View v) {
        Intent intent = new Intent(GeneralTests.this, MainActivity.class);
        startActivity(intent);
    }
}
