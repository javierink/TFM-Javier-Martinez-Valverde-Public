package com.example.healthcarehighbloodpressure;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.healthcarehighbloodpressure.omronDevice.OmronManager;
import com.example.healthcarehighbloodpressure.omronDevice.PersonalConfig;
import com.example.healthcarehighbloodpressure.services.LoopNotificationService;
import com.example.healthcarehighbloodpressure.services.Status;
import com.example.healthcarehighbloodpressure.supportClass.ContextManager;
import com.example.healthcarehighbloodpressure.supportClass.LoginManager;
import com.example.healthcarehighbloodpressure.supportClass.Storage;
import com.example.healthcarehighbloodpressure.surveys.Survey_select;
import com.example.healthcarehighbloodpressure.user_data.Login;
import com.example.healthcarehighbloodpressure.user_data.LoginDataView;
import com.example.healthcarehighbloodpressure.user_data.Register;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.LibraryManager.OmronPeripheralManager;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.OmronUtility.OmronConstants;

import org.json.JSONObject;

/**
 * Activity main of the app
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button btnRegister;
    private Button btnLogin;
    private Button btnLogout;

    private Button btnSurveys;
    private Button btnUser;
    private Button btnTests;
    private Button btnWristband;
    private Button btnStatus;
    private Button btnManual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //We get a reference to the interface controls
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogout = (Button)findViewById(R.id.btnLogout);
        btnSurveys = (Button)findViewById(R.id.btnSurveys);
        btnUser = (Button)findViewById(R.id.btnUser);
        btnTests = (Button)findViewById(R.id.btnTests);
        btnWristband = (Button)findViewById(R.id.btnWristband);
        btnStatus = (Button)findViewById(R.id.btnStatus);
        btnManual = (Button)findViewById(R.id.btnManual);

        // If the app is delete of the memory, relaunch the loads of singleton class among other things.
        if(!ContextManager.getSingletonInstance().isAppContext()){
            preLoads();
        }

        if(LoginManager.getInstance().isLogin()){
            btnRegister.setVisibility(View.GONE);
            btnLogin.setVisibility(View.GONE);

        } else {
            btnLogout.setVisibility(View.GONE);

            btnSurveys.setVisibility(View.GONE);
            btnUser.setVisibility(View.GONE);
            btnTests.setVisibility(View.GONE);
            btnWristband.setVisibility(View.GONE);
            btnStatus.setVisibility(View.GONE);
            btnManual.setVisibility(View.GONE);

        }

        //Permissions to ignore battery optimization
        String packageName = this.getPackageName();
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            //startActivity(new Intent().setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));
            intent.setData(Uri.parse("package:" + packageName));
            this.startActivity(intent);
        }

        //Permissions to get the gps access
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 0);
            }
        }


        boolean isOn = false;

        // Check if the notification service is on.
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LoopNotificationService.class.getName().equals(service.service.getClassName())) {
                isOn =  true;
            }
        }

        // If notification service isn't on, starts one instance.
        if(!isOn){
            Intent serIntentNotificacion=new Intent(this,LoopNotificationService.class);
            PendingIntent alarmIntentNotificacion=PendingIntent.getService(getApplicationContext(),1,serIntentNotificacion,0);
            AlarmManager alarmSerNotificacion=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmSerNotificacion.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()+20000,alarmIntentNotificacion);

            Log.d(TAG, "Start service");
        }else{
            Log.d(TAG, "Service started yet");
        }

    }

    public void preLoads(){
        Storage storage = new Storage();

        // Check if exist a file witch the data log
        if(storage.fileExists(getString(R.string.fileNamePersonalLoginInfo), this)){
            String jsonData = storage.readFromFile(getString(R.string.fileNamePersonalLoginInfo), this);

            try {
                JSONObject jsonObj = new JSONObject(jsonData);
                LoginManager.getInstance().setPersonalDataJson(jsonObj);

                Log.i(TAG,"Login with username: " + LoginManager.getInstance().getUsername());

            } catch (Throwable t) {
                Log.i(TAG, "Could not parse malformed JSON: \"" + jsonData + "\"");
            }
        }else{
            Log.i(TAG, "Personal info data file not found");
        }

        /**
         * It includes the key of the sdk and wraps a message so that it viasualice information of the identification by the terminal.
         * It should only run once, so I only pick up the context once.
         * */
        if(!OmronManager.getSingletonInstance().isAppContext()){
            OmronPeripheralManager.sharedManager(this).setAPIKey("NoDisponibleEnVersionPublica", null);
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(OmronConstants.OMRONBLEConfigDeviceAvailabilityNotification));

            OmronManager.getSingletonInstance().setAppContext(this);
        }

        ContextManager.getSingletonInstance().setAppContext(this);

    }

    /**
     * onReceiver message that checks if the sdk key has been included correctly.
     */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            final int status = intent.getIntExtra(OmronConstants.OMRONConfigurationStatusKey, 0);

            if(status == OmronConstants.OMRONConfigurationStatus.OMRONConfigurationFileSuccess) {

                Log.d(TAG, "Config File Extract Success");

            }else if(status == OmronConstants.OMRONConfigurationStatus.OMRONConfigurationFileError) {

                Log.d(TAG, "Config File Extract Failure");

            }else if(status == OmronConstants.OMRONConfigurationStatus.OMRONConfigurationFileUpdateError) {

                Log.d(TAG, "Config File Update Failure");
            }
        }
    };

    // Method to go to other activities(windows).

    public void openSurvey(View v) {
        Intent intent = new Intent(MainActivity.this, Survey_select.class);
        startActivity(intent);
    }

    public void openLoginData(View v) {
        Intent intent = new Intent(MainActivity.this, LoginDataView.class);
        startActivity(intent);
    }

    public void openTests(View v) {
        Intent intent = new Intent(MainActivity.this, Tests_select.class);
        startActivity(intent);
    }

    public void openRegister(View v) {
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    }

    public void openLogin(View v) {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }

    public void openWristband(View v) {
        Intent intent = new Intent(MainActivity.this, PersonalConfig.class);
        startActivity(intent);
    }

    public void openStatus(View v) {
        Intent intent = new Intent(MainActivity.this, Status.class);
        startActivity(intent);
    }

    public void openManual(View v) {
        Intent intent = new Intent(MainActivity.this, ManualBloodPressure.class);
        startActivity(intent);
    }

    /**
     * Method to logout. Delete the login file and old data in files that weren't send to the server
     * @param v View for the visual developed window
     */
    public void logout(View v) {
        Storage storage = new Storage();

        boolean bool = storage.deleteFile(getString(R.string.fileNameJsonDataSurveys), this);
        Log.d("Delete surveys file: ", Boolean.toString(bool));

        bool = storage.deleteFile(getString(R.string.fileNameJsonDataDevice), this);
        Log.d("Delete device file: ", Boolean.toString(bool));


        bool = storage.deleteFile(getString(R.string.fileNamePersonalLoginInfo), this);
        Log.d("Delete info file: ", Boolean.toString(bool));

        LoginManager.getInstance().logout();

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Avoid going back to surveys or other window
     */

    @Override
    public void onBackPressed() {
        return;
    }
}
