package com.example.healthcarehighbloodpressure.omronDevice;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.MainActivity;
import com.example.healthcarehighbloodpressure.R;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.DeviceConfiguration.OmronPeripheralManagerConfig;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Interface.OmronPeripheralManagerConnectListener;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Interface.OmronPeripheralManagerConnectStateListener;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Interface.OmronPeripheralManagerDataTransferListener;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Interface.OmronPeripheralManagerDisconnectListener;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.LibraryManager.OmronPeripheralManager;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Model.ErrorInfo;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Model.OmronPeripheral;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.OmronUtility.OmronConstants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class corresponds to the connect activity, connect the device and transfer data.
 */
public class ConnectDevice extends AppCompatActivity {

    final String TAG = "OmronApp";

    private OmronPeripheral omronPeripheral, mSelectedPeripheral;

    //Button transferButton;
    TextView textStatus, textScan, errorCode, errorDesc;
    private ProgressBar progressBar;

    ListView listView;
    private ArrayAdapter<String> listViewAdapter;
    private ArrayList<String> stringArray;

    Handler mHandler;
    Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_device);

        omronPeripheral = OmronManager.getSingletonInstance().getScanOmronPeripheral();
        //Going through the "intent" proves wrong as he can't be casted
        //omronPeripheral = (OmronPeripheral) getIntent().getParcelableExtra("OMRON_PERIPHERICAL");

        //transferButton= (Button) findViewById(R.id.buttonTransfer);
        textStatus = (TextView) findViewById(R.id.textState2);
        errorCode = (TextView) findViewById(R.id.textCode);
        errorDesc = (TextView) findViewById(R.id.textDesc);
        textScan = (TextView) findViewById(R.id.textScan);
        progressBar  = (ProgressBar) findViewById(R.id.progressBar);

        listView=(ListView)findViewById(R.id.listView);

        stringArray = new ArrayList<String>();
        stringArray.add("Nothing at the moment");

        listViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArray);

        listView.setAdapter(listViewAdapter);

        //transferButton.setEnabled(false);
    }

    /**
     * Only for iniciate the connect process in the button view. Call the connectPeripheral method.
     * Is possible delete and put in the other method.
     *
     * @param v Necessary to be able to be called from the view.
     */
    public void connect(View v){

        progressBar.setVisibility(View.VISIBLE);
        setStateChanges();
        connectPeripheral(omronPeripheral);

    }

    /**
     * Make a connection to the device, transfer the configuration data from the previous activity
     * and save the device in the OmronManager.
     *
     * @param omronPeripheral Object save in the scan activity
     */
    private void connectPeripheral(final OmronPeripheral omronPeripheral){

        // Pair to Device using OmronPeripheralManager
        OmronPeripheralManager.sharedManager(OmronManager.getSingletonInstance().getAppContext()).connectPeripheral(omronPeripheral, new OmronPeripheralManagerConnectListener() {
            @Override
            public void onConnectCompleted(final OmronPeripheral peripheral, final ErrorInfo resultInfo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (resultInfo.getResultCode() == 0 && peripheral != null) {

                            mSelectedPeripheral = peripheral;
                            // Save the OmronPeripheral in de omron manager
                            OmronManager.getSingletonInstance().setConnectedOmronPeripheral(peripheral);

                            // Print the device information in debug
                            if (peripheral.getLocalName() != null) {
                                textScan.setText(peripheral.getLocalName() + "\n" + peripheral.getUuid());
                                textStatus.setText(peripheral.getLocalName() + "\n" + peripheral.getUuid());

                                HashMap<String, String> deviceInformation = peripheral.getDeviceInformation();
                                Log.d(TAG, "Device Information : " + deviceInformation);

                                ArrayList<HashMap>  deviceSettings  = mSelectedPeripheral.getDeviceSettings();
                                if(deviceSettings != null) {
                                    Log.d(TAG, "Device Settings:" + deviceSettings.toString());
                                }
                                Object personalSettingsForUser1 = mSelectedPeripheral.getDeviceSettingsWithUser(1);
                                if(personalSettingsForUser1 != null){
                                    Log.d(TAG ,"Personal Settings for User 1:"+personalSettingsForUser1.toString());
                                }
                                Object personalSettingsForUser2 = mSelectedPeripheral.getDeviceSettingsWithUser(2);
                                if(personalSettingsForUser2 != null){
                                    Log.d(TAG ,"Personal Settings for User 2:"+personalSettingsForUser2.toString());
                                }

                                OmronPeripheralManagerConfig peripheralConfig = OmronPeripheralManager.sharedManager(OmronManager.getSingletonInstance().getAppContext()).getConfiguration();
                                Log.d(TAG, "Device Config :  " + peripheralConfig.getDeviceConfigGroupIdAndGroupIncludedId(peripheral.getDeviceGroupIDKey(), peripheral.getDeviceGroupIncludedGroupIDKey()));
                            }
                        } else {

                            textStatus.setText("-");
                            errorCode.setText(resultInfo.getDetailInfo());
                            errorDesc.setText(resultInfo.getMessageInfo());
                        }

                        //transferButton.setEnabled(true);
                        progressBar.setVisibility(View.GONE);

                    }
                });
            }
        });
    }

    /**
     * Starts service to read and sent the wristband data.
     *
     * @param v Necessary to be able to be called from the view.
     */
    public void startService(View v) {

        //Intent serviceIntent = new Intent(OmronManager.getSingletonInstance().getAppContext(), HealthGuideService.class);
        //HealthGuideService.enqueueWork(OmronManager.getSingletonInstance().getAppContext(), serviceIntent);
    }

    /**
     * Starts data transfer from the wristband to the app.
     *
     * @param v Necessary to be able to be called from the view.
     */
    public void transferData(View v) {

        if (mSelectedPeripheral == null) {
            errorDesc.setText("Device Not Paired");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        //transferButton.setEnabled(false);

        // Set State Change Listener
        setStateChanges();

        //Create peripheral object with localname and UUID
        OmronPeripheral peripheralLocal = new OmronPeripheral(mSelectedPeripheral.getLocalName(), mSelectedPeripheral.getUuid());

        OmronPeripheralManager.sharedManager(OmronManager.getSingletonInstance().getAppContext()).startDataTransferFromPeripheral(peripheralLocal, 1, true, new OmronPeripheralManagerDataTransferListener() {
            @Override
            public void onDataTransferCompleted(OmronPeripheral peripheral, final ErrorInfo resultInfo) {

                if (resultInfo.getResultCode() == 0 && peripheral != null) {

                    OmronManager.getSingletonInstance().setConnectedOmronPeripheral(peripheral); // Saving for Transfer Function

                    HashMap<String, Object> settings = (HashMap<String, Object>) peripheral.getDeviceSettingsWithUser(1);
                    if(settings != null) {
                        Log.d(TAG, "Device Settings : " + settings.toString());
                    }

                    ArrayList<HashMap> allSettings =  (ArrayList<HashMap>) peripheral.getDeviceSettings();
                    Log.i(TAG, "All settings : " + allSettings.toString());

                    // Get vital data for previously selected user using OmronPeripheral
                    // Only exist one user in Hearth Guide
                    Object output = peripheral.getVitalDataWithUser(1);

                    if (output instanceof ErrorInfo) {

                        final ErrorInfo errorInfo = (ErrorInfo) output;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                errorCode.setText(errorInfo.getResultCode() + " / " + errorInfo.getDetailInfo());
                                errorDesc.setText(errorInfo.getMessageInfo());

                                //transferButton.setEnabled(true);
                                progressBar.setVisibility(View.GONE);

                            }
                        });

                        disconnectDevice();

                    }else {

                        HashMap<String, Object> vitalData = (HashMap<String, Object>) output;

                        if (vitalData != null) {
                            manageData(vitalData, peripheral, true);
                        }
                    }

                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            textStatus.setText("-");
                            errorCode.setText(resultInfo.getResultCode() + " / " + resultInfo.getDetailInfo());
                            errorDesc.setText(resultInfo.getMessageInfo());

                            if(mHandler != null)
                                mHandler.removeCallbacks(mRunnable);


                            //transferButton.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    /**
     * It manages the information transferred to the app to check that there is information to be
     * managed. At this level of the connection, for some reason that is not explained, the view
     * and the info cannot be managed towards external processes.
     *
     * @param vitalData Container of the data read
     * @param peripheral Peripheral device
     * @param isWait Boolean to know if data is waiting
     */
    private void manageData(HashMap<String, Object> vitalData, OmronPeripheral peripheral, boolean isWait){

        Log.d(TAG, "Data device transfer:");
        // Blood Pressure Data
        final ArrayList<HashMap<String, Object>> bloodPressureItemList = (ArrayList<HashMap<String, Object>>) vitalData.get(OmronConstants.OMRONVitalDataBloodPressureKey);
        if (bloodPressureItemList != null) {

            //Debugging prints
            Log.d(TAG, "Blood data:");
            Log.d(TAG, "Size = " + Integer.toString(bloodPressureItemList.size()));
            for(HashMap<String, Object> obj : bloodPressureItemList)
            {
                Log.d(TAG, obj.toString());

            }
        }

        // Activity Data
        final ArrayList<HashMap<String, Object>> activityList = (ArrayList<HashMap<String, Object>>) vitalData.get(OmronConstants.OMRONVitalDataActivityKey);
        if (activityList!= null) {

            //Debugging prints
            Log.d(TAG, "Activity Data:");
            Log.d(TAG, "Size = " + Integer.toString(activityList.size()));
            for(HashMap<String, Object> obj : activityList)
            {
                Log.d(TAG, obj.toString());
            }
        }

        // Sleep Data
        ArrayList<HashMap<String, Object>> sleepingData = (ArrayList<HashMap<String, Object>>) vitalData.get(OmronConstants.OMRONVitalDataSleepKey);
        if (sleepingData != null) {

            //Debugging prints
            Log.d(TAG, "Sleep Data:");
            Log.d(TAG, "Size = " + Integer.toString(sleepingData.size()));
            for(HashMap<String, Object> obj : sleepingData)
            {
                Log.d(TAG, obj.toString());
            }
        }
        // Records Data
        ArrayList<HashMap<String, Object>> recordData = (ArrayList<HashMap<String, Object>>) vitalData.get(OmronConstants.OMRONVitalDataRecordKey);
        if (recordData != null) {

            //Debugging prints
            Log.d(TAG, "Records Data:");
            Log.d(TAG, "Size = " + Integer.toString(recordData.size()));
            for(HashMap<String, Object> obj : recordData)
            {
                Log.d(TAG, obj.toString());
            }
        }
        // Weight Data
        ArrayList<HashMap<String, Object>> weightData = (ArrayList<HashMap<String, Object>>) vitalData.get(OmronConstants.OMRONVitalDataWeightKey);
        if (weightData != null) {

            //Debugging prints
            Log.d(TAG, "Weight Data:");
            Log.d(TAG, "Size = " + Integer.toString(weightData.size()));
            for(HashMap<String, Object> obj : weightData)
            {
                Log.d(TAG, obj.toString());
            }
        }

        if(isWait) {

            mHandler = new Handler();
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    continueDataTransfer();
                }
            };

            mHandler.postDelayed(mRunnable, 1000);

        }else {

            if(mHandler != null)
                mHandler.removeCallbacks(mRunnable);

            continueDataTransfer();
        }

    }

    /**
     * The last method of transfer, ends the data transfer which causes it to be marked as read and
     * not transferred in the following readings. In this method, the data is processed by filling
     * in the list in the view.
     */
    private void continueDataTransfer() {

        OmronPeripheralManager.sharedManager(OmronManager.getSingletonInstance().getAppContext()).endDataTransferFromPeripheral(new OmronPeripheralManagerDataTransferListener() {
            @Override
            public void onDataTransferCompleted(final OmronPeripheral peripheral, final ErrorInfo errorInfo) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (errorInfo.getResultCode() == 0 && peripheral != null) {

                            // Get vital data for previously selected user using OmronPeripheral
                            Object output = peripheral.getVitalDataWithUser(1);

                            if (output instanceof ErrorInfo) {

                                final ErrorInfo errorInfo = (ErrorInfo) output;

                                errorCode.setText(errorInfo.getResultCode() + " / " + errorInfo.getDetailInfo());
                                errorDesc.setText(errorInfo.getMessageInfo());

                            }else {

                                HashMap<String, Object> vitalData = (HashMap<String, Object>) output;

                                // Edit the extracted information and store it in the view list, as
                                // well as refresh the view at the end.
                                if (vitalData != null) {
                                    stringArray.clear();
                                    Log.d(TAG, "Data device transfer:");

                                    // Blood Pressure Data
                                    final ArrayList<HashMap<String, Object>> bloodPressureItemList = (ArrayList<HashMap<String, Object>>) vitalData.get(OmronConstants.OMRONVitalDataBloodPressureKey);
                                    if (bloodPressureItemList != null) {
                                        stringArray.add("--Blood pressure: ");

                                        Log.d(TAG, "Blood data:");
                                        Log.d(TAG, "Size = " + Integer.toString(bloodPressureItemList.size()));
                                        for(HashMap<String, Object> obj : bloodPressureItemList)
                                        {
                                            Log.d(TAG, obj.toString());
                                            if(String.valueOf(obj.get("OMRONVitalDataErrorCodeKey")).equals("0")){
                                                stringArray.add("-Systolic: " + (String.valueOf(obj.get("OMRONVitalDataSystolicKey")) + "\t mmHg") + " / Diastolic: " + obj.get("OMRONVitalDataDiastolicKey") + "\t mmHg");
                                                stringArray.add("-Pulse: " + obj.get("OMRONVitalDataPulseKey"));
                                            }
                                        }
                                    }

                                    // Activity Data
                                    final ArrayList<HashMap<String, Object>> activityList = (ArrayList<HashMap<String, Object>>) vitalData.get(OmronConstants.OMRONVitalDataActivityKey);
                                    if (activityList!= null) {
                                        stringArray.add("--Activity: ");

                                        Log.d(TAG, "Activity Data:");
                                        Log.d(TAG, "Size = " + Integer.toString(activityList.size()));
                                        for(HashMap<String, Object> obj : activityList)
                                        {
                                            Log.d(TAG, obj.toString());
                                            HashMap<String, Object> steps = (HashMap<String, Object>) obj.get("OMRONActivityNormalStepsPerDay");
                                            stringArray.add("-Steps: " + (String.valueOf(steps.get("OMRONActivityDataMeasurementKey"))));
                                        }
                                    }

                                    // Sleep Data
                                    ArrayList<HashMap<String, Object>> sleepingData = (ArrayList<HashMap<String, Object>>) vitalData.get(OmronConstants.OMRONVitalDataSleepKey);
                                    if (sleepingData != null) {

                                        Log.d(TAG, "Sleep Data:");
                                        Log.d(TAG, "Size = " + Integer.toString(sleepingData.size()));
                                        for(HashMap<String, Object> obj : sleepingData)
                                        {
                                            Log.d(TAG, obj.toString());
                                        }
                                    }
                                    // Records Data
                                    ArrayList<HashMap<String, Object>> recordData = (ArrayList<HashMap<String, Object>>) vitalData.get(OmronConstants.OMRONVitalDataRecordKey);
                                    if (recordData != null) {

                                        Log.d(TAG, "Records Data:");
                                        Log.d(TAG, "Size = " + Integer.toString(recordData.size()));
                                        for(HashMap<String, Object> obj : recordData)
                                        {
                                            Log.d(TAG, obj.toString());
                                        }
                                    }
                                    // Weight Data
                                    ArrayList<HashMap<String, Object>> weightData = (ArrayList<HashMap<String, Object>>) vitalData.get(OmronConstants.OMRONVitalDataWeightKey);
                                    if (weightData != null) {

                                        Log.d(TAG, "Weight Data:");
                                        Log.d(TAG, "Size = " + Integer.toString(weightData.size()));
                                        for(HashMap<String, Object> obj : weightData)
                                        {
                                            Log.d(TAG, obj.toString());
                                        }
                                    }
                                    // Update the list of the listView
                                    listViewAdapter.notifyDataSetChanged();
                                }
                            }

                        }else {

                            textStatus.setText("-");
                            errorCode.setText(errorInfo.getResultCode() + " / " + errorInfo.getDetailInfo());
                            errorDesc.setText(errorInfo.getMessageInfo());

                        }
                        //transferButton.setEnabled(true);
                        progressBar.setVisibility(View.GONE);

                    }
                });


            }
        });
    }

    /**
     * Method to disconnected de selected device
     */
    private void disconnectDevice() {
        // Disconnect device using OmronPeripheralManager
        OmronPeripheralManager.sharedManager(OmronManager.getSingletonInstance().getAppContext()).disconnectPeripheral(mSelectedPeripheral, new OmronPeripheralManagerDisconnectListener() {
            @Override
            public void onDisconnectCompleted(OmronPeripheral peripheral, ErrorInfo resultInfo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Device disconnected");
                        //transferButton.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    /**
     * Is uses to change the internal state of de  connection in de sdk library in the view
     */
    private void setStateChanges() {
        // Listen to Device state changes using OmronPeripheralManager
        OmronPeripheralManager.sharedManager(OmronManager.getSingletonInstance().getAppContext()).onConnectStateChange(new OmronPeripheralManagerConnectStateListener() {

            @Override
            public void onConnectStateChange(final int state) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String status = "-";

                        if (state == OmronConstants.OMRONBLEConnectionState.CONNECTING) {
                            status = "Connecting...";
                        } else if (state == OmronConstants.OMRONBLEConnectionState.CONNECTED) {
                            status = "Connected";
                        } else if (state == OmronConstants.OMRONBLEConnectionState.DISCONNECTING) {
                            status = "Disconnecting...";
                        } else if (state == OmronConstants.OMRONBLEConnectionState.DISCONNECTED) {
                            status = "Disconnected";
                        }
                        textStatus.setText(status);
                    }
                });
            }
        });
    }

    /**
     * Goes the active activity to main activity
     *
     * @param v Necessary to be able to be called from the view.
     */
    public void toMain(View v) {

        Intent intent = new Intent(ConnectDevice.this, MainActivity.class);
        startActivity(intent);
    }
}
