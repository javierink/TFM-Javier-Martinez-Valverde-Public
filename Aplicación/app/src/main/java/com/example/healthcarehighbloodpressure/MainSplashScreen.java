package com.example.healthcarehighbloodpressure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.healthcarehighbloodpressure.omronDevice.OmronManager;
import com.example.healthcarehighbloodpressure.supportClass.ContextManager;
import com.example.healthcarehighbloodpressure.supportClass.LoginManager;
import com.example.healthcarehighbloodpressure.supportClass.Storage;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.LibraryManager.OmronPeripheralManager;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.OmronUtility.OmronConstants;

import org.json.JSONObject;

/**
 * Class of the MainSplashScreen activity, preloads different operations for the application.
 * Not in use in actual version. All the use is in the MainActivity now.
 */
public class MainSplashScreen extends AppCompatActivity {
    private static final String TAG = "MainSplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_splash_screen);

        Storage storage = new Storage();

        //boolean bool = storage.deleteFile(getString(R.string.fileNamePersonalLoginInfo), this);
        //Log.d("Delete: ", Boolean.toString(bool));

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

        // Move to the main app screen
        changeToMain();

    }

    /**
     * Change the view to the MainActivity
     */
    private void changeToMain(){

        Intent intent = new Intent(MainSplashScreen.this, MainActivity.class);
        startActivity(intent);
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
}
