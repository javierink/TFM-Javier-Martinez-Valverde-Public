package com.example.healthcarehighbloodpressure.omronDevice;

import android.os.AsyncTask;
import android.util.Log;

import com.example.healthcarehighbloodpressure.supportClass.DataManager;
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
 * This Class is a aux class to extract data of the wristband witch a async method. Necessary when it's not a call from an activity.
 */
public class TransferDeviceManager extends AsyncTask<String, Integer, String> {
    private static final String TAG = "HealthGuideService";

    /**
     * Async method
     * @param params Not use in this case
     * @return Not use in this case
     */
    @Override
    public String doInBackground(String... params) {

        // Call to method for the wristband connection
        transferData();

        return null;
    }

    /**
     * Starts data transfer from the wristband to the app.
     *
     */
    public void transferData() {

        OmronPeripheral mSelectedPeripheral = OmronManager.getSingletonInstance().getConnectedOmronPeripheral();

        if (mSelectedPeripheral == null) {
            Log.d(TAG, "Device Not Paired");
            return;
        }

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


                        Log.d(TAG, errorInfo.getResultCode() + " / " + errorInfo.getDetailInfo());
                        Log.d(TAG, errorInfo.getMessageInfo());

                        disconnectDevice();

                    }else {

                        HashMap<String, Object> vitalData = (HashMap<String, Object>) output;

                        if (vitalData != null) {
                            manageData(vitalData, peripheral, true);
                        }
                    }

                } else {


                    Log.d(TAG, "Code: " + resultInfo.getResultCode());
                    if(resultInfo.getDetailInfo() == null){
                        Log.d(TAG, resultInfo.getResultCode() + " / " + resultInfo.getDetailInfo());
                    }
                    Log.d(TAG, resultInfo.getMessageInfo());

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

        continueDataTransfer();

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

                if (errorInfo.getResultCode() == 0 && peripheral != null) {

                    // Get vital data for previously selected user using OmronPeripheral
                    Object output = peripheral.getVitalDataWithUser(1);

                    if (output instanceof ErrorInfo) {

                        Log.d(TAG, errorInfo.getResultCode() + " / " + errorInfo.getDetailInfo());
                        Log.d(TAG, errorInfo.getMessageInfo());

                    }else {

                        HashMap<String, Object> vitalData = (HashMap<String, Object>) output;

                        // Edit the extracted information and store it in the view list, as
                        // well as refresh the view at the end.
                        if (vitalData != null) {

                            Log.d(TAG, "Data device transfer:");

                            double daySteps = -1;

                            // Activity Data
                            final ArrayList<HashMap<String, Object>> activityList = (ArrayList<HashMap<String, Object>>) vitalData.get(OmronConstants.OMRONVitalDataActivityKey);
                            if (activityList!= null) {

                                Log.d(TAG, "Activity Data:");
                                Log.d(TAG, "Size = " + Integer.toString(activityList.size()));

                                for(HashMap<String, Object> obj : activityList)
                                {
                                    Log.d(TAG, obj.toString());
                                    HashMap<String, Object> steps = (HashMap<String, Object>) obj.get("OMRONActivityNormalStepsPerDay");
                                    Log.d(TAG, "-Steps1: " + (String.valueOf(steps.get("OMRONActivityDataMeasurementKey"))));
                                }

                                HashMap<String, Object> lastSteps = activityList.get(activityList.size()-1);
                                HashMap<String, Object> steps = (HashMap<String, Object>) lastSteps.get("OMRONActivityNormalStepsPerDay");

                                String stepString = String.valueOf(steps.get("OMRONActivityDataMeasurementKey"));
                                Log.d(TAG, "-Steps2: " + stepString);

                                daySteps = (Double.parseDouble(stepString));
                                Log.d(TAG, "-Steps3: " + daySteps);
                            }

                            // Blood Pressure Data
                            final ArrayList<HashMap<String, Object>> bloodPressureItemList = (ArrayList<HashMap<String, Object>>) vitalData.get(OmronConstants.OMRONVitalDataBloodPressureKey);
                            if (bloodPressureItemList != null) {

                                Log.d(TAG, "Blood data:");
                                Log.d(TAG, "Size = " + Integer.toString(bloodPressureItemList.size()));
                                for(HashMap<String, Object> obj : bloodPressureItemList)
                                {
                                    Log.d(TAG, obj.toString());
                                    if(String.valueOf(obj.get("OMRONVitalDataErrorCodeKey")).equals("0")){
                                        Log.d(TAG, "-Systolic: " + (String.valueOf(obj.get("OMRONVitalDataSystolicKey")) + "\t mmHg") + " / Diastolic: " + obj.get("OMRONVitalDataDiastolicKey") + "\t mmHg");
                                        Log.d(TAG, "-Pulse: " + obj.get("OMRONVitalDataPulseKey"));
                                    }
                                }

                                // Json creation and send to the server test
                                if(bloodPressureItemList.size()>=1){
                                    HashMap<String, Object> lastBlood = bloodPressureItemList.get(bloodPressureItemList.size()-1);

                                    if(String.valueOf(lastBlood.get("OMRONVitalDataErrorCodeKey")).equals("0")){

                                        DataManager.getInstance().sendDataDevice(Double.parseDouble(String.valueOf(lastBlood.get("OMRONVitalDataMeasurementStartDateKey"))),
                                                Double.parseDouble(String.valueOf(lastBlood.get("OMRONVitalDataPulseKey"))),
                                                Double.parseDouble(String.valueOf(lastBlood.get("OMRONVitalDataSystolicKey"))),
                                                Double.parseDouble(String.valueOf(lastBlood.get("OMRONVitalDataDiastolicKey"))),
                                                daySteps);

                                    }
                                }
                            }
                        }
                    }

                }else {

                    Log.d(TAG, errorInfo.getResultCode() + " / " + errorInfo.getDetailInfo());
                    Log.d(TAG, errorInfo.getMessageInfo());

                }
            }
        });
    }

    /**
     * Method to disconnected de selected device
     */
    private void disconnectDevice() {

        // Disconnect device using OmronPeripheralManager
        OmronPeripheralManager.sharedManager(OmronManager.getSingletonInstance().getAppContext()).disconnectPeripheral(OmronManager.getSingletonInstance().getConnectedOmronPeripheral(), new OmronPeripheralManagerDisconnectListener() {
            @Override
            public void onDisconnectCompleted(OmronPeripheral peripheral, ErrorInfo resultInfo) {
                Log.d(TAG, "Device disconnected");
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
                Log.d(TAG, status);
            }
        });
    }
}
