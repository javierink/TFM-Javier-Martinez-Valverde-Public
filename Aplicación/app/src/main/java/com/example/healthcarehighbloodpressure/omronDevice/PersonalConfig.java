package com.example.healthcarehighbloodpressure.omronDevice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.healthcarehighbloodpressure.GeneralTests;
import com.example.healthcarehighbloodpressure.MainActivity;
import com.example.healthcarehighbloodpressure.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class corresponds to the PersonalConfig activity that save the personal config for the Omron device.
 */
public class PersonalConfig extends AppCompatActivity {

    final String TAG = "PersonalConfig";

    Spinner spinnerHeight, spinnerWeight, spinnerStride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_config);

        // Create the content of the height spinner
        spinnerHeight = (Spinner) findViewById(R.id.spHeight);
        List<Integer> list = new ArrayList<Integer>();
        for (int i=30; i<=250; i++){
            list.add(i);
        }
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHeight.setAdapter(dataAdapter);

        // Default position of the height spinner
        int spinnerPosition = dataAdapter.getPosition(173);
        spinnerHeight.setSelection(spinnerPosition);

        // Create the content of the weight spinner
        spinnerWeight = (Spinner) findViewById(R.id.spWeight);
        list = new ArrayList<Integer>();
        for (int i=30; i<=150; i++){
            list.add(i);
        }
        dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeight.setAdapter(dataAdapter);

        // Default position of the weight spinner
        spinnerPosition = dataAdapter.getPosition(69);
        spinnerWeight.setSelection(spinnerPosition);

        // Create the content of the stride spinner
        spinnerStride = (Spinner) findViewById(R.id.spStride);
        list = new ArrayList<Integer>();
        for (int i=30; i<=110; i++){
            list.add(i);
        }
        dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStride.setAdapter(dataAdapter);

        // Default position of the stride spinner
        spinnerPosition = dataAdapter.getPosition(68);
        spinnerStride.setSelection(spinnerPosition);

    }

    /**
     *
     * @param v Necessary to be able to be called from the view.
     */
    public void config(View v){

        // Create the intent
        Intent intent = new Intent(PersonalConfig.this, ScanDevice.class);

        // Create the bundle to contain data for the next activity
        Bundle bundle = new Bundle();

        // Save in the bundle all the collected setting
        bundle.putInt("HEIGHT", (Integer) spinnerHeight.getSelectedItem());
        bundle.putInt("WEIGHT", (Integer) spinnerWeight.getSelectedItem());
        bundle.putInt("STRIDE", (Integer) spinnerStride.getSelectedItem());

        // Insert bundle in the intent
        intent.putExtra("PERSONAL_SETTINGS", bundle);
        startActivity(intent);
    }

    public void exitToMain(View v) {
        Intent intent = new Intent(PersonalConfig.this, MainActivity.class);
        startActivity(intent);
    }

}
