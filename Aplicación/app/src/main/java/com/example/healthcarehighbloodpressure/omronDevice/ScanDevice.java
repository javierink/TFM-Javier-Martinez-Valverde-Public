package com.example.healthcarehighbloodpressure.omronDevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.healthcarehighbloodpressure.MainActivity;
import com.example.healthcarehighbloodpressure.R;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.DeviceConfiguration.OmronPeripheralManagerConfig;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Interface.OmronPeripheralManagerScanListener;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Interface.OmronPeripheralManagerStopScanListener;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.LibraryManager.OmronPeripheralManager;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Model.ErrorInfo;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Model.OmronPeripheral;
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.OmronUtility.OmronConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This Class corresponds to the scan activity, does search of the near devices.
 */
public class ScanDevice extends AppCompatActivity {

    final String TAG = "OmronApp";

    private Button scanButton;

    HashMap<String, String> selectDevice;
    private ArrayList<OmronPeripheral> mPeripheralList;
    private ListView listView;
    private ProgressBar progressBar;

    // Custom class to be able to extract OmronPeripheral object from the list directly when clicked
    private ScannedDevicesAdapter mScannedDevicesAdapter;

    private Boolean isScan = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_device);

        scanButton= (Button) findViewById(R.id.scanButton);
        listView = (ListView) findViewById(R.id.listView);
        progressBar  = (ProgressBar) findViewById(R.id.progressBar);

        // In a first step, a configuration must be started in the sdk library
        startConfiguration();

        // It initializes an empty list where we will store the detected peripherals
        mPeripheralList = new ArrayList<OmronPeripheral>();
        mScannedDevicesAdapter = new ScannedDevicesAdapter(this, mPeripheralList);
        mScannedDevicesAdapter.notifyDataSetChanged();

        // The list is associated with the adapter containing the list of peripherals
        listView.setAdapter(mScannedDevicesAdapter);

        // The listView is programmed to click and pick up the peripheral object and save it in the
        // OmronManager, as well as to open the following activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ScanDevice.this, ConnectDevice.class);

                OmronManager.getSingletonInstance().setScanOmronPeripheral(mScannedDevicesAdapter.getItem(position));
                //Going through the "intent" proves wrong as he can't be casted
                //intent.putExtra("OMRON_PERIPHERICAL", mScannedDevicesAdapter.getItem(position));
                startActivity(intent);
            }
        });

    }

    /**
     * Initializes the configuration, first extract from the library the device we want to configure
     * and connect, later inicialize the startOmronPeripheralManager(if exist), this method continue
     * the general configuration of the device. In this App we create a connection for a specific
     * device, Omron Herat Guide.
     */
    private void startConfiguration() {

        Context ctx = OmronManager.getSingletonInstance().getAppContext();

        List<HashMap<String, String>> deviceList = (List<HashMap<String, String>>) OmronPeripheralManager.sharedManager(ctx).retrieveManagerConfiguration(ctx).get(OmronConstants.OMRONBLEConfigDeviceKey);

        // I know the last item is the Herat Guide type of device, in other case, must iterate list and search
        selectDevice = deviceList.get(deviceList.size()-1);

        if (selectDevice != null){
            String name = selectDevice.get(OmronConstants.OMRONBLEConfigDevice.ModelName);
            Log.d(TAG, "Selected device config: " + name);
            startOmronPeripheralManager();
        }else{
            Log.d(TAG, "Selected device config: is null");
        }
    }

    /**
     * This method does the config of the future device in sdk library, when we connect de App to the device
     * this config will be dumped in the device.
     */
    public void startOmronPeripheralManager(){
        // Create config peripheral
        OmronPeripheralManagerConfig peripheralConfig = OmronPeripheralManager.sharedManager(OmronManager.getSingletonInstance().getAppContext()).getConfiguration();

        // Filter device to scan and connect (optional).
        // Only Omron Heart Guide wristbands are visible.

        List<HashMap<String, String>> filterDevices = new ArrayList<>();
        filterDevices.add(selectDevice);
        peripheralConfig.deviceFilters = filterDevices;
        Log.d(TAG, "Device filter: " + selectDevice.toString());


        // Create device setting object
        ArrayList<HashMap> deviceSettings = new ArrayList<>();

        // Extract the personal setting of the before activity saved in the bundle
        Bundle bundle = getIntent().getBundleExtra("PERSONAL_SETTINGS");

        Log.d(TAG, "Personal setting are collected");

        String height = Integer.toString((bundle.getInt("HEIGHT")) * 100);
        String weight = Integer.toString((bundle.getInt("WEIGHT")) * 100);
        String stride = Integer.toString((bundle.getInt("STRIDE")) * 100);

        Log.d(TAG, "Personal setting: " + height + " - " + weight + " - " + stride);

        // Complete personal setting
        HashMap<String, String> settingsModel = new HashMap<String, String>();
        settingsModel.put(OmronConstants.OMRONDevicePersonalSettings.UserHeightKey, height);
        settingsModel.put(OmronConstants.OMRONDevicePersonalSettings.UserWeightKey, weight);
        settingsModel.put(OmronConstants.OMRONDevicePersonalSettings.UserStrideKey, stride);
        settingsModel.put(OmronConstants.OMRONDevicePersonalSettings.TargetSleepKey, "480");
        settingsModel.put(OmronConstants.OMRONDevicePersonalSettings.TargetStepsKey, "8000");

        HashMap<String, HashMap> userSettings = new HashMap<>();
        userSettings.put(OmronConstants.OMRONDevicePersonalSettingsKey, settingsModel);

        // Notification settings
        ArrayList<String> notificationsAvailable = new ArrayList<>();
        notificationsAvailable.add("android.intent.action.PHONE_STATE");
        notificationsAvailable.add("com.google.android.gm");
        notificationsAvailable.add("android.provider.Telephony.SMS_RECEIVED");

        HashMap<String, Object> notificationSettings = new HashMap<String, Object>();
        notificationSettings.put(OmronConstants.OMRONDeviceNotificationSettingsKey, notificationsAvailable);

        // Time Format
        HashMap<String, Object> timeFormatSettings = new HashMap<String, Object>();
        timeFormatSettings.put(OmronConstants.OMRONDeviceTimeSettings.FormatKey, OmronConstants.OMRONDeviceTimeFormat.Time24Hour);

        HashMap<String, HashMap> timeSettings = new HashMap<>();
        timeSettings.put(OmronConstants.OMRONDeviceTimeSettingsKey, timeFormatSettings);


        // Sleep Settings
        HashMap<String, Object> sleepTimeSettings = new HashMap<String, Object>();
        sleepTimeSettings.put(OmronConstants.OMRONDeviceSleepSettings.AutomaticKey, OmronConstants.OMRONDeviceSleepAutomatic.On);
        sleepTimeSettings.put(OmronConstants.OMRONDeviceSleepSettings.StartTimeKey, "20");
        sleepTimeSettings.put(OmronConstants.OMRONDeviceSleepSettings.StopTimeKey, "10");

        HashMap<String, HashMap> sleepSettings = new HashMap<>();
        sleepSettings.put(OmronConstants.OMRONDeviceSleepSettingsKey, sleepTimeSettings);

/*
        // Alarm Settings (predeterminate)
        // Alarm 1 Time
        HashMap<String, Object> alarmTime1 = new HashMap<String, Object>();
        alarmTime1.put(OmronConstants.OMRONDeviceAlarmSettings.HourKey, "13");
        alarmTime1.put(OmronConstants.OMRONDeviceAlarmSettings.MinuteKey, "30");
        // Alarm 1 Day (MON-SUN)
        HashMap<String, Object> alarmDays1 = new HashMap<String, Object>();
        alarmDays1.put(OmronConstants.OMRONDeviceAlarmSettings.MondayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays1.put(OmronConstants.OMRONDeviceAlarmSettings.TuesdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays1.put(OmronConstants.OMRONDeviceAlarmSettings.WednesdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays1.put(OmronConstants.OMRONDeviceAlarmSettings.ThursdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays1.put(OmronConstants.OMRONDeviceAlarmSettings.FridayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays1.put(OmronConstants.OMRONDeviceAlarmSettings.SaturdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays1.put(OmronConstants.OMRONDeviceAlarmSettings.SundayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        HashMap<String, Object> alarm1 = new HashMap<>();
        alarm1.put(OmronConstants.OMRONDeviceAlarmSettings.DaysKey, alarmDays1);
        alarm1.put(OmronConstants.OMRONDeviceAlarmSettings.TimeKey, alarmTime1);
        alarm1.put(OmronConstants.OMRONDeviceAlarmSettings.TypeKey, OmronConstants.OMRONDeviceAlarmType.Measure);


        // Alarm 2 Time
        HashMap<String, Object> alarmTime2 = new HashMap<String, Object>();
        alarmTime2.put(OmronConstants.OMRONDeviceAlarmSettings.HourKey, "16");
        alarmTime2.put(OmronConstants.OMRONDeviceAlarmSettings.MinuteKey, "30");
        // Alarm 2 Day (MON-SUN)
        HashMap<String, Object> alarmDays2 = new HashMap<String, Object>();
        alarmDays2.put(OmronConstants.OMRONDeviceAlarmSettings.MondayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays2.put(OmronConstants.OMRONDeviceAlarmSettings.TuesdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays2.put(OmronConstants.OMRONDeviceAlarmSettings.WednesdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays2.put(OmronConstants.OMRONDeviceAlarmSettings.ThursdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays2.put(OmronConstants.OMRONDeviceAlarmSettings.FridayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays2.put(OmronConstants.OMRONDeviceAlarmSettings.SaturdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays2.put(OmronConstants.OMRONDeviceAlarmSettings.SundayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        HashMap<String, Object> alarm2 = new HashMap<>();
        alarm2.put(OmronConstants.OMRONDeviceAlarmSettings.DaysKey, alarmDays2);
        alarm2.put(OmronConstants.OMRONDeviceAlarmSettings.TimeKey, alarmTime2);
        alarm2.put(OmronConstants.OMRONDeviceAlarmSettings.TypeKey, OmronConstants.OMRONDeviceAlarmType.Measure);


        // Alarm 3 Time
        HashMap<String, Object> alarmTime3 = new HashMap<String, Object>();
        alarmTime3.put(OmronConstants.OMRONDeviceAlarmSettings.HourKey, "15");
        alarmTime3.put(OmronConstants.OMRONDeviceAlarmSettings.MinuteKey, "40");
        // Alarm 3 Day (MON-SUN)
        HashMap<String, Object> alarmDays3 = new HashMap<String, Object>();
        alarmDays3.put(OmronConstants.OMRONDeviceAlarmSettings.MondayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays3.put(OmronConstants.OMRONDeviceAlarmSettings.TuesdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays3.put(OmronConstants.OMRONDeviceAlarmSettings.WednesdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays3.put(OmronConstants.OMRONDeviceAlarmSettings.ThursdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays3.put(OmronConstants.OMRONDeviceAlarmSettings.FridayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays3.put(OmronConstants.OMRONDeviceAlarmSettings.SaturdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays3.put(OmronConstants.OMRONDeviceAlarmSettings.SundayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        HashMap<String, Object> alarm3 = new HashMap<>();
        alarm3.put(OmronConstants.OMRONDeviceAlarmSettings.DaysKey, alarmDays3);
        alarm3.put(OmronConstants.OMRONDeviceAlarmSettings.TimeKey, alarmTime3);
        alarm3.put(OmronConstants.OMRONDeviceAlarmSettings.TypeKey, OmronConstants.OMRONDeviceAlarmType.Measure);

        // Alarm 4 Time
        HashMap<String, Object> alarmTime4 = new HashMap<String, Object>();
        alarmTime4.put(OmronConstants.OMRONDeviceAlarmSettings.HourKey, "15");
        alarmTime4.put(OmronConstants.OMRONDeviceAlarmSettings.MinuteKey, "45");
        // Alarm 4 Day (MON-SUN)
        HashMap<String, Object> alarmDays4 = new HashMap<String, Object>();
        alarmDays4.put(OmronConstants.OMRONDeviceAlarmSettings.MondayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays4.put(OmronConstants.OMRONDeviceAlarmSettings.TuesdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays4.put(OmronConstants.OMRONDeviceAlarmSettings.WednesdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays4.put(OmronConstants.OMRONDeviceAlarmSettings.ThursdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays4.put(OmronConstants.OMRONDeviceAlarmSettings.FridayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays4.put(OmronConstants.OMRONDeviceAlarmSettings.SaturdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays4.put(OmronConstants.OMRONDeviceAlarmSettings.SundayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        HashMap<String, Object> alarm4 = new HashMap<>();
        alarm4.put(OmronConstants.OMRONDeviceAlarmSettings.DaysKey, alarmDays4);
        alarm4.put(OmronConstants.OMRONDeviceAlarmSettings.TimeKey, alarmTime4);
        alarm4.put(OmronConstants.OMRONDeviceAlarmSettings.TypeKey, OmronConstants.OMRONDeviceAlarmType.Measure);

        // Alarm 5 Time
        HashMap<String, Object> alarmTime5 = new HashMap<String, Object>();
        alarmTime5.put(OmronConstants.OMRONDeviceAlarmSettings.HourKey, "15");
        alarmTime5.put(OmronConstants.OMRONDeviceAlarmSettings.MinuteKey, "50");
        // Alarm 5 Day (MON-SUN)
        HashMap<String, Object> alarmDays5 = new HashMap<String, Object>();
        alarmDays5.put(OmronConstants.OMRONDeviceAlarmSettings.MondayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays5.put(OmronConstants.OMRONDeviceAlarmSettings.TuesdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays5.put(OmronConstants.OMRONDeviceAlarmSettings.WednesdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays5.put(OmronConstants.OMRONDeviceAlarmSettings.ThursdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays5.put(OmronConstants.OMRONDeviceAlarmSettings.FridayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays5.put(OmronConstants.OMRONDeviceAlarmSettings.SaturdayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        alarmDays5.put(OmronConstants.OMRONDeviceAlarmSettings.SundayKey, OmronConstants.OMRONDeviceAlarmStatus.On);
        HashMap<String, Object> alarm5 = new HashMap<>();
        alarm5.put(OmronConstants.OMRONDeviceAlarmSettings.DaysKey, alarmDays5);
        alarm5.put(OmronConstants.OMRONDeviceAlarmSettings.TimeKey, alarmTime5);
        alarm5.put(OmronConstants.OMRONDeviceAlarmSettings.TypeKey, OmronConstants.OMRONDeviceAlarmType.Measure);
        */

        // Add Alarm1 and Alarm2 to List
        ArrayList<HashMap> alarms = new ArrayList<>();
        //alarms.add(alarm1);
        //alarms.add(alarm2);
        //alarms.add(alarm3);
        //alarms.add(alarm4);
        //alarms.add(alarm5);
        HashMap<String, Object> alarmSettings = new HashMap<>();
        alarmSettings.put(OmronConstants.OMRONDeviceAlarmSettingsKey, alarms);


        // Notification enable settings
        HashMap<String, Object> notificationEnableSettings = new HashMap<String, Object>();
        notificationEnableSettings.put(OmronConstants.OMRONDeviceNotificationStatusKey, OmronConstants.OMRONDeviceNotificationStatus.On);
        HashMap<String, HashMap> notificationStatusSettings = new HashMap<>();
        notificationStatusSettings.put(OmronConstants.OMRONDeviceNotificationEnableSettingsKey, notificationEnableSettings);


        // Add all specific settings to general setting object
        deviceSettings.add(userSettings);
        deviceSettings.add(notificationSettings);
        deviceSettings.add(alarmSettings);
        deviceSettings.add(timeSettings);
        deviceSettings.add(sleepSettings);
        deviceSettings.add(notificationStatusSettings);

        // Add general setting to peripheral config
        peripheralConfig.deviceSettings = deviceSettings;

        // Set Scan timeout interval (optional)
        peripheralConfig.timeoutInterval = 10;
        // Set User Hash Id (mandatory)
        // In the pdf guide it says it is mandatory but it works without it
        peripheralConfig.userHashId = "<email_address_of_user>"; // Set logged in user email

        // Set configuration for OmronPeripheralManager
        OmronPeripheralManager.sharedManager(OmronManager.getSingletonInstance().getAppContext()).setConfiguration(peripheralConfig);

        //Initialize the connection process.
        OmronPeripheralManager.sharedManager(OmronManager.getSingletonInstance().getAppContext()).startManager();

        // Notification Listener for BLE State Change
        LocalBroadcastManager.getInstance(OmronManager.getSingletonInstance()).registerReceiver(mMessageReceiver, new IntentFilter(OmronConstants.OMRONBLECentralManagerDidUpdateStateNotification));

        Log.d(TAG, "Configurated device config");

    }

    /**
     * Start or stop device search. In the search process it stores a list of available devices to
     * which you can connect by clicking on the list of the interface where they will appear.
     * @param v Necessary to be able to be called from the view.
     */
    public void startScanning(View v) {

        if(isScan) {

            scanButton.setText(getString(R.string.omron_scan_scan));
            progressBar.setVisibility(View.GONE);

            // Stop Scanning for Devices using OmronPeripheralManager
            OmronPeripheralManager.sharedManager(OmronManager.getSingletonInstance().getAppContext()).stopScanPeripherals(new OmronPeripheralManagerStopScanListener() {
                @Override
                public void onStopScanCompleted(final ErrorInfo resultInfo) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (resultInfo.getResultCode() == 0) {

                            } else {

                                Toast.makeText(ScanDevice.this, "Error Code : " + resultInfo.getResultCode() + "\nError Detail Code : " + resultInfo.getDetailInfo(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

        }else {

            scanButton.setText(getString(R.string.omron_scan_stop));
            progressBar.setVisibility(View.VISIBLE);

            // Start Scanning for Devices using OmronPeripheralManager
            OmronPeripheralManager.sharedManager(OmronManager.getSingletonInstance().getAppContext()).startScanPeripherals(new OmronPeripheralManagerScanListener() {

                @Override
                public void onScanCompleted(final ArrayList<OmronPeripheral> peripheralList, final ErrorInfo resultInfo) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (resultInfo.getResultCode() == 0) {

                                mPeripheralList = peripheralList;

                                // Save the peripheral list in the adapter and update the view
                                if (mScannedDevicesAdapter != null) {
                                    mScannedDevicesAdapter.setPeripheralList(mPeripheralList);
                                    mScannedDevicesAdapter.notifyDataSetChanged();
                                }


                            } else {

                                isScan = !isScan;

                                scanButton.setText(getString(R.string.omron_scan_scan));
                                progressBar.setVisibility(View.GONE);

                                Log.d(TAG, "No device scan code/info: " + resultInfo.getResultCode() + " / " + resultInfo.getDetailInfo());
                                Log.d(TAG, "No device scan info: " + resultInfo.getMessageInfo());

                            }

                        }
                    });
                }
            });
        }

        isScan = !isScan;
    }

    /**
     * mMessageReceiver class to notify states of the bluetooth.
     */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Get extra data included in the Intent
            int status = intent.getIntExtra(OmronConstants.OMRONBLEBluetoothStateKey, 0);

            if (status == OmronConstants.OMRONBLEBluetoothState.OMRONBLEBluetoothStateUnknown) {

                Log.d(TAG, "Bluetooth is in unknown state");

            } else if (status == OmronConstants.OMRONBLEBluetoothState.OMRONBLEBluetoothStateOff) {

                Log.d(TAG, "Bluetooth is currently powered off");

            } else if (status == OmronConstants.OMRONBLEBluetoothState.OMRONBLEBluetoothStateOn) {

                Log.d(TAG, "Bluetooth is currently powered on");
            }
        }
    };

    /**
     * Goes the active activity to main activity
     *
     * @param v Necessary to be able to be called from the view.
     */
    public void toMain(View v) {

        Intent intent = new Intent(ScanDevice.this, MainActivity.class);
        startActivity(intent);
    }

}
