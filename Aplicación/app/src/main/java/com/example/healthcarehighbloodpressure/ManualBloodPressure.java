package com.example.healthcarehighbloodpressure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.healthcarehighbloodpressure.services.GeoService;
import com.example.healthcarehighbloodpressure.supportClass.DataManager;
import com.example.healthcarehighbloodpressure.supportClass.InputFilterMinMax;
/**
 * Activity class for send a manual mesurement blood pressure to the server
 */
public class ManualBloodPressure extends AppCompatActivity {

    private EditText editTextDia;
    private EditText editTextSys;
    private EditText editTextPulse;
    private EditText editTextSteps;
    private TextView textViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_blood_pressure);

        editTextDia = (EditText)findViewById(R.id.editTextDia);
        editTextSys = (EditText)findViewById(R.id.editTextSys);
        editTextPulse = (EditText)findViewById(R.id.editTextPulse);
        editTextSteps = (EditText)findViewById(R.id.editTextSteps);
        textViewError = (TextView)findViewById(R.id.textViewError);

        //We limit the values of the editText element
        editTextDia.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "300")});
        editTextSys.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "300")});
        editTextPulse.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "300")});
        editTextSteps.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "100000")});

        //We change to invisible the notice to fill fields
        textViewError.setVisibility(View.INVISIBLE);
    }

    public void finish(View v) {
        String textSys = editTextSys.getText().toString();
        String textDia = editTextDia.getText().toString();
        String textPulse = editTextPulse.getText().toString();
        String textSteps = editTextSteps.getText().toString();

        //Need all editText completed for send the data info
        if(!textSys.matches("") && !textDia.matches("") && !textPulse.matches("") && !textSteps.matches("")){

            final Double pulse = Double.parseDouble(textPulse);
            final Double sys = Double.parseDouble(textSys);
            final Double dia = Double.parseDouble(textDia);
            final Double steps = Double.parseDouble(textSteps);

            Double ms = new Double(System.currentTimeMillis());

            //Send de measurement
            DataManager.getInstance().sendDataDevice(ms, pulse, sys, dia, steps);

            //Get the location to send to the server
            Intent geoIntent = new Intent(this, GeoService.class);
            GeoService.enqueueWork(this, geoIntent);

            Intent intent = new Intent(ManualBloodPressure.this, MainActivity.class);
            startActivity(intent);

        }else{
            textViewError.setVisibility(View.VISIBLE);
        }
    }
}
