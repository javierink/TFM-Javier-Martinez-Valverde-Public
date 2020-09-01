package com.example.healthcarehighbloodpressure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Activity class for the select of diferent test class. Not visible in normal apks.
 */
public class Tests_select extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests_select);
    }

    public void openGeo(View v) {
        Intent intent = new Intent(Tests_select.this, Geo_test.class);
        startActivity(intent);
    }

    public void openGeneral(View v) {
        Intent intent = new Intent(Tests_select.this, GeneralTests.class);
        startActivity(intent);
    }

    public void exitToMain(View v) {
        Intent intent = new Intent(Tests_select.this, MainActivity.class);
        startActivity(intent);
    }
}
